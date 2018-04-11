package com.example.user.myapplication;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;

/**
 * Created by user on 4/12/2018.
 */

public class ExitDialogFragment extends DialogFragment{


    public interface ExitDialogListener {
        public void onDialogPositiveClick(DialogFragment dialog);
        public void onDialogNegativeClick(DialogFragment dialog);
    }


    ExitDialogListener mListener;



    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof ExitDialogListener){
            this.mListener=(ExitDialogListener) context;
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage(R.string.text_exit)
                .setPositiveButton(R.string.text_yes, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogPositiveClick(ExitDialogFragment.this);
                    }
                })
                .setNegativeButton(R.string.text_no, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        mListener.onDialogNegativeClick(ExitDialogFragment.this);
                    }
                });

        return builder.create();
    }
}
