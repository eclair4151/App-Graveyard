package com.shemeshapps.drexelregistrationassistant.Activities;

import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.shemeshapps.drexelregistrationassistant.Adapters.TermExpandableListViewAdapter;
import com.shemeshapps.drexelregistrationassistant.CustomViews.ViewUpdater;
import com.shemeshapps.drexelregistrationassistant.Fragments.FilterFragmentDialog;
import com.shemeshapps.drexelregistrationassistant.Helpers.ErrorHelper;
import com.shemeshapps.drexelregistrationassistant.Models.ClassInfo;
import com.shemeshapps.drexelregistrationassistant.Models.Professor;
import com.shemeshapps.drexelregistrationassistant.Models.Term;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsClass;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsFilter;
import com.shemeshapps.drexelregistrationassistant.Networking.RequestUtil;
import com.shemeshapps.drexelregistrationassistant.R;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewProfessorActivity extends AppCompatActivity {

    View header;
    TermExpandableListViewAdapter webtmsClassAdapter;
    ProgressBar loadingBar;
    TextView noClassesText;
    Professor professor;
    ExpandableListView webtmsList;

    //load professor view
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_professor);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        professor = Parcels.unwrap(getIntent().getParcelableExtra("professor"));
        getSupportActionBar().setTitle(professor.first_last_name);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setContentView(R.layout.activity_view_class);
        webtmsList = (ExpandableListView)findViewById(R.id.webtms_expandable_result_list);
        webtmsClassAdapter = new TermExpandableListViewAdapter(this,new ArrayList<Term>(),webtmsList);
        webtmsList.setAdapter(webtmsClassAdapter);
        header = inflater.inflate(R.layout.professor_header_template, webtmsList, false);

        noClassesText = (TextView)header.findViewById(R.id.no_classes_text);
        loadingBar = (ProgressBar)header.findViewById(R.id.webtms_loading_progress);

        try
        {
            webtmsList.addHeaderView(header, null, false);
        }catch (Exception e)
        {
            ErrorHelper.LogError(e,"Add prof info header");
        }
        ViewUpdater.updateProfHeader(professor,header);
        loadingBar.setVisibility(View.VISIBLE);

        //load professor data to view
        TextView koofersLink = (TextView)header.findViewById(R.id.koofers_link);
        koofersLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(professor.koofers_url));
                startActivity(browserIntent);
            }
        });

        TextView rmpLink = (TextView)header.findViewById(R.id.rate_my_prof_link);
        rmpLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(professor.rate_my_prof_url));
                startActivity(browserIntent);
            }
        });

        TextView googleLink = (TextView)header.findViewById(R.id.google_link);
        googleLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(professor.google_url));
                startActivity(browserIntent);
            }
        });


        //show all classes that prof teaches
        webtmsList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Intent intent = new Intent(ViewProfessorActivity.this, WebtmsClassActivity.class);
                intent.putExtra("webtms_class", Parcels.wrap(webtmsClassAdapter.getChild(i,i1)));
                startActivity(intent);
                return false;
            }
        });


        webtmsList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                if(!webtmsClassAdapter.hasTermClasses(webtmsClassAdapter.getGroup(i)))
                {
                    webtmsClassAdapter.addLoading(webtmsClassAdapter.getGroup(i));
                    getWebtmsClasses(webtmsClassAdapter.getGroup(i),i);
                    return true;
                }
                return false;
            }
        });


        getClassTerms();
    }

    public void getClassTerms()
    {
        RequestUtil.getInstance(this).getProfTerms(professor.id, new Response.Listener<Term[]>() {
            @Override
            public void onResponse(Term[] response) {
                loadingBar.setVisibility(View.GONE);
                if(response.length ==0)
                {
                    noClassesText.setVisibility(View.VISIBLE);
                }
                else
                {
                    noClassesText.setVisibility(View.GONE);
                    webtmsClassAdapter.addGroups(new ArrayList<>(Arrays.asList(response)));
                }
            }
        });
    }

    public void getWebtmsClasses(final Term t, final int group)
    {
        RequestUtil.getInstance(this).getWebtmsClassesFromProf(professor.id, t, new Response.Listener<WebtmsClass[]>() {
            @Override
            public void onResponse(WebtmsClass[] response) {
                webtmsClassAdapter.removeLoading(t);
                webtmsClassAdapter.addClassesToTerm(t,new ArrayList<>(Arrays.asList(response)));
                webtmsList.expandGroup(group,true);

            }
        });
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
