package OurShoppingListDataBase;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import OurShoppingListObjs.OurShoppingListObj;

/**
 * Created by serguei on 28/04/17.
 */

abstract class OurTableRelation {
    final static String TAG = "OurTable";
    protected OurDBStore db = null;

    public OurTableRelation (OurDBStore db) {
        Log.d(TAG, "construtor");
        this.db = db;
    }
    /**** table generation ****/
    abstract protected void createTable(SQLiteDatabase db);
    /**** Table manipulation methods ****/
    abstract protected Integer insert(OurShoppingListObj obj1, OurShoppingListObj obj2, Integer position);
    abstract protected boolean modify(OurShoppingListObj obj1, OurShoppingListObj obj2, Integer position);
    abstract protected boolean remove(OurShoppingListObj obj1, OurShoppingListObj obj2);
}
