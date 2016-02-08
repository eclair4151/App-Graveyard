package com.shemeshapps.drexelregistrationassistant.Fragments;

import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.CompletionInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Response;
import com.shemeshapps.drexelregistrationassistant.Activities.WebtmsClassActivity;
import com.shemeshapps.drexelregistrationassistant.Adapters.ClassSearchAutoCompleteAdapter;
import com.shemeshapps.drexelregistrationassistant.Adapters.SmallTermSpinnerAdapter;
import com.shemeshapps.drexelregistrationassistant.Adapters.TermExpandableListViewAdapter;
import com.shemeshapps.drexelregistrationassistant.Adapters.TermSpinnerAdapter;
import com.shemeshapps.drexelregistrationassistant.Adapters.WebtmsClassAdapter;
import com.shemeshapps.drexelregistrationassistant.CustomViews.ClearableAutoCompleteTextView;
import com.shemeshapps.drexelregistrationassistant.Models.ClassInfo;
import com.shemeshapps.drexelregistrationassistant.Models.Term;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsClass;
import com.shemeshapps.drexelregistrationassistant.Networking.RequestUtil;
import com.shemeshapps.drexelregistrationassistant.R;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SearchByClass extends Fragment {

    View parentView;
    View header;
    TermExpandableListViewAdapter webtmsClassAdapter;
    ProgressBar loadingBar;
    TextView noClassesText;
    ClassInfo currentClass;
    ImageView filterButton;
    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.fragment_search_by_class, container, false);
        final ClearableAutoCompleteTextView autoCompleteTextView = (ClearableAutoCompleteTextView)parentView.findViewById(R.id.auto_complete_class_Search);

        final ClassSearchAutoCompleteAdapter adapter = new ClassSearchAutoCompleteAdapter(getActivity(), new ArrayList<ClassInfo>());
        autoCompleteTextView.setAdapter(adapter);

        final ExpandableListView webtmsList = (ExpandableListView)parentView.findViewById(R.id.search_by_class_result_list);
        webtmsClassAdapter = new TermExpandableListViewAdapter(getActivity(),new ArrayList<Term>(),webtmsList);
        webtmsList.setAdapter(webtmsClassAdapter);

        autoCompleteTextView.setOnClearListener(new ClearableAutoCompleteTextView.OnClearListener() {
            @Override
            public void onClear() {
                autoCompleteTextView.setText("");
                adapter.clear();
            }
        });


        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(autoCompleteTextView.getWindowToken(), 0);

                if(webtmsList.getHeaderViewsCount() == 0)
                {
                    header = (View)inflater.inflate(R.layout.class_info_header, webtmsList, false);
                    noClassesText = (TextView)header.findViewById(R.id.no_classes_text);
                    loadingBar = (ProgressBar)header.findViewById(R.id.webtms_loading_progress);
                    filterButton = (ImageView)header.findViewById(R.id.filter_button);
                    webtmsList.addHeaderView(header, null, false);
                }

                ClassInfo info = adapter.getItem(i);
                updateHeader(info);
                adapter.clear();
                webtmsClassAdapter.clear();
                loadingBar.setVisibility(View.VISIBLE);
                filterButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        FragmentManager fm = getActivity().getFragmentManager();
                        FilterFragmentDialog editNameDialog = new FilterFragmentDialog();
                        editNameDialog.show(fm, "filter_fragment");
                    }
                });
                getClassTerms();
            }
        });

        webtmsList.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView expandableListView, View view, int i, int i1, long l) {
                Intent intent = new Intent(getActivity(), WebtmsClassActivity.class);
                intent.putExtra("webtms_class", Parcels.wrap(webtmsClassAdapter.getChild(i,i1)));
                intent.putExtra("class_info", Parcels.wrap(currentClass));
                getActivity().startActivity(intent);
                return false;
            }
        });

        webtmsList.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {
            @Override
            public void onGroupExpand(int i) {
                getWebtmsClasses(currentClass.class_id,(Term)webtmsClassAdapter.getGroup(i));
            }
        });


        return parentView;
    }

    public void getWebtmsClasses(String classid, final Term t)
    {
        RequestUtil.getInstance(getActivity()).getWebtmsClasses(classid, t, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                webtmsClassAdapter.addClassesToTerm(t,Arrays.asList((WebtmsClass[]) response));

            }
        });
    }

    public void getClassTerms()
    {
        RequestUtil.getInstance(getActivity()).getClassTerms(1, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                loadingBar.setVisibility(View.GONE);
                if(((Term[]) response).length ==0)
                {
                    noClassesText.setVisibility(View.VISIBLE);
                }
                else
                {
                    noClassesText.setVisibility(View.GONE);
                    webtmsClassAdapter.addGroups(Arrays.asList((Term[])response));
                }
            }
        });
    }


    private void updateHeader(ClassInfo info)
    {
        currentClass = info;
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
}
