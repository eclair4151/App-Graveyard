package com.tomer.drexelgpacalc;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import com.tomer.drexelgpacalc.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity {

	static String[] array_year;
	static String[] array_term;
	static ArrayList<String> listofclasses = new ArrayList<String>();
	public static ClassList[][] terms = new ClassList[5][4];
	public static String currentclass;
	public static double currentclasscredit;
	public static String currentclassgrade;
	public static int currentclassposition;
	static ArrayAdapter<String> adapterthing;
	public static Spinner spinner_year;
	public static Spinner spinner_term;
	static int count = 1;
	public static double totalcredits;
	public static double totalcreditsandcr;
	static double termcredits;
	public static double totalgpa;
	static double termgpa;
	static double termnocreditclasses;
	static double totalnocreditclasses;
	static EditText edittotalcredit;
	static EditText edittermcredit;
	static EditText edittotalgpa;
	static EditText edittermgpa;
	static String test;
	static String filename = "saveclasses";
	static TextView theview;
	static FileOutputStream fos;
	static FileInputStream fis;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		setupeverything();
		//writefile(true); //incase you need to refresh the file
	    readfile();
        checkFirstLaunch();

	}

    private void checkFirstLaunch() {
        final String PREFS_NAME = "MyPrefsFile";

        SharedPreferences settings = getSharedPreferences(PREFS_NAME, 0);

        if (settings.getBoolean("my_first_time", true)) {

            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle("Sign in to Drexel One?");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Would you like to sign into Drexel One to download all you past classes? If not you can always click the action bar menu and sign in from there!")
                    .setCancelable(false)
                    .setPositiveButton("Yes",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            Intent intent = new Intent(getApplicationContext(),SignIn.class);
                            startActivity(intent);
                        }
                    })
                    .setNegativeButton("No",new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog,int id) {
                            dialog.cancel();
                        }
                    });

            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

            settings.edit().putBoolean("my_first_time", false).commit();
        }
    }

    @Override
	public boolean onCreateOptionsMenu(Menu menu) {

		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
    public void buttonforecaster(View view)
    {
    	
    	Intent openaddclass = new Intent(this,GpaAnalysis.class);
		startActivity(openaddclass);

    }
	public void buttonaddclass(View view) {
		
		
		
		Intent openaddclass = new Intent(this,AddClass.class);
		startActivity(openaddclass);

	}

	@Override
	public void onPause() {
		super.onPause();

	  writefile(false);

	}

	public static void refreshview() {
		int year = spinner_year.getSelectedItemPosition();
		int term = spinner_term.getSelectedItemPosition();

		listofclasses.clear();
		for (int i = 0; i < terms[year][term].getSize(); i++) {
			listofclasses.add(terms[year][term].getCourse(i).getname() + "   "
					+ terms[year][term].getCourse(i).getcredits() + "   "
					+ terms[year][term].getCourse(i).getgrade());

		}
		adapterthing.notifyDataSetChanged();
		setcreditandgpa();
	}

	public static void setcreditandgpa() {
		// get total credits needs work gpa is wrong
		int year = spinner_year.getSelectedItemPosition();
		int term = spinner_term.getSelectedItemPosition();
		totalcredits = 0;
        totalcreditsandcr = 0;
		termcredits = terms[year][term].getCredits(true);
		totalgpa = 0;
		termgpa =  terms[year][term].getGPA();
		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 4; j++) {
                totalcreditsandcr += terms[i][j].getCredits(true);
                totalcredits+= terms[i][j].getCredits(false);
                totalgpa+=terms[i][j].getGPA() * terms[i][j].getCredits(false);
			}
		}
        totalgpa = totalgpa/totalcredits;
        totalgpa = (Math.floor(totalgpa*100)/100);

		edittotalcredit.setText(String.format("%.1f", totalcreditsandcr));
		edittermcredit.setText(String.format("%.1f", termcredits));
		edittotalgpa.setText(String.format("%.2f", Math.floor(totalgpa*100)/100));
		edittermgpa.setText(String.format("%.2f", Math.floor(termgpa*100)/100));

	}

	public void deleteclass(final int position) {
		int year = spinner_year.getSelectedItemPosition();
		int term = spinner_term.getSelectedItemPosition();
		String classname = terms[year][term].getCourse(position).getname();
		AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
		dialogBuilder.setTitle("Delete?");
		dialogBuilder.setMessage("Are you sure you would like to delete "
				+ classname + "?");
		dialogBuilder.setPositiveButton("Yes",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						int year = spinner_year.getSelectedItemPosition();
						int term = spinner_term.getSelectedItemPosition();
						terms[year][term].removeClass(position);
						refreshview();
						Toast toast = Toast.makeText(getApplicationContext(),
								"Class Deleted", Toast.LENGTH_SHORT);
						toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
						toast.show();
					}
				});

		dialogBuilder.setNegativeButton("No",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						// Toast.makeText(getApplicationContext(), "no",
						// Toast.LENGTH_SHORT).show();
					}
				});
		dialogBuilder.setNeutralButton("Edit",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						//here we create activity to edit it  
						int year = spinner_year.getSelectedItemPosition();
						int term = spinner_term.getSelectedItemPosition();
						currentclass = terms[year][term].getCourse(position).getname();
						currentclasscredit = terms[year][term].getCourse(position).getcredits();
						currentclassgrade = terms[year][term].getCourse(position).getgrade();
						currentclassposition = position;
						
						Intent openaddclass = new Intent(getApplicationContext(), EditClass.class);
						startActivity(openaddclass);
						
						
						
					}
				});

		AlertDialog alertDialog = dialogBuilder.create();
		alertDialog.show();

	}
	
	public static void editrefreshcurrentclass(int position,String name, String grade, double credit)
	{
		int year = spinner_year.getSelectedItemPosition();
		int term = spinner_term.getSelectedItemPosition();
		terms[year][term].editclass(position, name, grade, credit);
		refreshview();
		
	}
	
	

	public static double getgpafromgrade(String grade) {
		double gpa;
		if (grade.contains("CR")) {
			gpa = -1.0;
		} else if (grade.contains("NC") || grade.contains("NF") ||grade.contains("DCU") ||grade.contains("W")) {
			gpa = -2.0;
		} else if (grade.contains("A+")) {
			gpa = 4.0;
		} else if (grade.contains("A-")) {
			gpa = 3.67;
		} else if (grade.contains("A")) {
			gpa = 4.0;
		} else if (grade.contains("B+")) {
			gpa = 3.33;
		} else if (grade.contains("B-")) {
			gpa = 2.67;
		} else if (grade.contains("B")) {
			gpa = 3.0;
		} else if (grade.contains("C+")) {
			gpa = 2.33;
		} else if (grade.contains("C-")) {
			gpa = 1.67;
		} else if (grade.contains("C")) {
			gpa = 2.0;
		} else if (grade.contains("D+")) {
			gpa = 1.33;
		} else if (grade.contains("D-")) {
			gpa = 1.0;
		} else if (grade.contains("D")) {
			gpa = 1.0;
		}
		else {
			gpa = 0.0;
		}
		return gpa;
	}

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items

        Intent intent = new Intent(this,SignIn.class);
        startActivity(intent);
        return super.onOptionsItemSelected(item);
    }

	public void setupeverything() {

		ListView list = (ListView) findViewById(R.id.listview_classes);
		adapterthing = new ArrayAdapter<String>(this,
				android.R.layout.simple_spinner_dropdown_item, listofclasses);
		list.setAdapter(adapterthing);
		list.setOnItemLongClickListener(new OnItemLongClickListener() {
			public boolean onItemLongClick(AdapterView<?> av, View v,
					int position, long id) {
				deleteclass(position);

				return true;

			}
		});


		array_year = getResources().getStringArray(R.array.yearArray);
		spinner_year = (Spinner) findViewById(R.id.spinner_year);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, array_year);
		spinner_year.setAdapter(adapter);

		array_term = getResources().getStringArray(R.array.termArray);

		spinner_term = (Spinner) findViewById(R.id.spinner_term);
		ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, array_term);
		spinner_term.setAdapter(adapter1);

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 4; j++) {
				terms[i][j] = new ClassList();
			}
		}

		spinner_year
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						refreshview();

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}

				});
		spinner_term
				.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> arg0, View arg1,
							int arg2, long arg3) {
						refreshview();

					}

					@Override
					public void onNothingSelected(AdapterView<?> arg0) {
						// TODO Auto-generated method stub

					}

				});

		edittotalcredit = (EditText) findViewById(R.id.edittext_credits);
		edittermcredit = (EditText) findViewById(R.id.edittext_credits_term);
		edittotalgpa = (EditText) findViewById(R.id.edittext_gpa);
		edittermgpa = (EditText) findViewById(R.id.edittext_gpa_term);
		theview = (TextView) findViewById(R.id.textview_credits);

	}

	public void writefile(boolean newfile) {
		try {
			fos = openFileOutput(filename, Context.MODE_PRIVATE);
			if(newfile == false)
			{
			fos.write(createfile().getBytes());
			}
			else
			{
				fos.write(createemptyfile().getBytes());
			}
			fos.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void readfile() {
		StringBuffer collected = new StringBuffer("");
		byte[] dataarray = new byte[1024];
		try {

			fis = openFileInput(filename);
			while (fis.read(dataarray) != -1) {
				collected.append(new String(dataarray));
			}
			fis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// check if its empty first
		
		String filetostring = collected.toString();
		if(filetostring.split("\\$").length != 20)
		{
			writefile(true);
			//somethings wrong just reatrt it.
		}
		else
		{
			setclassesfromfile(filetostring);
			
		}
        
	}
    public String createemptyfile()
    {
    	String emptyfile = "";
    	for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 4; j++) {
				if(i !=0 || j !=0)
				{
					emptyfile = emptyfile + "$";
				}
				emptyfile = emptyfile + "(empty)";
				
			}
    	}
    	return emptyfile;
    }
    
    public void setclassesfromfile(String file)
    {
    	
    	String[] term = file.split("\\$");
    	int count = 0;
    	for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 4; j++) 
			{
				
					String[] classes = term[count].split("\\|");
					count++;
					if(!classes[0].contains("(empty)"))
					{
						for(int k =0; k <classes.length;k++)
						{
							String[] pieces = classes[k].split(",");
							
							terms[i][j].addClass(pieces[0], pieces[2], Double.parseDouble(pieces[1]));
						}
					}
					
				
			}
    	}
    	
    	
    }
    
    
    
	public String createfile() {
		String totalfile = "";

		for (int i = 0; i < 5; i++) {
			for (int j = 0; j < 4; j++) {
				if (i != 0 || j !=0)
				  {
					  totalfile = totalfile + "$";
				  }
				if (terms[i][j].getSize() != 0) 
				{
					for (int k = 0; k < terms[i][j].getSize(); k++)
					{
						 if (k != 0)
	                        {
	                        	totalfile = totalfile + "|";
	                        }
						totalfile = totalfile + terms[i][j].getCourse(k).getname() + "," +terms[i][j].getCourse(k).getcredits() + "," + terms[i][j].getCourse(k).getgrade();
                       
					}
				}
				else
				{
					totalfile = totalfile + "(empty)";
				}
			  
			}
		}
       
		return totalfile; 
	}
}
