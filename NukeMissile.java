import greenfoot.*;
import java.util.List;

public class NukeMissile extends Projectile {
    private int blastRadius = 150; // Adjust as needed for size

    public NukeMissile(Enemy target) {
        super(target, 500, 2, "Nuke_Missile.png", 40); // Large, slow missile
    }

    @Override
    protected void onHit() {
        List<Enemy> enemies = getObjectsInRange(blastRadius, Enemy.class);
        for (Enemy e : enemies) {
            e.takeDamage(damage);
        }

        // Optional: play explosion sound or animation here
        getWorld().removeObject(this);
    }
}
