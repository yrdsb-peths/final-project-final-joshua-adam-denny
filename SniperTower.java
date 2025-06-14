import greenfoot.*;
import java.util.List;
/**
 * SniperTower Class is a specialized tower that can shoot enemies from a long range with high damage.
 * Ability: When upgraded to level 3, speeds up its shooting significantly for a short duration.
 * It also has a unique visual effect when the speed boost is active.
 * @author Joshua Stevens
 * @version Version 1.0
 */
public class SniperTower extends Tower {
    private int baseCooldownTime;
    private boolean speedBoostActive = false;
    private int speedBoostTimer = 0;
    private int upgradedCooldownTime;
    private int orbitAngle = 0;

    private String imageName = "Sniper_tower.png";
    private GreenfootImage towerImage;
    private GreenfootImage boostOverlay;

    public SniperTower() {
        GameWorld world = (GameWorld) getWorld();

        towerImage = new GreenfootImage(imageName);
        towerImage.scale(60, 60);

        boostOverlay = new GreenfootImage("boost.png");
        boostOverlay.scale(60, 60);

        setImage(towerImage);

        baseCooldownTime = 120;
        upgradedCooldownTime = baseCooldownTime;
        cooldownTime = upgradedCooldownTime;

        range = 1000;
        damage = 20;
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
    protected void shoot(Enemy target) {
        // Rotate the tower to face the enemy
        int dx = target.getX() - getX();
        int dy = target.getY() - getY();
        double angle = Math.toDegrees(Math.atan2(dy, dx));
        setRotation((int) angle);
        
        AudioManager.playSFX(new GreenfootSound("gunShotBig.mp3"));
        // Shoot a bullet and include this tower as the source
        getWorld().addObject(new Bullet(target, damage, bulletSpeed, this), getX(), getY());
    }


    
    @Override
    public boolean upgrade() {
        GameWorld world = (GameWorld) getWorld();
        if (level < maxLevel && world != null && world.spendMoney(upgradeCost)) {
            level++;
            damage += 20;
            if (level < 3) {
                upgradedCooldownTime = Math.max(30, upgradedCooldownTime - 21);
                cooldownTime = upgradedCooldownTime;
            } else if (level == 3) {
                upgradedCooldownTime = Math.max(30, upgradedCooldownTime - 22);
                cooldownTime = upgradedCooldownTime;
            }
            
            totalInvested += upgradeCost;
            upgradeCost += 150;
            updateImage();
            
            if (level == 3) {
                imageName = "Sniper_tower_1.png";
                towerImage = new GreenfootImage(imageName);
                towerImage.scale(100, 45);
        }

            if (level == maxLevel) {
                world.incrementMaxLevelSnipers();
            }
            return true;
        }
        return false;
    }

    @Override
    protected void updateImage() {
        GreenfootImage img = new GreenfootImage(towerImage); // fresh copy
        img.scale(100, 45);
        setImage(img);
        super.updateImage();  // add outline
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
                cooldownTime = upgradedCooldownTime;
                speedBoostActive = false;
                updateImage(); // reset to base image
            } else {
                drawBoostOverlay(); // draw with animated overlay
            }
        } else {
            updateImage();
        }

        orbitAngle = (orbitAngle + 5) % 360; // advance for rotation
        super.act();
    }

    /**
     * Checks if the speed boost is currently active.
     * @return true if the speed boost is active, false otherwise.
     */

    public boolean isBoostActive() {
        return speedBoostActive;
    }


    /**
     * Activates the speed boost for the sniper tower.
     * This reduces the cooldown time significantly and starts a timer for the boost duration.
     */
    public void activateSpeedBoost() {
        if (!speedBoostActive) {
            cooldownTime = cooldownTime / 8;
            cooldown = 0;
            speedBoostTimer = 5 * 60;
            speedBoostActive = true;
            AudioManager.playSFX(new GreenfootSound("sniperBoost.mp3"));
            
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

    private void drawBoostOverlay() {
        GreenfootImage base = new GreenfootImage(towerImage); // fresh copy
        base.scale(100, 55);

        GreenfootImage rotatedOverlay = new GreenfootImage(boostOverlay); // fresh copy
        rotatedOverlay.rotate(orbitAngle);

        base.drawImage(rotatedOverlay, 0, 0);
        setImage(base);
    }
}
