package games.wantz.spencer.nostalgiapetgame.gameplay.drawing;

import android.graphics.Canvas;

import java.util.List;

/**
 * An animation of a sprite sheet.
 *
 * @author Keegan Wantz wantzkt@uw.edu
 * @author Norris Spencer nisj@uw.edu
 * @version 1.B, 31 May 2018
 */
public class Animation {
    /**
     * The sprite sheet used by this animation.
     */
    private SpriteSheet mSpriteSheet;
    /**
     * The frame indicies for this animation.
     */
    private List<Integer> mFrames;
    /** The duration of each frame (in milliseconds) for this animation. */
    private List<Long> mFrameTimes;
    /** The current frame of the animation. */
    private int mCurFrame;
    /** The current elapsed time of the animation. */
    private long mCurTime;
    /** Does this animation loop? */
    private boolean mLoop;

    /**
     * Creates a new animation with the provided sprite sheet, frames, frame times, and if it should loop.
     *
     * @param theSpriteSheet The sprite sheet to use for this animation.
     * @param theFrame The frames of the sprite sheet to use for this animation.
     * @param theFrameTimes The duration of each frame for this animation (in milliseconds).
     * @param loop Should this animation loop?
     */
    public Animation(SpriteSheet theSpriteSheet, List<Integer> theFrame, List<Long> theFrameTimes, boolean loop) {
        mSpriteSheet = theSpriteSheet;
        mFrames = theFrame;
        mFrameTimes = theFrameTimes;
        mLoop = loop;
        mCurFrame = 0;
        mCurTime = 0;
    }

    /**
     * Resets this animation to its initial state.
     */
    public void reset() {
        mCurTime = 0;
        mCurFrame = 0;
    }

    /**
     * Updates this animation, possibly advancing to the next frame.
     *
     * @param updateMillis How many milliseconds have elapsed.
     */
    public void update(Long updateMillis) {
        mCurTime += updateMillis;
        if (mCurTime > mFrameTimes.get(mCurFrame)) {
            if (mCurFrame < mFrames.size() - 1 || mLoop) {
                mCurTime -= mFrameTimes.get(mCurFrame);
                mCurFrame++;
                if (mCurFrame == mFrames.size())
                    mCurFrame = 0;
            } else {
                mCurTime = mFrameTimes.get(mCurFrame);
            }

        }
    }

    /**
     * Draw this animation with a scale relative to the sprite sheet's existing scale.
     *
     * @param canvas The canvas to draw on.
     * @param dX The destination x.
     * @param dY The destination y.
     * @param scale The scale (relative to the sprite sheet's scale)
     */
    public void draw(Canvas canvas, int dX, int dY, float scale) {
        mSpriteSheet.draw(canvas, dX, dY, mFrames.get(mCurFrame), mSpriteSheet.getScale() * scale);
    }

    /**
     * Draw this animation without scaling relative to the sprite sheet's existing scale.
     *
     * @param canvas The canvas to draw on.
     * @param dX The destination x.
     * @param dY The destination y.
     */
    public void draw(Canvas canvas, int dX, int dY) {
        this.draw(canvas, dX, dY, 1f);
    }
}
