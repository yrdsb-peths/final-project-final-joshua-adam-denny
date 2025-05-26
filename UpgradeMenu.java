import greenfoot.*;

public class UpgradeMenu extends Actor {
    private Tower tower;
    private RangeCircle rangeCircle;

    public UpgradeMenu(Tower tower) {
        this.tower = tower;
        updateImage();
        showRangeCircle();
    }

    private void updateImage() {
        GreenfootImage img = new GreenfootImage(120, 60);
        img.setColor(Color.WHITE);
        img.fillRect(0, 0, img.getWidth(), img.getHeight());
        img.setColor(Color.BLACK);
        img.drawRect(0, 0, img.getWidth() - 1, img.getHeight() - 1);

        // If tower is max upgraded, disable the upgrade button display
        if (tower.isMaxUpgraded()) {
            img.drawString("Max Upgrade", 5, 20);
        } else {
            img.drawString("Upgrade ($" + tower.getUpgradeCost() + ")", 5, 20);
        }

        img.drawString("Sell ($" + tower.getSellValue() + ")", 5, 50);
        setImage(img);
    }

    public void showRangeCircle() {
        if (getWorld() == null) return; // Safety check
        rangeCircle = new RangeCircle(tower.getRange());
        GameWorld world = (GameWorld) getWorld();
        world.addObject(rangeCircle, tower.getX(), tower.getY());
        world.setPaintOrder(UpgradeMenu.class, RangeCircle.class);
    }

    public void handleClick(int globalX, int globalY) {
        int localX = globalX - getX() + getImage().getWidth() / 2;
        int localY = globalY - getY() + getImage().getHeight() / 2;

        GameWorld world = (GameWorld) getWorld();

        if (localY < 30) {
            if (tower.isMaxUpgraded()) {
                System.out.println("Tower is already at max upgrade.");
            } else if (tower.upgrade()) {
                System.out.println("Tower upgraded!");
                updateImage();  // Update buttons after upgrade
            } else {
                System.out.println("Upgrade failed.");
            }
        } else {
            tower.sell();
            System.out.println("Tower sold!");
            closeMenu();
            world.removeObject(this);
            world.clearUpgradeMenu();
            return;  // Exit early since menu is gone
        }

        // Keep menu open and update image if upgrade was successful or failed
        updateImage();
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
