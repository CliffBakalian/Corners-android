package model;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.bakalian.clifford.corners.SettingsActivity;

import java.util.Random;

/**
 * Created by clifford on 12/18/17.
 */

public class Computer {
    private boolean done = false;
    private boolean isEasy;
    private int moves[], pieces[];
    private int movesLeft;
    private Context context;
    private int SIZE;
    public Computer(Context c, int difficulty){
        context = c;
        SIZE = SettingsActivity.boardSize;
        /*if (difficulty == 1)
            isEasy = true;
        else
            isEasy = false;
        */
        moves = new int[4 * (2*SIZE - 3)];
        resetMoves();
        movesLeft = 4;
        pieces = new int[]{1,1,1,1};
    }
    //this is a change
    private int getMoves(Board b, Piece p) {
        int count = 0;
        int xPos = p.getXPos();
        int yPos = p.getYPos();
        int xOri = p.getXOrientation();
        int yOri = p.getYOrientation();
        xPos += xOri;
        //Log.d("COMPUTER", "STARTING POS: (" + xPos + "," + yPos + ")");
        //Log.d("COMPUTER", "SIZE: " + SIZE);
        while (0 <= xPos && xPos < SIZE){
            //Log.d("COUNT:", count+"");
            if (!b.isOccupied(xPos, yPos))
                moves[count++] = (yPos * SIZE) + xPos;
            xPos += xOri;
        }
        xPos = p.getXPos();
        yPos = p.getYPos();
        yPos += yOri;
        while(0 <= yPos && yPos < SIZE) {
            //Log.d("COUNTY:", yPos+"");
            if (!b.isOccupied(xPos, yPos))
                moves[count++] = ((yPos) * SIZE) + xPos;
            yPos += yOri;
        }
        return count;
    }

    private Piece playMoves(Board b){
        int count = 0;
        int illegal = -1;
        Piece temp = new Piece(context, 0,0, SIZE);
        int movesCopy[] = moves.clone();
        while (movesCopy[count] != -1){
            temp.changeXPos(movesCopy[count]%SIZE);
            temp.changeYPos(movesCopy[count]/SIZE);
            for (int i = 0; i < 4; i++){
                if (pieces[i] == 1) {
                    temp.changeSprite();
                    temp.rotate(i * 90);
                    if (getMoves(b, temp) == 0) {
                        illegal = checkIllegals(temp);
                        if (illegal == -1) {
                            b.playerMove(temp);
                            pieces[i] = -1;
                            Log.d("COMPUTER MOVE:", "Ori: " + i + " at (" + temp.getXPos()+"," + temp.getYPos()+")");
                            movesLeft--;
                            Log.d("1st MOVE Left Error:","Moves left: " + movesLeft);
                            if (movesLeft <= 0)
                                reset();
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
        while (pieces[ori] != 1 || ori == illegal) {
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
        pieces[ori] = -1;
        Log.d("COMPUTER MOVE:", "Ori: " + ori + " at (" + temp.getXPos()+"," + temp.getYPos()+")");
        movesLeft--;
        Log.d("2nd MOVE Left Error:","Moves left: " + movesLeft);
        if (movesLeft <= 0)
            reset();
        Log.d("second COMPUTER MOVE:","Moves left: " + movesLeft);
        return temp;
    }

    private int force_move(int o, int i){
        for(int k = 0; k < 4; k++)
            if (pieces[k] == 1 && k != i)
                return k;
        Log.d("No possible match","Nothing available");
        return -1;
    }
    private int checkIllegals(Piece p){
        int xOri = p.getXOrientation();
        int yOri = p.getYOrientation();
        int xPos = p.getXPos();
        int yPos = p.getYPos();
        //Log.d("SOMETHING WRONG:", "xPos: " + xPos + "yPos: " + yPos + " xOri: " + xOri + " yOri: " + yOri);
        if (xPos == 0 && yPos == 0 && xOri == -1 && yOri == -1)
            return 3;
        if (xPos == SIZE-1 && yPos == 0 && xOri == 1 && yOri == -1)
            return 0;
        if (xPos == 0 && yPos == SIZE-1 && xOri == -1 && yOri == 1)
            return 2;
        if (xPos == SIZE-1 && yPos == SIZE-1 && xOri == 1 && yOri == 1)
            return 1;
        return -1;
    }

    private void reset(){
        for( int i = 0; i< 4; i++){
            pieces[i] = 1;
        }
        movesLeft = 4;
    }

    private void resetMoves(){
        for (int i = 0; i< (4 * (2* SIZE - 3)); i++){
            moves[i] = -1;
        }
    }

    public Piece play(Board b, Piece p){
        resetMoves();
        for(int j = 0; j< pieces.length; j++)
            Log.d("PIECES AVAILABLE:",""+pieces[j]);
        if (getMoves(b, p) == 0){
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
