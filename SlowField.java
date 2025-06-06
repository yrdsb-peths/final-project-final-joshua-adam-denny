import greenfoot.*;
import java.util.List;

public class SlowField extends Actor {
    private int duration; // how long the field lasts
    private int radius;
    private int slowAmount;

    public SlowField(int radius, int slowAmount, int duration) {
        this.radius = radius;
        this.slowAmount = slowAmount;
        this.duration = duration;

        GreenfootImage img = new GreenfootImage(radius * 2, radius * 2);
        img.setColor(new Color(100, 100, 255, 100)); // semi-transparent blue
        img.fillOval(0, 0, radius * 2, radius * 2);
        setImage(img);
    }

    public void act() {
        if (duration-- <= 0) {
            getWorld().removeObject(this);
            return;
        }

        List<Enemy> enemies = getObjectsInRange(radius, Enemy.class);
        for (Enemy enemy : enemies) {
            enemy.applySlow(slowAmount, 5); // Apply small short slow repeatedly
        }
    }
}
