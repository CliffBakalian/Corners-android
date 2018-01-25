package com.bakalian.clifford.corners;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class StatActivity extends AppCompatActivity {

    TextView tG, tW, tP, pT, pE, pM, pH, eP, mP, hP;
    SharedPreferences sp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sp = getSharedPreferences("STAT_PREFS",Context.MODE_PRIVATE);
        setContentView(R.layout.activity_stat);

        tG = (TextView) findViewById(R.id.totalGames);
        tW = (TextView) findViewById(R.id.totalWins);
        tP = (TextView) findViewById(R.id.totalPercent);

        pT = (TextView) findViewById(R.id.totalPuzzles);
        pE = (TextView) findViewById(R.id.totalEasy);
        pM = (TextView) findViewById(R.id.totalMedium);
        pH = (TextView) findViewById(R.id.totalHard);
        eP = (TextView) findViewById(R.id.easyPercent);
        mP = (TextView) findViewById(R.id.mediumPercent);
        hP = (TextView) findViewById(R.id.hardPercent);

        tG.setText("" + sp.getInt("TOTALGAMES",0));
        tW.setText("" + sp.getInt("TOTALWINS",0));

        pT.setText("" + sp.getInt("TOTALPUZZ",0));
        pE.setText("" + sp.getInt("TOTALEASY",0));
        pM.setText("" + sp.getInt("TOTALMEDIUM",0));
        pH.setText("" + sp.getInt("TOTALHARD",0));

        calcTotalPercent();
    }

    public void resetScores(View view){
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt("TOTALGAMES", 0);
        editor.putInt("TOTALWINS", 0);
        editor.putInt("TOTALPUZZ", 0);
        editor.putInt("TOTALEASY", 0);
        editor.putInt("TOTALMEDIUM", 0);
        editor.putInt("TOTALHARD", 0);
        editor.apply();
        tG.setText("" + sp.getInt("TOTALGAMES",0));
        tW.setText("" + sp.getInt("TOTALWINS",0));
        calcTotalPercent();
    }

    public void calcTotalPercent(){
        double totalGames = (double)sp.getInt("TOTALGAMES",0);
        double totalWins = (double)sp.getInt("TOTALWINS",0);
        double totalPuzzles = (double)sp.getInt("TOTALPUZZ",0);
        double totalEasy = (double)sp.getInt("TOTALEASY",0);
        double totalMedium = (double)sp.getInt("TOTALMEDIUM",0);
        double totalHard = (double)sp.getInt("TOTALHARD",0);
        if (totalGames == 0){
            tP.setText("");
        }
        else
            tP.setText(String.format("%.2f", (totalWins/totalGames)* 100)+"%");
        if (totalPuzzles == 0){
            eP.setText("");
            hP.setText("");
            mP.setText("");
        }
        else{
            eP.setText(String.format("%.2f", (totalEasy/totalPuzzles)* 100)+"%");
            hP.setText(String.format("%.2f", (totalMedium/totalPuzzles)* 100)+"%");
            mP.setText(String.format("%.2f", (totalHard/totalPuzzles)* 100)+"%");
        }


    }
    public void ReturnHome(View view){
        super.onBackPressed();
    }
}
