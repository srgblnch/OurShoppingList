package OurShoppingListDataBase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by serguei on 22/03/17.
 */

public class OurShoppingListCSV {
    final static String TAG = "OurShoppingListCSV";
    OurShoppingListDB ourDB = null;

    public OurShoppingListCSV(OurShoppingListDB db) {
        ourDB = db;
        Log.d(TAG, "construtor");
    }

    protected boolean exportDB2CSV(String directory, String fileName) {
        Log.d(TAG, "exportDB2CSV("+directory+", "+fileName+")");

        Boolean returnCode = true;
        SQLiteDatabase db = ourDB.getReadableDatabase();
        BufferedWriter out = null;
        ArrayMap<String, Cursor> pairs = new ArrayMap<String, Cursor>();

        Cursor products = null;
        Cursor categories = null;
        Cursor shops = null;

        pairs.put("Products", products);
        pairs.put("Categories", categories);
        pairs.put("Shops", shops);

        if ( !launchQueries(db, pairs)) {
            Log.w(TAG, "In exportDB2CSV(): Impossible to complete the export");
            returnCode = false;
        } else {
            returnCode = prepareFile(directory, fileName, out);
            if ( returnCode == false) {
                Log.e(TAG, "In exportDB2CSV(): file " + fileName + " writer NOT build");
            } else {
                Log.d(TAG, "In exportDB2CSV(): file " + fileName + " writer build");
                Cursor[] cursors = new Cursor[]{products, categories, shops}
                returnCode = prepareHeader(cursors, out);
                if ( returnCode == false) {
                    Log.e(TAG, "In exportDB2CSV(): header NOT prepared");
                } else {
                    Log.e(TAG, "In exportDB2CSV(): header prepared");
                    returnCode = populateCSV(cursors, out);
                    if ( returnCode == false) {
                        Log.e(TAG, "In exportDB2CSV(): csv NOT populated");
                    } else {
                        Log.e(TAG, "In exportDB2CSV(): csv populated");
                    }
                }
            }
        }
        db.close();
        products.close();
        categories.close();
        shops.close();
        return returnCode;
    }

    /* TODO */
    protected boolean importDB2CSV(String directory, String fileName) {
        Log.d(TAG, "importDB2CSV(" + directory + ", " + fileName + ")");
        return false;
    }

    /************************************** Intenal methods  **************************************/
    private boolean launchQueries(SQLiteDatabase db, ArrayMap tables) {
        Iterator it = tables.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            if ( !doQuery(db, (String)pair.getKey(), (Cursor)pair.getValue())) {
                return false;
            }
            //it.remove(); // FIXME: ??
            // from http://stackoverflow.com/questions/1066589/iterate-through-a-hashmap
            // it says: "avoids a ConcurrentModificationException"
        }
        return true;
    }

    private boolean doQuery(SQLiteDatabase db, String tableName, Cursor cursor) {
        String query = "SELECT * FROM "+tableName;
        Cursor cursor = db.rawQuery(query, null);
        if (cursor == null) {
            Log.w(TAG, "In exportDB2CSV(): no registers return from '" + query +"' query");
            return false;
        }
        return true;
    }

    private boolean prepareFile(String directory, String fileName, BufferedWriter out) {
        try {
            File file = new File(directory, fileName);
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            out = new BufferedWriter(writer);
        } catch (IOException e) {
            Log.d(TAG, "In prepareFile(): IOException: " + e.getMessage());
            return false;
        }
        return true;
    }

    private boolean prepareHeader(Cursor[] cursors, BufferedWriter out) {
        String header = "";
        for (Cursor cursor: cursors) {
            String[] columnNames = cursor.getColumnNames();
            Log.d(TAG, "In prepareHeader(): query column names are "+columnNames);
            try{
                for (int i=0; i<columnNames.length; i++) {
                    header += columnNames[i];
                    if (i != columnNames.length-1) {
                        header += "\t";
                    }
                }
                out.write(header);
            } catch (IOException e) {
                Log.d(TAG, "In prepareHeader(): IOException: " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    private boolean populateCSV(Cursor[] cursors, BufferedWriter out) {
        String line = "";
        try{
            line = "";
            for (Cursor cursor: cursors) {
                if (cursor.moveToNext()) {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        line += cursor.getString(i) + "\t";
                    }
                } else { // if (cursor.isAfterLast()) {
                    for (int i = 0; i < cursor.getColumnCount(); i++) {
                        line += "\t";
                    }
                }
            }
            out.write(line);
        } catch (IOException e) {
            Log.d(TAG, "In populateCSV(): IOException: " + e.getMessage());
            return false;
        }
        return true;
    }
}
