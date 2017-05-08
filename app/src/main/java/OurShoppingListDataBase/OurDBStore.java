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

class OurDBStore extends SQLiteOpenHelper {
    final static String TAG = "OurDBStore";
    protected OurTableProducts products = null;
    protected OurTableCategories categories = null;
    protected OurTableShops shops = null;
    protected  OurTableRelationProductShop product_has_shop = null;
    Vector<String> tables = new Vector<>();

    public OurDBStore(Context context) {
        super(context, "OurShoppingList", null, 1);
        Log.d(TAG, "construtor");
        products = new OurTableProducts(this);
        tables.add(products.getTableName());
        categories = new OurTableCategories(this);
        tables.add(categories.getTableName());
        shops = new OurTableShops(this);
        tables.add(shops.getTableName());
        product_has_shop = new OurTableRelationProductShop(this);
        tables.add(product_has_shop.getTableName());
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i(TAG, "OnCreate()");

        shops.createTable(db);
        categories.createTable(db);
        products.createTable(db);
        product_has_shop.createTable(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.i(TAG, "onUpgrade("+oldVersion+", "+newVersion+")");
    }

    protected int getVersion() {
        SQLiteDatabase sqlite = getReadableDatabase();
        int version = sqlite.getVersion();
        sqlite.close();
        return version;
    }

    protected OurTableProducts getProductsTable() {
        return products;
    }

    protected OurTableCategories getCategoriesTable() {
        return categories;
    }

    protected OurTableShops getShopsTable() {
        return shops;
    }

    protected OurTableRelationProductShop getProductShopTable() {
        return product_has_shop;
    }

    /************************************** Intenal methods  **************************************/
    protected OurShoppingListObj builderById(String table, Integer id) {
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

    protected boolean tableExist(String tableName) {
        return tables.contains(tableName);

        /*boolean exist;

        String from = "sqlite_master";
        String[] select = new String[] {"name"};
        String where = "type = 'table' AND name = ?";
        String[] whereArgs = new String[] {tableName};

        SQLiteDatabase sqlite = getReadableDatabase();
        Cursor cursor = doQuery(sqlite, from, select, where, whereArgs, null);
        if ( cursor.moveToFirst() ){
            exist = true;
            Log.d(TAG, "table "+tableName+" found");
        } else {
            exist = false;
            Log.w(TAG, "table "+tableName+" NOT found!");
        }
        cursor.close();
        sqlite.close();
        return exist;*/
    }

    protected boolean isNameInTable(String table, String name) {
        return getIdFromName(table, name) != -1;
    }

    protected OurShoppingListObj builderByName(String table, String name) {
        Log.d(TAG, "builderByName("+table+","+name+")");
        Integer id = getIdFromName(table, name);
        return builderById(table, id);
    }

    protected Integer getIdFromName(String table, String name) {
        Log.d(TAG, "getIdFromName("+table+","+name+")");
        Integer id = -1;

        String from = table;
        String[] select = new String[] {"id"};
        String where = "name LIKE ?";
        String[] whereArgs = new String[] {name};

        if ( tableExist(table) ) {
            SQLiteDatabase sqlite = getReadableDatabase();
            Cursor cursor = doQuery(sqlite, from, select, where, whereArgs, null);
            Log.d(TAG, cursor.getCount() + " '" + table + "' with name '" + name + "' located in the database");
            if (cursor.moveToNext()) {
                id = cursor.getInt(0);
                Log.d(TAG, "recovered " + name + ": " + id);
            }
            cursor.close();
            sqlite.close();
        }
        return id;
    }

    protected String getNameFromId(String table, Integer id) {
        String name = null;

        String from = table;
        String[] select = new String[] {"name"};
        String where = "id == ?";
        String[] whereArgs = new String[] {id.toString()};

        SQLiteDatabase sqlite = getReadableDatabase();
        Cursor cursor = doQuery(sqlite, from, select, where, whereArgs, null);
        Log.d(TAG, cursor.getCount()+" "+table+" with id "+id+" located in the database");
        if ( cursor.moveToFirst() ) {
            name = cursor.getString(0);
            Log.d("OurDBStore", "recovered "+id+": "+name);
        }
        cursor.close();
        sqlite.close();
        return name;
    }

    protected String getFieldFromName(String table, String field, String name) {
        String content = "";

        String from = table;
        String[] select = new String[] {field};
        String where = "name LIKE ?";
        String[] whereArgs = new String[] {name};

        if ( tableExist(table) ) {
            SQLiteDatabase sqlite = getReadableDatabase();
            Cursor cursor = doQuery(sqlite, from, select, where, whereArgs, null);
            Log.d(TAG, cursor.getCount() + " '" + table + "' with name '" + name + "' located in the database");
            if (cursor.moveToNext()) {
                content = cursor.getString(0);
                Log.d(TAG, "recovered for "+name+" field: "+field+" value: "+content);
            }
            cursor.close();
            sqlite.close();
        }
        return content;
    }

    protected Vector<String> getAllInTable(String table) {
        Log.d(TAG, "getAllInTable()");
        Integer id;
        String name;
        Vector<String> vector = new Vector<String>();

        String from = table;
        String[] select = new String[] {"id", "name"};
        String where = null;
        String[] whereArgs = null;
        String orderBy = "name COLLATE NOCASE";

        SQLiteDatabase sqlite = getReadableDatabase();
        Cursor cursor = doQuery(sqlite, from, select, where, whereArgs, orderBy);
        Log.d(TAG, cursor.getCount()+" "+table+" located in the database");
        while ( cursor.moveToNext() ) {
            id = cursor.getInt(0);
            name = cursor.getString(1);
            Log.d(TAG, "recovered "+id+": "+name);
            vector.add(name);
        }
        cursor.close();
        sqlite.close();
        return vector;
    }

    protected Cursor doQuery(SQLiteDatabase sqlite, String from, String[] select, String where, String[] whereArgs, String orderBy) {
        String query = "SELECT ";
        if ( select == null ) {
            query += "*";
        } else {
            query += select.toString();
        }
        query += " FROM "+from;
        if ( where != null ) {
            query += where;
            if ( whereArgs != null ) {
                for (String questionMark : whereArgs){
                    query.replace("?", questionMark);
                }
            }
        }
        if ( orderBy != null ) {
            query += " ORDER BY "+orderBy;
        }
        Cursor cursor = sqlite.query(from, select, where, whereArgs, null, null, orderBy);
        Log.d(TAG, "made query: " + query);
        return cursor;
    }
}

