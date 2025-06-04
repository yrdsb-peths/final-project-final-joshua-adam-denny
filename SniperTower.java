import greenfoot.*;
import java.util.List;

public class SniperTower extends Tower {
    public SniperTower() {
        GameWorld world = (GameWorld) getWorld();
        GreenfootImage img = new GreenfootImage("Sniper_tower.png");
        img.scale(60, 60);
        setImage(img);

        cooldownTime = 120;  // slower fire rate
        range = 1000;        // long range
        damage = 20;         // high damage
        bulletSpeed = 30;
        baseCost = 300;
        upgradeCostPerLevel = 150;
        
        upgradeCost = upgradeCostPerLevel;  
        totalInvested = baseCost;

        if (world != null && world.isSniperBoostActive()) {
            damage *= 2; // Double the damage if the boost is active
        }
        
        if (level == maxLevel && world != null) {
            world.unlockSniperAbility();
        }
    }

    @Override
    public boolean upgrade() {
        GameWorld world = (GameWorld) getWorld();
        if (level < maxLevel && world != null && world.spendMoney(upgradeCost)) {
            level++;
            damage += 10;                    // boost damage significantly
            cooldownTime = Math.max(60, cooldownTime - 30); // slightly faster
            totalInvested += upgradeCost;
            upgradeCost += 100;            // higher cost per upgrade
            updateImage();
            
            if (level == maxLevel && world != null) {
                world.unlockSniperAbility();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void updateImage() {
        GreenfootImage img = new GreenfootImage("Sniper_tower.png");
        img.scale(60, 60);
        setImage(img);
    
        super.updateImage();  // This will add the outline based on level
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
}
