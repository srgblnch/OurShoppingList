# OurShoppingList (for Android)

This is mi first Android App and a reencouter with Java after almost two decades. This toy-project starts from the idea that "If there isn't apps that does the things the way I like, do your one", and share it as free software.

This project is under the GPLv3+ License.

## Features

- [x] Main screen with alphabetical check list of products
  - [x] New product Action button
    - [ ] List product names starting like what the user is typing
  - [ ] Group products into categories (expandable widget)
  - [x] Short click: flip "buy" tick
  - [x] Long click: edit the product (like the new product view)
    - [x] Allow to set the product category
    - [x] Allow to set the shops where it can be bought
    - [x] Select how many should be bought
    - [x] Allow to store a modified product as well as delete it
    - [ ] Feature to have products to be bought only once (removed once bought)
    - [ ] Product picture
  - [ ] Products to be bought only once
  - [ ] Barcode scan
- [x] Categories screen alphabetically listed
  - [ ] Show products on each category (maybe short click)
  - [ ] Allow quick selection of products to be set to the given category (while in show products in the category).
  - [ ] When a category is deleted, reassign its products as "Unclassified"
- [x] Shops screen alphabetically listed
  - [ ] Allow quick selection of products to be assigned to the given shop
  - [ ] Allow to modify positions of the products for the given shop
  - [ ] Unassign products when a shop is deleted
    - [ ] List products that hasn't a shop assign
  - [ ] Info of "products to be bought"/"products assign to the shop"
- [x] "In the shop" Action button
  - [x] Select the shop where one is going to buy
  - [ ] Feature to auto-position based on the ongoing products loaded to the cart
- [ ] Import/Export the SQLite
  - [ ] csv files
    - Columns: productName\tbuy\thowmany\tcategoryName\tshop,position;shop,position;...
       - Example: Aigua\tTrue\t3\tAlimentació\tEroski,3;Caprabo,0
  - [ ] json
- [ ] Multiple device synchronization
  - [ ] Via Bluetooth
  - [ ] Via a server
    - [ ] Allow users to share the list of products between them
  - [ ] Peer2peer (perhaps over MQTT)
- [ ] History of products (statistics with special enfasis on privacy, this must be information only for the owners of the list).
  - [ ] When they where bought
    - [ ] Price log

## Internal pending improvements

- [ ] unify the Adaptor classes
