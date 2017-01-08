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

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Vector;

import OurShoppingListDataBase.DatabaseSingleton;
import OurShoppingListObjs.Categories;
import OurShoppingListObjs.Product;
import OurShoppingListObjs.Products;

/**
 * Created by serguei on 30/08/16.
 */

public class ProductNewActivity extends AppCompatActivity {
    final static String TAG = "ProductNewActivity";

    final static int SHOP_SELECTION_RESULT = 1;

    protected EditText productText;
    protected CheckBox buyCheckBox;
    protected Spinner categorySpinner;
    protected Button shopsSpinner;
    protected Button less;
    protected TextView howMany;
    protected Button more;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.product_edit);

        productText = (EditText) findViewById(R.id.productName);
        buyCheckBox = (CheckBox) findViewById(R.id.buyCheck);
        categorySpinner = (Spinner) findViewById(R.id.category);
        shopsSpinner = (Button) findViewById(R.id.shops);
        less = (Button) findViewById(R.id.less);
        howMany = (TextView) findViewById(R.id.howMany);
        more = (Button) findViewById(R.id.more);

        buyCheckBox.setChecked(true);
        DatabaseSingleton db = DatabaseSingleton.getInstance();
        if (db.getNumberOfCategories() == 0) {  // No categories at all
            categorySpinner.setVisibility(View.GONE);
        } else {
            Vector<String> categories = db.getCategoryNames();
            Integer unclassifiedId = categories.indexOf("Unclassified");
            Log.d(TAG, "categories for the spinner: "+categories+" unclassifiedId = "+unclassifiedId);
            ArrayAdapter<String> categoriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
            Log.d(TAG, "adapted build: "+categoriesAdapter);
            categoriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            categorySpinner.setAdapter(categoriesAdapter);
            categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapter, View view, int position, long id) {
                    String category = adapter.getItemAtPosition(position).toString();
//                    Toast.makeText(adapter.getContext(), "Selected: "+category+", position: "+position+", id: "+id, Toast.LENGTH_LONG).show();
                }

                public void onNothingSelected(AdapterView<?> adapter) {
                    // TODO Auto-generated method stub

                }
            });
            categorySpinner.setSelection(unclassifiedId);
            Log.d(TAG, "categorySpinner.getSelectedProduct: "+categorySpinner.getSelectedItem());
            // TODO: set the selection to the "unclassified"
        }
        howMany.setText("1");
        less.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String strValue = howMany.getText().toString();
                Integer value = Integer.parseInt(strValue);
                if (value-1 > 0){
                    value -= 1;
                    howMany.setText(""+value);
                }
            }
        });
        more.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                String strValue = howMany.getText().toString();
                Integer value = Integer.parseInt(strValue);
                value += 1;
                howMany.setText(""+value);
            }
        });
        shopsSpinner.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), ShopsSelectionActivity.class);
                intent.putExtra("productId", -1);
                startActivityForResult(intent, SHOP_SELECTION_RESULT);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_accept:
                Product newProduct = saveProduct();
                if ( newProduct != null ) {
                    Log.d(TAG, "Saved "+newProduct + "(" + newProduct.getId() + ")");
                    finish();
                    return true;
                }
                Log.w(TAG, "NOT Saved "+newProduct + "(" + newProduct.getId() + ")");
                return false;
            case R.id.action_cancel:
                Log.d(TAG, "Cancelled");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected Product saveProduct() {
        String name = productText.getText().toString();
        boolean buy = buyCheckBox.isChecked();
        String category = categorySpinner.getSelectedItem().toString();
        Categories categories = Categories.getInstance();
        Integer categoryId = categories.elementByName(category).getId();
        Integer howMany = Integer.parseInt(this.howMany.getText().toString());
        Log.d(TAG, "saveProduct("+name+", "+buy+", "+category+", "+howMany+")");
        try {
            Product obj = new Product(name, buy, categoryId, howMany);
            Products.getInstance().add(obj);
            Log.d(TAG, "saveProduct: "+obj);
            return obj;
        } catch ( Exception e ) {
            Log.d(TAG, "saveProduct Exception: "+e);
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == SHOP_SELECTION_RESULT ) {
            Log.d(TAG, "end ProductNew");
            updateSelectedShosList(/*data*/);
        }
    }

    /* Data area*/
    private void updateSelectedShosList()
    {
        Log.d(TAG, "updateSelectedShosList()");

    }
}
