package OurShoppingListDataBase;

import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.Buffer;
import java.util.Arrays;
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

        boolean returnCode = true;
        Vector<String> productFields = ourDB.getProductFields();
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
                    Log.d(TAG, "In exportDB2CSV(): Well done!!");
                }
            }
            try {
                out.close();
            } catch (IOException e) {
                Log.i(TAG, "In exportDB2CSV(): Exception closing the file");
            }
        }
        return returnCode;
    }

    /* TODO */
    protected boolean importDB2CSV(File file) {
        Log.d(TAG, "importDB2CSV(" + file.getAbsolutePath() + ", " + file.getName() + ")");

        boolean returnCode = true;
        Vector<String> fields = new Vector<String>();
        BufferedReader in = null;

        in = openFile(file);
        if ( in == null ) {
            returnCode = false;
        } else {
            returnCode = loadHeader(in, fields);
            try {
                if ( returnCode ) {
                    String line;
                    while ( (line = in.readLine() ) != null ) {
                        Log.d(TAG, "Get the line: "+line);
                        if ( ! processLine(line, fields) ) {
                            Log.w(TAG, "There has been an issue reading " + line);
                        }
                    }
                Log.d(TAG, "In importDB2CSV(): Well done!!");
                }
                in.close();
            } catch (IOException e) {
                Log.i(TAG, "In importDB2CSV(): Exception closing the file");
            }
        }

        return returnCode;
    }

    /************************************** Internal methods  *************************************/
    private boolean processLine(String line, Vector<String> fields) {
        String[] components = line.split("\t");
        for (int i=0; i<components.length; i++) {
            if ( fields.elementAt(i).equals("product") ) {
                if (ourDB.isProductInDB(components[i])) {
''
                }
            }

        }
        return true;
    }



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

    private BufferedReader openFile(File file) {
        BufferedReader in = null;
        try {
            FileReader reader = new FileReader(file);
            in = new BufferedReader(reader);
        } catch (IOException e) {
            Log.d(TAG, "In openFile(): IOException: " + e.getMessage());
            return null;
        } catch (Exception e) {
            Log.d(TAG, "In openFile(): Exception: " + e.getMessage());
            return null;
        }
        return in;
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

    private boolean loadHeader(BufferedReader in, Vector<String> fields) {
        int dbVersion = -1;
        int csvVersion = -1;
        String header = "";
        try {
            dbVersion = getVersionField(in, "db");
            csvVersion = getVersionField(in, "csv");
            if ( dbVersion == 1 && csvVersion == 0) {
                header = in.readLine();
                if ( header != null ) {
                    for (String element: header.split("\t")) {
                        fields.add(element);
                    }
                    return true;
                }
            } else {
                Log.w(TAG, "ALERT! VERSIONS!");
            }
        } catch (IOException e) {
            Log.i(TAG, "In loadHeader(): Exception reading lines "+e.getMessage());
        } catch (Exception e) {
            Log.d(TAG, "In loadHeader(): Exception: " + e.getMessage());
        }
        return false;
    }

    private boolean populateCSV(OutputStreamWriter out, Vector<String> productFields,
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

    private int getVersionField(BufferedReader in, String field) throws Exception {
        String line = in.readLine();
        if ( line.isEmpty() ) {
            throw new Exception("Could not get the line");
        }else if ( ! line.startsWith("# ")) {
            throw new Exception("First line not understandable");
        } else {
            String[] elements = line.split(" ");
            if ( elements[1].equals(field) ) {
                return Integer.parseInt(elements[3]);
            }
        }
        return -1;
    }
}
