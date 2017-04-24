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

package OurShoppingListObjs;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import OurShoppingListDataBase.OurData;

/**
 * Created by serguei on 25/08/16.
 * Any product created will be included in the list (singleton) of all known products once its name is
 * assigned.
 * The products can have 0 to many shops assigned.
 */

public class Product extends OurShoppingListObj implements Comparable<Product> {
    final static String TAG = "Product";

    protected boolean buy;
    protected Integer category = 0;
    protected Integer howmany = 1;
    /* TODO: other information like how many or store an small photo. */
//    protected List<Shop> shops;

    /****
     * This constructor is made to recover information from the database
     */
    public Product(Integer id, SQLiteDatabase db) {
        super("");
        String query = "SELECT name, buy, category, howmany FROM Products WHERE id == "+id;
        // TODO: include other columns in the query

        Cursor cursor = db.rawQuery(query, null);
        Log.d(TAG+"()", cursor.getCount()+" Products with id "+id+" located in the database");
        if ( cursor.moveToFirst() ) {
            this.id = id;
            this.name = cursor.getString(0);
            this.buy = (cursor.getInt(1) > 0);
            this.category = cursor.getInt(2);
            String categoryName = OurData.getInstance().getCategory(this.category).getName();
            this.howmany = cursor.getInt(3);
            Log.d(TAG+"("+id+": "+name+")",
                    "buy = "+buy+", category = "+category+"("+categoryName+"), how many = "+howmany+"");
        }
        cursor.close();
        db.close();
//        Log.d("Product("+id+": "+name+")",
//                "Query "+query+" done");
        // FIXME: TBRemoved, force the howmany to be at least 1
//        if (this.howmany <= 0) {
//            this.howmany = 1;
//            OurData.getInstance().modifyProduct(this);
//        }

        //TODO: recover the shopsLst for this Product using the "Products_has_Shops" table
    }

    /****
     * This is a generic constructor that, given the name and if it exists in the database,
     * it recovers it. but is doesn't it initialise values without introducint it to the database.
     */
    public Product(String name) {
        super(name);
        OurData db = OurData.getInstance();
        if (db.isProductInDB(name)) {
            this.id = db.getProductId(name);
            Product tmp = db.getProduct(this.id);
            this.buy = tmp.getBuy();
            this.category = tmp.getCategoryId();
            this.howmany = tmp.getHowMany();
        } else {
            this.buy = false;
            this.category = 0;
            this.howmany = 1;
        }
    }

    /****
     * Constructor made to be used from the NewProduct Actvity. If the product in fact exist,
     * this constructor will modify the register acording to the information provided
     */
    public Product(String name, boolean buy, Integer category, Integer howmany) {
        // FIXME: to be extended as long as the ProductNewActivity has more widgets
        super(name);
        OurData db = OurData.getInstance();
        if (db.isProductInDB(name)) {
            Product tmp = new Product(name);
            this.buy = tmp.getBuy();
            this.category = tmp.getCategoryId();
            this.howmany = tmp.getHowMany();
        }
        this.buy = buy;
        this.category = category;
        this.howmany = howmany;
        if (db.isProductInDB(name)) {
            db.modifyProduct(this);
        }
    }

    /****
     * TODO:
     * there must be a way to catch object destruction in order to remove from its shops
     * and also from the all knonw products.
     */

    @Override
    public int compareTo(Product other) {
        int cmp = this.getName().compareToIgnoreCase(other.getName());
        Log.d(TAG+"("+id+": "+name+")",
                "Compare "+this.getName()+" with "+other.getName()+" and return "+cmp);
        return cmp;
    }

    @Override
    public String toString() {
        if ( getName().length() > 0 ){
            return getName();
        } else {
            return "Empty Product";  /* TODO: assign a string id for the translation feature */
        }
    }

    public boolean getBuy() {
        return buy;
    }

    public void setBuy(boolean value) {
        this.buy = value;
        OurData db = OurData.getInstance();
        db.modifyProduct(this);
        //Product tmp = db.getProduct(this.id);
    }

    public Integer getCategoryId() {
        return category;
    }

    public String getCategory() {
        if (category == -1) {
            return "";
        }
        return OurData.getInstance().getCategory(category).getName();
    }

    public void setCategoryId(Integer id) {
        this.category = id;
        if (id >= 0) {
            OurData.getInstance().modifyProduct(this);
        }
    }

    public void setCategory(String name) {
        Category category = OurData.getInstance().getCategoryByName(name);
        Log.d(TAG+"("+id+": "+this.name+").setCategory("+name+")",
                "category: "+category.getId()+", "+category.getName());
        if (category == null) {
            this.category = -1;
        } else {
            this.category = category.getId();
            OurData.getInstance().modifyProduct(this);
        }
    }

    public Integer getHowMany() {
        if (howmany < 1) {
            return 1;
        }
        return howmany;
    }

    public void setHowMany(Integer value) {
        if (value > 0) {
            this.howmany = value;
            OurData.getInstance().modifyProduct(this);
        } else {
            Log.w(TAG+"("+id+": "+name+")",
                    "Ignoring too small assigment to how many.");
        }
    }

//    /*************************/
//    /* Shop information area */
//    public boolean hasShop() {
//        return ( shops.size() > 0 );
//    }
//
    public boolean isInShop(Shop shop) {
        return OurData.getInstance().isProductInShop(this, shop);
    }

    public Integer getPoitionInShop(Shop shop) {
        return OurData.getInstance().getProductPositionInShop(this, shop);
    }

    public void setPoitionInShop(Shop shop, Integer position) {
        OurData.getInstance().setProductPositionInShop(this, shop, position);
    }

    public void assignShop(Shop shop) {
        OurData.getInstance().insertProductInShop(this, shop, 0);
    }

    public void unassignShop(Shop shop) {
        OurData.getInstance().removeProductInShop(this, shop);
    }
//    /* done Shop information area */
    /******************************/
}
