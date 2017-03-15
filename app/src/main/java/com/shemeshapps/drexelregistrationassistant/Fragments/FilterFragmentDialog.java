package com.shemeshapps.drexelregistrationassistant.Fragments;

import android.app.Activity;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Response;
import com.shemeshapps.drexelregistrationassistant.Models.Professor;
import com.shemeshapps.drexelregistrationassistant.Models.WebtmsFilter;
import com.shemeshapps.drexelregistrationassistant.R;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by tomer on 2/7/16.
 */
public class FilterFragmentDialog extends DialogFragment {

    public FilterFragmentDialog() {
        // Empty constructor required for DialogFragment
    }

    //custom dialog fragment to show options for filtering classes
    //@SuppressWarnings("unchecked")
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View parentView = getActivity().getLayoutInflater().inflate(R.layout.filter_popup_fragment, null);
        final WebtmsFilter webtmsFilter = Parcels.unwrap(getArguments().getParcelable("filter"));

        //setup filter for every option
        Button b = (Button)parentView.findViewById(R.id.filter_by_prof_button);
        b.setTypeface(null, ((webtmsFilter.filteredProfessors!=null)?Typeface.BOLD:Typeface.NORMAL));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                showFilter("Professor", webtmsFilter.professors,webtmsFilter.filteredProfessors, new Response.Listener<List<?>>() {
                    @Override
                    public void onResponse(List<?> response) {
                        webtmsFilter.filteredProfessors = (List<Professor>)response;
                        ((Button)view).setTypeface(null, ((response!=null)?Typeface.BOLD:Typeface.NORMAL));

                    }
                });
            }
        });


        b = (Button)parentView.findViewById(R.id.filter_by_method);
        b.setTypeface(null, ((webtmsFilter.filteredInstruction_methods!=null)?Typeface.BOLD:Typeface.NORMAL));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                showFilter("Instruction Methods", webtmsFilter.instruction_methods,webtmsFilter.filteredInstruction_methods, new Response.Listener<List<?>>() {
                    @Override
                    public void onResponse(List<?> response) {
                        webtmsFilter.filteredInstruction_methods = (List<String>)response;
                        ((Button)view).setTypeface(null, ((response!=null)?Typeface.BOLD:Typeface.NORMAL));

                    }
                });
            }
        });

        b = (Button)parentView.findViewById(R.id.filter_by_type);
        b.setTypeface(null, ((webtmsFilter.filteredInstruction_types!=null)?Typeface.BOLD:Typeface.NORMAL));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                showFilter("Instruction Types", webtmsFilter.instruction_types,webtmsFilter.filteredInstruction_types, new Response.Listener<List<?>>() {
                    @Override
                    public void onResponse(List<?> response) {
                        webtmsFilter.filteredInstruction_types = (List<String>)response;
                        ((Button)view).setTypeface(null, ((response!=null)?Typeface.BOLD:Typeface.NORMAL));

                    }
                });
            }
        });

        b = (Button)parentView.findViewById(R.id.filter_by_campus);
        b.setTypeface(null, ((webtmsFilter.filteredCampus!=null)?Typeface.BOLD:Typeface.NORMAL));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                showFilter("Instruction Methods", webtmsFilter.campus,webtmsFilter.filteredCampus ,new Response.Listener<List<?>>() {
                    @Override
                    public void onResponse(List<?> response) {
                        webtmsFilter.filteredCampus = (List<String>)response;
                        ((Button)view).setTypeface(null, ((response!=null)?Typeface.BOLD:Typeface.NORMAL));

                    }
                });
            }
        });


        b = (Button)parentView.findViewById(R.id.filter_by_start);
        b.setTypeface(null, ((webtmsFilter.starttime!=-1)?Typeface.BOLD:Typeface.NORMAL));

        //setup time filter
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view1) {

                int hour = 12;
                int min = 0;
                if(webtmsFilter.starttime != -1)
                {
                    hour = webtmsFilter.starttime/100;
                    min = webtmsFilter.starttime%100;
                }
                TimePickerDialog d = new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        webtmsFilter.starttime = hourOfDay*100 + minute;
                        ((Button)view1).setTypeface(null, Typeface.BOLD);

                    }
                },hour,min,false);
                d.setTitle("Don't show classes that start before");
                d.setButton(DialogInterface.BUTTON_NEUTRAL, "Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        webtmsFilter.starttime = -1;
                        ((Button)view1).setTypeface(null, Typeface.NORMAL);
                    }
                });

                d.show();
            }
        });

        b = (Button)parentView.findViewById(R.id.filter_by_end);
        b.setTypeface(null, ((webtmsFilter.endtime!=-1)?Typeface.BOLD:Typeface.NORMAL));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view1) {
                int hour = 12;
                int min = 0;
                if(webtmsFilter.endtime != -1)
                {
                    hour = webtmsFilter.endtime/100;
                    min = webtmsFilter.endtime%100;
                }
                TimePickerDialog d =    new TimePickerDialog(getActivity(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        webtmsFilter.endtime = hourOfDay*100 + minute;
                        ((Button)view1).setTypeface(null, Typeface.BOLD);
                    }
                },hour,min,false);
                d.setButton(DialogInterface.BUTTON_NEUTRAL, "Reset", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        webtmsFilter.endtime = -1;
                        ((Button)view1).setTypeface(null, Typeface.NORMAL);

                    }
                });
                d.setTitle("Don't show classes that end after");
                d.show();
            }
        });

        //setup day chooser filter
        b = (Button)parentView.findViewById(R.id.filter_by_day);
        b.setTypeface(null, ((webtmsFilter.filteredDays!=null)?Typeface.BOLD:Typeface.NORMAL));
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                ArrayList<String> filteredDays = null;
                if(webtmsFilter.filteredDays != null)
                {
                    //some gross mapping to go from chars from user to days used by class
                    Map<String,String> daymap = new HashMap<>();
                    daymap.put("M","Monday");
                    daymap.put("T","Tuesday");
                    daymap.put("W","Wednesday");
                    daymap.put("R","Thursday");
                    daymap.put("F","Friday");
                    daymap.put("S","Saturday");
                    filteredDays = new ArrayList<>();
                    for(String d:webtmsFilter.filteredDays)
                    {
                        filteredDays.add(daymap.get(d));
                    }
                }


                showFilter("Days I Want Classes On", webtmsFilter.days,filteredDays, new Response.Listener<List<?>>() {
                    @Override
                    public void onResponse(List<?> response) {
                        if(response != null)
                        {
                            //map days back for some reason. i forget why...
                            Map<String,String> daymap2 = new HashMap<>();
                            daymap2.put("Monday","M");
                            daymap2.put("Tuesday","T");
                            daymap2.put("Wednesday","W");
                            daymap2.put("Thursday","R");
                            daymap2.put("Friday","F");
                            daymap2.put("Saturday","S");

                            webtmsFilter.filteredDays = new ArrayList<>();
                            for(String d: (List<String>)response)
                            {
                                webtmsFilter.filteredDays.add(daymap2.get(d));
                            }
                            ((Button)view).setTypeface(null, Typeface.BOLD);

                        }
                        else
                        {
                            webtmsFilter.filteredDays = null;
                            ((Button)view).setTypeface(null, Typeface.NORMAL);
                        }

                    }
                });
            }
        });


        CheckBox c = (CheckBox)parentView.findViewById(R.id.filter_by_full);
        if(!webtmsFilter.showFull)
        {
            c.setChecked(false);
        }
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                webtmsFilter.showFull = isChecked;
            }
        });



        return new AlertDialog.Builder(getActivity())
                .setTitle("Add Filters")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                mListener.onComplete(webtmsFilter);
                            }
                        }
                )
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .setNeutralButton("Reset All",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {

                        mListener.onResetAll();
                    }
                })
                .setView(parentView)
                .create();
    }


    //for each filter option show the new popup wich choice boxes
    private void showFilter(String name, final List<?> options,final List<?> selected, final Response.Listener<List<?>> listener)
    {
        final List<Object> response = new ArrayList<>();
        if(selected!=null)
        {
            response.addAll(selected);
        }else
        {
            response.addAll(options);
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        boolean[] array = new boolean[options.size()];
        Arrays.fill(array, Boolean.TRUE);


        String[] choicesStrings = new String[options.size()];
        for(int i =0; i < options.size(); i++)
        {
            choicesStrings[i] = options.get(i).toString();
            if(selected!=null && !selected.contains(options.get(i)))
            {
                array[i] = false;
            }
        }

        builder.setTitle(name)
                .setMultiChoiceItems(choicesStrings, array,
                        new DialogInterface.OnMultiChoiceClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                                if(isChecked)
                                {
                                    response.add(options.get(which));
                                }
                                else
                                {
                                    response.remove(options.get(which));
                                }
                            }
                        })
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        if(response.size() == 0)
                        {
                            Toast.makeText(getActivity(),"You must select at least 1 option",Toast.LENGTH_SHORT).show();
                        }
                        else if(response.size() == options.size())
                        {
                            listener.onResponse(null);
                        }
                        else
                        {
                            listener.onResponse(response);
                        }
                    }
                })
                .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).setNeutralButton("Reset",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        listener.onResponse(null);
                        dialog.dismiss();
                    }
                });
        builder.create().show();
    }

    public static interface OnCompleteListener {
        public abstract void onComplete(WebtmsFilter filter);
        public abstract void onResetAll();

    }



    private OnCompleteListener mListener;

    // make sure the Activity implemented it
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            this.mListener = (OnCompleteListener)activity;
        }
        catch (final ClassCastException e) {
            throw new ClassCastException(activity.toString() + " must implement OnCompleteListener");
        }
    }

}