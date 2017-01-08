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

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import OurShoppingListObjs.Categories;
import OurShoppingListObjs.Category;

/**
 * Created by serguei on 04/10/16.
 */

public class CategoryModifyActivity extends CategoryNewActivity {
    protected Integer id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");
        categoryText.setText(extras.getString("name"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_modify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem category) {
        switch (category.getItemId()) {
            case R.id.action_modify:
                Category newCategory = saveCategory();
                if ( newCategory != null ) {
                    Log.d("CategoryModify", "Modified: "+newCategory + "(" + newCategory.getId() + ")");
                    finish();
                    return true;
                }
                Log.w("CategoryModify", "NOT MODIFIED: "+newCategory + "(" + newCategory.getId() + ")");
                return false;
            case R.id.action_delete:
                requestConfirmation();
                return true;
            default:
                return super.onOptionsItemSelected(category);
        }
    }

    protected Category saveCategory() {
        String name = categoryText.getText().toString();
        try {
            Categories categories = Categories.getInstance();
            Category category = categories.elementById(id);
            category.setName(name);
            return category;
        } catch ( Exception e ) {
            return null;
        }
    }

    protected void requestConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete action")
                .setMessage("Are you sure you want delete this category?")
                .setPositiveButton(android.R.string.yes, dialogClickListener)
                .setNegativeButton(android.R.string.no, dialogClickListener)
                .show();
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    Log.i("CategoryModify", "Delete confirmed");
                    deleteCategory();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    Log.i("CategoryModify", "Delete cancelled");
                    break;
            }
        }
    };

    protected void deleteCategory() {
        Categories categories = Categories.getInstance();
        categories.remove(id);
        finish();
    }
}
