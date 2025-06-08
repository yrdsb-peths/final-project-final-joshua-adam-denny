/**
 * Write a description of class Base here.
 * 
 * @Joshua Stevens
 * @version (a version number or a date)
 */
public class Bullet extends Projectile {
    public Bullet(Enemy target, int damage, int speed, Tower source) {
        super(target, damage, speed, source, "images/Bullet.png", 16);
    }

    @Override
    protected void onHit() {
        dealDamage();
        getWorld().removeObject(this);
    }
}
