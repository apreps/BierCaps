# BierCaps

An Android application developed to ease the management of beer caps collection.

### Status
The application is functional but some improvements are being implemented.

## Available Features

- **Quickly add a new beer cap and visualize caps in the collection**. The main page of the application shows a scrolling gridview with all the items of the collection and a fixed top button to quickly add a new item to the collection. Searching the database is also available in the main page.

![BierCaps main page](https://raw.github.com/apreps/BierCaps/master/screenshots/main-page.png)

- **Add a new beer cap to the collection.** 
Basic information like the name, country of origin, some notes and a rating can be inserted. None of the fields is mandatory, therefore you can edit the information after adding the cap to the collection.

![BierCaps add cap](https://raw.github.com/apreps/BierCaps/master/screenshots/add-new-beer.png)

- **Remove beer cap.** This can be done by simply long press the image of the cap. A warning message is displayed to confirm the action.

![BierCaps remove cap](https://raw.github.com/apreps/BierCaps/master/screenshots/warning-remove.png)

- **Edit beer cap.** After adding a cap, it is possible to change all the basic information by pressing "Edit" while in the beer cap information page. If the user accidentaly hits the "Go Back" button while editing, a warning is displayed to validate the action.

![BierCaps - edit option](https://raw.github.com/apreps/BierCaps/master/screenshots/edit-beer-option.png) ![BierCaps - edit beer cap](https://raw.github.com/apreps/BierCaps/master/screenshots/warning.png)

- **Search database.** Easily search the collection by hitting the search button (magnifying glass) on the top of the main page.

![BierCaps - search database](https://raw.github.com/apreps/BierCaps/master/screenshots/main-page-search.png)   ![BierCaps - search database](https://raw.github.com/apreps/BierCaps/master/screenshots/search-result.png)

- **Export and import database;.** It is possible to export the database to the memory of the device, as well as, import a database to the application. The database file to be imported should be placed in the device's root memory (card or internal).

![BierCaps - import/export database option](https://raw.github.com/apreps/BierCaps/master/screenshots/import-export-option.png)  ![BierCaps - import/export database ui ](https://raw.github.com/apreps/BierCaps/master/screenshots/import-export-db.png)

## TO-DO

- Automatically fill beer caps basic informations;
- List to choose which camera application to use;
- Test on smaller/bigger devices;
- Allow to change beer cap image;

## Bugs

- When trying to import a database and no file is found in the memory (exits the application);
- Search database is case sensitive;
