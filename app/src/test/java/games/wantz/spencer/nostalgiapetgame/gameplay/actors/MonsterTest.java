package games.wantz.spencer.nostalgiapetgame.gameplay.actors;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.*;

public class MonsterTest {

    /** delta to define the float precision. */
    private final float DELTA = 0.1f;

    /** used to create an instance of the Monster class for testing. */
    private Monster testMonster;

    /** used to create an instance of the Poop class for testing. */
    private Monster.Poop testPoop;

    @Before
    public void setUp() throws Exception {
        testMonster = new Monster("UIDTestString", 1, false, 100.0f, 80.1f,
                100.0f, 80.2f, 100.0f, 80.0f, 100.0f,
                80.4f, 100.0f, 80.5f, System.currentTimeMillis());
    }

    @After
    public void tearDown() throws Exception {
        testMonster = null;
        assertNull(testMonster);
    }

    @Ignore
    @Test
    public void updateTest() {
        //Test the false boolean of the initial if statement
        testMonster = new Monster("UIDTestString", 1, false, 100.1f, 80.1f,
                100.2f, 80.2f, 100.3f, 80.3f, 100.4f,
                80.4f, 100.5f, 80.5f, System.currentTimeMillis());
        assertEquals(0, testMonster.getX(), 0);

        //Test the true boolean of the initial if statement.
        //Check the mX value of mWanderFlag result == 1
        testMonster = new Monster("UIDTestString", 1, true, 100.1f, 80.1f,
                100.2f, 80.2f, 100.3f, 80.3f, 100.4f,
                80.4f, 100.5f, 80.5f, System.currentTimeMillis());
        assertEquals(0, testMonster.getX(), 0);


        //Check the mX value of mWanderFlag result == 1
    }

    @Test
    public void doFeedTest() {
        testMonster.setHunger(50.0f);
        testMonster.setFun(50.0f);
        testMonster.setBladder(50.0f);
        testMonster.doFeed();
        assertEquals(100.0f, testMonster.getHunger(), DELTA);
        assertEquals(30.0f, testMonster.getBladder(), DELTA);
        assertEquals(40.0f, testMonster.getFun(), DELTA);
        assertNotEquals(50.0f, testMonster.getHunger(), DELTA);
        assertNotEquals(50.0f, testMonster.getBladder(), DELTA);
        assertNotEquals(50.0f, testMonster.getFun(), DELTA);
    }

    @Test
    public void doToiletTest() {
        testMonster.setBladder(50.0f);
        testMonster.setFun(50.0f);
        testMonster.doToilet();
        assertEquals(100.0f, testMonster.getBladder(), DELTA);
        assertEquals(40.0f, testMonster.getFun(), DELTA);
        assertNotEquals(50.0f, testMonster.getBladder(), DELTA);
        assertNotEquals(50.0f, testMonster.getFun(), DELTA);
    }

    @Ignore
    @Test
    public void createPoopTest() {
        testMonster.setFun(50.0f);
    }

    @Test
    public void doShowerTest() {
        testMonster.setDirty(50.0f);
        testMonster.setFun(50.0f);
        testMonster.doShower();
        assertEquals(100.0f, testMonster.getDirty(), DELTA);
        assertEquals(55.0f, testMonster.getFun(), DELTA);
        assertNotEquals(50.0f, testMonster.getDirty(), DELTA);
        assertNotEquals(50.0f, testMonster.getFun(), DELTA);
        //Need to check the poop list to verify its empty.
    }

    @Test
    public void doHatchedTest() {
        assertEquals(false, testMonster.getHatched());
        testMonster = new Monster("UIDTestString", 1, true, 100.1f, 80.1f,
                100.2f, 80.2f, 100.3f, 80.3f, 100.4f,
                80.4f, 100.5f, 80.5f, System.currentTimeMillis());
        assertEquals(true, testMonster.getHatched());
    }

    @Ignore
    @Test
    public void onPauseTest() {

    }

    @Ignore
    @Test
    public void onResumeTest() {

    }

    @Test
    public void getUIDTest() {
        assertEquals("UIDTestString", testMonster.getUID());
        assertNotEquals("UIDTestStringFail", testMonster.getUID());
    }

    @Test
    public void getAndSetXTest() {
        testMonster.setX(1);
        assertEquals(1, testMonster.getX());
        assertNotEquals(2, testMonster.getX());
    }

    @Test
    public void getAndSetYTest() {
        testMonster.setY(3);
        assertEquals(3, testMonster.getY(), 0);
        assertNotEquals(4, testMonster.getY(), 0);
    }

    @Test
    public void getBreedTest() {
        assertEquals(1, testMonster.getBreed());
        assertNotEquals(0, testMonster.getBreed());
    }

    @Test
    public void getHatchedTest() {
        assertEquals(false, testMonster.getHatched());
        testMonster = new Monster("UIDTestString", 1, true, 100.1f, 80.1f,
                100.2f, 80.2f, 100.3f, 80.3f, 100.4f,
                80.4f, 100.5f, 80.5f, System.currentTimeMillis());
        assertEquals(true, testMonster.getHatched());
    }

