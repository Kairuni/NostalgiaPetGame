package games.wantz.spencer.nostalgiapetgame.gameplay.drawing;

import android.graphics.Canvas;

import java.util.List;

public class Animation {

    private SpriteSheet mSprtSht;

    private List<Integer> mFrames;

    private List<Float> mFrameTime;

    private boolean mLoop;

    public Animation (SpriteSheet theSprtSht, List<Integer> theFrame, List<Float> theFrameTime) {
        mSprtSht = theSprtSht;
        mFrames = theFrame;
        mFrameTime = theFrameTime;
    }

    public void Draw (Canvas c, int dX, int dY) {
        // mSprtSht.Draw();
    }
}
