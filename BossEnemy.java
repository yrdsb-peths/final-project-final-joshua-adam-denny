import greenfoot.*;

public class BossEnemy extends Enemy {
    public BossEnemy(int speed, int health) {
        super(speed, health);
        GreenfootImage img = new GreenfootImage(100, 200);
        img.setColor(Color.MAGENTA);
        img.fillOval(0, 0, 100, 200);
        setImage(img);
    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0 && getWorld() != null) {
            ((GameWorld)getWorld()).addMoney(1000); 
            getWorld().removeObject(this);
        }
    }
    
    @Override
    public int getLifeDamage() {
        return 100;
    }
}
