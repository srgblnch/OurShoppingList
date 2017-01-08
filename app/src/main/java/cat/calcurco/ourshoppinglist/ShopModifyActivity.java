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

import OurShoppingListObjs.Shop;
import OurShoppingListObjs.Shops;

/**
 * Created by serguei on 05/10/16.
 */

public class ShopModifyActivity extends ShopNewActivity {
    final static String TAG = "ShopModifyActivity";

    protected Integer id;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle extras = getIntent().getExtras();
        id = extras.getInt("id");
        shopText.setText(extras.getString("name"));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.product_modify, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem shop) {
        switch (shop.getItemId()) {
            case R.id.action_modify:
                Shop newShop = saveShop();
                if ( newShop != null ) {
                    Log.d(TAG, "Modified: "+newShop + "(" + newShop.getId() + ")");
                    finish();
                    return true;
                }
                Log.w(TAG, "NOT MODIFIED: "+newShop);
                return false;
            case R.id.action_delete:
                requestConfirmation();
                return true;
            default:
                return super.onOptionsItemSelected(shop);
        }
    }

    protected Shop saveShop() {
        String name = shopText.getText().toString();
        try {
            Shops shops = Shops.getInstance();
            Shop shop = shops.elementById(id);
            shop.setName(name);
            return shop;
        } catch ( Exception e ) {
            return null;
        }
    }

    protected void requestConfirmation() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setIcon(android.R.drawable.ic_dialog_alert)
                .setTitle("Delete action")
                .setMessage("Are you sure you want delete this item?")
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
                    deleteShop();
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    Log.i(TAG, "Delete cancelled");
                    break;
            }
        }
    };

    protected void deleteShop() {
        Shops.getInstance().remove(id);
        finish();
    }
}

