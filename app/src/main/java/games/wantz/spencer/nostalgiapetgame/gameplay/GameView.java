package games.wantz.spencer.nostalgiapetgame.gameplay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import games.wantz.spencer.nostalgiapetgame.gameplay.GameThread;
import games.wantz.spencer.nostalgiapetgame.gameplay.actors.Monster;
import games.wantz.spencer.nostalgiapetgame.R;
import games.wantz.spencer.nostalgiapetgame.gameplay.drawing.SpriteSheet;

/**
 * A view used for handling all game updates and rendering.
 *
 * @author Keegan Wantz wantzkt@uw.edu
 *
 * @version 0.1, 11 May 2018
 */
public class GameView extends SurfaceView {
    private static final String GAME_VIEW_LOG = "GAME_VIEW";

    /**
     * The sprite sheet for pets and icons. Loaded asynchronously.
     */
    private SpriteSheet mUnits;
    /** The sprite sheet for background images. Loaded asynchronously. */
    private SpriteSheet mBackground;
    private SpriteSheet mFixtures;
    /** The thread that handles all game logic. */
    private GameThread mGameThread;

    /** The player's monster. */
    private Monster mMonster;
    /** The value to scale our sprite sheets by to fit the device screen. */
    private float mScalar;
    /** The device's width and height. */
    private int mDeviceWidth, mDeviceHeight;
    /** What frame to use on the pet sprite sheet. */
    private int mMonsterFrame;
    /** A frame counter used for changing the pet sprite sheet. */
    private int mCounter;

    /* An async task used to load assets. */
    private AssetLoader mAssetLoader;

    /** Necessary constructor, calls the other constructor. */
    public GameView(Context context) {
        this(context, null);
    }

    /**
     * Creates the GameView, calculates scaling, the device's dimensions, prepares the sprite sheets,
     *  and creates callback methods for the SurfaceHolder for asynchronous updates.
     * @param context The context in which this View is made.
     */
    public GameView(Context context, AttributeSet set) {
        super(context, set);

        Log.d(GAME_VIEW_LOG, "Created game view.");

        // This is a bit convoluted, but it retrieves the device width/height.
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        mDeviceWidth = displayMetrics.widthPixels;
        mDeviceHeight = displayMetrics.heightPixels;

        // Target is 16 by 9, and our base resolution is 160 x 90, so our scalar should be
        // the actual width divided by 160, we're less concerned about height.
        mScalar = mDeviceWidth / 160.0f;

        // Temporary, set it to the small blobby one.
        mMonsterFrame = 0;

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

        Log.d(GAME_VIEW_LOG, "Creating asset loader.");
        mAssetLoader = new AssetLoader();
        mAssetLoader.execute();
        Log.d(GAME_VIEW_LOG, "Asset loader running.");
    }



    public void gameViewPause() {
        Log.d(GAME_VIEW_LOG, "Pausing and killing thread.");
        mGameThread.setActive(false);
        mGameThread = null;
    }

    public void gameViewResume() {
        if (mGameThread == null) {
            Log.d(GAME_VIEW_LOG, "Resumed, recreating thread.");
            mGameThread = new GameThread(this);
            mGameThread.setActive(true);
        }
    }

    public void setMonster(Monster monster) {
        mMonster = monster;
    }

    /**
     * Updates the monster, allowing it to walk back and forth and in the future become hungry.
     */
    public void Update() {
        if (mMonster != null) {
            mMonster.Update(32);

            mCounter++;
            mMonsterFrame = mMonster.getBreed() * 16;
            if (mCounter > 30) {
                mMonsterFrame = mMonster.getBreed() * 16 + 1;
                if (mCounter > 45)
                    mCounter = 0;
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
        if (mAssetLoader.mDone.get()) {

            // Draw the background and the monster.
            mBackground.Draw(canvas, 0, 0, 0);

            // And draw the monster in (roughly) the center of the screen, biased towards the top a bit.
            // Offset so we can draw from the middle, as we are using positioning relative to the center of the game.
            int offset = (int) (16 * mScalar);

            if (mMonster != null) {
                // Draws the monster, offset from the middle of the screen.
                mUnits.Draw(canvas, mDeviceWidth / 2 + mMonster.getX() - offset, mDeviceHeight / 3 + mMonster.getY() - offset, mMonsterFrame);

                // SOON:
                mMonster.Draw(canvas);
            }
        }
    }

    private class AssetLoader extends AsyncTask<Void, Void, Void> {
        List<Bitmap> mLoadedBmps;
        final AtomicBoolean mDone;

        public AssetLoader() {
            mDone = new AtomicBoolean(false);
        }

        @Override
        protected Void doInBackground(Void... params) {

            mLoadedBmps = new ArrayList<Bitmap>();
            mLoadedBmps.add(BitmapFactory.decodeResource(getResources(), R.drawable.main_background));
            mLoadedBmps.add(BitmapFactory.decodeResource(getResources(), R.drawable.pets_and_icons));
            mLoadedBmps.add(BitmapFactory.decodeResource(getResources(), R.drawable.fixtures));


            Log.d(GAME_VIEW_LOG, "Returning null!");
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(GAME_VIEW_LOG, "Asset loader on post execute.");
            mBackground = new SpriteSheet(mLoadedBmps.get(0), 160, 90, mScalar);
            mUnits = new SpriteSheet(mLoadedBmps.get(1), 32, 32, mScalar);
            mFixtures = new SpriteSheet(mLoadedBmps.get(2), 64, 64, mScalar);
            mDone.set(true);
            Log.d(GAME_VIEW_LOG, "mDone set to " + mDone.toString());
        }
    }
}