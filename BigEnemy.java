import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BigEnemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BigEnemy extends Enemy {

    public BigEnemy(int speed) {
        super(speed);
        this.health = 20; // big enemy has more health
        //setImage("big_enemy.png"); // make sure to use a bigger image if you have one
    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0 && getWorld() != null) {
            ((GameWorld)getWorld()).addMoney(50); // higher reward
            getWorld().removeObject(this);
        }
    }
    
    @Override
    public int getLifeDamage() {
        return 10;
    }
}


