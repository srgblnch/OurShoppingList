package OurShoppingListDataBase;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Vector;

/**
 * Created by serguei on 24/04/17.
 */

class OurCSVExporter {
    final static String TAG = "OurCSVExporter";
    OurDBStore ourDB = null;

    protected OurCSVExporter(OurDBStore db) {
        ourDB = db;
        Log.d(TAG, "construtor");
    }

    protected boolean doExport(File directory, String fileName) {
        Log.d(TAG, "doExport(" + directory.getAbsolutePath() + ", " + fileName + ")");

        boolean returnCode = true;
        Vector<String> productFields = ourDB.getProductsTable().getProductFields();
        Vector<String> productLinks = new Vector<String>(Arrays.asList("category", "shop"));
        OutputStreamWriter out = null;

        out = prepareFile(directory, fileName);
        if ( out == null) {
            returnCode = false;
        } else {
            returnCode = prepareHeader(out, productFields, productLinks);
            if ( returnCode ) {
                returnCode = populateCSV(out, productFields, productLinks);
                if ( returnCode ) {
                    Log.d(TAG, "In doExport(): Well done!!");
                }
            }
            try {
                out.close();
            } catch (IOException e) {
                Log.i(TAG, "In doExport(): Exception closing the file");
            }
        }
        return returnCode;
    }

    @Nullable
    private OutputStreamWriter prepareFile(File directory, String fileName) {
        OutputStreamWriter out = null;
        try {
            if ( ! fileName.endsWith(".csv")) {
                fileName += ".csv";
            }
            File file = new File(directory, fileName);
            if (file.createNewFile()) {
                // fixme: perhaps check in the action in the user like to overwrite a file
                Log.d(TAG, "In prepareFile(): created: "+file.getAbsolutePath());
                file.setWritable(true);
                FileOutputStream ostream = new FileOutputStream(file);
                out = new OutputStreamWriter(ostream);
            } else {
                throw new Exception("File "+fileName+" already exists!");
            }
        } catch (IOException e) {
            Log.d(TAG, "In prepareFile(): IOException: " + e.getMessage());
            return null;
        } catch (Exception e) {
            Log.d(TAG, "In prepareFile(): Exception: " + e.getMessage());
            return null;
        }
        return out;
    }

    private boolean prepareHeader(OutputStreamWriter out, Vector<String> productFields,
                                  Vector<String> productLinks) {
        String header = "# db version "+ourDB.getVersion()+"\n"
                +"# csv version 0\n";
        header += "product\t";
        for (String fieldName: productFields) {
            header += fieldName+"\t";
        }
        for (String links: productLinks){
            header += links+"\t";
        }
        try{
            header = header.substring(0, header.length() - 1)+"\n";  // replace the last \t by \n
            out.write(header, 0, header.length());
        } catch (IOException e) {
            Log.d(TAG, "In prepareHeader(): IOException: " + e.getMessage());
            return false;
        }
        return true;
    }

    private boolean populateCSV(OutputStreamWriter out, Vector<String> productFields,
                                Vector<String> productLinks) {
        String line = "";

        for (String productName : ourDB.getProductsTable().getProductNames()) {
            line = productName+"\t";
            for (String field : productFields) {
                line += ourDB.getProductsTable().getProductField(productName, field);
                line += "\t";
            }
            for (String link : productLinks) {
                if ( link == "category" ) {
                    line += solveCategory(productName)+"\t";
                } else if ( link == "shop" ) {
                    line += solveShop(productName)+"\t";
                }
            }
            try{
                line = line.substring(0, line.length() - 1)+"\n";  // replace the last \t by \n
                out.write(line);
            } catch (IOException e) {
                Log.d(TAG, "In populateCSV(): in "+productName+" IOException: " + e.getMessage());
                return false;
            }
        }
        return true;
    }

    private String solveCategory(String productName) {
        int categoryId = Integer.parseInt(ourDB.getProductsTable().getProductField(productName, "category"));
        return ourDB.getCategoriesTable().getCategoryName(categoryId);
    }

    private String solveShop(String  productName) {
        String register = "";
        int productId = ourDB.getProductsTable().getProductId(productName);
        Vector<String> shops = ourDB.getProductsTable().getProductShops(productName);
        int shopId;
        int position;

        for (String shopName : shops) {
            shopId = ourDB.getShopsTable().getShopId(shopName);
            position = ourDB.getProductShopTable().productPositionInShop(productName, productId, shopName, shopId);
            register += shopName+","+position+";";
        }
        return register;
    }
}
