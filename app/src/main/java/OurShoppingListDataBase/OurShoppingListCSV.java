package OurShoppingListDataBase;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.ArrayMap;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

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

    protected boolean exportDB2CSV(File directory, String fileName) {
        Log.d(TAG, "exportDB2CSV(" + directory.getAbsolutePath() + ", " + fileName + ")");

        Boolean returnCode = true;
        Vector<String> productFields = ourDB.getProductFields();
        Vector<String> productLinks = new Vector<String>(Arrays.asList("category", "shop"));
        BufferedWriter out = null;

        if ( prepareFile(directory, fileName, out) ) {
            if ( prepareHeader(out, productFields, productLinks) ) {
                if ( populateCSV(out, productFields, productLinks) ) {
                    Log.i(TAG, "In exportDB2CSV(): Well done!!");
                }
            }
        }
        return returnCode;
    }

    /* TODO */
    protected boolean importDB2CSV(File file) {
        Log.d(TAG, "importDB2CSV(" + file.getAbsolutePath() + ", " + file.getName() + ")");
        return false;
    }

    /************************************** Intenal methods  **************************************/
    private boolean prepareFile(File directory, String fileName, BufferedWriter out) {
        try {
            if ( ! fileName.endsWith(".csv")) {
                fileName += ".csv";
            }
            File file = new File(directory, fileName);
            if (file.createNewFile()) {
                Log.d(TAG, "In prepareFile(): created: "+file.getAbsolutePath());
                file.setWritable(true);
                FileWriter writer = new FileWriter(file.getAbsoluteFile());
                out = new BufferedWriter(writer);
            } else {
                throw new Exception("File "+fileName+" already exists!");
            }
        } catch (IOException e) {
            Log.d(TAG, "In prepareFile(): IOException: " + e.getMessage());
            return false;
        } catch (Exception e) {
            Log.d(TAG, "In prepareFile(): Exception: " + e.getMessage());
            return false;
        }
        return true;
    }

    private boolean prepareHeader(BufferedWriter out, Vector<String> productFields,
                                  Vector<String> productLinks) {
        String header = "# db version "+ourDB.getVersion()+"\n"
                +"# csv version 0\n"
                +"product\t";
        for (String fieldName: productFields) {
            header += fieldName+"\t";
        }
        for (String links: productLinks){
            header += links+"\t";
        }
//        try{
            header = header.substring(0, header.length() - 1)+"\n";  // replace the last \t by \n
//            out.write(header, 0, header.length());
//        } catch (IOException e) {
//            Log.d(TAG, "In prepareHeader(): IOException: " + e.getMessage());
//            return false;
//        }
        return true;
    }

    private boolean populateCSV(BufferedWriter out, Vector<String> productFields,
                                Vector<String> productLinks) {
        String line = "";

        for (String productName : ourDB.getProductNames()) {
            line = productName+"\t";
            for (String field : productFields) {
                line += ourDB.getProductField(productName, field);
                line += "\t";
            }
            for (String link : productLinks) {
                if ( link == "category" ) {
                    line += solveCategory(productName)+"\t";
                } else if ( link == "shop" ) {
                    line += solveShop(productName)+"\t";
                }
            }
//            try{
                line = line.substring(0, line.length() - 1)+"\n";  // replace the last \t by \n
//                out.write(line);
//            } catch (IOException e) {
//                Log.d(TAG, "In populateCSV(): in "+productName+" IOException: " + e.getMessage());
//                return false;
//            }
        }
        return true;
    }

    private String solveCategory(String productName) {
        int categoryId = Integer.parseInt(ourDB.getProductField(productName, "category"));
        return ourDB.getCategoryName(categoryId);
    }

    private String solveShop(String  productName) {
        String register = "";
        int productId = ourDB.getProductId(productName);
        Vector<String> shops = ourDB.getProductShops(productName);
        int shopId;
        int position;

        for (String shopName : shops) {
            shopId = ourDB.getShopId(shopName);
            position = ourDB.productPositionInShop(productName, productId, shopName, shopId);
            register += shopName+","+position+";";
        }
        return register;
    }
}
