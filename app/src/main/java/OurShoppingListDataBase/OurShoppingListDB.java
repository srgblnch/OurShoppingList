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
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.Arrays;
import java.util.Vector;

import OurShoppingListObjs.Category;
import OurShoppingListObjs.Product;
import OurShoppingListObjs.OurShoppingListObj;
import OurShoppingListObjs.Shop;

/**
 * Created by serguei on 18/09/16.
 */

public class OurShoppingListDB extends SQLiteOpenHelper {
    final static String TAG = "OurShoppingListDB";

    public OurShoppingListDB(Context context) {
        super(context, "OurShoppingList", null, 1);
        Log.d(TAG, "construtor");
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "OnCreate()");
        createCategoriesTable(db);
        createProductsTable(db);
        createShopsTable(db);
        createRelationProductsWithshops(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade("+oldVersion+", "+newVersion+")");
    }

    protected int getVersion() {
        SQLiteDatabase db = getReadableDatabase();
        int version = db.getVersion();
        db.close();
        return version;
    }

    /**************************************** Products table  ****************************************/

    private void createProductsTable(SQLiteDatabase db) {
        Log.d(TAG, "createProductsTable()");
        String creator =
            "CREATE TABLE `Products` (" +
                    "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                    "`name` TEXT NOT NULL," +
                    "`buy` NUMERIC NOT NULL," +
                    "`category` INTEGER," +
                    "`howmany` INTEGER" +
                    ");";
        db.execSQL(creator);
    }

    protected Vector<String> getProductFields() {
        return new Vector<String>(Arrays.asList("buy", "howmany"));
    }

    protected Product getProductObj(Integer id) {
        Log.d(TAG, "getProductObj("+id+")");
        return (Product)builderById("Products", id);
    }

    protected Product getProductObj(String name) {
        Log.d(TAG, "getProductObj("+name+")");
        return (Product)builderByName("Products", name);
    }

    protected Vector<String> getProductNames() {
        Log.d(TAG, "getProductNames()");
        return getAllInTable("Products");
    }

    protected boolean isProductInDB(String name) {
        return isNameInTable("Products", name);
    }

    protected Integer getProductId(String name) {
        return getIdFromName("Products", name);
    }

    protected String getProductName(Integer id) {
        return getNameFromId("Products", id);
    }

    protected String getProductField(String name, String fieldName) {
        return getFieldFromName("Products", fieldName, name);
    }

    protected Vector<String> getProductShops(String productName){
        int productId = getProductId(productName);
        Vector<String> shopNames = new Vector<String>();
        int shopId;

        for (String shopName : getShopNames()){
            shopId = getShopId(shopName);
            if ( isProductInShop(productName, productId, shopName, shopId) ){
                shopNames.add(shopName);
            }
        }
        return shopNames;
    }

    protected Integer insertProductObj(Product obj) {
        Log.d(TAG, "insertProductObj("+obj.getName()+")");
        if ( getIdFromName("Products", obj.getName()) == -1 ) { //Doesn't exist
            int buy;
            if ( obj.getBuy() ) { buy = 1; } else { buy = 0; }
            String insert = "INSERT INTO Products VALUES ( null, '"+obj.getName()+"', "+buy+", "
                    +obj.getCategoryId()+", "+obj.getHowMany()+")";
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(insert);
            Log.d(TAG, "Insert: "+insert);
            return getIdFromName("Products", obj.getName());
        } else {
            Log.w(TAG, "Insert failed, "+obj.getName()+"already exist");
            return -1;
        }
    }

    protected boolean modifyProductObj(Product obj) {
        Integer id = obj.getId();
        Log.d(TAG, "modifyProductObj("+id+")");
        if (getProductObj(id) == null) {
            return false;
        }
        String name = obj.getName();
        int buy;
        if ( obj.getBuy() ) { buy = 1; } else { buy = 0; }
        String modification = "UPDATE Products SET "+
                "name = '"+obj.getName()+"', "+
                "buy = "+buy+", "+
                "category = "+obj.getCategoryId()+", "+
                "howmany = "+obj.getHowMany()+" "+
                "WHERE id = "+id;
        Log.d(TAG, modification);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(modification);
        return true;
    }

