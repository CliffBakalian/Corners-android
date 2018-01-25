package com.bakalian.clifford.corners;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SettingsActivity extends AppCompatActivity {

    public static int boardSize = 5;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
    }
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        //ImageView b1 = (ImageView) findViewById(R.id.easy_image);
        //b1.setBackgroundResource(R.drawable.easy_s);
        ImageView b2 = (ImageView) findViewById(R.id.sizeFive);
        b2.setBackgroundResource(R.drawable.setting_5_s);
    }
    /*
    public void setEasy(View view){
        ImageView b1 = (ImageView) findViewById(R.id.easy_image);
        b1.setBackgroundResource(R.drawable.easy_s);
        ImageView b2 = (ImageView) findViewById(R.id.hard_image);
        b2.setBackgroundResource(R.drawable.hard);
    }

    public void setNormal(View view){
        ImageView b1 = (ImageView) findViewById(R.id.easy_image);
        b1.setBackgroundResource(R.drawable.easy);
        ImageView b2 = (ImageView) findViewById(R.id.hard_image);
        b2.setBackgroundResource(R.drawable.hard_s);
    }
    */
    public void setFive(View view){
        boardSize = 5;
        ImageView b1 = (ImageView) findViewById(R.id.sizeFive);
        b1.setBackgroundResource(R.drawable.setting_5_s);
        ImageView b2 = (ImageView) findViewById(R.id.sizeSeven);
        b2.setBackgroundResource(R.drawable.setting_7);
    }

    public void setSeven(View view){
        boardSize = 7;
        ImageView b1 = (ImageView) findViewById(R.id.sizeFive);
        b1.setBackgroundResource(R.drawable.setting_5);
        ImageView b2 = (ImageView) findViewById(R.id.sizeSeven);
        b2.setBackgroundResource(R.drawable.setting_7_s);
    }

    public void setThemeOne(View view) {
        SharedPreferences sharedPref = getSharedPreferences("THEME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ONE_COLOR", "#00f2ff");
        editor.putString("TWO_COLOR", "#ff9000");
        editor.apply();
    }

    public void setThemeTwo(View view) {
        SharedPreferences sharedPref = getSharedPreferences("THEME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ONE_COLOR", "#721c47");
        editor.putString("TWO_COLOR", "#d1a377");
        editor.apply();
    }

    public void setThemeThree(View view) {
        SharedPreferences sharedPref = getSharedPreferences("THEME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ONE_COLOR", "#e18a07");
        editor.putString("TWO_COLOR", "#c0c0c0");
        editor.apply();
    }
    public void setThemeFour(View view) {
        SharedPreferences sharedPref = getSharedPreferences("THEME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ONE_COLOR", "#aaf200");
        editor.putString("TWO_COLOR", "#ffcf75");
        editor.apply();
    }
    public void setThemeFive(View view) {
        SharedPreferences sharedPref = getSharedPreferences("THEME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ONE_COLOR", "#ffd197");
        editor.putString("TWO_COLOR", "#7f3d17");
        editor.apply();
    }
    public void setThemeSix(View view) {
        SharedPreferences sharedPref = getSharedPreferences("THEME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("ONE_COLOR", "#2a2927");
        editor.putString("TWO_COLOR", "#fccf0d");
        editor.apply();
    }
    public void swap(View view) {
        String temp = "";
        SharedPreferences sharedPref = getSharedPreferences("THEME", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        temp = sharedPref.getString("ONE_COLOR","#00f2ff");
        editor.putString("ONE_COLOR", sharedPref.getString("TWO_COLOR","#ff9000"));
        editor.putString("TWO_COLOR", temp);
        editor.apply();
    }
    public void ReturnHome(View view){
        super.onBackPressed();
    }
}
