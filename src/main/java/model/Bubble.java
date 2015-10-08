package model;

import java.util.ArrayList;

import controller.LevelController;
import utility.Logger;
import utility.Settings;

/**
 * Created by Jim on 9/8/2015.
 *
 * @author Jim
 * @version 0.1
 * @since 9/8/2015
 */
public class Bubble extends SpriteBase {

    /**
     * The counter that is needed for the movement of the bubble.
     */
    private int counter;

    /**
     * This boolean indicates whether it is fired to the right.
     */
    private boolean firedRight;

    /**
     * Only in the first stage of firing the bubble, the bubble is able to catch monsters.
     * This boolean indicates if the bubble can catch a monster.
     */
    private boolean ableToCatch;

    /**
     * This boolean indicates if the bubble has captured a monster.
     */
    private boolean isPrisonBubble;

    /**
     * The levelController that created the player.
     */
    private LevelController levelController;

    /**
     * The bubble that will be shot to catch the monsters.
     * @param x The x coordinate 
     * @param y The y coordinate
     * @param r The rotation
     * @param dx The dx of x
     * @param dy The dy of y
     * @param dr The dr of r
     * @param firedRight If the bubble was fired to the right.
     * @param levelController that controller of the level where the bubble is in.
     */
    public Bubble(double x,
                  double y,
                  double r,
                  double dx,
                  double dy,
                  double dr,
                  boolean firedRight,
                  LevelController levelController) {
        super("../bubble.png", x, y, r, dx, dy, dr);

        counter = 0;
        this.firedRight = firedRight;
        this.ableToCatch = true;
        this.isPrisonBubble = false;
        this.levelController = levelController;

        attach(levelController);
        attach(levelController.getScreenController());
    }

    /**
     * This method is used to check if a bubble is .
     * @return true if Bubble extends life_time of Bubble
     */
    public boolean checkPop() {
        return (counter > Settings.BUBBLE_LIVE_TIME);
    }

    /**
     * This method is used to move the bubble.
     */
    public void move() {

        counter++;

        if (counter < Settings.BUBBLE_FLY_TIME) {
            moveHorizontally();
        } else {
            moveVertically();
        }

        Double newX = getX() + getDx();
        Double newY = getY() + getDy();

        if (!newX.equals(getX()) || !newY.equals(getY())) {
            Logger.log(String.format("Bubble moved from (%f, %f) to (%f, %f)",
                    getX(), getY(), newX, newY));
        }

        super.move();
    }

    /**
     * This function handles the vertical movement, 
     * it allows the bubbles to float to the screen but stop there..
     */
    private void moveVertically() {
        setDx(0);
        if (!levelController.causesCollision(getX(), getX() + getWidth(),
                getY() - Settings.BUBBLE_FLY_SPEED,
                getY() + getHeight() - Settings.BUBBLE_FLY_SPEED)) {
            setDy(-Settings.BUBBLE_FLY_SPEED);
            if (getY() < 0) {
                setY(Settings.SCENE_HEIGHT);
            }
        } else if (getY() <= 35) {
            setDy(0);
        }
        ableToCatch = false;
    }

    /**
     * This function handles the horizontal movement.
     */
    private void moveHorizontally() {
        if (firedRight) {
            if (!levelController.causesCollision(getX() + Settings.BUBBLE_INIT_SPEED,
                    getX() + getWidth() + Settings.BUBBLE_INIT_SPEED,
                    getY(),
                    getY() + getHeight())) {
                setDx(Settings.BUBBLE_INIT_SPEED);
            } else {
                setDx(0);
            }
        } else {
            if (!levelController.causesCollision(getX() - Settings.BUBBLE_INIT_SPEED,
                    getX() + getWidth() - Settings.BUBBLE_INIT_SPEED,
                    getY(),
                    getY() + getHeight())) {
                setDx(-Settings.BUBBLE_INIT_SPEED);
            } else {
                setDx(0);
            }
        }
    }

    /**
     * This method sets the boolean to whether it is able to be caught.
     * @param bool set to the boolean of whether the monsters can be caught.
     */
    public void setAbleToCatch(final boolean bool) {
        ableToCatch = bool;
    }

    /**
     * This method checks to see if a monster is able to be caught.
     * @return true if the monster can be caught.
     */
    public boolean getAbleToCatch() {
        return ableToCatch;
    }

    /**
     * This method sets the boolean to whether it is a prisonBubble.
     * @param bool set to the boolean of whether the monsters is a prisonBubble.
     */
    public void setIsPrisonBubble(final boolean bool) {
        isPrisonBubble = bool;
    }

    /**
     * This method checks to see if a bubble has caught a monster.
     * @return true if the monster can be caught.
     */
    public boolean getIsPrisonBubble() {
        return isPrisonBubble;
    }
}
