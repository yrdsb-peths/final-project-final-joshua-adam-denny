public class Bullet extends Projectile {
    public Bullet(Enemy target, int damage, int speed) {
        super(target, damage, speed, "Bullet.png", 20); // 20 is radius
        int actualDamage = damage;
        
    }


    @Override
    protected void onHit() {
        target.takeDamage(damage);
        getWorld().removeObject(this);
    }
}
