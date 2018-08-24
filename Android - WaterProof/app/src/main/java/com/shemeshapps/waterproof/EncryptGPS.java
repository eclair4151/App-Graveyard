package com.shemeshapps.waterproof;

import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.Collections;

public class EncryptGPS extends AppCompatActivity {

    LocationListener locationListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_encrypt_gps);
        getSupportActionBar().setTitle("Encrypt Coordinates");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        // Acquire a reference to the system Location Manager
        Button currentLocation = (Button)findViewById(R.id.currentLocation);
        Button encryptButton = (Button)findViewById(R.id.encryptButton);
        final EditText latBox = (EditText)findViewById(R.id.encLatitudeBox);
        final EditText lonBox = (EditText)findViewById(R.id.encLongitudeBox);
        final TextView latOutput = (TextView)findViewById(R.id.encLatitudeOutput);
        final TextView lonOutput = (TextView)findViewById(R.id.encLongitudeOutput);

        currentLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final LocationManager locationManager = (LocationManager) EncryptGPS.this.getSystemService(Context.LOCATION_SERVICE);

                locationListener = new LocationListener() {
                    public void onLocationChanged(Location location) {
                        // Called when a new location is found by the network location provider.
                        // makeUseOfNewLocation(location);
                        latBox.setText(Double.toString(location.getLatitude()));
                        lonBox.setText(Double.toString(location.getLongitude()));

                        locationManager.removeUpdates(locationListener);
                    }

                    public void onStatusChanged(String provider, int status, Bundle extras) {}

                    public void onProviderEnabled(String provider) {}

                    public void onProviderDisabled(String provider) {}
                };

// Register the listener with the Location Manager to receive location updates
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, locationListener);
            }
        });


        encryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latOutput.setText("Latitude: " + encryptThing(latBox.getText().toString()));
                lonOutput.setText("Longitude: " + encryptThing(lonBox.getText().toString()));

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == android.R.id.home) {
            finish();
        }

        return super.onOptionsItemSelected(item);
    }


    public String encryptThing(String thing)
    {
        int[] key = {3,4,1,5,8,6};
        char[] coor = thing.toCharArray();
        String newCoor = "";
        int keyIndex = 0;
        for(int i =0 ; i < coor.length; i++)
        {
            if(Character.isDigit(coor[i]))
            {
                int t = (Character.getNumericValue(coor[i]) + key[keyIndex%6])%10;
                keyIndex++;
                newCoor += Integer.toString(t);
            }
            else
            {
                newCoor += coor[i];
            }
        }
        return newCoor;

    }

}
