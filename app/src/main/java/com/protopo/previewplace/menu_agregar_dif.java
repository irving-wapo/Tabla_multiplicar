package com.protopo.previewplace;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
/**
 * Created by Francisco on 07/03/2016.
 */
public class menu_agregar_dif extends DialogFragment{
    private DialogListener listener;

    public interface DialogListener {

        public void onSingleChoiceItems(DialogFragment dialog,int arg);
        public void onDialogNegativeClick(DialogFragment dialog);
    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            listener = (DialogListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement DialogListener");
        }
    }
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle(R.string.add_title)
                .setItems(R.array.menu_diferencial,
                        new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {

                                listener.onSingleChoiceItems(menu_agregar_dif.this, arg1);

                            }
                        })
                .setNegativeButton(android.R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                listener.onDialogNegativeClick(menu_agregar_dif.this);
                            }
                        });

        return builder.create();
    }
}
