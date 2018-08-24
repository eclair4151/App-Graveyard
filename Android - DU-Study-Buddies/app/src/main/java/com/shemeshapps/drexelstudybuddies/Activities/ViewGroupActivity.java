package com.shemeshapps.drexelstudybuddies.Activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;

import com.parse.FunctionCallback;
import com.parse.ParseException;
import com.parse.SaveCallback;
import com.shemeshapps.drexelstudybuddies.Helpers.Utils;
import com.shemeshapps.drexelstudybuddies.Models.Group;
import com.shemeshapps.drexelstudybuddies.NetworkingServices.RequestUtil;
import com.shemeshapps.drexelstudybuddies.R;

public class ViewGroupActivity extends ActionBarActivity {

    /*
        Edited by Kelly on 3/14/15
    */
    Button delete;
    ProgressBar loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_group);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final Button join = (Button)findViewById(R.id.join_group_button);
        final Group g = (Group) getIntent().getSerializableExtra("group");
        loading = (ProgressBar)findViewById(R.id.group_info_loading);
        delete = (Button)findViewById(R.id.delete_group);
        TextView txtClass = (TextView) findViewById(R.id.txtClass);
        txtClass.setText("Class: " + g.course);
        TextView txtName = (TextView) findViewById(R.id.txtName);
        txtName.setText("Name: " + g.groupName);
        TextView txtLocation  = (TextView) findViewById(R.id.txtLocation);
        txtLocation.setText("Location: " + g.location);
        TextView txtCreator = (TextView) findViewById(R.id.txtCreator);
        txtCreator.setText("Creator: " + g.creator);
        TextView txtDate = (TextView) findViewById(R.id.txtDate);
        txtDate.setText("Date: " + Utils.formatDate(g.startTime));
        TextView txtTime = (TextView) findViewById(R.id.txtTime);
        txtTime.setText("Time: " + Utils.formatTime(g.startTime) + " - " + Utils.formatTime(g.endTime));
        TextView txtAttending = (TextView) findViewById(R.id.txtAttending);
        txtAttending.setText("People Attending: ");
        for(int i = 0; i < g.attendingUsers.size(); i++) {
            txtAttending.append(g.attendingUsers.get(i));
            if(i!=g.attendingUsers.size()-1)
            {
                txtAttending.append(", ");
            }
        }
        TextView txtDescription = (TextView) findViewById(R.id.txtDescription);
        txtDescription.setText("Description: " + g.description);

        updateButton(join,g);


        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Utils.didICreateGroup(g,getApplicationContext()))
                {
                    Intent i = new Intent(getApplicationContext(),CreateGroupActivity.class);
                    i.putExtra("group",g);
                    startActivity(i);
                    finish();
                }
                else
                {

                    loading.setVisibility(View.VISIBLE);
                    SaveCallback callback = new SaveCallback() {
                        @Override
                        public void done(ParseException e) {
                            updateButton(join, g);
                            loading.setVisibility(View.GONE);

                            TextView txtAttending = (TextView) findViewById(R.id.txtAttending);
                            txtAttending.setText("People Attending: ");
                            for(int i = 0; i < g.attendingUsers.size(); i++) {
                                txtAttending.append(g.attendingUsers.get(i));
                                if(i!=g.attendingUsers.size()-1)
                                {
                                    txtAttending.append(", ");
                                }
                            }
                        }
                    };

                    if(Utils.amIAttendingGroup(g,getApplicationContext())) {

                        RequestUtil.leaveStudyGroup(Utils.GroupToParseObject(g), callback);
                    }
                    else
                    {
                        RequestUtil.joinStudyGroup(Utils.GroupToParseObject(g), callback);
                    }
                }

            }
        });
    }

    private void updateButton(final Button join,final Group g)
    {
        if(Utils.didICreateGroup(g,this))
        {
            join.setText("Edit Group");
            delete.setVisibility(View.VISIBLE);
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new AlertDialog.Builder(ViewGroupActivity.this)
                            .setTitle("Delete Group?")
                            .setMessage("Are you sure you want to delete this study group?")
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    join.setEnabled(false);
                                    loading.setVisibility(View.VISIBLE);
                                    delete.setEnabled(false);
                                    RequestUtil.deleteStudyGroup(g.id,new FunctionCallback() {
                                        @Override
                                        public void done(Object o, ParseException e) {
                                            finish();
                                        }

                                        @Override
                                        public void done(Object o, Throwable throwable) {
                                            finish();
                                        }
                                    });
                                }
                            })
                            .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            })
                            .setIcon(android.R.drawable.ic_dialog_alert)
                            .show();
                }
            });
        }
        else if(Utils.amIAttendingGroup(g,this))
        {
            join.setText("Leave Group");
        }
        else
        {
            join.setText("Join Group");
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
