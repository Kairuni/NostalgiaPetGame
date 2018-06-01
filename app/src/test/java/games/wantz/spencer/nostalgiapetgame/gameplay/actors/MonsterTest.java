package games.wantz.spencer.nostalgiapetgame.gameplay.actors;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tests a monster via JUnit.
 *
 * @author Norris Spencer
 * @version 1.B, 31 May 2018
 */
public class MonsterTest {
    /**
     * Delta to define the float precision.
     */
    private final float DELTA = 0.1f;

    /**
     * Used to create an instance of the Monster class for testing.
     */
    private Monster testMonster;

    /** Used to create an instance of the Poop subclass class for testing. */
    private Monster.Poop testPoop;

    /** Creates a pre-hatched monster with 100 in maxes, breed 1, and everything set to 80 except for fun which is also 100. */
    @Before
    public void setUp() {
        testMonster = new Monster("UIDTestString", 1, true, 100, 80,
                100, 80, 100, 80, 100,
                80, 100, 80, System.currentTimeMillis());
    }

    /** Tests feeding the creature. */
    @Test
    public void doFeedTest() {
        testMonster.setHunger(50.0f);
        testMonster.setFun(50.0f);
        testMonster.setBladder(50.0f);
        testMonster.doFeed();
        assertEquals(100.0f, testMonster.getHunger(), DELTA);
        assertEquals(30.0f, testMonster.getBladder(), DELTA);
        assertEquals(40.0f, testMonster.getFun(), DELTA);
    }

    /** Tests using the toilet. */
    @Test
    public void doToiletTest() {
        testMonster.setBladder(50.0f);
        testMonster.setFun(50.0f);
        testMonster.doToilet();
        assertEquals(100.0f, testMonster.getBladder(), DELTA);
        assertEquals(40.0f, testMonster.getFun(), DELTA);
    }

    /** Tests using the shower. */
    @Test
    public void doShowerTest() {
        testMonster.setDirty(50.0f);
        testMonster.setFun(50.0f);
        testMonster.doShower();
        assertEquals(100.0f, testMonster.getDirty(), DELTA);
        assertEquals(55.0f, testMonster.getFun(), DELTA);

    }

    /** Tests hatching the monster. */
    @Test
    public void doHatchedTest() {
        testMonster = new Monster("UIDTestString", 1, false, 100, 80,
                100, 80, 100, 80, 100,
                80, 100, 80, System.currentTimeMillis());
        assertEquals(false, testMonster.getHatched());
        testMonster.doHatched();
        assertEquals(true, testMonster.getHatched());
    }

    /** Tests get UID. */
    @Test
    public void getUIDTest() {
        assertEquals("UIDTestString", testMonster.getUID());
    }

    /** Tests getting and setting X value. */
    @Test
    public void getAndSetXTest() {
        testMonster.setX(1);
        assertEquals(1, testMonster.getX());
    }

    /** Tests getting and setting Y value. */
    @Test
    public void getAndSetYTest() {
        testMonster.setY(3);
        assertEquals(3, testMonster.getY(), 0);
    }

    /** Tests getting the breed. */
    @Test
    public void getBreedTest() {
        assertEquals(1, testMonster.getBreed());
    }

    /** Tests getAndSetHealthTest. */
    @Test
    public void getAndSetHealthTestValid() {
        testMonster.setHealth(50.0f);
        assertEquals(50.0f, testMonster.getHealth(), DELTA);
    }

    /**
     * Tests get and set health test above maximum value.
     */
    @Test
    public void getAndSetHealthTestAboveMax() {
        testMonster.setHealth(105.0f);
        assertEquals(100.0f, testMonster.getHealth(), DELTA);
    }

    /**
     * Tests get and set health test below zero.
     */
    @Test
    public void getAndSetHealthTestBelowZero() {
        testMonster.setHealth(-5.0f);
        assertEquals(0.0f, testMonster.getHealth(), DELTA);
    }