    protected boolean removeProductObj(Integer id) {
        Log.d(TAG, "removeProductObj("+id+")");
        if (getProductObj(id) == null) {
            return false;
        }
        String deletion = "DELETE FROM Products WHERE id = " + id;
        Log.d(TAG, deletion);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(deletion);
        return true;
    }

    /* FIXME: to be removed in production */
    protected void _populateProducts() {
        String[] nameLst = {"Broquil", "Fairy", "Sabó Rentaplats", "Torrades", "Pastadents",
                "Desodorant", "Alls", "Erdäpfel", "Bacon", "Penil salat", "Pitzes",
                "Aigua", "Sucre"};
        Product product;
        for (String name: nameLst) {
            product = new Product(name);
            insertProductObj(product);
        }
    }

    /************************************** Categories table **************************************/

    private void createCategoriesTable(SQLiteDatabase db) {
        Log.d(TAG, "createCategoriesTable()");
        String creator =
                "CREATE TABLE `Categories` (" +
                        "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "`name` TEXT NOT NULL" +
                        ");";
        db.execSQL(creator);
    }

    public Category getCategoryObj(Integer id) {
        Log.d(TAG, "getCategoryObj("+id+")");
        return (Category)builderById("Categories", id);
    }

    public Category getCategoryObj(String name) {
        Log.d(TAG, "getCategoryObj("+name+")");
        return (Category)builderByName("Categories", name);
    }

    public Vector<String> getCategoryNames() {
        Log.d(TAG, "getCategoryNames()");
        return getAllInTable("Categories");
    }

    protected boolean isCategoryInDB(String name) {
        return isNameInTable("Categories", name);
    }

    protected Integer getCategoryId(String name) {
        return getIdFromName("Categories", name);
    }

    protected String getCategoryName(Integer id) {
        return getNameFromId("Categories", id);
    }

    protected Integer insertCategoryObj(Category obj) {
        Log.d(TAG, "insertCategoryObj("+obj.getName()+")");
        if ( getIdFromName("Categories", obj.getName()) == -1 ) { //Doesn't exist
            String insert = "INSERT INTO Categories VALUES ( null, '"+obj.getName()+"')";
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(insert);
            Log.d(TAG, "Insert: "+insert);
            return getIdFromName("Categories", obj.getName());
        } else {
            Log.w(TAG, "Insert failed, "+obj.getName()+"already exist");
            return -1;
        }
    }

    protected boolean modifyCategoryObj(Category obj) {
        Integer id = obj.getId();
        Log.d(TAG, "modifyCategoryObj("+id+")");
        if (getCategoryObj(id) == null) {
            return false;
        }
        String name = obj.getName();
        String modification = "UPDATE Categories SET "+
                "name = '"+obj.getName()+
                "WHERE id = "+id;

        SQLiteDatabase db = getWritableDatabase();
        db.rawQuery(modification, null);
        return true;
    }

    protected boolean removeCategoryObj(Integer id) {
        Log.d(TAG, "removeCategoryObj("+id+")");
        if (getProductObj(id) == null) {
            return false;
        }
        String deletion = "DELETE FROM Categories WHERE id = " + id;
        SQLiteDatabase db = getWritableDatabase();
        db.rawQuery(deletion, null);
        return true;
    }

    /* FIXME: to be removed in production */
    protected void _populateCategories() {
        String[] nameLst = {"Unclassified"};
        Category obj;
        for (String name: nameLst) {
            obj = new Category(name);
            insertCategoryObj(obj);
        }
    }

    /**************************************** Shops table  ****************************************/

    private void createShopsTable(SQLiteDatabase db) {
        Log.d(TAG, "createShopsTable()");
        String creator =
                "CREATE TABLE `Shops` (" +
                        "`id` INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT," +
                        "`name` TEXT NOT NULL" +
                        ");";
        db.execSQL(creator);
    }

    public Shop getShopObj(Integer id) {
        Log.d(TAG, "getShopObj("+id+")");
        return (Shop)builderById("Shops", id);
    }

