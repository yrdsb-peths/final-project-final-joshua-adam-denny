import greenfoot.*;
import java.util.List;

public class SniperTower extends Tower {
    public SniperTower() {
        GreenfootImage img = new GreenfootImage("sniper_tower.png");  // Make sure this file exists
        img.scale(50, 50);  // Optional: adjust size
        setImage(img);      // ✅ SET the image on the actor

        cooldownTime = 120; // slower fire rate (2 seconds)
        range = 1000;        // longer range
        damage = 10;         // more damage per bullet
        bulletSpeed = 15;
    }

    @Override
    protected Enemy getEnemyInRange() {
        List<Enemy> enemies = getObjectsInRange(range, Enemy.class);
        Enemy farthest = null;
        int maxX = -1;
        for (Enemy e : enemies) {
            if (e.getX() > maxX) {
                maxX = e.getX();
                farthest = e;
            }
        }
        return farthest;
    }

    @Override
    protected void shoot(Enemy target) {
        getWorld().addObject(new Bullet(target, damage, bulletSpeed), getX(), getY());
    }
}
