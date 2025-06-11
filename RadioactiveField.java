import greenfoot.*;
import java.util.List;
/**
 * Creates a pool/puddle of radioactive energy that deals damage every second while enemies are inside the area
 * 
 * @author Adam Fung
 * @version Version 1.1
 */
public class RadioactiveField extends Actor {
    private int duration;
    private int tickInterval;
    private int tickTimer;
    private int damagePerTick;
    private int radius;

    /**
     * Constructor for RadioactiveField.
     * Initializes the radioactive field with a duration, damage per tick, and radius.
     * 
     * @param duration The duration the field lasts in ticks.
     * @param damagePerTick The amount of damage dealt to enemies every tick.
     * @param radius The radius of the radioactive field.
     */
    public RadioactiveField(int duration, int damagePerTick, int radius) {
        this.duration = duration;
        this.tickInterval = 60;
        this.tickTimer = tickInterval;
        this.damagePerTick = damagePerTick;
        this.radius = radius;

        GreenfootImage img = new GreenfootImage("radioactive.png");
        img.scale(radius, radius);
        img.setTransparency(150);
        setImage(img);
    }

    public void act() {
        if (duration-- <= 0) { //removes pool after a certain amount of time
            getWorld().removeObject(this);
            return;
        }

        if (--tickTimer <= 0) { //deals damage every second
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
