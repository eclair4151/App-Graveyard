package com.tomer.drexelgpacalc;



import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class ForecasterFragment extends Fragment{
	
	EditText credits, gpa, desired_gpa;
    TextView results;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.forecaster_fragment, container, false);

        credits = (EditText) view.findViewById(R.id.edit_credits);
        gpa = (EditText) view.findViewById(R.id.edit_gpa);
       	desired_gpa = (EditText) view.findViewById(R.id.edit_desired_gpa);
       	results = (TextView) view.findViewById(R.id.edit_results);
        Button calculate = (Button)view.findViewById(R.id.button_submit);
        calculate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateCredits();
            }
        });
       	double precredit = MainActivity.totalcreditsandcr;
       	double pregpa = MainActivity.totalgpa;
       	
       	credits.setText(String.format("%.1f", precredit));
       	gpa.setText(String.format("%.3f", pregpa));
        return view;
    }

    @Override
    public void onPause()
    {
        dismissKeyboard();
        super.onPause();
    }
    public void calculateCredits() {
        dismissKeyboard();
    	double currentGPA, currentCredits, desiredGPA;
    	String gpa_text = gpa.getText().toString();
    	String desired_gpa_text = desired_gpa.getText().toString();
    	String credits_text = credits.getText().toString();
    	if (gpa_text.equals("") || desired_gpa_text.equals("") || credits_text.equals(""))
    		results.setText("Please enter data for the fields above.");
    	else
    	{
    		currentGPA = Double.parseDouble(gpa_text);
    		desiredGPA = Double.parseDouble(desired_gpa_text);
    		currentCredits = Double.parseDouble(credits_text);
    	
    		results.setText("");
    		printCreditsNeeded(currentGPA, currentCredits, desiredGPA);
    	}
    }
    
    public void printCreditsNeeded(double currentGPA, double currentCredits, double desiredGPA)
    {
    	double testGPA = 4.0;
    	double credits;

    	credits = creditsNeeded(currentGPA,currentCredits,desiredGPA,testGPA);

    	// Error check credits (positive) and GPA (between 0 and 4 && current must be 0 if credits are 0)
    	if (currentCredits < 0)
    		results.setText("The current number of credits is not valid\n");
    	else if (currentGPA < 0 || desiredGPA < 0 || currentGPA > 4 || desiredGPA > 4)
    		results.setText("GPA is not a valid value. Must be in the range of 0.0 - 4.0\n");
    	else if (currentGPA !=0 && currentCredits ==0)
    		results.setText("GPA not valid without any credit.\n");

    	// Determine the number of credits needed, if possible
    	else if ((desiredGPA == 4.0 || credits > 120) && (currentGPA != 4.0 && currentCredits != 0))
    		results.setText("Cannot attain desired GPA with 120 or less credits\n");
    	else if (Math.abs(desiredGPA-currentGPA) < 1e-14 || currentCredits==0 || desiredGPA==4.0)
    		results.setText("Any number of credits with a GPA of " + desiredGPA + " or greater.\n");
    	else if (desiredGPA-currentGPA < -1e-14)
    		results.setText("A Desired GPA lower than Current GPA is not allowed.\n");
    	else
    	{
    		while (testGPA > desiredGPA && credits <= 120)
    		{
    			results.setText(results.getText().toString() + String.format("%.1f",roundHalf(credits)) + " credits with a GPA of " + String.format("%.1f",testGPA) +"\n");
    			testGPA -= 0.1;
    			credits = creditsNeeded(currentGPA,currentCredits,desiredGPA,testGPA);
    		}
    	}
    }
    
    public double creditsNeeded(double currentGPA, double currentCredits, double desiredGPA, double testGPA)
    {
    	if (testGPA == desiredGPA)
    		return 1e10; // arbitrary, large value
    	return currentCredits*(desiredGPA-currentGPA) / (testGPA - desiredGPA);
    }
    
    public double roundHalf(double number)
    {
    	return Math.ceil(  (double) Math.round(number*2 * 100000) / 100000  )/2.0;
    }

    public void dismissKeyboard()
    {

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(credits.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(gpa.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(desired_gpa.getWindowToken(), 0);
    }
}
