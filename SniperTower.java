import greenfoot.*;
import java.util.List;

public class SniperTower extends Tower {
    public SniperTower() {
        GreenfootImage img = new GreenfootImage("Sniper_tower.png");
        img.scale(50, 50);
        setImage(img);

        cooldownTime = 120;  // slower fire rate
        range = 1000;        // long range
        damage = 10;         // high damage
        bulletSpeed = 15;
        baseCost = 300;
        upgradeCostPerLevel = 150;

        upgradeCost = upgradeCostPerLevel;   // more expensive to upgrade
    }

    @Override
    public boolean upgrade() {
        GameWorld world = (GameWorld) getWorld();
        if (level < maxLevel && world.spendMoney(upgradeCost)) {
            level++;
            damage += 5;                    // boost damage significantly
            cooldownTime = Math.max(60, cooldownTime - 20); // slightly faster
            totalInvested += upgradeCost;
            upgradeCost += 100;            // higher cost per upgrade
            updateImage();
            return true;
        }
        return false;
    }

    @Override
    protected void updateImage() {
        //GreenfootImage img = new GreenfootImage("sniper_tower_level" + level + ".png");
        GreenfootImage img = new GreenfootImage("Sniper_tower.png");

        img.scale(50, 50);
        setImage(img);
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

    /*@Override
    protected void shoot(Enemy target) {
        getWorld().addObject(new Bullet(target, damage, bulletSpeed), getX(), getY());
    }
    
    public void sell() {

    }*/
}
