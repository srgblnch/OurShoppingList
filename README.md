# OurShoppingList (for Android)

This is my first Android App and a reencouter with Java after almost two decades. This _toy-project_ starts from the idea that "_If there isn't apps that does the things the way I like, I'll propose one_", and share it as free software. For any user be sure the app doesn't do anything unwanted (like a dark usage of the information, including any statistics) the free software license is perfect; the sources can be read by anyone to be sure it does what it says. If there is some feature or behaviour that some one like to have it different, the license allow to do it always sharing back the contribution.

![license GPLv3+](https://img.shields.io/badge/license-GPLv3+-green.svg)
![2 - Pre-Alpha](https://img.shields.io/badge/Development_Status-2_--_pre--alpha-orange.svg)

## Base features

- [x] Main screen with alphabetical check list of products, when one likes to review what has to be bought.
- [x] "in the shop" activity to use when one is buying.
  - [x] Select the shop where one is going to buy
- [ ] Sinchronization between a set of devices

### Main Activity

- [x] New products action button
- [x] Short click: flip "buy" tick.
- [x] Long click: edit the product (like the new product view)
- [x] special actions menu
  - [x] Activity to manage _categories_
  - [x] Activity to manage _shops_
- [ ] Group products in _Categories_

### New/Modify Product Activity

- [ ] Auto-complete: While introducing a new name, check in the current known products if any contains this substring
- [x] Set product category
  - [ ] Direct creation of a new _category_
- [x] set the shops where the product can be bought
  - [ ] Direct creatiob of a new _shop_
- [x] Select how many should be bought
- [x] store a modified product (and also option to delete)
  - TODO: decide if autosave or candel when control-back button
- [ ] Special product types "to be bought inly once" ([logical] remove once bought)
- [ ] Products pictures
- [ ] Barcode scan
- [ ] Priority of a product to be bought (to provide info to ring the bell if one should go to a certain shop soon).

### New/Modify Category Activity

- [x] Alphabetical list of categories
- [ ] Show the products on a given category
- [ ] Quick selection of products to be included to a category
- [ ] When category is deleted, reassign its products as "Unclassified"

### New/Modify Shop Activity

- [x] Alphabetical list of shops
- [ ] Quick selection of products to be assigned to a shop
  - [ ] Copy products from another shop
  - [ ] Assign products from a given category
- [ ] Modify product positions within a shop (drag&drop)
- [ ] Unassign products when a shop is deleted
  - TODO: solve what to do with products that doesn't have any shop
- [ ] Information about "products to be bought"/"products assign to the shop"
  - [ ] Ponderation based on the product priorities

### "In the shop" activity

- [ ] auto-position based on the ongoing products loaded to the cart
- [ ] Slice product to tag as "today is not in the shop" (instead of "in the card")
- [ ] Maintain the application above the screen lock while buying.

## Internal features

- [x] SQLite internal database
  - [ ] Import/Export
    - [x] csv files
      - Columns: productName\tbuy\thowmany\tcategoryName\tshop,position;shop,position;...
         - Example: Product\tTrue\t3\tLebensmittel\tFleischerei,0;Supermarkt,3
    - [ ] json
  - [ ] Transactions (like log what is being made, thought for the sync with other devices later on)
- [ ] Multiple device synchronization
  - [ ] Via Bluetooth
  - [ ] Via a server
    - [ ] Allow users to share the list of products between them
  - [ ] Peer2peer (perhaps over MQTT)
    - [ ] Trusted discovery of partners
- [ ] History of products (statistics with special enfasis on privacy, this must be information only for the owners of the list).
  - [ ] When they where bought (frequency, priority)
    - [ ] Price log (evolution plot)

## Internal pending improvements

- [ ] unify the Adaptor classes
- [x] Split the OurShoppingListDB class
  - [ ] review and bugfix the rawQuery usage.
- [ ] Prevent names (any OurShoppingListObj) with ctrl characters (like \t or \n), but allow language specific simbols (like accents, dieresis)
- [ ] avoid extension hardcoding in the import/export
- [ ] separate the line generation in csv export, to be reused in the transaction content.
- [ ] Sort in the csv export by name and not by id.
- [ ] InShopping activity, show only the products to be bought.

