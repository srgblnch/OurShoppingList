package OurShoppingListDataBase;

import android.content.ContentValues;
import android.content.Intent;
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

    public String getTableName() {
        return "Products_has_Shops";
    }

    /**** table generation ****/

    protected void createTable(SQLiteDatabase sqlite) {
        Log.d(TAG, "createRelationProductsWithshops()");
        String creator =
                "CREATE TABLE IF NOT EXISTS `"+getTableName()+"` (" +
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

        ContentValues values = new ContentValues();

        Log.d(TAG, "insert("+product.getName()+", "+shop.getName()+")");

        if ( !isProductInShop(product, shop)) {
            SQLiteDatabase sqlite = db.getWritableDatabase();
            values.put("Product", product.getId());
            values.put("Shop", shop.getId());
            values.put("position", position);
            sqlite.insert("Products_has_Shops", null, values);
            sqlite.close();
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

        String table = "Products_has_Shops";
        ContentValues values = new ContentValues();
        String where = "Product = ? AND Shop = ?";
        String[] whereArgs = new String[] {product.getId().toString(), shop.getId().toString()};

        values.put("position", position);

        Log.d(TAG, "modify("+product.getName()+", "+shop.getName()+", "+position+")");
        Integer oldPosition = productPositionInShop(product, shop);
        if ( oldPosition >= 0 ) {
            if ( oldPosition != position ) {
                SQLiteDatabase sqlite = db.getWritableDatabase();
                sqlite.update(table, values, where, whereArgs);
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

        String table = "Products_has_Shops";
        String where = "Product = ? AND Shop = ?";
        String[] whereArgs = new String[] {product.getId().toString(), shop.getId().toString()};

        Log.d(TAG, "remove("+product.getName()+", "+shop.getName()+")");
        Integer counter;

        SQLiteDatabase sqlite = db.getReadableDatabase();
        sqlite.delete(table, where, whereArgs);
        sqlite.close();
        return true;
    }

    @Override
    protected boolean drop() {
        Integer returnCode;
        SQLiteDatabase sqlite = db.getWritableDatabase();
        returnCode = sqlite.delete("Products_has_Shops", null, null);
        createTable(sqlite);
        sqlite.close();
        return returnCode > 0;
    }

    /**** request/action methods ****/

    protected Vector<String> getShopProducts(Shop shop) {
        Integer productId;
        Vector<String> productNames = new Vector<String>();

        String from = "Products_has_Shops";
        String[] select = new String[] {"Product"};
        String where = "Shop = ?";
        String[] whereArgs = new String[] {shop.getId().toString()};
        String orderBy = "position";

        Log.d(TAG, "getShopProducts("+shop.getName()+")");
        SQLiteDatabase sqlite = db.getReadableDatabase();
        Cursor cursor = sqlite.query(from, select, where, whereArgs, null, null, orderBy);
        Log.d(TAG, cursor.getCount()+" 'Products_has_Shops' with shop '"+shop.getName()+"' located in the database");
        while ( cursor.moveToNext() ) {
            productId = cursor.getInt(0);
            productNames.add( db.getProductsTable().getProductName(productId));
        }
        cursor.close();
        sqlite.close();
        return productNames;
    }

    protected boolean isProductInShop(Product product, Shop shop) {
        return isProductInShop(product.getName(), product.getId(), shop.getName(), shop.getId());
    }

    protected boolean isProductInShop(String productName, Integer productId, String shopName, Integer shopId) {
        Integer counter;

        String from = "Products_has_Shops";
        String[] select = new String[] {"Product", "Shop"};
        String where = "Product = ? AND Shop = ?";
        String[] whereArgs = new String[] {productId.toString(), shopId.toString()};

        Log.d(TAG, "isProductInShop("+productName+","+shopName+")");
        SQLiteDatabase sqlite = db.getReadableDatabase();
        Cursor cursor = sqlite.query(from, select, where, whereArgs, null, null, null);
        Log.d(TAG, cursor.getCount()+" 'Products_has_Shops' with shop '"+shopName+"' located in the database");
        counter = cursor.getCount();
        cursor.close();
        sqlite.close();
        return counter > 0;
    }

    protected Integer productPositionInShop(Product product, Shop shop) {
        return productPositionInShop(product.getName(), product.getId(), shop.getName(), shop.getId());
    }

    protected Integer productPositionInShop(String productName, Integer productId, String shopName, Integer shopId) {
        Integer position = -1;

        String from = "Products_has_Shops";
        String[] select = new String[] {"position"};
        String where = "Product = ? AND Shop = ?";
        String[] whereArgs = new String[] {productId.toString(), shopId.toString()};

        Log.d(TAG, "productPositionInShop("+productName+", "+shopName+")");
        if ( isProductInShop(productName, productId, shopName, shopId)) {
            SQLiteDatabase sqlite = db.getReadableDatabase();
            Cursor cursor = sqlite.query(from, select, where, whereArgs, null, null, null);
            if ( cursor.moveToFirst() ) {
                position = cursor.getInt(0);
            }
            cursor.close();
            sqlite.close();
        }
        return position;
    }
}
