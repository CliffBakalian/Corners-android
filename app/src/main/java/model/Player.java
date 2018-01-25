package model;

/**
 * Created by clifford on 12/13/17.
 */

public class Player {
    private String name;
    private int moves[];
    private int movesLeft;

    public Player(String name){
        this.name = name;
        movesLeft = 4;
        moves = new int[]{1,1,1,1};
    }

    public boolean placePiece(int piece){
        if (moves[piece] == 0)
            return false;
        moves[piece] = 0;
        movesLeft--;
        if (movesLeft == 0){
            resetMoves();
        }
        return true;
    }

    public void resetMoves(){
        for (int i =0; i < 4; i++)
            moves[i] = 1;
        movesLeft = 4;
    }

    public int[] getMoves(){ return moves; }

    public int movesLeft(){ return movesLeft;}
    public String getName(){ return name; }
}
