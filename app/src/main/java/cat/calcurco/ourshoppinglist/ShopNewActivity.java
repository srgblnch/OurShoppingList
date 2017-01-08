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

import OurShoppingListObjs.Shop;
import OurShoppingListObjs.Shops;

/**
 * Created by serguei on 05/10/16.
 */

public class ShopNewActivity extends AppCompatActivity {
    final static String TAG = "ShopNewActivity";

    protected EditText shopText;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.shop_edit);

        shopText = (EditText) findViewById(R.id.shopName);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_add, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem shop) {
        switch (shop.getItemId()) {
            case R.id.action_accept:
                Shop newShop = saveShop();
                if ( newShop != null ) {
                    Log.d(TAG, "Saved "+newShop + "(" + newShop.getId() + ")");
                    finish();
                    return true;
                }
                Log.w(TAG, "NOT Saved "+newShop);
                return false;
            case R.id.action_cancel:
                Log.d(TAG, "Cancelled");
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(shop);
        }
    }

    protected Shop saveShop() {
        String name = shopText.getText().toString();

        Log.d(TAG, "saveShop("+name+")");
        try {
            Shop obj = new Shop(name);
            Shops.getInstance().add(obj);
            Log.d(TAG, "saveShop: "+obj);
            return obj;
        } catch ( Exception e ) {
            Log.d(TAG, "saveShop Exception: "+e);
            return null;
        }
    }
}
