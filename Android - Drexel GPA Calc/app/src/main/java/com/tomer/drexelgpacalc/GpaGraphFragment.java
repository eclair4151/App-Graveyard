package com.tomer.drexelgpacalc;

import android.graphics.Color;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.formatter.IValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;


public class GpaGraphFragment extends android.support.v4.app.Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.gpa_graph_fragment, container, false);

        //RelativeLayout contin = (RelativeLayout)view.findViewById(R.id.graph_container);
        LineChart chart = (LineChart)view.findViewById(R.id.chart);
        //chart.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

        //contin.addView(chart);

        List<Entry> entries = new ArrayList<Entry>();
        List<Entry> entries2 = new ArrayList<Entry>();


        int index = 1;
        int max = 0;

        float totalcredsofar = 0f;
        float totalgpasofar = 0f;
        for(ClassList[] year:MainActivity.terms)
        {
            boolean hasyear = false;
            for(ClassList term:year)
            {
                if(term.getGPA() != 0.0)
                {
                    entries.add(new Entry((float)index, (float)term.getGPA()));

                    totalcredsofar+= term.getCredits(false);
                    totalgpasofar+=term.getGPA() * term.getCredits(false);
                    entries2.add(new Entry((float)index, (float)Math.floor((totalgpasofar/totalcredsofar)*100)/100f));
                    hasyear = true;
                }


                index++;
            }
            if(hasyear)
            {
                max +=1;
            }
        }

        LineDataSet dataSet = new LineDataSet(entries, "Term GPA"); // add entries to dataset
        LineDataSet dataSet2 = new LineDataSet(entries2, "Overall GPA"); // add entries to dataset

        List<ILineDataSet> dataSets = new ArrayList<ILineDataSet>();
        dataSets.add(dataSet2);
        dataSets.add(dataSet);
        LineData lineData = new LineData(dataSets);
        //lineData.setValueFormatter(new MyValueFormatter());

        lineData.setDrawValues(false);
        chart.setData(lineData);

        dataSet.setLineWidth(5f);
        dataSet2.setLineWidth(5f);
        dataSet2.setColors(Color.rgb(255, 53, 53));
        dataSet.setColors(Color.rgb(12, 109, 255));

        dataSet2.setCircleColor(Color.rgb(0,0,0));
        dataSet.setCircleColor(Color.rgb(0,0,0));
        dataSet.setCircleColorHole(Color.rgb(0,0,0));
        dataSet2.setCircleColorHole(Color.rgb(0,0,0));

        chart.getAxisRight().setEnabled(false);
        XAxis top = chart.getXAxis();

        YAxis left = chart.getAxisLeft();
        left.setAxisMinimum(0);
        left.setAxisMaximum(4);
        left.setLabelCount(5,true);

        top.setLabelCount(6,true);
        top.setValueFormatter(new MyYAxisValueFormatter());
        top.setLabelRotationAngle(-60f);
        top.setAxisMinimum(1);
        top.setAxisMaximum(21);
        chart.setTouchEnabled(false);
        Description d = new Description();
        d.setText("");
        chart.setDescription(d);
        chart.invalidate();
        



        return view;
    }


    public class MyYAxisValueFormatter implements IAxisValueFormatter {

        private DecimalFormat mFormat;

        @Override
        public String getFormattedValue(float value, AxisBase axis) {
            // "value" represents the position of the label on the axis (x or y)
            if(value < 4)
            {
                return "     Freshman";
            }
            else if (value < 8)
            {
                return "     Sophomore";
            }
            else if (value < 12)
            {
                return "     Pre-Junior";
            }
            else if (value < 16)
            {
                return "     Junior";
            }
            else if (value < 20)
            {
                return "     Senior";
            }
            else
            {
                return "";
            }
            //return mFormat.format(value) + " $";
        }


    }

}
