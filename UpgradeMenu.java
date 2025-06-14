import greenfoot.*;
/**
 * UpgradeMenu Class is a UI element that allows players to upgrade or sell a tower. as well as display the total damage dealt by the tower.
 * 
 * @author Joshua Stevens
 * @version Version 2.0
 */
public class UpgradeMenu extends UI {
    private Tower tower;
    private RangeCircle rangeCircle;


    /**
     * Constructor for UpgradeMenu.
     * @param tower The tower that this menu is associated with.
     */
    public UpgradeMenu(Tower tower) {
        this.tower = tower;
    }

    /**
     * Act method that updates the menu image every frame.
     * It also handles the display of the total damage dealt by the tower.
     */
    public void act() {
        updateImage(); // Will re-render every frame with latest damage
    }

    
    private void updateImage() {
        GreenfootImage img = new GreenfootImage("ui/upgrademenu.png");
        setImage(img);

    
        if (tower.isMaxUpgraded()) {
            img.drawString("Max Upgrade", 5, 20);
        } else {
            img.drawString("Upgrade ($" + tower.getUpgradeCost() + ")", 5, 18);
        }
    
        img.drawString("Sell ($" + tower.getSellValue() + ")", 5, 45);
    
        // NEW: Show damage dealt
        img.drawString("Dmg: " + tower.getTotalDamageDone(), 5, 70);
    
        setImage(img);
    }


    /**
     * Displays the range circle of the tower.
     * This method creates a RangeCircle object and adds it to the world.
     */
    public void showRangeCircle() {
        if (getWorld() == null) return; // Safety check
        rangeCircle = new RangeCircle(tower.getRange());
        GameWorld world = (GameWorld) getWorld();
        world.addObject(rangeCircle, tower.getX(), tower.getY());
        world.setPaintOrder(
            UpgradeMenu.class,
            NukeMissile.class,
            DDCRender.class,
            CustomLabel.class,
            PauseButton.class,
            PauseMenu.class,
            Button.class,
            EndGamePopup.class,
            Transition.class, 
            Sidebar.class,
            UI.class, 
            ExplosionEffect.class,
            Bullet.class,
            Tower.class, 
            Enemy.class,
            ImageActor.class
        ); // Upgrade Menu on top of everything
    }

    /**
     * Handles click events on the upgrade menu.
     * @param globalX The global X coordinate of the click.
     * @param globalY The global Y coordinate of the click.
     */
    public void handleClick(int globalX, int globalY) {
        int localY = globalY - getY() + getImage().getHeight() / 2;
    
        GameWorld world = (GameWorld) getWorld();
    
        if (localY < 30 || Greenfoot.isKeyDown("enter")) {
            if (tower.isMaxUpgraded()) {
                
            } else if (tower.upgrade()) {
                
                updateImage();  // Update upgrade button
                updateRangeCircle();  // <-- NEW: update the range circle radius
            } else {
                
            }
        } else if (localY >= 30 || Greenfoot.isKeyDown("d")) {
            tower.sell();
            world.clearUpgradeMenu();
            return;
        }
    
        // Just updating buttons again in case upgrade failed
        updateImage();
    }

    private void updateRangeCircle() {
        if (rangeCircle != null && getWorld() != null) {
            getWorld().removeObject(rangeCircle);
        }
    
        showRangeCircle();  // Re-adds the circle with new range
    }

    

    /**
     * Checks if the given global coordinates are within the bounds of this menu.
     * @param globalX The global X coordinate to check.
     * @param globalY The global Y coordinate to check.
     * @return true if the coordinates are within the menu, false otherwise.
     */
    public boolean contains(int globalX, int globalY) {
        int myX = getX();
        int myY = getY();
        return globalX >= myX - getImage().getWidth() / 2 && globalX <= myX + getImage().getWidth() / 2 &&
               globalY >= myY - getImage().getHeight() / 2 && globalY <= myY + getImage().getHeight() / 2;
    }

    /**
     * Closes the upgrade menu and removes the range circle if it exists.
     */
    public void closeMenu() {
        if (rangeCircle != null && getWorld() != null) {
            getWorld().removeObject(rangeCircle);
            rangeCircle = null;
        }
    }
}
