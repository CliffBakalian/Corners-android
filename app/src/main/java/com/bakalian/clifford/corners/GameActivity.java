package com.bakalian.clifford.corners;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;

import view.GameView;

public class GameActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int bsize = SettingsActivity.boardSize;
        setContentView(new GameView(this, size.x, size.y, bsize));
    }

    @Override
    public void onBackPressed() {
        if (MainActivity.num_players == 1) {
            SharedPreferences sharedPref = getSharedPreferences("STAT_PREFS",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            int i = sharedPref.getInt("TOTALGAMES", 0);
            i++;
            editor.putInt("TOTALGAMES", i);
            editor.apply();
        }
        super.onBackPressed();
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        View decorView = getWindow().getDecorView();
        if (hasFocus) {
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
    
}
