import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Write a description of class Base here.
 * 
 * @Joshua Stevens
 * @version (a version number or a date)
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

