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
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.CheckBox;

import OurShoppingListObjs.Categories;
import OurShoppingListObjs.Product;
import OurShoppingListObjs.Products;
import OurShoppingListObjs.Shops;

public class MainActivity extends AppCompatActivity {
    final static String TAG = "MainActivity";
    final static int NEW_PRODUCT_RESULT = 1;
    final static int MODIFY_PRODUCT_RESULT = 2;
    final static int SHOP_SELECTED = 3;

    protected Products products;

    private RecyclerView recyclerView;
    public Adaptor4Products adaptor;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton goToShop = (FloatingActionButton) findViewById(R.id.goToShop);
        goToShop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "TODO: show the list of shops", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                launchShopListing(view);
            }
        });

        FloatingActionButton toAddProduct = (FloatingActionButton) findViewById(R.id.toAddProduct);
        toAddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchProductNewActivity(view);
            }
        });

        /* Prepare the data link */
        products = Products.getInstance().setContext(this);
        Categories categories = Categories.getInstance().setContext(this);
        Shops shops = Shops.getInstance().setContext(this);

        /* Prepare the widget view */
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adaptor = new Adaptor4Products(this, products);
        recyclerView.setAdapter(adaptor);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adaptor.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the corresponding product and invert its "bought"ability
                Product product = products.elementByPosition(recyclerView.getChildAdapterPosition(view));
                product.setBuy(!product.getBuy());
                //get the checkbox to modify what it shows
                CheckBox toBeBought = (CheckBox) view.findViewById(R.id.productCheckBox);
                toBeBought.setChecked(product.getBuy());
                products.modify(product);
            }
        });
        adaptor.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Product product = products.elementByPosition(recyclerView.getChildAdapterPosition(view));
                launchProductModifyActivity(view, product);
                return true;
            }
        });
        updateProductsList(); // FIXME
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds products to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

//        if (id == R.id.action_importexport) {
//            Log.d("MainActivity", "Import/export");
//            launchImportExport(null);
//            return true;
//        }

        if (id == R.id.action_refresh) {
            Log.d(TAG, "Refresh");
            updateProductsList();
            return true;
        }
        if (id == R.id.action_importexport) {
            Log.d(TAG, "ImportExport");
            launchImportExport(null);
            return true;
        }
        if (id == R.id.action_categories) {
            Log.d(TAG, "Categories");
            launchCategories(null);
            return true;
        }
        if (id == R.id.action_shops) {
            Log.d(TAG, "Shops");
            launchShops(null);
            return true;
        }
        if (id == R.id.action_settings) {
            Log.d(TAG, "Settings");
            launchSettings(null);
            return true;
        }
        if (id == R.id.action_license) {
            Log.d(TAG, "License");
            launchLicense(null);
            return true;
        }
        if (id == R.id.action_about) {
            Log.d(TAG, "About");
            launchAboutActivity(null);
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    /* actions area */
    public void launchSettings(View view){
        Intent intent = new Intent(this, SettingsActivity.class);
        Log.d(TAG, "launchSettings()");
        startActivity(intent);
    }

    public void launchAboutActivity(View view){
        Intent intent = new Intent(this, AboutActivity.class);
        Log.d(TAG, "launchAboutActivity()");
        startActivity(intent);
    }

    public void launchLicense(View view) {
        Intent intent = new Intent(this, LicenseActivity.class);
        Log.d(TAG, "launchLicense()");
        startActivity(intent);
    }

    public void launchProductNewActivity(View view) {
        Intent intent = new Intent(this, ProductNewActivity.class);
        Log.d(TAG, "launchProductnewActivity()");
        startActivityForResult(intent, NEW_PRODUCT_RESULT);
    }

    public void launchProductModifyActivity(View view, Product product) {
        Intent intent = new Intent(this, ProductModifyActivity.class);
        intent.putExtra("id", product.getId());
        intent.putExtra("name", product.getName());
        intent.putExtra("buy", product.getBuy());
        intent.putExtra("category", product.getCategoryId());
        intent.putExtra("howmany", product.getHowMany());
        Log.d(TAG, "launchProductModifyActivity()");
        startActivityForResult(intent, MODIFY_PRODUCT_RESULT);
    }

    public void launchImportExport(View view) {
        Intent intent = new Intent(this, ImportExportActivity.class);
        Log.d(TAG, "launchImportExport()");
        startActivity(intent);
    }

    public void launchCategories(View view) {
        Intent intent = new Intent(this, CategoriesActivity.class);
        Log.d(TAG, "launchCategories()");
        startActivity(intent);
    }

    public void launchShops(View view) {
        Intent intent = new Intent(this, ShopsActivity.class);
        Log.d(TAG, "launchShops()");
        startActivity(intent);
    }

    public void launchShopListing(View view) {
        Intent intent = new Intent(this, ShopListingActivity.class);
        Log.d(TAG, "launchShopListing()");
        startActivityForResult(intent, SHOP_SELECTED);
    }

    public void launchGoShopping(Integer shopId) {
        Intent intent = new Intent(this, ShoppingActivity.class);
        Log.d(TAG, "launchGoShopping("+shopId+")");
        intent.putExtra("ShopId", shopId);
        startActivity(intent);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == NEW_PRODUCT_RESULT ) {
            Log.d(TAG, "end ProductNew");
            updateProductsList(/*data*/);
        } else if ( requestCode == MODIFY_PRODUCT_RESULT ) {
            Log.d(TAG, "end ProductModify");
            updateProductsList(/*data*/);
        } else if ( requestCode == SHOP_SELECTED ) {
            if ( data != null ){
                if ( data.getExtras() != null ) {
                    Integer shopId = data.getExtras().getInt("ShopId");
                    Log.d(TAG, "shop selected: " + shopId);
                    launchGoShopping(shopId);
                }
            }
        }
    }

    /* Data area*/
    private void updateProductsList()
    {
        Log.d(TAG, "UpdateProductsList()");
        adaptor.notifyDataSetChanged();
        adaptor.updateDate();
    }
}
