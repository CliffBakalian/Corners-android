package model;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;
import android.view.MotionEvent;

//import com.bakalian.clifford.corners.R;

import java.nio.channels.FileChannel;

import view.GameView;

import static android.content.Context.MODE_PRIVATE;


/**
 * Created by clifford on 12/15/17.
 */

public class Game {
    private GameView gv;
    public Board gameBoard;
    private int PHONEWIDTH, PHONEHEIGHT;

    private Piece temp, last;

    private int lastX = -1, lastY = -1;
    private int pieceSize;
    private Player players[];
    private Hud HUD[];
    private boolean player1 = true;
    private Computer computer;
    private int SIZE;
    private boolean gameOver = false, allowSubmit = true, isIllegal = false, overTouch = false;
    private boolean doubletap = false;
    private Rect rightBottom, leftBottom, rightTop, leftTop, illigalLeft, illegalRight;
    private int topHeight, sideWidth, illegalWidth;
    private String oneColor, twoColor;

    public Game(GameView gv, int x, int y, int numPlayers, int size){
        this.gv = gv;
        PHONEWIDTH = x;
        PHONEHEIGHT = y;
        SIZE = size;
        HUD = new Hud[6];
        for (int i = 0; i< 6; i++){
            HUD[i] = new Hud(gv.context, i);
        }
        if (numPlayers == 2){
            computer = null;
        }
        else
            computer = new Computer(gv.context, 0);
        gameBoard = new Board(gv, x, SIZE);
        pieceSize = (int) gameBoard.getPieceSize();
        players = new Player[numPlayers];
        players[0] = new Player("Player 1");
        if (numPlayers == 2)
            players[1] = new Player("Player 2");
        temp = new Piece(gv.context, 0,0, SIZE);
        temp.setBlank();
        last = null;
        sideWidth = (int)(PHONEWIDTH * .03);
        illegalWidth = 0;
        topHeight = PHONEHEIGHT;
        int rightSideX = PHONEWIDTH - sideWidth;
        rightTop= new Rect(rightSideX,0,PHONEWIDTH, topHeight);
        leftTop = new Rect(0,0,sideWidth, topHeight);
        rightBottom= new Rect(rightSideX,0,PHONEWIDTH, 2 *PHONEHEIGHT);
        leftBottom = new Rect(0,0,sideWidth, 2*PHONEHEIGHT);
        illegalRight= new Rect(PHONEWIDTH,0,PHONEWIDTH, 2*PHONEHEIGHT);
        illigalLeft = new Rect(0,0,0, 2*PHONEHEIGHT);
        SharedPreferences shareprefs = gv.context.getSharedPreferences("THEME", MODE_PRIVATE);
        oneColor = shareprefs.getString("ONE_COLOR","#00f2ff");
        twoColor = shareprefs.getString("TWO_COLOR","#ff9000");
    }

    public void start(){
        player1 = true;

    }

    private boolean nextMove(){
        int playerIndex = (player1 || players.length == 1)? 0: 1;
        player1 = !player1;
        topHeight = 0;
        if (players.length != 2) {
            last = computer.play(gameBoard, last);
            if (last == null) {
                if (computer.getDone())
                    player1 = !player1;
                return false;
            }
            else
                player1 = !player1;
        }
        boolean t = gameBoard.validMove(players[playerIndex], last);
        if (!t)
            player1 = !player1;
        return (t);
    }

    public void registerTouch(MotionEvent event){
        int x = (int) event.getX();
        int y = (int) event.getY();
        int boardx = gameBoard.getBoardPosX();
        int boardy = gameBoard.getBoardPosY();
        /*if (gameOver && allowSubmit)
            Log.d("GAME OVER:","YOU CAN TOUCH END GAME NOW");
        else if (gameOver)
            Log.d("GAME OVER:","YOU CANNOT TOUCH");
            */
        if (gameOver && doubletap){
            gv.ga.onBackPressed();
        }
        else if (gameOver)
            overTouch = true;
        else {
            if (lastY != -1 && HUD[5].hasCollided((int) event.getX(), (int) event.getY())) {
                if (allowSubmit)
                    touchSubmit();
            } else if (x > boardx && x < boardx + gameBoard.getBoardSizeX() &&
                    y > boardy && y < boardy + gameBoard.getBoardSizeX()) {
                //Log.d("CRASH PRE", "("+x+","+y+")");
                x -= boardx;
                x /= pieceSize;
                y -= boardy;
                y /= pieceSize;
                if (x >= SIZE)
                    x = SIZE - 1;
                if (y >= SIZE)
                    y = SIZE - 1;
                //Log.d("CRASH POST", "("+x+","+y+")");
                if (!gameBoard.isOccupied(x, y)) {
                    if (temp.isBlank()) {
                        temp.changeSprite();
                    }
                    if (x == lastX && y == lastY) {
                        temp.rotate(90);
                        //Log.d("Touch", "ROTATED");
                    } else {
                        lastY = y;
                        lastX = x;
                        temp.changeSprite();
                        temp.changeXPos(x);
                        temp.changeYPos(y);
                        //Log.d("Touch", "MOVED");
                    }
                }
            } else if (!temp.isBlank() && !hubCollied((int) event.getX(), (int) event.getY(), temp) &&
                    !HUD[4].hasCollided((int) event.getX(), (int) event.getY())) {
                temp.setBlank();
                lastX = -1;
                lastY = -1;
            }
        }
    }

