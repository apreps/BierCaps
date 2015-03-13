package com.example.biercaps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddBeer extends Activity {

	static final int REQUEST_IMAGE_CAPTURE = 1;
	static final int REQUEST_TAKE_PHOTO = 1;
	
	String mCurrentPhotoPath;
    //saves the location of an image
    String mAbsolutePhotoPath = "";
	
	EditText beerName, beerCountry, beerNotes;
	RatingBar beerRate;
	String name, country, notes;
	float rate;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_beer);

        //fonts used in UI
        final Typeface typeFaceOswaldBold= Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
        final Typeface typeFaceQuattrocentoBold = Typeface.createFromAsset(getAssets(), "fonts/Quattrocento-Bold.ttf");
        final Typeface typeFaceQuattrocentoRegular = Typeface.createFromAsset(getAssets(), "fonts/Quattrocento-Regular.ttf");

        //Add fonts to buttons
        Button buttonGoBack = (Button) findViewById(R.id.button_go_back_add_beer);
        buttonGoBack.setTypeface(typeFaceOswaldBold);

        Button buttonAddNewBeer = (Button) findViewById(R.id.button_add_new_beer);
        buttonAddNewBeer.setTypeface(typeFaceOswaldBold);

        Button buttonCamera = (Button) findViewById(R.id.button_add_picture);
        buttonCamera.setTypeface(typeFaceOswaldBold);

        //and textviews
        TextView beerNameTv = (TextView) findViewById(R.id.text_beer_name);
        beerNameTv.setTypeface(typeFaceQuattrocentoBold);

        TextView beerCountryTv = (TextView) findViewById(R.id.text_beer_country);
        beerCountryTv.setTypeface(typeFaceQuattrocentoBold);

        TextView beerNotesTv = (TextView) findViewById(R.id.text_beer_notes);
        beerNotesTv.setTypeface(typeFaceQuattrocentoBold);

        TextView beerRatingTv = (TextView) findViewById(R.id.text_beer_rating);
        beerRatingTv.setTypeface(typeFaceQuattrocentoBold);

        //get all editext elements, set font type and size
        beerName = (EditText) findViewById(R.id.insert_beer_name);
        beerName.setTypeface(typeFaceQuattrocentoRegular);
        beerName.setTextSize(20);

        beerCountry = (EditText) findViewById(R.id.insert_beer_country);
        beerCountry.setTypeface(typeFaceQuattrocentoRegular);
        beerCountry.setTextSize(20);

        beerNotes = (EditText) findViewById(R.id.insert_beer_notes);
        beerNotes.setTypeface(typeFaceQuattrocentoRegular);
        beerNotes.setTextSize(20);

        beerRate = (RatingBar) findViewById(R.id.ratingBarAddBeer);

        //if "Go Back" button is clicked, close current activity and go to previous one
		buttonGoBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

        //if "Add Beer" button is clicked
		buttonAddNewBeer.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

                //get inserted values
				name = beerName.getText().toString();
				country = beerCountry.getText().toString();
				notes = beerNotes.getText().toString();
				rate = beerRate.getRating();

				//create a new beer with inserted values
				Beer b = new Beer(name, country, notes, rate, mAbsolutePhotoPath);

				//add new beer to database
				b.save();	

				//display confirmation message
				Context context = getApplicationContext();
				CharSequence text = "Beer " + name + " added to collection !";
				int duration = Toast.LENGTH_SHORT;

				Toast toast = Toast.makeText(context, text, duration);
				toast.show();

				//close activity
				finish();
			}
		});

        //if "Camera" button is clicked
		buttonCamera.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
	}
	
	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
	    // Ensure that there's a camera activity to handle the intent
	    if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
	        // Create the File where the photo should go
	        File photoFile = null;
	        try {
	            photoFile = createImageFile();
	        } catch (IOException ex) {
	            // Error occurred while creating the File
	            
	        }
	        // Continue only if the File was successfully created
	        if (photoFile != null) {
	            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
	                    Uri.fromFile(photoFile));
	            startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
	        }
	    }
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Log.d("CAMERA", "Picture was taken successfully!");
	    }
	}

	private File createImageFile() throws IOException {

        //get current time
        java.util.Date date = Calendar.getInstance().getTime();

	    //parse date to dd-MM-yyyy format
        DateFormat formatter = new SimpleDateFormat("dd-MM-yyyy");
        String timeStamp = formatter.format(date);

        //get name
        beerName = (EditText) findViewById(R.id.insert_beer_name);
        String fileBeerName = beerName.getText().toString();

        fileBeerName = fileBeerName.replaceAll(" ", "_");

        //create file name
	    String imageFileName = fileBeerName + "_" + timeStamp + "_";

        //get folder to save picture
        File storageDir = Environment.getExternalStoragePublicDirectory(
	            Environment.DIRECTORY_PICTURES);

        File imagesFolder = new File(storageDir, "BierCaps");

        if(! imagesFolder.exists())
            imagesFolder.mkdir();

        File image = File.createTempFile(
	        imageFileName,  /* prefix */
	        ".jpg",         /* suffix */
	        imagesFolder      /* directory */
	    );

	    // Save a file: path for use with ACTION_VIEW intents
	    mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        mAbsolutePhotoPath = image.getAbsolutePath();

	    return image;
	}
}
