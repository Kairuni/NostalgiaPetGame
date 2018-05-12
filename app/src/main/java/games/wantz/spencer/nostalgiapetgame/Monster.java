package games.wantz.spencer.nostalgiapetgame;

import java.util.Random;

import games.wantz.spencer.nostalgiapetgame.drawing.SpriteSheet;

/**
 * A class that represents the player's pet monster.
 *
 * @author Keegan Wantz wantzkt@uw.edu
 * @version 0.1, 11 May 2018
 */
public class Monster {
    // Change direction every 4 seconds.
    /**
     * How often to change wander state (in frames).
     */
    private final int WANDER_RESET = 120;

    /**
     * The Unique ID for this monster.
     */
    private String mUID;
    /**
     * The breed of this monster.
     */
    private int mBreed;
    /** Health statistics for this monster, to be used later. */
    private float mMaxHealth, mHealth;
    /** Stamina statistics for this monster, to be used later. */
    private float mMaxStamina, mStamina;
    /** Hunger statistics for this monster, to be used later. */
    private float mMaxHunger, mHunger;
    /** Bladder statistics for this monster, to be used later. */
    private float mMaxBladder, mBladder;

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

    /** What direction is the pet currently wandering? 0 right, 1 left, other values are standing still. */
    private int mWanderFlag;
    /** The current timer for wandering, in frames. */
    private int mWanderTimer;

    /** Random for random movement. */
    private Random mRandom;

    /** Static sprite sheet for all monster sprites, currently unused. */
    static SpriteSheet sMonsterSheet = null;

    /**
     * Construct a monster with the given parameters.
     *
     * @param mUID        Unique ID.
     * @param mBreed      Monster's breed.
     * @param mMaxHealth  Max health.
     * @param mHealth     Current health.
     * @param mMaxStamina Max stamina.
     * @param mStamina    Current stamina.
     * @param mMaxHunger  Max hunger.
     * @param mHunger     Current hunger.
     * @param mMaxBladder Max bladder.
     * @param mBladder    Current bladder.
     */
    Monster(String mUID, int mBreed, float mMaxHealth, float mHealth, float mMaxStamina,
            float mStamina, float mMaxHunger, float mHunger, float mMaxBladder, float mBladder) {
        mUID = mUID;
        mBreed = mBreed;
        mMaxHealth = mMaxHealth;
        mHealth = mHealth;
        mMaxStamina = mMaxStamina;
        mStamina = mStamina;
        mMaxHunger = mMaxHunger;
        mHunger = mHunger;
        mMaxBladder = mMaxBladder;
        mBladder = mBladder;

        mX = 0;
        mY = 0;

        mRandom = new Random();

        mWanderFlag = mRandom.nextInt(3);
        mWanderTimer = 0;
    }

    /**
     * Updates the current state of the monster.
     * Currently only makes the monster move. In the future updates will include
     * changes to hunger, stamina, bladder, and health.
     */
    public void Update() {
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

    }


}
