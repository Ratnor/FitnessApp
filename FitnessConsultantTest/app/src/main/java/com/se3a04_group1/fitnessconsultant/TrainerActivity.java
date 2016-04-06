package com.se3a04_group1.fitnessconsultant;

/**
 * Created by Jay on 4/4/2016.
 */

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Switch;
import android.widget.Toast;


public class TrainerActivity extends AppCompatActivity {


    Button btnTimer,btnBack;
    TextView textTime,textDistance;
    boolean on = false;
    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();

    // Timer for regular run
    Runnable timerRunnable = new Runnable() {
        public void run() {
            long millis = System.currentTimeMillis() - startTime;
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            textTime.setText(String.format("%d:%02d", minutes, seconds));

            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    // Changes stop to start
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        textTime.setText("start");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Run Tracker");


        btnTimer = (Button) findViewById(R.id.btnTimer);
        btnBack = (Button) findViewById(R.id.btnTimer);
        textTime = (TextView) findViewById(R.id.textTime);
        textDistance = (TextView) findViewById(R.id.textDistance);

        btnTimer.setOnClickListener(onClickListener);
        btnBack.setOnClickListener(onClickListener);

    }

    // Sends back to session
    public void sendSession(View view){
        Intent intent = new Intent(this, SessionActivity.class);
        startActivity(intent);
    }

    //
    public void startStop(View v){
        Button b = (Button) v;
        if (b.getText().equals("stop")) {
            timerHandler.removeCallbacks(timerRunnable);

            // Stops service for calculating distance
            Intent intent = new Intent(this,TrainerService.class);
            stopService(intent);
            b.setText("start");
        } else {
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);

            // Starts service for calculating distance
            Intent intent = new Intent(this,TrainerService.class);
            startService(intent);

            b.setText("stop");
        }
    }

    // Listener for buttons
    private View.OnClickListener onClickListener = new View.OnClickListener(){
                    @Override
                    public void onClick(View v){
                        switch(v.getId()){
                            case R.id.btnTimer:
                                startStop(v);
                                break;
                            case R.id.btnBack:
                    sendSession(v);
                    break;
            }
        }
    };

}
