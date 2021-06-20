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

public class selection_dialog extends DialogFragment {
    //region Vars
    private RadioGroup mRadioGroup;
    private RadioButton mRadioButtonX,mRadioButtonO;

    private Controller mController;
    private int mId = -1;
    //endregion
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        return initViews();
    }

    private Dialog initViews(){
        final AlertDialog.Builder builder =new  AlertDialog.Builder(getActivity());
        LayoutInflater lf = getActivity().getLayoutInflater();
        final View dialogView = lf.inflate(R.layout.selection_dialog_layout,null);

        mRadioGroup = dialogView.findViewById(R.id.radioGroup1);
        mRadioButtonO = dialogView.findViewById(R.id.radioButtonO);
        mRadioButtonX = dialogView.findViewById(R.id.radioButtonX);
        Button button1 = dialogView.findViewById(R.id.button1);

        mController = Controller.getInstance();

        button1.setOnClickListener( (v)->{
            mId = mRadioGroup.getCheckedRadioButtonId();
            //check incase no checkButton was checked
            if(mId == -1){
                return;
            }
            if (mId == mRadioButtonO.getId()) {
                mController.setUserMarker("O");
                mController.setAIMarker("X");

            }else if (mId == mRadioButtonX.getId()){
                mController.setUserMarker("X");
                mController.setAIMarker("O");

            }//end of conditionals
            //Toast.makeText(getContext(), getUserMarker(), Toast.LENGTH_LONG).show();

            mController.setResetStatus(false);
            dismiss();

        });

        builder.setView(dialogView);

        Dialog dialog  = builder.create();

        setCancelable(false);

        return  dialog;


    }

    @Override
    public void setCancelable(boolean cancelable) {
        super.setCancelable(cancelable);
    }


    @Override
    public void setEnterTransition(@Nullable Object transition) {
        //TODO:find code for this method
        super.setEnterTransition(transition);
    }
}
