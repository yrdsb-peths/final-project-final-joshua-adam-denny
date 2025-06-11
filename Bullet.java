/**
 * Projectile that deals damage when in contact with enemy. used by basic tower, sniper, and machine gun
 * 
 * @author Joshua Stevens
 * @version Version 1.3
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
