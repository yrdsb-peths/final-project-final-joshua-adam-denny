import greenfoot.*;  
import java.util.List;

public class Bullet extends Actor {
    private Enemy target;

    public Bullet(Enemy target) {
        this.target = target;
    }

    public void act() {
        if (target != null && getWorld().getObjects(Enemy.class).contains(target)) {
            turnTowards(target.getX(), target.getY());
            move(5);

            if (intersects(target)) {
                World world = getWorld(); // Store it once
                if (world != null) {
                    ((GameWorld) world).addMoney(10); // reward
                    world.removeObject(target);       // remove enemy
                    world.removeObject(this);         // remove bullet
                }
            }

        } else {
            World world = getWorld();
            if (world != null) {
                world.removeObject(this); // remove bullet if target is gone
            }
        }
    }
}
