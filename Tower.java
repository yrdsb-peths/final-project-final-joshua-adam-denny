import greenfoot.*;
import java.util.List;

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

    public void act() {
        if (cooldown > 0) {
            cooldown--;
        } else {
            Enemy target = getEnemyInRange();
            if (target != null) {
                shoot(target);
                cooldown = cooldownTime;
            }
        }
    }

    protected Enemy getEnemyInRange() {
        List<Enemy> enemies = getObjectsInRange(range, Enemy.class);
        if (!enemies.isEmpty()) {
            return enemies.get(0);
        }
        return null;
    }

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
        System.out.println("Upgrade not implemented for this tower type.");
        return false;
    }

    public void sell() {
        GameWorld world = (GameWorld) getWorld();
        int refund = (int)(totalInvested * 0.8);
        world.addMoney(refund);
        getWorld().removeObject(this);
    }

    @Override
    public void addedToWorld(World world) {
        totalInvested = baseCost;
        updateImage();
    }

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
