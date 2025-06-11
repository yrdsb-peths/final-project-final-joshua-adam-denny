import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * RangeCircle Class is an actor that represents a circular range of a tower or effect in the game.
 * 
 * @author Joshua Stevens
 * @version Version 1.0
 */
public class RangeCircle extends Actor {
    public RangeCircle(int range) {
        GreenfootImage circle = new GreenfootImage(range * 2, range * 2);
        circle.setColor(Color.DARK_GRAY);
        circle.setTransparency(150);
        circle.fillOval(0, 0, range * 2, range * 2);
        setImage(circle);
    }
}

