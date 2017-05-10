/* ##### BEGIN GPL LICENSE BLOCK #####
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, see <http://www.gnu.org/licenses/>.
 *
 * ##### END GPL LICENSE BLOCK #####
 * __author__ = "Sergi Blanch-Torne"
 * __email__ = "srgblnchtrn@protonmail.ch"
 * __copyright__ = "Copyright 2016 Sergi Blanch-Torne"
 * __license__ = "GPLv3+"
 * __status__ = "development"
 */

package OurShoppingListDataBase;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.util.Vector;

import OurShoppingListObjs.Category;
import OurShoppingListObjs.Product;
import OurShoppingListObjs.Shop;

/**
 * Created by serguei on 18/09/16.
 */
public class OurData {
    final static String TAG = "OurData";
    private static OurData ourInstance = new OurData();
    private Context context = null;
    private OurDBStore db;

    public static OurData getInstance() {
        return ourInstance;
    }

    private OurData() {
        Log.d(TAG, "Constructor");
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        Log.d(TAG, "setContext(...)");
        if ( this.context == null ) {
            this.context = context;
            db = new OurDBStore(context);
        } else {
            Log.w(TAG, "Try to assign context when it already was!");
        }
    }

    public boolean dropDatabase() {
        boolean returnCode = true;
        if ( db == null ) {
            Log.e(TAG, "database not ready, set the context first!!");
            return false;
        }
        if ( ! db.getProductsTable().drop() ) {
            Log.e(TAG, "Failed to drop Products");
            returnCode = false;
        }
        if ( ! db.getCategoriesTable().drop() ) {
            Log.e(TAG, "Failed to drop Categories");
            returnCode = false;
        }
        if ( ! db.getShopsTable().drop() ) {
            Log.e(TAG, "Failed to drop Categories");
            returnCode = false;
        }
        if ( ! db.getProductShopTable().drop() ) {
            Log.e(TAG, "Failed to drop Categories");
            returnCode = false;
        }
        return returnCode;
    }

    /***************************************** Products area *****************************************/

    public Product getProduct(Integer id) {
        Log.d(TAG, "getProduct("+id+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return null;
        }
        return db.getProductsTable().getProductObj(id);
    }

