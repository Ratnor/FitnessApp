package com.se3a04_group1.fitnessconsultant;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by Jay on 4/5/2016.
 */
public class v02Activity extends Activity {

    Button btnTimer,btnBack;
    TextView textTime,textDistance;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();

    // Timer for regular run
    Runnable timerRunnable = new Runnable() {
        public void run() {
            final long startTime = System.currentTimeMillis();
            long millis = (startTime + 720000) - System.currentTimeMillis();
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

        btnTimer = (Button) findViewById(R.id.btnTimer);
        btnBack = (Button) findViewById(R.id.btnBack);
        textTime = (TextView) findViewById(R.id.textTime);
        textDistance = (TextView) findViewById(R.id.textDistance);

        btnTimer.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Button b = (Button) v;
                if (b.getText().equals("stop")) {
                    timerHandler.removeCallbacks(timerRunnable);
                    b.setText("start");
                } else {

                    timerHandler.postDelayed(timerRunnable, 0);
                    b.setText("stop");
                }

            }
        });
    }

    // Sends back to session
    public void sendSession(View v){
        Intent intent = new Intent(this, SessionActivity.class);
        startActivity(intent);
    }



}
