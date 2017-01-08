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

/**
 * Created by serguei on 25/08/16.
 */

public interface OurLists {
    OurShoppingListObj elementById(Integer id); // Return an Object by its id
    OurShoppingListObj elementByPosition(Integer id); // Return an Object by its position in the container
    OurShoppingListObj elementByName(String name);  // Return an Object by its position in the container
    Integer add(OurShoppingListObj obj) throws Exception;  // Return the id of the inserted Object
    boolean modify(OurShoppingListObj obj);  // Modifies the object
    boolean remove(Integer id); // Removes the Object with the given id
    int size(); // Returns the number of elements
}
