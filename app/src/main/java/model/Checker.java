package model;

import android.content.Context;

import com.bakalian.clifford.corners.SettingsActivity;

public class Checker extends Player{
    protected int moves[];
    protected int SIZE;
    protected boolean done = false;
    protected Context context;
    public Checker(Context c,Board b){
        super("Computer",c, b);
        context = c;
        SIZE = SettingsActivity.boardSize;
        context = c;
        resetMoves();
    }

    /**
     * @param p the last piece that was played
     * @return which illegal move was played
     */
    protected int checkIllegals(Piece p){
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

    /**
     *
     * @param p the last piece played
     * @return
     */
    protected int getMoves(Piece p) {
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
            if (!board.isOccupied(xPos, yPos))
                moves[count++] = (yPos * SIZE) + xPos;
            xPos += xOri;
        }
        xPos = p.getXPos();
        yPos = p.getYPos();
        yPos += yOri;
        while(0 <= yPos && yPos < SIZE) {
            //Log.d("COUNTY:", yPos+"");
            if (!board.isOccupied(xPos, yPos))
                moves[count++] = ((yPos) * SIZE) + xPos;
            yPos += yOri;
        }
        return count;
    }
    /*
    protected void resetMoves(){
        for (int i = 0; i< (4 * (2* SIZE - 3)); i++){
            moves[i] = -1;
        }
    }
    */
    /*
    protected void reset(){
        for( int i = 0; i< 4; i++){
            pieces[i] = 1;
        }
        movesLeft = 4;
    }
    */
}
