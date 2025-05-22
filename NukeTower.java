import greenfoot.*;
import java.util.List;

public class NukeTower extends Tower {

    public NukeTower() {
        GreenfootImage img = new GreenfootImage("Nuke_tower.png");
        img.scale(60, 60);
        setImage(img);

        baseCost = 5000;
    }

    @Override
    public void addedToWorld(World world) {
        super.addedToWorld(world);
        detonate();
    }

    private void detonate() {
        List<Enemy> enemies = getWorld().getObjects(Enemy.class);
        for (Enemy enemy : enemies) {
            getWorld().removeObject(enemy);
        }
        getWorld().removeObject(this);
    }

    @Override
    public boolean upgrade() {
        return false;
    }

    @Override
    public void sell() {
        // No refund / no selling
    }
}
