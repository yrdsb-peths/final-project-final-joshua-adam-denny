import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class FastEnemy extends Enemy {

    public FastEnemy(int speed, int health, int moneyDeath) {
        super(speed, health, moneyDeath); // faster than base enemy
        
        GreenfootImage img = new GreenfootImage("bee2.png");
        img.scale(80, 80);             // scale to your desired size
        setBaseImage(img);             // IMPORTANT: set base image for burn tint
        
        setImage(new GreenfootImage(img));  // set the image after scaling
    }

    @Override
    public int getLifeDamage() {
        return 2;
    }
}
