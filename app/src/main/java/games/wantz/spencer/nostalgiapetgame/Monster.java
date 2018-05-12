package games.wantz.spencer.nostalgiapetgame;

import android.graphics.BitmapFactory;

import java.util.Random;

import games.wantz.spencer.nostalgiapetgame.drawing.SpriteSheet;

public class Monster {
    // Change direction every 4 seconds.
    private int WANDER_RESET = 120;

    private String mUID;
    private int mBreed;
    private float mMaxHealth, mHealth;
    private float mMaxStamina, mStamina;
    private float mMaxHunger, mHunger;
    private float mMaxBladder, mBladder;

    private int mX, mY;
    public int getX() {return mX;}
    public int getY() {return mY;}

    private int mWanderFlag;
    private int mWanderTimer;

    private Random mRandom;

    static SpriteSheet sMonsterSheet = null;

    public Monster(String mUID, int mBreed, float mMaxHealth, float mHealth, float mMaxStamina,
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

    // Currently ticks at a FIXED RATE
    public void Update() {
        // Move every other frame, some types of monsters may move more.
        //if (mWanderTimer % 2 == 0) {
        if (mWanderFlag == 0) {
            mX += 2;
        } else if (mWanderFlag == 1) {
            mX -= 2;
        }
        //}
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
