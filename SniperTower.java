import greenfoot.*;
import java.util.List;

public class SniperTower extends Tower {
    private int baseCooldownTime;  // store original cooldown time
    private boolean speedBoostActive = false;
    private int speedBoostTimer = 0;  // counts down the boost duration in frames
    private int upgradedCooldownTime;

    public SniperTower() {
        GameWorld world = (GameWorld) getWorld();

        GreenfootImage img = new GreenfootImage("Sniper_tower.png");
        img.scale(60, 60);
        setImage(img);

        baseCooldownTime = 120;
        upgradedCooldownTime = baseCooldownTime;
        cooldownTime = upgradedCooldownTime;


        range = 1000;        // long range
        damage = 20;         // high damage
        bulletSpeed = 30;
        baseCost = 300;
        upgradeCostPerLevel = 150;

        upgradeCost = upgradeCostPerLevel;
        totalInvested = baseCost;

        
        if (level == maxLevel && world != null) {
            world.unlockSniperAbility();
        }
    }

    @Override
    public boolean upgrade() {
        GameWorld world = (GameWorld) getWorld();
        if (level < maxLevel && world != null && world.spendMoney(upgradeCost)) {
            level++;
            damage += 10;  // boost damage significantly
            upgradedCooldownTime = Math.max(60, upgradedCooldownTime - 30);
            cooldownTime = upgradedCooldownTime;

            totalInvested += upgradeCost;
            upgradeCost += 100;  // higher cost per upgrade
            updateImage();

 
            if (level == maxLevel && world != null) {
                world.incrementMaxLevelSnipers();
            }

            return true;
        }
        return false;
    }

    @Override
    protected void updateImage() {
        GreenfootImage img = new GreenfootImage("Sniper_tower.png");
        img.scale(60, 60);
        setImage(img);

        super.updateImage();  // add outline based on level
    }

    @Override
    protected Enemy getEnemyInRange() {
        List<Enemy> enemies = getObjectsInRange(range, Enemy.class);
        Enemy farthest = null;
        int maxX = -1;
        for (Enemy e : enemies) {
            if (e.getX() > maxX) {
                maxX = e.getX();
                farthest = e;
            }
        }
        return farthest;
    }

    @Override
    public void act() {
        if (speedBoostActive) {
            speedBoostTimer--;
            if (speedBoostTimer <= 0) {
                cooldownTime = baseCooldownTime;
                speedBoostActive = false;
                System.out.println("Speed boost expired on tower at (" + getX() + "," + getY() + ")");
                updateImage(); // reset to normal image when boost ends
            } else {
                addBoostImage();  // show green transparent overlay
            }
        }
        else {
            updateImage(); // ensure normal image if not boosted
        }
    
        super.act();
    }




    public boolean isBoostActive() {
        return speedBoostActive;
    }

    
    public void activateSpeedBoost() {
        if (!speedBoostActive) {
            cooldownTime = baseCooldownTime / 10;
            cooldown = 0;
            speedBoostTimer = 5 * 60;
            speedBoostActive = true;
        
        }
    }
    
    @Override
    public void sell() {
        GameWorld world = (GameWorld) getWorld();
        int refund = (int)(totalInvested * 0.8);
        if (world != null) {
            world.addMoney(refund);
            if (level == maxLevel) {
                world.decrementMaxLevelSnipers();
            }
        }
        getWorld().removeObject(this);
    }


    private void addBoostImage() {
        GreenfootImage base = new GreenfootImage("Sniper_tower.png");
        base.scale(60, 60);
    
        GreenfootImage overlay = new GreenfootImage(base.getWidth(), base.getHeight());
        overlay.setColor(new Color(0, 255, 0, 100)); // green with transparency (alpha=100/255)
        overlay.fillRect(0, 0, overlay.getWidth(), overlay.getHeight());
    
        base.drawImage(overlay, 0, 0);
        setImage(base);
    }

}
