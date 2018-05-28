package games.wantz.spencer.nostalgiapetgame.gameplay.drawing;

import android.graphics.Canvas;
import android.graphics.Point;

import java.util.ArrayList;
import java.util.List;

public class AnimationScene {
    List<Animation> mAnimations;
    List<List<Point>> mLocations;
    // In milliseconds
    List<List<Long>> mTimeToLocations;
    List<Integer> mFrames;
    // In milliseconds
    List<Long> mFrameTimers;

    long mCurrentTime;

    public AnimationScene(List<Animation> animations, List<List<Point>> animationPoints, List<List<Long>> timeToLocations) {
        if (animations.size() != animationPoints.size() || animations.size() != timeToLocations.size()) {
            throw new IllegalArgumentException("All arguments for AnimationScene must be the same length.");
        }

        mAnimations = animations;
        mLocations = animationPoints;
        mTimeToLocations = timeToLocations;
        mFrames = new ArrayList<>(animations.size());
        mFrameTimers = new ArrayList<>(animations.size());
        for (int i = 0; i < mAnimations.size(); i++) {
            mFrames.add(1);
            mFrameTimers.add(0L);
        }

        mCurrentTime = 0;
    }

    public void Draw(Canvas canvas, long timeUpdateInMillis) {
        for (int i = 0; i < mAnimations.size(); i++) {
            int frame = mFrames.get(i);
            List<Point> locations = mLocations.get(i);
            List<Long> times = mTimeToLocations.get(i);

            // First, calculate the current frame time
            long currentTimer = mFrameTimers.get(i) + timeUpdateInMillis;
            // Now, if we have passed the current time, increment the current frame.
            if (currentTimer > times.get(frame)) {
                // If we still have frames to execute, move on to the next one
                if (frame < times.size()) {
                    currentTimer -= times.get(frame);
                    frame++;
                    mFrames.set(i, frame);
                } else {
                    // Otherwise, freeze at the final frame
                    currentTimer = times.get(frame);
                }
            }


            // Frame 0 is initial, and frame is always at least 1.
            Point previousPoint = locations.get(frame - 1);
            Point currentPoint = locations.get(frame);

            // With this in mind, we always start at the previous and move towards the current.
            Point diff = new Point(currentPoint.x - previousPoint.x, currentPoint.y - previousPoint.y);

            // Calculate how far along in the frame we are:
            float mult = (float) currentTimer / (float) times.get(frame);

            Point drawPoint = new Point(previousPoint.x + (int) (diff.x * mult), previousPoint.y + (int) (diff.y * mult));

            Animation curAnim = mAnimations.get(i);
            curAnim.Draw(canvas, drawPoint.x, drawPoint.y);
        }
    }
}
