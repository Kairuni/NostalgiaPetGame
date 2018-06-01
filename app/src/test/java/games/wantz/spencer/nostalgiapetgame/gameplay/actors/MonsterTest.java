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
    public void testDoToilet() {
        testMonster.setBladder(50.0f);
        testMonster.setFun(50.0f);
        testMonster.doToilet();
        assertEquals(100.0f, testMonster.getBladder(), DELTA);
        assertEquals(40.0f, testMonster.getFun(), DELTA);
    }

    /** Tests using the shower. */
    @Test
    public void testDoShower() {
        testMonster.setDirty(50.0f);
        testMonster.setFun(50.0f);
        testMonster.doShower();
        assertEquals(100.0f, testMonster.getDirty(), DELTA);
        assertEquals(55.0f, testMonster.getFun(), DELTA);

    }

    /** Tests hatching the monster. */
    @Test
    public void testDoHatched() {
        testMonster = new Monster("UIDTestString", 1, false, 100, 80,
                100, 80, 100, 80, 100,
                80, 100, 80, System.currentTimeMillis());
        assertEquals(false, testMonster.getHatched());
        testMonster.doHatched();
        assertEquals(true, testMonster.getHatched());
    }

    /** Tests get UID. */
    @Test
    public void testGetUID() {
        assertEquals("UIDTestString", testMonster.getUID());
    }

    /** Tests getting and setting X value. */
    @Test
    public void testGetAndSetX() {
        testMonster.setX(1);
        assertEquals(1, testMonster.getX());
    }

    /** Tests getting and setting Y value. */
    @Test
    public void testGetAndSetY() {
        testMonster.setY(3);
        assertEquals(3, testMonster.getY(), 0);
    }

    /** Tests getting the breed. */
    @Test
    public void testGetBreed() {
        assertEquals(1, testMonster.getBreed());
    }

    /** Tests getAndSetHealthTest. */
    @Test
    public void testGetAndSetHealthValid() {
        testMonster.setHealth(50.0f);
        assertEquals(50.0f, testMonster.getHealth(), DELTA);
    }

    /**
     * Tests get and set health test above maximum value.
     */
    @Test
    public void testGetAndSetHealthAboveMax() {
        testMonster.setHealth(105.0f);
        assertEquals(100.0f, testMonster.getHealth(), DELTA);
    }

    /**
     * Tests get and set health test below zero.
     */
    @Test
    public void testGetAndSetHealthBelowZero() {
        testMonster.setHealth(-5.0f);
        assertEquals(0.0f, testMonster.getHealth(), DELTA);
    }

    /**
     * Tests get and set stamina with valid values.
     */
    @Test
    public void testGetAndSetStaminaValid() {
        testMonster.setStamina(50.0f);
        assertEquals(50.0f, testMonster.getStamina(), DELTA);
    }

    /**
     * Tests get and set stamina above max values.
     */
    @Test
    public void testGetAndSetStaminaAboveMax() {
        testMonster.setStamina(105.0f);
        assertEquals(100.0f, testMonster.getStamina(), DELTA);
    }

    /**
     * Tests get and set stamina below zero.
     */
    @Test
    public void testGetAndSetStaminaBelowZero() {
        testMonster.setStamina(-5.0f);
        assertEquals(0.0f, testMonster.getStamina(), DELTA);
    }

    /**
     * Tests get and set hunger.
     */
    @Test
    public void testGetAndSetHungerValid() {
        testMonster.setHunger(50.0f);
        assertEquals(50.0f, testMonster.getHunger(), DELTA);
    }

    /**
     * Tests get and set hunger above max.
     */
    @Test
    public void testGetAndSetHungerAboveMax() {
        testMonster.setHunger(105.0f);
        assertEquals(100.0f, testMonster.getHunger(), DELTA);
    }

    /**
     * Tests get and set hunger below zero.
     */
    @Test
    public void testGetAndSetHungerBelowZero() {
        testMonster.setHunger(-5.0f);
        assertEquals(0.0f, testMonster.getHunger(), DELTA);
    }

    /**
     * Tests get and set bladder.
     */
    @Test
    public void testGetAndSetBladderValid() {
        testMonster.setBladder(50.0f);
        assertEquals(50.0f, testMonster.getBladder(), DELTA);
    }

    /**
     * Tests get and set bladder above max.
     */
    @Test
    public void testGetAndSetBladderAboveMax() {
        testMonster.setBladder(105.0f);
        assertEquals(100.0f, testMonster.getBladder(), DELTA);
    }

    /**
     * tests get and set bladder below zero.
     */
    @Test
    public void testGetAndSetBladderBelowZero() {
        testMonster.setBladder(-5.0f);
        assertEquals(0.0f, testMonster.getBladder(), DELTA);
    }

    /**
     * Tests get and set fun.
     */
    @Test
    public void testGetAndSetFunValid() {
        testMonster.setFun(50.0f);
        assertEquals(50.0f, testMonster.getFun(), DELTA);
    }

    /**
     * Tests get and set fun above max.
     */
    @Test
    public void testGetAndSetFunAboveMax() {
        testMonster.setFun(105.0f);
        assertEquals(100.0f, testMonster.getFun(), DELTA);
    }

    /**
     * Tests get and set fun below zero.
     */
    @Test
    public void testGetAndSetFunBelowZero() {
        testMonster.setFun(-5.0f);
        assertEquals(0.0f, testMonster.getFun(), DELTA);
    }

    /**
     * Tests get and set dirty.
     */
    @Test
    public void testGetAndSetDirtyValid() {
        testMonster.setDirty(50.0f);
        assertEquals(50.0f, testMonster.getDirty(), DELTA);
    }

    /**
     * Tests get and set dirty above max.
     */
    @Test
    public void testGetAndSetDirtyAboveMax() {
        testMonster.setDirty(105.0f);
        assertEquals(100.0f, testMonster.getDirty(), DELTA);
    }

    /**
     * Tests get and set dirty below zero.
     */
    @Test
    public void testGetAndSetDirtyBelowZero() {
        testMonster.setDirty(-5.0f);
        assertEquals(0.0f, testMonster.getDirty(), DELTA);
    }

    /** Tests health percent. */
    @Test
    public void testGetHealthPerc() {
        testMonster.setHealth(50.1f);
        assertEquals(50.1f, testMonster.getHealthPercent(), DELTA);
    }

    /** Tests get stamina percent. */
    @Test
    public void testGetStaminaPerc() {
        testMonster.setStamina(50.2f);
        assertEquals(50.2f, testMonster.getStaminaPercent(), DELTA);
    }

    /** Tests get hunger percent. */
    @Test
    public void testGetHungerPerc() {
        testMonster.setHunger(50.3f);
        assertEquals(50.3f, testMonster.getHungerPercent(), DELTA);
    }

    /** Tests get bladder percent. */
    @Test
    public void testGetBladderPerc() {
        testMonster.setBladder(50.4f);
        assertEquals(50.4f, testMonster.getBladderPercent(), DELTA);
    }

    /**
     * Tests unhatched update, shouldn't update anything.
     */
    @Test
    public void testUpdateUnhatched() {
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
    public void testUpdateHealthGain() {
        assertEquals(80, testMonster.getHealth(), DELTA);
        testMonster.update(100L);
        assertTrue(80 < testMonster.getHealth());
    }

    /**
     * Tests health loss.
     */
    @Test
    public void testUpdateHealthLoss() {
        testMonster.setHunger(0);
        assertEquals(80, testMonster.getHealth(), DELTA);
        testMonster.update(100L);
        assertTrue(80 > testMonster.getHealth());
    }

    /**
     * Tests hunger loss.
     */
    @Test
    public void testUpdateHunger() {
        assertEquals(80, testMonster.getHunger(), DELTA);
        testMonster.update(100L);
        assertTrue(80 > testMonster.getHunger());
    }

    /**
     * Tests bladder loss.
     */
    @Test
    public void testUpdateBladder() {
        assertEquals(80, testMonster.getBladder(), DELTA);
        testMonster.update(100L);
        assertTrue(80 > testMonster.getBladder());
    }

    /**
     * Tests fun loss.
     */
    @Test
    public void testUpdateFun() {
        assertEquals(100, testMonster.getFun(), DELTA);
        testMonster.update(100L);
        assertTrue(100 > testMonster.getFun());
    }

    /**
     * Tests dirty loss.
     */
    @Test
    public void testUpdateDirty() {
        assertEquals(80, testMonster.getDirty(), DELTA);
        testMonster.update(100L);
        assertTrue(80 > testMonster.getDirty());
    }

    /**
     * Tests resume.
     */
    @Test
    public void testOnResume() {
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
    public void testGetPoops() {
        assertEquals(0, testMonster.getPoops().size());
        testMonster.setBladder(0);
        testMonster.update(10L);
        assertEquals(1, testMonster.getPoops().size());
    }
}