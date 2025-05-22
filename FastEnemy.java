import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

public class FastEnemy extends Enemy {

    public FastEnemy(int speed, int health) {
        super(speed, health); // faster than base enemy
        
        GreenfootImage img = new GreenfootImage("bee2.png");
        img.scale(80, 80);             // scale to your desired size
        setBaseImage(img);             // IMPORTANT: set base image for burn tint
        
        setImage(new GreenfootImage(img));  // set the image after scaling
    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0 && getWorld() != null) {
            ((GameWorld)getWorld()).addMoney(20); // reward for fast enemy
            getWorld().removeObject(this);
        }
    }

    @Override
    public int getLifeDamage() {
        return 2;
    }
}
