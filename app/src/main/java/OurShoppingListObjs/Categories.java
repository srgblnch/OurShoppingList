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

public class Categories implements OurLists {
    private static Categories ourInstance = new Categories();
    private int size;

    protected Context context;

    public static Categories getInstance() {
        return ourInstance;
    }

    private Categories() {
        Log.d("Categories", "Construtor");
    }

    public Context getContext() {
        return context;
    }

    public Categories setContext(Context context) {
        Log.d("Categories", "setContext(...)");
        if ( this.context == null ) {
            this.context = context;
            DatabaseSingleton db = DatabaseSingleton.getInstance();
            db.setContext(context);
            size = db.getNumberOfCategories();
            if (size == 0) {
                Log.d("Categories", "No categories found, populating with development list");
                db._populateCategories();  // FIXME: This is only for development to have initial products
            } else {
                Log.d("Categories", size+" found. Not necessary to populate from scratch");
            }
        }
        return this;
    }

    /****************/
    /* OurList area */
    // getters:
    public Category elementById(Integer id) {
        Log.d("Categories", "elementById("+id+")");
        if (context == null) {
            return null;
        }
        return DatabaseSingleton.getInstance().getCategory(id);
    }

    public Category elementByPosition(Integer position) {
        Log.d("Categories", "elementByPosition("+position+")");
        if (context == null) {
            return null;
        }
        return DatabaseSingleton.getInstance().getCategoryByPosition(position);
    }

    public Category elementByName(String name){
        Log.d("Categories", "elementByName("+name+")");
        if (context == null) {
            return null;
        }
        return DatabaseSingleton.getInstance().getCategoryByName(name);
    }

    // setters:
    public Integer add(OurShoppingListObj category) {
        Log.d("Categories", "add("+category.getName()+")");
        if (context == null) {
            return -1;
        }
        Integer id = DatabaseSingleton.getInstance().insertCategory((Category) category);
        category.setId(id);
        size = DatabaseSingleton.getInstance().getNumberOfCategories();
        return id;
    }

    public boolean modify(OurShoppingListObj'' category) {
        Log.d("Categories", "modify("+category.getName()+")");
        if (context == null) {
            return false;
        }
        return DatabaseSingleton.getInstance().modifyCategory((Category) category);
    }

    public boolean remove(Integer id) {
        boolean removed;
        Log.d("Categories", "remove("+id+")");
        if (context == null) {
            return false;
        }
        removed = DatabaseSingleton.getInstance().removeCategory(id);
        size = DatabaseSingleton.getInstance().getNumberOfCategories();
        return removed;
    }

    public int size() {
        Log.d("Categories", "size() = "+size);
        if (context == null) {
            return 0;
        }
        return size;//DatabaseSingleton.getInstance().getNumberOfCategories();
    }
    /* done OurList area */
}
