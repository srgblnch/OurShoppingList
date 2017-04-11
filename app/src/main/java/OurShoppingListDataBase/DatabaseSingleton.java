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
public class DatabaseSingleton {
    private static DatabaseSingleton ourInstance = new DatabaseSingleton();
    private Context context = null;
    private OurShoppingListDB db;

    public static DatabaseSingleton getInstance() {
        return ourInstance;
    }

    private DatabaseSingleton() {
        Log.d("DatabaseSingleton", "Constructor");
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        Log.d("DatabaseSingleton", "setContext(...)");
        if ( this.context == null ) {
            this.context = context;
            db = new OurShoppingListDB(context);
        } else {
            Log.w("DatabaseSingleton", "Try to assign context when it already was!");
        }
    }

    /***************************************** Products area *****************************************/

    public Product getProduct(Integer id) {
        Log.d("DatabaseSingleton", "getProduct("+id+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return null;
        }
        return db.getProductObj(id);
    }

    public Product getProductByPosition(Integer position) {
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return null;
        }
        String name = db.getProductNames().get(position);
        Log.d("DatabaseSingleton", "getProductByPosition("+position+")");
        return db.getProductObj(name);
    }

    public Product getProductByName(String name) {
        Log.d("DatabaseSingleton", "getProductByName("+name+")");
        return db.getProductObj(name);
    }

    public boolean isProductInDB(String name) {
        Log.d("DatabaseSingleton", "isProductInDB("+name+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return false;
        }
        return db.isProductInDB(name);
    }

    public Integer getProductId(String name) {
        Log.d("DatabaseSingleton", "getProductId("+name+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return -1;
        }
        return db.getProductId(name);
    }

    public Integer insertProduct(Product obj) {
        Log.d("DatabaseSingleton", "insertProduct("+obj.getName()+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return -1;
        }
        return db.insertProductObj(obj);
    }

    public boolean modifyProduct(Product obj) {
        Log.d("DatabaseSingleton", "modifyProduct("+obj.getId()+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return false;
        }
        //return db.modifyProductObj(obj);
        boolean res = db.modifyProductObj(obj);
        Product tmp = new Product(obj.getId(), db.getReadableDatabase());
        return res;
    }

    public boolean removeProduct(Integer id) {
        Log.d("DatabaseSingleton", "removeProduct("+id+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return false;
        }
        return db.removeProductObj(id);
    }

    public Integer getNumberOfProducts() {
        Log.d("DatabaseSingleton", "getNumberOfProducts()");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return 0;
        }
        Vector<String> elementsLst = db.getProductNames();
        return elementsLst.size();
    }

    public Vector<String> getProductNames() {
        Log.d("DatabaseSingleton", "getCategoryNames()");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return null;
        }
        return db.getProductNames();
    }

    public void _populateProducts() {
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return;
        }
        db._populateProducts();
    }

    /************************************** Categories area  **************************************/

    public Category getCategory(Integer id) {
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return null;
        }
        return db.getCategoryObj(id);
    }

    public Category getCategoryByPosition(Integer position) {
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return null;
        }
        String name = db.getCategoryNames().get(position);
        Log.d("DatabaseSingleton", "getCategoryByPosition("+position+")");
        return db.getCategoryObj(name);
    }

    public Category getCategoryByName(String name) {
        Log.d("DatabaseSingleton", "getShopByName("+name+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return null;
        }
        return db.getCategoryObj(name);
    }

    public boolean isCategoryInDB(String name) {
        Log.d("DatabaseSingleton", "isCategoryInDB("+name+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return false;
        }
        return db.isCategoryInDB(name);
    }

    public Integer getCategoryId(String name) {
        Log.d("DatabaseSingleton", "getCategoryId("+name+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return -1;
        }
        return db.getCategoryId(name);
    }

    public Integer insertCategory(Category obj) {
        Log.d("DatabaseSingleton", "insertCategory("+obj.getName()+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return -1;
        }
        return db.insertCategoryObj(obj);
    }

    public boolean modifyCategory(Category obj) {
        Log.d("DatabaseSingleton", "modifyCategory("+obj.getId()+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return false;
        }
        //return db.modifyCategoryObj(obj);
        boolean res = db.modifyCategoryObj(obj);
        Category tmp = new Category(obj.getId(), db.getReadableDatabase());
        return res;
    }

    public boolean removeCategory(Integer id) {
        Log.d("DatabaseSingleton", "removeCategory("+id+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return false;
        }
        return db.removeCategoryObj(id);
    }

    public Integer getNumberOfCategories() {
        Log.d("DatabaseSingleton", "getNumberOfCategories()");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return 0;
        }
        Vector<String> categoriesLst = db.getCategoryNames();
        return categoriesLst.size();
    }

    public Vector<String> getCategoryNames() {
        Log.d("DatabaseSingleton", "getCategoryNames()");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return null;
        }
        return db.getCategoryNames();
    }

    public void _populateCategories() {
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return;
        }
        db._populateCategories();
    }

    /***************************************** Shops area *****************************************/

    public Shop getShop(Integer id) {
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return null;
        }
        return db.getShopObj(id);
    }

    public Shop getShopByPosition(Integer position) {
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return null;
        }
        String name = db.getShopNames().get(position);
        Log.d("DatabaseSingleton", "getShopByPosition("+position+")");
        return db.getShopObj(name);
    }

    public Shop getShopByName(String name) {
        Log.d("DatabaseSingleton", "getShopByName("+name+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return null;
        }
        return db.getShopObj(name);
    }

    public boolean isShopInDB(String name) {
        Log.d("DatabaseSingleton", "isShopInDB("+name+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return false;
        }
        return db.isShopInDB(name);
    }

    public Integer getShopId(String name) {
        Log.d("DatabaseSingleton", "getShopId("+name+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return -1;
        }
        return db.getShopId(name);
    }

    public Integer insertShop(Shop obj) {
        Log.d("DatabaseSingleton", "insertShop("+obj.getName()+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return -1;
        }
        return db.insertShopObj(obj);
    }

    public boolean modifyShop(Shop obj) {
        Log.d("DatabaseSingleton", "modifyShop("+obj.getId()+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return false;
        }
        //return db.modifyShopObj(obj);
        boolean res = db.modifyShopObj(obj);
        Shop tmp = new Shop(obj.getId(), db.getReadableDatabase());
        return res;
    }

    public boolean removeShop(Integer id) {
        Log.d("DatabaseSingleton", "removeShop("+id+")");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return false;
        }
        return db.removeShopObj(id);
    }

    public Integer getNumberOfShops() {
        Log.d("DatabaseSingleton", "getNumberOfShop()");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return 0;
        }
        Vector<String> shopsLst = db.getShopNames();
        return shopsLst.size();
    }

    public Vector<String> getShopNames() {
        Log.d("DatabaseSingleton", "getShopNames()");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return null;
        }
        return db.getShopNames();
    }

    public void _populateShops() {
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return;
        }
        db._populateShops();
    }

    /***************************** Products may have some shops assigned *****************************/

    public Vector<String> getShopProducts(Shop shop) {
        Log.d("DatabaseSingleton", "getShopProducts()");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return null;
        }
        return db.getShopProducts(shop);
    }

    public boolean isProductInShop(Product product, Shop shop) {
        Log.d("DatabaseSingleton", "isProductInShop()");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return false;
        }
        return db.isProductInShop(product, shop);
    }

    public Integer getProductPositionInShop(Product product, Shop shop) {
        Log.d("DatabaseSingleton", "getProductPositionInShop()");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return null;
        }
        return db.productPositionInShop(product, shop);
    }

    public boolean setProductPositionInShop(Product product, Shop shop, Integer position) {
        Log.d("DatabaseSingleton", "setProductPositionInShop()");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return false;
        }
        return db.modifyProductInShopPosition(product, shop, position);
    }

    public Integer insertProductInShop(Product product, Shop shop, Integer position) {
        Log.d("DatabaseSingleton", "insertProductInShop()");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return null;
        }
        return db.insertProductInShop(product, shop, position);
    }

    public boolean modifyProductInShopPosition(Product product, Shop shop, Integer position) {
        Log.d("DatabaseSingleton", "modifyProductInShopPosition()");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return false;
        }
        return db.modifyProductInShopPosition(product, shop, position);
    }

    public boolean removeProductInShop(Product product, Shop shop) {
        Log.d("DatabaseSingleton", "removeProductInShop()");
        if (db == null) {
            Log.e("DatabaseSingleton", "database not ready, set the context first!!");
            return false;
        }
        return db.removeProductInShop(product, shop);
    }

    /********************************** Import/export methods  ************************************/

    public boolean exportDB2CSV(File directory, String fileName) {
        OurShoppingListCSV csv = new OurShoppingListCSV(db);
        return csv.exportDB2CSV(directory, fileName);
    }

    public boolean importDB2CSV(File file) {
        OurShoppingListCSV csv = new OurShoppingListCSV(db);
        return csv.importDB2CSV(file);
    }
}
