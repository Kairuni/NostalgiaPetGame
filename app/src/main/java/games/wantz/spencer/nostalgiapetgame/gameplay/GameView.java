package games.wantz.spencer.nostalgiapetgame.gameplay;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PointF;
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
import java.util.Random;
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
 * @version 1.B, 31 May 2018
 */
public class GameView extends SurfaceView {
    //Final field Variables
    /** Tag for logging. */
    private static final String GAME_VIEW_LOG = "GAME_VIEW";
    /** The URL to use for updating the monster. */
    private static final String MONSTER_UPDATE_URL =
            "http://www.kairuni.com/NostalgiaPet/updateMonster.php?";
    /** How much gravity the ball should have while playing the ball game. */
    private static final float BALL_GRAVITY = 1000f;

    //Non-Final field Variables
    /** AtomicInteger value used to control what the next scene should be. */
    private AtomicInteger mNextScene;
    /** AtomicBoolean value used to determine when assets have finished loading. */
    private AtomicBoolean mAssetsDone;
    /** Should I draw my status bars? */
    private AtomicBoolean mDrawBars;

    /** The thread that handles all game logic. */
    private GameThread mGameThread;
    /** The player's monster. */
    private Monster mMonster;
    /** The SQLite database to save monsters in. */
    private MonsterDB mMonsterDB;
    /** The sprite sheet for pets and icons. Loaded asynchronously. */
    private SpriteSheet mUnits;
    /** The sprite sheet for background images. Loaded asynchronously. */
    private SpriteSheet mBackground;
    /** The sprite sheet for stationary fixtures. */
    private SpriteSheet mFixtures;
    /** List of animations to use for gameplay. */
    private List<Animation> mAnimations;
    /** List of animations for fixtures. */
    private List<Animation> mFixtureAnimations;
    /** The ball game's point. */
    private PointF mBallPoint;
    /** The ball's velocity for the ball game. */
    private PointF mBallVelocity;
    /** Random number generator for various uses. */
    private Random mRandom;

    /** Scenes */
    private List<AnimationScene> mSceneList;
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
        mAssetsDone = new AtomicBoolean(false);
        mDrawBars = new AtomicBoolean(false);
        mAnimations = null;
        mFixtureAnimations = null;

        mBallPoint = null;

        mRandom = new Random();

        mNextScene = new AtomicInteger();
        mNextScene.set(-1);
        mCurScene = -1;

