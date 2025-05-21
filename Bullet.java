import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.List;

/**
 * Write a description of class Bullet here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Bullet extends Actor {
    private Enemy target;

    public Bullet(Enemy target) {
        this.target = target;
    }

    public void act() {
        if (target != null && getWorld().getObjects(Enemy.class).contains(target)) {

            turnTowards(target.getX(), target.getY());
            move(5);
            if (intersects(target)) {
                getWorld().removeObject(target);
                getWorld().removeObject(this);
            }
        } else {
            getWorld().removeObject(this);
        }
    }
}

