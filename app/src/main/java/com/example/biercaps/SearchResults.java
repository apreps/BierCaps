package com.example.biercaps;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;

import java.util.List;


public class SearchResults extends Activity {
    GridView gridView_search_results;
    ImageAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

        //get inserted string
        final String searchQuery = getIntent().getExtras().getString("searchQuery");
        Log.d("Inserted query: ",searchQuery);

        gridView_search_results = (GridView)findViewById(R.id.gridview_search_results);
        mAdapter = new ImageAdapter(getApplicationContext());

        //add font to "Go Back" button
        final Typeface typeFaceOswaldBold= Typeface.createFromAsset(getAssets(), "fonts/Oswald-Bold.ttf");
        Button buttonGoBackResults = (Button) findViewById(R.id.button_go_back_results);
        buttonGoBackResults.setTypeface(typeFaceOswaldBold);

        //if "Go Back" button is clicked, return to the previous activiy
        buttonGoBackResults.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                finish();
            }
        });

        //search database for beers with name == query
        List<Beer> beers = Beer.find(Beer.class, "name = ?", searchQuery);
        Log.d("Inserted query number of results: ",Integer.toString(beers.size()));

        //filter results alphabetically
        mAdapter.filter(beers);

        //update grid
        gridView_search_results.invalidateViews();
        gridView_search_results.setAdapter(mAdapter);
    }
}
