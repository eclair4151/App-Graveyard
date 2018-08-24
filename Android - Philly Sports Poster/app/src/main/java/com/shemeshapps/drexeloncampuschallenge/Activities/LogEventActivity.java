package com.shemeshapps.drexeloncampuschallenge.Activities;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Response;
import com.shemeshapps.drexeloncampuschallenge.Helpers.GetSpinnerItemHelper;
import com.shemeshapps.drexeloncampuschallenge.Helpers.PictureTypeSelect;
import com.shemeshapps.drexeloncampuschallenge.Helpers.SimpleSpinnerAdapter;
import com.shemeshapps.drexeloncampuschallenge.Networking.RequestUtil;
import com.shemeshapps.drexeloncampuschallenge.R;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class LogEventActivity extends AppCompatActivity {

    String currentDate;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_event);

        final File f = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES), "pre_crop.jpg");
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inPreferredConfig = Bitmap.Config.ARGB_8888;
        final ProgressBar spinner = (ProgressBar)findViewById(R.id.postingSpinner);

        ExifInterface exif = null;
        try {
            exif = new ExifInterface(f.getAbsolutePath());
        } catch (IOException e) {
            e.printStackTrace();
        }
        int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION,
                ExifInterface.ORIENTATION_UNDEFINED);

        Bitmap pic = PictureTypeSelect.rotateBitmap(BitmapFactory.decodeFile(f.getAbsolutePath(), options), orientation);

        ImageView image = (ImageView)findViewById(R.id.activity_image);
        image.setImageBitmap(pic);


        final Button submit = (Button)findViewById(R.id.submit_button);

        final EditText name = (EditText)findViewById(R.id.activity_name);
        final EditText description = (EditText)findViewById(R.id.activity_description);

        final Spinner activity = (Spinner)findViewById(R.id.activity_spinner);
        final SimpleSpinnerAdapter adapter1 = new SimpleSpinnerAdapter(this, GetSpinnerItemHelper.getActivities());
        activity.setAdapter(adapter1);


        final Spinner times = (Spinner)findViewById(R.id.activity_minutes_spinner);
        final SimpleSpinnerAdapter adapter2 = new SimpleSpinnerAdapter(this, GetSpinnerItemHelper.getTimes());
        times.setAdapter(adapter2);

        final TextView date = (TextView)findViewById(R.id.activity_date);
        final SimpleDateFormat simpleDate =  new SimpleDateFormat("MM/dd/yy");
        currentDate  = simpleDate.format(new Date());
        date.setText(Html.fromHtml("Date: <u>" + currentDate + "</u>"));

        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar c = Calendar.getInstance();
                int mYear = c.get(Calendar.YEAR);
                int mMonth = c.get(Calendar.MONTH);
                int mDay = c.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dpd = new DatePickerDialog(LogEventActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.set(year, monthOfYear, dayOfMonth);
                                currentDate = simpleDate.format(calendar.getTime());
                                date.setText(Html.fromHtml("Date: <u>" + currentDate + "</u>"));

                            }
                        }, mYear, mMonth, mDay);
                dpd.show();
            }
        });

        final CheckBox checkBox = (CheckBox)findViewById(R.id.activity_terms_check);

        final TextView agree = (TextView)findViewById(R.id.agree_text);
        agree.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkBox.setChecked(!checkBox.isChecked());
                if(checkBox.isChecked())
                {
                    agree.setTextColor(getResources().getColor(R.color.white));
                }
            }
        });

        TextView termLink = (TextView)findViewById(R.id.agree_link);
        termLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.oncampuschallenge.org/rules"));
                startActivity(browserIntent);
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean valid = true;
                if(name.getText().toString().trim().isEmpty())
                {
                    name.setError("Required");
                    valid = false;
                }

                if(description.getText().toString().trim().length() < 26 || description.getText().toString().length() > 300)
                {
                    description.setError("Must be between 25 and 300 chars");
                    valid = false;
                }

                if(!checkBox.isChecked())
                {
                    agree.setTextColor(getResources().getColor(R.color.wrongRed));
                    valid = false;
                }


                if(valid)
                {
                    submit.setVisibility(View.GONE);
                    spinner.setVisibility(View.VISIBLE);
                    RequestUtil.getInstance(LogEventActivity.this).PostPhoto(new Response.Listener() {
                        @Override
                        public void onResponse(Object response) {
                            if(response == null)
                            {
                                submit.setVisibility(View.VISIBLE);
                                spinner.setVisibility(View.GONE);
                                Toast.makeText(getApplicationContext(), "There was a problem posting the photo", Toast.LENGTH_LONG).show();
                            }
                            else
                            {
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    },name.getText().toString(),adapter1.getSpinnerItemId(activity.getSelectedItemPosition()),description.getText().toString(),adapter2.getSpinnerItemId(times.getSelectedItemPosition()),currentDate,f);
                }
            }
        });


    }

}
