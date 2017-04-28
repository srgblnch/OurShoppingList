package OurShoppingListDataBase;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Vector;

import OurShoppingListObjs.OurShoppingListObj;
import OurShoppingListObjs.Shop;

/**
 * Created by serguei on 28/04/17.
 */

class OurTableShops extends OurTable {
    final static String TAG = "OurTableCategories";

    public OurTableShops(OurDBStore db) {
        super(db);
        Log.d(TAG, "construtor");
    }

    /**** table generation ****/

    @Override
    protected void createTable(SQLiteDatabase sqlite) {
        Log.d(TAG, "createShopsTable()");
        String creator =
                "CREATE TABLE `Shops` (" +
                        "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "`name` TEXT NOT NULL" +
                        ");";
        sqlite.execSQL(creator);
    }

    /**** Table manipulation methods ****/

    @Override
    protected Integer insert(OurShoppingListObj obj) {
        Shop shop = (Shop) obj;
        Log.d(TAG, "insert("+shop.getName()+")");
        if ( db.getIdFromName("Shops", shop.getName()) == -1 ) { //Doesn't exist
            String insert = "INSERT INTO Shops VALUES ( null, '"+shop.getName()+"')";
            SQLiteDatabase sqlite = db.getWritableDatabase();
            sqlite.execSQL(insert);
            Log.d(TAG, "Insert: "+insert);
            return db.getIdFromName("Shops", shop.getName());
        } else {
            Log.w(TAG, "Insert failed, "+shop.getName()+"already exist");
            return -1;
        }
    }

    @Override
    protected boolean modify(OurShoppingListObj obj) {
        Shop shop = (Shop) obj;
        Integer id = shop.getId();
        Log.d(TAG, "modify("+id+")");
        if ( getShopObj(id) == null ) {
            return false;
        }
        String modification = "UPDATE Shops SET "+
                "name = '"+shop.getName()+"' "+
                "WHERE id = "+id;
        Log.d(TAG, modification);
        SQLiteDatabase sqlite = db.getWritableDatabase();
        sqlite.execSQL(modification);
        return true;
    }

    @Override
    protected boolean remove(Integer id) {
        Log.d(TAG, "remove("+id+")");
        Shop shop = getShopObj(id);
        if (shop == null) {
            return false;
        }
        Vector<String> productsInShop = db.getProductShopTable().getShopProducts(shop);
        for (String name : productsInShop) {
            db.getProductsTable().getProductObj(name).unassignShop(shop);
        }
        String deletion = "DELETE FROM Shops WHERE id = " + id;
        Log.d(TAG, deletion);
        SQLiteDatabase sqlite = db.getWritableDatabase();
        sqlite.execSQL(deletion);
        return true;
    }

    /**** request/action methods ****/

    public Shop getShopObj(Integer id) {
        Log.d(TAG, "getShopObj("+id+")");
        return (Shop)db.builderById("Shops", id);
    }

    public Shop getShopObj(String name) {
        Log.d(TAG, "getShopObj("+name+")");
        return (Shop)db.builderByName("Shops", name);
    }

    public Vector<String> getShopNames() {
        Log.d(TAG, "getShopNames()");
        return db.getAllInTable("Shops");
    }

    protected boolean isShopInDB(String name) {
        return db.isNameInTable("Shops", name);
    }

    protected Integer getShopId(String name) {
        return db.getIdFromName("Shops", name);
    }

    protected String getShopName(Integer id) {
        return db.getNameFromId("Shops", id);
    }
}
