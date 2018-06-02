package model;

import android.content.Context;

/**
 * Created by clifford on 12/13/17.
 */

public class Player {
    protected String name;
    protected Board board;
    protected Piece pieces[];
    protected int movesLeft;
    protected Context context;
    public Player(String name, Context context, Board b){
        board = b;
        this.name = name;
        this.context = context;
        movesLeft = 4;
        pieces = new Piece[]{new Piece(context, 0,0, b.getSize()),new Piece(context, 0,0,b.getSize()),
                            new Piece(context, 0,0,b.getSize()),new Piece(context, 0,0,b.getSize())};
    }

    public boolean placePiece(int piece){
        if (pieces[piece] == null)
            return false;
        pieces[piece] = null;
        movesLeft--;
        if (movesLeft == 0){
            resetMoves();
        }
        return true;
    }

    public void resetMoves(){
        for (int i =0; i < 4; i++)
            pieces[i] = new Piece(context,0,0, board.getSize());
        movesLeft = 4;
    }

    public Piece[] getPieces(){ return pieces; }

    public int movesLeft(){ return movesLeft;}
    public String getName(){ return name; }
}
