/**
 * Created by clifford on 12/13/17.
 */
package model;

import android.graphics.*;
import android.util.Log;
import android.view.WindowManager;

import com.bakalian.clifford.corners.R;

import view.GameView;


public class Board {
    private Piece board[][];
    private int SIZE;
    private int xPos, yPos;
    private Bitmap sprite;
    private int border;
    private int pieceSize;
    public Board(GameView gm, int x, int size) {
        SIZE = size;
        if (size == 7)
            sprite = BitmapFactory.decodeResource(gm.context.getResources(), R.drawable.seven);
        else
            sprite = BitmapFactory.decodeResource(gm.context.getResources(), R.drawable.five);
        board = new Piece[SIZE][SIZE];
        Piece temp = new Piece(gm.context, 0, 0, SIZE);
        pieceSize = temp.getWidth();
        border = (sprite.getWidth() - (temp.getHeight() * size)) / 2;
        for (int i = 0; i < SIZE; i++)
            for (int j = 0; j < SIZE; j++)
                board[i][j] = null;
        xPos = (x - sprite.getWidth())/2;
        yPos = (sprite.getHeight()/5);
    }

    public boolean isOccupied(int x, int y){
        return board[x][y] != null;
    }

    private void addPiece(Piece piece){
        board[piece.getXPos()][piece.getYPos()] = piece;

    }
    public boolean validMove(Player player, Piece piece){
        int x = piece.getXPos();
        int y = piece.getYPos();
        int count = 0;
        while (0 <= x && x < SIZE){
            if (!(isOccupied(x,y)))
                count++;
            if (count > 1)
                return true;
            x += piece.getXOrientation();
        }
        x = piece.getXPos();
        while (0 <= y && y < SIZE){
            if (!isOccupied(x,y))
                count++;
            if (count > 1)
                return true;
            y += piece.getYOrientation();
        }

        int moves[] = player.getMoves();
        if (x == 0 && y == 0 && moves[3] == 0)
            count -= 1;
        else if (x == 0 && y == SIZE && moves[2] == 0)
            count -= 1;
        else if (x == SIZE && y == 0 && moves[0] == 0)
            count -= 1;
        else if (x == SIZE && y == SIZE && moves[1] == 0)
            count -= 1;
        return count != 0;
    }

    public void draw(Canvas canvas){
        canvas.drawBitmap(sprite,xPos,yPos,null);
        for( int i = 0; i< SIZE; i++){
            for( int j = 0; j < SIZE; j++){
                if(isOccupied(i, j)){
                    board[i][j].onDraw(canvas, xPos + i*board[i][j].getWidth() + border, yPos + j*board[i][j].getHeight() + border);
                }
            }
        }
    }

    public int getBoardSizeX(){ return sprite.getWidth(); }

    public int getBoardPosX(){ return xPos;}
    public int getBoardPosY(){ return yPos;}

    public void playerMove(Piece piece){ addPiece(piece); }

    public double getPieceSize(){return pieceSize;}
    public int getBorder() { return border;}
}

