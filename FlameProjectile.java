import greenfoot.*;
import java.util.List;

public class FlameProjectile extends Projectile {
    private int aoeRadius;
    private int lifeSpan;  // new field to track remaining life in frames

    public FlameProjectile(Enemy target, int damage, int speed, int aoeRadius, int lifeSpan) {
        super(target, damage, speed, "flame.png", aoeRadius * 2);
        this.aoeRadius = aoeRadius;
        this.lifeSpan = lifeSpan;  // set lifespan here
    }

    @Override
    public void act() {
        // Reduce lifespan every act
        lifeSpan--;
        if (lifeSpan <= 0) {
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
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

        if (isAtEdge()) {
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
        }
    }

    @Override
    protected void onHit() {
        if (getWorld() != null) {
            List<Enemy> enemies = getObjectsInRange(aoeRadius, Enemy.class);
            for (Enemy e : enemies) {
                e.takeDamage(damage);
                e.applyBurn(new BurnEffect(1, 5, 20));
            }
            getWorld().removeObject(this);
        }
    }
}
