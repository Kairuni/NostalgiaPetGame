package games.wantz.spencer.nostalgiapetgame.gameplay;

import android.graphics.Canvas;

/**
 * A thread for running the game. I attempted to use an Async Task for the same purpose, but
 * I could not get it to work at all.
 * This thread controls when the GameView updates, and calls entity update functions.
 *
 * @author Keegan Wantz wantzkt@uw.edu
 *
 * @version 0.1, 11 May 2018
 */
public class GameThread extends Thread {
    /** The number of animation/logic frames to do every second. */
    private static final int FPS = 60;
    /**
     * The GameView that we're tied to.
     */
    private final GameView mGameView;
    /**
     * Is this thread active?
     */
    private boolean mActive;

    /**
     * Sets if the thread is active or not.
     */
    public void setActive(boolean active) {
        mActive = active;
    }

    /**
     * Constructs the thread with the provided GameView.
     */
    public GameThread(GameView gameView) {
        mGameView = gameView;
        mActive = false;
    }

    /** Runs the thread, looping until it is no longer active. */
    @Override
    public void run() {
        while (mActive) {
            // Have to declare this out here
            Canvas canvas = null;
            try {
                // Because this can fail, if the canvas has already been freed.
                canvas = mGameView.getHolder().lockCanvas();
                // Synchronized so that we can use multithreading without errors.
                synchronized (mGameView.getHolder()) {
                    // Draw to the canvas.
                    mGameView.draw(canvas);
                }
            } finally {
                // Free up the canvas if it's not nulled out.
                if (canvas != null) {
                    mGameView.getHolder().unlockCanvasAndPost(canvas);
                }
            }
            // Have the GameView update its contents, including the monster..
            // TODO: Do the update logic here, instead of in the game view?
            mGameView.update();
            try {
                // Super naive sleep, as the previous actions took some time, but that's fine for now.
                //sleep(1000 / FPS);
                sleep(1);
            } catch (InterruptedException e) {

            }
        }

    }
}