    public Product getProductByPosition(Integer position) {
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return null;
        }
        String name = db.getProductsTable().getProductNames().get(position);
        Log.d(TAG, "getProductByPosition("+position+")");
        return db.getProductsTable().getProductObj(name);
    }

    public Product getProductByName(String name) {
        Log.d(TAG, "getProductByName("+name+")");
        return db.getProductsTable().getProductObj(name);
    }

    public boolean isProductInDB(String name) {
        Log.d(TAG, "isProductInDB("+name+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return false;
        }
        return db.getProductsTable().isProductInDB(name);
    }

    public Integer getProductId(String name) {
        Log.d(TAG, "getProductId("+name+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return -1;
        }
        return db.getProductsTable().getProductId(name);
    }

    public Integer insertProduct(Product obj) {
        Log.d(TAG, "insertProduct("+obj.getName()+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return -1;
        }
        return db.getProductsTable().insert(obj);
    }

    public boolean modifyProduct(Product obj) {
        Log.d(TAG, "modifyProduct("+obj.getId()+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return false;
        }
        //return db.modifyProductObj(obj);
        boolean res = db.getProductsTable().modify(obj);
        Product tmp = new Product(obj.getId(), db.getReadableDatabase());
        return res;
    }

    public boolean removeProduct(Integer id) {
        Log.d(TAG, "removeProduct("+id+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return false;
        }
        return db.getProductsTable().remove(id);
    }

    public Integer getNumberOfProducts() {
        Log.d(TAG, "getNumberOfProducts()");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return 0;
        }
        Vector<String> elementsLst = db.getProductsTable().getProductNames();
        return elementsLst.size();
    }

    public Vector<String> getProductNames() {
        Log.d(TAG, "getCategoryNames()");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return null;
        }
        return db.getProductsTable().getProductNames();
    }

    /************************************** Categories area  **************************************/

    public Category getCategory(Integer id) {
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return null;
        }
        return db.getCategoriesTable().getCategoryObj(id);
    }

    public Category getCategoryByPosition(Integer position) {
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return null;
        }
        String name = db.getCategoriesTable().getCategoryNames().get(position);
        Log.d(TAG, "getCategoryByPosition("+position+")");
        return db.getCategoriesTable().getCategoryObj(name);
    }

    public Category getCategoryByName(String name) {
        Log.d(TAG, "getShopByName("+name+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return null;
        }
        return db.getCategoriesTable().getCategoryObj(name);
    }

    public boolean isCategoryInDB(String name) {
        Log.d(TAG, "isCategoryInDB("+name+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return false;
        }
        return db.getCategoriesTable().isCategoryInDB(name);
    }

    public Integer getCategoryId(String name) {
        Log.d(TAG, "getCategoryId("+name+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return -1;
        }
        return db.getCategoriesTable().getCategoryId(name);
    }

    public Integer insertCategory(Category obj) {
        Log.d(TAG, "insertCategory("+obj.getName()+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return -1;
        }
        return db.getCategoriesTable().insert(obj);
    }

    public boolean modifyCategory(Category obj) {
        Log.d(TAG, "modifyCategory("+obj.getId()+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return false;
        }
        //return db.modifyCategoryObj(obj);
        boolean res = db.getCategoriesTable().modify(obj);
        Category tmp = new Category(obj.getId(), db.getReadableDatabase());
        return res;
    }

    public boolean removeCategory(Integer id) {
        Log.d(TAG, "removeCategory("+id+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return false;
        }
        return db.getCategoriesTable().remove(id);
    }

    public Integer getNumberOfCategories() {
        Log.d(TAG, "getNumberOfCategories()");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return 0;
        }
        Vector<String> categoriesLst = db.getCategoriesTable().getCategoryNames();
        return categoriesLst.size();
    }

    public Vector<String> getCategoryNames() {
        Log.d(TAG, "getCategoryNames()");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return null;
        }
        return db.getCategoriesTable().getCategoryNames();
    }

    /***************************************** Shops area *****************************************/

    public Shop getShop(Integer id) {
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return null;
        }
        return db.getShopsTable().getShopObj(id);
    }

    public Shop getShopByPosition(Integer position) {
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return null;
        }
        String name = db.getShopsTable().getShopNames().get(position);
        Log.d(TAG, "getShopByPosition("+position+")");
        return db.getShopsTable().getShopObj(name);
    }

    public Shop getShopByName(String name) {
        Log.d(TAG, "getShopByName("+name+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return null;
        }
        return db.getShopsTable().getShopObj(name);
    }

    public boolean isShopInDB(String name) {
        Log.d(TAG, "isShopInDB("+name+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return false;
        }
        return db.getShopsTable().isShopInDB(name);
    }

    public Integer getShopId(String name) {
        Log.d(TAG, "getShopId("+name+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return -1;
        }
        return db.getShopsTable().getShopId(name);
    }

    public Integer insertShop(Shop obj) {
        Log.d(TAG, "insertShop("+obj.getName()+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return -1;
        }
        return db.getShopsTable().insert(obj);
    }

    public boolean modifyShop(Shop obj) {
        Log.d(TAG, "modifyShop("+obj.getId()+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return false;
        }
        //return db.modifyShopObj(obj);
        boolean res = db.getShopsTable().modify(obj);
        Shop tmp = new Shop(obj.getId(), db.getReadableDatabase());
        return res;
    }

    public boolean removeShop(Integer id) {
        Log.d(TAG, "removeShop("+id+")");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return false;
        }
        return db.getShopsTable().remove(id);
    }

    public Integer getNumberOfShops() {
        Log.d(TAG, "getNumberOfShop()");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return 0;
        }
        Vector<String> shopsLst = db.getShopsTable().getShopNames();
        return shopsLst.size();
    }

    public Vector<String> getShopNames() {
        Log.d(TAG, "getShopNames()");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return null;
        }
        return db.getShopsTable().getShopNames();
    }

    /***************************** Products may have some shops assigned *****************************/

    public Vector<String> getShopProducts(Shop shop) {
        Log.d(TAG, "getShopProducts()");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return null;
        }
        return db.getProductShopTable().getShopProducts(shop);
    }

    public boolean isProductInShop(Product product, Shop shop) {
        Log.d(TAG, "isProductInShop()");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return false;
        }
        return db.getProductShopTable().isProductInShop(product, shop);
    }

    public Integer getProductPositionInShop(Product product, Shop shop) {
        Log.d(TAG, "getProductPositionInShop()");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return null;
        }
        return db.getProductShopTable().productPositionInShop(product, shop);
    }

    public boolean setProductPositionInShop(Product product, Shop shop, Integer position) {
        Log.d(TAG, "setProductPositionInShop()");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return false;
        }
        return db.getProductShopTable().modify(product, shop, position);
    }

    public Integer insertProductInShop(Product product, Shop shop, Integer position) {
        Log.d(TAG, "insertProductInShop()");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return null;
        }
        return db.getProductShopTable().insert(product, shop, position);
    }

    public boolean modifyProductInShopPosition(Product product, Shop shop, Integer position) {
        Log.d(TAG, "modifyProductInShopPosition()");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return false;
        }
        return db.getProductShopTable().modify(product, shop, position);
    }

    public boolean removeProductInShop(Product product, Shop shop) {
        Log.d(TAG, "removeProductInShop()");
        if (db == null) {
            Log.e(TAG, "database not ready, set the context first!!");
            return false;
        }
        return db.getProductShopTable().remove(product, shop);
    }

    /********************************** Import/export methods  ************************************/

    // TODO: distinguish between different formats (when more than one possible)

    public boolean doExport(String format, File destination, String name) {
        if ( format.equals("csv")) {
            OurPorterCSV csv = new OurPorterCSV(db);
            csv.setDestination(destination);
            csv.setFileName(name);
            return csv.doExport();
        }
        return false;
    }

    public boolean doImport(String format, File file) {
        if ( format.equals("csv")) {
            OurPorterCSV csv = new OurPorterCSV(db);
            csv.setDestination(file);
            return csv.doImport();
        }
        return false;
    }
}
