package com.se3a04_group1.fitnessconsultant;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {

    static public int GENDER_FEMALE = 0; // these are defined by the order in the options list
    static public int GENDER_MALE = 1;

    NoDefaultSpinner age_spin;
    NoDefaultSpinner height_ft_spin;
    NoDefaultSpinner height_in_spin;
    int selectedGender = -1;
    int selectedExerciseLvl = -1;
    //String selectedExerciseLvl = "";

    ArrayAdapter<String> gender_adapter;
    ArrayAdapter<String> exercise_adapter;
    TextView dialog;

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

        dialog = (TextView) findViewById(R.id.textView_Profile);

        // CREATE THE SPINNER CHOICE BOXES
        // create age dropdown choices
        age_spin = (NoDefaultSpinner) findViewById(R.id.spinner_age);
        age_spin.setPrompt(getString(R.string.prompt_age));
        Integer[] ages = createRange(18, 100);
        ArrayAdapter<Integer> ages_adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item, ages);
        age_spin.setAdapter(ages_adapter);

        // create height (ft) dropdown choices
        height_ft_spin = (NoDefaultSpinner) findViewById(R.id.spinner_height_ft);
        height_ft_spin.setPrompt(getString(R.string.prompt_ft));
        Integer[] heightFt = createRange(4,8);
        ArrayAdapter<Integer> height_ft_adapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_dropdown_item, heightFt);
        height_ft_spin.setAdapter(height_ft_adapter);

        // create height (in) dropdown choices
        height_in_spin = (NoDefaultSpinner) findViewById(R.id.spinner_height_in);
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
                        selectedGender = which;

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
                        selectedExerciseLvl = which;

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

        Context context = w.getContext();
        /*File file = new File(context.getFilesDir(), getString(R.string.save_file_name));*/

        //retrieve the shared preferences file
        SharedPreferences sharedPref = context.getSharedPreferences(getString(R.string.preference_file_key),Context.MODE_PRIVATE);
        //create editor for shared preferences file
        SharedPreferences.Editor editor = sharedPref.edit();

        //write to shared preferences file
        boolean failFlag = false; // flag to check if any of the fields were left unfilled

        Button submit_button = (Button)findViewById(R.id.button_submit);
        dialog.setText("ERROR: You must input the following before pressing the \"Save Changes\" button:\n");
        // save contents of Age field
        if (age_spin.getSelectedItemPosition() != -1){
            editor.putInt(getString(R.string.saved_age), (Integer) age_spin.getSelectedItem());
            //editor.commit();
            //JUST FOR TESTING READING FROM THE FILE - REMOVE!
            //int test = sharedPref.getInt(getString(R.string.saved_age),-1);
            //submit_button.setText(String.valueOf(test));
        }else{
            failFlag = true;
            dialog.append("Age\n");
            /* cant get formatted text to work
            Resources res = getResources();
            String text = String.format(res.getString(R.string.profile_reset_hinttext),"Age");
            CharSequence styledText = Html.fromHtml(text);
            dialog.setText(styledText);
            */
        }
        // save contents of Height field
        if (height_ft_spin.getSelectedItemPosition() != -1 && height_in_spin.getSelectedItemPosition() != -1){
            editor.putInt(getString(R.string.saved_height_ft), (Integer) height_ft_spin.getSelectedItem());
            editor.putInt(getString(R.string.saved_height_in), (Integer) height_in_spin.getSelectedItem());
        }else{
            failFlag = true;
            dialog.append("Height\n");
        }
        // save contents of Gender field
        if (selectedGender != -1){
            editor.putInt(getString(R.string.saved_gender),selectedGender);
        }else{
            failFlag = true;
            dialog.append("Gender\n");
        }
        // save contents of Exercise Level field
        if (selectedExerciseLvl != -1){
            editor.putInt(getString(R.string.saved_exercise_lvl),selectedExerciseLvl);
        }else{
            failFlag = true;
            dialog.append("Exercise Level\n");
        }
        // save contents of Weight field
        EditText weight = (EditText) findViewById(R.id.editText_weight);
        String weight_string =  weight.getText().toString();

        if (!weight_string.equals("")){
            editor.putInt(getString(R.string.saved_weight),Integer.parseInt(weight_string));
        } else {
            failFlag = true;
            dialog.append("Weight\n");
        }
        // save contents of Waist Circumference field
        EditText waist = (EditText) findViewById(R.id.editText_waist);
        String waist_string =  waist.getText().toString();

        if (!waist_string.equals("")){
            editor.putInt(getString(R.string.saved_waist_circum),Integer.parseInt(waist_string));
        } else {
            failFlag = true;
            dialog.append("Waist Circumference\n");
        }
        //startPhysicianService(w);
        //stopService(w);

        // Redirection to next Activity upon button press
        if (failFlag == false){ // if all the fields are filled
            // commit the saves to the editor
            editor.commit();

            startPhysicianService(w); // CAN MOVE THIS LINE OUTSIDE FLAG CHECK FOR TESTING PURPOSES (then dont need to fill all fields)

            //TODO: Call Dietician: Daily Rec Cal Intake

            // Redirect to the Overall Fitness Report page
            Intent intent = new Intent(this, ReportActivity.class);
            startActivity(intent);

            this.finish(); // not sure if this is the correct way to end the activity
        }else{
            editor.clear(); // do not commit changes to datastore
        }

    }

    // Method to start the service
    public void startPhysicianService(View view) {
        startService(new Intent(getBaseContext(), PhysicianService.class));
    }

    // Method to stop the service
    public void stopService(View view) {
        stopService(new Intent(getBaseContext(), PhysicianService.class));
    }


}
