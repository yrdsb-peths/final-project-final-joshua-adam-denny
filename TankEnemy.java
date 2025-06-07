import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class TankEnemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class TankEnemy extends Enemy {
    private GreenfootSound walk = new GreenfootSound("heavyFootsteps.mp3");

    public TankEnemy(int speed, int health, int moneyDeath) {
        super(speed, health, moneyDeath);
        GreenfootImage img = new GreenfootImage("hippo.png");
        img.scale(80, 80);
        setBaseImage(img);
    }

    @Override
    public int getLifeDamage() {
        return 5;
    }
    @Override
    public void act() {
        super.act(); // Ensure superclass behavior runs
        if (isDead == false) {
            walk.setVolume(60);
            walk.playLoop();
        }
        else if (isDead == true) 
        {
            walk.stop();
        }
    }   
}