    public Shop getShopObj(String name) {
        Log.d(TAG, "getShopObj("+name+")");
        return (Shop)builderByName("Shops", name);
    }

    public Vector<String> getShopNames() {
        Log.d(TAG, "getShopNames()");
        return getAllInTable("Shops");
    }

    protected boolean isShopInDB(String name) {
        return isNameInTable("Shops", name);
    }

    protected Integer getShopId(String name) {
        return getIdFromName("Shops", name);
    }

    protected String getShopName(Integer id) {
        return getNameFromId("Shops", id);
    }

    protected Integer insertShopObj(Shop obj) {
        Log.d(TAG, "insertShopObj("+obj.getName()+")");
        if ( getIdFromName("Shops", obj.getName()) == -1 ) { //Doesn't exist
            String insert = "INSERT INTO Shops VALUES ( null, '"+obj.getName()+"')";
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(insert);
            Log.d(TAG, "Insert: "+insert);
            return getIdFromName("Shops", obj.getName());
        } else {
            Log.w(TAG, "Insert failed, "+obj.getName()+"already exist");
            return -1;
        }
    }

    protected boolean modifyShopObj(Shop obj) {
        Integer id = obj.getId();
        Log.d(TAG, "modifyShopObj("+id+")");
        if (getProductObj(id) == null) {
            return false;
        }
        String name = obj.getName();
        String modification = "UPDATE Shops SET "+
                "name = '"+obj.getName()+"', "+
                "WHERE id = "+id;
        Log.d(TAG, modification);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(modification);
        return true;
    }

    protected boolean removeShopObj(Integer id) {
        Log.d(TAG, "removeShopObj("+id+")");
        Shop shop = getShopObj(id);
        if (shop == null) {
            return false;
        }
        Vector<String> productsInShop = getShopProducts(shop);
        for (String name : productsInShop) {
            getProductObj(name).unassignShop(shop);
        }
        String deletion = "DELETE FROM Shops WHERE id = " + id;
        Log.d(TAG, deletion);
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL(deletion);
        return true;
    }

    /* FIXME: to be removed in production */
    protected void _populateShops() {
        String[] nameLst = {"Eroski", "La Sirena", "Bonarea", "Esclat"};
        Shop obj;
        for (String name: nameLst) {
            obj = new Shop(name);
            insertShopObj(obj);
        }
    }

    /***************************** Products may have some shops assigned *****************************/

    private void createRelationProductsWithshops(SQLiteDatabase db) {
        Log.d(TAG, "createRelationProductsWithshops()");
        String creator =
            "CREATE TABLE `Products_has_Shops` (" +
                    "`Product`INTEGER NOT NULL," +
                    "`Shop`INTEGER NOT NULL," +
                    "`position` INTEGER," +
                    "PRIMARY KEY(Product,Shop)" +
                    ");";
        db.execSQL(creator);
    }

    protected Vector<String> getShopProducts(Shop shop) {
        Log.d(TAG, "getShopProducts("+shop.getName()+")");
        String query = "SELECT Product FROM Products_has_Shops WHERE Shop == "+shop.getId()+" ORDER BY position";
        Integer productId;
        Vector<String> productNames = new Vector<String>();

        SQLiteDatabase db = getReadableDatabase();
        Log.w(TAG, "Query '" + query + "' is going to be launched");
        Cursor cursor = db.rawQuery(query, null);
        Log.d(TAG, cursor.getCount()+" 'Products_has_Shops' with shop '"+shop.getName()+"' located in the database");
        while ( cursor.moveToNext() ) {
            productId = cursor.getInt(0);
            productNames.add(getProductName(productId));
        }
        cursor.close();
        db.close();
        return productNames;
    }

    protected boolean isProductInShop(Product product, Shop shop) {
        return isProductInShop(product.getName(), product.getId(), shop.getName(), shop.getId());
    }

