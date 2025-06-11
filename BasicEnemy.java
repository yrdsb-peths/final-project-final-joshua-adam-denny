import greenfoot.*;
/**
 * the first enemy that spawns in the game. easiest to kill and has nothing unique about it
 * 
 * @Joshua Stevens
 * @version (a version number or a date)
 */
public class BasicEnemy extends Enemy {

    public BasicEnemy(int speed, int health, int moneyDeath) {
        super(speed, health, moneyDeath);
        GreenfootImage img = new GreenfootImage("man.png");
        img.scale(37, 75);
        setBaseImage(img); // sets baseImage and sets actor image
        if (!isDead) {
            this.movement = new GreenfootSound("lightFootsteps.mp3");
        }
    }
    
    @Override
    public void act() {
        super.act(); // Ensure superclass behavior runs
    }   

}
