import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * 3rd enemy that spawns in the game, has higher Hp
 * 
 * @author Joshua Stevens
 * @version Version 1.0a version number or a date)
 */
public class TankEnemy extends Enemy {

    public TankEnemy(int speed, int health, int moneyDeath) {
        super(speed, health, moneyDeath);
        GreenfootImage img = new GreenfootImage("hippo.png");
        img.scale(80, 80);
        setBaseImage(img);
        if (!isDead) {
            this.movement = new GreenfootSound("heavyFootsteps.mp3");
        }
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


