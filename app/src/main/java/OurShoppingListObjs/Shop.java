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

package OurShoppingListObjs;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import OurShoppingListDataBase.OurData;

/**
 * Created by serguei on 25/08/16.
 * A shop may have a name and a list of ids from the list of all known products.
 * the products in the list can can be removed or added.
 */

public class Shop extends OurShoppingListObj implements Comparable<Shop> {

    public Shop(Integer id, SQLiteDatabase db) {
        super("");

        String query = "SELECT name FROM Shops WHERE id == "+id;

        Cursor cursor = db.rawQuery(query, null);
        Log.d("Shop", cursor.getCount()+" Shops with id "+id+" located in the database");
        if ( cursor.moveToFirst() ) {
            this.id = id;
            setName(cursor.getString(0));
            Log.d("Shop", "Shop("+id+": "+name+")");
        }
        cursor.close();
        db.close();
        Log.d("Shop", "Query "+query+" done");

        //TODO: recover the productsLst for this shop using the "Products_has_Shops" table
    }

    public Shop(String name) {
        super(name);
        OurData db = OurData.getInstance();
        if (db.isShopInDB(name)) {
            this.id = db.getShopId(name);
            Shop tmp = db.getShop(this.id);
        }
    }

    @Override
    public int compareTo(Shop other) {
        int cmp = this.getName().compareToIgnoreCase(other.getName());
        Log.d("Shop("+id+": "+name+")",
                "Compare "+this.getName()+" with "+other.getName()+" and return "+cmp);
        return cmp;
    }

    @Override
    public String toString() {
        if ( getName().length() > 0 ){
            return getName();
        } else {
            return "Empty Shop";  /* TODO: assign a string id for the translation feature */
        }
    }

    // TODO: assign, sort and remove products from a shop

    public Integer countProductsToBeBougth() {
        Integer ctr = 0;
        OurData db = OurData.getInstance();
        // todo: improve. This would be very slow.
        for ( String productName: db.getShopProducts(this)) {
            Product product = new Product(productName);
            if ( product.getBuy() ) {
                ctr += 1;
            }
        }
        return ctr;
    }

    public Integer countProducts() {
        OurData db = OurData.getInstance();
        return db.getShopProducts(this).size();
    }
}
