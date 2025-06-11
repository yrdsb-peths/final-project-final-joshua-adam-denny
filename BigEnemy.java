import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 4th enemy that spawns in the game, has the 2nd highest hp in the game, but a bit slower
 * 
 * @author Joshua Stevens
 * @version Version 1.0
 */
public class BigEnemy extends Enemy {

    /**
     * Constructor for BigEnemy.
     * Initializes the enemy with specified speed, health, and money upon death.
     * Sets the image for the enemy and initializes sound if not dead.
     * 
     * @param speed The speed of the enemy.
     * @param health The health of the enemy.
     * @param moneyDeath The amount of money given upon the enemy's death.
     */
    public BigEnemy(int speed, int health, int moneyDeath) {
        super(speed, health, moneyDeath);
        GreenfootImage img = new GreenfootImage("teddybear.png");
        img.scale(80, 80); // or scale appropriately to your design
        setBaseImage(img);
        if (!isDead) {
            this.movement = new GreenfootSound("jeepDriving.mp3");
        }
    }

    
    @Override
    public int getLifeDamage() {
        return 10;
    }
    @Override
    public void act() {
        super.act(); // Ensure superclass behavior runs
    }   
}


