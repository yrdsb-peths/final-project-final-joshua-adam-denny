import greenfoot.*;
import java.util.List;
/**
 * FlameRing Class is an effect that creates a ring of fire around the actor that applies damage and burn effects to enemies within a certain radius.
 * 
 * @author Joshua Stevens
 * @version Version 1.0
 */
public class FlameRing extends Actor {
    private int radius;
    private int damage;
    private int burnDuration;
    private boolean effectApplied = false;

    private GreenfootImage[] frames = new GreenfootImage[9];
    private int currentFrame = 0;
    private int frameDelay = 3;
    private int frameCounter = 0;


    /**
     * Constructor for FlameRing.
     * Initializes the ring with a specified radius, damage, and burn duration.
     * Loads the animation frames for the ring effect.
     * 
     * @param radius The radius of the flame ring.
     * @param damage The damage dealt to enemies within the ring.
     * @param burnDuration The duration of the burn effect applied to enemies.
     */
    public FlameRing(int radius, int damage, int burnDuration) {
        this.radius = radius;
        this.damage = damage;
        this.burnDuration = burnDuration;
        loadFrames();
        setImage(frames[0]);
    }

    private void loadFrames() {
        for (int i = 0; i < frames.length; i++) {
            GreenfootImage img = new GreenfootImage("Fire ring/Fire" + i + ".png");
            img.scale(radius * 2, radius * 2);
            frames[i] = img;
        }
    }

    public void act() {
        if (!effectApplied && getWorld() != null) {
            effectApplied = true;
            List<Enemy> enemies = getWorld().getObjects(Enemy.class);
            for (Enemy e : enemies) {
                if (distanceTo(e) <= radius) {
                    e.takeDamage(damage);
                    e.applyBurn(new BurnEffect(1, burnDuration, 10));
                }
            }
        }

        animate();
    }

    private void animate() {
        frameCounter++;
        if (frameCounter >= frameDelay) {
            frameCounter = 0;
            currentFrame++;
            if (currentFrame < frames.length) {
                setImage(frames[currentFrame]);
            } else {
                getWorld().removeObject(this);
            }
        }
    }

    private double distanceTo(Actor other) {
        return Math.hypot(getX() - other.getX(), getY() - other.getY());
    }
}
