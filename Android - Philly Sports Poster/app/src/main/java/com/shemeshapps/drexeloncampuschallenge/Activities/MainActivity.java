package com.shemeshapps.drexeloncampuschallenge.Activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Response;
import com.isseiaoki.simplecropview.CropImageView;
import com.shemeshapps.drexeloncampuschallenge.Helpers.PictureTypeSelect;
import com.shemeshapps.drexeloncampuschallenge.Helpers.SessionHelper;
import com.shemeshapps.drexeloncampuschallenge.Models.Drexel;
import com.shemeshapps.drexeloncampuschallenge.Models.Participant;
import com.shemeshapps.drexeloncampuschallenge.Models.User;
import com.shemeshapps.drexeloncampuschallenge.Networking.RequestUtil;
import com.shemeshapps.drexeloncampuschallenge.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class MainActivity extends AppCompatActivity {

    private Uri takenPhotoLocation;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        loadUserInfo();
        loadDrexelInfo();
        //startActivityForResult(new Intent(MainActivity.this, LogEventActivity.class), 3);


        if(getIntent().getExtras()== null || !getIntent().getExtras().getBoolean("justLoggedIn"))
        {
            RequestUtil.getInstance(this).GetUserInfoWithSavedSession(new Response.Listener() {
                @Override
                public void onResponse(Object response) {
                    loadUserInfo((User)response);
                }
            });
        }

        RequestUtil.getInstance(this).GetDrexelInfo(new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                loadDrexelInfo((Drexel) response);
            }
        });

        Button logNewActivity = (Button)findViewById(R.id.log_activity_button);
        logNewActivity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new PictureTypeSelect(MainActivity.this, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        if(i==PictureTypeSelect.PictureType.TAKE.ordinal())
                        {
                            Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                                File f = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "pre_crop.jpg");
                                if(f.exists())f.delete();

                                takenPhotoLocation = Uri.fromFile(f);
                                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,takenPhotoLocation );
                                startActivityForResult(takePictureIntent, 0);
                            }
                        }
                        else if(i==PictureTypeSelect.PictureType.UPLOAD.ordinal())
                        {
                            Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                            intent.setType("image/*");
                            startActivityForResult(intent, 1);
                        }
                    }
                });
            }
        });
    }


    private void loadDrexelInfo()
    {
        loadDrexelInfo(SessionHelper.getDrexelInfo(this));
    }
    private void loadDrexelInfo(Drexel d)
    {
        RelativeLayout cellRow2 = (RelativeLayout)findViewById(R.id.cell_row_2);
        setCellContent((RelativeLayout) cellRow2.getChildAt(0), "Drexels Entries", d.entries);
        setCellContent((RelativeLayout) cellRow2.getChildAt(1), "Drexels Rank", d.position);
        setCellContent((RelativeLayout) cellRow2.getChildAt(2), "Drexels Points", d.totalPoints);

        LinearLayout rows = (LinearLayout)findViewById(R.id.top_participants);
        int index = 0;
        for(Participant p :d.topParticipants)
        {
            setRowContent((RelativeLayout)rows.getChildAt(index),p.name,p.points);
            index++;
        }

        TextView  rowHeader = (TextView)findViewById(R.id.participants_title);
        rowHeader.setText("Top 10 participants out of " + d.participants);

    }

    private void loadUserInfo()
    {
        loadUserInfo(SessionHelper.getUserInfo(this));
    }
    private void loadUserInfo(User u)
    {
        RelativeLayout cellRow1 = (RelativeLayout)findViewById(R.id.cell_row_1);
        setCellContent((RelativeLayout) cellRow1.getChildAt(0),"Your Entries",u.entries);
        setCellContent((RelativeLayout) cellRow1.getChildAt(1),"Your Rank",u.rank);
        setCellContent((RelativeLayout) cellRow1.getChildAt(2),"Your Points",u.totalPoints);

        TextView name = (TextView)findViewById(R.id.name_title);
        name.setText("Hello " + u.name.split(" ")[0] + "!");
    }


    private void setCellContent(RelativeLayout cell, String title, int value)
    {
        TextView titleView = (TextView)cell.findViewById(R.id.cell_title);
        titleView.setText(title);

        TextView numberView = (TextView)cell.findViewById(R.id.cell_number);
        numberView.setText(Integer.toString(value));
    }

    private void setRowContent(RelativeLayout row, String name, int points)
    {
        TextView titleView = (TextView)row.findViewById(R.id.participant_name);
        titleView.setText(name);

        TextView numberView = (TextView)row.findViewById(R.id.participant_score);
        numberView.setText(Integer.toString(points));
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent result) {
        if (resultCode == RESULT_OK) {
            if (requestCode == 0) {

                //startActivityForResult(new Intent(MainActivity.this, CropImageActivity.class),2);
                startActivityForResult(new Intent(MainActivity.this,LogEventActivity.class),3);
            } else if (requestCode == 1) {
                if (result != null) {
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(result.getData());
                        File f = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "pre_crop.jpg");
                        if (f.exists()) f.delete();
                        OutputStream output = new FileOutputStream(f);
                        byte[] buffer = new byte[4 * 1024]; // or other buffer size
                        int read;

                        while ((read = inputStream.read(buffer)) != -1) {
                            output.write(buffer, 0, read);
                        }
                        output.flush();
                        output.close();
                        inputStream.close();

                    } catch (Exception e) {
                        Log.e("broken image", "image broke");
                    }
                    //startActivityForResult(new Intent(MainActivity.this, CropImageActivity.class),2);
                    startActivityForResult(new Intent(MainActivity.this,LogEventActivity.class),3);
                }
            }
            else if(requestCode==2)
            {
                startActivityForResult(new Intent(MainActivity.this,LogEventActivity.class),3);
            }
            else if(requestCode == 3)
            {
                RequestUtil.getInstance(this).GetUserInfoWithSavedSession(new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        loadUserInfo((User)response);
                    }
                });

                RequestUtil.getInstance(this).GetDrexelInfo(new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        loadDrexelInfo((Drexel) response);
                    }
                });
            }
        }


    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_settings) {
            RequestUtil.getInstance(this).logout();
            return true;
        }

        if(id==R.id.open_website)
        {
            startActivity(new Intent(MainActivity.this,CampusWebview.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
