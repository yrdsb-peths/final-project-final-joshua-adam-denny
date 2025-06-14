import greenfoot.*;
import java.util.List;
/**
 * Fire projectile utilized by the flamethrower tower. applies burn when an enemy is hit by it
 * 
 * @author Joshua Stevens
 * @version Version 1.1
 */
public class FlameProjectile extends Projectile {
    private int aoeRadius;
    private int lifeSpan;
    private int towerLevel;


    /**
     * Constructor for FlameProjectile.
     * Initializes the projectile with a target enemy, damage, speed, area of effect radius, lifespan, tower level, and source tower.
     * 
     * @param target The enemy that this projectile will target.
     * @param damage The damage dealt by this projectile.
     * @param speed The speed of the projectile.
     * @param aoeRadius The radius of the area of effect for this projectile.
     * @param lifeSpan The lifespan of the projectile in ticks.
     * @param towerLevel The level of the tower that fired this projectile.
     * @param sourceTower The tower that fired this projectile.
     */
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
                

                int burnDamage = 1 + towerLevel;
                int totalTicks = 4 + towerLevel;
                int interval = Math.max(8, 20 - 2 * towerLevel);

                e.applyBurn(new BurnEffect(burnDamage, totalTicks, interval));
            }
            getWorld().removeObject(this);
        }
    }
}
