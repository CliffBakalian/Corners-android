package model;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;

import com.bakalian.clifford.corners.R;

/**
 * Created by clifford on 12/16/17.
 */

public class Hud {

    private Bitmap sprite;
    private int xPos, yPos;
    public Hud(Context context, int id){
        if (id == 4)
            sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.settings);
        else if (id == 5)
            sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.submit);
        else {
            sprite = BitmapFactory.decodeResource(context.getResources(), R.drawable.piece);
            if (id < 5)
                rotate(90 * id);
        }
    }

    private void rotate(int degrees){
        Matrix matrix = new Matrix();
        matrix.postRotate(degrees);
        Bitmap scaledBitmap = Bitmap.createScaledBitmap(sprite, sprite.getWidth(), sprite.getHeight(), true);
        sprite = Bitmap.createBitmap(scaledBitmap , 0, 0, scaledBitmap .getWidth(), scaledBitmap .getHeight(), matrix, true);
    }

    public void onDraw(Canvas canvas, int x, int y){
        xPos = x;
        yPos = y;
        canvas.drawBitmap(sprite, xPos, yPos, null);
    }

    public int getSize(){
        return sprite.getWidth();
    }

    public boolean hasCollided(int x, int y){
        return (x > xPos && y > yPos && x < xPos + sprite.getWidth() && y < yPos + sprite.getHeight());
    }

}

