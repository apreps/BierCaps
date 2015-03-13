package com.example.biercaps;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class BeerEdit extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beer_edit);

        //Instanciate font types for UI
        final Typeface typeFaceOswaldBold= Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
        Typeface typeFaceQuattrocentoBold = Typeface.createFromAsset(getAssets(), "fonts/Quattrocento-Bold.ttf");
        final Typeface typeFaceQuattrocentoRegular = Typeface.createFromAsset(getAssets(), "fonts/Quattrocento-Regular.ttf");

        //add fonts to textview
        TextView beerNameDetailTv = (TextView) findViewById(R.id.textViewDetailName);
        beerNameDetailTv.setTypeface(typeFaceQuattrocentoBold);

        TextView beerCountryDetailTv = (TextView) findViewById(R.id.textViewDetailCountry);
        beerCountryDetailTv.setTypeface(typeFaceQuattrocentoBold);

        TextView beerNotesDetailTv = (TextView) findViewById(R.id.textViewDetailNotes);
        beerNotesDetailTv.setTypeface(typeFaceQuattrocentoBold);

        TextView beerRatingDetailTv = (TextView) findViewById(R.id.editBeerRating);
        beerRatingDetailTv.setTypeface(typeFaceQuattrocentoBold);

		//get beer details from previous activity (display beer details)
		final String beerName = getIntent().getExtras().getString("beerDetailsName");
		String beerCountry = getIntent().getExtras().getString("beerDetailsCountry");
		String beerNotes = getIntent().getExtras().getString("beerDetailsNotes");
		float ratingBeer = getIntent().getExtras().getFloat("beerRate");

        //get all editext fields
		final EditText etBeerName = (EditText) findViewById(R.id.editBeerName); 
		final EditText etBeerCountry = (EditText) findViewById(R.id.editBeerCountry);
		final EditText etBeerNotes = (EditText) findViewById(R.id.editBeerNotes);
		final RatingBar rbBeerRate = (RatingBar) findViewById(R.id.ratingBarEditBeer);

        //set font type and size
		etBeerName.setText(beerName);
        etBeerName.setTypeface(typeFaceQuattrocentoRegular);
        etBeerName.setTextSize(20);

		etBeerCountry.setText(beerCountry);
        etBeerCountry.setTypeface(typeFaceQuattrocentoRegular);
        etBeerCountry.setTextSize(20);

		etBeerNotes.setText(beerNotes);
        etBeerNotes.setTypeface(typeFaceQuattrocentoRegular);
        etBeerNotes.setTextSize(20);

		rbBeerRate.setRating(ratingBeer);		

        //get buttons and set font type
		Button buttonGoBack = (Button) findViewById(R.id.buttonGoBack);
        buttonGoBack.setTypeface(typeFaceOswaldBold);


        Button buttonSaveChanges = (Button) findViewById(R.id.buttonSaveChanges);
        buttonSaveChanges.setTypeface(typeFaceOswaldBold);

        //if "Go Back" button is clicked, show warning message
		buttonGoBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {

                // create a new custom dialog
                final Dialog dialog = new Dialog(BeerEdit.this);

                //remove (aka hide) title bar
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.setCancelable(false);
                dialog.setContentView(R.layout.custom_dialog);

                //add warning text
                TextView warningTextTv = (TextView) dialog.findViewById(R.id.warning_text);
                warningTextTv.setText(getString(R.string.dialog_are_you_sure));

                //set font to warning text
                warningTextTv.setTypeface(typeFaceQuattrocentoRegular);

                //get buttons and set type font
                Button dialogButtonYes = (Button) dialog.findViewById(R.id.dialogButtonOK);
                dialogButtonYes.setTypeface(typeFaceOswaldBold);

                Button dialogButtonNo = (Button) dialog.findViewById(R.id.dialogButtonNotOK);
                dialogButtonNo.setTypeface(typeFaceOswaldBold);

                // if button "Yes" is clicked, go back to previous activity
                dialogButtonYes.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        finish();
                    }
                });

                // if button "no" is clicked, do nothing and close the custom dialog
                dialogButtonNo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                //show custom dialog
                dialog.show();
			}
		});

        //if "Save" button is clicked
		buttonSaveChanges.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				//get previous record of Beer "beerName"
				List<Beer> beers = Beer.find(Beer.class, "name = ?", beerName);
				Log.d("HOW MANY BEERS WITH NAME NAME: ", Integer.toString(beers.size()));
				
				//get new updated data
				String updatedBeerName = etBeerName.getText().toString();
				String updatedBeerCountry = etBeerCountry.getText().toString();
				String updatedBeerNotes = etBeerNotes.getText().toString();
				float updatedBeerRating = rbBeerRate.getRating();
				
				Log.v("NEW BEER VALUES NAME: ", updatedBeerName);
				Log.v("NEW BEER VALUES COUNTRY: ", updatedBeerCountry);
				Log.v("NEW BEER VALUES NOTES: ", updatedBeerNotes);
				
				//update record
				Beer beerToUpdate = beers.get(0);
				beerToUpdate.name = updatedBeerName;
				beerToUpdate.country = updatedBeerCountry;
				beerToUpdate.notes = updatedBeerNotes;
				beerToUpdate.rate = updatedBeerRating;
				
				//updates the previous entry with the new values
				beerToUpdate.save();

                //display confirmation message
                Context context = getApplicationContext();
                CharSequence text = "Beer " + etBeerName.getText() + " updated !";
                int duration = Toast.LENGTH_SHORT;

                Toast toast = Toast.makeText(context, text, duration);
                toast.show();

				Intent intent = new Intent(BeerEdit.this, BierCaps.class);
			    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);   
			    startActivity(intent);
			}
		});

	}
}
