package model.gameobject.enemy;

import controller.LevelController;
import controller.LevelControllerMethods;
import controller.ScreenController;
import javafx.animation.AnimationTimer;
import model.gameobject.bubble.Bubble;
import model.gameobject.level.Level;
import model.gameobject.level.Wall;
import model.gameobject.player.Player;
import model.support.Coordinates;
import model.support.SpriteBase;
import org.junit.Before;
import org.junit.Test;
import utility.Settings;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


/**
 * This class tests the Monster class.
 */
public class MonsterTest {

	private Monster monster;
	private LevelController levelController;

	private static double epsilon = 0.001;
	
    /**
     * This is run before all the tests to initialize them.
     */
	@Before
	public void setUp() {
		levelController = mock(LevelController.class);
        ScreenController screenController = mock(ScreenController.class);
        Level level = mock(Level.class);
        ArrayList<Wall> walls = new ArrayList<>();
        
        when(levelController.getCurrLvl()).thenReturn(level);
        when(level.getWalls()).thenReturn(walls);
        when(levelController.getScreenController()).thenReturn(screenController);
        
        Coordinates coordinates = new Coordinates(35, 1, 0, 0, 0, 0);
        monster = new Walker(coordinates, Settings.MONSTER_SPEED, true, levelController);
	}
	
	/**
	 * This tests that collisions can occur.
	 * @throws Exception .
	 */
	@Test
	public void testCheckCollision1() throws Exception {
	  Coordinates coordinates = new Coordinates(1, 1, 0, 0, 0, 0);
	  monster = new Walker(coordinates, Settings.MONSTER_SPEED, true, levelController);
	  Bubble bubble = mock(Bubble.class);
		SpriteBase sprite = mock(SpriteBase.class);
		when(bubble.getSpriteBase()).thenReturn(sprite);
		when(sprite.getXCoordinate()).thenReturn(1.0);
		when(sprite.getYCoordinate()).thenReturn(1.0);
		when(sprite.getWidth()).thenReturn(300.0);
		when(sprite.getHeight()).thenReturn(300.0);
        when(bubble.isAbleToCatch()).thenReturn(true);
        monster.checkCollision(bubble);
        when(bubble.isAbleToCatch()).thenReturn(false);
        monster.checkCollision(bubble);
        assertTrue(monster.isCaughtByBubble());
        assertEquals(monster.getPrisonBubble(), bubble);
	}
	
	   
	/**
	 * This tests if the monster is facing right.
	 * @throws Exception .
	 */
	@Test
	public void testIsFacingRight() throws Exception {
		assertTrue(monster.isFacingRight());
		monster.setFacingRight(false);
		assertFalse(monster.isFacingRight());
	}

	/**
	 * This tests the getspeed method. 
	 * @throws Exception .
	 */
	@Test
	public void testGetSpeed() throws Exception {
		assertEquals(monster.getSpeed(), Settings.MONSTER_SPEED, 0);
	}
	
	/**
	 * This tests that the monsters move. 
	 * @throws Exception .
	 */
	@Test
	public void testMove() throws Exception {
		double newX = monster.getSpriteBase().getXCoordinate() + Settings.MONSTER_SPEED;
	  monster.move();
	  System.out.println(monster.getSpriteBase().getXCoordinate());
		assertEquals(monster.getSpriteBase().getXCoordinate(), newX, 0);
	}
	
	/**
	 * This tests getting the rotation.
	 * @throws Exception .
	 */
	@Test
	public void testRotation() throws Exception {
    	monster.getSpriteBase().setRotation(10);
    	assertEquals(10.0, monster.getSpriteBase().getRotation(), epsilon);
    	monster.getSpriteBase().setDRotation(10);
    	assertEquals(10.0, monster.getSpriteBase().getDRotation(), epsilon);
    }

	/**
	 * This tests getting the height and width.
	 * @throws Exception .
	 */
	@Test
    public void testHeightWidth() throws Exception {
    	monster.getSpriteBase().setHeight(100);
    	assertEquals(100.0, monster.getSpriteBase().getHeight(), epsilon);
    	monster.getSpriteBase().setWidth(100);
    	assertEquals(100.0, monster.getSpriteBase().getWidth(), epsilon);
    }
    
    /**
     * This tests that the sprite changed.
     * @throws Exception .
     */
	@Test
    public void testSpriteChanged() throws Exception {
    	monster.getSpriteBase().setSpriteChanged(false);
    	assertFalse(monster.getSpriteBase().getSpriteChanged());
    }
    
    /**
     * This tests if the monster can actually move.
     * @throws Exception .
     */
	@Test
    public void testCanMove() throws Exception {
    	monster.getSpriteBase().setCanMove(false);
    	double newX = monster.getSpriteBase().getXCoordinate();
    	monster.move();
    	assertEquals(newX, monster.getSpriteBase().getXCoordinate(), epsilon);
    }

	/**
	 * This tests the die function.
	 */
	@Test
	public void testDie() {
		assertFalse(monster.isDead());
		Player player = mock(Player.class);
		Bubble bubble = mock(Bubble.class);
		monster.setPrisonBubble(bubble);
		monster.die(player);

		verify(player, atLeastOnce()).scorePoints(Settings.POINTS_KILL_MONSTER);
		assertTrue(monster.isDead());
		verify(levelController, atLeastOnce()).spawnPowerup(monster);
		verify(bubble, atLeastOnce()).setIsPopped(true);
	}

	/**
	 * This tests the die when the player is null.
	 */
	@Test
	public void testDieNull() {
		assertFalse(monster.isDead());
		Bubble bubble = mock(Bubble.class);
		monster.setPrisonBubble(bubble);
		monster.die(null);

		assertTrue(monster.isDead());
		verify(levelController, atLeastOnce()).spawnPowerup(monster);
		verify(bubble, atLeastOnce()).setIsPopped(true);
	}

	/**
	 * This tests the timer of Monster.
	 */
	@Test
	public void testTimer() {
		AnimationTimer timer = monster.createTimer();
		LevelControllerMethods lcm = mock(LevelControllerMethods.class);
		when(levelController.getLevelControllerMethods()).thenReturn(lcm);
		when(lcm.getGamePaused()).thenReturn(false);

		SpriteBase spriteBase = mock(SpriteBase.class);
		monster.setSpriteBase(spriteBase);

		timer.handle(1);

		verify(spriteBase, atLeastOnce()).move();
	}

	/**
	 * This tests the setDead function.
	 */
	@Test
	public void testSetDead() {
		assertFalse(monster.isDead());
		monster.setDead(true);
		assertTrue(monster.isDead());
	}

	/**
	 * This tests the factorSpeed function.
	 */
	@Test
	public void testFactorSpeed() {
		double speed = monster.getSpeed();
		monster.factorSpeed(3.0);
		assertEquals(speed * 3, monster.getSpeed(), 0.1);
	}

	/**
	 * This tests the destroy function.
	 */
	@Test
	public void testDestroy() {
		assertEquals(2, monster.countObservers());
		monster.destroy();
		assertEquals(0, monster.countObservers());
	}

	/**
	 * This tests the setSpeed function.
	 */
	@Test
	public void testSetSpeed() {
		monster.setSpeed(3.0);
		assertEquals(3.0, monster.getSpeed(), 0.1);
	}


}