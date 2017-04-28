package OurShoppingListDataBase;

import android.util.Log;

/**
 * Created by serguei on 28/04/17.
 */

abstract class OurPorter {
    final static String TAG = "OurPorter";
    OurDBStore ourDB = null;

    public OurPorter(OurDBStore db) {
        ourDB = db;
        Log.d(TAG, "construtor");
    }

    abstract boolean doExport();
    abstract boolean doImport();
}
