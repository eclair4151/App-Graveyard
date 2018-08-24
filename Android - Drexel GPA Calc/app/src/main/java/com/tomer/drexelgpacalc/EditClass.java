package com.tomer.drexelgpacalc;

import com.tomer.drexelgpacalc.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

public class EditClass extends Activity{
      EditText name;
      EditText credit;
      int position;
      Spinner gradespinner;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.editclass);
		gradespinner = (Spinner)findViewById(R.id.expandableListView2e);
		name = (EditText)findViewById(R.id.editor_name);
		credit = (EditText)findViewById(R.id.editor_credit);
		position = MainActivity.currentclassposition;

		name.setText(MainActivity.currentclass);
		credit.setText(String.format("%.1f", MainActivity.currentclasscredit));
		
		gradespinner.setSelection(grade2id(MainActivity.currentclassgrade)); 
		
	}
	
	
	
	public int grade2id(String grade)
	{
		int position;
		if (grade.contains("CR")) {
			position = 13;
		} else if (grade.contains("NC")) {
			position = 14;
		} else if (grade.contains("A+")) {
			position = 0;
		} else if (grade.contains("A-")) {
			position = 2;
		} else if (grade.contains("A")) {
			position = 1;
		} else if (grade.contains("B+")) {
			position = 3;
		} else if (grade.contains("B-")) {
			position = 5;
		} else if (grade.contains("B")) {
			position = 4;
		} else if (grade.contains("C+")) {
			position = 6;
		} else if (grade.contains("C-")) {
			position = 8;
		} else if (grade.contains("C")) {
			position = 7;
		} else if (grade.contains("D+")) {
			position = 9;
		} else if (grade.contains("D-")) {
			position = 11;
		} else if (grade.contains("D")) {
			position = 10;
		}

		else {
			position = 12;
		}	
		
		return position;
	}
     public void button_save(View view)
    {
	
    	 MainActivity.editrefreshcurrentclass(position, name.getText().toString(), gradespinner.getSelectedItem().toString(), Double.parseDouble(credit.getText().toString()));
    	 finish();
    	 
    	 
    }
     public void button_cancel(View view)
     {
    	 finish();
     }
}
