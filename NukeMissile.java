import greenfoot.*;
import java.util.List;

public class NukeMissile extends Projectile {
    private boolean exploded = false;
    private int explosionRadius;
    private int lifetime = 60; // frames before explosion
    private Tower sourceTower;
    private boolean lostTarget = false;
    private int level;
    private int projectileType; 

    public NukeMissile(Enemy target, int damage, int speed, int radius, Tower sourceTower, int scale, int fuseTime, int level, int projectileType)  {
        
        super(target, damage, speed, sourceTower, level >= 2 ? "NukeMissile2.png" : "NukeMissile.png", scale);
        this.explosionRadius = radius;
        this.sourceTower = sourceTower;
        this.lifetime = fuseTime;
        this.level = level;
        this.projectileType = projectileType;

    }


    @Override
    public void act() {
        if (exploded || getWorld() == null) return;
    
        if (lifetime <= 0) {
            explode();
            return;
        }
    
        // Target still exists and is in world
        if (!lostTarget && target != null && getWorld().getObjects(Enemy.class).contains(target)) {
            turnTowards(target.getX(), target.getY());
        } else {
            if (!lostTarget) {
                lostTarget = true;
                // If it's projectile type 2, explode immediately upon losing target
                if (projectileType == 2) {
                    explode();
                    return;
                }
            }
        }
    
        move(speed);
        lifetime--;
    
        // If target exists and is close, explode
        if (!lostTarget && target != null && getWorld().getObjects(Enemy.class).contains(target)) {
            if (distanceTo(target) < 10) {
                explode();
                return;
            }
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
