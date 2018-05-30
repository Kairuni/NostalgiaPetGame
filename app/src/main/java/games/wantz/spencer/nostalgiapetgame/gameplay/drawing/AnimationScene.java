package games.wantz.spencer.nostalgiapetgame.gameplay.drawing;

import android.graphics.Canvas;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Keegan Wantz wantzkt@uw.edu
 * @version B.1, 28 May 2018
 */
public class AnimationScene {
    private List<Animation> mAnimations;
    private List<List<Point>> mLocations;
    // In milliseconds
    private List<List<Long>> mTimeToLocations;
    private List<Integer> mFrames;
    // In milliseconds
    private List<Long> mFrameTimers;
    // Is a given set complete?
    private List<Boolean> mIsComplete;

    // has the entire scene completed its animation?
    private boolean mSceneAnimationComplete;

    public boolean getIsComplete() {
        return mSceneAnimationComplete;
    }

    public AnimationScene(List<Animation> animations, List<List<Point>> animationPoints, List<List<Long>> timeToLocations) {
        if (animations.size() != animationPoints.size() || animations.size() != timeToLocations.size()) {
            throw new IllegalArgumentException("All arguments for AnimationScene must be the same length.");
        }

        mAnimations = animations;
        mLocations = animationPoints;
        mTimeToLocations = timeToLocations;
        mFrames = new ArrayList<>();
        mFrameTimers = new ArrayList<>();
        mIsComplete = new ArrayList<>();

        for (int i = 0; i < mAnimations.size(); i++) {
            mFrames.add(1);
            mFrameTimers.add(0L);
            mIsComplete.add(false);
        }

        mSceneAnimationComplete = false;
    }

    public void update(long timeUpdateInMillis) {
        for (int i = 0; i < mAnimations.size(); i++) {
            mAnimations.get(i).update(timeUpdateInMillis);

            int frame = mFrames.get(i);
            List<Point> locations = mLocations.get(i);
            List<Long> times = mTimeToLocations.get(i);

            // First, calculate the current frame time
            long currentTimer = mFrameTimers.get(i) + timeUpdateInMillis;
            // Now, if we have passed the current time, increment the current frame.
            if (currentTimer > times.get(frame)) {
                // If we still have frames to execute, move on to the next one
                if (frame < times.size() - 1) {
                    currentTimer -= times.get(frame);
                    frame++;
                    mFrames.set(i, frame);
                } else {
                    mIsComplete.set(i, true);
                    // Otherwise, freeze at the final frame
                    currentTimer = times.get(frame);
                }
            }
            mFrameTimers.set(i, currentTimer);
        }
    }

    public void reset() {
        for (Animation anim : mAnimations) {
            anim.reset();
        }

        for (int i = 0; i < mFrames.size(); i++) {
            mFrames.set(i, 1);
            mFrameTimers.set(i, 0L);
            mIsComplete.set(i, false);
        }
        mSceneAnimationComplete = false;
    }

    public void draw(Canvas canvas) {
        boolean done = true;
        for (int i = 0; i < mAnimations.size(); i++) {
            if (!mIsComplete.get(i)) {
                done = false;
            }

            int frame = mFrames.get(i);
            List<Point> locations = mLocations.get(i);
            List<Long> times = mTimeToLocations.get(i);

            long currentTimer = mFrameTimers.get(i);

            // Frame 0 is initial, and frame is always at least 1.
            Point previousPoint = locations.get(frame - 1);
            Point currentPoint = locations.get(frame);

            // With this in mind, we always start at the previous and move towards the current.
            Point diff = new Point(currentPoint.x - previousPoint.x, currentPoint.y - previousPoint.y);

            // Calculate how far along in the frame we are:
            float mult = (float) currentTimer / (float) times.get(frame);

            Point drawPoint = new Point(previousPoint.x + (int) (diff.x * mult), previousPoint.y + (int) (diff.y * mult));

            Animation curAnim = mAnimations.get(i);
            curAnim.draw(canvas, drawPoint.x, drawPoint.y);
        }

        if (done)
            mSceneAnimationComplete = true;
    }

}
