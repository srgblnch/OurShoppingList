package OurShoppingListDataBase;

import android.util.Log;

import java.io.File;


/**
 * Created by serguei on 22/03/17.
 */

class OurCSVGenerator {
    final static String TAG = "OurCSVGenerator";
    OurDBStore ourDB = null;

    public OurCSVGenerator(OurDBStore db) {
        ourDB = db;
        Log.d(TAG, "construtor");
    }

    protected boolean doExport(File directory, String fileName) {
        Log.d(TAG, "doExport(" + directory.getAbsolutePath() + ", " + fileName + ")");
        return new OurCSVExporter(ourDB).doExport(directory, fileName);
    }

    protected boolean doImport(File file) {
        Log.d(TAG, "doImport(" + file.getAbsolutePath() + ", " + file.getName() + ")");
        return new OurCSVImporter(ourDB).doImport(file);
    }
}
