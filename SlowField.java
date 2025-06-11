import greenfoot.*;
import java.util.List;
/**
 * SlowField Class is an effect that creates a field that slows down enemies within a certain radius.
 * 
 * @author Joshua Stevens
 * @version Version 1.0a version number or a date)
 */
public class SlowField extends Actor {
    private int duration; // how long the field lasts
    private int radius;
    private int slowAmount;

    public SlowField(int radius, int slowAmount, int duration) {
        this.radius = radius;
        this.slowAmount = slowAmount;
        this.duration = duration;

        GreenfootImage img = new GreenfootImage("Field.png");
        img.setColor(new Color(100, 100, 255, 100)); // semi-transparent blue
        setImage(img);
    }



    public void act() {
        // Check if the field should still be active
        if (duration-- <= 0) {
            getWorld().removeObject(this);
            return;
        }
        // Apply slow effect to all enemies within the radius
        List<Enemy> enemies = getObjectsInRange(radius, Enemy.class);
        for (Enemy enemy : enemies) {
            enemy.applySlow(slowAmount, 5); // Apply small short slow repeatedly
        }
    }
}
