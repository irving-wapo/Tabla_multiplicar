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


public class msg_borrar extends DialogFragment
{
    private DialogListener listener;

    public interface  DialogListener{
        public void si_btn_msg(DialogFragment dialog);
        public void no_btn_msg(DialogFragment dialog);
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

        builder.setTitle(R.string.msjBorrar)
                .setPositiveButton(android.R.string.yes,
                        new DialogInterface.OnClickListener()
                        {
                            public void onClick(DialogInterface dialog, int id)
                            {

                                try
                                {
                                    listener.si_btn_msg(msg_borrar.this);
                                }
                                catch (Exception ex) { Toast.makeText(getContext(), R.string.msjError, Toast.LENGTH_SHORT).show(); }
                            }
                        })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {
                        listener.no_btn_msg(msg_borrar.this);
                    }
                });
        return builder.create();
    }



}