    private void touchSubmit(){
        int playerIndex;
        boolean illegal = true;

        if (players.length == 2 && !player1){
            playerIndex = 1;
        }
        else
            playerIndex = 0;

        if (!checkIllegals(temp.getXPos(), temp.getYPos())) {
            if (temp.getXOrientation() == -1 && temp.getYOrientation() == -1) {
                if (!players[playerIndex].placePiece(3))
                    isIllegal= true;
                else
                    illegal = false;
            } else if (temp.getXOrientation() == -1 && temp.getYOrientation() == 1) {
                if (!players[playerIndex].placePiece(2))
                    isIllegal= true;
                else
                    illegal = false;

            } else if (temp.getXOrientation() == 1 && temp.getYOrientation() == 1) {
                if (!players[playerIndex].placePiece(1))
                    isIllegal = true;
                else
                    illegal = false;
            } else {
                if (!players[playerIndex].placePiece(0))
                    isIllegal = true;
                else
                    illegal = false;
            }
            if (!illegal) {
                Piece t = new Piece(temp);
                last = t;
                gameBoard.playerMove(t);
                temp.setBlank();
                lastX = -1;
                lastY = -1;
                if (!nextMove()) {
                    gameOver = true;
                    allowSubmit = false;
                }
                //Log.d("TOUCH", "PLACED at (" + "\t" + t.getXPos() + "," + t.getYPos()+")");
                //Log.d("PLACEMENT", "X ori: " + t.getXOrientation() + "\tY ori: " + t.getYOrientation());
            }
        }
        else {
            //Log.d("ILLEGALS MOVE:","ISILLEGAL is true");
            isIllegal = true;
        }
    }
    private boolean checkIllegals(int x, int y){
        if (last == null) {
            //Log.d("ILLEGAL:", x +"\t" + y + "\t" + (SIZE/2));
            return !(x == (SIZE / 2) && y == SIZE / 2);
        }
        //Log.d("INPUT:", "x: " + x + "\ty: " + y);
        int xOri = last.getXOrientation();
        int yOri = last.getYOrientation();

        if (x == 0 && y == 0)
            if (temp.getXOrientation() == -1 && temp.getYOrientation() == -1)
                return true;
        if (x == 0 && y == (SIZE - 1))
            if (temp.getXOrientation() == -1 && temp.getYOrientation() == 1)
                return true;
        if (x == (SIZE - 1) && y == 0) {
            //Log.d("ERROR:", "x: " + temp.getXOrientation() + "\ty: " + temp.getYOrientation());
            //Log.d("ERROR:", ((temp.getXOrientation() == 1 && temp.getYOrientation() == 1)) + "");
            if (temp.getXOrientation() == 1 && temp.getYOrientation() == -1)
                return true;
        }
        if (x == (SIZE - 1) && y == (SIZE - 1)) {
            //Log.d("ERROR:", "x: " + temp.getXOrientation() + "\ty: " + temp.getYOrientation());
            //Log.d("ERROR:", ((temp.getXOrientation() == 1 && temp.getYOrientation() == 1)) + "");
            if (temp.getXOrientation() == 1 && temp.getYOrientation() == 1)
                return true;
        }

        if (xOri == -1 && yOri == -1)
            return !((x == last.getXPos() && y < last.getYPos()) || (x < last.getXPos() && y == last.getYPos()));
        else if (xOri == -1 && yOri == 1)
            return !((x == last.getXPos() && y > last.getYPos()) || (x < last.getXPos() && y == last.getYPos()));
        else if (xOri == 1 && yOri == -1) {
            return !((x == last.getXPos() && y < last.getYPos()) || (x > last.getXPos() && y == last.getYPos()));
        }
        else
            return !((x == last.getXPos() && y > last.getYPos()) || (x > last.getXPos() && y == last.getYPos()));
    }

