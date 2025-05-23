import greenfoot.*;
import java.util.List;

public class FlameProjectile extends Projectile {
    private int aoeRadius;
    private int lifeSpan;
    private int towerLevel;

    public FlameProjectile(Enemy target, int damage, int speed, int aoeRadius, int lifeSpan, int towerLevel) {
        super(target, damage, speed, "flame.png", aoeRadius * 2);
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
                
                // Scale burn effect with tower level
                int burnDamage = 1 + towerLevel / 2; // +0 at L1, +1 at L2-3, +2 at L4+
                int totalTicks = 4 + towerLevel;     // more ticks with level
                int interval = Math.max(8, 20 - 2 * towerLevel); // faster ticks with level
                
                e.applyBurn(new BurnEffect(burnDamage, totalTicks, interval));
            }
            getWorld().removeObject(this);
        }
    }
}

