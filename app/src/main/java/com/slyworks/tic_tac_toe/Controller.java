package com.slyworks.tic_tac_toe;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.os.SystemClock;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

import java.util.Random;

/**
 * Created by Joshua Sylvanus, 11:26 AM, 6/20/2021.
 */
//for controlling the overall actions being performed in the game
class Controller {
    //region Vars
    private final Context mContext;
    private final View mRootView;
    private final int[] mView_ids;
    private final int[][] mCombinationsOfThrees;

    private String mAIMarker;
    private String mUserMarker;

    private boolean mIsGameOver;

    private final Handler mMainHandler = new Handler(Looper.getMainLooper());

    private int mSpotToBePlayedTo;
    private boolean mIsAIToPlay = false;

    private static Controller instance;
    //endregion

    public static Controller getInstance(Context context, View rootView, int[][] combinationOfThrees, int[] view_ids){
        if(instance == null){
           instance = new Controller(context, rootView, combinationOfThrees, view_ids);
        }
        return instance;
    }
    //for the dialog
    public static Controller getInstance(){ return instance; }
    private Controller(Context context, View rootView, int[][] combinationOfThrees, int[] view_ids){
        this.mContext = context;
        this.mRootView = rootView;
        this.mCombinationsOfThrees = combinationOfThrees;
        this.mView_ids = view_ids;
    }

    public String getAIMarker(){return mAIMarker;}
    public void setAIMarker(String marker){mAIMarker = marker;}

    public String getUserMarker(){return mUserMarker;}
    public void setUserMarker(String marker){mUserMarker = marker;}

    private int setBothMarkerColors(String marker){
      return getUserMarker().equals(marker) ? mContext.getResources().getColor(R.color.blue) : mContext.getResources().getColor(R.color.red);
    }

    public void userToPlay(int textView_id){
        if(!performCheck(textView_id)) return;

        play(textView_id, getUserMarker());

        checkIfThereIsAWinner();

        if(!mIsGameOver) AIToPlay();
    }

    //region userToPlay() helper methods
    private boolean performCheck(int textView_id){
        if(mIsGameOver){ return false; }
        if(!isSpotEmpty(textView_id)){
           displayMessage("spot has already been played");
           return false;
        }

        return true;
    }
    private boolean isSpotEmpty(int textView_id){
        return ((TextView)mRootView.findViewById(textView_id)).getText().toString().isEmpty();
    }

    private void setAIToPlay(boolean isAIToPlay){ mIsAIToPlay = isAIToPlay; }
    public boolean getAIToPlay(){ return mIsAIToPlay; }
    //endregion

    public void AIToPlay(){
        //first:check if there is a 2 to be made 3
        //second:check if there is an opponents 2 and block
        //third:play to the center spot
        //fourth:play randomly

        mMainHandler.post(()->{
            SystemClock.sleep(700);

            checkAppropriateSpotToPlayTo();

            play(getSpotToBePlayedTo(), getAIMarker());

            checkIfThereIsAWinner();
        } );
    }
   //region AIToPlay() helper methods
    private void  setSpotToBePlayedTo(int spot_id){mSpotToBePlayedTo = spot_id;}
    private int getSpotToBePlayedTo(){return mSpotToBePlayedTo; }
    private Spot checkAppropriateSpotToPlayTo() {
        int markedSpots = 0;
        int userMarkedSpots = 0;
        int AIMarkedSpots = 0;

        Spot spot = null;

        outer:for(int i=0; i < mCombinationsOfThrees.length;i++){

              inner:for(int j=0; j < 3; j++){
                  TextView textView = mRootView.findViewById(mCombinationsOfThrees[i][j]);

                  //for individual combination of 3
                  if(!textView.getText().toString().isEmpty()){
                      markedSpots++;
                      if(textView.getText().toString().equals(getAIMarker()))
                          AIMarkedSpots++;
                      else
                          userMarkedSpots++;

                  }

                  if(AIMarkedSpots == 2){
                      inner_one:for(int k=0;k<3;k++){
                          TextView textView2 = mRootView.findViewById(mCombinationsOfThrees[i][k]);

                          if(textView2.getText().toString().isEmpty()){
                               setSpotToBePlayedTo(mCombinationsOfThrees[i][k]);
                               spot = Spot.TWO_FILLED_AI;
                               break outer;
                          }
                      }
                  }else if(userMarkedSpots == 2){
                      inner_two:for(int k=0; k<3; k++){
                          TextView textView2 = mRootView.findViewById(mCombinationsOfThrees[i][k]);

                          if(textView2.getText().toString().isEmpty()){
                              setSpotToBePlayedTo(mCombinationsOfThrees[i][k]);
                              spot = Spot.TWO_FILLED_USER;
                              break outer;
                          }
                      }
                  }else if( ((TextView)mRootView.findViewById(mView_ids[4])).getText().toString().isEmpty() ){
                      //return center spot
                      setSpotToBePlayedTo(mView_ids[4]);
                     spot =  Spot.CENTER;
                     break outer;

                  }else{
                    setSpotToBePlayedTo(getRandomSpotGenerator());
                    spot =  Spot.RANDOM;
                    break outer;
                  }
              }
          }

          return spot;
    }
    private int getRandomSpotGenerator(){
        Random outerArrayRand = new Random();
        Random innerArrayRand = new Random();

        int outerArrayIndex = outerArrayRand.nextInt(mCombinationsOfThrees.length - 1);
        int innerArrayIndex = innerArrayRand.nextInt(2);

        TextView textView = mRootView.findViewById(mCombinationsOfThrees[outerArrayIndex][innerArrayIndex]);

        if( !textView.getText().toString().isEmpty() )
            getRandomSpotGenerator();

        return mCombinationsOfThrees[outerArrayIndex][innerArrayIndex];
    }
    //endregion

    private void checkIfThereIsAWinner(){
        int markedSpots = 0;
        int userMarkedSpots = 0;
        int AIMarkedSpots = 0;

        outer:for(int[] combinationOfThree: mCombinationsOfThrees){
            markedSpots = 0;
            userMarkedSpots = 0;
            AIMarkedSpots = 0;

            inner:for(int textView_id: combinationOfThree){

                TextView textView = mRootView.findViewById(textView_id);

                if(!textView.getText().toString().isEmpty()){
                    markedSpots++;

                    if(textView.getText().toString().equals(getUserMarker())){ userMarkedSpots++; }
                    else{ AIMarkedSpots++; }

                    if(userMarkedSpots == 3 || AIMarkedSpots == 3){
                        setGameWinner(userMarkedSpots == 3 ? "USER" : "AI" );
                        break outer; }

                }
            }
        }
    }

    private void setGameWinner(String winner){
        mMainHandler.post(()-> displaySnackBar(winner + " Wins"));
        mIsGameOver = true;
    }
    private void play(int textView_id, String marker){
        TextView textView = mRootView.findViewById(textView_id);
        textView.setTextColor(setBothMarkerColors(marker));
        textView.setText(marker);
    }
    public void resetBoard(){
        for(int id: mView_ids){
            TextView textView = mRootView.findViewById(id);
            textView.setText("");
        }

        mIsGameOver = false;
    }
    private void displayMessage(String message){ Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show(); }
    private void displaySnackBar(String message){ Snackbar.make(mRootView, message, Snackbar.LENGTH_LONG).show(); }
}
