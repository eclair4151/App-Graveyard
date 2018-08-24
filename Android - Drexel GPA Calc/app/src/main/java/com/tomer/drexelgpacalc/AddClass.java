package com.tomer.drexelgpacalc;

import java.io.InputStream;
import java.util.ArrayList;
import java.io.*;

import com.tomer.drexelgpacalc.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;


public class AddClass extends Activity{
    EditText textbox,manualadd;
    ListView list;
    ArrayAdapter<String> adapterthing;
    Spinner gradespinner;
    boolean selected = false;
    ArrayList<String> listedclasses;
    ArrayList<String> listedclassesparse;
    String selectedclass;
    double credits;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addclasslayout);

        textbox = (EditText)findViewById(R.id.editText1);
        list = (ListView) findViewById(R.id.listView1);
        gradespinner = (Spinner)findViewById(R.id.expandableListView2);

        listedclasses = new ArrayList<String>();
        listedclassesparse = new ArrayList<String>();

        adapterthing = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_dropdown_item, listedclassesparse);
        list.setAdapter(adapterthing);

        list.setOnItemClickListener(new OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,int position, long id)
            {
                String[] cutup = listedclassesparse.get(position).toString().split(",");
                selectedclass = cutup[0].trim();
                credits = Double.parseDouble(cutup[2].trim());
                textbox.setText(list.getItemAtPosition(position).toString());
                listedclassesparse.clear();
                adapterthing.notifyDataSetChanged();
                selected=true;
            }});


        textbox.addTextChangedListener(new TextWatcher(){
            public void afterTextChanged(Editable s) {
                getnewlist();
                selected = false;
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
        //manualadd = (EditText)findViewById(R.id.etCredits);
        readlist();
    }



    public void buttonclick(View view) //Clicks add class
    {
        if (selected==true)
        {
            int year = MainActivity.spinner_year.getSelectedItemPosition();
            int term = MainActivity.spinner_term.getSelectedItemPosition();

            MainActivity.terms[year][term].addClass(selectedclass , gradespinner.getSelectedItem().toString().trim(), credits);
            //MainActivity.terms[year][term].addClass("test", "A-", 4);


            MainActivity.refreshview();
            finish();

        }
        else if (selected == false && textbox.getText().toString().isEmpty() == false)
        {


            classnotfoundalert();

            //double newcredit = Double.parseDouble(manualadd.getText().toString());

        }

    }

    private void classnotfoundalert()
    {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Class Not Found");
        alert.setMessage("That class doesn't appear to be in our database. Please enter the number of credits it is worth and we will add it anyway:");

// Set an EditText view to get user input
        final EditText input = new EditText(this);
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        alert.setView(input);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
               credits =Double.parseDouble(input.getText().toString());
                int year = MainActivity.spinner_year.getSelectedItemPosition();
                int term = MainActivity.spinner_term.getSelectedItemPosition();

                MainActivity.terms[year][term].addClass(textbox.getText().toString() , gradespinner.getSelectedItem().toString().trim(), credits);


                MainActivity.refreshview();
                finish();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();

    }

    public void getnewlist()
    {

        listedclassesparse.clear();
        adapterthing.notifyDataSetChanged();

        if (textbox.getText().toString().matches("")==false && textbox.length() >=3 )
        {

          for(int i=0;i<listedclasses.size();i++)
            {
                if (listedclasses.get(i).toLowerCase().contains(textbox.getText().toString().toLowerCase()))
                {
                //String[] stringparts = listedclasses.get(i).toString().split(",");
                listedclassesparse.add(listedclasses.get(i));
                }
            }
        }
        adapterthing.notifyDataSetChanged();
    }


    private void readlist()
    {
        AssetManager am = getResources().getAssets();
        try
        {
            InputStream is = am.open("FinalList.txt");
            DataInputStream in = new DataInputStream(is);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;

            while ((strLine = br.readLine()) != null)
            {
                    listedclasses.add(strLine);
            }
            in.close();
        }catch (IOException e)
        {
            e.printStackTrace();
        }
    }

}