    private boolean hubCollied(int x, int y, Piece p){
        int playerIndex = (player1 || players.length == 1)? 0: 1;
        int pieces[] = players[playerIndex].getMoves();
        for (int i = 0; i< 4; i++){
            if (pieces[i] == 1) {
                if (HUD[i].hasCollided(x,y)) {
                    p.changeSprite();
                    //Log.d("TOUCH:", "ROTATED: " + (i * 90));
                    p.rotate(i * 90);
                    return true;
                }
            }
        }
        return false;
    }

    public void draw(Canvas canvas){
        if (players.length == 2)
            drawSides(canvas);
        drawIllegal(canvas);
        drawLast(canvas);
        drawMessage(canvas);
        drawHub(canvas);
        gameBoard.draw(canvas);
        drawTemp(canvas);
        if(gameOver && overTouch)
            drawEndGame(canvas);
    }

    private void drawEndGame(Canvas canvas){
        Paint paint = new Paint();
        if (!player1){
            paint.setColor(Color.parseColor(twoColor));
        }
        else {
            paint.setColor(Color.parseColor(oneColor));
        }
        rightTop.right= PHONEWIDTH;
        rightTop.bottom = 2 * PHONEHEIGHT;
        leftTop.left= 0;
        leftTop.bottom = 2 * PHONEHEIGHT;
        if(rightTop.left > PHONEWIDTH/2)
            rightTop.left -= PHONEWIDTH/100;
        if (leftTop.right < PHONEWIDTH/2)
            leftTop.right += PHONEWIDTH/100;
        canvas.drawRect(rightTop, paint);
        canvas.drawRect(leftTop, paint);
        if (!(leftTop.right < PHONEWIDTH/2) && !(rightTop.left > PHONEWIDTH/2)){
            doubletap = true;
            String message;
            if (players.length == 1 && !player1)
                message = "GAME OVER";
            else if (players.length ==1 && player1)
                message = "YOU WIN!";
            else if (players.length == 2 && !player1)
                message = "Player 2 Wins";
            else
                message = "Player 1 Wins";
            Paint mPaint = new Paint();
            mPaint.setTextSize((int) (gameBoard.getBoardPosX() * 0.8));
            int textPos = (int) mPaint.measureText(message);
            float scale = 0.8f;
            while (textPos > (gameBoard.getBoardSizeX() * 0.8)) {
                mPaint.setTextSize((int) (gameBoard.getBoardPosX() * scale));
                textPos = (int) mPaint.measureText(message);
                scale -= .1f;
            }
            textPos = (PHONEWIDTH - textPos) / 2;
            int textHeight = (int)(gameBoard.getBoardPosX() * scale);
            textHeight = (PHONEHEIGHT - textHeight)/2;
            canvas.drawText(message, textPos, textHeight, mPaint);
        }

    }

    private void drawLast(Canvas canvas) {
        if (last != null) {
            Paint paint = new Paint();
            paint.setColor(Color.GRAY);
            int lastx = gameBoard.getBoardPosX() + gameBoard.getBorder() + (last.getXPos() * last.getWidth());
            int lasty = gameBoard.getBoardPosY() + gameBoard.getBorder() + (last.getYPos() * last.getHeight());
            Rect drawLast = new Rect(lastx, lasty, lastx+ last.getWidth(), lasty+ last.getHeight());
            canvas.drawRect(drawLast, paint);
        }
    }

