package OurShoppingListDataBase;

import android.util.Log;

import java.io.File;


/**
 * Created by serguei on 22/03/17.
 */

class OurPorterCSV extends OurPorter {
    final static String TAG = "OurPorterCSV";
    private File destination;
    private String fileName;


    public OurPorterCSV(OurDBStore db) {
        super(db);
        Log.d(TAG, "construtor");
    }



    @Override
    protected boolean doExport() {
        Log.d(TAG, "doExport(" + destination.getAbsolutePath() + ", " + fileName + ")");
        return new OurCSVExporter(ourDB).doExport(destination, fileName);
    }

    @Override
    protected boolean doImport() {
        Log.d(TAG, "doImport(" + destination.getAbsolutePath() + ", " + destination.getName() + ")");
        return new OurCSVImporter(ourDB).doImport(destination);
    }

    public File getDestination() {
        return destination;
    }

    public void setDestination(File destination) {
        this.destination = destination;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
