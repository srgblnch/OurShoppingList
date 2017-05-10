package OurShoppingListDataBase;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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

    public String getTableName() {
        return "Products";
    }

    /**** table generation ****/

    @Override
    protected void createTable(SQLiteDatabase sqlite) {
        Log.d(TAG, "createProductsTable()");
        String creator =
                "CREATE TABLE IF NOT EXISTS `"+getTableName()+"` (" +
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

        ContentValues values = new ContentValues();

        Log.d(TAG, "insert("+product.getName()+")");
        if ( db.getIdFromName("Products", obj.getName()) == -1) {  // if doesn't exist
            int buy;
            if ( product.getBuy() ) { buy = 1; } else { buy = 0; }
            SQLiteDatabase sqlite = this.db.getWritableDatabase();
            values.put("name", product.getName());
            values.put("buy", product.getBuy());
            values.put("category", product.getCategoryId());
            values.put("howmany", product.getHowMany());
            sqlite.insert("Products", null, values);
            sqlite.close();
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
        Integer buy;
        if ( product.getBuy() ) { buy = 1; } else { buy = 0; }

        String table = "Products";
        ContentValues values = new ContentValues();
        String where = "id = ?";
        String[] whereArgs = new String[] {id.toString()};

        values.put("name", product.getName());
        values.put("buy", buy);
        values.put("category", product.getCategoryId());
        values.put("howmany", product.getHowMany());

        Log.d(TAG, "modify("+id+")");
        if ( getProductObj(id) == null ) {
            return false;
        }
        SQLiteDatabase sqlite = db.getWritableDatabase();
        sqlite.update(table, values, where, whereArgs);
        sqlite.close();
        return true;
    }

    @Override
    protected boolean remove(Integer id) {
        String table = "Products";
        String where = "id = ?";
        String[] whereArgs = new String[] {id.toString()};

        Log.d(TAG, "remove("+id+")");
        Product product = getProductObj(id);
        if ( product == null ) {
            return false;
        }
        SQLiteDatabase sqlite = db.getWritableDatabase();
        sqlite.delete(table, where, whereArgs);
        sqlite.close();
        return true;
    }

    @Override
    protected boolean drop() {
        Integer returnCode;
        SQLiteDatabase sqlite = db.getWritableDatabase();
        returnCode = sqlite.delete("Products", null, null);
        createTable(sqlite);
        sqlite.close();
        return returnCode > 0;
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

    protected Vector<String> getProductsInCategory(Integer categoryId) {
        String name;
        Vector<String> productNames = new Vector<String>();

        String from = "Products";
        String[] select = new String[] {"name"};
        String where = "category = ?";
        String[] whereArgs = new String[] {categoryId.toString()};

        SQLiteDatabase sqlite = db.getReadableDatabase();
        Cursor cursor = sqlite.query(from, select, where, whereArgs, null, null, null);
        while ( cursor.moveToNext() ) {
            name = cursor.getString(0);
            productNames.add(name);
        }
        cursor.close();
        db.close();
        return productNames;
    }
}
