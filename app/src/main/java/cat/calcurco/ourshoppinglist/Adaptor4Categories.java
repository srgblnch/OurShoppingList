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
import android.widget.TextView;

import OurShoppingListObjs.Categories;
import OurShoppingListObjs.Category;

/**
 * Created by serguei on 04/10/16.
 */

public class Adaptor4Categories extends
        RecyclerView.Adapter<Adaptor4Categories.ViewHolder> {
    protected Categories categories;
    protected LayoutInflater inflater;  // Build layout from XML
    protected Context context;  // needed by the inflator

    protected View.OnClickListener onClickListener;
    protected View.OnLongClickListener onLongClickListener;

    public Adaptor4Categories(Context context, Categories categories) {
        this.context = context;
        this.categories = categories;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //Creamos nuestro ViewHolder, con los tipos de elementos a modificar
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView categoryName;

        public ViewHolder(View productView) {
            super(productView);
            categoryName = (TextView) productView.findViewById(R.id.header);
        }
    }
    // Build the ViewHolder with a view to a non initialized element
    @Override
    public Adaptor4Categories.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflamos la vista desde el xml
        View v = inflater.inflate(R.layout.category_view, parent, false);
        v.setOnClickListener(onClickListener);
        v.setOnLongClickListener(onLongClickListener);
        return new Adaptor4Categories.ViewHolder(v);
    }

    // Using the ViewHolder as basement, populate it
    @Override
    public void onBindViewHolder(Adaptor4Categories.ViewHolder holder, int position) {
        Category category = categories.elementByPosition(position);
        populateView(holder, category);
        Log.i("Adaptor4Categories", "onBindViewHolder, position " + position
                + " with "+  category.getName() + " (id:" + category.getId() + ")");
    }

    public void populateView(Adaptor4Categories.ViewHolder holder, Category category) {
        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(this.context);
        boolean debugFlag = sharedPref.getBoolean(SettingsFragment.KEY_PREF_DEBUG_FLAG, false);

        String name = category.getName();
        if ( debugFlag ) {
            name = name+" (id:" + category.getId() + ")";
        }
        holder.categoryName.setText(name);
    }

    public void updateDate(){
        Log.d("Adaptor4Categories", "updateDate");
        notifyDataSetChanged();
    }

    // Number of elements in the list
    @Override public int getItemCount() {
        if ( categories != null ) {
            return categories.size();
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
