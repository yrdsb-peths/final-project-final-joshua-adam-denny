import greenfoot.*;
/**
 * A net sent out by the basic tower when at max level that slows down enemies in an area 
 * 
 * @author Joshua Stevens
 * @version Version 1.0a version number or a date)
 */
public class SlowFieldBullet extends Projectile {
    private int fieldRadius = 100;
    private int fieldSlow = 4;
    private int fieldDuration = 180;

    /**
     * Constructor for SlowFieldBullet.
     * Initializes the bullet with a target enemy, damage, speed, and tower reference.
     *
     * @param target The enemy that this bullet will target.
     * @param damage The damage this bullet will deal to the target.
     * @param speed The speed of the bullet.
     * @param tower The tower that fired this bullet.
     */
    public SlowFieldBullet(Enemy target, int damage, int speed, Tower tower) {
        super(target, damage, speed, tower, "net.png", 30); // Adjust image name and scale as needed
    }

    @Override
    protected void onHit() {
        dealDamage();

        // Create a slow field at the bullet's current position
        // and remove the bullet from the world
        if (getWorld() != null) {
            getWorld().addObject(
                new SlowField(fieldRadius, fieldSlow, fieldDuration),
                getX(), getY()
            );
            getWorld().removeObject(this);
        }
    }
}
