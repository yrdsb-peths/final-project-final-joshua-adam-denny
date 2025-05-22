public class Bullet extends Projectile {
    public Bullet(Enemy target) {
        super(target, 10, 5, "Bullet.png", 20); // Example values
    }

    @Override
    protected void onHit() {
        target.takeDamage(damage);
        getWorld().removeObject(this);
    }
}
