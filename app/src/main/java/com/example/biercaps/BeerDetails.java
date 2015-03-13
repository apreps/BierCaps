package com.example.biercaps;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

public class BeerDetails extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beer_details);

        //Instanciate fonts for UI
        Typeface typeFaceOswaldBold= Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
        Typeface typeFaceQuattrocentoBold = Typeface.createFromAsset(getAssets(), "fonts/Quattrocento-Bold.ttf");
        Typeface typeFaceQuattrocentoRegular = Typeface.createFromAsset(getAssets(), "fonts/Quattrocento-Regular.ttf");

        //add fonts to textviews
        TextView beerNameDetailTv = (TextView) findViewById(R.id.textViewDetailName);
        beerNameDetailTv.setTypeface(typeFaceQuattrocentoBold);

        TextView beerCountryDetailTv = (TextView) findViewById(R.id.textViewDetailCountry);
        beerCountryDetailTv.setTypeface(typeFaceQuattrocentoBold);

        TextView beerNotesDetailTv = (TextView) findViewById(R.id.textViewDetailNotes);
        beerNotesDetailTv.setTypeface(typeFaceQuattrocentoBold);

        TextView beerRatingDetailTv = (TextView) findViewById(R.id.textViewDetailRating);
        beerRatingDetailTv.setTypeface(typeFaceQuattrocentoBold);
		
		//get beer details
		final String beerName = getIntent().getExtras().getString("beerDetailsName");
		final String beerCountry = getIntent().getExtras().getString("beerDetailsCountry");
		final String beerNotes = getIntent().getExtras().getString("beerDetailsNotes");
		final float ratingBeer = getIntent().getExtras().getFloat("beerRate");

        //get textview and set font type and size
		TextView tvBeerName = (TextView) findViewById(R.id.textViewBeerName);
        tvBeerName.setTypeface(typeFaceQuattrocentoRegular);
        tvBeerName.setTextSize(20);

		TextView tvBeerCountry = (TextView) findViewById(R.id.textViewBeerCountry);
        tvBeerCountry.setTypeface(typeFaceQuattrocentoRegular);
        tvBeerCountry.setTextSize(20);

		TextView tvBeerNotes = (TextView) findViewById(R.id.textViewBeerNotes);
        tvBeerNotes.setTypeface(typeFaceQuattrocentoRegular);
        tvBeerNotes.setTextSize(20);

		RatingBar rbBeerRate = (RatingBar) findViewById(R.id.ratingBarDetailBeer);
		
		tvBeerName.setText(beerName);
		tvBeerCountry.setText(beerCountry);
		tvBeerNotes.setText(beerNotes);
		rbBeerRate.setRating(ratingBeer);

        //get buttons and set font type
        Button buttonEdit = (Button) findViewById(R.id.buttonEditBeer);
        buttonEdit.setTypeface(typeFaceOswaldBold);

        Button buttonGoBack = (Button) findViewById(R.id.buttonGoBack);
        buttonGoBack.setTypeface(typeFaceOswaldBold);

        //if button "Go Back" is clicked, go to previous activity
		buttonGoBack.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});

        //if "Edit" button is clicked
        buttonEdit.setOnClickListener(new View.OnClickListener() {
			public void onClick(View v) {
				Intent intentEditBeer = new Intent(getApplicationContext(), BeerEdit.class);	

                //saves current values to display on the next activity to edit the properties
				intentEditBeer.putExtra("beerDetailsName",beerName);
				intentEditBeer.putExtra("beerDetailsCountry",beerCountry);
				intentEditBeer.putExtra("beerDetailsNotes",beerNotes);
				intentEditBeer.putExtra("beerRate", ratingBeer);
				
				startActivity(intentEditBeer);
			}
		});
	}
}
