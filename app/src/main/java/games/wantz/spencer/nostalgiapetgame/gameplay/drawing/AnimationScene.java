package games.wantz.spencer.nostalgiapetgame.gameplay.drawing;

import android.graphics.Canvas;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Scenes that move animations around the screen.
 *
 * @author Keegan Wantz wantzkt@uw.edu
 * @version 1.B, 31 May 2018
 */
public class AnimationScene {
    /**
     * A list of animations that this scene uses.
     */
    private List<Animation> mAnimations;
    /**
     * A list of booleans that tell if an individual frame has completed its movements.
     */
    private List<Boolean> mIsComplete;
    /**
     * A list of Integers that is used to determine what location to move to next.
     */
    private List<Integer> mFrames;
    /** How long the current frame has been running, in milliseconds. */
    private List<Long> mFrameTimers;
    /** How long a given frame should take, in milliseconds. */
    private List<List<Long>> mTimeToLocations;
    /**
     * Where a given animation should move to.
     */
    private List<List<Point>> mLocations;
    /** Denotes if the entire scene has completed its movements. */
    private boolean mSceneAnimationComplete;

    /**
     * Create an animation scene with the given animations, points to move those animations to,
     *   and a list of times it should take to complete those movements.
     *
     * @param animations The animations to use in this scene.
     * @param animationPoints The points to move these animations to.
     * @param timeToLocations The amount of time it should take to move to those points.
     */
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

    /**
     * Update the scene, moving all animations as needed.
     *
     * @param timeUpdateInMillis How long has elapsed, in milliseconds.
     */
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

    /**
     * Reset this scene to its initial state.
     */
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

    /**
     * Draws this scene to the provided canvas.
     *
     * @param canvas The canvas to draw to.
     */
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

    /**
     * Has this scene completed?
     *
     * @return True if it has, false otherwise.
     */
    public boolean getIsComplete() {
        return mSceneAnimationComplete;
    }
}