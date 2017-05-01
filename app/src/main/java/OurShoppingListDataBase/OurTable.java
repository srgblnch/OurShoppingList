package OurShoppingListDataBase;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import OurShoppingListObjs.OurShoppingListObj;

/**
 * Created by serguei on 28/04/17.
 */

abstract class OurTable {
    final static String TAG = "OurTable";
    protected OurDBStore db = null;

    public OurTable(OurDBStore db) {
        Log.d(TAG, "construtor");
        this.db = db;
    }
    /**** table generation ****/
    abstract protected void createTable(SQLiteDatabase sqlite);
    /**** Table manipulation methods ****/
    abstract protected Integer insert(OurShoppingListObj obj);
    abstract protected boolean modify(OurShoppingListObj obj);
    abstract protected boolean remove(Integer id);
}
