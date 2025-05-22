import greenfoot.*;

public abstract class Projectile extends Actor {
    protected Enemy target;
    protected int damage;
    protected int speed;

    public Projectile(Enemy target, int damage, int speed, String imageFile, int scale) {
        this.target = target;
        this.damage = damage;
        this.speed = speed;

        GreenfootImage img = new GreenfootImage(imageFile);
        img.scale(scale, scale);
        setImage(img);
    }

    public void act() {
        if (target != null && getWorld() != null && getWorld().getObjects(Enemy.class).contains(target)) {
            turnTowards(target.getX(), target.getY());
            move(speed);

            if (intersects(target)) {
                onHit();
            }
        } else {
            if (getWorld() != null) {
                getWorld().removeObject(this);
            }
        }
    }

    protected abstract void onHit(); // Each subclass defines what happens on hit
}
