import greenfoot.*;
/**
 * A net sent out by the basic tower when at max level that slows down enemies in an area 
 * 
 * @Joshua Stevens
 * @version (a version number or a date)
 */
public class SlowFieldBullet extends Projectile {
    private int fieldRadius = 100;
    private int fieldSlow = 4;
    private int fieldDuration = 180;

    public SlowFieldBullet(Enemy target, int damage, int speed, Tower tower) {
        super(target, damage, speed, tower, "net.png", 30); // Adjust image name and scale as needed
    }

    @Override
    protected void onHit() {
        dealDamage();

        if (getWorld() != null) {
            getWorld().addObject(
                new SlowField(fieldRadius, fieldSlow, fieldDuration),
                getX(), getY()
            );
            getWorld().removeObject(this);
        }
    }
}
