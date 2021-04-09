package edu.sjsu.android.mapdemo;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.content.ContentValues;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback,
        LoaderManager.LoaderCallbacks<Cursor> {
    private final LatLng LOCATION_UNIV = new LatLng(37.335371, -121.881050);
    private final LatLng LOCATION_CS = new LatLng(37.333714, -121.881860);
    private GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        assert mapFragment != null;
        mapFragment.getMapAsync(this);

        int LOADER_ID = 1;
        LoaderManager.getInstance(this).initLoader(LOADER_ID, null, this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {
        this.googleMap = googleMap;
        // Invoke loaderCallbacks to retrieve and draw already saved locations in map

        googleMap.setOnMapClickListener(
                new GoogleMap.OnMapClickListener(){

            @Override
            public void onMapClick(LatLng latLng) {
                // Add a marker to the map
                // Creating an instance of LocationInsertTask
                // Staring the Latitude, longitude and zoom level to SQLite database
                // Display "Marker is added to the Map" message
                googleMap.addMarker(new MarkerOptions().position(latLng).title("Marker"));

                LocationInsertTask locationInsertTask = new LocationInsertTask();

                ContentValues values = new ContentValues();
                values.put(LocationDB.LATITUDE, Double.toString(latLng.latitude));
                values.put(LocationDB.LONGITUDE, Double.toString(latLng.longitude));
                values.put(LocationDB.ZOOM, Float.toString(googleMap.getCameraPosition().zoom));

                locationInsertTask.doInBackground(values);
                Toast.makeText(MainActivity.this,
                        "Marker " + "is added to the Map.",
                        Toast.LENGTH_SHORT).show();

            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener(){

            @Override
            public void onMapLongClick(LatLng latLng) {
                // Removing all markers from the google map
                // Creating an instance LocationDeleteTask
                // Deleting all the rows from SQLite database table
                // Display "All markers are removed" message
                LocationDeleteTask locationDeleteTask = new LocationDeleteTask();
                locationDeleteTask.doInBackground();
                googleMap.clear();
                Toast.makeText(MainActivity.this, "All markers are removed.",
                        Toast.LENGTH_LONG).show();
            }
        });
    }

    private class LocationInsertTask extends AsyncTask<ContentValues, Void, Void> {

        @Override
        protected Void doInBackground(ContentValues... contentValues) {
            // Setting up values to insert the clicked location into SQLite database
            getContentResolver().insert(LocationContentProvider.CONTENT_URI, contentValues[0]);
            return null;
        }
    }

    private class LocationDeleteTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            //Deleting all the locations stored in SQLite database
            getContentResolver()
                    .delete(LocationContentProvider.CONTENT_URI, null, null);
            return null;
        }
    }

    public void onClick_CS(View v){
        googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_CS, 18);
        googleMap.animateCamera(update);
    }

    public void onClick_Univ(View v){
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 14);
        googleMap.animateCamera(update);
    }

    public void onClick_City(View v){
        googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        CameraUpdate update = CameraUpdateFactory.newLatLngZoom(LOCATION_UNIV, 10);
        googleMap.animateCamera(update);
    }

    @NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        // Uri to the content provider LocationContentProvider
        // Fetches all the rows from marker table
        CursorLoader cursorLoader = new CursorLoader(MainActivity.this,
                LocationContentProvider.CONTENT_URI
                , null, null, null, null);
        cursorLoader.loadInBackground();
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        double lat = 0;
        double lng = 0;
        float zoom = 0;
        LatLng lastPosition = null;
        // number of markers available in the SQLite database table
        int markerCount = 0;
        if(data != null){
            markerCount = data.getCount();
            data.moveToFirst();
        }

        // move the current record pointer to the first row of the table

        for(int i = 0; i < markerCount; i++){
            // Get the latitude
            // Get the longitude
            // Get the zoom level
            // Creating an instance of Latlng to plot the location in Google Map
            // Drawing the marker in the Google Maps
            // Traverse the pointer in the next row
            lat = Double.parseDouble(data.getString(data.getColumnIndex(LocationDB.LATITUDE)));
            lng = Double.parseDouble(data.getString(data.getColumnIndex(LocationDB.LONGITUDE)));
            zoom = Float.parseFloat(data.getString(data.getColumnIndex(LocationDB.ZOOM)));

            LatLng latLng = new LatLng(lat, lng);
            MarkerOptions markerOptions = new MarkerOptions();
            markerOptions.position(latLng).title("Marker");
            googleMap.addMarker(markerOptions);

            lastPosition = latLng;

            if(!data.moveToNext())
                break;
        }
        if(markerCount > 0){
            // Moving CameraPosition to last clicked position
            // Setting the zoom level in the map on last position
            if(zoom == 14){
                googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
            }
            else if(zoom == 10){
                googleMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
            }
            else{
                googleMap.setMapType(GoogleMap.MAP_TYPE_TERRAIN);
            }
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(lastPosition, zoom));
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {

    }
}