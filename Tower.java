import greenfoot.*;
import java.util.List;

public class Tower extends Actor {
    protected int cooldownTime = 60;
    protected int cooldown = 0;
    protected int range = 100;
    protected int damage = 1;
    protected int bulletSpeed = 5;

    protected int level = 1;         // current upgrade level
    protected int maxLevel = 3;      // max upgrades allowed
    protected int upgradeCost = 50;  // cost to upgrade

    public void act() {
        if (cooldown > 0) {
            cooldown--;
        } else {
            Enemy target = getEnemyInRange();
            if (target != null) {
                shoot(target);
                cooldown = cooldownTime;
            }
        }
    }

    protected Enemy getEnemyInRange() {
        List<Enemy> enemies = getObjectsInRange(range, Enemy.class);
        if (!enemies.isEmpty()) {
            return enemies.get(0);
        }
        return null;
    }

    protected void shoot(Enemy target) {
        getWorld().addObject(new Bullet(target, damage, bulletSpeed), getX(), getY());
    }

    // Upgrade method to improve tower stats
    public boolean upgrade() {
        GameWorld world = (GameWorld)getWorld();
        if (level < maxLevel && world.spendMoney(upgradeCost)) {
            level++;
            damage += 1;
            range += 20;
            cooldownTime = Math.max(20, cooldownTime - 10); // faster shooting
            upgradeCost += 50; // next upgrade costs more
            updateImage();
            return true;
        }
        return false;
    }

    protected void updateImage() {
        // You can override this in subclasses to change image per level
        // Example: setImage("basic_tower_level" + level + ".png");
    }
}
