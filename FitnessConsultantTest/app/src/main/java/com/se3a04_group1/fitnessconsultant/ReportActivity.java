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

        int tot_cal = sharedPref.getInt(getString(R.string.saved_totCal), -1);
        if (tot_cal != -1){
            field = (TextView) findViewById(R.id.textView_CalsConsumed);
            field.setText(String.valueOf(tot_cal) + "cal");
        }

        float rec_cal = sharedPref.getFloat(getString(R.string.saved_recCal), -1);
        if (tot_cal != -1){
            field = (TextView) findViewById(R.id.textView_DailyCals);
            field.setText(String.valueOf(rec_cal) + "cal");
        }

        float v02Max = sharedPref.getFloat(getString(R.string.saved_v02Max),-1.0f);
        if (v02Max != -1.0f){
            field = (TextView) findViewById(R.id.textView_VO2);
            field.setText(String.valueOf(v02Max) + "mL/(kg·min)");
        }

        //Button gender_button = (Button)findViewById(R.id.button_gender);
        //gender_button.setText("Female");

        //float bmi  = sharedPref.getFloat(getString(R.string.saved_bmi),-1.0f);

        String bmiCat = sharedPref.getString("getString(R.string.saved_bmi_category)","");
        String bfCat = sharedPref.getString("getString(R.string.saved_bf_category)","");
        String exerciselvl = sharedPref.getString(getString(R.string.saved_exercise_lvl),"");
        TextView summary = (TextView) findViewById(R.id.textView_overall_assessment);
        summary.setText("");

        if ( (bmiCat.equals("Overweight")||bmiCat.equals("Obese"))  ){
            if (!bfCat.equals("Obese")){
                // case of a very fit person with high muscle density
                summary.append("Although your BMI puts you in the " + bmiCat + " category, your %Body Fat puts you in the " + bfCat + "category, " +
                        "so you are physically fit. BMI alone can be misleading when a person has high muscle mass, since muscle weighs more than fat. " +
                        "You should eat around " + rec_cal + "calories per day at your current fitness level of " + exerciselvl + ".");
            }else{
                summary.append("Your BMI puts you in the " + bmiCat + " category, and your %Body Fat puts you in the " + bfCat + "category, " +
                        "so may wish to consult your doctor to determine if your health is being affected. " +
                        "You should eat less than " + rec_cal + "calories per day at your current fitness level of " + exerciselvl + ".");
            }

        }else if (bmiCat.contains("Thinness") || bfCat.equals("Dangerously Low") || bfCat.equals("Essential Fat")){
            // case of an underweight person
            summary.append("Your BMI puts you in the " + bmiCat + " category, and your %Body Fat puts you in the " + bfCat + "category, " +
                    "so you should see a doctor because these numbers are low and may affect your health and lower your lifespan." +
                    "You should eat in excess of " + rec_cal + "calories per day at your current fitness level of " + exerciselvl + ".");
        }else{
            summary.append("Your BMI puts you in the " + bmiCat + " category, and your %Body Fat puts you in the " + bfCat + "category, " +
                    "which is generally within normal range." +
                    "You should eat in about " + rec_cal + "calories per day at your current fitness level of " + exerciselvl + ". " +
                    "You can eat more or less than this number to gain or lose weight or to adjust for changes in fitness level.");
        }

        float compare = tot_cal - rec_cal;

        if (compare > 10.0f){
            summary.append("\nYou have consumed a caloric excess today, based on your recommended caloric intake.");
        }else if (compare < -10.0f){
            summary.append("\nYou have consumed a caloric deficit today, based on your recommended caloric intake.");
        }else{
            summary.append("\nYou are approximately consuming the amount of calories recommended for you, based on your recommended caloric intake.");
        }


    }

}
