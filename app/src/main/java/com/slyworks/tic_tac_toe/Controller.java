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

    private boolean mIsAIToPlay = false;

    private static Controller instance;

    private boolean mIsResetting;
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

            play(checkAppropriateSpotToPlayTo(), getAIMarker());

            checkIfThereIsAWinner();
        } );
    }
   //region AIToPlay() helper methods
    private int checkAppropriateSpotToPlayTo(){
        //returning the id of the appropriate place to play to
        int freeSpotsPerArray = 0;
        int userMarkedSpotsPerArray = 0;
        int AIMarkedSpotsPerArray = 0;

        int fullIndex = -1;

        Spot spot = Spot.NOT_SET;

        outer:for(int i=0 ; i < mCombinationsOfThrees.length ; i++){
            //for each of the inner arrays of 3, get number of free spots, AI and user marked spots
            inner:for(int j=0 ; j < 3; j++){
               TextView textView = mRootView.findViewById(mCombinationsOfThrees[i][j]);

               if(textView.getText().toString().isEmpty()){
                   freeSpotsPerArray++;
               }else if(!textView.getText().toString().isEmpty()){
                   if(textView.getText().toString().equals(getAIMarker())) AIMarkedSpotsPerArray++;
                   else userMarkedSpotsPerArray++;
               }

               //at the end of each inner 3 iterations
                if(j == 2){
                    if(AIMarkedSpotsPerArray == 2 && freeSpotsPerArray == 1){
                        for(int k=0 ; k < 3 ; k++){
                            TextView textView2 = mRootView.findViewById(mCombinationsOfThrees[i][k]);

                            if(textView2.getText().toString().isEmpty()){
                                fullIndex = mCombinationsOfThrees[i][k];
                                return fullIndex;
                            }
                        }
                    }else if( userMarkedSpotsPerArray == 2 && freeSpotsPerArray == 1){
                        for(int k=0 ; k < 3 ; k++){
                            TextView textView2 = mRootView.findViewById(mCombinationsOfThrees[i][k]);

                            if(textView2.getText().toString().isEmpty()){
                                fullIndex = mCombinationsOfThrees[i][k];
                                spot = Spot.TWO_FILLED_USER;
                            }
                        }
                    } else {
                       TextView textView3 = mRootView.findViewById(mView_ids[4]);
                       if(textView3.getText().toString().isEmpty() && spot != Spot.TWO_FILLED_USER){
                           fullIndex = mView_ids[4];
                           spot = Spot.CENTER;
                       }
                    }
                }


                //at the overall end
                if(i == mCombinationsOfThrees.length-1 && j == 2){
                    if(spot != Spot.NOT_SET) return fullIndex;
                    else {
                        fullIndex = getRandomSpotGenerator();
                        spot = Spot.RANDOM;
                    }
                }
            }

            freeSpotsPerArray = 0; userMarkedSpotsPerArray = 0; AIMarkedSpotsPerArray = 0;
        }

        return fullIndex;
    }

    private int getRandomSpotGenerator(){
        Random outerArrayRand = new Random();
        Random innerArrayRand = new Random();

        int outerArrayIndex = outerArrayRand.nextInt(mCombinationsOfThrees.length - 1);
        int innerArrayIndex = innerArrayRand.nextInt(2);

        TextView textView = mRootView.findViewById(mCombinationsOfThrees[outerArrayIndex][innerArrayIndex]);

        if( !textView.getText().toString().isEmpty() ) return getRandomSpotGenerator();

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

    public void  setResetStatus(boolean status){ mIsResetting = status; }
    public boolean isStillResetting(){  return mIsResetting; }
}
