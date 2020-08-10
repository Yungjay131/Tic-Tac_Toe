package com.slyworks.tic_tac_toe;

import android.content.Intent;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private  static int[] array_ids = {R.id.textView1, R.id.textView2, R.id.textView3, R.id.textView4, R.id.textView5,
                                       R.id.textView6, R.id.textView7, R.id.textView8, R.id.textView9};
    public static int[] horizontal1 = {R.id.textView1, R.id.textView2, R.id.textView3};
    public static int[] horizontal2 = {R.id.textView4, R.id.textView5, R.id.textView6};
    public static int[] horizontal3 = {R.id.textView7, R.id.textView8, R.id.textView9};

    public static int[] vertical1 = {R.id.textView1, R.id.textView4, R.id.textView7};
    public static int[] vertical2 = {R.id.textView2, R.id.textView5, R.id.textView8};
    public static int[] vertical3 = {R.id.textView3, R.id.textView6, R.id.textView9};

    public static int[] diagonal1 = {R.id.textView1, R.id.textView5, R.id.textView9};
    public static int[] diagonal2 = {R.id.textView3, R.id.textView5, R.id.textView7};

    private static int[][] combinationsArray = {vertical1,vertical2,vertical3,horizontal1,horizontal2,
            horizontal3,diagonal1,diagonal2};
    private winner_dialog mWinner;

private final String TAG = getClass().getSimpleName();
private  controller control =  new controller();
    private View mView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
                setContentView(R.layout.activity_main);

        TextView mTextView1 = findViewById(R.id.textView1);
        TextView mTextView2 = findViewById(R.id.textView2);
        TextView mTextView3 = findViewById(R.id.textView3);
        TextView mTextView4 = findViewById(R.id.textView4);
        TextView mTextView5 = findViewById(R.id.textView5);
        TextView mTextView6 = findViewById(R.id.textView6);
        TextView mTextView7 = findViewById(R.id.textView7);
        TextView mTextView8 = findViewById(R.id.textView8);
        TextView mTextView9 = findViewById(R.id.textView9);

        mTextView1.setOnClickListener(this);
        mTextView2.setOnClickListener(this);
        mTextView3.setOnClickListener(this);
        mTextView4.setOnClickListener(this);
        mTextView5.setOnClickListener(this);
        mTextView6.setOnClickListener(this);
        mTextView7.setOnClickListener(this);
        mTextView8.setOnClickListener(this);
        mTextView9.setOnClickListener(this);

        mView = mTextView1.getRootView();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        selection_dialog selection = new selection_dialog();
        selection.show(getSupportFragmentManager(),"dialog1");



    }

    @Override
    public void onClick(final View view){
        final View view2  = view.getRootView();

        switch(view.getId()){
            case R.id.textView1:
            case R.id.textView2:
            case R.id.textView3:
            case R.id.textView4:
            case R.id.textView5:
            case R.id.textView6:
            case R.id.textView7:
            case R.id.textView8:
            case R.id.textView9:
              boolean isAiToPlay =  control.userPlay(view,view2,this,combinationsArray,array_ids);
              if(isAiToPlay) {
                  Handler mainHandler = new Handler(getMainLooper());
                  mainHandler.post(()->{
                      SystemClock.sleep(1000);
                      control.comPlay(view2, view, array_ids, MainActivity.this, combinationsArray);
                  });
              }
                break;
        }
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater mf = getMenuInflater();
        mf.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch(item.getItemId()){
            case R.id.action_reload:
                  control.resetBoard(mView, array_ids);
                selection_dialog selection = new selection_dialog();
                selection.show(getSupportFragmentManager(),"dialog2");
                return true;
            case R.id.action_settings:
                  startActivity(new Intent());
                  return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }

    public void showWinnerDialog(){

    }
}
