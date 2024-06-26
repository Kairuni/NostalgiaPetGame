package games.wantz.spencer.nostalgiapetgame.gameplay.actors;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * A class that represents the player's pet monster.
 *
 * @author Keegan Wantz wantzkt@uw.edu
 * @version 1.B, 31 May 2018
 */
public class Monster implements Serializable {

    //Final Field Variables
    /**
     * UID for serialization.
     */
    private static final long serialVersionUID = 0xe05c26dfe03e72b9L;
    // Change direction every 4 seconds.
    /** How often to change wander state (in frames). */
    private static final int WANDER_RESET = 120;
    /** Statistic change per millisecond. */
    private static final float CHANGE_PER_MILLI = 1.0f / 1000.0f;
    /**
     * The multiplier for reducing health while starving.
     */
    private static final float DYING_MULTIPLIER = 5.0f;
    /**
     * The maximum number of poops.
     */
    private static final int MAX_POOPS = 30;

    //Non-Final Field Variables
    /** The Unique ID for this monster. */
    private String mUID;
    /** Random for random movement. */
    private Random mRandom;
    /** A list of poop objects to draw on the screen as well as the monster. */
    private List<Poop> mPoops;
    /** The breed of this monster. */
    private int mBreed;
    /** The current X and Y coordinates for this montster. */
    private int mX, mY;
    /** What direction is the pet currently wandering? 0 right, 1 left, other values are standing still. */
    private int mWanderFlag;
    /** The current timer for wandering, in frames. */
    private int mWanderTimer;
    /** Health statistics for this monster. */
    private float mMaxHealth, mHealth;
    /** Stamina statistics for this monster. */
    private float mMaxStamina, mStamina;
    /** Hunger statistics for this monster. */
    private float mMaxHunger, mHunger;
    /** Bladder statistics for this monster. */
    private float mMaxBladder, mBladder;
    /** Fun statistic for this monster. */
    private float mMaxFun, mFun;
    /** Dirty statistics for this monster. */
    private float mMaxDirty, mDirty;
    /** The epoch time we paused the monster at. */
    private long mPauseTime;
    /** Has this monster been hatched? */
    private boolean mIsHatched;

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
    public Monster(String UID, int Breed, boolean hatched, float maxHealth, float Health, float maxStamina,
                   float Stamina, float maxHunger, float Hunger, float maxBladder, float Bladder,
                   float Fun, float Dirty, long lastAccess) {
        mUID = UID;
        mBreed = Breed;
        mIsHatched = hatched;
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

        mPauseTime = lastAccess;

        mPoops = new ArrayList<>();

        // Let the monster catch up.
        onResume();
    }

