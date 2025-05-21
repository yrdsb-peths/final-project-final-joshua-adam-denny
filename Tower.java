import greenfoot.*;
import java.util.List;
  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Write a description of class Tower here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Tower extends Actor {
    private int cooldown = 0;

    public void act() {
        if (cooldown > 0) {
            cooldown--;
        } else {
            Enemy target = getEnemyInRange();
            if (target != null) {
                getWorld().addObject(new Bullet(target), getX(), getY());
                cooldown = 60; // 1 second cooldown at 60 FPS
            }
        }
    }

    private Enemy getEnemyInRange() {
        List<Enemy> enemies = getObjectsInRange(100, Enemy.class);
        if (!enemies.isEmpty()) {
            return enemies.get(0);
        }
        return null;
    }
}

