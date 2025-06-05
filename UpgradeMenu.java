import greenfoot.*;

public class UpgradeMenu extends Actor {
    private Tower tower;
    private RangeCircle rangeCircle;

    public UpgradeMenu(Tower tower) {
        this.tower = tower;
        updateImage();
        showRangeCircle();
    }

    public void act() {
        updateImage(); // Will re-render every frame with latest damage
    }

    
    private void updateImage() {
        GreenfootImage img = new GreenfootImage("ui/upgrademenu.png ");
        setImage(img);

    
        if (tower.isMaxUpgraded()) {
            img.drawString("Max Upgrade", 5, 20);
        } else {
            img.drawString("Upgrade ($" + tower.getUpgradeCost() + ")", 5, 20);
        }
    
        img.drawString("Sell ($" + tower.getSellValue() + ")", 5, 40);
    
        // NEW: Show damage dealt
        img.drawString("Dmg: " + tower.getTotalDamageDone(), 5, 60);
    
        setImage(img);
    }


    public void showRangeCircle() {
        if (getWorld() == null) return; // Safety check
        rangeCircle = new RangeCircle(tower.getRange());
        GameWorld world = (GameWorld) getWorld();
        world.addObject(rangeCircle, tower.getX(), tower.getY());
        world.setPaintOrder(
            NukeMissile.class,
            UpgradeMenu.class,
            DDCRender.class,
            Label.class,
            Button.class,
            EndGamePopup.class,
            Transition.class, 
            Sidebar.class, 
            RangeCircle.class,
            UI.class, 
            Tower.class, 
            Enemy.class
        );
    }

    public void handleClick(int globalX, int globalY) {
        int localX = globalX - getX() + getImage().getWidth() / 2;
        int localY = globalY - getY() + getImage().getHeight() / 2;
    
        GameWorld world = (GameWorld) getWorld();
    
        if (localY < 30) {
            if (tower.isMaxUpgraded()) {
                
            } else if (tower.upgrade()) {
                
                updateImage();  // Update upgrade button
                updateRangeCircle();  // <-- NEW: update the range circle radius
            } else {
                
            }
        } else {
            tower.sell();
            
            closeMenu();
            world.removeObject(this);
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

    

    public boolean contains(int globalX, int globalY) {
        int myX = getX();
        int myY = getY();
        return globalX >= myX - getImage().getWidth() / 2 && globalX <= myX + getImage().getWidth() / 2 &&
               globalY >= myY - getImage().getHeight() / 2 && globalY <= myY + getImage().getHeight() / 2;
    }

    public void closeMenu() {
        if (rangeCircle != null && getWorld() != null) {
            getWorld().removeObject(rangeCircle);
            rangeCircle = null;
        }
    }
}
