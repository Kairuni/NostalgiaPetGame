package games.wantz.spencer.nostalgiapetgame.gameplay.drawing;

import android.graphics.Point;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import android.util.Log;

public class SceneBuilder {
    /** For reference.  Should be used when possible. */
    public static int IDLE_IDX = 0;
    /** For reference.  Should be used when possible. */
    public static int FEED_IDX = 1;
    /** For reference.  Should be used when possible. */
    public static int MEAT_IDX = 2;
    /** For reference.  Should be used when possible. */
    public static int POOP_IDX = 3;
    /** For reference.  Should be used when possible. */
    public static int TUB_IDX = 0;
    /** For reference.  Should be used when possible. */
    public static int TOILET_IDX = 1;

    private SceneBuilder() {
        throw new IllegalStateException("Should never be able to instantiate this class.");
    }

    // Returns 0 -- idle, 1 -- feed, 2 -- meat.
    public static List<Animation> buildMonsterAnimations(SpriteSheet monsterSheet, int breed) {
        List<Animation> outList = new ArrayList<>();

        int startIdx = breed * 16;
        // The current monster idling
        Integer idleFrames[] = {startIdx, startIdx + 1};
        Long idleTimes[] = {1600L, 400L};
        outList.add(new Animation(monsterSheet, Arrays.asList(idleFrames), Arrays.asList(idleTimes), true));

        // The current monster feeding
        Integer feedFrames[] = {startIdx + 2, startIdx + 3};
        Long feedTimes[] = {400L, 400L};
        outList.add(new Animation(monsterSheet, Arrays.asList(feedFrames), Arrays.asList(feedTimes), true));

        // Also build the meat eat animation
        Integer meatFrames[] = {69, 70, 71};
        Long meatTimes[] = {400L, 800L, 400L};
        outList.add(new Animation(monsterSheet, Arrays.asList(meatFrames), Arrays.asList(meatTimes), false));

        // And the poop animation
        Integer poopFrames[] = {7, 8};
        Long poopTimes[] = {100L, 100L};
        outList.add(new Animation(monsterSheet, Arrays.asList(poopFrames), Arrays.asList(poopTimes), true));

        return outList;
    }

    // 0 -- tub, 1 -- outhouse
    public static List<Animation> buildFixtureAnimations(SpriteSheet fixturesSheet) {
        List<Animation> outList = new ArrayList<>();


        Integer tubFrames[] = {0};
        Long tubTimes[] = {0L};
        outList.add(new Animation(fixturesSheet, Arrays.asList(tubFrames), Arrays.asList(tubTimes), true));

        Integer outhouseFrames[] = {1};
        Long outhouseFrameTimes[] = {0L};
        outList.add(new Animation(fixturesSheet, Arrays.asList(outhouseFrames), Arrays.asList(outhouseFrameTimes), true));

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

        Point monsterPoint = new Point(phoneWidth / 2 + (int) (16 * scalar), phoneHeight / 2 - offset);
        feedPoints.add(monsterPoint);
        feedPoints.add(monsterPoint);
        Point meatPoint = new Point(phoneWidth / 2 - (int) (16 * scalar), phoneHeight / 2 - offset);
        meatPoints.add(meatPoint);
        meatPoints.add(meatPoint);

        List<List<Point>> animationPoints = new ArrayList<>();
        animationPoints.add(feedPoints);
        animationPoints.add(meatPoints);

        // Use the same timer for how long it takes the object to "move" - 2 seconds of waiting.
        List<Long> timers = new ArrayList<>();
        timers.add(0L);
        timers.add(2000L);
        List<List<Long>> timeToLocations = new ArrayList<>();
        timeToLocations.add(timers);
        timeToLocations.add(timers);

        return new AnimationScene(feedAnimations, animationPoints, timeToLocations);
    }

    public static AnimationScene buildShowerScene(Animation idleAnimation, Animation tubAnimation, int phoneWidth, int phoneHeight) {
        float scalar = phoneWidth / 160.0f;
        int offset = (int) (16 * scalar);
        int cX = phoneWidth / 2;
        int cY = phoneHeight / 2 - offset;

        List<Animation> showerAnimations = new ArrayList<>();
        showerAnimations.add(idleAnimation);
        showerAnimations.add(tubAnimation);

        // Coding this one slightly differently from the above, I feel like the array list conversion from arrays is easier to read.
        // To explain the weird scalar multiplications, it's so that we deal with locations relative to screen size, rather than exact values.
        // The images themselves are scaled, after all.
        Point monsterPoints[] = {
                new Point(cX - (int) (64 * scalar), cY),
                // Hop into the tub
                new Point(cX - (int) (32 * scalar), cY - (int) (20 * scalar)),
                // Land in the middle of the tub
                new Point(cX, cY),
                new Point(cX - (int) (16 * scalar), cY),
                new Point(cX + (int) (16 * scalar), cY),
                new Point(cX, cY)};
        Point tubPoints[] = {new Point(cX - (int) (2 * scalar), cY - (int) (5 * scalar)), new Point(cX - (int) (2 * scalar), cY - (int) (5 * scalar))};
        List<List<Point>> pList = new ArrayList<>();
        pList.add(Arrays.asList(monsterPoints));
        pList.add(Arrays.asList(tubPoints));

        // And the timers.
        Long monsterTimes[] = {0L, 100L, 100L, 250L, 500L, 100L};
        Long tubTimes[] = {1L, 1L};
        List<List<Long>> tList = new ArrayList<>();
        tList.add(Arrays.asList(monsterTimes));
        tList.add(Arrays.asList(tubTimes));

        return new AnimationScene(showerAnimations, pList, tList);
    }

    public static AnimationScene buildToiletScene(Animation idleAnimation, Animation tubAnimation, int phoneWidth, int phoneHeight) {
        float scalar = phoneWidth / 160.0f;
        int offset = (int) (16 * scalar);
        int cX = phoneWidth / 2;
        int cY = phoneHeight / 2 - offset;

        List<Animation> toiletAnimations = new ArrayList<>();
        toiletAnimations.add(idleAnimation);
        toiletAnimations.add(tubAnimation);

        // Same as the above method
        Point monsterPoints[] = {
                new Point(cX - (int) (64 * scalar), cY + (int) (40 * scalar)),
                // Walk into the outhouse
                new Point(cX, cY),
                new Point(cX, cY)};
        Point outhousePoints[] = {new Point(cX - (int) (2 * scalar), cY - (int) (5 * scalar)), new Point(cX - (int) (2 * scalar), cY - (int) (5 * scalar))};
        List<List<Point>> pList = new ArrayList<>();
        pList.add(Arrays.asList(monsterPoints));
        pList.add(Arrays.asList(outhousePoints));

        // And the timers.
        Long monsterTimes[] = {0L, 200L, 1800L};
        Long outhouseTimes[] = {1L, 1L};
        List<List<Long>> tList = new ArrayList<>();
        tList.add(Arrays.asList(monsterTimes));
        tList.add(Arrays.asList(outhouseTimes));

        return new AnimationScene(toiletAnimations, pList, tList);
    }
}
