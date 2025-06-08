import greenfoot.*;
import java.util.List;
/**
 * Write a description of class Base here.
 * 
 * @Adam Fung
 * @version (a version number or a date)
 */
public class RadioactiveField extends Actor {
    private int duration;
    private int tickInterval;
    private int tickTimer;
    private int damagePerTick;
    private int radius;

    public RadioactiveField(int duration, int damagePerTick, int radius) {
        this.duration = duration;
        this.tickInterval = 60;
        this.tickTimer = tickInterval;
        this.damagePerTick = damagePerTick;
        this.radius = radius;

        GreenfootImage img = new GreenfootImage(radius * 2, radius * 2);
        img.setColor(new Color(0, 255, 0, 80));
        img.fillOval(0, 0, radius * 2, radius * 2);
        setImage(img);
    }

    public void act() {
        if (duration-- <= 0) {
            getWorld().removeObject(this);
            return;
        }

        if (--tickTimer <= 0) {
            applyDamage();
            tickTimer = tickInterval;
        }
    }

    private void applyDamage() {
        List<Enemy> enemies = getWorld().getObjects(Enemy.class);
        for (Enemy e : enemies) {
            if (distanceTo(e) <= radius) {
                e.takeDamage(damagePerTick);
            }
        }
    }

    private double distanceTo(Actor a) {
        return Math.hypot(getX() - a.getX(), getY() - a.getY());
    }
}