        AssetLoader assetLoader;
        Log.d(GAME_VIEW_LOG, "Creating asset loader.");
        assetLoader = new AssetLoader();
        assetLoader.execute();
        Log.d(GAME_VIEW_LOG, "Asset loader running.");
    }

    /**
     * Called when the activity pauses, updates the monster on the remote server and joins the thread.
     */
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
            mMonster.onPause();
            mMonsterDB = new MonsterDB(getContext().getApplicationContext());
            mMonsterDB.insertMonster(mMonster);
        }

        mGameThread = null;
    }

    /**
     * Restarts the thread and has the monster catch up with lost time.
     */
    public void gameViewResume() {
        if (mGameThread == null) {
            Log.d(GAME_VIEW_LOG, "Resumed, recreating thread.");
            mGameThread = new GameThread(this);
            mGameThread.setActive(true);
            mMonster.onResume();
        }
    }

    /**
     * Called when the user pushes the feed button, sets the next scene to the feed scene.
     */
    public void doFeed() {
        mNextScene.set(0);
    }

    /**
     * Called when the user pushes the shower button, sets the next scene to the shower scene.
     */
    public void doShower() {
        mNextScene.set(1);
    }

    /**
     * Called when the user pushes the toilet button, sets the next scene to the toilet scene.
     */
    public void doToilet() {
        mNextScene.set(2);
    }

    /**
     * Creates a ball for the ball game.
     */
    public void doGame() {
        int cX = mDeviceWidth / 2;
        int cY = mDeviceHeight / 2;

        mBallPoint = new PointF(cX, cY);
        mBallVelocity = null;
    }

    /**
     * Toggles the drawing of the pet statistics.
     */
    public void toggleStats() {
        mDrawBars.set(!mDrawBars.get());
    }

    /**
     * Handles the ball game.
     */
    private void handleBallGame() {
        int cX = mDeviceWidth / 2;
        int cY = mDeviceHeight / 2 ;

        if (mBallVelocity == null) {
            mBallVelocity = new PointF(-200 + mRandom.nextInt(400), -1800);
        } else if (mBallPoint.y < cY) {
            if (mBallVelocity.y > 1000) {
                mMonster.setFun(mMonster.getFun() + 7);
                mBallVelocity.x = -2000 + mRandom.nextInt(4000);
                mBallVelocity.y = -1800 + mRandom.nextInt(200);
            }
        } else {
            mMonster.setFun(mMonster.getFun() - 5);
            mBallVelocity.x = -200 + mRandom.nextInt(400);
            mBallVelocity.y = 3000 + mRandom.nextInt(300);
        }
    }

    /**
     * Updates the ball game, effectively applying gravity to the ball.
     * @param tickMillis The number of milliseconds that have elapsed.
     */
    private void updateBallGame(Long tickMillis) {
        if (mBallPoint != null && mBallVelocity != null) {
            mBallPoint.x += mBallVelocity.x * tickMillis / 1000f;
            mBallPoint.y += mBallVelocity.y * tickMillis / 1000f;

            mBallVelocity.y += BALL_GRAVITY * tickMillis / 1000f;

            if ((mBallPoint.x < 0 && mBallVelocity.x < 0) || (mBallPoint.x > mDeviceWidth && mBallVelocity.x > 0)) {
                mBallVelocity.x = -mBallVelocity.x;
            }
            if (mBallPoint.y > mDeviceHeight * 1.5) {
                mBallPoint = null;
                mBallVelocity = null;
            } else if (mBallPoint.y < -30 && mBallVelocity.y < 0) {
                mBallVelocity.y = -mBallVelocity.y;
            }
        }
    }

    /**
     * When the user clicks the game view itself, do this. This hatches unhatched mons, or bumps the ball into the air.
     */
    public void doClick() {
        if (mMonster != null && !mMonster.getHatched()) {
            Log.d(GAME_VIEW_LOG, "Hatching!");
            mMonster.doHatched();
        } else if (mMonster != null && mBallPoint != null) {
            handleBallGame();
        }
    }

    /**
     * Updates the monster and any running scene.
     */
    public void update(Long tickMillis) {
        if (mMonster != null && mAssetsDone.get()) {
            mMonster.update(tickMillis);

            // If their monster is dead, well, kill it by making a new monster.
            if (mMonster.getHealth() == 0) {
                mMonster = new Monster(mMonster.getUID(),
                        mRandom.nextInt(3) * 3, false,
                        80 + mRandom.nextInt(40), 10,
                        80 + mRandom.nextInt(40), 10,
                        80 + mRandom.nextInt(40), 10,
                        80 + mRandom.nextInt(40), 10,
                        100, 100, System.currentTimeMillis()
                );
                // Rebuild our animations
                buildAnimations(mUnits, mFixtures, mDeviceWidth, mDeviceHeight);
                // Reset the current scene to -1.
                mCurScene = -1;
            }

            mAnimations.get(SceneBuilder.IDLE_IDX).update(tickMillis);
            mAnimations.get(SceneBuilder.POOP_IDX).update(tickMillis);

            if (mCurScene == -1) {
                mCurScene = mNextScene.getAndSet(-1);
                if (mCurScene != -1) {
                    mSceneList.get(mCurScene).reset();
                }
            } else if (mMonster.getHatched()) {
                mSceneList.get(mCurScene).update(16);
            }

            updateBallGame(tickMillis);
        }
    }

    /**
     * Called when a scene is completed, to change the monster's statistics.
     *
     * @param sceneID The scene that finished.
     */
    private void completedScene(int sceneID) {
        if (sceneID == 0) {
            mMonster.doFeed();
        } else if (sceneID == 1) {
            mMonster.doShower();
        } else if (sceneID == 2) {
            mMonster.doToilet();
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

            if (mMonster != null) {
                if (!mMonster.getHatched()) {
                    // Log.d(GAME_VIEW_LOG, "Monster not hatched yet?");
                    mUnits.draw(canvas, cX, cY, mMonster.getBreed() * 16 + 4);
                } else {

                    if (mCurScene != -1 && mSceneList.size() > mCurScene) {
                        mSceneList.get(mCurScene).draw(canvas);
                        if (mSceneList.get(mCurScene).getIsComplete()) {
                            mSceneList.get(mCurScene).reset();
                            completedScene(mCurScene);
                            mCurScene = -1;
                        }
                    } else if (mCurScene == -1) {
                        // Draws the monster, offset from the middle of the screen.
                        mAnimations.get(0).draw(canvas, cX + mMonster.getX(), cY + mMonster.getY());
                        // Draw all the poops
                        for (Monster.Poop p : mMonster.getPoops()) {
                            mAnimations.get(3).draw(canvas, cX + p.x, cY + p.y, p.scale);
                        }

                        // Draw the bubbles:
                        if (mMonster.getHungerPercent() < 30) {
                            mUnits.draw(canvas, cX + mMonster.getX() + offset, cY + mMonster.getY() - offset, 6);
                        } else if (mMonster.getBladderPercent() < 30) {
                            mUnits.draw(canvas, cX + mMonster.getX() + offset, cY + mMonster.getY() - offset, 22);
                        } else if (mMonster.getDirty() < 30) {
                            mUnits.draw(canvas, cX + mMonster.getX() + offset, cY + mMonster.getY() - offset, 38);
                        } else if (mMonster.getFun() < 30) {
                            mUnits.draw(canvas, cX + mMonster.getX() + offset, cY + mMonster.getY() - offset, 54);
                        }
                    }

                    if (mBallPoint != null) {
                        mUnits.draw(canvas, (int) mBallPoint.x, (int) mBallPoint.y, 5);
                    }
                }
                if (mDrawBars.get()) {
                    drawStatBars(canvas);
                }
            }
        }
    }

    /**
     * Draws the stat bars to the screen.
     *
     * @param canvas The canvas to draw to.
     */
    public void drawStatBars(Canvas canvas) {
        // So, size: left + 100,
        float barLeft = 200;
        float barWidth = mDeviceWidth / 1.5f;
        float barTop = mDeviceHeight / 6;
        float barHeight = mDeviceHeight / 16;
        float barOffset = barHeight + barHeight / 4;

        Paint bgPaint = new Paint();
        Paint barPaint = new Paint();
        bgPaint.setARGB(255, 0, 0, 0);
        bgPaint.setTextSize(barHeight);
        barPaint.setARGB(255, 0, 255, 0);

        canvas.drawRect(barLeft, barTop, barLeft + barWidth, barTop + barHeight, bgPaint);
        canvas.drawText("Health", barLeft + barWidth, barTop + barHeight, bgPaint);
        canvas.drawRect(barLeft, barTop, barLeft + barWidth * mMonster.getHealthPercent() / 100, barTop + barHeight, barPaint);

        canvas.drawRect(barLeft, barTop + barOffset, barLeft + barWidth, barTop + barHeight + barOffset, bgPaint);
        canvas.drawText("Stamina", barLeft + barWidth, barTop + barHeight + barOffset, bgPaint);
        canvas.drawRect(barLeft, barTop + barOffset, barLeft + barWidth * mMonster.getStaminaPercent() / 100, barTop + barHeight + barOffset, barPaint);

        canvas.drawRect(barLeft, barTop + barOffset * 2, barLeft + barWidth, barTop + barHeight + barOffset * 2, bgPaint);
        canvas.drawText("Stomach", barLeft + barWidth, barTop + barHeight + barOffset * 2, bgPaint);
        canvas.drawRect(barLeft, barTop + barOffset * 2, barLeft + barWidth * mMonster.getHungerPercent() / 100, barTop + barHeight + barOffset * 2, barPaint);

        canvas.drawRect(barLeft, barTop + barOffset * 3, barLeft + barWidth, barTop + barHeight + barOffset * 3, bgPaint);
        canvas.drawText("Bladder", barLeft + barWidth, barTop + barHeight + barOffset * 3, bgPaint);
        canvas.drawRect(barLeft, barTop + barOffset * 3, barLeft + barWidth * mMonster.getBladderPercent() / 100, barTop + barHeight + barOffset * 3, barPaint);

        canvas.drawRect(barLeft, barTop + barOffset * 4, barLeft + barWidth, barTop + barHeight + barOffset * 4, bgPaint);
        canvas.drawText("Fun", barLeft + barWidth, barTop + barHeight + barOffset * 4, bgPaint);
        canvas.drawRect(barLeft, barTop + barOffset * 4, barLeft + barWidth * mMonster.getFun() / 100, barTop + barHeight + barOffset * 4, barPaint);

        canvas.drawRect(barLeft, barTop + barOffset * 5, barLeft + barWidth, barTop + barHeight + barOffset * 5, bgPaint);
        canvas.drawText("Clean", barLeft + barWidth, barTop + barHeight + barOffset * 5, bgPaint);
        canvas.drawRect(barLeft, barTop + barOffset * 5, barLeft + barWidth * mMonster.getDirty() / 100, barTop + barHeight + barOffset * 5, barPaint);
    }

    /**
     * Builds the monster animations and scenes for the current monster.
     *
     * @param monsterSheet The sprite sheet to use.
     * @param fixturesSheet The fixtures to use.
     * @param phoneWidth The phone's width.
     * @param phoneHeight The phone's height.
     */
    public void buildAnimations(SpriteSheet monsterSheet, SpriteSheet fixturesSheet, int phoneWidth, int phoneHeight) {
        mAnimations = SceneBuilder.buildMonsterAnimations(monsterSheet, mMonster.getBreed());
        mFixtureAnimations = SceneBuilder.buildFixtureAnimations(fixturesSheet);

        /* Next, build the scenes. */
        mSceneList.add(SceneBuilder.buildFeedScene(mAnimations.get(SceneBuilder.FEED_IDX),
                mAnimations.get(SceneBuilder.MEAT_IDX), phoneWidth, phoneHeight));
        mSceneList.add(SceneBuilder.buildShowerScene(mAnimations.get(SceneBuilder.IDLE_IDX),
                mFixtureAnimations.get(SceneBuilder.TUB_IDX), phoneWidth, phoneHeight));
        mSceneList.add(SceneBuilder.buildToiletScene(mAnimations.get(SceneBuilder.IDLE_IDX),
                mFixtureAnimations.get(SceneBuilder.TOILET_IDX), phoneWidth, phoneHeight));
    }

    /**
     * Builds a URL for updating the monster remotely.
     *
     * @return The URL to access.
     */
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

    /**
     * Loads all assets needed for the game in the background.
     */
    private class AssetLoader extends AsyncTask<Void, Void, Void> {

        /** The bitmaps that we've loaded. */
        private List<Bitmap> mLoadedBmps;

        /**
         * Loads all bitmaps, then builds all animations.
         *
         * @param params Unused.
         * @return Unused.
         */
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

        /**
         * Sets the mAssetsDone AtomicBoolean to true.
         *
         * @param aVoid Unused.
         */
        @Override
        protected void onPostExecute(Void aVoid) {
            Log.d(GAME_VIEW_LOG, "Asset loader done");
            mAssetsDone.set(true);
        }
    }
}