    /**
     * Tests get and set stamina with valid values.
     */
    @Test
    public void getAndSetStaminaTestValid() {
        testMonster.setStamina(50.0f);
        assertEquals(50.0f, testMonster.getStamina(), DELTA);
    }

    /**
     * Tests get and set stamina above max values.
     */
    @Test
    public void getAndSetStaminaTestAboveMax() {
        testMonster.setStamina(105.0f);
        assertEquals(100.0f, testMonster.getStamina(), DELTA);
    }

    /**
     * Tests get and set stamina below zero.
     */
    @Test
    public void getAndSetStaminaTestBelowZero() {
        testMonster.setStamina(-5.0f);
        assertEquals(0.0f, testMonster.getStamina(), DELTA);
    }

    /**
     * Tests get and set hunger.
     */
    @Test
    public void getAndSetHungerTestValid() {
        testMonster.setHunger(50.0f);
        assertEquals(50.0f, testMonster.getHunger(), DELTA);
    }

    /**
     * Tests get and set hunger above max.
     */
    @Test
    public void getAndSetHungerTestAboveMax() {
        testMonster.setHunger(105.0f);
        assertEquals(100.0f, testMonster.getHunger(), DELTA);
    }

    /**
     * Tests get and set hunger below zero.
     */
    @Test
    public void getAndSetHungerTestBelowZero() {
        testMonster.setHunger(-5.0f);
        assertEquals(0.0f, testMonster.getHunger(), DELTA);
    }

    /**
     * Tests get and set bladder.
     */
    @Test
    public void getAndSetBladderTestValid() {
        testMonster.setBladder(50.0f);
        assertEquals(50.0f, testMonster.getBladder(), DELTA);
    }

    /**
     * Tests get and set bladder above max.
     */
    @Test
    public void getAndSetBladderTestAboveMax() {
        testMonster.setBladder(105.0f);
        assertEquals(100.0f, testMonster.getBladder(), DELTA);
    }

    /**
     * tests get and set bladder below zero.
     */
    @Test
    public void getAndSetBladderTestBelowZero() {
        testMonster.setBladder(-5.0f);
        assertEquals(0.0f, testMonster.getBladder(), DELTA);
    }

    /**
     * Tests get and set fun.
     */
    @Test
    public void getAndSetFunTestValid() {
        testMonster.setFun(50.0f);
        assertEquals(50.0f, testMonster.getFun(), DELTA);
    }

    /**
     * Tests get and set fun above max.
     */
    @Test
    public void getAndSetFunTestAboveMax() {
        testMonster.setFun(105.0f);
        assertEquals(100.0f, testMonster.getFun(), DELTA);
    }

    /**
     * Tests get and set fun below zero.
     */
    @Test
    public void getAndSetFunTestBelowZero() {
        testMonster.setFun(-5.0f);
        assertEquals(0.0f, testMonster.getFun(), DELTA);
    }

    /**
     * Tests get and set dirty.
     */
    @Test
    public void getAndSetDirtyTestValid() {
        testMonster.setDirty(50.0f);
        assertEquals(50.0f, testMonster.getDirty(), DELTA);
    }

    /**
     * Tests get and set dirty above max.
     */
    @Test
    public void getAndSetDirtyTestAboveMax() {
        testMonster.setDirty(105.0f);
        assertEquals(100.0f, testMonster.getDirty(), DELTA);
    }

    /**
     * Tests get and set dirty below zero.
     */
    @Test
    public void getAndSetDirtyTestBelowZero() {
        testMonster.setDirty(-5.0f);
        assertEquals(0.0f, testMonster.getDirty(), DELTA);
    }

    /** Tests health percent. */
    @Test
    public void getHealthPercTest() {
        testMonster.setHealth(50.1f);
        assertEquals(50.1f, testMonster.getHealthPercent(), DELTA);
    }