    protected boolean isProductInShop(String productName, int productId, String shopName, int shopId) {
        Log.d(TAG, "isProductInShop("+productName+","+shopName+")");
        String query = "SELECT Product, Shop FROM Products_has_Shops WHERE Product == "+productId+" AND Shop == "+shopId;
        Integer counter;

        SQLiteDatabase db = getReadableDatabase();
        Log.w(TAG, "Query '" + query + "' is going to be launched");
        Cursor cursor = db.rawQuery(query, null);
        Log.d(TAG, cursor.getCount()+" 'Products_has_Shops' with shop '"+shopName+"' located in the database");
        counter = cursor.getCount();
        cursor.close();
        db.close();

        return counter > 0;
    }

    protected Integer productPositionInShop(Product product, Shop shop) {
        return productPositionInShop(product.getName(), product.getId(), shop.getName(), shop.getId());
    }

    protected Integer productPositionInShop(String productName, int productId, String shopName, int shopId) {
        Log.d(TAG, "productPositionInShop("+productName+", "+shopName+")");
        Integer position = -1;

        if ( isProductInShop(productName, productId, shopName, shopId)) {
            String query = "SELECT position FROM Products_has_Shops WHERE Product == "+productId+" AND Shop == "+shopId;
            SQLiteDatabase db = getReadableDatabase();
            Log.w(TAG, "Query '" + query + "' is going to be launched");
            Cursor cursor = db.rawQuery(query, null);
            if ( cursor.moveToFirst() ) {
                position = cursor.getInt(0);
            }
            cursor.close();
            db.close();
        }
        return position;
    }

    protected Integer insertProductInShop(Product product, Shop shop, Integer position) {
        Log.d(TAG, "insertProductInShop("+product.getName()+", "+shop.getName()+")");

        if ( !isProductInShop(product, shop)) {
            String insert = "INSERT INTO Products_has_Shops VALUES ( "+product.getId()+", "+shop.getId()+", "+position+" )";
            SQLiteDatabase db = getWritableDatabase();
            db.execSQL(insert);
            Log.d(TAG, "Insert: "+insert);
            db.close();
            return 1;
        } else {
            Log.w(TAG, "Insert failed, pair ("+product.getId()+","+shop.getId()+") already exist");
            return 0;
        }
    }

    protected boolean modifyProductInShopPosition(Product product, Shop shop, Integer newPosition) {
        Log.d(TAG, "modifyProductInShopPosition("+product.getName()+", "+shop.getName()+", "+newPosition+")");

        Integer oldPosition = productPositionInShop(product, shop);
        if ( oldPosition >= 0 ) {
            if ( oldPosition != newPosition ) {
                String modification = "UPDATE Products_has_Shops SET "+
                        "position = "+newPosition+" "+
                        "WHERE Product == "+product.getId()+" AND Shop == "+shop.getId();
                Log.d(TAG, modification);
                SQLiteDatabase db = getWritableDatabase();
                db.execSQL(modification);
                db.close();
                return true;
            } else {
                Log.w(TAG, "position hasn't change");
            }
        } else {
            Log.w(TAG, "Update failed, pair ("+product.getId()+","+shop.getId()+") doesn't exist");
        }
        return false;
    }

    protected boolean removeProductInShop(Product product, Shop shop) {
        Log.d(TAG, "removeProductInShop("+product.getName()+", "+shop.getName()+")");
        String query = "SELECT Product, Shop FROM Products_has_Shops WHERE Product == "+product.getId()+" AND Shop == "+shop.getId();
        Integer counter;

        SQLiteDatabase db = getReadableDatabase();
        Log.w(TAG, "Query '" + query + "' is going to be launched");
        Cursor cursor = db.rawQuery(query, null);
        Log.d(TAG, cursor.getCount()+" 'Products_has_Shops' with shop '"+shop.getName()+"' located in the database");
        counter = cursor.getCount();
        cursor.close();
        db.close();

        if ( counter == 1) {
            String deletion = "DELETE FROM Products_has_Shops WHERE Product == "+product.getId()+" AND Shop == "+shop.getId();
            db = getWritableDatabase();
            db.execSQL(deletion);
            Log.d(TAG, "Delete: "+deletion);
            db.close();
            return true;
        } else {
            Log.w(TAG, "Delete failed, pair ("+product.getId()+","+shop.getId()+") doesn't exist");
            return false;
        }
    }

