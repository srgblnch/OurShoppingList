package OurShoppingListDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;
import java.util.Vector;

import OurShoppingListObjs.OurShoppingListObj;
import OurShoppingListObjs.Product;

/**
 * Created by serguei on 28/04/17.
 */

class OurTableProducts extends OurTable {
    final static String TAG = "OurTableProducts";

    public OurTableProducts(OurDBStore db) {
        super(db);
        Log.d(TAG, "construtor");
    }

    /**** table generation ****/

    @Override
    protected void createTable(SQLiteDatabase sqlite) {
        Log.d(TAG, "createProductsTable()");
        String creator =
                "CREATE TABLE `Products` (" +
                        "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "`name` TEXT NOT NULL," +
                        "`buy` NUMERIC NOT NULL," +
                        "`category` INTEGER," +
                        "`howmany` INTEGER" +
                        ");";
        sqlite.execSQL(creator);
    }

    /**** Table manipulation methods ****/

    @Override
    protected Integer insert(OurShoppingListObj obj) {
        Product product = (Product)obj;
        Log.d(TAG, "insert("+product.getName()+")");
        if ( db.getIdFromName("Products", obj.getName()) == -1) {  // if doesn't exist
            int buy;
            if ( product.getBuy() ) { buy = 1; } else { buy = 0; }
            /*String insert = "INSERT INTO Products VALUES ( null, '"+product.getName()+"', "+buy+", "
                    +product.getCategoryId()+", "+product.getHowMany()+")";*/
            String insert = "INSERT INTO Products VALUES ( null, '?', ?, ?, ?)";
            SQLiteDatabase sqlite = this.db.getWritableDatabase();
            sqlite.rawQuery(insert, new String[] {product.getName(), ""+buy, product.getCategoryId().toString(), product.getHowMany().toString()});
            Log.d(TAG, "Insert: "+insert);
            return db.getIdFromName("Products", obj.getName());
        } else {
            Log.w(TAG, "Insert failed, "+obj.getName()+"already exist");
            return -1;
        }
    }

    @Override
    protected boolean modify(OurShoppingListObj obj) {
        Product product = (Product)obj;
        Integer id = product.getId();
        Log.d(TAG, "modify("+id+")");
        if ( getProductObj(id) == null ) {
            return false;
        }
        int buy;
        if ( product.getBuy() ) { buy = 1; } else { buy = 0; }
        /*String modification = "UPDATE Products SET "+
                "name = '"+product.getName()+"', "+
                "buy = "+buy+", "+
                "category = "+product.getCategoryId()+", "+
                "howmany = "+product.getHowMany()+" "+
                "WHERE id = "+id;*/
        String modification = "UPDATE Products SET name = '?', buy = ?, category = ?, howmany = ? WHERE id = ?";
        Log.d(TAG, modification);
        SQLiteDatabase sqlite = db.getWritableDatabase();
        sqlite.rawQuery(modification, new String[] {product.getName(), ""+buy, product.getCategoryId().toString(), product.getHowMany().toString(), ""+id});
        //sqlite.update("Products", new ContentValues())
        return true;
    }

    @Override
    protected boolean remove(Integer id) {
        Log.d(TAG, "remove("+id+")");
        if ( getProductObj(id) == null ) {
            return false;
        }
        //String deletion = "DELETE FROM Products WHERE id = " + id;
        String deletion = "DELETE FROM Products WHERE id = ?";
        Log.d(TAG, deletion);
        SQLiteDatabase sqlite = db.getWritableDatabase();
        sqlite.rawQuery(deletion, new String[] {""+id});
        return true;
    }

    /**** request/action methods ****/

    protected Product getProductObj(Integer id) {
        Log.d(TAG, "getProductObj("+id+")");
        return (Product)db.builderById("Products", id);
    }

    protected Product getProductObj(String name) {
        Log.d(TAG, "getProductObj("+name+")");
        return (Product)db.builderByName("Products", name);
    }

    protected Vector<String> getProductFields() {
        return new Vector<String>(Arrays.asList("buy", "howmany"));
    }

    protected Vector<String> getProductNames() {
        Log.d(TAG, "getProductNames()");
        return db.getAllInTable("Products");
    }

    protected boolean isProductInDB(String name) {
        return db.isNameInTable("Products", name);
    }

    protected Integer getProductId(String name) {
        return db.getIdFromName("Products", name);
    }

    protected String getProductName(Integer id) {
        return db.getNameFromId("Products", id);
    }

    protected String getProductField(String name, String fieldName) {
        return db.getFieldFromName("Products", fieldName, name);
    }

    protected Vector<String> getProductShops(String productName){
        int productId = getProductId(productName);
        Vector<String> shopNames = new Vector<String>();
        int shopId;

        for (String shopName : db.getShopsTable().getShopNames()){
            shopId = db.getShopsTable().getShopId(shopName);
            if ( db.getProductShopTable().isProductInShop(productName, productId, shopName, shopId) ){
                shopNames.add(shopName);
            }
        }
        return shopNames;
    }
}
