# OurShoppingList (for Android)

This is my first Android App and a reencouter with Java after almost two decades. This _toy-project_ starts from the idea that "_If there isn't apps that does the things the way I like, I'll propose one_", and share it as free software. For any user be sure the app doesn't do anything unwanted (like a dark usage of the information, including any statistics) the free software license is perfect; the sources can be read by anyone to be sure it does what it says. If there is some feature or behaviour that some one like to have it different, the license allow to do it always sharing back the contribution.

![license GPLv3+](https://img.shields.io/badge/license-GPLv3+-green.svg)
![2 - Pre-Alpha](https://img.shields.io/badge/Development_Status-2_--_pre--alpha-orange.svg)

## Base features

- [x] Main screen with alphabetical check list of _products_, when one likes to review what has to be bought.
- [x] "in the shop" activity to use when one is buying.
  - [x] Select the _shop_ where one is going to buy.
- [ ] Sinchronization between a set of devices.

### Main Activity

- [x] New _products_ action button.
- [x] Short click: flip "buy" tick.
- [x] Long click: edit the _product_ (like the new product view).
- [x] Special actions menu.
  - [x] Activity to manage _categories_.
  - [x] Activity to manage _shops_.
- [ ] Group products in _categories_.

### New/Modify Product Activity

- [ ] Auto-complete: While introducing a new name, check in the current known _products_ if any contains this substring.
- [x] Set _product_ to a _category_.
  - [ ] Direct creation of a new _category_.
- [x] Set the _shops_ where the _product_ can be bought.
  - [ ] Direct creation of a new _shop_.
- [x] Select how many should be bought.
- [x] Store a modified _product_ (and also option to delete).
  - TODO: decide if autosave or cancel when control-back button.
- [ ] Special _product_ types "to be bought only once" ([logical] remove once bought).
- [ ] Products pictures.
- [ ] Barcode scan.
- [ ] Priority of a _product_ to be bought (to provide info to "_ring the bell_" if one should go to a certain shop soon).
- [ ] _Product_ aliases (multiple ways to call what represent to be the same product, useful when search available).

### New/Modify Category Activity

- [x] Alphabetical list of _categories_.
- [ ] Show the _products_ on a given _category_.
- [ ] Quick selection of _products_ to be included to a _category_.
- [ ] When _category_ is deleted, reassign its _products_ as "_Unclassified_".

### New/Modify Shop Activity

- [x] Alphabetical list of _shops_.
- [ ] Quick selection of _products_ to be assigned to a _shop_.
  - [ ] Copy _products_ from another _shop_.
  - [ ] Assign _products_ from a given _category_.
- [ ] Modify _product_ positions within a _shop_ (drag&drop).
- [ ] Unassign _products_ when a _shop_ is deleted.
  - TODO: solve what to do with _products_ that doesn't have any _shop_.
- [x] Information about "_products_ to be bought"/"_products_ assign to the _shop_".
  - [ ] Ponderation based on the _product_ priorities.

### "In the shop" activity

- [ ] Show only _products_ to be bought (and hide them while are added to the shopping cart").
- [ ] Auto-position based on the ongoing _products_ loaded to the cart.
- [ ] Slice _product_ to tag as "today is not in the _shop_" (instead of "in the cart").
  - [ ] Perhaps also to fastly remove from this _shop_.
- [ ] Maintain the application above the screen lock while buying.

### Import/Export activity

- [ ] Directory and file chooser.
- [ ] Option to remove all (or selective) elements of the _Products_, _Categories_ and _Shops_.
- [ ] Progress bar while doing the import/export operation.

## Internal features

- [x] SQLite internal database.
  - [ ] Import/Export.
    - [x] csv
      - Columns: productName\tbuy\thowmany\tcategoryName\tshop,position;shop,position;...
         - Example: Product\tTrue\t3\tLebensmittel\tFleischerei,0;Supermarkt,3
    - [ ] json
  - [ ] Transactions (like log what is being made, thought for the sync with other devices later on).
- [ ] Multiple device synchronization.
  - [ ] Via Bluetooth.
  - [ ] Via a server.
    - [ ] Allow users to share the list of _products_ between them.
  - [ ] Peer2peer (perhaps over MQTT).
    - [ ] Trusted discovery of partners.
  - [ ] Users shall have control about when the application will synchronise between other application instances.
- [ ] History of _products_ (statistics with special enfasis on privacy, this must be information only for the owners of the list).
  - [ ] When they where bought (frequency, priority).
    - [ ] Price log (evolution plot).

## Internal pending improvements

- [ ] Unify the Adaptor classes.
- [x] Split the OurShoppingListDB class.
  - [x] Review and bugfix the rawQuery usage (names with apostrophe).
  - [ ] Replace rawQuery(...) by query(...) (and insert(...), update(...), delete(...)).
- [ ] Prevent names (any OurShoppingListObj) with ctrl characters (like \t or \n), but allow language specific simbols (like accents, dieresis).
- [ ] Avoid extension hardcoding in the import/export.
- [ ] Separate the line generation in csv export, to be reused in the transaction content.
- [ ] Sort in the csv export by name and not by id.

### Known issues

- [ ] The application is too slow when managing a list of ~150 products

