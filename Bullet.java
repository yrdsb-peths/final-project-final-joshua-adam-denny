import greenfoot.*;

public class Bullet extends Actor {
    private Enemy target;
    private int damage;
    private int speed;

    public Bullet(Enemy target, int damage, int speed) {
        this.target = target;
        this.damage = damage;
        this.speed = speed;
    }

    public void act() {
        if (target != null && getWorld().getObjects(Enemy.class).contains(target)) {
            turnTowards(target.getX(), target.getY());
            move(speed);

            if (intersects(target)) {
                target.takeDamage(damage);
                getWorld().removeObject(this);
            }
        } else {
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
        }
    }
}
