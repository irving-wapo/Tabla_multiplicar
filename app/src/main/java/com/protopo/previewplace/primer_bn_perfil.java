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
public class primer_bn_perfil extends DialogFragment {

    private DialogListener listener;

    public interface DialogListener {

        public void primerPositiveClick(DialogFragment dialog, Double valor,Double valor1);
        public void primerNegativeClick(DialogFragment dialog);
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
        builder.setView(inflater.inflate(R.layout.primer_bn_perfil, null)).setTitle(R.string.slBnI)
                // Add action buttons
                .setPositiveButton(android.R.string.ok,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                EditText editText = (EditText) getDialog().findViewById(R.id.txtAtrasPI);
                                EditText editText1 = (EditText) getDialog().findViewById(R.id.txtCotaInicial);
                                Double value,value1;
                                try
                                {
                                    value = Double.valueOf(editText.getText().toString());
                                    value1 = Double.valueOf(editText1.getText().toString());
                                    listener.primerPositiveClick(primer_bn_perfil.this, value,value1);
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
                                listener.primerNegativeClick(primer_bn_perfil.this);
                            }
                        });

        // Create the AlertDialog object and return it
        return builder.create();
    }

}
