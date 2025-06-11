import greenfoot.*;
import java.util.List;
/**
 * missile utilized by the nuke tower and machine gun tower(only at level 3) that does splash damage to enemies around where it explodes
 * 
 * @author Joshua Stevens
 * @version Version 1.0
 */
public class NukeMissile extends Projectile {
    private boolean exploded = false;
    private int explosionRadius;
    private int lifetime = 60; // frames before explosion
    private Tower sourceTower;
    private boolean lostTarget = false;
    private int level;
    private int projectileType; 


    /**
     * Constructor for NukeMissile.
     * Initializes the missile with target, damage, speed, explosion radius, source tower, scale, fuse time, level, and projectile type.
     * 
     * @param target The enemy that this missile will target.
     * @param damage The damage dealt by the missile.
     * @param speed The speed of the missile.
     * @param radius The explosion radius of the missile.
     * @param sourceTower The tower that fired this missile.
     * @param scale The scale of the missile image.
     * @param fuseTime The time before the missile explodes.
     * @param level The upgrade level of the tower that fired this missile.
     * @param projectileType The type of projectile (1 for small explosion, 2 for large explosion).
     */
    public NukeMissile(Enemy target, int damage, int speed, int radius, Tower sourceTower, int scale, int fuseTime, int level, int projectileType)  {
        
        super(target, damage, speed, sourceTower, level >= 3 ? "NukeMissile2.png" : "NukeMissile.png", scale);
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
        
        if (level >= 3) {
            int fieldDuration = 300; // 5 seconds
            int dotDamage = 25;
            getWorld().addObject(new RadioactiveField(fieldDuration, dotDamage, explosionRadius), getX(), getY());
        }
        if (projectileType == 1) {
            AudioManager.playSFX(new GreenfootSound("explosionSmall.mp3"));
        }
        else if (projectileType == 2) 
        {
            AudioManager.playSpecialSFX(new GreenfootSound("explosionBig.mp3"));
        }
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
