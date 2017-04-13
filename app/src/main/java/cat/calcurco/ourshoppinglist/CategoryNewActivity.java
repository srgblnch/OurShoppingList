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

package cat.calcurco.ourshoppinglist;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import OurShoppingListObjs.Categories;
import OurShoppingListObjs.Category;

/**
 * Created by serguei on 04/10/16.
 */

public class CategoryNewActivity extends AppCompatActivity {
    protected EditText categoryText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_edit);

        categoryText = (EditText) findViewById(R.id.categoryName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.ourshoppinglistobj_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem category) {
        switch (category.getItemId()) {
            case R.id.action_accept:
                Category newCategory = saveCategory();
                if ( newCategory != null ) {
                    Log.d("CategoryNew", "Saved "+newCategory + "(" + newCategory.getId() + ")");
                    finish();
                    return true;
                }
                Log.w("CategoryNew", "NOT Saved "+newCategory + "(" + newCategory.getId() + ")");
                return false;
            case R.id.action_cancel:
                Log.d("CategoryNew", "Cancelled");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(category);
        }
    }

    protected Category saveCategory() {
        String name = categoryText.getText().toString();

        Log.d("CategoryActivity", "saveCategory("+name+")");
        try {
            Category obj = new Category(name);
            Categories.getInstance().add(obj);
            Log.d("CategoryNew", "saveCategory: "+obj);
            return obj;
        } catch ( Exception e ) {
            Log.d("CategoryNew", "saveCategory Exception: "+e);
            return null;
        }
    }
}
