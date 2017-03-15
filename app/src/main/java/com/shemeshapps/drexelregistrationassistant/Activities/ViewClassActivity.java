package com.shemeshapps.drexelregistrationassistant.Activities;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
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
import com.shemeshapps.drexelregistrationassistant.Models.Term;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsClass;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsFilter;
import com.shemeshapps.drexelregistrationassistant.Networking.RequestUtil;
import com.shemeshapps.drexelregistrationassistant.R;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;

public class ViewClassActivity extends AppCompatActivity implements FilterFragmentDialog.OnCompleteListener{

    View header;
    TermExpandableListViewAdapter webtmsClassAdapter;
    ProgressBar loadingBar;
    TextView noClassesText;
    ClassInfo classInfo;
    ImageView filterButton;
    WebtmsFilter filter= null;
    ExpandableListView webtmsList;
    ImageView sortButton;

    //setup all layout
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        classInfo = Parcels.unwrap(getIntent().getParcelableExtra("ClassInfo"));
        getSupportActionBar().setTitle(classInfo.class_id);
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        setContentView(R.layout.activity_view_class);
        webtmsList = (ExpandableListView)findViewById(R.id.webtms_expandable_result_list);
        webtmsClassAdapter = new TermExpandableListViewAdapter(this,new ArrayList<Term>(),webtmsList);
        webtmsList.setAdapter(webtmsClassAdapter);
        header = inflater.inflate(R.layout.class_info_header, webtmsList, false);
        noClassesText = (TextView)header.findViewById(R.id.no_classes_text);
        loadingBar = (ProgressBar)header.findViewById(R.id.webtms_loading_progress);
        filterButton = (ImageView)header.findViewById(R.id.filter_button);
        try
        {
            webtmsList.addHeaderView(header, null, false);
        }catch (Exception e)
        {
            ErrorHelper.LogError(e,"Add webtms search header");
        }
        ViewUpdater.updateClassHeader(classInfo,header);
        loadingBar.setVisibility(View.VISIBLE);

        //when clicking on a class
        webtmsList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Intent intent = new Intent(ViewClassActivity.this, WebtmsClassActivity.class);
                intent.putExtra("webtms_class", Parcels.wrap(webtmsClassAdapter.getChild(i,i1)));
                startActivity(intent);
                return false;
            }
        });

        //expand term when clicked on. if we ont have data show a loader and get it
        webtmsList.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView expandableListView, View view, int i, long l) {
                if(!webtmsClassAdapter.hasTermClasses(webtmsClassAdapter.getGroup(i)))
                {
                    webtmsClassAdapter.addLoading(webtmsClassAdapter.getGroup(i));
                    getWebtmsClasses(classInfo.class_id,webtmsClassAdapter.getGroup(i),i);
                    return true;
                }
                return false;
            }
        });

        final ProgressBar filterLoading = (ProgressBar)findViewById(R.id.filter_loading);

        //on filter click if we dont have it get it. should not have duplicated code here...
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(filter == null)
                {
                    filterLoading.setVisibility(View.VISIBLE);
                    RequestUtil.getInstance(ViewClassActivity.this).getClassFilters(classInfo.class_id, new Response.Listener<WebtmsFilter>() {
                        @Override
                        public void onResponse(WebtmsFilter response) {
                            filter = response;
                            FragmentManager fm = getFragmentManager();
                            FilterFragmentDialog editNameDialog = new FilterFragmentDialog();
                            Bundle b = new Bundle();
                            b.putParcelable("filter", Parcels.wrap(response));
                            editNameDialog.setArguments(b);
                            editNameDialog.show(fm, "filter_fragment");
                            filterLoading.setVisibility(View.GONE);
                        }
                    });
                }
                else
                {
                    FragmentManager fm = getFragmentManager();
                    FilterFragmentDialog editNameDialog = new FilterFragmentDialog();
                    Bundle b = new Bundle();
                    b.putParcelable("filter", Parcels.wrap(filter));
                    editNameDialog.setArguments(b);
                    editNameDialog.show(fm, "filter_fragment");
                }

            }
        });

        //sort, which is disabled for now
        sortButton = (ImageView)findViewById(R.id.class_sort_button);
        sortButton.setVisibility(View.GONE);
        sortButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(ViewClassActivity.this);
                String[] options = {"Section", "Day/Time", "Professor Rating", "Spots Available"};
                builder.setTitle("Sort By")
                        .setItems(options, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        });
                builder.create().show();
            }
        });

        getClassTerms();
    }


    //gets all terms this class if offered
    public void getClassTerms()
    {
        RequestUtil.getInstance(this).getClassTerms(classInfo.class_id, new Response.Listener<Term[]>() {
            @Override
            public void onResponse(Term[] response) {
                loadingBar.setVisibility(View.GONE);
                if(response.length ==0)
                {
                    noClassesText.setVisibility(View.VISIBLE);
                    filterButton.setVisibility(View.GONE);
                    sortButton.setVisibility(View.GONE);

                }
                else
                {
                    noClassesText.setVisibility(View.GONE);
                    filterButton.setVisibility(View.VISIBLE);
                    //sortButton.setVisibility(View.VISIBLE);
                    webtmsClassAdapter.addGroups(new ArrayList<>(Arrays.asList(response)));
                }
            }
        });
    }

    //gets classes for a term
    public void getWebtmsClasses(String classid, final Term t, final int group)
    {
        RequestUtil.getInstance(this).getWebtmsClasses(classid, t, new Response.Listener<WebtmsClass[]>() {
            @Override
            public void onResponse(WebtmsClass[] response) {
                webtmsClassAdapter.removeLoading(t);
                webtmsClassAdapter.addClassesToTerm(t,new ArrayList<>(Arrays.asList(response)));
                webtmsList.expandGroup(group,true);

            }
        });
    }

    //gets called when you press filter
    @Override
    public void onComplete(WebtmsFilter filter) {
        if(filter != null)
        {
            this.filter = filter;
            webtmsClassAdapter.addFilter(filter);
        }

    }

    //filter is reset
    @Override
    public void onResetAll() {
        filter.filteredDays = null;
        filter.starttime = -1;
        filter.endtime = -1;
        filter.showFull = true;
        filter.filteredInstruction_types = null;
        filter.filteredCampus = null;
        filter.filteredInstruction_methods = null;
        filter.filteredProfessors = null;
        webtmsClassAdapter.removeFilter();
    }

    //add back button
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
