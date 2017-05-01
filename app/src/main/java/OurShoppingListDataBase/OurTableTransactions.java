package OurShoppingListDataBase;

import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import OurShoppingListObjs.OurShoppingListObj;
import OurShoppingListObjs.Product;
import OurShoppingListObjs.Category;
import OurShoppingListObjs.Shop;

/**
 * Created by serguei on 30/04/17.
 */

class OurTableTransactions extends OurTable {
    final static String TAG = "OurTableTransactions";

    public OurTableTransactions(OurDBStore db) {
        super(db);
        Log.d(TAG, "construtor");
    }

    /**** table generation ****/

    @Override
    protected void createTable(SQLiteDatabase sqlite) {
        Log.d(TAG, "createTransactionsTable()");
        String creator =
                "CREATE TABLE `Transactions` (" +
                        "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        //when
                        "`tableName` TEXT NOT NULL" +
                        "`transaction` TEXT NOT NULL" +
                        ");";
        sqlite.execSQL(creator);
    }

    /**** Table manipulation methods ****/

    @Override
    protected Integer insert(OurShoppingListObj obj) {
        if ( obj instanceof Product ) {

        } else if ( obj instanceof Category ) {

        } else if ( obj instanceof Shop) {

        }
        return -1;
    }

    @Override
    protected boolean modify(OurShoppingListObj obj) {
        // it doesn't have sense to modify a transaction
        return false;
    }

    @Override
    protected boolean remove(Integer id) {
        // fixme: Has any sense to remove a transaction?
        return false;
    }
}
