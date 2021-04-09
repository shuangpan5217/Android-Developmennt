package edu.sjsu.android.locationdemo;

import androidx.appcompat.app.AppCompatActivity;

import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private GPSTracker gps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        Button showLocationButton = findViewById(R.id.showLocationButton);

        showLocationButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                gps = new GPSTracker(MainActivity.this);

                Location location = gps.getLocation();

                //check if GPS enabled
                if(gps.canGetLocation()){
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();
                    Toast.makeText(getApplicationContext(), "You Location is - \nLat: "
                    + latitude + "\nLong: " + longitude, Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}