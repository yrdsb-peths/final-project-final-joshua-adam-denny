import greenfoot.*;

public class BasicEnemy extends Enemy {
    private GreenfootSound walk = new GreenfootSound("lightFootsteps.mp3");

    public BasicEnemy(int speed, int health, int moneyDeath) {
        super(speed, health, moneyDeath);
        GreenfootImage img = new GreenfootImage("man.png");
        img.scale(37, 75);
        setBaseImage(img); // sets baseImage and sets actor image
    }
    
    @Override
    public void act() {
        super.act(); // Ensure superclass behavior runs
        if (isDead == false) {
            walk.playLoop();
        }
        else if (isDead == true) 
        {
            walk.stop();
        }
    }   

}