    /************************************** Intenal methods  **************************************/
    private OurShoppingListObj builderById(String table, Integer id) {
        Log.d(TAG, "builderById("+table+","+id+")");
        if ( table == "Products" ) {
            return new Product(id, getReadableDatabase());
        } else if ( table == "Shops" ) {
            return new Shop(id, getReadableDatabase());
        } else if ( table == "Categories" ) {
            return new Category(id, getReadableDatabase());
        } else {
            return null;
        }
    }

    private boolean tableExist(String tableName) {
        String query = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+tableName+"';";
        boolean exist;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        if ( cursor.moveToFirst() ){
            exist = true;
            Log.d(TAG, "table "+tableName+" found");
        } else {
            exist = false;
            Log.w(TAG, "table "+tableName+" NOT found!");
        }
        cursor.close();
        db.close();
        return exist;
    }

    private boolean isNameInTable(String table, String name) {
        return getIdFromName(table, name) != -1;
    }

    private OurShoppingListObj builderByName(String table, String name) {
        Log.d(TAG, "builderByName("+table+","+name+")");
        Integer id = getIdFromName(table, name);
        return builderById(table, id);
    }

    private Integer getIdFromName(String table, String name) {
        Log.d(TAG, "getIdFromName("+table+","+name+")");
        String query = "SELECT id FROM "+table+" WHERE name LIKE '"+name+"'";
        Integer id = -1;

        if ( tableExist(table) ) {
            SQLiteDatabase db = getReadableDatabase();
            Log.w(TAG, "Query '" + query + "' is going to be launched");
            Cursor cursor = db.rawQuery(query, null);
            Log.d(TAG, cursor.getCount() + " '" + table + "' with name '" + name + "' located in the database");
            if (cursor.moveToNext()) {
                id = cursor.getInt(0);
                Log.d(TAG, "recovered " + name + ": " + id);
            }
            cursor.close();
            db.close();
            if (id != -1) {
                Log.d(TAG, "Query " + query + " done");
            } else {
                Log.w(TAG, "Query " + query + " failed");
            }
        }
        return id;
    }

    private String getNameFromId(String table, Integer id) {
        String query = "SELECT name FROM "+table+" WHERE id == "+id;
        String name = null;

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Log.d("OurShoppingListDB", cursor.getCount()+" "+table+" with id "+id+" located in the database");
        if ( cursor.moveToFirst() ) {
            name = cursor.getString(0);
            Log.d("OurShoppingListDB", "recovered "+id+": "+name);
        }
        cursor.close();
        db.close();
        Log.d("OurShoppingListDB", "Query "+query+" done");
        return name;
    }

    private String getFieldFromName(String table, String field, String name) {
        String query = "SELECT "+field+" FROM "+table+" WHERE name LIKE "+name;
        String content = "";

        if ( tableExist(table) ) {
            SQLiteDatabase db = getReadableDatabase();
            Log.w(TAG, "Query '" + query + "' is going to be launched");
            Cursor cursor = db.rawQuery(query, null);
            Log.d(TAG, cursor.getCount() + " '" + table + "' with name '" + name + "' located in the database");
            if (cursor.moveToNext()) {
                content = cursor.getString(0);
                Log.d(TAG, "recovered for "+name+" field: "+field+" value: "+content);
            }
            cursor.close();
            db.close();
            if (content.length() != 0) {
                Log.d(TAG, "Query " + query + " done");
            } else {
                Log.w(TAG, "Query " + query + " failed");
            }
        }
        return content;
    }

    private Vector<String> getAllInTable(String table) {
        Log.d(TAG, "getAllInTable()");
        Integer id;
        String name;
        String query = "SELECT id, name FROM "+table+ " ORDER BY name COLLATE NOCASE";
        Vector<String> vector = new Vector<String>();

        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);
        Log.d(TAG, cursor.getCount()+" "+table+" located in the database");
        while ( cursor.moveToNext() ) {
            id = cursor.getInt(0);
            name = cursor.getString(1);
            Log.d(TAG, "recovered "+id+": "+name);
            vector.add(name);
        }
        cursor.close();
        db.close();
        Log.d(TAG, "Query "+query+" done");
        return vector;
    }
}

