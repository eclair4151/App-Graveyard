package com.shemeshapps.drexelregistrationassistant.Fragments;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Response;
import com.shemeshapps.drexelregistrationassistant.Activities.ViewClassActivity;
import com.shemeshapps.drexelregistrationassistant.Activities.WebtmsClassActivity;
import com.shemeshapps.drexelregistrationassistant.Adapters.WebtmsClassAdapter;
import com.shemeshapps.drexelregistrationassistant.CustomViews.ViewUpdater;
import com.shemeshapps.drexelregistrationassistant.Models.ClassInfo;
import com.shemeshapps.drexelregistrationassistant.Models.Colleges;
import com.shemeshapps.drexelregistrationassistant.Models.Subjects;
import com.shemeshapps.drexelregistrationassistant.Models.Term;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsClass;
import com.shemeshapps.drexelregistrationassistant.Networking.RequestUtil;
import com.shemeshapps.drexelregistrationassistant.R;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * Created by Tomer on 2/7/16.
 */
//temp page for browsing. i know this is ugly, but what ever it works for now.
public class BrowseFragment extends Fragment {

    View parentView;
    ArrayList<String> values = new ArrayList<>();
    int currPage = 0;
    ArrayAdapter<String> adapter;
    WebtmsClassAdapter webtmsAdapter;
    Term[] terms;
    Colleges[] subjects;
    ClassInfo[] classes;
    ListView list;
    SwipeRefreshLayout refreshLayout;
    View header;
    LayoutInflater inflater;
    int selectedTerm;
    int selectedSubject;
    int selectedCollege;
    int selectedClass;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.browse_webtms_fragment, container, false);
        header = (View)inflater.inflate(R.layout.class_info_header, list, false);
        ImageView filterButton = (ImageView)header.findViewById(R.id.filter_button);
        filterButton.setVisibility(View.GONE);
        list = (ListView)parentView.findViewById(R.id.browse_webtms_list);
        refreshLayout = (SwipeRefreshLayout)parentView.findViewById(R.id.browseWebtmsRefresh);
        refreshLayout.setEnabled(false);
        refreshLayout.setColorSchemeResources(
                R.color.refresh_progress_1,
                R.color.refresh_progress_2,
                R.color.refresh_progress_3);
        values.clear();
        currPage = 0;
        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.simple_list_item, android.R.id.text1, values);

        webtmsAdapter = new WebtmsClassAdapter(getActivity(),new ArrayList<WebtmsClass>());


        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(currPage==4)
                {
                    Intent intent = new Intent(getActivity(), WebtmsClassActivity.class);
                    intent.putExtra("webtms_class", Parcels.wrap(webtmsAdapter.getItem(i-1)));
                    getActivity().startActivity(intent);
                }
                else
                {
                    updateScreen(i);
                }
            }
        });
        setTerms();
        return parentView;
    }

    //user gross pages as ints and just stuff data into list
    private void updateScreen(int i)
    {
        currPage++;
        if(currPage==0)
        {
            setTerms();
        }
        else if(currPage==1)
        {
            selectedTerm = i;
            setColleges();
        }
        else if(currPage==2)
        {
            selectedCollege = i;
            setSubjects();
        }
        else if(currPage==3)
        {
            selectedSubject = i;
            setClasses();
        }
        else if(currPage==4)
        {
            selectedClass = i;
            currPage--;
            setWebtms();
        }
    }
    private void setColleges()
    {
        if(list.getHeaderViewsCount() != 0)
        {
            list.removeHeaderView(header);
        }
        values.clear();
        RequestUtil.getInstance(getActivity()).getSubjects(new Response.Listener<Colleges[]>() {
            @Override
            public void onResponse(Colleges[] response) {
                subjects =  response;
                for (Colleges t:response)
                {
                    values.add(t.college);
                }
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setSubjects()
    {
        //college is center for hospitality
        if(list.getHeaderViewsCount() != 0)
        {
            list.removeHeaderView(header);
        }
        values.clear();
        for (Subjects t:subjects[selectedCollege].subjects)
        {
            values.add(t.subject);
        }
        adapter.notifyDataSetChanged();
    }

    private void setClasses()
    {
        if(list.getHeaderViewsCount() != 0)
        {
            list.removeHeaderView(header);
        }
        values.clear();
        list.setAdapter(adapter);
        refreshLayout.setRefreshing(true);
        RequestUtil.getInstance(getActivity()).getClassesInSubjectTerm(subjects[selectedCollege].subjects[selectedSubject].subject,terms[selectedTerm],new Response.Listener<ClassInfo[]>() {
            @Override
            public void onResponse(ClassInfo[] response) {
                classes = response;
                for (ClassInfo t: response)
                {
                    values.add(t.class_id + " - " + t.title);
                }
                refreshLayout.setRefreshing(false);
                adapter.notifyDataSetChanged();
            }
        });
    }

    private void setWebtms()
    {
        Intent intent = new Intent(getActivity(), ViewClassActivity.class);
        intent.putExtra("ClassInfo", Parcels.wrap(classes[selectedClass]));
        intent.putExtra("Term",Parcels.wrap(terms[selectedTerm]));
        startActivity(intent);
//        list.setAdapter(webtmsAdapter);
//
//
//        if(list.getHeaderViewsCount() == 0)
//        {
//            ViewUpdater.updateClassHeader(classes[selectedClass],header);
//            list.addHeaderView(header, null, false);
//        }
//
//        webtmsAdapter.clear();
//        refreshLayout.setRefreshing(true);
//        RequestUtil.getInstance(getActivity()).getWebtmsClasses(classes[selectedClass].class_id, terms[selectedTerm], new Response.Listener<WebtmsClass[]>() {
//            @Override
//            public void onResponse(WebtmsClass[] response) {
//                webtmsAdapter.clear();
//                webtmsAdapter.addAll(Arrays.asList(response));
//                refreshLayout.setRefreshing(false);
//            }
//        });
    }

    private void setTerms()
    {
        values.clear();
        RequestUtil.getInstance(getActivity()).getTerms(new Response.Listener<Term[]>() {
            @Override
            public void onResponse(Term[] response) {
                terms = response;
                for (Term t:response)
                {
                    values.add(t.toString());
                }
                adapter.notifyDataSetChanged();

            }
        });
    }

    public boolean goBack()
    {
        if(currPage == 0)
        {
            return false;
        }
        else
        {
            currPage--;
            if(currPage==0)
            {
                setTerms();
            }
            else if(currPage==1)
            {
                setColleges();
            }
            else if(currPage==2)
            {
                setSubjects();
            }
            else if(currPage==3)
            {
                setClasses();
            }

            return true;

        }
    }
}
