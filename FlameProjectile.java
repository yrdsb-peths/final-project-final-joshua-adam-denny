import greenfoot.*;
import java.util.List;
/**
 * Write a description of class Base here.
 * 
 * @Joshua Stevens
 * @version (a version number or a date)
 */
public class FlameProjectile extends Projectile {
    private int aoeRadius;
    private int lifeSpan;
    private int towerLevel;

    public FlameProjectile(Enemy target, int damage, int speed, int aoeRadius, int lifeSpan, int towerLevel, Tower sourceTower) {
        super(target, damage, speed, sourceTower, "flame.png", aoeRadius * 2);
        this.aoeRadius = aoeRadius;
        this.lifeSpan = lifeSpan;
        this.towerLevel = towerLevel;
    }

    @Override
    public void act() {
        lifeSpan--;
        if (lifeSpan <= 0 && getWorld() != null) {
            getWorld().removeObject(this);
            return;
        }

        move(speed);

        List<Enemy> enemies = getObjectsInRange(aoeRadius, Enemy.class);
        for (Enemy e : enemies) {
            if (this.intersects(e)) {
                onHit();
                return;
            }
        }

        if (isAtEdge() && getWorld() != null) {
            getWorld().removeObject(this);
        }
    }

    @Override
    protected void onHit() {
        if (getWorld() != null) {
            List<Enemy> enemies = getObjectsInRange(aoeRadius, Enemy.class);
            for (Enemy e : enemies) {
                e.takeDamage(damage);
                if (sourceTower != null) sourceTower.addDamage(damage); // Track damage
                

                int burnDamage = 1 + towerLevel / 2;
                int totalTicks = 4 + towerLevel;
                int interval = Math.max(8, 20 - 2 * towerLevel);

                e.applyBurn(new BurnEffect(burnDamage, totalTicks, interval));
            }
            getWorld().removeObject(this);
        }
    }
}
