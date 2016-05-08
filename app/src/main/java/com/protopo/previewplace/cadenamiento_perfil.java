package com.protopo.previewplace;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

/**
 * Created by Francisco on 08/05/2016.
 */
public class cadenamiento_perfil extends DialogFragment {
    private DialogListener listener;

    public interface DialogListener {

        public void cadenamientoPositiveClick(DialogFragment dialog, int valor,int valor1,double valor2);
        public void cadenamientoNegativeClick(DialogFragment dialog);
    }

    // Override the Fragment.onAttach() method to instantiate the
    // NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the
            // host
            listener = (DialogListener) activity;
        } catch (ClassCastException e) {
            // The activity doesn't implement the interface, throw exception
            throw new ClassCastException(activity.toString()
                    + " must implement DialogListener");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // Get the layout inflater
        LayoutInflater inflater = getActivity().getLayoutInflater();



        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflater.inflate(R.layout.cadenamiento_perfil, null)).setTitle(R.string.slCadenamiento)
                // Add action buttons
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText editText = (EditText) getDialog().findViewById(R.id.txtKM);
                                EditText editText1 = (EditText) getDialog().findViewById(R.id.txtM);
                                EditText editText2 = (EditText)getDialog().findViewById(R.id.txtAtrasPC);
                                int value,value1;
                                double value2;
                                try
                                {
                                    value = Integer.valueOf(editText.getText().toString());
                                    value1 = Integer.valueOf(editText1.getText().toString());
                                    value2 = Double.valueOf(editText2.getText().toString());

                                    listener.cadenamientoPositiveClick(cadenamiento_perfil.this, value,value1,value2);
                                }
                                catch(Exception ex)
                                {
                                    Toast.makeText(getContext(),R.string.msgFloat,Toast.LENGTH_SHORT).show();
                                }

                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                listener.cadenamientoNegativeClick(cadenamiento_perfil.this);
                            }
                        });

        // Create the AlertDialog object and return it
        return builder.create();
    }
}
