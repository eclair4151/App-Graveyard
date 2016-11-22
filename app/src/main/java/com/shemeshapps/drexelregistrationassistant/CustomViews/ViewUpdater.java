package com.shemeshapps.drexelregistrationassistant.CustomViews;

import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

import com.shemeshapps.drexelregistrationassistant.Models.ClassInfo;
import com.shemeshapps.drexelregistrationassistant.Models.Professor;
import com.shemeshapps.drexelregistrationassistant.R;

import org.w3c.dom.Text;

/**
 * Created by tomer on 3/4/16.
 */
public class ViewUpdater {
    public static void updateClassHeader(ClassInfo info, View header)
    {
        TextView classid = (TextView)header.findViewById(R.id.class_header_class_id);
        classid.setText(info.class_id + (info.writing_intensive?" [WI]":""));

        TextView title = (TextView)header.findViewById(R.id.class_header_class_title);
        title.setText(info.title);

        TextView credits = (TextView)header.findViewById(R.id.class_header_credits);
        credits.setText("Credits: " + ((info.credits_lower_range == info.credits_upper_range)?Float.toString(info.credits_lower_range):(Float.toString(info.credits_lower_range) + "-" + Float.toString(info.credits_upper_range))));

        TextView description = (TextView)header.findViewById(R.id.class_header_description);
        description.setText(info.description);


        TextView college = (TextView)header.findViewById(R.id.class_header_class_college);
        if(info.college!=null)
        {
            college.setVisibility(View.VISIBLE);
            SpannableStringBuilder collegestr = new SpannableStringBuilder("College: " + info.college);
            collegestr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 8, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            college.setText(collegestr);
        }
        else
        {
            college.setVisibility(View.GONE);
        }

        TextView repeat = (TextView)header.findViewById(R.id.class_header_class_repeat_status);
        if(info.repeat_status!= null)
        {
            repeat.setVisibility(View.VISIBLE);
            SpannableStringBuilder repeatstr = new SpannableStringBuilder("Repeat Status: " + info.repeat_status);
            repeatstr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            repeat.setText(repeatstr);
        }
        else
        {
            repeat.setVisibility(View.GONE);
        }

        TextView restrictions = (TextView)header.findViewById(R.id.class_header_class_restrictions);
        if (info.restrictions!=null)
        {
            restrictions.setVisibility(View.VISIBLE);
            SpannableStringBuilder restrictionsstr = new SpannableStringBuilder("Restrictions: " + info.restrictions);
            restrictionsstr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 13, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            restrictions.setText(restrictionsstr);
        }
        else
        {
            restrictions.setVisibility(View.GONE);
        }

        TextView prereqs = (TextView)header.findViewById(R.id.class_header_class_prereqs);
        if (info.prereqs!=null)
        {
            prereqs.setVisibility(View.VISIBLE);
            SpannableStringBuilder prereqsstr = new SpannableStringBuilder("Prerequisites: " + info.prereqs);
            prereqsstr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            prereqs.setText(prereqsstr);
        }
        else
        {
            prereqs.setVisibility(View.GONE);
        }

        TextView coreqs = (TextView)header.findViewById(R.id.class_header_class_coreqs);
        if (info.coreqs!=null)
        {
            coreqs.setVisibility(View.VISIBLE);
            SpannableStringBuilder coreqsstr = new SpannableStringBuilder("Corequisites: " + info.coreqs);
            coreqsstr.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD), 0, 14, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            coreqs.setText(coreqsstr);
        }
        else
        {
            coreqs.setVisibility(View.GONE);
        }
    }

    public static void updateProfHeader(Professor prof, View header)
    {
        TextView name = (TextView)header.findViewById(R.id.prof_name);
        name.setText(prof.first_last_name);

        if(prof.rate_my_prof_num_ratings != 0)
        {
            TextView rmpLink = (TextView)header.findViewById(R.id.rate_my_prof_link);
            final SpannableStringBuilder sb = new SpannableStringBuilder("Rate My Professor");
            final UnderlineSpan bss = new UnderlineSpan();
            sb.setSpan(bss, 0, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            rmpLink.setText(sb);

            RatingBar rating = (RatingBar)header.findViewById(R.id.rate_my_prof_star_rating);
            rating.setRating(prof.rate_my_prof_rating);

            TextView numRating = (TextView)header.findViewById(R.id.rate_my_prof_num_ratings);
            numRating.setText("(" + prof.rate_my_prof_num_ratings + ")");

            rmpLink.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                }
            });
        }
        else
        {
            View rmp = header.findViewById(R.id.rate_my_prof_layout);
            rmp.setVisibility(View.GONE);
        }

        if(prof.koofers_num_ratings != 0)
        {
            TextView rmpLink = (TextView)header.findViewById(R.id.koofers_link);
            final SpannableStringBuilder sb = new SpannableStringBuilder("Koofers");
            final UnderlineSpan bss = new UnderlineSpan();
            sb.setSpan(bss, 0, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
            rmpLink.setText(sb);

            RatingBar rating = (RatingBar)header.findViewById(R.id.koofers_star_rating);
            rating.setRating(prof.koofers_rating);

            TextView numRating = (TextView)header.findViewById(R.id.koofers_num_ratings);
            numRating.setText("(" + prof.koofers_num_ratings + ")");
        }
        else
        {
            View rmp = header.findViewById(R.id.koofers_layout);
            rmp.setVisibility(View.GONE);
        }

        TextView google = (TextView)header.findViewById(R.id.google_link);
        final SpannableStringBuilder sb = new SpannableStringBuilder("View On Google");
        final UnderlineSpan bss = new UnderlineSpan();
        sb.setSpan(bss, 0, sb.length(), Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        google.setText(sb);
    }
}