    private void drawIllegal(Canvas canvas){
        Paint paint = new Paint();
        paint.setColor(Color.parseColor("#ff1616"));
        if(isIllegal && illegalWidth < sideWidth){
            illegalWidth +=2;
            illigalLeft.right = illegalWidth;
            illegalRight.left = PHONEWIDTH - illegalWidth;
            canvas.drawRect(illigalLeft,paint);
            canvas.drawRect(illegalRight,paint);
        }
        if(!isIllegal && illegalWidth > 0){
            illegalWidth -=2;
            illigalLeft.right = illegalWidth;
            illegalRight.left = PHONEWIDTH - illegalWidth;
            canvas.drawRect(illigalLeft,paint);
            canvas.drawRect(illegalRight,paint);
        }
        if(isIllegal && illegalWidth >= sideWidth){
            isIllegal = false;
        }
    }
    private void drawSides(Canvas canvas) {
        Paint paint = new Paint();
        if (player1)
            paint.setColor(Color.parseColor(twoColor));
        else
            paint.setColor(Color.parseColor(oneColor));
        canvas.drawRect(rightBottom, paint);
        canvas.drawRect(leftBottom, paint);
        if (!gameOver) {
            if (topHeight < 2 * PHONEHEIGHT) {
                allowSubmit = false;
                topHeight += 30;
                rightTop.bottom = topHeight;
                leftTop.bottom = topHeight;
            }
        }
        if(topHeight >= PHONEHEIGHT)
            allowSubmit = true;
        if (player1)
            paint.setColor(Color.parseColor(oneColor));
        else
            paint.setColor(Color.parseColor(twoColor));
        canvas.drawRect(rightTop, paint);
        canvas.drawRect(leftTop,paint);
    }
    private void drawMessage(Canvas canvas){
        if(gameOver){
            String message;
            if(!player1)
                if (players.length == 2)
                    message = "Player 2 wins";
                else {
                    message = "Computer Wins";
                }
            else
                message = "Player 1 wins";
            Paint mPaint = new Paint();
            mPaint.setTextSize((int) (gameBoard.getBoardPosX() * 0.8));
            int textPos = (int) mPaint.measureText(message);
            float scale = 0.8f;
            while (textPos > (gameBoard.getBoardSizeX() * 0.8)) {
                mPaint.setTextSize((int) (gameBoard.getBoardPosX() * scale));
                textPos = (int) mPaint.measureText(message);
                scale -= .1f;
            }
            textPos = (PHONEWIDTH - textPos) / 2;
            int textHeight = (int)(gameBoard.getBoardPosX() * scale);
            textHeight = (gameBoard.getBoardPosY() - textHeight)/2;
            canvas.drawText(message, textPos, gameBoard.getBoardPosY() -textHeight, mPaint);
        }
         else if (players.length == 2) {
            String message;
            if (player1)
                message = "PLAYER 1's TURN";
            else
                message = "PLAYER 2's TURN";
            Paint mPaint = new Paint();
            mPaint.setTextSize((int) (gameBoard.getBoardPosX() * 0.8));
            int textPos = (int) mPaint.measureText(message);
            float scale = 0.8f;
            while (textPos > (gameBoard.getBoardSizeX() * 0.8)) {
                mPaint.setTextSize((int) (gameBoard.getBoardPosX() * scale));
                textPos = (int) mPaint.measureText(message);
                scale -= .1f;
            }
            textPos = (PHONEWIDTH - textPos) / 2;
            int textHeight = (int)(gameBoard.getBoardPosX() * scale);
            textHeight = (gameBoard.getBoardPosY() - textHeight)/2;
            canvas.drawText(message, textPos, gameBoard.getBoardPosY() -textHeight, mPaint);
        }
    }
    private void drawTemp(Canvas canvas){
        int tempX = gameBoard.getBoardPosX() + gameBoard.getBorder();
        int tempY = gameBoard.getBoardPosY() + gameBoard.getBorder();
        tempX += pieceSize * temp.getXPos();
        tempY += pieceSize * temp.getYPos();
        Rect src = new Rect(0,0,temp.getWidth()-1,temp.getHeight()-1);
        int size = (int)gameBoard.getPieceSize();
        Rect dest = new Rect(tempX,tempY,tempX+size, tempY+size);
        canvas.drawBitmap(temp.getSprite(), src, dest, null);
    }
    private void drawHub(Canvas canvas){
        //HUD[4].onDraw(canvas, 30, 30);
        int x = gameBoard.getBoardPosX();
        int playerIndex = (player1 || players.length == 1)? 0: 1;
        int pieces[] = players[playerIndex].getMoves();;
        int movesLeft = players[playerIndex].movesLeft();;
        int pSize = HUD[0].getSize();
        //int bias = sizeX - (pSize * movesLeft);//((SIZE-movesLeft) * pSize)/(movesLeft + 1);
        int bias = (gameBoard.getBoardSizeX()-(movesLeft * pSize))/(movesLeft + 1);
        int y = 2 * gameBoard.getBoardPosY() + gameBoard.getBoardSizeX();
        x += bias;
        int count = 0;
        for (int i = 0; i< 4; i++){
            if (pieces[i] == 1) {
                HUD[i].onDraw(canvas, x + (count * (pSize + bias)), y);
                count++;
            }
        }
        HUD[5].onDraw(canvas, (PHONEWIDTH - HUD[5].getSize())/2,y + (int) (1.5 * pSize));
    }
}
