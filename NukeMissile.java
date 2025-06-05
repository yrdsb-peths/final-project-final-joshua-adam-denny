import greenfoot.*;
import java.util.List;

public class NukeMissile extends Projectile {
    private boolean exploded = false;
    private int explosionRadius;
    private int lifetime = 60; // frames before explosion
    private Tower sourceTower;
    private boolean lostTarget = false;

    public NukeMissile(Enemy target, int damage, int speed, int radius, Tower sourceTower, int scale, int fuseTime) {
        super(target, damage, speed, sourceTower, "NukeMissile.png", scale);
        this.explosionRadius = radius;
        this.sourceTower = sourceTower;
        this.lifetime = fuseTime;
    }


    @Override
    public void act() {
        if (exploded || getWorld() == null) return;

        // Explode after lifetime
        if (lifetime <= 0) {
            explode();
            return;
        }

        // If target still exists and is in world
        if (!lostTarget && target != null && getWorld().getObjects(Enemy.class).contains(target)) {
            turnTowards(target.getX(), target.getY());
        } else {
            lostTarget = true; // stop tracking target
        }

        move(speed);
        lifetime--;

        // If target is still alive and within range, detonate on impact
        if (!lostTarget && target != null && distanceTo(target) < 10) {
            explode();
        }
    }

    private void explode() {
        if (exploded || getWorld() == null) return;
        exploded = true;

        List<Enemy> enemies = getWorld().getObjects(Enemy.class);
        for (Enemy e : enemies) {
            if (distanceTo(e) <= explosionRadius) {
                e.takeDamage(damage);
                if (sourceTower != null) sourceTower.addDamage(damage);
            }
        }

        getWorld().addObject(new ExplosionEffect(explosionRadius), getX(), getY());
        getWorld().removeObject(this);
    }

    private double distanceTo(Actor other) {
        return Math.hypot(getX() - other.getX(), getY() - other.getY());
    }

    @Override
    protected void onHit() {
        explode();
    }
}
