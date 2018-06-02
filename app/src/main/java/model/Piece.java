package model;

import android.content.Context;
import android.graphics.*;
import android.util.Log;

import com.bakalian.clifford.corners.R;


/**
 * Created by clifford on 12/13/17.
 */

public class Piece {
    private int xPos, yPos;
    private int width, height, orientation;
    private Bitmap sprite;
    private Context context;
    private boolean isBlank;
    private int size;
    public Piece(Context context, int x, int y, int size){
        xPos = x;
        yPos = y;
        this.context = context;
        this.size = size;
        orientation = 0;
        if(size ==7)
            sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.sevenpiece);
        else
            sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.fivepiece);
        width = sprite.getWidth();
        height = sprite.getHeight();
    }

    public void onDraw(Canvas canvas, int row, int col){
        canvas.drawBitmap(sprite, row, col, null);
    }
    public Piece(Piece p){
        this(p.context, p.getXPos(),p.getYPos(),p.size);
        this.sprite = p.sprite;
        this.orientation = p.orientation;
    }

    public int getXPos(){ return xPos; }

    public int getYPos(){ return yPos; }

    public int getXOrientation(){
        if (orientation > 1)
            return -1;
        return 1;
    }

    public int getYOrientation(){
        if (orientation == 0 || orientation == 3)
            return -1;
        return 1;
    }

    public void rotate(int degrees){
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(sprite, sprite.getWidth(), sprite.getHeight(), true);
        sprite = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
        switch(degrees){
            case 270: orientation++;
            case 180: orientation++;
            case 90:  orientation++; break;
            default: break;
        }
        if (orientation == 4)
            orientation = 0;
        if (orientation == 5)
            orientation = 1;
        if (orientation == 6)
            orientation = 2;
    }

    public int getWidth(){ return width;}

    public int getHeight(){ return height;}

    public void setBlank(){
        isBlank = true;
        sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.blank);
    }
    public void changeSprite(){
        isBlank = false;
        orientation = 0;
        if (size == 7)
        sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.sevenpiece);
        else
            sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.fivepiece);
    }

    public void changeXPos(int i ){
        xPos = i;
    }

    public void changeYPos(int i ){
        yPos = i;
    }

    public boolean isBlank(){
        return isBlank;
    }

    public Bitmap getSprite(){
        return sprite;
    }
}
