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

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import OurShoppingListDataBase.OurData;

/**
 * Created by serguei on 03/09/16.
 */

public class Category extends OurShoppingListObj implements Comparable<Category> {
    final static String TAG = "Category";

    public Category(Integer id, SQLiteDatabase db) {
        super("");
        String query = "SELECT name FROM Categories WHERE id == "+id;

        Cursor cursor = db.rawQuery(query, null);
        Log.d(TAG, cursor.getCount()+" Categories with id "+id+" located in the database");
        if ( cursor.moveToFirst() ) {
            this.id = id;
            setName(cursor.getString(0));
            Log.d(TAG, "Category("+id+": "+name+")");
        }
        cursor.close();
        db.close();
        Log.d(TAG, "Query "+query+" done");
    }

    public Category(String name) {
        super(name);
        OurData db = OurData.getInstance();
        if (db.isCategoryInDB(name)) {
            this.id = db.getCategoryId(name);
            Category tmp = db.getCategory(this.id);
        }
    }

    @Override
    public int compareTo(Category other) {
        int cmp = this.getName().compareToIgnoreCase(other.getName());
        Log.d(TAG+"("+id+": "+name+")",
                "Compare "+this.getName()+" with "+other.getName()+" and return "+cmp);
        return cmp;
    }

    @Override
    public String toString() {
        if ( getName().length() > 0 ){
            return getName();
        } else {
            return "Empty Category";  /* TODO: assign a string id for the translation feature */
        }
    }
}
