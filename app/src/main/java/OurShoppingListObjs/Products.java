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
 * Created by serguei on 26/08/16.
 * Singleton class that contains all the items this application know.
 */
public class Products implements OurLists {
    private static Products ourInstance = new Products();
    private int size;

    protected Context context;

    public static Products getInstance() {
        return ourInstance;
    }

    private Products() {
        Log.d("Products", "Construtor");
    }

    public Context getContext() {
        return context;
    }

    public Products setContext(Context context) {
        Log.d("Products", "setContext(...)");
        if ( this.context == null ) {
            this.context = context;
            DatabaseSingleton db = DatabaseSingleton.getInstance();
            db.setContext(context);
            size = db.getNumberOfProducts();
            if (size == 0) {
                Log.d("Products", "No products found, populating with development list");
                db._populateProducts();  // FIXME: This is only for development to have initial products
            } else {
                Log.d("Products", size+" found. Not necessary to populate from scratch");
            }
        }
        return this;
    }

    /****************/
    /* OurList area */
    // getters:
    public Product elementById(Integer id) {
        Log.d("Products", "elementById("+id+")");
        if (context == null) {
            return null;
        }
        return DatabaseSingleton.getInstance().getProduct(id);
    }

    public Product elementByPosition(Integer position) {
        Log.d("Products", "elementByPosition("+position+")");
        if (context == null) {
            return null;
        }
        return DatabaseSingleton.getInstance().getProductByPosition(position);
    }

    public Product elementByName(String name){
        Log.d("Products", "elementByName("+name+")");
        if (context == null) {
            return null;
        }
        return DatabaseSingleton.getInstance().getProductByName(name);
    }

    // setters:
    public Integer add(OurShoppingListObj product) {
        Log.d("Products", "add("+product.getName()+")");
        if (context == null) {
            return -1;
        }
        Integer id = DatabaseSingleton.getInstance().insertProduct((Product) product);
        product.setId(id);
        size = DatabaseSingleton.getInstance().getNumberOfProducts();
        return id;
    }

    public boolean modify(OurShoppingListObj product) {
        Log.d("Products", "modify("+product.getName()+")");
        if (context == null) {
            return false;
        }
        return DatabaseSingleton.getInstance().modifyProduct((Product) product);
    }

    public boolean remove(Integer id) {
        boolean removed;
        Log.d("Products", "remove("+id+")");
        if (context == null) {
            return false;
        }
        removed = DatabaseSingleton.getInstance().removeProduct(id);
        size = DatabaseSingleton.getInstance().getNumberOfProducts();
        return removed;
    }

    public int size() {
        Log.d("Products", "size() = "+size);
        if (context == null) {
            return 0;
        }
        return size;//DatabaseSingleton.getInstance().getNumberOfProducts();
    }
    /* done OurList area */
}

