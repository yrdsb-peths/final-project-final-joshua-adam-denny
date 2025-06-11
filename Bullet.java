/**
 * Projectile that deals damage when in contact with enemy. used by basic tower, sniper, and machine gun
 * 
 * @author Joshua Stevens
 * @version Version 1.3
 */
public class Bullet extends Projectile {
    /**
     * Constructor for Bullet.
     * Initializes the bullet with a target enemy, damage, speed, and source tower.
     *
     * @param target The enemy that this bullet will target.
     * @param damage The damage this bullet will deal to the target.
     * @param speed The speed of the bullet.
     * @param source The tower that fired this bullet.
     */
    public Bullet(Enemy target, int damage, int speed, Tower source) {
        super(target, damage, speed, source, "images/Bullet.png", 16);
    }

    @Override
    protected void onHit() {
        dealDamage();
        getWorld().removeObject(this);
    }
}
