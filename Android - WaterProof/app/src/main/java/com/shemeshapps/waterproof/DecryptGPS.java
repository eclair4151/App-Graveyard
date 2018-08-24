package com.shemeshapps.waterproof;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class DecryptGPS extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_decrypt_gps);
        final Button decryptButton = (Button)findViewById(R.id.decryptButton);
        final EditText latBox = (EditText)findViewById(R.id.decLatitudeBox);
        final EditText lonBox = (EditText)findViewById(R.id.decLongitudeBox);

        final TextView latOutput = (TextView)findViewById(R.id.decLatitudeOutput);
        final TextView lonOutput = (TextView)findViewById(R.id.decLongitudeOutput);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Decrypt Coordinates");
        decryptButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                latOutput.setText("Latitude: " + decryptThing(latBox.getText().toString()));
                lonOutput.setText("Longitude: " + decryptThing(lonBox.getText().toString()));

            }
        });
    }



    public String decryptThing(String thing)
    {
        //341586
        int[] key = {3,4,1,5,8,6};
        char[] coor = thing.toCharArray();
        String newCoor = "";
        int keyIndex = 0;
        for(int i =0 ; i < coor.length; i++)
        {
            if(Character.isDigit(coor[i]))
            {
                int t = (Character.getNumericValue(coor[i]) - key[keyIndex%6]);
                if(t < 0)
                {
                    t += 10;
                }
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
}
