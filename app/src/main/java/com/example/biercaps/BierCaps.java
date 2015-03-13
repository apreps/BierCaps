package com.example.biercaps;

import android.app.Activity;
import android.app.Dialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;
import java.util.List;

public class BierCaps extends Activity {
	TextView beerName;
	ImageAdapter mAdapter;
	GridView gridView;
	int beerPosition;
	Context c;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bier_caps);

        //Instanciate fonts for UI
        final Typeface typeFaceOswaldBold= Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
        final Typeface typeFaceQuattrocentoRegular = Typeface.createFromAsset(getAssets(), "fonts/Quattrocento-Regular.ttf");

        //opens activity to add beer on button press
		Button buttonAddBeer = (Button) findViewById(R.id.button_add_beer);
        buttonAddBeer.setTypeface(typeFaceOswaldBold);
		buttonAddBeer.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intentAddBeer = new Intent(getApplicationContext(), AddBeer.class);
				startActivityForResult(intentAddBeer,1);
			}
		});

		//initialize grid with beers
		initializeGrid(this);

		//opens a new activity with beer details
		gridView.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View v, int position, long id) {

				Beer b = (Beer) gridView.getItemAtPosition(position);
				Log.d("Clicked Beer name: ", b.getName());
				
				Beer b1 = mAdapter.getItem(position);

                //Get beer details to display on "Beer details" activity
                String beerNameTemp   = b.getName();
				String beerCountryTemp   = b.getCountry();
				Log.d("BEER COUNTRY", beerCountryTemp);
				String beerNotesTemp     = b.getNotes();
				float beerRateTemp = b.getRate();

				Intent intentBeerDetails = new Intent(getApplicationContext(), BeerDetails.class);
				intentBeerDetails.putExtra("beerDetailsName",beerNameTemp);
				intentBeerDetails.putExtra("beerDetailsCountry",beerCountryTemp);
				intentBeerDetails.putExtra("beerDetailsNotes",beerNotesTemp);
				intentBeerDetails.putExtra("beerRate", beerRateTemp);
				
				startActivityForResult(intentBeerDetails,1);
			}
		});

		//opens a pop up dialog to remove beer
		gridView.setOnItemLongClickListener(new OnItemLongClickListener() {

			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int position, long arg3) {

				beerPosition = position;
				c = getApplicationContext();

                // create a new custom dialog
                final Dialog dialog = new Dialog(BierCaps.this);

                //remove (aka hide) title bar
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_dialog);

                //add warning text
                TextView warningTextTv = (TextView) dialog.findViewById(R.id.warning_text);
                warningTextTv.setText(getString(R.string.dialog_delete_beer));

                //set font to warning text
                warningTextTv.setTypeface(typeFaceQuattrocentoRegular);

                //set font to button "Yes"
                Button dialogButtonYes = (Button) dialog.findViewById(R.id.dialogButtonOK);
                dialogButtonYes.setTypeface(typeFaceOswaldBold);

                // if button "Yes" is clicked, go back to previous activity
                dialogButtonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //get beer in grid
                        Beer b = (Beer) gridView.getItemAtPosition(beerPosition);
                        Log.d("Clicked Beer name: ", b.getName());

                        //get beer in database
                        List<Beer> beers = Beer.find(Beer.class, "name = ?", b.getName());

                        //if no beer is found
                        if(beers.size() == 0){
                            CharSequence text = "Impossible to remove beer !";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(c, text, duration);
                            toast.show();
                        }

                        try {
                            //remove beer from database
                            beers.get(0).delete();

                            //reload grid
                            reloadGrid(c);

                        } catch (NullPointerException npe) {

                            Log.d("NULL POINTER", npe.toString());
                        }

                        dialog.dismiss();
                    }
                });

                //set font to button "No"
                Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNotOK);
                dialogButtonNo.setTypeface(typeFaceOswaldBold);

                // if button "no" is clicked, do nothing and close the custom dialog
                dialogButtonNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //show custom dialog
                dialog.show();

				return true;
			}
		});


        //handle action bar search results
        handleIntent(getIntent());

	}

    @Override
    protected void onNewIntent(Intent intent) {

        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {

        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);

            Intent intentSearchResults = new Intent(getApplicationContext(), SearchResults.class);
            intentSearchResults.putExtra("searchQuery",query);
            startActivity(intentSearchResults);
        }
    }

	//after adding beer
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data)  
	{  
		super.onActivityResult(requestCode, resultCode, data);  

		// check if the request code 
		if(requestCode==1)  
		{  
			//reload the grid
			reloadGrid(this);

			List<Beer> books = Beer.listAll(Beer.class);
			Log.d("SIZE OF BEER ARRAY after adding beer",Integer.toString(books.size()));
		}  

	} 


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.bier_caps, menu);

        // Associate searchable configuration with the SearchView
        SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));

        return super.onCreateOptionsMenu(menu);
    }

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

        final Typeface typeFaceOswaldBold= Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
        Typeface typeFaceQuattrocentoBold = Typeface.createFromAsset(getAssets(), "fonts/Quattrocento-Bold.ttf");
        final Typeface typeFaceQuattrocentoRegular = Typeface.createFromAsset(getAssets(), "fonts/Quattrocento-Regular.ttf");

		if (id == R.id.action_import_export) {
            // create a new custom dialog
            final Dialog dialog = new Dialog(BierCaps.this);

            //remove (aka hide) title bar
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.custom_dialog);

            //add warning text
            TextView warningTextTv = (TextView) dialog.findViewById(R.id.warning_text);
            warningTextTv.setText(getString(R.string.dialog_what_you_want_to_do));

            //set font to warning text
            warningTextTv.setTypeface(typeFaceQuattrocentoRegular);

            Button dialogButtonExportDb = (Button) dialog.findViewById(R.id.dialogButtonOK);
            dialogButtonExportDb.setText("Export DB");
            dialogButtonExportDb.setTypeface(typeFaceOswaldBold);

            Button dialogButtonImportDb = (Button) dialog.findViewById(R.id.dialogButtonNotOK);
            dialogButtonImportDb.setText("Import DB");
            dialogButtonImportDb.setTypeface(typeFaceOswaldBold);

            // if button "Yes" is clicked, go back to previous activity
            dialogButtonExportDb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    File direct = new File(Environment.getExternalStorageDirectory() + "//BackupFolder/DatabaseName");

                    if(!direct.exists())
                    {
                        if(direct.mkdir())
                        {
                            //directory is created;
                            exportDB();
                            dialog.dismiss();
                        }

                    }

                    exportDB();
                    dialog.dismiss();
                }
            });

            dialogButtonImportDb.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        importDB();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    dialog.dismiss();
                }
            });

            dialog.show();

            return true;
		}

		return super.onOptionsItemSelected(item);
	}

	//initialize grid with saved beers
	public void initializeGrid(Context c){
        Log.d("Gridview","Initializing...");

		mAdapter = new ImageAdapter(c);
		gridView = (GridView)findViewById(R.id.gridview);
		gridView.setAdapter(mAdapter);
	}
	
	//updates grid with beers
	public void reloadGrid(Context c){
        Log.d("Gridview","Reloaded !");

        List<Beer> beers = Beer.listAll(Beer.class);
        mAdapter = new ImageAdapter(c);
		gridView.invalidateViews();
		gridView.setAdapter(mAdapter);
	}

    //Export database to the root of the internal memory
    void exportDB(){

        final Typeface typeFaceOswaldBold= Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
        Typeface typeFaceQuattrocentoBold = Typeface.createFromAsset(getAssets(), "fonts/Quattrocento-Bold.ttf");
        final Typeface typeFaceQuattrocentoRegular = Typeface.createFromAsset(getAssets(), "fonts/Quattrocento-Regular.ttf");

        final File dbFile =
                new File(Environment.getDataDirectory() + "/data/com.example.biercaps/databases/beers.db");

        final File exportDir = new File(Environment.getExternalStorageDirectory(), "");
        File exportedDb = new File(Environment.getExternalStorageDirectory(), "/beers.db");

        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }

        final File file = new File(exportDir, dbFile.getName());

        try {

            if(exportedDb.exists()){
                // create a new custom dialog
                final Dialog dialog = new Dialog(BierCaps.this);

                //remove (aka hide) title bar
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_dialog);

                //add warning text
                TextView warningTextTv = (TextView) dialog.findViewById(R.id.warning_text);
                warningTextTv.setText(getString(R.string.dialog_overwrite));

                //set font to warning text
                warningTextTv.setTypeface(typeFaceQuattrocentoRegular);

                Button dialogButtonYesOverwrite = (Button) dialog.findViewById(R.id.dialogButtonOK);
                dialogButtonYesOverwrite.setText("Yes, do it !");
                dialogButtonYesOverwrite.setTypeface(typeFaceOswaldBold);

                Button dialogButtonNoOverwrite = (Button) dialog.findViewById(R.id.dialogButtonNotOK);
                dialogButtonNoOverwrite.setText("Nope !");
                dialogButtonNoOverwrite.setTypeface(typeFaceOswaldBold);

                // if button "Yes" is clicked, go back to previous activity
                dialogButtonYesOverwrite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {

                            file.createNewFile();
                            copyFile(dbFile, file);
                            dialog.dismiss();

                            //Display successful export message to user
                            CharSequence text = "Database exported !";
                            int duration = Toast.LENGTH_SHORT;

                            Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                            toast.show();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                });

                // if button "Yes" is clicked, go back to previous activity
                dialogButtonNoOverwrite.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
            else{
                file.createNewFile();
                this.copyFile(dbFile, file);

                //Display successful export message to user
                CharSequence text = "Database exported !";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(c, text, duration);
                toast.show();
            }
        } catch (IOException e) {
            Log.e("ERROR", e.getMessage(), e);
        }
    }

    //importing database from the root of the internal memory of the device
    private void importDB() throws IOException {
        Log.d("Importing Database", "bla bla");
        List<Beer> books2 = Beer.listAll(Beer.class);
        Log.d("SIZE ARRAY before importing",Integer.toString(books2.size()));

        File exportedDb = new File(Environment.getExternalStorageDirectory(), "/beers.db");
        File applicationDb =
                new File(Environment.getDataDirectory() + "/data/com.example.biercaps/databases/beers.db");


        copyFile(new FileInputStream(exportedDb), new FileOutputStream(applicationDb));
        Log.d("Importing database", "done");
        List<Beer> books3 = Beer.listAll(Beer.class);
        Log.d("SIZE ARRAY after importing",Integer.toString(books3.size()));

        //Display successful export message to user
        CharSequence text = "Database imported !";
        int duration = Toast.LENGTH_SHORT;

        Toast toast = Toast.makeText(getApplicationContext(), text, duration);
        toast.show();

        reloadGrid(getApplicationContext());
    }

    void copyFile(File src, File dst) throws IOException {
        FileChannel inChannel = new FileInputStream(src).getChannel();
        FileChannel outChannel = new FileOutputStream(dst).getChannel();
        try {
            inChannel.transferTo(0, inChannel.size(), outChannel);
        } finally {
            if (inChannel != null)
                inChannel.close();
            if (outChannel != null)
                outChannel.close();
        }
    }

    /**
     * Thanks to: http://stackoverflow.com/questions/6540906/android-simple-export-and-import-of-sqlite-database
     * @param fromFile
     * @param toFile
     * @throws IOException
     */

    public final static void copyFile(FileInputStream fromFile, FileOutputStream toFile) throws IOException {
        FileChannel fromChannel = null;
        FileChannel toChannel = null;
        try {
            fromChannel = fromFile.getChannel();
            toChannel = toFile.getChannel();
            fromChannel.transferTo(0, fromChannel.size(), toChannel);
        } finally {
            try {
                if (fromChannel != null) {
                    fromChannel.close();
                }
            } finally {
                if (toChannel != null) {
                    toChannel.close();
                }
            }
        }
    }

}

