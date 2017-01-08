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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import OurShoppingListObjs.Categories;
import OurShoppingListObjs.Category;

public class CategoriesActivity extends AppCompatActivity {
    final static String TAG = "CategoriesActivity";
    final static int NEW_CATEGORY_RESULT = 1;
    final static int MODIFY_CATEGORY_RESULT = 2;

    protected Categories categories;

    private RecyclerView recyclerView;
    public Adaptor4Categories adaptor;
    private RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_categories);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton toAddCategory = (FloatingActionButton) findViewById(R.id.toAddCategory);
        toAddCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchCategoryNewActivity(view);
            }
        });

        categories = Categories.getInstance();
        /* Prepare the widget view */
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        adaptor = new Adaptor4Categories(this, categories);
        recyclerView.setAdapter(adaptor);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        adaptor.setOnItemClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //get the corresponding product and invert its "bought"ability
                Category category = categories.elementByPosition(recyclerView.getChildAdapterPosition(view));
            }
        });
        adaptor.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                Category category = categories.elementByPosition(recyclerView.getChildAdapterPosition(view));
                launchCategoryModifyActivity(view, category);
                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds products to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_categories, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_refresh) {
            Log.d(TAG, "Refresh");
            updateProductsList();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    public void launchCategoryNewActivity(View view) {
        Intent intent = new Intent(this, CategoryNewActivity.class);
        Log.d(TAG, "launchCategoryNewActivity()");
        startActivityForResult(intent, NEW_CATEGORY_RESULT);
    }

    public void launchCategoryModifyActivity(View view, Category category) {
        Intent intent = new Intent(this, CategoryModifyActivity.class);
        intent.putExtra("id", category.getId());
        intent.putExtra("name", category.getName());
        Log.d(TAG, "launchCategoryModifyActivity()");
        startActivityForResult(intent, MODIFY_CATEGORY_RESULT);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if ( requestCode == NEW_CATEGORY_RESULT ) {
            Log.d(TAG, "end CategoryNew");
            updateProductsList(/*data*/);
        } else if ( requestCode == MODIFY_CATEGORY_RESULT) {
            Log.d(TAG, "end CategoryModify");
            updateProductsList(/*data*/);
        }
    }

    /* Data area*/
    private void updateProductsList()
    {
        Log.d(TAG, "UpdateCategoriesList()");
        adaptor.notifyDataSetChanged();
        adaptor.updateDate();
    }
}
