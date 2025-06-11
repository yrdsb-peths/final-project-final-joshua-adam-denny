import greenfoot.*;
/**
 * Projectile Class is an abstract class that represents a projectile in the game.
 * Damages enemies upon impact and tracks damage dealt by the source tower.
 * 
 * @author Joshua Stevens
 * @version Version 1.2
 */
public abstract class Projectile extends Actor {
    protected Enemy target;
    protected int damage;
    protected int speed;
    protected Tower sourceTower; // make it protected if subclasses might need it

    public Projectile(Enemy target, int damage, int speed, Tower tower, String imageFile, int scale) {
        this.target = target;
        this.damage = damage;
        this.speed = speed;
        this.sourceTower = tower; // FIXED: tower is now a constructor parameter

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

    protected void dealDamage() {
        if (target != null) {
            target.takeDamage(damage);

            if (sourceTower != null) {
                sourceTower.addDamage(damage); // Track damage dealt by the tower
            }
        }
    }

    protected abstract void onHit(); // Each subclass defines what happens on hit
}
