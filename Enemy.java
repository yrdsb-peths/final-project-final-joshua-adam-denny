import greenfoot.*;

public class Enemy extends Actor {
    protected int speed;
    protected int health;

    public Enemy(int speed, int health) {
        this.speed = speed;
        this.health = health;
        setImage("man.png");
    }

    public int getSpeed() {
        return speed;
    }

    public void takeDamage(int damage) {
        health -= damage;
        if (health <= 0 && getWorld() != null) {
            ((GameWorld)getWorld()).addMoney(10);
            getWorld().removeObject(this);
        }
    }
    public int getHealth() {
        return health;
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
