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

import android.content.Context;
import android.util.Log;

import OurShoppingListDataBase.DatabaseSingleton;

/**
 * Created by serguei on 03/10/16.
 */

public class Shops implements OurLists {
    private static Shops ourInstance = new Shops();
    private int size;

    protected Context context;

    public static Shops getInstance() {
        return ourInstance;
    }

    private Shops() {
        Log.d("Shops", "Construtor");
    }

    public Context getContext() {
        return context;
    }

    public Shops setContext(Context context) {
        Log.d("Shops", "setContext(...)");
        if ( this.context == null ) {
            this.context = context;
            DatabaseSingleton db = DatabaseSingleton.getInstance();
            db.setContext(context);
            size = db.getNumberOfShops();
            if (size == 0) {
                Log.d("Shops", "No shops found, populating with development list");
                db._populateShops();  // FIXME: This is only for development to have initial products
            } else {
                Log.d("Shops", size+" found. Not necessary to populate from scratch");
            }
        }
        return this;
    }

    /****************/
    /* OurList area */
    // getters:
    public Shop elementById(Integer id) {
        Log.d("Shops", "elementById("+id+")");
        if (context == null) {
            return null;
        }
        return DatabaseSingleton.getInstance().getShop(id);
    }

    public Shop elementByPosition(Integer position) {
        Log.d("Shops", "elementByPosition("+position+")");
        if (context == null) {
            return null;
        }
        return DatabaseSingleton.getInstance().getShopByPosition(position);
    }

    public Shop elementByName(String name){
        Log.d("Shops", "elementByName("+name+")");
        if (context == null) {
            return null;
        }
        return DatabaseSingleton.getInstance().getShopByName(name);
    }

    // setters:
    public Integer add(OurShoppingListObj shop) {
        Log.d("Shops", "add("+shop.getName()+")");
        if (context == null) {
            return -1;
        }
        Integer id = DatabaseSingleton.getInstance().insertShop((Shop) shop);
        shop.setId(id);
        size = DatabaseSingleton.getInstance().getNumberOfShops();
        return id;
    }

    public boolean modify(OurShoppingListObj shop) {
        Log.d("Shop", "modify("+shop.getName()+")");
        if (context == null) {
            return false;
        }
        return DatabaseSingleton.getInstance().modifyShop((Shop) shop);
    }

    public boolean remove(Integer id) {
        boolean removed;
        Log.d("Shop", "remove("+id+")");
        if (context == null) {
            return false;
        }
        removed = DatabaseSingleton.getInstance().removeShop(id);
        size = DatabaseSingleton.getInstance().getNumberOfShops();
        return removed;
    }

    public int size() {
        Log.d("Shop", "size() = "+size);
        if (context == null) {
            return 0;
        }
        return size;//DatabaseSingleton.getInstance().getNumberOfShops();
    }
    /* done OurList area */
}