package com.se3a04_group1.fitnessconsultant;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;

public class ProfileActivity extends AppCompatActivity {

    ArrayAdapter<String> gender_adapter;
    ArrayAdapter<String> exercise_adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
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
        });
        */

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set view title
        setTitle("EDIT PROFILE");

        // CREATE THE SPINNER CHOICE BOXES
        // create age dropdown choices
        NoDefaultSpinner age_spin = (NoDefaultSpinner) findViewById(R.id.spinner_age);
        age_spin.setPrompt(getString(R.string.prompt_age));
        Integer[] ages = createRange(18, 100);
        ArrayAdapter<Integer> ages_adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item, ages);
        age_spin.setAdapter(ages_adapter);

        // create height (ft) dropdown choices
        NoDefaultSpinner height_ft_spin = (NoDefaultSpinner) findViewById(R.id.spinner_height_ft);
        height_ft_spin.setPrompt(getString(R.string.prompt_ft));
        Integer[] heightFt = createRange(4,8);
        ArrayAdapter<Integer> height_ft_adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item, heightFt);
        height_ft_spin.setAdapter(height_ft_adapter);

        // create height (in) dropdown choices
        NoDefaultSpinner height_in_spin = (NoDefaultSpinner) findViewById(R.id.spinner_height_in);
        height_in_spin.setPrompt(getString(R.string.prompt_in));
        Integer[] heightIn = createRange(0,11);
        ArrayAdapter<Integer> height_in_adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item, heightIn);
        height_in_spin.setAdapter(height_in_adapter);

        // create gender selector
        String[] gender_button = new String[] {"Female", "Male"};
        gender_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, gender_button);

        // create exercise level selector
        String[] exercise_button = new String[] {getString(R.string.exercise_opt0), getString(R.string.exercise_opt1), getString(R.string.exercise_opt2), getString(R.string.exercise_opt3)};
        exercise_adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, exercise_button);


    }

    // Returns an array with values in the range of start to end, inclusive. E.g. [start...end]
    public Integer[] createRange(int start, int end){
        int range = end - start + 1;
        Integer[] array = new Integer[range];
        for (int i = 0; i < range; i++){
            array[i] = start + i;
        }
        return array;
    }

    public void onClickGender(View w) {
        new AlertDialog.Builder(this)
                .setTitle("Select your gender")

                .setAdapter(gender_adapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Button gender_button = (Button)findViewById(R.id.button_gender);

                        switch(which){
                            case 0:{
                                gender_button.setText("Female");
                                break;
                            }
                            case 1: {
                                gender_button.setText("Male");
                                break;
                            }
                        }

                        dialog.dismiss();
                    }
                }).create().show();
    }

    public void onClickExercise(View w) {
        new AlertDialog.Builder(this)
                .setTitle("Select your exercise level for the average week")

                .setAdapter(exercise_adapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Button exercise_button = (Button)findViewById(R.id.button_exercise);

                        switch(which){
                            case 0:{
                                exercise_button.setText(R.string.exercise_opt0);
                                break;
                            }
                            case 1: {
                                exercise_button.setText(R.string.exercise_opt1);
                                break;
                            }
                            case 2: {
                                exercise_button.setText(R.string.exercise_opt2);
                                break;
                            }
                            case 3: {
                                exercise_button.setText(R.string.exercise_opt3);
                                break;
                            }
                        }

                        dialog.dismiss();
                    }
                }).create().show();
    }

    public void onClickSave(View w) {

    }


}
