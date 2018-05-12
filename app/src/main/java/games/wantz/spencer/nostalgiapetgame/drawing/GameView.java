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
 * A view used for rendering game objects, including buttons and sprites.
 *
 * Mostly just used as a blank slate for the game objects, may be expanded later.
 */
public class GameView extends SurfaceView {
    private final SpriteSheet mUnits;
    private final SpriteSheet mBackground;
    private final GameThread mGameThread;
    private Monster mMonster;

    private final float mScalar;

    private final int mDeviceWidth, mDeviceHeight;

    private int mMonsterFrame;

    private int mCounter;


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
        mUnits = new SpriteSheet(BitmapFactory.decodeResource(getResources(), R.drawable.pets_and_icons), 32,32,mScalar);

        mGameThread = new GameThread(this);

        SurfaceHolder mHolder = getHolder();

        mHolder.addCallback(new SurfaceHolder.Callback() {
            @Override
            public void surfaceCreated(SurfaceHolder holder) {
                mGameThread.setActive(true);
                mGameThread.start();
            }

            @Override
            public void surfaceDestroyed(SurfaceHolder holder) {
                mGameThread.setActive(false);
                boolean retry = true;
                while (retry) {
                    try {
                        mGameThread.join();
                        retry = false;
                    } catch (InterruptedException e) {
                    }
                }
            }

            @Override
            public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {}
        });
    }

    public GameView(Context context, AttributeSet set) {
        this(context);
    }

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

    @Override
    public void draw(Canvas canvas) {
        super.draw(canvas);

        // Draw the background and the monster.
        mBackground.Draw(canvas, 0, 0, 0);

        // And draw the monster in the center of the screen.
        int w = mDeviceWidth;
        int h = mDeviceHeight;

        int offset = (int)(16 * mScalar);

        if (mMonster != null) {
            // Let's see if this works. ;_;
            mUnits.Draw(canvas, w / 2 + mMonster.getX() - offset, h / 3 + mMonster.getY() - offset, mMonsterFrame);
        }
    }
}
