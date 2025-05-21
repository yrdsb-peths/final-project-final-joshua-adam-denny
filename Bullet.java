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
            if (target != null) {
                target.takeDamage(1); // 1 damage per hit
            }
            getWorld().removeObject(this); // bullet disappears
        }

    } else {
        if (getWorld() != null) {
            getWorld().removeObject(this); // target gone
        }
    }
}

}
