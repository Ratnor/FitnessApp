package com.se3a04_group1.fitnessconsultant;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ReportActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setTitle("OVERALL FITNESS REPORT");

        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        TextView field;
        // Set BMI textview
        float bmi  = sharedPref.getFloat(getString(R.string.saved_bmi),-1.0f);
        if (bmi != -1.0f){
            field = (TextView) findViewById(R.id.textView_BMI);
            field.setText(String.valueOf(bmi) + " kg/m^2");
        }
        // Set Body Fat Percentage textview
        long bf = sharedPref.getLong(getString(R.string.saved_body_fat),-1);
        if (bf != -1){
            field = (TextView) findViewById(R.id.textView_BodyFat);
            field.setText(String.valueOf(bf) + "%");
        }

        //TODO: Implement editing the overall_assessment textView to display the results of BMI and BF (saved_bmi_category and saved_bf_category)

        //TODO: Implement checking Physician's Relative Fitness category

        //TODO: Implement checking Dietician's data

        float v02Max = sharedPref.getFloat(getString(R.string.saved_v02Max),-1.0f);
        if (v02Max != -1.0f){
            field = (TextView) findViewById(R.id.textView_VO2);
            field.setText(String.valueOf(v02Max) + "mL/(kg·min)");
        }

        //Button gender_button = (Button)findViewById(R.id.button_gender);
        //gender_button.setText("Female");


    }

}
