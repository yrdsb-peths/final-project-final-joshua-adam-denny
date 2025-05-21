import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Enemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Enemy extends Actor {
    protected int speed;
    protected int health;

    public Enemy(int speed) {
        this.speed = speed;
        this.health = 1; // basic enemy has 1 health
    }

    public int getSpeed() {
        return speed;
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0 && getWorld() != null) {
            ((GameWorld)getWorld()).addMoney(10); // reward for regular enemy
            getWorld().removeObject(this);
        }
    }

    public void act() {
        move(speed);
        if (getX() >= getWorld().getWidth()) {
            getWorld().removeObject(this);
        }
    }
}


