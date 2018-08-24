package com.shemeshapps.drexelstudybuddies.Activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TimePicker;
import android.widget.Toast;

import com.parse.FunctionCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.shemeshapps.drexelstudybuddies.Helpers.Utils;
import com.shemeshapps.drexelstudybuddies.Models.Group;
import com.shemeshapps.drexelstudybuddies.NetworkingServices.RequestUtil;
import com.shemeshapps.drexelstudybuddies.R;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class CreateGroupActivity extends ActionBarActivity {

    EditText txtDate, txtStartTime, txtEndTime, txtGroupName, txtLocation, txtCourse, txtDescr;
    ProgressBar loading;
    Button create;
    LinearLayout root;
    private int mYear, mMonth, mDay, mStartHour, mStartMinute, mEndHour,mEndMinute;
    Group editingGroup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        editingGroup = (Group) getIntent().getSerializableExtra("group");
        txtDate = (EditText)findViewById(R.id.date_txt);
        txtStartTime = (EditText)findViewById(R.id.start_time_txt);
        txtEndTime = (EditText) findViewById(R.id.end_time_txt);
        txtGroupName = (EditText) findViewById(R.id.grp_name);
        txtCourse = (EditText)findViewById(R.id.course_txt);
        txtLocation = (EditText)findViewById(R.id.location_txt);
        txtDescr = (EditText)findViewById(R.id.desc_txt);
        loading = (ProgressBar)findViewById(R.id.create_group_loader);
        create = (Button)findViewById(R.id.submit_grp_btn);
        root = (LinearLayout)findViewById(R.id.createGroupRoot);
        final Calendar c = Calendar.getInstance();
        mStartHour = c.get(Calendar.HOUR_OF_DAY);
        mStartMinute = c.get(Calendar.MINUTE);
        mEndHour = c.get(Calendar.HOUR_OF_DAY);
        mEndMinute = c.get(Calendar.MINUTE);
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        txtDate.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                DatePickerDialog dpd = new DatePickerDialog(CreateGroupActivity.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                mYear = year;
                                mMonth = monthOfYear;
                                mDay = dayOfMonth;
                                txtDate.setText(Utils.formatDate(monthOfYear, dayOfMonth, year));
                                txtDate.setError(null);
                            }
                        }, mYear, mMonth, mDay);
                dpd.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                dpd.show();
            }

        });


        txtStartTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                TimePickerDialog tpd = new TimePickerDialog(CreateGroupActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {
                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,int minute) {
                                mStartHour = hourOfDay;
                                mStartMinute = minute;
                                txtStartTime.setText(Utils.formatTime(mStartHour, mStartMinute));
                                txtStartTime.setError(null);
                            }
                        }, mStartHour, mStartMinute, false);

                tpd.show();

            }

        });

        txtEndTime.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                TimePickerDialog tpd = new TimePickerDialog(CreateGroupActivity.this,
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {
                                mEndHour = hourOfDay;
                                mEndMinute = minute;
                                txtEndTime.setText(Utils.formatTime(mEndHour, mEndMinute));
                                txtEndTime.setError(null);
                            }
                        }, mEndHour, mEndMinute, false);
                tpd.show();

            }

        });

        if(editingGroup!=null)
        {
            setEditingGroup();
        }
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createGroup();
            }
        });
    }


    private void setEditingGroup()
    {
        create.setText("Update");
        getSupportActionBar().setTitle("Update Group");
        txtGroupName.setText(editingGroup.groupName);
        txtDate.setText(Utils.formatDate(editingGroup.startTime));
        txtStartTime.setText(Utils.formatTime(editingGroup.startTime));
        txtEndTime.setText(Utils.formatTime(editingGroup.endTime));
        txtLocation.setText(editingGroup.location);
        txtCourse.setText(editingGroup.course);
        txtDescr.setText(editingGroup.description);

        Calendar c = new GregorianCalendar();
        c.setTime(editingGroup.startTime);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mMonth = c.get(Calendar.MONTH);
        mYear = c.get(Calendar.YEAR);
        mStartHour = c.get(Calendar.HOUR_OF_DAY);
        mStartMinute = c.get(Calendar.MINUTE);

        Calendar c2 = new GregorianCalendar();
        c2.setTime(editingGroup.endTime);
        mEndHour = c2.get(Calendar.HOUR_OF_DAY);
        mEndMinute = c2.get(Calendar.MINUTE);

    }

    private void createGroup()
    {
        int childcount = root.getChildCount();
        boolean missingField = false;
        for (int i=0; i < childcount; i++){
            View v = root.getChildAt(i);
            if(v instanceof EditText)
            {
                if(((EditText)v).getText().toString().isEmpty())
                {
                    ((EditText)v).setError("Required Field");
                    missingField = true;
                }
                else
                {
                    ((EditText)v).setError(null);
                }
            }
        }

        if(!missingField)
        {
            boolean timerError = false;
            long studyTime;
            final long minStudyTime = 30L * 60 * 1000;
            final int maxDays = 14;
            Date maxStartDate;


            if(editingGroup== null)
                editingGroup = new Group();

            Calendar startCal = Calendar.getInstance();
            startCal.set(Calendar.HOUR_OF_DAY,mStartHour);
            startCal.set(Calendar.MINUTE,mStartMinute);
            startCal.set(Calendar.DAY_OF_MONTH,mDay);
            startCal.set(Calendar.MONTH,mMonth);
            startCal.set(Calendar.YEAR,mYear);
            editingGroup.startTime = startCal.getTime();

            Calendar endCal = Calendar.getInstance();
            endCal.set(Calendar.HOUR_OF_DAY,mEndHour);
            endCal.set(Calendar.MINUTE,mEndMinute);
            endCal.set(Calendar.DAY_OF_MONTH,mDay);
            endCal.set(Calendar.MONTH,mMonth);
            endCal.set(Calendar.YEAR,mYear);
            editingGroup.endTime = endCal.getTime();

            Calendar maxCal = Calendar.getInstance();
            maxCal.add(Calendar.DAY_OF_YEAR, maxDays);
            maxStartDate = maxCal.getTime();

            studyTime = (editingGroup.endTime.getTime() - editingGroup.startTime.getTime());

            if(editingGroup.endTime.before(editingGroup.startTime))
            {
                txtEndTime.setError("End time is before start time");
                timerError = true;
            }

            if(editingGroup.startTime.before(new Date()))
            {
                txtStartTime.setError("Start time is before current time");
                timerError = true;
            }

            if(studyTime < minStudyTime)
            {
                txtEndTime.setError("Study time is less than " + minStudyTime/(60 * 1000) + " minutes");
                timerError = true;
            }

            if(editingGroup.startTime.after(maxStartDate))
            {
                txtDate.setError("Start time is more than " + maxDays + " days from current time");
                timerError = true;
            }

            if(!timerError)
            {
                create.setEnabled(false);
                loading.setVisibility(View.VISIBLE);
                editingGroup.course = txtCourse.getText().toString();
                editingGroup.description = txtDescr.getText().toString();
                editingGroup.groupName = txtGroupName.getText().toString();
                editingGroup.location = txtLocation.getText().toString();

                RequestUtil.postStudyGroup(editingGroup, new SaveCallback() {
                    @Override
                    public void done(ParseException e) {
                        if(e==null)
                        {
                            finish();
                        }
                        else
                        {
                            create.setEnabled(true);
                            loading.setVisibility(View.GONE);
                            Toast.makeText(getApplicationContext(),"An error has occurred",Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }


        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        switch (menuItem.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return (super.onOptionsItemSelected(menuItem));
    }
}
