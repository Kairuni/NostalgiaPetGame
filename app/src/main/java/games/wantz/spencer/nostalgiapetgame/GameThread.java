package games.wantz.spencer.nostalgiapetgame;

import android.graphics.Canvas;

import games.wantz.spencer.nostalgiapetgame.drawing.GameView;

/**
 * A thread for running the game. I attempted to use an Async Task for the same purpose, but
 * I could not get it to work at all.
 * This thread controls all game logic, and updating the GameView's canvas.
 */
public class GameThread extends Thread {
    /** The number of animation/logic frames to do every second. */
    static final int FPS = 30;
    private GameView mGameView;
    private boolean mActive;

    public void setActive(boolean active) {mActive = active;}

    public GameThread(GameView gameView) {
        mGameView = gameView;
        mActive = false;
    }


    @Override
    public void run() {
        while (mActive) {
            Canvas canvas = null;
            try {
                canvas = mGameView.getHolder().lockCanvas();
                synchronized (mGameView.getHolder()) {
                    mGameView.draw(canvas);
                }
            } finally {
                if (canvas != null) {
                    mGameView.getHolder().unlockCanvasAndPost(canvas);
                }
            }
            // Calculate the next frame:
            mGameView.Update();
            try {
                // Super naive sleep, as the previous actions took some time, but that's fine for now.
                sleep(1000 / FPS);
            } catch (InterruptedException e) {}
        }

    }
}