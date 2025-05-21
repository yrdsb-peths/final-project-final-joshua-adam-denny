import greenfoot.*;

public class BasicTower extends Tower {
    public BasicTower() {
        GreenfootImage img = new GreenfootImage("basic_tower.png");
        //img.scale(50, 50);  // scale to 50x50 pixels (adjust as needed)
        setImage(img);
        
        cooldownTime = 60;             
        range = 100;                   
        damage = 1;                   
        bulletSpeed = 5;
    }


    @Override
    protected Enemy getEnemyInRange() {
        // You can customize targeting behavior here
        return super.getEnemyInRange();
    }

    @Override
    protected void shoot(Enemy target) {
        getWorld().addObject(new Bullet(target, damage, bulletSpeed), getX(), getY());
    }
}
