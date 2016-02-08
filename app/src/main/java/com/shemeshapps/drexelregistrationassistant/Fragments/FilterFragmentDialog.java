package com.shemeshapps.drexelregistrationassistant.Fragments;

import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.shemeshapps.drexelregistrationassistant.R;

/**
 * Created by tomer on 2/7/16.
 */
public class FilterFragmentDialog extends DialogFragment {

    public FilterFragmentDialog() {
        // Empty constructor required for DialogFragment
    }



    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        View parentView = getActivity().getLayoutInflater().inflate(R.layout.filter_popup_fragment, null);

        Button b = (Button)parentView.findViewById(R.id.filter_by_prof_button);
        final String profs[] = {"Erin Solovey","David Breen","William Mongan"};
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                // Set the dialog title
                builder.setTitle("Select Professors")
                        // Specify the list array, the items to be selected by default (null for none),
                        // and the listener through which to receive callbacks when items are selected
                        .setMultiChoiceItems(profs, null,
                                new DialogInterface.OnMultiChoiceClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which,
                                                        boolean isChecked) {

                                    }
                                })
                        // Set the action buttons
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                // User clicked OK, so save the mSelectedItems results somewhere
                                // or return them to the component that opened the dialog

                            }
                        })
                        .setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {

                            }
                        });
                builder.create().show();
            }
        });

        return new AlertDialog.Builder(getActivity())
                .setTitle("Add Filters")
                .setPositiveButton("OK",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // do something...
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
                .setView(parentView)
                .create();
    }
}