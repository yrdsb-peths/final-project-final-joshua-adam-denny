import greenfoot.*;
import java.util.List;
/**
 * Write a description of class Base here.
 * 
 * @Joshua Stevens
 * @version (a version number or a date)
 */
public class SniperTower extends Tower {
    private int baseCooldownTime;
    private boolean speedBoostActive = false;
    private int speedBoostTimer = 0;
    private int upgradedCooldownTime;
    private int orbitAngle = 0;

    private GreenfootImage towerImage;
    private GreenfootImage boostOverlay;

    public SniperTower() {
        GameWorld world = (GameWorld) getWorld();

        towerImage = new GreenfootImage("Sniper_tower.png");
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
            damage += 10;
            upgradedCooldownTime = Math.max(60, upgradedCooldownTime - 30);
            cooldownTime = upgradedCooldownTime;

            totalInvested += upgradeCost;
            upgradeCost += 100;
            updateImage();

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
        img.scale(60, 60);
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
                System.out.println("Speed boost expired on tower at (" + getX() + "," + getY() + ")");
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

    public boolean isBoostActive() {
        return speedBoostActive;
    }

    public void activateSpeedBoost() {
        if (!speedBoostActive) {
            cooldownTime = baseCooldownTime / 10;
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
        base.scale(60, 60);

        GreenfootImage rotatedOverlay = new GreenfootImage(boostOverlay); // fresh copy
        rotatedOverlay.rotate(orbitAngle);

        base.drawImage(rotatedOverlay, 0, 0);
        setImage(base);
    }
}
