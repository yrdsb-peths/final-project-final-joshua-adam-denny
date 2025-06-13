import greenfoot.*;

/**
 * A net sent out by the basic tower when at max level that slows down enemies in an area 
 * 
 * @author Joshua Stevens
 * @version Version 1.0
 */
public class SlowFieldBullet extends Projectile {
    private int fieldRadius = 100;
    private int fieldSlow = 4;
    private int fieldDuration = 180;

    private int targetX;
    private int targetY;

    public SlowFieldBullet(Enemy target, int damage, int speed, Tower tower) {
        super(target, damage, speed, tower, "net.png", 30);

        if (target != null) {
            targetX = target.getX();
            targetY = target.getY();
        } else {
            targetX = getX();
            targetY = getY();
        }
    }

    @Override
    public void act() {
        if (getWorld() == null) return;

        // Move toward last known target position
        turnTowards(targetX, targetY);
        move(speed);

        // If close enough to drop the field, do so
        if (Math.hypot(getX() - targetX, getY() - targetY) <= speed + 2) {
            onHit();
        }
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
