import greenfoot.*;

public class ExplosionEffect extends Actor {
    private int timer = 30;

    public ExplosionEffect(int radius) {
        GreenfootImage img = new GreenfootImage(radius * 2, radius * 2);
        img.setColor(new Color(255, 100, 0, 128));
        img.fillOval(0, 0, radius * 2, radius * 2);
        setImage(img);
    }

    public void act() {
        timer--;
        if (timer <= 0) {
            getWorld().removeObject(this);
        }
    }
}
