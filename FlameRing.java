import greenfoot.*;
import java.util.List;

public class FlameRing extends Actor {
    private int radius;
    private int damage;
    private int burnDuration;
    private boolean applied = false;
    private int lifespan = 10; // frames to stay visible before removal
    private int age = 0;

    public FlameRing(int radius, int damage, int burnDuration) {
        this.radius = radius;
        this.damage = damage;
        this.burnDuration = burnDuration;

        GreenfootImage img = new GreenfootImage(radius * 2, radius * 2);
        img.setColor(new Color(255, 100, 0, 150));
        img.fillOval(0, 0, radius * 2, radius * 2);
        setImage(img);
    }

    public void act() {
        if (!applied && getWorld() != null) {
            applied = true;
            List<Enemy> enemies = getWorld().getObjects(Enemy.class);
            for (Enemy e : enemies) {
                if (distanceTo(e) <= radius) {
                    e.takeDamage(damage);
                    e.applyBurn(new BurnEffect(1, burnDuration, 10)); // You can tweak damage and interval
                }
            }
        }

        // Visual lifespan timer — fade out
        age++;
        getImage().setTransparency(Math.max(0, 200 - age * 20));

        if (age >= lifespan) {
            getWorld().removeObject(this);
        }
    }

    private double distanceTo(Actor other) {
        return Math.hypot(getX() - other.getX(), getY() - other.getY());
    }
}
