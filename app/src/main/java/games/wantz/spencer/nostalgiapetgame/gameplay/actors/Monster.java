package games.wantz.spencer.nostalgiapetgame.gameplay.actors;

import android.graphics.Canvas;
import android.graphics.Point;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import games.wantz.spencer.nostalgiapetgame.gameplay.drawing.Animation;
import games.wantz.spencer.nostalgiapetgame.gameplay.drawing.AnimationScene;
import games.wantz.spencer.nostalgiapetgame.gameplay.drawing.AnimationSceneBuilder;
import games.wantz.spencer.nostalgiapetgame.gameplay.drawing.SpriteSheet;

/**
 * A class that represents the player's pet monster.
 *
 * @author Keegan Wantz wantzkt@uw.edu
 * @version 0.1, 11 May 2018
 */
public class Monster implements Serializable {
    private static final long serialVersionUID = 0xe05c26dfe03e72b9L;

    // Change direction every 4 seconds.
    /**
     * How often to change wander state (in frames).
     */
    private final int WANDER_RESET = 120;
    /**
     * Statistic change per millisecond.
     */
    private final float CHANGE_PER_MILLI = 1 / 1000;

    /**
     * The Unique ID for this monster.
     */
    private String mUID;
    /**
     * The breed of this monster.
     */
    private int mBreed;
    /**
     * Health statistics for this monster.
     */
    private float mMaxHealth, mHealth;
    /**
     * Stamina statistics for this monster.
     */
    private float mMaxStamina, mStamina;
    /**
     * Hunger statistics for this monster.
     */
    private float mMaxHunger, mHunger;
    /**
     * Bladder statistics for this monster.
     */
    private float mMaxBladder, mBladder;
    /**
     * Fun statistics for this monster.
     */
    private float mMaxFun, mFun;
    /**
     * Dirty statistics for this monster.
     */
    private float mMaxDirty, mDirty;

    /**
     * The current X and Y coordinates for this montster.
     */
    private int mX, mY;

    /**
     * Get the X coordinate.
     */
    public int getX() {
        return mX;
    }

    /**
     * Get the Y coordinate.
     */
    public int getY() {
        return mY;
    }

    Animation mIdleAnimation;
    List<AnimationScene> mSceneList;
    int mActiveScene;

    public int getBreed() {
        return mBreed;
    }

    /**
     * What direction is the pet currently wandering? 0 right, 1 left, other values are standing still.
     */
    private int mWanderFlag;
    /**
     * The current timer for wandering, in frames.
     */
    private int mWanderTimer;

    /**
     * Random for random movement.
     */
    private Random mRandom;

    /**
     * The epoch time we paused the monster at.
     */
    private long pauseTime;

    /**
     * Construct a monster with the given parameters.
     *
     * @param UID        Unique ID.
     * @param Breed      Monster's breed.
     * @param maxHealth  Max health.
     * @param Health     Current health.
     * @param maxStamina Max stamina.
     * @param Stamina    Current stamina.
     * @param maxHunger  Max hunger.
     * @param Hunger     Current hunger.
     * @param maxBladder Max bladder.
     * @param Bladder    Current bladder.
     * @param Fun        Current 'fun' value - how entertained the creature is.
     * @param Dirty      Current 'dirty' value - how much the creature needs a bath.
     */
    public Monster(String UID, int Breed, float maxHealth, float Health, float maxStamina,
                   float Stamina, float maxHunger, float Hunger, float maxBladder, float Bladder,
                   float Fun, float Dirty) {
        mUID = UID;
        mBreed = Breed;
        mMaxHealth = maxHealth;
        mHealth = Health;
        mMaxStamina = maxStamina;
        mStamina = Stamina;
        mMaxHunger = maxHunger;
        mHunger = Hunger;
        mMaxBladder = maxBladder;
        mBladder = Bladder;
        mMaxFun = 100.0f;
        mFun = Fun;
        mMaxDirty = 100.0f;
        mDirty = Dirty;

        mX = 0;
        mY = 0;

        mRandom = new Random();

        mWanderFlag = mRandom.nextInt(3);
        mWanderTimer = 0;

        mIdleAnimation = null;
        mSceneList = new ArrayList<AnimationScene>();
    }

    /**
     * Updates the current state of the monster.
     * Currently only makes the monster move. In the future updates will include
     * changes to hunger, stamina, bladder, and health.
     */
    public void Update(long tickMillis) {
        // Currently ticks at a FIXED RATE
        if (mWanderFlag == 0) {
            mX += 2;
        } else if (mWanderFlag == 1) {
            mX -= 2;
        }
        mWanderTimer++;

        if (mWanderTimer > WANDER_RESET) {
            mWanderFlag = mRandom.nextInt(3);
            mWanderTimer = 0;
        }

        // If we're too far left or right, walk back towards the center of the screen.
        if (mX < -500) {
            mWanderFlag = 0;
        } else if (mX > 500) {
            mWanderFlag = 1;
        }

        for (AnimationScene scene : mSceneList) {
            scene.Update(tickMillis);
        }

    }

    public void buildAnimations(SpriteSheet monsterSheet, SpriteSheet fixturesSheet, int phoneWidth, int phoneHeight) {
        List<Animation> animations = AnimationSceneBuilder.buildMonsterAnimations(monsterSheet, mBreed);
        List<Animation> fixtureAnimations = AnimationSceneBuilder.buildFixtureAnimations(fixturesSheet);

        /* Next, build the scenes. */
        mSceneList.add(AnimationSceneBuilder.buildFeedScene(animations.get(1), animations.get(2), phoneWidth, phoneHeight));
        // TODO: REMOVE THIS AFTER TESTING THAT FEED SCENE PLAYS
        mActiveScene = 0;

    }

    public void Draw(Canvas canvas) {
        if (mActiveScene != -1 && mSceneList.size() > mActiveScene) {
            mSceneList.get(mActiveScene).Draw(canvas);
        } else if (mIdleAnimation != null) {
            mIdleAnimation.Draw(canvas, mX, mY);
        }
    }

    public void onPause() {
        pauseTime = System.currentTimeMillis();
    }

    public void onResume() {
        long unpauseTime = System.currentTimeMillis();
        Update(unpauseTime - pauseTime);
        pauseTime = unpauseTime;
    }


}
