package OurShoppingListDataBase;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Vector;

import OurShoppingListObjs.Category;
import OurShoppingListObjs.OurShoppingListObj;

/**
 * Created by serguei on 28/04/17.
 */

class OurTableCategories extends OurTable {
    final static String TAG = "OurTableCategories";

    public OurTableCategories(OurDBStore db) {
        super(db);
        Log.d(TAG, "construtor");
    }

    public String getTableName() {
        return "Categories";
    }

    /**** table generation ****/

    @Override
    protected void createTable(SQLiteDatabase sqlite) {
        Log.d(TAG, "createCategoriesTable()");
        String creator =
                "CREATE TABLE IF NOT EXISTS `"+getTableName()+"` (" +
                        "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "`name` TEXT NOT NULL" +
                        ");";
        sqlite.execSQL(creator);
    }

    /**** Table manipulation methods ****/

    @Override
    protected Integer insert(OurShoppingListObj obj) {
        Category  category = (Category) obj;

        ContentValues values = new ContentValues();

        Log.d(TAG, "insert("+category.getName()+")");
        if ( db.getIdFromName("Categories", category.getName()) == -1 ) { //Doesn't exist
            SQLiteDatabase sqlite = db.getWritableDatabase();
            values.put("name", category.getName());
            sqlite.insert("Categories", null, values);
            sqlite.close();
            return db.getIdFromName("Categories", obj.getName());
        } else {
            Log.w(TAG, "Insert failed, "+obj.getName()+"already exist");
            return -1;
        }
    }

    @Override
    protected boolean modify(OurShoppingListObj obj) {
        Category  category = (Category) obj;
        Integer id = category.getId();

        String table = "Categories";
        ContentValues values = new ContentValues();
        String where = "id = ?";
        String[] whereArgs = new String[] {id.toString()};

        values.put("name", category.getName());

        Log.d(TAG, "modify("+id+")");
        if ( getCategoryObj(id) == null ) {
            return false;
        }
        SQLiteDatabase sqlite = db.getWritableDatabase();
        sqlite.update(table, values, where, whereArgs);
        sqlite.close();
        return true;
    }

    @Override
    protected boolean remove(Integer id) {
        String table = "Categories";
        String where = "id = ?";
        String[] whereArgs = new String[] {id.toString()};

        Log.d(TAG, "remove("+id+")");
        Category category = getCategoryObj(id);
        if ( category == null ) {
            return false;
        }
        // fixme: what to do with products that has this category
        Vector<String> productsInCategory = db.getProductsTable().getProductsInCategory(id);
        for (String name : productsInCategory) {
            db.getProductsTable().getProductObj(name).setCategoryId(-1);
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
        returnCode = sqlite.delete("Categories", null, null);
        createTable(sqlite);
        sqlite.close();
        return returnCode > 0;
    }

    /**** request/action methods ****/

    public Category getCategoryObj(Integer id) {
        Log.d(TAG, "getCategoryObj("+id+")");
        return (Category)db.builderById("Categories", id);
    }

    public Category getCategoryObj(String name) {
        Log.d(TAG, "getCategoryObj("+name+")");
        return (Category)db.builderByName("Categories", name);
    }

    public Vector<String> getCategoryNames() {
        Log.d(TAG, "getCategoryNames()");
        return db.getAllInTable("Categories");
    }

    protected boolean isCategoryInDB(String name) {
        return db.isNameInTable("Categories", name);
    }

    protected Integer getCategoryId(String name) {
        return db.getIdFromName("Categories", name);
    }

    protected String getCategoryName(Integer id) {
        return db.getNameFromId("Categories", id);
    }
}
