package games.wantz.spencer.nostalgiapetgame.gameplay.drawing;

import android.graphics.Canvas;

import java.util.List;

public class Animation {

    private SpriteSheet mSpriteSheet;

    private List<Integer> mFrames;

    private List<Long> mFrameTimes;

    private boolean mLoop;

    private int mCurFrame;
    private long mCurTime;

    public Animation(SpriteSheet theSpriteSheet, List<Integer> theFrame, List<Long> theFrameTimes, boolean loop) {
        mSpriteSheet = theSpriteSheet;
        mFrames = theFrame;
        mFrameTimes = theFrameTimes;
        mLoop = loop;
        mCurFrame = 0;
        mCurTime = 0;
    }

    public void reset() {
        mCurTime = 0;
        mCurFrame = 0;
    }

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

    public void draw(Canvas canvas, int dX, int dY) {
        mSpriteSheet.draw(canvas, dX, dY, mFrames.get(mCurFrame));
    }
}
