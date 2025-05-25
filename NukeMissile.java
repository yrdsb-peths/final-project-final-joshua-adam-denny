import greenfoot.*;
import java.util.List;

public class NukeMissile extends Projectile {
    private boolean exploded = false;
    private int explosionRadius = 300;

    public NukeMissile(Enemy target, int damage, int speed, int radius) {
        super(target, damage, speed, "NukeMissile.png", 100);
        this.explosionRadius = radius;
    }


    @Override
    public void act() {
        if (exploded) return;
    
        if (target != null && getWorld().getObjects(Enemy.class).contains(target)) {
            turnTowards(target.getX(), target.getY());
            move(speed);
    
            if (distanceTo(target) < 10) {
                onHit(); // this will call explode()
            }
        } else {
            onHit(); // also explode if target is missing
        }
    }


    private void explode() {
        exploded = true;
        List<Enemy> enemies = getWorld().getObjects(Enemy.class);
        for (Enemy enemy : enemies) {
            if (distanceTo(enemy) <= explosionRadius) {
                enemy.takeDamage(damage);
            }
        }

        getWorld().addObject(new ExplosionEffect(explosionRadius), getX(), getY()); // Optional: explosion visual
        getWorld().removeObject(this);
    }

    @Override
    protected void onHit() {
        explode(); // Your custom explosion logic
    }

    
    private double distanceTo(Actor other) {
        return Math.hypot(getX() - other.getX(), getY() - other.getY());
    }
}
