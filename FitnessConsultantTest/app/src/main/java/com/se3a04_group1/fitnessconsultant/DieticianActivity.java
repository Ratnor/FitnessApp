package com.se3a04_group1.fitnessconsultant;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.TextView;

public class DieticianActivity extends AppCompatActivity {
    int totalCalories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dietician);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        setTitle("Track Calories");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    public void onClickCalories(View view) {
        Context context = view.getContext();

        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);

        SharedPreferences.Editor editor = sharedPref.edit();



        EditText calories = (EditText) findViewById(R.id.editText_calories);
        String calories_string =  calories.getText().toString();

        TextView tot_cal = (TextView) findViewById(R.id.edit_TotCal);
        String tot_cal_string = tot_cal.getText().toString();

        int cal = Integer.parseInt(calories_string);
        totalCalories += cal;
        tot_cal.setText(totalCalories + "");

        if (!tot_cal_string.equals("N/A")){
            editor.putInt(getString(R.string.saved_totCal), totalCalories);

        }
        editor.commit();

        startDieticianService(view);


    }

    private void startDieticianService(View view) {
        startService(new Intent(getBaseContext(), DieticianService.class));
    }
}
