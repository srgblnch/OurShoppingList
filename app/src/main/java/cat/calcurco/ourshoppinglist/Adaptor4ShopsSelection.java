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

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import OurShoppingListObjs.Product;
import OurShoppingListObjs.Shop;
import OurShoppingListObjs.Shops;

/**
 * Created by serguei on 05/10/16.
 */

public class Adaptor4ShopsSelection extends
        RecyclerView.Adapter<Adaptor4ShopsSelection.ViewHolder> {
    final static String TAG = "Adaptor4ShopsSelection";
    protected Shops shops;
    protected Product product;
    protected LayoutInflater inflater;  // Build layout from XML
    protected Context context;  // needed by the inflator

    protected View.OnClickListener onClickListener;

    public Adaptor4ShopsSelection(Context context, Shops shops, Product product) {
        this.context = context;
        this.shops = shops;
        this.product = product;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView shopName;
        public CheckBox selected;

        public ViewHolder(View productView) {
            super(productView);
            shopName = (TextView) productView.findViewById(R.id.header);
            selected = (CheckBox) productView.findViewById(R.id.shopCheckBox);
        }
    }
    // Build the ViewHolder with a view to a non initialized element
    @Override
    public Adaptor4ShopsSelection.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos la vista desde el xml
        View v = inflater.inflate(R.layout.shop_selection, parent, false);
        v.setOnClickListener(onClickListener);
        return new Adaptor4ShopsSelection.ViewHolder(v);
    }

    // Using the ViewHolder as basement, populate it
    @Override
    public void onBindViewHolder(Adaptor4ShopsSelection.ViewHolder holder, int position) {
        Shop shop = shops.elementByPosition(position);
        populateView(holder, shop);
        Log.i(TAG, "onBindViewHolder, position " + position
                + " with "+  shop.getName() + " (id:" + shop.getId() + ")");
    }

    public void populateView(Adaptor4ShopsSelection.ViewHolder holder, Shop shop) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.context);
        boolean debugFlag = sharedPref.getBoolean(SettingsFragment.KEY_PREF_DEBUG_FLAG, false);

        String name = shop.getName();
        if ( debugFlag ) {
            name = name+" (id:" + shop.getId() + ")";
        }
        holder.shopName.setText(name);
        holder.selected.setChecked(product.isInShop(shop));
    }

    public void updateDate(){
        Log.d(TAG, "updateDate");
        notifyDataSetChanged();
    }

    // Number of elements in the list
    @Override public int getItemCount() {
        if ( shops != null ) {
            return shops.size();
        }
        return 0;
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }
}
