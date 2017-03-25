package OurShoppingListObjs;

import OurShoppingListDataBase.DatabaseSingleton;

/**
 * Created by serguei on 24/03/17.
 */

public class ImportExport {
    final static String TAG = "ImportExport";

    public ImportExport() {

    }

    public boolean exportDB2CSV(String directory, String fileName) {
        DatabaseSingleton db = DatabaseSingleton.getInstance();
        return db.exportDB2CSV(directory, fileName);
    }

    public boolean importDB2CSV(String directory, String fileName) {
        DatabaseSingleton db = DatabaseSingleton.getInstance();
        return db.importDB2CSV(directory, fileName);
    }
}

