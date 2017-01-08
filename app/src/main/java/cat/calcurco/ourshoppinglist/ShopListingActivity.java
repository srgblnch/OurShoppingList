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
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import OurShoppingListObjs.Shop;
import OurShoppingListObjs.Shops;

import static cat.calcurco.ourshoppinglist.MainActivity.SHOP_SELECTED;

public class ShopListingActivity extends AppCompatActivity {
    final static String TAG = "ShopListingActivity";

    protected Shops shops;
    protected Shop selected;
    protected Intent thisIntent;

    private RecyclerView recyclerView;
    public Adaptor4Shops adaptor;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shop_listing);

        thisIntent = this.getIntent();

        shops = Shops.getInstance();

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adaptor = new Adaptor4Shops(this, shops);
        recyclerView.setAdapter(adaptor);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adaptor.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the corresponding item and invert its "bought"ability
                selected = shops.elementByPosition(recyclerView.getChildAdapterPosition(view));
//                Snackbar.make(view, "Shop Selected: "+selected.getName(), Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                Log.d(TAG, "Shop selected: "+selected.getName()+" ("+selected.getId()+")");
                thisIntent.putExtra("ShopId", selected.getId());
                setResult(SHOP_SELECTED, thisIntent);
                finish();
            }
        });
    }

}
