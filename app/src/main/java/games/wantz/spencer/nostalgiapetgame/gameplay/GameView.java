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
import android.widget.Toast;

import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

import games.wantz.spencer.nostalgiapetgame.gameplay.actors.Monster;
import games.wantz.spencer.nostalgiapetgame.R;
import games.wantz.spencer.nostalgiapetgame.gameplay.data.MonsterDB;
import games.wantz.spencer.nostalgiapetgame.gameplay.drawing.Animation;
import games.wantz.spencer.nostalgiapetgame.gameplay.drawing.AnimationScene;
import games.wantz.spencer.nostalgiapetgame.gameplay.drawing.SceneBuilder;
import games.wantz.spencer.nostalgiapetgame.gameplay.drawing.SpriteSheet;

/**
 * A view used for handling all game updates and rendering.
 *
 * @author Keegan Wantz wantzkt@uw.edu
 *
 * @version B.1, 28 May 2018
 */
public class GameView extends SurfaceView {

    //Final Field Variables
    /**  */
    private static final String GAME_VIEW_LOG = "GAME_VIEW";
    /**  */
    private final static String MONSTER_UPDATE_URL = "http://www.kairuni.com/NostalgiaPet/updateMonster.php?";

    //Non-Final Field Variables
    /** AtomicInteger value used to control what the next scene should be. */
    private AtomicInteger mNextScene;
    /** AtomicBoolean value used to determine when assets have finished loading. */
    private AtomicBoolean mAssetsDone;
    /** The thread that handles all game logic. */
    private GameThread mGameThread;
    /** The player's monster. */
    private Monster mMonster;
    /**  */
    private MonsterDB mMonsterDB;
    /** The sprite sheet for pets and icons. Loaded asynchronously. */
    private SpriteSheet mUnits;
    /** The sprite sheet for background images. Loaded asynchronously. */
    private SpriteSheet mBackground;
    private SpriteSheet mFixtures;
    /** List of pregenerated animations. */
    List<Animation> mAnimations;
    /** List of pregenerated animations for fixtures. */
    List<Animation> mFixtureAnimations;
    /** Scenes */
    List<AnimationScene> mSceneList;
    /** The currently playing scene. */
    private int mCurScene;
    /** The device's width and height. */
    private int mDeviceWidth, mDeviceHeight;
    /** The value to scale our sprite sheets by to fit the device screen. */
    private float mScalar;

    /**
     * Necessary constructor, calls the other constructor.
     *
     * @param context passes in which this View is made.
     */
    public GameView(Context context) {
        this(context, null);
    }

