package OurShoppingListObjs;

import java.io.File;

import OurShoppingListDataBase.DatabaseSingleton;

/**
 * Created by serguei on 24/03/17.
 */

public class ImportExport {
    final static String TAG = "ImportExport";

    public ImportExport() {

    }

    public boolean exportDB2CSV(File directory, String fileName) {
        DatabaseSingleton db = DatabaseSingleton.getInstance();
        return db.exportDB2CSV(directory, fileName);
    }

    public boolean importDB2CSV(File file) {
        DatabaseSingleton db = DatabaseSingleton.getInstance();
        return db.importDB2CSV(file);
    }
}

