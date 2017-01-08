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

import java.util.Vector;

import OurShoppingListDataBase.DatabaseSingleton;
import OurShoppingListObjs.Product;
import OurShoppingListObjs.Products;
import OurShoppingListObjs.Shop;

/**
 * Created by serguei on 11/10/16.
 */

public class Adaptor4ProductsInShop extends
        RecyclerView.Adapter<Adaptor4ProductsInShop.ViewHolder> {
    final static String TAG = "Adaptor4ProductsInShop";
    protected Shop shop;
    protected Vector<Product> products;

    protected LayoutInflater inflater;  // Build layout from XML
    protected Context context;  // needed by the inflator

    protected View.OnClickListener onClickListener;
//    protected View.OnLongClickListener onLongClickListener;

    public Adaptor4ProductsInShop(Context context, Shop shop) {
        Log.d(TAG, "Adaptor4ProductsInShop("+context+", "+shop+")");
        this.context = context;
        this.shop = shop;
        this.products = new Vector<Product>();

        Vector<String> productsInShop = DatabaseSingleton.getInstance().getShopProducts(shop);
        Log.d(TAG, "products to buy in "+shop.getName()+": "+productsInShop);
        Product product;

        int prevPos = 0;
        for (String productName: productsInShop) {
            product = new Product(productName);
            Log.d(TAG, "Build "+product.getName());
            if ( product.getPoitionInShop(shop) <= prevPos) {
                product.setPoitionInShop(shop, prevPos+1);
                prevPos += 1;
            }
            // TODO: only products to be bought

            products.add(product);

        }
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public CheckBox toBeBought;
        //public ImageView foto;

        public ViewHolder(View productView) {
            super(productView);
            productName = (TextView) productView.findViewById(R.id.header);
            toBeBought = (CheckBox) productView.findViewById(R.id.productCheckBox);
            //foto = (ImageView) productView.findViewById(R.id.foto);
        }
    }
    // Build the ViewHolder with a view to a non initialized element
    @Override
    public Adaptor4ProductsInShop.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos la vista desde el xml
        View v = inflater.inflate(R.layout.product_view, parent, false);
        v.setOnClickListener(onClickListener);
//        v.setOnLongClickListener(onLongClickListener);
        return new Adaptor4ProductsInShop.ViewHolder(v);
    }

    // Using the ViewHolder as basement, populate it
    @Override
    public void onBindViewHolder(Adaptor4ProductsInShop.ViewHolder holder, int position) {
        Product product = products.get(position);
        populateView(holder, product);
        Log.i("Adaptor4ProductsInShop", "onBindViewHolder, position " + position
                + " with "+  product.getName() + " (id:" + product.getId() + ")");
    }

    public void populateView(Adaptor4ProductsInShop.ViewHolder holder, Product product) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.context);
        boolean debugFlag = sharedPref.getBoolean(SettingsFragment.KEY_PREF_DEBUG_FLAG, false);

        String name = product.getName();
        Integer howmany = product.getHowMany();
        if (howmany > 1) {
            name = howmany+" "+name;
        }
        if ( debugFlag ) {
            name = name+" (id:" + product.getId() + ") position: " + product.getPoitionInShop(shop);
        }
        holder.productName.setText(name);
        holder.toBeBought.setChecked(product.getBuy());
    }

    public void updateDate(){
        Log.d("Adaptor4ProductsInShop", "updateDate");
        notifyDataSetChanged();
    }

    // Number of elements in the list
    @Override public int getItemCount() {
        if ( products != null ) {
            return products.size();
        }
        return 0;
    }

    public void setOnItemClickListener(View.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

//    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener){
//        this.onLongClickListener = onLongClickListener;
//    }

    protected Product elementByPosition(int position){
        return products.get(position);
    }
}
