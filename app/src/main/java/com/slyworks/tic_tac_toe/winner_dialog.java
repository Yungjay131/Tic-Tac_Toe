package com.slyworks.tic_tac_toe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;

public class winner_dialog extends DialogFragment {
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder =new  AlertDialog.Builder(getActivity());
        LayoutInflater lf = getActivity().getLayoutInflater();
        final View dialogView = lf.inflate(R.layout.winner_dialog_layout,null);

        //TextView mTextView = dialogView.findViewById(R.id.textView);

        builder.setView(dialogView);
        return builder.create();
    }
}
