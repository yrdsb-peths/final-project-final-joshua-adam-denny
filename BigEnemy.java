import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class BigEnemy here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class BigEnemy extends Enemy {

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
}


