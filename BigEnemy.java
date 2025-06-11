import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 4th enemy that spawns in the game, has the 2nd highest hp in the game, but a bit slower
 * 
 * @Joshua Stevens
 * @version (a version number or a date)
 */
public class BigEnemy extends Enemy {

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


