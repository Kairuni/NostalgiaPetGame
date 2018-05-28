package games.wantz.spencer.nostalgiapetgame.gameplay.drawing;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AnimationSceneBuilder {
    private AnimationSceneBuilder() {
        throw new IllegalStateException("Should never be able to instantiate this class.");
    }

    // Returns 0 -- idle, 1 -- feed, 2 -- meat.
    public static List<Animation> buildMonsterAnimations(SpriteSheet monsterSheet, int breed) {
        List<Animation> outList = new ArrayList<>();

        int startIdx = breed * 16;
        // The current monster idling
        Integer idleFrames[] = {startIdx, startIdx + 1};
        Float idleTimes[] = {0.8f, 0.2f};
        outList.add(new Animation(monsterSheet, Arrays.asList(idleFrames), Arrays.asList(idleTimes), true));

        // The current monster feeding
        Integer feedFrames[] = {startIdx + 2, startIdx + 3};
        Float feedTimes[] = {0.2f, 0.2f};
        outList.add(new Animation(monsterSheet, Arrays.asList(feedFrames), Arrays.asList(feedTimes), true));

        // Also build the meat eat, tub, and outhouse 'animations', even though they aren't really animations.
        Integer meatFrames[] = {69, 70, 71};
        Float meatTimes[] = {0.2f, 0.4f, 0.4f};
        outList.add(new Animation(monsterSheet, Arrays.asList(meatFrames), Arrays.asList(meatTimes), false));

        return outList;
    }

    // 0 -- tub, 1 -- outhouse
    public static List<Animation> buildFixtureAnimations(SpriteSheet fixturesSheet) {
        List<Animation> outList = new ArrayList<>();

        Integer outhouseFrames[] = {1};
        Float outhouseFrameTimes[] = {0f};
        outList.add(new Animation(fixturesSheet, Arrays.asList(outhouseFrames), Arrays.asList(outhouseFrameTimes), true));

        Integer tubFrames[] = {0};
        Float tubTimes[] = {0f};
        outList.add(new Animation(fixturesSheet, Arrays.asList(tubFrames), Arrays.asList(tubTimes), true));

        return outList;
    }

    public static AnimationScene buildFeedScene(Animation feedAnimation, Animation meatAnimation, int phoneWidth, int phoneHeight) {
        List<Animation> feedAnimations = new ArrayList<>();
        feedAnimations.add(feedAnimation);
        feedAnimations.add(meatAnimation);

        List<Point> feedPoints = new ArrayList<>();
        List<Point> meatPoints = new ArrayList<>();
        float scalar = phoneWidth / 160.0f;
        int offset = (int) (16 * scalar);

        Point monsterPoint = new Point(phoneWidth / 2 + 50, phoneHeight / 2 - offset);
        feedPoints.add(monsterPoint);
        feedPoints.add(monsterPoint);
        Point meatPoint = new Point(phoneWidth / 2 - 50, phoneHeight / 2 - offset);
        meatPoints.add(monsterPoint);
        meatPoints.add(monsterPoint);

        List<List<Point>> animationPoints = new ArrayList<>();
        animationPoints.add(feedPoints);
        animationPoints.add(meatPoints);

        // Use the same timer for how long it takes the object to move.
        List<Long> timers = new ArrayList<>();
        timers.add(0L);
        timers.add(2L);
        List<List<Long>> timeToLocations = new ArrayList<>();
        timeToLocations.add(timers);
        timeToLocations.add(timers);

        return new AnimationScene(feedAnimations, animationPoints, timeToLocations);
    }

}
