package com.bakalian.clifford.corners;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.StateSet;
import android.view.View;

import view.GameView;


public class MainActivity extends AppCompatActivity {

    public static int num_players = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void startSinglePlayer(View view){
        num_players = 1;
        Intent newGame = new Intent(this, GameActivity.class);
        startActivity(newGame);
    }

    public void startMultiPlayer(View view){
        num_players = 2;
        Intent newGame = new Intent(this, GameActivity.class);
        startActivity(newGame);
    }

    public void settings(View view) {
        Intent settings = new Intent(this, SettingsActivity.class);
        startActivity(settings);
    }

    public void stats(View view){
        Intent stats = new Intent(this, StatActivity.class);
        startActivity(stats);
    }
    public void about(View view) {
        Intent about = new Intent(this, AboutActivity.class);
        startActivity(about);
    }
}