    /**
     * Creates the GameView, calculates scaling, the device's dimensions, prepares the sprite sheets,
     *  and creates callback methods for the SurfaceHolder for asynchronous updates.
     *
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

        // Makes our thread.
        mGameThread = new GameThread(this);

        // Retrieve the SurfaceHolder, and add its needed callback methods.
        SurfaceHolder mHolder = getHolder();
        mHolder.addCallback(new SurfaceHolder.Callback() {
            /** When we create a surface, we want the thread to run. */
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                if (mGameThread != null) {
                    mGameThread.setActive(true);
                    mGameThread.start();
                }
            }

            /** When the surface is destroyed, we want to STOP the thread via join. */
            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                if (mGameThread != null) {
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
            }

            /** Unused. */
            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
            }
        });
        mSceneList = new ArrayList<AnimationScene>();
        mAssetsDone = new AtomicBoolean();
        mAssetsDone.set(false);
        mAnimations = null;
        mFixtureAnimations = null;

        mNextScene = new AtomicInteger();
        mNextScene.set(-1);
        mCurScene = -1;

        AssetLoader assetLoader;
        Log.d(GAME_VIEW_LOG, "Creating asset loader.");
        assetLoader = new AssetLoader();
        assetLoader.execute();
        Log.d(GAME_VIEW_LOG, "Asset loader running.");
    }

    public void gameViewPause() {
        Log.d(GAME_VIEW_LOG, "Pausing and killing thread.");
        mGameThread.setActive(false);
        try {
            // Stops the thread.
            mGameThread.join();
        } catch (InterruptedException e) {

        }
        // Save the monster into our local db as well
        if (mMonsterDB == null) {
            mMonsterDB = new MonsterDB(getContext().getApplicationContext());
            mMonsterDB.insertMonster(mMonster);
        }
        mGameThread = null;
    }

    public void gameViewResume() {
        if (mGameThread == null) {
            Log.d(GAME_VIEW_LOG, "Resumed, recreating thread.");
            mGameThread = new GameThread(this);
            mGameThread.setActive(true);
        }
    }

    public void doFeed() {
        mNextScene.set(0);
    }

    public void doToilet() {
        mNextScene.set(2);
    }

    public void doShower() {
        mNextScene.set(1);
    }

    public void doHatch() {
        if (mMonster != null && !mMonster.getHatched()) {
            Log.d(GAME_VIEW_LOG, "Hatching!");
            mMonster.doHatched();
        }
    }

    /**
     * Updates the monster, allowing it to walk back and forth and in the future become hungry.
     */
    public void update() {
        if (mMonster != null) {
            mMonster.update(16);

            if (mCurScene == -1) {
                mCurScene = mNextScene.getAndSet(-1);
                if (mCurScene != -1) {
                    mSceneList.get(mCurScene).reset();
                }
            } else if (mMonster.getHatched()) {
                mSceneList.get(mCurScene).update(16);
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
        if (mAssetsDone.get()) {

            // Draw the background and the monster.
            mBackground.draw(canvas, (int) (80 * mScalar), (int) (45f * mScalar), 0);

            // And draw the monster in (roughly) the center of the screen, biased towards the top a bit.
            // Offset so we can draw from the middle, as we are using positioning relative to the center of the game.
            int offset = (int) (16 * mScalar);

            int cX = mDeviceWidth / 2;
            int cY = mDeviceHeight / 2 - offset;

            // TODO: POOPS
            if (mMonster != null) {
                // TODO: DRAW HUNGRY/ETC BUBBLES VIA MONSTER SPRITE SHEET
                if (!mMonster.getHatched()) {
                    Log.d(GAME_VIEW_LOG, "Monster not hatched yet?");
                    mUnits.draw(canvas, cX, cY, mMonster.getBreed() * 16 + 4);
                } else {

                    if (mCurScene != -1 && mSceneList.size() > mCurScene) {
                        mSceneList.get(mCurScene).draw(canvas);
                        if (mSceneList.get(mCurScene).getIsComplete()) {
                            // TODO: HANDLE SCENE COMPLETION

                            mSceneList.get(mCurScene).reset();
                            mCurScene = -1;
                        }
                    }
                    // Not an else if because we may have just stopped the scene above ^
                    // Draw the monster idling:
                    if (mCurScene == -1) {
                        // Draws the monster, offset from the middle of the screen.
                        mAnimations.get(0).draw(canvas, cX + mMonster.getX(), cY + mMonster.getY());
                        // Draw all the poops
                        for (Monster.Poop p : mMonster.getPoops()) {
                            mAnimations.get(3).draw(canvas, cX + p.x, cY + p.y, p.scale);
                        }
                    }
                }
            }
        }
    }

    public void buildAnimations(SpriteSheet monsterSheet, SpriteSheet fixturesSheet, int phoneWidth, int phoneHeight) {
        mAnimations = SceneBuilder.buildMonsterAnimations(monsterSheet, mMonster.getBreed());
        mFixtureAnimations = SceneBuilder.buildFixtureAnimations(fixturesSheet);

        /* Next, build the scenes. */
        mSceneList.add(SceneBuilder.buildFeedScene(mAnimations.get(1), mAnimations.get(2), phoneWidth, phoneHeight));
        mSceneList.add(SceneBuilder.buildShowerScene(mAnimations.get(0), mFixtureAnimations.get(0), phoneWidth, phoneHeight));
        // TODO: REMOVE THIS AFTER TESTING THAT FEED SCENE PLAYS
        mCurScene = -1;
    }

    private class AssetLoader extends AsyncTask<Void, Void, Void> {
        List<Bitmap> mLoadedBmps;

        @Override
        protected Void doInBackground(Void... params) {

            mLoadedBmps = new ArrayList<Bitmap>();
            mLoadedBmps.add(BitmapFactory.decodeResource(getResources(), R.drawable.main_background));
            mLoadedBmps.add(BitmapFactory.decodeResource(getResources(), R.drawable.pets_and_icons));
            mLoadedBmps.add(BitmapFactory.decodeResource(getResources(), R.drawable.fixtures));

            mBackground = new SpriteSheet(mLoadedBmps.get(0), 160, 90, mScalar);
            mUnits = new SpriteSheet(mLoadedBmps.get(1), 32, 32, mScalar);
            mFixtures = new SpriteSheet(mLoadedBmps.get(2), 64, 64, mScalar);

            Log.d(GAME_VIEW_LOG, "Waiting for monster");
            while (mMonster == null) {
                // Wait.
            }

            Log.d(GAME_VIEW_LOG, "Monster received.");
            buildAnimations(mUnits, mFixtures, mDeviceWidth, mDeviceHeight);

            return null;
        }
        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(GAME_VIEW_LOG, "Asset loader done");
            mAssetsDone.set(true);
        }
    }


    public String buildMonsterURL() {
        StringBuilder sb = new StringBuilder(MONSTER_UPDATE_URL.length() + 128);
        sb.append(MONSTER_UPDATE_URL);
        try {
            sb.append("uid=");
            sb.append(URLEncoder.encode(mMonster.getUID(), "UTF-8"));
            sb.append("&breed=");
            sb.append(URLEncoder.encode(Integer.toString(mMonster.getBreed()), "UTF-8"));
            sb.append("&hatched=");
            sb.append(URLEncoder.encode(Boolean.toString(mMonster.getHatched()), "UTF-8"));
            sb.append("&mhealth=");
            sb.append(URLEncoder.encode(Float.toString(mMonster.getMaxHealth()), "UTF-8"));
            sb.append("&health=");
            sb.append(URLEncoder.encode(Float.toString(mMonster.getHealth()), "UTF-8"));
            sb.append("&mstamina=");
            sb.append(URLEncoder.encode(Float.toString(mMonster.getMaxStamina()), "UTF-8"));
            sb.append("&stamina=");
            sb.append(URLEncoder.encode(Float.toString(mMonster.getStamina()), "UTF-8"));
            sb.append("&mhunger=");
            sb.append(URLEncoder.encode(Float.toString(mMonster.getMaxHunger()), "UTF-8"));
            sb.append("&hunger=");
            sb.append(URLEncoder.encode(Float.toString(mMonster.getHunger()), "UTF-8"));
            sb.append("&mbladder=");
            sb.append(URLEncoder.encode(Float.toString(mMonster.getMaxBladder()), "UTF-8"));
            sb.append("&bladder=");
            sb.append(URLEncoder.encode(Float.toString(mMonster.getBladder()), "UTF-8"));
            sb.append("&fun=");
            sb.append(URLEncoder.encode(Float.toString(mMonster.getFun()), "UTF-8"));
            sb.append("&dirty=");
            sb.append(URLEncoder.encode(Float.toString(mMonster.getDirty()), "UTF-8"));
            sb.append("&lastaccess=");
            sb.append(URLEncoder.encode(Long.toString(System.currentTimeMillis()), "UTF-8"));

        } catch (Exception e) {
            Toast.makeText(getContext(), "Something wrong with the url" + e.getMessage(), Toast.LENGTH_LONG)
                    .show();
        }
        return sb.toString();
    }

    public void setMonster(Monster monster) {
        mMonster = monster;
    }

}