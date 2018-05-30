package games.wantz.spencer.nostalgiapetgame.gameplay.drawing;

import android.graphics.Canvas;

import java.util.List;

public class Animation {

    private SpriteSheet mSprtSht;

    private List<Integer> mFrames;

    private List<Float> mFrameTime;

    private boolean mLoop;

    public Animation(SpriteSheet theSprtSht, List<Integer> theFrame, List<Float> theFrameTime, boolean loop) {
        mSprtSht = theSprtSht;
        mFrames = theFrame;
        mFrameTime = theFrameTime;
        mLoop = loop;
    }

    public void draw(Canvas c, int dX, int dY) {
        mSprtSht.Draw(c, dX, dY, mFrames.get(1));
    }
}