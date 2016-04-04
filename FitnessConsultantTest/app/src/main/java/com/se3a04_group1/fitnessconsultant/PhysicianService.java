package com.se3a04_group1.fitnessconsultant;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;

public class PhysicianService extends Service {
    public PhysicianService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Consulting Physician", Toast.LENGTH_LONG).show(); // THIS IS JUST FOR TESTING

        // retrieve the shared preferences file
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // retrieve stored profile data
        int weight = sharedPref.getInt(getString(R.string.saved_weight),-1);
        int heightFt = sharedPref.getInt(getString(R.string.saved_height_ft),-1);
        int heightIn = sharedPref.getInt(getString(R.string.saved_height_in),-1);
        int waist = sharedPref.getInt(getString(R.string.saved_waist_circum),-1);
        int gender = sharedPref.getInt(getString(R.string.saved_gender),-1);


        // calculate and store BMI
        if (weight != -1 && heightFt != -1 && heightIn != -1){
            float BMI = calculateBMI(weight, heightFt, heightIn);
            editor.putFloat(getString(R.string.saved_bmi), BMI);
            editor.putString(getString(R.string.saved_bmi_category),findBMICategory(BMI));
        }

        //calculate and store Body Fat Percentage
        if (weight != -1 && waist != -1 && gender != -1){
            long BF = calculateBodyFat(weight,waist,gender);
            editor.putLong(getString(R.string.saved_body_fat), BF);
            editor.putString(getString(R.string.saved_bf_category),findBFCategory(BF,gender));
        }

        //TODO: Implement calculateRelFit() - Calculate Relative Fitness

        editor.commit();

        stopSelf();
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Physician Done", Toast.LENGTH_LONG).show(); // THIS IS JUST FOR TESTING
    }

    // Calculates BMI in the Imperial system for an adult
    public float calculateBMI(int weight, int feet, int inches){
        int height = feet*12 + inches;
        float bmi = (weight * 703)/(height*height);
        return ( round(bmi,1) );
    }

    // Categorises by BMI for adults
    public String findBMICategory(float bmi){
        String category;
        if (bmi < 16){
            category = "Severe Thinness";
        }else if(bmi < 17) {
            category = "Moderate Thinness";
        }else if(bmi < 18.5){
            category = "Mild Thinness";
        }else if(bmi < 25){
            category = "Normal";
        }else if(bmi < 30){
            category = "Overweight";
        }else{
            category = "Obese";
        }
        return category;
    }

    // Calculates the Body Fat Percentage in Imperial system using YMCA formula
    public long calculateBodyFat(int weight, int waist, int gender){
        long bodyfat;
        if (gender == ProfileActivity.GENDER_FEMALE){
            //Log.d("BF", "In Female branch");
            // use female-specific formula
            bodyfat = Math.round(100*(-76.76 + (4.15 * waist) - (0.082 * weight)) / weight);
        }else{
            // use male-specific formula
            bodyfat =  Math.round(100*(-98.42 + (4.15 * waist) - (0.082 * weight)) /weight);
        }
        //System.out.println("Body fat: " + bodyfat);
        //Log.d("BF", "Body fat: " + bodyfat);
        return bodyfat;
    }

    // Categorises by Body Fat Percentage using the YMCA scale
    public String findBFCategory(long bf, int gender){
        String cat0 = "Dangerously Low";
        String cat1 = "Essential Fat";
        String cat2 = "Athletic";
        String cat3 = "Fit";
        String cat4 = "Acceptable";
        String cat5 = "Obese";
        long lim0,lim1,lim2,lim3,lim4;
        if (gender == ProfileActivity.GENDER_FEMALE){
            // use female-specific scale
            lim0 = 10; lim1 = 14; lim2 = 21; lim3 = 25; lim4 = 32;
        }else {
            // use male-specific scale
            lim0 = 2; lim1 = 6; lim2 = 14; lim3 = 18; lim4 = 25;
        }

        if (bf < lim0){
            return cat0;
        }else if(bf < lim1){
            return cat1;
        }else if(bf < lim2){
            return cat2;
        }else if(bf < lim3){
            return cat3;
        }else if(bf < lim4){
            return cat4;
        }else{return cat5;}
    }

    private static float round (float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }


}
