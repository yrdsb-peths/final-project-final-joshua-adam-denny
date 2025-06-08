import greenfoot.*;
/**
 * Write a description of class Base here.
 * 
 * @Joshua Stevens
 * @version (a version number or a date)
 */
public class TowerPreview extends Actor {
    private String towerType;
    private RangeCircle rangeCircle;

    public TowerPreview(String towerType, int range) {
        this.towerType = towerType;

        GreenfootImage img;
        try {
            img = new GreenfootImage(towerType + "_tower.png");
            if (img.getWidth() <= 1 || img.getHeight() <= 1) {
                throw new Exception("Image not found or invalid size");
            }
        } catch (Exception e) {
            img = new GreenfootImage(40, 40);
            img.setColor(Color.GREEN);
            img.fill();
        }
        img.setTransparency(100);
        setImage(img);

        rangeCircle = new RangeCircle(range);
    }

    @Override
    protected void addedToWorld(World world) {
        // Add the range circle to the world at the same position
        world.addObject(rangeCircle, getX(), getY());
    }

    @Override
    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            setLocation(mouse.getX(), mouse.getY());
            rangeCircle.setLocation(getX(), getY());
        }
    }

    public String getTowerType() {
        return towerType;
    }

    public void removePreview() {
        World world = getWorld();
        if (world != null) {
            world.removeObject(rangeCircle);
            world.removeObject(this);
        }
    }
}
