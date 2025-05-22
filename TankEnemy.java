import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class TankEnemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TankEnemy extends Enemy {

    public TankEnemy(int speed) {
        super(speed - 1); // slower but tanky
        this.health = 5;
        //setImage("tank-enemy.png");
    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0 && getWorld() != null) {
            ((GameWorld)getWorld()).addMoney(30); // reward for tank enemy
            getWorld().removeObject(this);
        }
    }
    @Override
    public int getLifeDamage() {
        return 5;
    }
}


