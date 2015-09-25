package model;

import controller.LevelController;
import org.junit.BeforeClass;
import org.junit.Test;

import utility.Settings;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * This class tests what happens to the walkers.
 * @author Lili
 *
 */
public class WalkerTest {

	private static Walker walker;
	private static LevelController levelcontroller;
	
	 /**
     * This is run before all the tests to initialize them.
     */
	@BeforeClass
	public static void before() {
		levelcontroller = mock(LevelController.class);
		walker = new Walker(0, 0, 0, 10, 0, 0, Settings.MONSTER_SPEED, true, levelcontroller);
	}
	
	/**
	 * This tests what happens when the monster is switched direction while facing left.
	 */
	@Test
	public void testSwitchingDirectionFalse() {
		walker.switchDirection();
		assertEquals(false, walker.isFacingRight());
	}
	
	/**
	 * This tests what happens when the monster is switched direction while facing right.
	 */
	@Test
	public void testSwitchingDirectionTrue() {
		walker.setFacingRight(false);
		walker.switchDirection();
		assertEquals(true, walker.isFacingRight());
	}

	/**
	 * This tests what happens the monster moved.
	 *
	 * The 32 comes from the size of the sprite.
	 * And the 37 is the sprite size + 1 speed (=5),
	 * 32 + 5 = 37.
	 *
	 * @throws Exception .
	 */
	@Test
	public void testMove() throws Exception {
		walker.move();
		assertEquals(Level.SPRITE_SIZE + Settings.MONSTER_SPEED , walker.getX(), 0);
		walker.setFacingRight(false);
		walker.move();
		assertEquals(Level.SPRITE_SIZE, walker.getX(), 0);
	}
	
	/**
	 * This tests what happens when the monsters collides with a bubble.
	 *
	 * The 37 comes from the sprite size + speed (32 + 5).
	 *
	 * @throws Exception .
	 */
	@Test
	public void testMoveBubble() throws Exception {
		Bubble bubble = mock(Bubble.class);
		when(bubble.getX()).thenReturn(30.0);
        when(bubble.getY()).thenReturn(4.0);
        when(bubble.getWidth()).thenReturn(32.0);
        when(bubble.getHeight()).thenReturn(32.0);
        when(bubble.getAbleToCatch()).thenReturn(true);
        walker.setWidth(32.0);
        walker.setHeight(32.0);
        walker.checkCollision(bubble);
        walker.move();
        System.out.println(walker.isCaughtByBubble());
        System.out.println(walker.getX());
        assertEquals(bubble.getX(), walker.getX(), 0);
	}
	
	/**
	 * Test what happens when the walker moves out of the bottom screen.
	 * @throws Exception .
	 */
	@Test
	public void testMoveDown() throws Exception {
		Walker walker1 = new Walker(0, Settings.SCENE_HEIGHT
				, 0, 0, 0, 0, Settings.PLAYER_SPEED, true, levelcontroller);
		walker1.move();
    	assertEquals(Level.SPRITE_SIZE, walker1.getY(), 0.0001);
	}
	
	/**
	 * Test what happens when the walker has a horizontal collision.
	 * @throws Exception .
	 */
	@Test
	public void testMoveCollision() throws Exception {
		when(levelcontroller.causesCollision(walker.getX(), walker.getX() + walker.getWidth(), 
				walker.getY() - walker.calculateGravity(), walker.getY() + walker.getHeight()
								- walker.calculateGravity())
				).thenReturn(true);
		double locationY = walker.getY();
		walker.move();
		assertEquals(locationY, walker.getY(), 0.0001);
	}
}
