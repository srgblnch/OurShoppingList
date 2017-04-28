package OurShoppingListDataBase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Vector;

import OurShoppingListObjs.OurShoppingListObj;
import OurShoppingListObjs.Product;
import OurShoppingListObjs.Shop;

/**
 * Created by serguei on 28/04/17.
 */

class OurTableRelationProductShop extends OurTableRelation {
    final static String TAG = "OurTableProductShop";

    public OurTableRelationProductShop(OurDBStore db) {
        super(db);
        Log.d(TAG, "construtor");
    }

    /**** table generation ****/

    protected void createTable(SQLiteDatabase sqlite) {
        Log.d(TAG, "createRelationProductsWithshops()");
        String creator =
                "CREATE TABLE `Products_has_Shops` (" +
                        "`Product`INTEGER NOT NULL," +
                        "`Shop`INTEGER NOT NULL," +
                        "`position` INTEGER," +
                        "PRIMARY KEY(Product,Shop)" +
                        ");";
        sqlite.execSQL(creator);
    }

    /**** Table manipulation methods ****/

    @Override
    protected Integer insert(OurShoppingListObj obj1, OurShoppingListObj obj2, Integer position) {
        Product product = (Product)obj1;
        Shop shop = (Shop)obj2;
        Log.d(TAG, "insert("+product.getName()+", "+shop.getName()+")");

        if ( !isProductInShop(product, shop)) {
            String insert = "INSERT INTO Products_has_Shops VALUES ( "+product.getId()+", "+shop.getId()+", "+position+" )";
            SQLiteDatabase sqlite = db.getWritableDatabase();
            sqlite.execSQL(insert);
            Log.d(TAG, "Insert: "+insert);
            db.close();
            return 1;
        } else {
            Log.w(TAG, "Insert failed, pair ("+product.getId()+","+shop.getId()+") already exist");
            return 0;
        }
    }

    @Override
    protected boolean modify(OurShoppingListObj obj1, OurShoppingListObj obj2, Integer position) {
        Product product = (Product)obj1;
        Shop shop = (Shop)obj2;
        Log.d(TAG, "modify("+product.getName()+", "+shop.getName()+", "+position+")");

        Integer oldPosition = productPositionInShop(product, shop);
        if ( oldPosition >= 0 ) {
            if ( oldPosition != position ) {
                String modification = "UPDATE Products_has_Shops SET "+
                        "position = "+position+" "+
                        "WHERE Product == "+product.getId()+" AND Shop == "+shop.getId();
                Log.d(TAG, modification);
                SQLiteDatabase sqlite = db.getWritableDatabase();
                sqlite.execSQL(modification);
                sqlite.close();
                return true;
            } else {
                Log.w(TAG, "position hasn't change");
            }
        } else {
            Log.w(TAG, "Update failed, pair ("+product.getId()+","+shop.getId()+") doesn't exist");
        }
        return false;
    }

    @Override
    protected boolean remove(OurShoppingListObj obj1, OurShoppingListObj obj2) {
        Product product = (Product)obj1;
        Shop shop = (Shop)obj2;
        Log.d(TAG, "removeProductInShop("+product.getName()+", "+shop.getName()+")");
        String query = "SELECT Product, Shop FROM Products_has_Shops WHERE Product == "+product.getId()+" AND Shop == "+shop.getId();
        Integer counter;

        SQLiteDatabase sqlite = db.getReadableDatabase();
        Log.w(TAG, "Query '" + query + "' is going to be launched");
        Cursor cursor = sqlite.rawQuery(query, null);
        Log.d(TAG, cursor.getCount()+" 'Products_has_Shops' with shop '"+shop.getName()+"' located in the database");
        counter = cursor.getCount();
        cursor.close();
        sqlite.close();

        if ( counter == 1) {
            String deletion = "DELETE FROM Products_has_Shops WHERE Product == "+product.getId()+" AND Shop == "+shop.getId();
            sqlite = db.getWritableDatabase();
            sqlite.execSQL(deletion);
            Log.d(TAG, "Delete: "+deletion);
            sqlite.close();
            return true;
        } else {
            Log.w(TAG, "Delete failed, pair ("+product.getId()+","+shop.getId()+") doesn't exist");
            return false;
        }
    }

    /**** request/action methods ****/

    protected Vector<String> getShopProducts(Shop shop) {
        Log.d(TAG, "getShopProducts("+shop.getName()+")");
        String query = "SELECT Product FROM Products_has_Shops WHERE Shop == "+shop.getId()+" ORDER BY position";
        Integer productId;
        Vector<String> productNames = new Vector<String>();

        SQLiteDatabase sqlite = db.getReadableDatabase();
        Log.w(TAG, "Query '" + query + "' is going to be launched");
        Cursor cursor = sqlite.rawQuery(query, null);
        Log.d(TAG, cursor.getCount()+" 'Products_has_Shops' with shop '"+shop.getName()+"' located in the database");
        while ( cursor.moveToNext() ) {
            productId = cursor.getInt(0);
            productNames.add( db.getProductsTable().getProductName(productId));
        }
        cursor.close();
        db.close();
        return productNames;
    }

    protected boolean isProductInShop(Product product, Shop shop) {
        return isProductInShop(product.getName(), product.getId(), shop.getName(), shop.getId());
    }

    protected boolean isProductInShop(String productName, int productId, String shopName, int shopId) {
        Log.d(TAG, "isProductInShop("+productName+","+shopName+")");
        String query = "SELECT Product, Shop FROM Products_has_Shops WHERE Product == "+productId+" AND Shop == "+shopId;
        Integer counter;

        SQLiteDatabase sqlite = db.getReadableDatabase();
        Log.w(TAG, "Query '" + query + "' is going to be launched");
        Cursor cursor = sqlite.rawQuery(query, null);
        Log.d(TAG, cursor.getCount()+" 'Products_has_Shops' with shop '"+shopName+"' located in the database");
        counter = cursor.getCount();
        cursor.close();
        sqlite.close();

        return counter > 0;
    }

    protected Integer productPositionInShop(Product product, Shop shop) {
        return productPositionInShop(product.getName(), product.getId(), shop.getName(), shop.getId());
    }

    protected Integer productPositionInShop(String productName, int productId, String shopName, int shopId) {
        Log.d(TAG, "productPositionInShop("+productName+", "+shopName+")");
        Integer position = -1;

        if ( isProductInShop(productName, productId, shopName, shopId)) {
            String query = "SELECT position FROM Products_has_Shops WHERE Product == "+productId+" AND Shop == "+shopId;
            SQLiteDatabase sqlite = db.getReadableDatabase();
            Log.w(TAG, "Query '" + query + "' is going to be launched");
            Cursor cursor = sqlite.rawQuery(query, null);
            if ( cursor.moveToFirst() ) {
                position = cursor.getInt(0);
            }
            cursor.close();
            sqlite.close();
        }
        return position;
    }
}
