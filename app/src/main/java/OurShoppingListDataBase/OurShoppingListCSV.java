package OurShoppingListDataBase;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.Arrays;
import java.util.Vector;

import OurShoppingListObjs.Categories;
import OurShoppingListObjs.Product;
import OurShoppingListObjs.Category;
import OurShoppingListObjs.Products;
import OurShoppingListObjs.Shop;
import OurShoppingListObjs.Shops;

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
        // fixme: horrible but first approach
        String[] components = line.split("\t");
        // about product
        Product product = null;
        String name;
        boolean buy;
        int howmany;
        // about category
        String categoryName;
        boolean categoryExist;
        Category category;
        // about shops
        String shopsLst;
        String shopName;
        boolean shopExist;
        int inShopPosition;
        Shop shop;

        if ( fields.contains("product") ){
            name = components[fields.indexOf("product")];
            product = new Product(name);
            Log.d(TAG, "processing product "+name);
        }
        if ( fields.contains("buy") ) {
            buy = Integer.parseInt(components[fields.indexOf("buy")]) > 0 ? true : false;
            if ( product.getBuy() != buy ) {
                Log.d(TAG, "buy change for "+product.getName()
                        +" from "+product.getBuy()+" to "+buy);
                product.setBuy(buy);
            } else {
                Log.d(TAG, "buy doesn't change for "+product.getName());
            }
        }
        if ( fields.contains("howmany") ) {
            howmany = Integer.parseInt(components[fields.indexOf("howmany")]);
            if ( product.getHowMany() != howmany ) {
                Log.d(TAG, "howmany change for "+product.getName()
                        +" from "+product.getHowMany()+" to "+howmany);
                product.setHowMany(howmany);
            } else {
                Log.d(TAG, "howmany doesn't change for "+product.getName());
            }
        }
        if ( fields.contains("category") ) {
            categoryName = components[fields.indexOf("category")];
            categoryExist = ourDB.isCategoryInDB(categoryName);
            if ( ! product.getCategory().equals(categoryName) ) {
                Log.d(TAG, "category change for "+product.getName()
                        +" from "+product.getCategory()+" to "+categoryName);
                category = new Category(categoryName);
                if ( ! categoryExist ) {
                    Categories.getInstance().add(category);
                }
                product.setCategoryId(category.getId());
            } else {
                Log.d(TAG, "category doesn't change for " + product.getName());
            }
        }
        if ( fields.contains("shop") ) {
            shopsLst = components[fields.indexOf("shop")];
            for ( String shopWithPosition: shopsLst.split(";") ) {
                String[] pair = shopWithPosition.split(",");
                shopName = pair[0];
                inShopPosition = Integer.parseInt(pair[1]);
                shopExist = ourDB.isShopInDB(shopName);
                shop = new Shop(shopName);
                if ( ! shopExist ) {
                    Shops.getInstance().add(shop);
                }
                if ( ! product.isInShop(shop) ) {
                    Log.d(TAG, "product "+product.getName()+" not in shop "+shop.getName());
                    product.assignShop(shop);
                    product.setPoitionInShop(shop, inShopPosition);
                } else if ( product.getPoitionInShop(shop) != inShopPosition) {
                    Log.d(TAG, "product "+product.getName()
                            +" change position in shop "+shop.getName()
                            +" from "+product.getPoitionInShop(shop)+" to "+inShopPosition);
                    product.setPoitionInShop(shop, inShopPosition);
                } else {
                    Log.d(TAG, "product "+product.getName()+" no changes for shop "+shop.getName());
                }
            }
        }
        Products products = Products.getInstance();
        products.modify(product);
        return true;
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

    @Nullable
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
