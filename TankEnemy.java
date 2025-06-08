import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Base here.
 * 
 * @Joshua Stevens
 * @version (a version number or a date)
 */
public class TankEnemy extends Enemy {

    public TankEnemy(int speed, int health, int moneyDeath) {
        super(speed, health, moneyDeath);
        GreenfootImage img = new GreenfootImage("hippo.png");
        img.scale(80, 80);
        setBaseImage(img);
        super.movement = new GreenfootSound("heavyFootsteps.mp3");
    }

    @Override
    public int getLifeDamage() {
        return 5;
    }
    @Override
    public void act() {
        super.act(); // Ensure superclass behavior runs
    }   
}


