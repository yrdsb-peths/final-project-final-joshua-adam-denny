import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class TankEnemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TankEnemy extends Enemy {

    public TankEnemy(int speed, int health) {
        super(speed, health);
        GreenfootImage img = new GreenfootImage("hippo.png");
        img.scale(80, 80);
        setBaseImage(img);
    }


    @Override
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0 && getWorld() != null) {
            ((GameWorld)getWorld()).addMoney(50); // reward for tank enemy
            getWorld().removeObject(this);
        }
    }
    @Override
    public int getLifeDamage() {
        return 5;
    }
}