    @Test
    public void getAndSetHealthTest() {
        testMonster.setHealth(50.0f);
        assertEquals(50.0f, testMonster.getHealth(), DELTA);
        assertNotEquals(49.6f, testMonster.getHealth(), DELTA);
        testMonster.setHealth(-5.0f);
        assertEquals(0.0f, testMonster.getHealth(), DELTA);
        assertNotEquals(-5.0f, testMonster.getHealth(), DELTA);
        testMonster.setHealth(105.0f);
        assertEquals(100.0f, testMonster.getHealth(), DELTA);
        assertNotEquals(105f, testMonster.getHealth(), DELTA);
    }

    @Test
    public void getAndSetStaminaTest() {
        testMonster.setStamina(50.2f);
        assertEquals(50.2f, testMonster.getStamina(), DELTA);
        assertNotEquals(49.7f, testMonster.getStamina(), DELTA);
        testMonster.setStamina(-5.0f);
        assertEquals(0.0f, testMonster.getStamina(), DELTA);
        assertNotEquals(-5.0f, testMonster.getStamina(), DELTA);
        testMonster.setStamina(105.0f);
        assertEquals(100.0f, testMonster.getStamina(), DELTA);
        assertNotEquals(105f, testMonster.getStamina(), DELTA);
    }

    @Test
    public void getAndSetHungerTest() {
        testMonster.setHunger(50.3f);
        assertEquals(50.3f, testMonster.getHunger(), DELTA);
        assertNotEquals(49.8f, testMonster.getHunger(), DELTA);
        testMonster.setHunger(-5.0f);
        assertEquals(0.0f, testMonster.getHunger(), DELTA);
        assertNotEquals(-5.0f, testMonster.getHunger(), DELTA);
        testMonster.setHunger(105.0f);
        assertEquals(100.0f, testMonster.getHunger(), DELTA);
        assertNotEquals(105f, testMonster.getHunger(), DELTA);
    }

    @Test
    public void getAndSetBladderTest() {
        testMonster.setBladder(50.4f);
        assertEquals(50.4f, testMonster.getBladder(), DELTA);
        assertNotEquals(49.9f, testMonster.getBladder(), DELTA);
        testMonster.setBladder(-5.0f);
        assertEquals(0.0f, testMonster.getBladder(), DELTA);
        assertNotEquals(-5.0f, testMonster.getBladder(), DELTA);
        testMonster.setBladder(105.0f);
        assertEquals(100.0f, testMonster.getBladder(), DELTA);
        assertNotEquals(105f, testMonster.getBladder(), DELTA);
    }

    @Test
    public void getAndSetFunTest() {
        testMonster.setFun(50.5f);
        assertEquals(50.5f, testMonster.getFun(), DELTA);
        assertNotEquals(50.0f, testMonster.getFun(), DELTA);
        testMonster.setFun(-5.0f);
        assertEquals(0.0f, testMonster.getFun(), DELTA);
        assertNotEquals(-5.0f, testMonster.getFun(), DELTA);
        testMonster.setFun(105.0f);
        assertEquals(100.0f, testMonster.getFun(), DELTA);
        assertNotEquals(105f, testMonster.getFun(), DELTA);
    }

    @Test
    public void getAndSetDirtyTest() {
        testMonster.setDirty(50.6f);
        assertEquals(50.6f, testMonster.getDirty(), DELTA);
        assertNotEquals(50.1f, testMonster.getDirty(), DELTA);
        testMonster.setDirty(-5.0f);
        assertEquals(0.0f, testMonster.getDirty(), DELTA);
        assertNotEquals(-5.0f, testMonster.getDirty(), DELTA);
        testMonster.setDirty(105.0f);
        assertEquals(100.0f, testMonster.getDirty(), DELTA);
        assertNotEquals(105f, testMonster.getDirty(), DELTA);
    }

    @Ignore
    @Test
    public void getPoopsTest() {

    }

    @Test
    public void getHealthPercTest() {
        testMonster.setHealth(50.1f);
        assertEquals(50.1f, testMonster.getHealthPercent(), DELTA);
        assertNotEquals(49.6f, testMonster.getHealthPercent(), DELTA);
    }

    @Test
    public void getStaminaPercTest() {
        testMonster.setStamina(50.2f);
        assertEquals(50.2f, testMonster.getStaminaPercent(), DELTA);
        assertNotEquals(49.7f, testMonster.getStaminaPercent(), DELTA);
    }

    @Test
    public void getHungerPercTest() {
        testMonster.setHunger(50.3f);
        assertEquals(50.3f, testMonster.getHungerPercent(), DELTA);
        assertNotEquals(49.8f, testMonster.getHungerPercent(), DELTA);
    }

    @Test
    public void getBladderPercTest() {
        testMonster.setBladder(50.4f);
        assertEquals(50.4f, testMonster.getBladderPercent(), DELTA);
        assertNotEquals(49.9f, testMonster.getBladderPercent(), DELTA);
    }

    @Test
    public void getFunPercTest() {
        testMonster.setBladder(50.5f);
        assertEquals(50.5f, testMonster.getBladderPercent(), DELTA);
        assertNotEquals(50.0f, testMonster.getBladderPercent(), DELTA);
    }

    @Test
    public void getDirtyPercTest() {
        testMonster.setBladder(50.6f);
        assertEquals(50.6f, testMonster.getBladderPercent(), DELTA);
        assertNotEquals(50.1f, testMonster.getBladderPercent(), DELTA);
    }
}