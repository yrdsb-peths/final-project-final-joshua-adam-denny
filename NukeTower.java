import greenfoot.*;
import java.util.List;

public class NukeTower extends Tower {
    public NukeTower() {
        GreenfootImage img = new GreenfootImage("Nuke_tower.png");
        img.scale(60, 60);
        setImage(img);

        baseCost = 5000;
        range = 250;               // Very large range
        cooldownTime = 3600;       // 60 seconds at 60 FPS
    }

    @Override
    protected void shoot(Enemy target) {
        getWorld().addObject(new NukeMissile(target), getX(), getY());
    }

    @Override
    public boolean upgrade() {
        return false; // No upgrades
    }

    @Override
    public void sell() {
        // No refunds for NukeTower
    }
}
