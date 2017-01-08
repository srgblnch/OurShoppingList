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

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;

import OurShoppingListObjs.Product;
import OurShoppingListObjs.Products;
import OurShoppingListObjs.Shop;
import OurShoppingListObjs.Shops;

public class ShoppingActivity extends AppCompatActivity {
    final static String TAG = "ShoppingActivity";

    protected Shop shop;

    private RecyclerView recyclerView;
    public Adaptor4ProductsInShop adaptor;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shopping);
        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            Integer shopId = extras.getInt("ShopId");
            Log.d(TAG, "Intent shopId: "+shopId);
            shop = Shops.getInstance().elementById(shopId);
            Log.d(TAG, "Corresponds to "+shop.getName()+" shop");
            setTitle(shop.getName());
        }


        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adaptor = new Adaptor4ProductsInShop(this, shop);
        recyclerView.setAdapter(adaptor);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adaptor.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the corresponding product and invert its "bought"ability
                Product product = adaptor.elementByPosition(recyclerView.getChildAdapterPosition(view));
                product.setBuy(!product.getBuy());
                //get the checkbox to modify what it shows
                CheckBox toBeBought = (CheckBox) view.findViewById(R.id.productCheckBox);
                toBeBought.setChecked(product.getBuy());
                Products.getInstance().modify(product);
            }
        });
    }
}
