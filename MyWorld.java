import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class MyWorld here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class MyWorld extends World {
    public MyWorld() {
        super(600, 400, 1);
        prepare();
    }

    private void prepare() {
        addObject(new Enemy(), 0, 200);
    }

    public void act() {
    if (Greenfoot.mouseClicked(this)) {
        MouseInfo mi = Greenfoot.getMouseInfo();
        addObject(new Tower(), mi.getX(), mi.getY());
    }
}

}

