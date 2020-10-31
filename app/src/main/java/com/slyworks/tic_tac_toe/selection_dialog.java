package com.slyworks.tic_tac_toe;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import static com.slyworks.tic_tac_toe.controller.getuserMarker;

public class selection_dialog extends DialogFragment {

private RadioGroup mRadioGroup;
private RadioButton mRadioButtonX,mRadioButtonO;


    private controller control = new controller();

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        final AlertDialog.Builder builder =new  AlertDialog.Builder(getActivity());
        LayoutInflater lf = getActivity().getLayoutInflater();
        final View dialogView = lf.inflate(R.layout.selection_dialog_layout,null);


        mRadioGroup = dialogView.findViewById(R.id.radioGroup1);
        mRadioButtonO = dialogView.findViewById(R.id.radioButtonO);
        mRadioButtonX = dialogView.findViewById(R.id.radioButtonX);
        Button button1 = dialogView.findViewById(R.id.button1);


        //setting OnclickListener for the RadioGroup in general


        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    int id = mRadioGroup.getCheckedRadioButtonId();
                    RadioButton rb = dialogView.findViewById(mRadioGroup.getCheckedRadioButtonId());

                    if (id == mRadioButtonO.getId()) {
                        control.setuserMarker("O");
                        control.setAIMarker("X");

                    }else if (id == mRadioButtonX.getId()){
                        control.setuserMarker("X");
                        control.setAIMarker("O");

                    }
                Toast.makeText(getContext(), getuserMarker(), Toast.LENGTH_LONG).show();
                    dismiss();


            }
        });

        builder.setView(dialogView);
       return  builder.create();


    }
}
