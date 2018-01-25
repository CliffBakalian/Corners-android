package view;

import android.content.*;
import android.graphics.*;
import android.view.*;

import com.bakalian.clifford.corners.GameActivity;
import com.bakalian.clifford.corners.MainActivity;

import control.GameLoopThread;
import model.*;

/**
 * Created by clifford on 12/15/17.
 */

public class GameView extends SurfaceView{
    private Game game;
    private long lastClick;
    private GameLoopThread glt;
    private SurfaceHolder holder;
    public Context context;
    public GameActivity ga;
    public GameView(GameActivity ga, int x, int y, int size){
        super(ga);
        this.ga = ga;
        this.context = ga;
        this.glt = new GameLoopThread(this);
        this.game = new Game(this, x, y, MainActivity.num_players, size);
        this.game.start();
        holder = getHolder();
        holder.addCallback(new SurfaceHolder.Callback() {
            public void surfaceDestroyed(SurfaceHolder holder) {
                boolean retry = false;
                glt.setRunning(false);
                while(retry) {
                    try {
                        glt.join();
                        retry = false;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            public void surfaceCreated(SurfaceHolder holder) {
                glt.setRunning(true);
                glt.start();
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) { }

        });
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        canvas.drawColor(Color.WHITE);
        this.game.gameBoard.draw(canvas);
        this.game.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(System.currentTimeMillis() - lastClick > 500) {
            lastClick = System.currentTimeMillis();
            synchronized (getHolder()) {
                this.game.registerTouch(event);
            }
        }
        return true;
    }
}
