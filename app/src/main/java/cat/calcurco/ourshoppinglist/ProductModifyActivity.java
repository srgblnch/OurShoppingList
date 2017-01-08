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
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import java.util.Vector;

import OurShoppingListDataBase.DatabaseSingleton;
import OurShoppingListObjs.Categories;
import OurShoppingListObjs.Category;
import OurShoppingListObjs.Product;
import OurShoppingListObjs.Products;

/**
 * Created by serguei on 31/08/16.
 */

public class ProductModifyActivity extends ProductNewActivity {
    final static String TAG = "ProductModifyActivity";
    protected Integer id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");
        String name = extras.getString("name");
        productText.setText(name);
        buyCheckBox.setChecked(extras.getBoolean("buy"));
        howMany.setText(""+extras.getInt("howmany"));
        // FIXME: review this mesh
        DatabaseSingleton db = DatabaseSingleton.getInstance();
        Categories categories = Categories.getInstance();
        Integer categoryId = extras.getInt("category");
        Category category = categories.elementById(categoryId);
        Log.d(TAG, "Product "+name+" category: "+category.getName()+" ("+categoryId+")");
        Vector<String> categoriesStrLst = db.getCategoryNames();
        Log.d(TAG, "categories list: "+categoriesStrLst);
        Integer categoryPosition = categoriesStrLst.indexOf(category.getName());
        if (categoryPosition != -1) {
            Log.d(TAG, "categorySpinner.setSelection: " + categoryPosition);
            categorySpinner.setSelection(categoryPosition);
            Log.d(TAG, "categorySpinner.getSelectedItem: " + categorySpinner.getSelectedItem());
        }
        shopsSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShopsSelectionActivity.class);
                intent.putExtra("productId", id);
                startActivityForResult(intent, SHOP_SELECTION_RESULT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_modify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_modify:
                Product newProduct = saveProduct();
                if ( newProduct != null ) {
                    Log.d(TAG, "Modified: "+newProduct + "(" + newProduct.getId() + ")");
                    finish();
                    return true;
                } //else {
                Log.w(TAG, "NOT MODIFIED: "+newProduct);
                return false;
            case R.id.action_delete:
                requestConfirmation();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected Product saveProduct() {
        String name = productText.getText().toString();
        boolean buy = buyCheckBox.isChecked();
        String category = categorySpinner.getSelectedItem().toString();
        Integer howmany = Integer.parseInt(howMany.getText().toString());
        try {
            Products products = Products.getInstance();
            Product product = products.elementById(id);
            product.setName(name);
            product.setBuy(buy);
            product.setCategory(category);
            product.setHowMany(howmany);
            products.modify(product);
            return product;
        } catch ( Exception e ) {
            Log.e(TAG, "Exception saving product!"+e);
            return null;
        }
    }

    protected void requestConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete action")
                .setMessage("Are you sure you want delete this product?")
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
                    Log.i(TAG, "Delete confirmed");
                    deleteProduct();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    Log.i(TAG, "Delete cancelled");
                    break;
            }
        }
    };

    protected void deleteProduct() {
        Products products = Products.getInstance();
        products.remove(id);
        finish();
    }
}
