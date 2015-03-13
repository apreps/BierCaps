package com.example.biercaps;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 
 * Inspired in http://stackoverflow.com/questions/15261088/gridview-with-two-columns-and-auto-resized-images
 *
 */
public class ImageAdapter extends BaseAdapter {
	private List<Beer> beers = new ArrayList<Beer>();
	private LayoutInflater inflater;
	private Context mContext;
    static Typeface typeFaceQuattrocentoRegular;

    public ImageAdapter(Context c) {
        mContext = c;
        inflater = LayoutInflater.from(c);

        beers = Beer.listAll(Beer.class);

        typeFaceQuattrocentoRegular = Typeface.createFromAsset(c.getAssets(), "fonts/Quattrocento-Regular.ttf");


        //sort gridview elements alphabetically
        Collections.sort(beers, new Comparator() {

            public int compare(Object o1, Object o2) {
                Beer p1 = (Beer) o1;
                Beer p2 = (Beer) o2;
                return p1.getName().compareToIgnoreCase(p2.getName());
            }
        });
    }

    public int getCount() {
        return beers.size();
    }

    public Beer getItem(int position) {
        return beers.get(position);
    }

    public long getItemId(int position) {
        return 0;
    }

    // create a new ImageView for each item referenced by the Adapter
    public View getView(int position, View convertView, ViewGroup parent) {

        View v = convertView;
        DataHolder holder = null;
        Beer item = (Beer)getItem(position);

        if(holder == null) {
            v = inflater.inflate(R.layout.grid_item, parent, false);
            holder = new DataHolder();

            holder.name = (TextView) v.findViewById(R.id.text);
            holder.name.setTypeface(typeFaceQuattrocentoRegular);
            holder.name.setTextSize(18);

            holder.picture = (ImageView) v.findViewById(R.id.picture);
        }
        else{
            holder = (DataHolder) v.getTag();
        }

        v.setTag(holder);
        holder.picture.setImageBitmap(decodeScaledBitmapFromSdCard(item.photoPath, 150, 150));
        holder.name.setText(item.name);

        return v;
    }

    // Filter Class
    public void filter(List<Beer> searchResult) {
        beers = searchResult;

        //notifyDataSetChanged();
    }

    static class DataHolder{
        ImageView picture;
        TextView name;
    }

    /*
        Thanks to: http://stackoverflow.com/questions/15377186/decode-file-from-sdcard-android-to-avoid-out-of-memory-error-due-to-large-bitmap
     */

    public static Bitmap decodeScaledBitmapFromSdCard(String filePath,
                                                      int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            // Calculate ratios of height and width to requested height and width
            final int heightRatio = Math.round((float) height / (float) reqHeight);
            final int widthRatio = Math.round((float) width / (float) reqWidth);

            // Choose the smallest ratio as inSampleSize value, this will guarantee
            // a final image with both dimensions larger than or equal to the
            // requested height and width.
            inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
        }

        return inSampleSize;
    }
}
