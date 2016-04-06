package com.se3a04_group1.fitnessconsultant;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.util.Log;
import android.widget.Toast;
/**
 * Created by Cole on 2016-04-06.
 */
public class DieticianService extends Service {
    public DieticianService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // Let it continue running until it is stopped.
        Toast.makeText(this, "Consulting Dietician", Toast.LENGTH_LONG).show(); // THIS IS JUST FOR TESTING

        // retrieve the shared preferences file
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();

        // retrieve stored profile data
        int age = sharedPref.getInt(getString(R.string.saved_age), -1);
        int weight = sharedPref.getInt(getString(R.string.saved_weight),-1);
        int heightFt = sharedPref.getInt(getString(R.string.saved_height_ft),-1);
        int heightIn = sharedPref.getInt(getString(R.string.saved_height_in),-1);
        int gender = sharedPref.getInt(getString(R.string.saved_gender),-1);
        int activity = sharedPref.getInt(getString(R.string.saved_exercise_lvl), -1);
        int totCal = sharedPref.getInt(getString(R.string.saved_totCal), -1);


        // calculate and store BMI
        if (weight != -1 && heightFt != -1 && heightIn != -1){
            float recCal = calculateRecCal(weight, heightFt, heightIn, age);
            editor.putFloat(getString(R.string.saved_recCal), recCal);
            editor.putString(getString(R.string.saved_cal_category), find_cal_Category(recCal-totCal));
        }


        //TODO: Implement calculateRelFit() - Calculate Relative Fitness

        editor.commit();

        stopSelf();
        return START_STICKY;
    }

    public String find_cal_Category(float calDiff) {
        String category;
        if (calDiff < -600){
            category = "Extremely Over Fed";
        }else if(-600 <= calDiff && calDiff <= -300) {
            category = "Somewhat Over Fed";
        }else if(-300 <= calDiff && calDiff <= 0){
            category = "Over Fed";
        }else if(0 <= calDiff && calDiff <= 300){
            category = "Under Fed";
        }else if(300 <= calDiff && calDiff <= 600){
            category = "Somewhat UnderFed";
        }else{
            category = "Extremely Under Fed";
        }
        return category;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Dietician Done", Toast.LENGTH_LONG).show(); // THIS IS JUST FOR TESTING
    }

    // Calculates BMI in the Imperial system for an adult
    public float calculateRecCal(int weight, int feet, int inches, int age){
        float height = (feet*12 + inches)*2.54f;
        float weight_kg = (weight)/2.2f;


        float recCal = 66.47f+(13.75f*weight_kg)+(height*5.0f)-(6.75f*age);
        return ( round(recCal,1) );
    }


    private static float round (float value, int precision) {
        int scale = (int) Math.pow(10, precision);
        return (float) Math.round(value * scale) / scale;
    }

}

