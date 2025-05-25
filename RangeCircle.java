import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class RangeCircle extends Actor {
    public RangeCircle(int range) {
        GreenfootImage circle = new GreenfootImage(range * 2, range * 2);
        circle.setColor(Color.DARK_GRAY);
        circle.setTransparency(150);
        circle.fillOval(0, 0, range * 2, range * 2);
        setImage(circle);
    }
}

