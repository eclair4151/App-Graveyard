package com.shemeshapps.drexelregistrationassistant.Activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.shemeshapps.drexelregistrationassistant.CustomViews.TextViewButton;
import com.shemeshapps.drexelregistrationassistant.Helpers.ResourceHelper;
import com.shemeshapps.drexelregistrationassistant.Models.ClassInfo;
import com.shemeshapps.drexelregistrationassistant.Models.Professors;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsClass;
import com.shemeshapps.drexelregistrationassistant.R;

import org.apmem.tools.layouts.FlowLayout;
import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class WebtmsClassActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webtms_class);
        final WebtmsClass webtmsClass = Parcels.unwrap(this.getIntent().getParcelableExtra("webtms_class"));
        final ClassInfo classInfo = Parcels.unwrap(this.getIntent().getParcelableExtra("class_info"));

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setTitle(webtmsClass.class_id + " Section " +  webtmsClass.section);
        TextView textbook = (TextView)findViewById(R.id.webtms_class_textbooks);
        textbook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(webtmsClass.textbook_link));
                startActivity(browserIntent);
            }
        });

        TextView crn = (TextView)findViewById(R.id.webtms_crn);
        SpannableStringBuilder crnstr = new SpannableStringBuilder("CRN: " + webtmsClass.crn);
        crnstr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 4, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        crn.setText(crnstr);

        TextView classid = (TextView)findViewById(R.id.webtms_classid);
        SpannableStringBuilder classidstr = new SpannableStringBuilder("Class: " + webtmsClass.class_id);
        classidstr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        classid.setText(classidstr);

        TextView title = (TextView)findViewById(R.id.webtms_class_title);
        SpannableStringBuilder titlestr = new SpannableStringBuilder("Title: " + classInfo.title);
        titlestr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 6, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        title.setText(titlestr);

        TextView section = (TextView)findViewById(R.id.webtms_section);
        SpannableStringBuilder sectionstr = new SpannableStringBuilder("Section: " + webtmsClass.section);
        sectionstr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        section.setText(sectionstr);

        TextView credits = (TextView)findViewById(R.id.webtms_credits);
        SpannableStringBuilder creditsstr = new SpannableStringBuilder("Credits: " + webtmsClass.credits);
        creditsstr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        credits.setText(creditsstr);

        TextView campus = (TextView)findViewById(R.id.webtms_class_campus);
        SpannableStringBuilder campusStr = new SpannableStringBuilder("Campus: " + webtmsClass.campus);
        campusStr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 7, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        campus.setText(campusStr);

        TextView insttype = (TextView)findViewById(R.id.webtms_class_instruction_type);
        SpannableStringBuilder insttypestr = new SpannableStringBuilder("Instructor Type: " + webtmsClass.instruction_type);
        insttypestr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 16, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        insttype.setText(insttypestr);

        TextView instmethod = (TextView)findViewById(R.id.webtms_class_instruction_method);
        SpannableStringBuilder instmethodstr = new SpannableStringBuilder("Instructor Method:: " + webtmsClass.instruction_method);
        instmethodstr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 18, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        instmethod.setText(instmethodstr);

        TextView enroll = (TextView)findViewById(R.id.webtms_class_enrollment);
        SpannableStringBuilder enrollstr = new SpannableStringBuilder("Enrollment: " + webtmsClass.current_enroll + "/" + webtmsClass.max_enroll);
        enrollstr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 11, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        enroll.setText(enrollstr);

        TextView comments = (TextView)findViewById(R.id.webtms_class_comments);
        SpannableStringBuilder commentsstr = new SpannableStringBuilder("Section Comments: " + webtmsClass.section_comments);
        commentsstr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 17, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        comments.setText(commentsstr);

        TextView room = (TextView)findViewById(R.id.webtms_class_room);
        SpannableStringBuilder roomstr;
        if(webtmsClass.building.equals("TBD")) {
            roomstr = new SpannableStringBuilder("Room: " + webtmsClass.building);
        }
        else {
            roomstr = new SpannableStringBuilder("Room: " + webtmsClass.building + " " + webtmsClass.room);
        }
        roomstr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        room.setText(roomstr);

        TextView time = (TextView)findViewById(R.id.webtms_class_time);
        SpannableStringBuilder timestr = new SpannableStringBuilder("Time: " + webtmsClass.getFormatedTime());
        timestr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        time.setText(timestr);

        FlowLayout flow = (FlowLayout)findViewById(R.id.webtms_class_professors);
        int index = 0;
        for(final Professors p:webtmsClass.professors)
        {
            TextViewButton t = new TextViewButton(WebtmsClassActivity.this);
            final SpannableStringBuilder sb = new SpannableStringBuilder(p.first_last_name + ((index==webtmsClass.professors.length-1)?"":", "));
            final UnderlineSpan bss = new UnderlineSpan();
            sb.setSpan(bss, 0, ((index==webtmsClass.professors.length-1)?sb.length():sb.length()-2), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            t.setTextColor(ResourceHelper.getColor(R.color.link_blue,WebtmsClassActivity.this));
            t.setText(sb);
            flow.addView(t);
            t.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    profPopup(p);
                }
            });
            index++;
        }
    }

    public void profPopup(final Professors p)
    {
        final List<String> options = new ArrayList<>();
        if(p.koofers_num_ratings!=0)
        {
            options.add("View on Koofers.com");
        }

        if(p.rate_my_prof_num_ratings!=0)
        {
            options.add("View on Ratemyprofessors.com");
        }

        options.add("View on Google.com");
        options.add("View classes they are teaching");

        AlertDialog.Builder builder = new AlertDialog.Builder(WebtmsClassActivity.this);
        builder.setTitle("Select an Action")
                .setItems(options.toArray(new String[options.size()]), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        if(options.get(which).equals("View on Koofers.com"))
                        {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(p.koofers_url));
                            startActivity(browserIntent);
                        }
                        else if(options.get(which).equals("View on Ratemyprofessors.com"))
                        {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(p.rate_my_prof_url));
                            startActivity(browserIntent);
                        }
                        else if(options.get(which).equals("View on Google.com"))
                        {
                            Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(p.google_url));
                            startActivity(browserIntent);
                        }
                        else
                        {
                            finish();
                        }

                    }
                });
        builder.create().show();
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
