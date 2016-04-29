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

public class dialog_nombre_archivo extends DialogFragment {
    private DialogListener listener;

    public interface  DialogListener{
        public void aceptar_btn(DialogFragment dialog, String nombre );
        public void cancelar_btn(DialogFragment dialog);
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        try
        {
            listener = (DialogListener) activity;
        }
        catch (ClassCastException e)  {
            throw new ClassCastException(activity.toString() +
                    " must implement DialogListener");   }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState )
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.dialog_nombre_archivo, null)).setTitle(R.string.lblNombre)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {
                                EditText txtNombre = (EditText) getDialog().findViewById(R.id.txtNombre);
                                try
                                {
                                  listener.aceptar_btn(dialog_nombre_archivo.this, String.valueOf(txtNombre.getText()));
                                }
                                catch (Exception ex) { Toast.makeText(getContext(), R.string.msjError, Toast.LENGTH_SHORT).show(); }
                            }
                        })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        listener.cancelar_btn(dialog_nombre_archivo.this);
                    }
                });
        return builder.create();
    }


}
