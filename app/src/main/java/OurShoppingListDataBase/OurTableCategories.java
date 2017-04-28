package OurShoppingListDataBase;

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

    /**** table generation ****/

    @Override
    protected void createTable(SQLiteDatabase sqlite) {
        Log.d(TAG, "createCategoriesTable()");
        String creator =
                "CREATE TABLE `Categories` (" +
                        "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "`name` TEXT NOT NULL" +
                        ");";
        sqlite.execSQL(creator);
    }

    /**** Table manipulation methods ****/

    @Override
    protected Integer insert(OurShoppingListObj obj) {
        Category  category = (Category) obj;
        Log.d(TAG, "insertCategoryObj("+category.getName()+")");
        if ( db.getIdFromName("Categories", category.getName()) == -1 ) { //Doesn't exist
            String insert = "INSERT INTO Categories VALUES ( null, '"+category.getName()+"')";
            SQLiteDatabase sqlite = db.getWritableDatabase();
            sqlite.execSQL(insert);
            Log.d(TAG, "Insert: "+insert);
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
        Log.d(TAG, "modify("+id+")");
        if ( getCategoryObj(id) == null ) {
            return false;
        }
        String modification = "UPDATE Categories SET "+
                "name = '"+category.getName()+"' "+
                "WHERE id = "+id;
        Log.d(TAG, modification);
        SQLiteDatabase sqlite = db.getWritableDatabase();
        sqlite.execSQL(modification);
        return true;
    }

    @Override
    protected boolean remove(Integer id) {
        Log.d(TAG, "remove("+id+")");
        if ( getCategoryObj(id) == null ) {
            return false;
        }
        String deletion = "DELETE FROM Categories WHERE id = " + id;
        SQLiteDatabase sqlite = db.getWritableDatabase();
        sqlite.rawQuery(deletion, null);
        return true;
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
