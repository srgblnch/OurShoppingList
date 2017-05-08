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
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import OurShoppingListObjs.Product;
import OurShoppingListObjs.Products;

/**
 * Created by serguei on 26/08/16.
 * TODO: long click request confirmation to delete
 */

public class Adaptor4Products extends
        RecyclerView.Adapter<Adaptor4Products.ViewHolder> {
    protected Products products;
    protected LayoutInflater inflater;  // Build layout from XML
    protected Context context;  // needed by the inflator

    protected View.OnClickListener onClickListener;
    protected View.OnLongClickListener onLongClickListener;

    public Adaptor4Products(Context context, Products products) {
        this.context = context;
        this.products = products;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView productName;
        public CheckBox toBeBought;
        //public ImageView foto;
        public Button decrease;
        public Button increase;

        public ViewHolder(View productView) {
            super(productView);
            productName = (TextView) productView.findViewById(R.id.header);
            toBeBought = (CheckBox) productView.findViewById(R.id.productCheckBox);
            //foto = (ImageView) productView.findViewById(R.id.foto);
            decrease = (Button) productView.findViewById(R.id.decrease);
            increase = (Button) productView.findViewById(R.id.increase);
        }
    }
    // Build the ViewHolder with a view to a non initialized element
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos la vista desde el xml
        View v = inflater.inflate(R.layout.product_view, parent, false);
        v.setOnClickListener(onClickListener);
        v.setOnLongClickListener(onLongClickListener);
        return new ViewHolder(v);
    }

    // Using the ViewHolder as basement, populate it
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = products.elementByPosition(position);
        populateView(holder, product);
        Log.i("Adaptor4Products", "onBindViewHolder, position " + position
                + " with "+  product.getName() + " (id:" + product.getId() + ")");
    }

    public void populateView(ViewHolder holder, final Product product) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.context);
        boolean debugFlag = sharedPref.getBoolean(SettingsFragment.KEY_PREF_DEBUG_FLAG, false);

        String name = product.getName();
        Integer howmany = product.getHowMany();
        if (howmany > 1) {
            name = howmany+" "+name;
        }
        if ( debugFlag ) {
            name = name+" (id:" + product.getId() + ")";
        }
        holder.productName.setText(name);
        holder.toBeBought.setChecked(product.getBuy());
        holder.decrease.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                if ( product.getHowMany() > 1 ) {
                    product.setHowMany(product.getHowMany()-1);
                    product.setBuy(true);
                } else {
                    product.setBuy(false);
                }
                updateDate();
            }
        });
        holder.increase.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {
                product.setHowMany(product.getHowMany()+1);
                product.setBuy(true);
                updateDate();
            }
        });
    }

    public void updateDate(){
        Log.d("Adaptor4Products", "updateDate");
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

    public void setOnLongClickListener(View.OnLongClickListener onLongClickListener){
        this.onLongClickListener = onLongClickListener;
    }
}
