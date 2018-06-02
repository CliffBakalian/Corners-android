package model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bakalian.clifford.corners.SettingsActivity;

import java.util.Random;

/**
 * Created by clifford on 12/18/17.
 */

public class Computer extends Checker{
    public Computer(Context c, Board b){
        super(c,b);
    }

    private Piece playMoves(Board b){
        int count = 0;
        int illegal = -1;
        Piece temp = new Piece(context, 0,0, b.getSize());
        int movesCopy[] = moves.clone();
        while (movesCopy[count] != -1){
            temp.changeXPos(movesCopy[count]%SIZE);
            temp.changeYPos(movesCopy[count]/SIZE);
            for (int i = 0; i < 4; i++){
                if (pieces[i] != null) {
                    temp.changeSprite();
                    temp.rotate(i * 90);
                    if (getMoves(temp) == 0) {
                        illegal = checkIllegals(temp);
                        if (illegal == -1) {
                            b.playerMove(temp);
                            pieces[i] = null;
                            Log.d("COMPUTER MOVE:", "Ori: " + i + " at (" + temp.getXPos()+"," + temp.getYPos()+")");
                            movesLeft--;
                            Log.d("1st MOVE Left Error:","Moves left: " + movesLeft);
                            if (movesLeft <= 0)
                                resetMoves();
                            Log.d("1st COMPUTER MOVE:","Moves left: " + movesLeft);
                            return temp;
                        }

                    }
                    moves = movesCopy.clone();
                }
            }
            count++;
        }
        int index;
        int ori = (int) Math.floor(Math.random() * 4);
        //int max = (SIZE*SIZE) - 1;
        //int maxX = (SIZE * (SIZE - 1));
        index = (int) Math.floor(Math.random() * count);
        //Log.d("ERROR", "Moves Left: " + movesLeft + "illegal: " + illegal);
        //Log.d("ERROR", "PRE: index: " + index + " ori: "+ ori);
        int infinite = 0;
        while (pieces[ori] == null || ori == illegal) {
            infinite++;
            if (infinite > 50){
                ori = force_move(ori,illegal);
                if(ori == -1)
                    return null;
            }
            Log.d("ERROR LOOP?", "Pieces[ori]: " + pieces[ori]);
            //Log.d("ERROR", "Moves Left: " + movesLeft);
            Log.d("ERROR", "ori: "+ ori + " illegal: " + illegal);
            ori = (int) Math.floor(Math.random() * 4);
        }
        //Log.d("ERROR", "POST: index: " + index + " ori: "+ ori);
        temp.changeXPos(moves[index]%SIZE);
        temp.changeYPos(moves[index]/SIZE);
        temp.changeSprite();
        temp.rotate(ori * 90);
        b.playerMove(temp);
        pieces[ori] = null;
        Log.d("COMPUTER MOVE:", "Ori: " + ori + " at (" + temp.getXPos()+"," + temp.getYPos()+")");
        movesLeft--;
        Log.d("2nd MOVE Left Error:","Moves left: " + movesLeft);
        if (movesLeft <= 0)
            resetMoves();
        Log.d("second COMPUTER MOVE:","Moves left: " + movesLeft);
        return temp;
    }

    private int force_move(int o, int i){
        for(int k = 0; k < 4; k++)
            if (pieces[k] != null && k != i)
                return k;
        Log.d("No possible match","Nothing available");
        return -1;
    }

    public Piece play(Board b, Piece p){
        resetMoves();
        for(int j = 0; j< pieces.length; j++)
            Log.d("PIECES AVAILABLE:",""+pieces[j]);
        if (getMoves(p) == 0){
            SharedPreferences sharedPref = context.getSharedPreferences("STAT_PREFS", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            int i = sharedPref.getInt("TOTALWINS", 0);
            //Log.d("WIN INITIAL", "i: " + i);
            i++;
            editor.putInt("TOTALWINS", i);
            editor.apply();
            //Log.d("WIN SHOULD INCRESASE", "i: " + i);
            Log.d("GAME", "GAME OVER");
            done = true;
            return null;
        }
        return playMoves(b);
    }

    public boolean getDone(){ return done;}

}
