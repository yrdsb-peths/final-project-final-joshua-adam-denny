import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class FastEnemy extends Enemy {
    private GreenfootSound drone = new GreenfootSound("droneFlying.mp3");
    public FastEnemy(int speed, int health, int moneyDeath) {
        super(speed, health, moneyDeath); // faster than base enemy
        
        GreenfootImage img = new GreenfootImage("bee2.png"); 
        setBaseImage(img);             // IMPORTANT: set base image for burn tint
        
        setImage(new GreenfootImage(img));  // set the image after scaling
    }

    @Override
    public int getLifeDamage() {
        return 2;
    }
    
    @Override
    public void act() {
        super.act(); // Ensure superclass behavior runs
        if (isDead == false) {
            drone.playLoop();
        }
        else if (isDead == true) 
        {
            drone.stop();
        }
    }   
}
