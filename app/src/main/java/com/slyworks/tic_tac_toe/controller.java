package com.slyworks.tic_tac_toe;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import org.w3c.dom.Text;
import java.util.Random;

/**
 * Created by Joshua Sylvanus, 7:36pm, 13/05/2020.
 */

public class controller {
    //region initialisation code
    private static String userMarker;
    private static String AIMarker;
    private static boolean isGameOver = false;
    private  Context context;
    private static int index;
    private static int index2;
    private static int index5;
    private static final Handler mMainHandler = new Handler(Looper.getMainLooper());
    //endregion

    public static String getAIMarker() {
        return AIMarker;
    }

    public void setAIMarker(String marker) {AIMarker = marker;
    }

    public static String getuserMarker() {
        return userMarker;
    }

    public void setuserMarker(String marker) {
        userMarker = marker;
    }


    public boolean userPlay(View view, View view2, Context context, int[][] combinationsArray, int[] array_ids) {
        this.context = context;
        boolean isAiToPlay = false;
        if (!isGameOver) {
            if (isSpotEmpty(view)) {
                int id = view.getId();
                TextView mTextView = (TextView)view /*.findViewById(id)*/;

                //setting the text color
                mTextView.setTextColor(colorChooser(context, getuserMarker()));
                mTextView.setText(getuserMarker());
                isAiToPlay = true;
            } else {
                Toast.makeText(context, "spot already played", Toast.LENGTH_SHORT).show();

                //spot not valid
                isAiToPlay = false;
            }

            for (int[] ints : combinationsArray) {
                String str = checkingForWinner(view2, ints);
                if (str.equals("USER")) {
                    Toast.makeText(context, "Player One wins!!!",
                            Toast.LENGTH_LONG).show();
                    mMainHandler.post(()-> Snackbar.make(view2,"Player One wins!!!",Snackbar.LENGTH_LONG).show());

                    //TODO:show dialog asking if user wants to play again
                    isGameOver = true;
                    break;
                }
            }
        }//end of if
return  isAiToPlay;
    }

    public void comPlay(final View view, final View view2, final int[] array_ids, final Context context, final int[][] combinationsArray) {

               int non_empty_textViews = 0;
               //checking if the user has won the game
               if (!isGameOver) {
                   for (int array_id : array_ids) {
                       TextView mTextView = view.findViewById(array_id);
                       if (!mTextView.getText().toString().isEmpty())
                           non_empty_textViews++;
                   }

                   if (non_empty_textViews >= 8) {
                       return;
                   } else {
                       this.context = context;
                       TextView mTextView = view.findViewById(comDecisionAlgorithm(view,view2, combinationsArray));
                       mTextView.setTextColor(colorChooser(context, getAIMarker()));
                       mTextView.setText(getAIMarker());
                   }
               }

               for (int[] ints : combinationsArray) {
                   String str = checkingForWinner(view, ints);
                   if (str.equals("AI")) {
                       //show dialog and set winner to user
                       Toast.makeText(context, "AI wins!!!",
                               Toast.LENGTH_LONG).show();
                       mMainHandler.post(()-> Snackbar.make(view2,"AI wins!!!",Snackbar.LENGTH_LONG).show());
                       isGameOver = true;
                       break;
                   }
               }

    }

    public boolean isSpotEmpty(View view){
        //check if textView.getText().isEmpty()
      int id = view.getId();
      TextView mTextView = view.findViewById(id);
     return  mTextView.getText().toString().isEmpty();
    }

    public int colorChooser(Context context, String marker){
        //for setting the color
        //blue and red,should be based on
        //AI==red
        //user == blue, irrespective of marker chosen
        this.context = context;
        if(marker.equals(getuserMarker()))
        return context.getResources().getColor(R.color.blue);
        else
        return  context.getResources().getColor(R.color.red);
    }

    public String checkingForWinner(View view, int[] array){
       //checking the 8 possible combinations if there is a winner
       //this check should be performed immediately the users marker is given a free spot
        int non_empty_spots=0;
        int user_marker_count = 0;
        int AI_marker_count=0;
        String winner = "none";
        for (int i1 : array) {
            TextView mTextView = view.findViewById(i1);
            if (!mTextView.getText().toString().isEmpty()) {
                non_empty_spots++;
                if (mTextView.getText().toString().equals(getuserMarker())) {
                    user_marker_count++;
                } else {
                    AI_marker_count++;
                }

                if ((non_empty_spots == 3) && (user_marker_count == 3)) {
                    //declare user winner
                    return winner = "USER";
                } else if ((non_empty_spots == 3) && (AI_marker_count == 3)) {
                    //declare AI winner
                    return winner = "AI";
                }
            }
        }

        return winner;
    }

    public int comDecisionAlgorithm(View view,View view2, int[][] array){
        //check if AI is one spot away from winning
        //then play to that spot
        int user_marker_count = 0;
        int AI_marker_count=0;
        int individual_array_free_spots=0;
        int j=0;
        int i=0;
        int k=0;
       index = -1;
       index2 = -1;
        boolean check=true;
        //checking which array has 2 spots occupied by the same marker
        //and having the 3rd or last spot empty

        for(i=0; i<array.length; i++) {
            for (j = 0; j < array[i].length; j++) {
                TextView mTextView = view.findViewById(array[i][j]);
                if (mTextView.getText().toString().isEmpty()) {
                    individual_array_free_spots++;
                } else if (!mTextView.getText().toString().isEmpty()) {
                    if (mTextView.getText().toString().equalsIgnoreCase(getAIMarker())) {
                        AI_marker_count++;
                    } else if (mTextView.getText().toString().equalsIgnoreCase(getuserMarker())) {
                        user_marker_count++;
                    }
                }
            }
                if (AI_marker_count == 2 && individual_array_free_spots == 1) {
                    //returning the array number
                    index = i;
                }else if(user_marker_count == 2 && individual_array_free_spots == 1) {
                    index = i;
                }

                individual_array_free_spots = 0;
                user_marker_count = 0;
                AI_marker_count = 0;

        }

        //using the array number to get the right spot to play
            if (index != -1) {
                //loop through the returned array and play the empty spot
                for (k=0; k<array[index].length; k++) {
                    Log.w("warning", "comDecisionAlgorithm: "+ array[index].length );
                    Log.w("warning", "comDecisionAlgorithm: "+ index);
                    TextView mTextView = view.findViewById(array[index][k]);
                    if (mTextView.getText().toString().isEmpty()) {
                        //exact position to be played to
                        index2 = array[index][k]; }
                }//end of for
            }//end of if

           if (index == -1) {
                //meaning there isn't an appropriate spot , play random
                return randomSpotGenerator(view,view2,array); }
        return index2;
        }
    public int randomSpotGenerator(View view,View view2, int[][] array) {
        //random spot generator
        index5 = -1;
        Random rand = new Random();
        Random rand2 = new Random();
        int index3 = rand.nextInt(array.length - 1);
        int index4 = rand2.nextInt(2);

        int id = array[index3][index4];

        TextView mTextView = view.findViewById(id);
        TextView mTextView2 = view.findViewById(view2.getId());

        if(mTextView.getText().toString().isEmpty() && mTextView.getId() != mTextView2.getId()){
             index5 = id;
        }else {
            //recursion to try again
            randomSpotGenerator(view,view2, array);
        }
        return index5;
    }

    public void resetBoard(View view, int[] array_ids){
        for(int i: array_ids){
           TextView mTextView = view.findViewById(i);
           mTextView.setText("");
        }
        isGameOver=false;
    }
}
