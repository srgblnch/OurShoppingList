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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import OurShoppingListDataBase.DatabaseSingleton;
import OurShoppingListObjs.Product;
import OurShoppingListObjs.Products;
import OurShoppingListObjs.Shop;
import OurShoppingListObjs.Shops;

/**
 * Created by serguei on 05/10/16.
 */

public class ShopsSelectionActivity extends AppCompatActivity {
    final static String TAG = "ShopsSelectionActivity";

    protected Shops shops;
    protected Product product;

    private RecyclerView recyclerView;
    public Adaptor4ShopsSelection adaptor;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        Integer id = extras.getInt("productId");
        product = Products.getInstance().elementById(id);
        Log.d(TAG, "Bundle product: "+product.getName());
        setContentView(R.layout.activity_shop_selection);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Prepare the data link */
        shops = Shops.getInstance();

        /* Prepare the widget view */
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adaptor = new Adaptor4ShopsSelection(this, shops, product);
        recyclerView.setAdapter(adaptor);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adaptor.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseSingleton db = DatabaseSingleton.getInstance();
                Shop shop = shops.elementByPosition(recyclerView.getChildAdapterPosition(view));
                CheckBox selected = (CheckBox) view.findViewById(R.id.shopCheckBox);
                boolean isProductInShop = product.isInShop(shop);
                selected.setChecked(!isProductInShop);
                if ( isProductInShop ) {
                    Log.d("ShopSelection", "Remove product "+product.getName()+" from "+shop.getName()+" shop");
                    product.unassignShop(shop);
                } else {
                    Log.d("ShopSelection", "Insert product "+product.getName()+" to "+shop.getName()+" shop");
                    product.assignShop(shop);
                }
            }
        });
        //updateShopsList(); // FIXME
    }


    /* Data area*/
    private void updateShopsList()
    {
        Log.d(TAG, "updateShopsList()");
        adaptor.notifyDataSetChanged();
        adaptor.updateDate();
    }
}

