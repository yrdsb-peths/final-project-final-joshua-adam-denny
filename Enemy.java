import greenfoot.*;

public class Enemy extends Actor {
    protected int speed;
    protected int health;

    public Enemy(int speed) {
        this.speed = speed;
        this.health = 1;
        setImage("man.png");
    }

    public int getSpeed() {
        return speed;
    }

    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0 && getWorld() != null) {
            ((GameWorld)getWorld()).addMoney(10);
            getWorld().removeObject(this);
        }
    }

    // Allow subclasses to define how many lives to subtract
    public int getLifeDamage() {
        return 1;
    }

    public void act() {
        move(speed);

        World world = getWorld();
        if (world != null && getX() >= world.getWidth() - 1) {
            ((GameWorld) world).loseLife(getLifeDamage());
            world.removeObject(this);
        }
    }
}
