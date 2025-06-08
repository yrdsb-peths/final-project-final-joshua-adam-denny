import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Base here.
 * 
 * @Joshua Stevens
 * @version (a version number or a date)
 */
public class BigEnemy extends Enemy {
    private GreenfootSound drive = new GreenfootSound("jeepDriving.mp3");

    public BigEnemy(int speed, int health, int moneyDeath) {
        super(speed, health, moneyDeath);
        GreenfootImage img = new GreenfootImage("teddybear.png");
        img.scale(80, 80); // or scale appropriately to your design
        setBaseImage(img);
    }

    
    @Override
    public int getLifeDamage() {
        return 10;
    }
    @Override
    public void act() {
        super.act(); // Ensure superclass behavior runs
        if (isDead == false) {
            drive.playLoop();
        }
        else if (isDead == true) 
        {
            drive.stop();
        }
    }   
}