    /**
     * Updates the current state of the monster.
     * Currently only makes the monster move. In the future updates will include
     * changes to hunger, stamina, bladder, and health.
     */
    public void update(long tickMillis) {
        if (mIsHatched) {
            // Currently ticks at a FIXED RATE
            if (mWanderFlag == 0) {
                mX += 5;
            } else if (mWanderFlag == 1) {
                mX -= 5;
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

            // Handle biological functions
            float ch = CHANGE_PER_MILLI * tickMillis;
            if (mHunger > 50) {
                setHealth(mHealth + ch);
            } else if (mHunger == 0) {
                setHealth(mHealth - ch * DYING_MULTIPLIER);
            }
            setHunger(mHunger - ch);
            mBladder -= ch;
            while (mBladder < 0) {
                createPoop();
            }
            setFun(mFun - ch);
            setDirty(mDirty - ch);
            for (int i = 0; i < mPoops.size(); i++)
                setDirty(mDirty - ch / 4);
        }
        mPauseTime = System.currentTimeMillis();
    }

    /**
     * Change the monster's statistics as if it had eaten - decreases bladder and fun, refills hunger gauge.
     */
    public void doFeed() {
        mHunger = mMaxHunger;
        setBladder(mBladder - 20);
        setFun(mFun - 10);
    }

    /**
     * Change the monster's statistics as if it had used the bathroom, decreases fun, refills bladder gauge.
     */
    public void doToilet() {
        mBladder = mMaxBladder;
        setFun(mFun - 10);
    }

    /**
     * Called internally when the user fails to allow their poor pet to use the bathroom in a timely fashion.
     * This results in the creation of a poop that will be rendered on screen and removed when the user showers the creature.
     * Dirtiness increases faster if poops are present.
     * 'Fun' also decreases faster.
     * This does relieve bladder, however.
     */
    private void createPoop() {
        mBladder += mMaxBladder;
        setFun(mFun - 20);

        if (mPoops.size() < MAX_POOPS) {
            // Make a new poop with a random X value.
            mPoops.add(new Poop(mX - 400 + (int) (800 * mRandom.nextFloat()),
                    mY + (int) (400 * mRandom.nextFloat()),
                    .75f + .75f * mRandom.nextFloat()));
        }
    }

    /**
     * Change the monster's statistics as if it had taken a shower, increases fun gauge, maxes dirty gauge, and clears all poops.
     */
    public void doShower() {
        mDirty = mMaxDirty;
        setFun(mFun+5);

        mPoops.clear();
    }

    /**
     * Hatches the monster.
     */
    public void doHatched() {
        mIsHatched = true;
    }

    /**
     * Called when the game pauses, stores a time so that the monster can catch up when the application is resumed.
     */
    public void onPause() {
        mPauseTime = System.currentTimeMillis();
    }

    /**
     * Using the current time, catch the monster up.
     */
    public void onResume() {
        long unpauseTime = System.currentTimeMillis();
        update(unpauseTime - mPauseTime);
        mPauseTime = unpauseTime;
    }

    /* GETTERS AND SETTERS */
    /* Of note, the setters will not go below 0 or above the respective max. */
    public void setX(int mX) {
        this.mX = mX;
    }

    public void setY(int mY) {
        this.mY = mY;
    }

    public void setHealth(float health) {
        mHealth = health;
        if (mHealth < 0)
            mHealth = 0;
        if (mHealth > mMaxHealth)
            mHealth = mMaxHealth;
    }

    public void setStamina(float stamina) {
        mStamina = stamina;
        if (mStamina < 0)
            mStamina = 0;
        if (mStamina > mMaxStamina)
            mStamina = mMaxStamina;
    }

    public void setHunger(float hunger) {
        mHunger = hunger;
        if (mHunger < 0)
            mHunger = 0;
        if (mHunger > mMaxHunger)
            mHunger = mMaxHunger;
    }

    public void setBladder(float bladder) {
        mBladder = bladder;
        if (mBladder < 0)
            mBladder = 0;
        if (mBladder > mMaxBladder)
            mBladder = mMaxBladder;
    }

    public void setFun(float fun) {
        mFun = fun;
        if (mFun < 0)
            mFun = 0;
        if (mFun > mMaxFun)
            mFun = mMaxFun;
    }

    public void setDirty(float dirty) {
        mDirty = dirty;
        if (mDirty < 0)
            mDirty = 0;
        if (mDirty > mMaxDirty)
            mDirty = mMaxDirty;
    }

    public String getUID() {
        return mUID;
    }

    public int getX() {
        return mX;
    }

    public int getY() {
        return mY;
    }

    public int getBreed() {
        return mBreed;
    }

    public boolean getHatched() {
        return mIsHatched;
    }

    public float getHealth() {
        return mHealth;
    }

    public float getStamina() {
        return mStamina;
    }

    public float getHunger() {
        return mHunger;
    }

    public float getBladder() {
        return mBladder;
    }

    public float getFun() {
        return mFun;
    }

    public float getDirty() {
        return mDirty;
    }

    public float getMaxHealth() {
        return mMaxHealth;
    }

    public float getMaxStamina() {
        return mMaxStamina;
    }

    public float getMaxHunger() {
        return mMaxHunger;
    }

    public float getMaxBladder() {
        return mMaxBladder;
    }

    public float getHealthPercent() {
        return 100 * mHealth / mMaxHealth;
    }

    public float getStaminaPercent() {
        return 100 * mStamina / mMaxStamina;
    }

    public float getHungerPercent() {
        return 100 * mHunger / mMaxHunger;
    }

    public float getBladderPercent() {
        return 100 * mBladder / mMaxBladder;
    }

    public List<Poop> getPoops() {
        return new ArrayList<Poop>(mPoops);
    }

    /**
     * A list of 'poop' objects that show up on the screen when the pet has soiled itself.
     */
    public class Poop implements Serializable {
        /** UID for serialization. */
        private static final long serialVersionUID = 0xe05c26dfe04e72b9L;


        /** The poop's x coordinate. */
        public int x;
        /** The poop's y coordinate.  */
        public int y;
        /** The poop's scale, for variety. */
        public float scale;

        /** Constructor to make poops. */
        public Poop(int x, int y, float scale) {
            this.x = x;
            this.y = y;
            this.scale = scale;
        }
    }
}
