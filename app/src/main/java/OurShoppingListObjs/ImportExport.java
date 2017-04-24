package OurShoppingListObjs;

import java.io.File;

import OurShoppingListDataBase.OurData;

/**
 * Created by serguei on 24/03/17.
 */

public class ImportExport {
    final static String TAG = "ImportExport";

    public ImportExport() {

    }

    public boolean exportDB2CSV(File directory, String fileName) {
        OurData db = OurData.getInstance();
        return db.exportDB2CSV(directory, fileName);
    }

    public boolean importDB2CSV(File file) {
        OurData db = OurData.getInstance();
        return db.importDB2CSV(file);
    }
}

