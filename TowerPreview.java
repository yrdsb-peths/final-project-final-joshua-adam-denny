import greenfoot.*;
/**
 * TowerPreview Class is an actor that provides a visual preview of a tower before it is placed in the world.
 * 
 * @author Joshua Stevens
 * @version Version 1.0a version number or a date)
 */
public class TowerPreview extends Actor {
    private String towerType;
    private RangeCircle rangeCircle;

    /**
     * Constructor for TowerPreview.
     * Initializes the tower type and creates a visual representation of the tower.
     * @param towerType The type of tower to preview.
     * @param range The range of the tower, used to create a RangeCircle.
     */

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


    /**
     * Constructor for TowerPreview with a default range of 100.
     * @param towerType The type of tower to preview.
     */
    @Override
    protected void addedToWorld(World world) {
        // Add the range circle to the world at the same position
        world.addObject(rangeCircle, getX(), getY());
    }


    /**
     * Act method that updates the position of the preview based on mouse movement.
     * It also updates the position of the range circle to match the preview.
     */
    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            setLocation(mouse.getX(), mouse.getY());
            rangeCircle.setLocation(getX(), getY());
        }
    }

    /**
     * Returns the type of tower being previewed.
     * @return The tower type as a String.
     */
    public String getTowerType() {
        return towerType;
    }

    /**
     * Removes the preview from the world.
     * This method removes both the TowerPreview and its associated RangeCircle from the world.
     */
    public void removePreview() {
        World world = getWorld();
        if (world != null) {
            world.removeObject(rangeCircle);
            world.removeObject(this);
        }
    }
}
