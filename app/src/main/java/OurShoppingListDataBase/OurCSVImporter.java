package OurShoppingListDataBase;

import android.support.annotation.Nullable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.LineNumberReader;
import java.util.Vector;

import OurShoppingListObjs.Categories;
import OurShoppingListObjs.Category;
import OurShoppingListObjs.Product;
import OurShoppingListObjs.Products;
import OurShoppingListObjs.Shop;
import OurShoppingListObjs.Shops;

/**
 * Created by serguei on 24/04/17.
 */

class OurCSVImporter {
    final static String TAG = "OurCSVImporter";
    OurDBStore ourDB = null;

    protected OurCSVImporter(OurDBStore db) {
        ourDB = db;
        Log.d(TAG, "construtor");
    }

    protected boolean doImport(File file) {
        Log.d(TAG, "doImport(" + file.getAbsolutePath() + ", " + file.getName() + ")");

        boolean returnCode = true;
        Vector<String> fields = new Vector<String>();
        BufferedReader in = null;
        Integer nRows, currentRow = 0;

        in = openFile(file);
        if ( in == null ) {
            returnCode = false;
        } else {
            returnCode = loadHeader(in, fields);
            try {
                if ( returnCode ) {
                    String line;
                    nRows = totalNumberOfLines(file)-3;  // db and csv version lines and header
                    Log.d(TAG, "Expected to process "+nRows+" lines");
                    while ( (line = in.readLine() ) != null ) {
                        Log.d(TAG, "Get the line "+currentRow+" :"+line);
                        if ( ! processLine(line, fields) ) {
                            Log.w(TAG, "There has been an issue reading " + line);
                        }
                        currentRow++;
                    }
                    Log.d(TAG, "In doImport(): Well done!!");
                }
                in.close();
            } catch (IOException e) {
                Log.i(TAG, "In doImport(): Exception closing the file");
            }
        }

        return returnCode;
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

    private Integer totalNumberOfLines(File file) {
        LineNumberReader lnr = null;
        Integer n = -1;
        try {
            lnr = new LineNumberReader(new FileReader(file));
            while ( lnr.readLine() != null );  // iterate until the last line
            n = lnr.getLineNumber()+1;
            lnr.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return n;
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

    private int getVersionField(BufferedReader in, String field) throws Exception {
        String line = in.readLine();
        line = line.replace("\t", " ").trim();
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

    private boolean processLine(String line, Vector<String> fields) {
        // fixme: horrible but first approach
        String[] components = line.split("\t");
        // about product
        boolean productExist;
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
            productExist = ourDB.getProductsTable().isProductInDB(name);
            product = new Product(name);
            if ( ! productExist ) {
                Products.getInstance().add(product);
            }
            Log.d(TAG, "processing product "+name);
        }
        if ( fields.contains("buy") ) {
            // FIXME: it may be '0', '1', 'true' or 'false'
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
            categoryExist = ourDB.getCategoriesTable().isCategoryInDB(categoryName);
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
                shopExist = ourDB.getShopsTable().isShopInDB(shopName);
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
        return products.modify(product);
    }
}
