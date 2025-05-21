import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Enemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Enemy extends Actor {
    public void act() {
        move(1); // moves to the right slowly

        if (getX() >= getWorld().getWidth()) {
            getWorld().removeObject(this); // remove when off-screen
        }
    }
}

