package OurShoppingListObjs;

import android.content.AsyncTaskLoader;

import java.io.File;
import java.lang.ref.WeakReference;


import OurShoppingListDataBase.OurData;
import cat.calcurco.ourshoppinglist.ImportExportActivity;

/**
 * Created by serguei on 24/03/17.
 */

public class DataLoader extends AsyncTaskLoader<Boolean> {
    private String TAG = new String("DataLoader");

    private enum Action {IMPORT, EXPORT};
    Action action;
    File directory, file;
    String fileName;

    public static WeakReference<ImportExportActivity> activity;

    public DataLoader(ImportExportActivity activity) {
        super(activity);
        this.activity = new WeakReference<ImportExportActivity>(activity);
    }

//    @Override
//    protected void onStartLoading() {
//
//    }

    @Override
    public Boolean loadInBackground() {
        Boolean result;
        OurData db = OurData.getInstance();
        if ( action == Action.IMPORT ) {
            result = Boolean.valueOf(db.doImport("csv", file));
        } else if ( action == Action.EXPORT ) {
            result = Boolean.valueOf(db.doExport("csv", directory, fileName));
        } else {
            result = Boolean.FALSE;
        }
        return result;
    }

//    @Override
//    public void deliverResult(final Boolean data) {
//
//        super.deliverResult(data);
//    }

//    @Override
//    protected void onReset() {
//        super.onReset();
//
//    }

    public boolean exportDB2CSV(File directory, String fileName) {
        action = Action.EXPORT;
        this.directory = directory;
        this.fileName = fileName;
//        OurData db = OurData.getInstance();
//        return db.doExport("csv", directory, fileName);
        return true; //loadInBackground();
    }

    public boolean importDB2CSV(File file) {
        action = Action.IMPORT;
        this.file = file;
//        OurData db = OurData.getInstance();
//        return db.doImport("csv", file);
        return true; //loadInBackground();
    }
}

