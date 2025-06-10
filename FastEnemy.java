import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * Write a description of class Base here.
 * 
 * @Joshua Stevens
 * @version (a version number or a date)
 */
public class FastEnemy extends Enemy {
    public FastEnemy(int speed, int health, int moneyDeath) {
        super(speed, health, moneyDeath); // faster than base enemy
        GreenfootImage img = new GreenfootImage("bee2.png"); 
        setBaseImage(img);             // IMPORTANT: set base image for burn tint
        setImage(new GreenfootImage(img));  // set the image after scaling
        if (!isDead) {
            this.movement = new GreenfootSound("droneFlying.mp3");
        }
    }

    @Override
    public int getLifeDamage() {
        return 2;
    }
    
    @Override
    public void act() {
        super.act(); // Ensure superclass behavior runs
    }
}
