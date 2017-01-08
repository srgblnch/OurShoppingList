# OurShoppingList (for Android)

This is mi first Android App and a reencouter with Java after almost two decades. It comes from the fact that I didn't find apps to maintain a shopping list that acts like I want. I thought I could learn how to do one and do the features I like be myself.

## Features

- [x] Main screen with alphabetical check list of products
  - [x] New product Action button
    - [ ] List product names starting like what the user is typing
  - [ ] Group them in categories (expandable)
  - [x] Short click flip "buy" tick
  - [x] Long click edit the product
    - [x] Allow to set the product category
    - [x] Allow to set the shops where it can be bought
    - [x] Select how many should be bought
    - [x] Allow to store a modified product as well as delete it
    - [ ] Feature to have products to be bought only once (removed once bought)
    - [ ] Product picture
- [x] Categories screen alphabetically listed
  - [ ] Show products on each category
  - [ ] Allow quick selection of products to be set to the given category
  - [ ] When a category is deleted, reassign its products as "Unclassified"
- [x] Shops screen alphabetically listed
  - [ ] Allow quick selection of products to be assigned to the given shop
  - [ ] Allow to modify positions of the products for the given shop
  - [ ] Unassign products when a shop is deleted
- [x] "In the shop" Action button
  - [x] Select the shop where one is going to buy
  - [ ] Feature to auto-position based on the ongoing products loaded to the cart
- [ ] Import/Export the SQLite to csv files
  - Columns: productName\tbuy\thowmany\tcategoryName\tshop,position;shop,position;...
    - Example: Aigua\tTrue\t3\tAlimentació\tEroski,3;Caprabo,0
- [ ] Multiple device synchronization
  - [ ] Via Bluetooth
  - [ ] Via a server
    - [ ] Allow users to share the list of products between them
  - [ ] Peer2peer

## Internal development

- [ ] unify the Adaptor classes
