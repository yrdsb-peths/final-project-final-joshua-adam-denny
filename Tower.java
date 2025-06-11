import greenfoot.*;
import java.util.List;
/**
 * Tower Class is a base class for all tower types in the game.
 * 
 * @author Joshua Stevens
 * @version Version 5.0
 */
public class Tower extends Actor {
    protected int cooldownTime = 60;
    protected int cooldown = 0;
    protected int range = 100;
    protected int damage = 1;
    protected int bulletSpeed = 5;
    protected int totalDamageDone = 0;
    protected int level = 0;         // current upgrade level (start at 0)
    protected int maxLevel = 3;      // max upgrades allowed
    protected int upgradeCost = 50;  // cost to upgrade
    protected int baseCost;
    protected int upgradeCostPerLevel;
    protected int totalInvested = 0;
    protected GameWorld gw;

    public void act() {
        if (cooldown > 0) {
            cooldown--;
        } else {
            Enemy target = getEnemyInRange();
            if (target != null && gw.getStatus() == GameWorld.Status.RUNNING) {
                shoot(target);
                cooldown = cooldownTime;
            }
        }
    }

    /**
     * Constructor for Tower.
     * Initializes the tower with a base cost and upgrade cost per level.
     * 
     * @param baseCost The initial cost of the tower.
     * @param upgradeCostPerLevel The cost to upgrade the tower per level.
     */
    protected Enemy getEnemyInRange() {
        List<Enemy> enemies = getObjectsInRange(range, Enemy.class);
        if (!enemies.isEmpty()) {
            return enemies.get(0);
        }
        return null;
    }

    /**
     * Shoots a bullet at the specified target enemy.
     * This method rotates the tower to face the enemy and creates a bullet object.
     * 
     * @param target The enemy that this tower will shoot at.
     */
    protected void shoot(Enemy target) {
        // Rotate the tower to face the enemy
        int dx = target.getX() - getX();
        int dy = target.getY() - getY();
        double angle = Math.toDegrees(Math.atan2(dy, dx));
        setRotation((int) angle);
    
        // Shoot a bullet and include this tower as the source
        getWorld().addObject(new Bullet(target, damage, bulletSpeed, this), getX(), getY());
    }

    
    public boolean upgrade() {
        return false;
    }

    
    /**
     * sells the tower, refunding 80% of the total invested amount.
     * This method removes the tower from the world and adds the refund amount to the game world.
     * @return void
     */
    public void sell() {
        GameWorld world = (GameWorld) getWorld();
        int refund = (int)(totalInvested * 0.8);
        world.addMoney(refund);
        getWorld().removeObject(this);
    }

    @Override
    public void addedToWorld(World world) {
        gw = (GameWorld) world;
        totalInvested = baseCost;
        updateImage();
    }


    /**
     * Updates the tower's image based on its level.
     * This method creates an outlined version of the tower's image depending on its upgrade level.
     */
    protected void updateImage() {
        GreenfootImage baseImg = getImage();
        if (baseImg == null) return;

        GreenfootImage outlinedImg;

        if (level == 0) {
            // No outline for level 0
            outlinedImg = baseImg;
        } else if (level == 1) {
            outlinedImg = createOutlinedImage(baseImg, Color.GREEN);
        } else if (level == 2) {
            outlinedImg = createOutlinedImage(baseImg, Color.RED);
        } else if (level == 3) {
            outlinedImg = createOutlinedImage(baseImg, Color.BLUE);
        } else {
            outlinedImg = baseImg;
        }

        setImage(outlinedImg);
    }


    /**
     * Creates an outlined version of the base image.
     * This method draws the base image with an outline color around it.
     * 
     * @param baseImg The original image to outline.
     * @param outlineColor The color of the outline.
     * @return A new GreenfootImage with the outline applied.
     */
    private GreenfootImage createOutlinedImage(GreenfootImage baseImg, Color outlineColor) {
        int w = baseImg.getWidth();
        int h = baseImg.getHeight();

        GreenfootImage outline = new GreenfootImage(w + 2, h + 2);
        outline.clear();

        // Draw outline in 8 directions around center
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                GreenfootImage colored = new GreenfootImage(baseImg);
                replaceColor(colored, outlineColor);
                outline.drawImage(colored, dx + 1, dy + 1);
            }
        }
        outline.drawImage(baseImg, 1, 1);

        return outline;
    }

    
    // Getters for tower properties
    public int getRange() 
    {
        return range;
    }
    
    public int getLevel() 
    {
        return level;
    }
    public int getMaxLevel() 
    {
        return maxLevel;
    }
    
    public int getUpgradeCost() 
    {
        return upgradeCost;
    }
    
    public int getSellValue() 
    {
        return (int)(totalInvested * 0.8);
    }
    
    public boolean isMaxUpgraded() {
        return level >= maxLevel;  // assuming you track level and max level
    }

    public void addDamage(int dmg) {
        totalDamageDone += dmg;
    }
    
    public int getTotalDamageDone() {
        return totalDamageDone;
    }

    
    
    // Replace all visible pixels with outlineColor (keeping alpha)
    private void replaceColor(GreenfootImage img, Color outlineColor) {
        int width = img.getWidth();
        int height = img.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color pixel = img.getColorAt(x, y);
                if (pixel.getAlpha() > 0) {
                    img.setColorAt(x, y, outlineColor);
                }
            }
        }
    }
}