    /** Tests get stamina percent. */
    @Test
    public void getStaminaPercTest() {
        testMonster.setStamina(50.2f);
        assertEquals(50.2f, testMonster.getStaminaPercent(), DELTA);
    }

    /** Tests get hunger percent. */
    @Test
    public void getHungerPercTest() {
        testMonster.setHunger(50.3f);
        assertEquals(50.3f, testMonster.getHungerPercent(), DELTA);
    }

    /** Tests get bladder percent. */
    @Test
    public void getBladderPercTest() {
        testMonster.setBladder(50.4f);
        assertEquals(50.4f, testMonster.getBladderPercent(), DELTA);
    }

    /**
     * Tests unhatched update, shouldn't update anything.
     */
    @Test
    public void updateTestUnhatched() {
        testMonster = new Monster("UIDTestString", 1, false, 100, 80,
                100, 80, 100, 80, 100,
                80, 100, 80, System.currentTimeMillis());
        assertEquals(80, testMonster.getHealth(), DELTA);
        assertEquals(80, testMonster.getStamina(), DELTA);
        assertEquals(80, testMonster.getHunger(), DELTA);
        assertEquals(80, testMonster.getBladder(), DELTA);
        assertEquals(100, testMonster.getFun(), DELTA);
        assertEquals(80, testMonster.getDirty(), DELTA);
        testMonster.update(10000L);
        assertEquals(80, testMonster.getHealth(), DELTA);
        assertEquals(80, testMonster.getStamina(), DELTA);
        assertEquals(80, testMonster.getHunger(), DELTA);
        assertEquals(80, testMonster.getBladder(), DELTA);
        assertEquals(100, testMonster.getFun(), DELTA);
        assertEquals(80, testMonster.getDirty(), DELTA);
    }

    /**
     * Tests health gain.
     */
    @Test
    public void updateTestHealthGain() {
        assertEquals(80, testMonster.getHealth(), DELTA);
        testMonster.update(100L);
        assertTrue(80 < testMonster.getHealth());
    }

    /**
     * Tests health loss.
     */
    @Test
    public void updateTestHealthLoss() {
        testMonster.setHunger(0);
        assertEquals(80, testMonster.getHealth(), DELTA);
        testMonster.update(100L);
        assertTrue(80 > testMonster.getHealth());
    }

    /**
     * Tests hunger loss.
     */
    @Test
    public void updateTestHunger() {
        assertEquals(80, testMonster.getHunger(), DELTA);
        testMonster.update(100L);
        assertTrue(80 > testMonster.getHunger());
    }

    /**
     * Tests bladder loss.
     */
    @Test
    public void updateTestBladder() {
        assertEquals(80, testMonster.getBladder(), DELTA);
        testMonster.update(100L);
        assertTrue(80 > testMonster.getBladder());
    }

    /**
     * Tests fun loss.
     */
    @Test
    public void updateTestFun() {
        assertEquals(100, testMonster.getFun(), DELTA);
        testMonster.update(100L);
        assertTrue(100 > testMonster.getFun());
    }

    /**
     * Tests dirty loss.
     */
    @Test
    public void updateTestDirty() {
        assertEquals(80, testMonster.getDirty(), DELTA);
        testMonster.update(100L);
        assertTrue(80 > testMonster.getDirty());
    }

    /**
     * Tests resume.
     */
    @Test
    public void onResumeTest() {
        assertEquals(80, testMonster.getDirty(), DELTA);
        testMonster.onPause();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            fail("The sleep was stopped before 1000 millis passed.");
        }
        testMonster.onResume();

        assertTrue(80 > testMonster.getDirty());
    }

    /**
     * Tests pooping.
     */
    @Test
    public void getPoopsTest() {
        assertEquals(0, testMonster.getPoops().size());
        testMonster.setBladder(0);
        testMonster.update(10L);
        assertEquals(1, testMonster.getPoops().size());
    }
}