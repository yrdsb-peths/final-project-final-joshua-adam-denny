import greenfoot.*;
import java.util.List;

public class FlameThrowerTower extends Tower {
    private int coneProjectiles = 3;  // Start with 3 projectiles
    private final int maxProjectiles = 6;  // Max projectiles at max upgrade
    private int coneAngle = 60;       // Total cone angle in degrees (spread)
    
    public FlameThrowerTower() {
        GreenfootImage img = new GreenfootImage("FlameThrower_tower.png");
        img.scale(80, 80);
        setImage(img);

        cooldownTime = 7;    // Much faster firing rate
        range = 100;         // Shorter range for flame thrower
        damage = 1;
        bulletSpeed = 15;    // Speed of flame projectiles

        baseCost = 4500;
        upgradeCostPerLevel = 900;
        upgradeCost = upgradeCostPerLevel;
    }

    @Override
    public boolean upgrade() {
        GameWorld world = (GameWorld) getWorld();
        if (level < maxLevel && world.spendMoney(upgradeCost)) {
            level++;
            damage = Math.max(3, damage + 1);
            cooldownTime = Math.max(3, cooldownTime - 1);  // Faster firing with upgrades
            // Increase coneProjectiles gradually up to maxProjectiles
            if (coneProjectiles < maxProjectiles) {
                coneProjectiles++;
            }

            upgradeCost += 300;
            updateImage();
            return true;
        }
        return false;
    }

    @Override
    protected void updateImage() {
        GreenfootImage img = new GreenfootImage("FlameThrower_tower.png");
        img.scale(80, 80);
        setImage(img);
        super.updateImage();
    }

    @Override
    protected Enemy getEnemyInRange() {
        return super.getEnemyInRange();
    }

    @Override
    protected void shoot(Enemy target) {
        if (target == null) return;

        double baseAngle = Math.atan2(target.getY() - getY(), target.getX() - getX()) * 180 / Math.PI;

        for (int i = 0; i < coneProjectiles; i++) {
            double offset = ((double)i / (coneProjectiles - 1) - 0.5) * coneAngle;
            double projectileAngle = baseAngle + offset;

            FlameProjectile fp = new FlameProjectile(target, damage, bulletSpeed, 50, 20, level);
            fp.setRotation((int) projectileAngle);

            getWorld().addObject(fp, getX(), getY());
        }
    }

    @Override
    public void sell() {
        GameWorld world = (GameWorld)getWorld();
        world.addMoney(totalInvested / 2);
        world.removeObject(this);
    }
}
