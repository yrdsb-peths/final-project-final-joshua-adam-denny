import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BigEnemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BigEnemy extends Enemy {

    public BigEnemy(int speed, int health) {
        super(speed, health);
        GreenfootImage img = new GreenfootImage("teddybear.png");
        img.scale(80, 80); // or scale appropriately to your design
        setBaseImage(img);
    }


    @Override
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0 && getWorld() != null) {
            ((GameWorld)getWorld()).addMoney(100); // higher reward
            getWorld().removeObject(this);
        }
    }
    
    @Override
    public int getLifeDamage() {
        return 10;
    }
}


