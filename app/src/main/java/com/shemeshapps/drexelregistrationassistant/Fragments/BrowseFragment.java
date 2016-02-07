package com.shemeshapps.drexelregistrationassistant.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.shemeshapps.drexelregistrationassistant.R;

import java.util.ArrayList;


/**
 * Created by Tomer on 2/7/16.
 */
public class BrowseFragment extends Fragment {

    View parentView;
    ArrayList<String> values = new ArrayList<>();
    int currPage = 0;
    ArrayAdapter<String> adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        parentView = inflater.inflate(R.layout.browse_webtms_fragment, container, false);
        ListView list = (ListView)parentView.findViewById(R.id.browse_webtms_list);
        values.add("Fall Quarter 15-16");
        values.add("Winter Quarter 15-16");
        values.add("Spring Quarter 15-16");
        values.add("Summer Quarter 15-16");

        adapter = new ArrayAdapter<String>(getActivity(),
                R.layout.simple_list_item, android.R.id.text1, values);
        list.setAdapter(adapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(currPage==0)
                {
                    setColleges();
                    currPage++;
                }
                else if(currPage==1)
                {
                    setSubjects();
                    currPage++;
                }
                else if(currPage==2)
                {
                    setClasses();
                    currPage++;
                }
                else if(currPage==3)
                {
                    setWebtms();
                    currPage++;
                }
            }
        });
        return parentView;
    }

    private void setColleges()
    {
        values.clear();
        values.add("Antoinette Westphal College of Media Arts & Design");
        values.add("Center for Civic Engagement");
        values.add("Center for Hospitality and Sport Management");
        values.add("Close School of Entrepreneurship");
        values.add("College of Arts and Sciences");
        values.add("College of Computing and Informatics");
        values.add("College of Engineering");
        values.add("College of Nursing & Health Professions");
        values.add("COM School of Biomedical Sciences & Professional Studies");
        values.add("Goodwin College of Professional Studies");
        values.add("LeBow College of Business");
        values.add("Pennoni Honors College");
        values.add("School of Biomedical Engineering, Science & Health Systems");
        values.add("School of Education");
        values.add("School of Public Health");
        values.add("University Courses");
        adapter.notifyDataSetChanged();
    }

    private void setSubjects()
    {
        //college is center for hospitality
        values.clear();
        values.add("Culinary Arts");
        values.add("Food Science");
        values.add("Hotel & Restaurant Management");
        values.add("Sport Management");
        adapter.notifyDataSetChanged();
    }

    private void setClasses()
    {
        values.clear();
        values.add("CULA115 - Culinary Fundamentals");
        values.add("CULA120 - Techniques and Traditions I");
        values.add("CULA125 - Foundations of Professional Baking");
        values.add("CULA315 - Fundamentals of American Cuisine");
        adapter.notifyDataSetChanged();
    }

    private void setWebtms()
    {

    }
}
