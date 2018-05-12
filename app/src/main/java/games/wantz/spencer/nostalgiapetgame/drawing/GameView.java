package games.wantz.spencer.nostalgiapetgame.drawing;

import android.app.Activity;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.BitmapFactory;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import games.wantz.spencer.nostalgiapetgame.GameActivity;
import games.wantz.spencer.nostalgiapetgame.GameThread;
import games.wantz.spencer.nostalgiapetgame.Monster;
import games.wantz.spencer.nostalgiapetgame.R;

/**
 * A view used for handling all game updates and rendering.
 *
 * @author Keegan Wantz wantzkt@uw.edu
 *
 * @version 0.1, 11 May 2018
 */
public class GameView extends SurfaceView {
    /**
     * The sprite sheet for pets and icons.
     */
    private final SpriteSheet mUnits;
    /**
     * The sprite sheet for background images.
     */
    private final SpriteSheet mBackground;
    /**
     * Our game thread.
     */
    private final GameThread mGameThread;
    /** The player's monster. */
    private Monster mMonster;
    /** The value to scale our sprite sheets by to fit the device screen. */
    private final float mScalar;
    /** The device's width and height. */
    private final int mDeviceWidth, mDeviceHeight;
    /** What frame to use on the pet sprite sheet. */
    private int mMonsterFrame;
    /** A frame counter used for changing the pet sprite sheet. */
    private int mCounter;

    /**
     * Creates the GameView, calculates scaling, the device's dimensions, prepares the sprite sheets,
     *  and creates callback methods for the SurfaceHolder for asynchronous updates.
     * @param context The context in which this View is made.
     */
    public GameView(Context context) {
        super(context, null);

        // This is a bit convoluted, but it retrieves the device width/height.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mDeviceWidth = displayMetrics.widthPixels;
        mDeviceHeight = displayMetrics.heightPixels;

        // Target is 16 by 9, and our base resolution is 160 x 90, so our scalar should be
        // the actual width divided by 160, we're less concerned about height.
        mScalar = mDeviceWidth / 160.0f;

        // Temporary, set it to the small blobby one.
        mMonsterFrame = 29;

        // Make our spritesheets.
        mBackground = new SpriteSheet(BitmapFactory.decodeResource(getResources(), R.drawable.main_background), 160, 90, mScalar);
        mUnits = new SpriteSheet(BitmapFactory.decodeResource(getResources(), R.drawable.pets_and_icons), 32, 32, mScalar);

        // Makes our thread.
        mGameThread = new GameThread(this);

        // Retrieve the SurfaceHolder, and add its needed callback methods.
        SurfaceHolder mHolder = getHolder();
        mHolder.addCallback(new SurfaceHolder.Callback() {
            /** When we create a surface, we want the thread to run. */
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mGameThread.setActive(true);
                mGameThread.start();
            }

            /** When the surface is destroyed, we want to STOP the thread via join. */
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mGameThread.setActive(false);
                boolean retry = true;
                // Attempts to stop the thread, can be interrupted.
                while (retry) {
                    try {
                        // Stops the thread.
                        mGameThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }

            /** Unused. */
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
        });
    }

    /** Necessary constructor, calls the other constructor. */
    public GameView(Context context, AttributeSet set) {
        this(context);
    }

    /**
     * Updates the monster, allowing it to walk back and forth and in the future become hungry.
     */
    public void Update() {
        if (mMonster != null) {
            mMonster.Update();

            mCounter++;
            mMonsterFrame = 37;
            if (mCounter > 30) {
                mMonsterFrame = 29;
                if (mCounter > 45)
                    mCounter = 0;
            }

        } else {
            Log.d("GameView", "Trying to get monster from parent.");
            GameActivity parentActivity = (GameActivity) getContext();
            mMonster = parentActivity.getMonster();
            if (mMonster == null) {
                Log.d("GameView", "Monster was null?");
            }
        }
    }

    /**
     * Draws the current game state to the canvas.
     *
     * @param canvas The canvas to draw to.
     */
    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Draw the background and the monster.
        mBackground.Draw(canvas, 0, 0, 0);

        // And draw the monster in (roughly) the center of the screen, biased towards the top a bit.
        // Offset so we can draw from the middle, as we are using positioning relative to the center of the game.
        int offset = (int)(16 * mScalar);

        if (mMonster != null) {
            // Draws the monster, offset from the middle of the screen.
            mUnits.Draw(canvas, mDeviceWidth / 2 + mMonster.getX() - offset, mDeviceHeight / 3 + mMonster.getY() - offset, mMonsterFrame);
        }
    }
}
