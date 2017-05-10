package OurShoppingListObjs;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.io.File;


import OurShoppingListDataBase.OurData;

/**
 * Created by serguei on 24/03/17.
 */

//public class ImportExport<Boolean> extends AsyncTaskLoader<Boolean> {
public class ImportExport {
    private String TAG = new String("ImportExport");
    //private enum Action {IMPORT, EXPORT};
    //Action action;
    //File directory, file;
    //String fileName;

    public ImportExport(Context context) {
        //super(context);
    }

//    @Override
//    protected void onStartLoading() {
//
//    }

//    @Override
//    public Boolean loadInBackground() {
//        Boolean result;
//        OurData db = OurData.getInstance();
//        if ( action == Action.IMPORT ) {
//            //return Boolean.valueOf(db.doImport("csv", file));
//        } else if ( action == Action.EXPORT ) {
//            //result = Boolean.valueOf(db.doExport("csv", directory, fileName));
//        }
//        return result;
//    }

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
//        action = Action.EXPORT;
//        this.directory = directory;
//        this.fileName = fileName;
        OurData db = OurData.getInstance();
        return db.doExport("csv", directory, fileName);
//        return loadInBackground();
    }

    public boolean importDB2CSV(File file) {
//        action = Action.IMPORT;
//        this.file = file;
        OurData db = OurData.getInstance();
        return db.doImport("csv", file);
//        return loadInBackground();
    }
}

