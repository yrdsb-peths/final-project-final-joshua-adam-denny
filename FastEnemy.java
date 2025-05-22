import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class FastEnemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class FastEnemy extends Enemy {

    public FastEnemy(int speed) {
        super(speed + 2); // faster than base enemy
        this.health = 1;
        setImage("bee2.png");
    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0 && getWorld() != null) {
            ((GameWorld)getWorld()).addMoney(15); // reward for fast enemy
            getWorld().removeObject(this);
        }
    }
    
    @Override
    public int getLifeDamage() {
        return 2;
    }
}


