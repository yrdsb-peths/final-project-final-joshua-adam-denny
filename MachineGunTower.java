import greenfoot.*;
/**
 * MachineGunTower Class is a tower that shoots bullets at enemies and can launch a missile when upgraded to level 3.
 * 
 * @author Joshua Stevens
 * @version Version 1.0
 */
public class MachineGunTower extends Tower {
    public String imageName = ("MachineGun_tower.png");
    private int shotNum = 0;
    int fuseTime = 15;

    /**
     * Constructor for MachineGunTower.
     * Initializes the tower with its base cost, upgrade cost, and other properties.
     */
    public MachineGunTower() {
        GreenfootImage img = new GreenfootImage(imageName);
        img.scale(75, 53);
        setImage(img);

        cooldownTime = 9;             
        range = 300;                   
        damage = 3;                   
        bulletSpeed = 10;
         
        baseCost = 750;
        upgradeCostPerLevel = 150;
        upgradeCost = upgradeCostPerLevel;
        totalInvested = baseCost;
    }

    @Override
    public boolean upgrade() {
        GameWorld world = (GameWorld) getWorld();
        if (level < maxLevel && world.spendMoney(upgradeCost)) {
            level++;
            damage += 3;
            cooldownTime = Math.max(3, cooldownTime - 2);
            totalInvested += upgradeCost; // track what's actually spent
            upgradeCost += 150;
            if (level == 3) {
                imageName = "MachineGun_tower_1.png";
            }
            updateImage();
            return true;
        }
        return false;
    }

    @Override
    protected void updateImage() {
        GreenfootImage img = new GreenfootImage(imageName);
        setImage(img);
    
        super.updateImage();  // This will add the outline based on level
    }
    
    @Override
    protected void shoot(Enemy target) {
        // Rotate the tower to face the enemy
        int dx = target.getX() - getX();
        int dy = target.getY() - getY();
        double angle = Math.toDegrees(Math.atan2(dy, dx));
        setRotation((int) angle);
        // Shoot a bullet
        AudioManager.playSFX(new GreenfootSound("gunShotSmall.mp3"));
        if (shotNum > 25 && level == 3)
        {
            launchMissle(target);
            shotNum = 0;
            
        }
        else
        {
            getWorld().addObject(new Bullet(target, damage, bulletSpeed, this), getX(), getY());
            shotNum++;
        }
    }
    
    private void launchMissle(Enemy target) {
        if (getWorld() != null) {
            int nukeDamage = 30;
            int missileSpeed = 15;
            int explosionRadius = 70;

            NukeMissile missile = new NukeMissile(target, nukeDamage, missileSpeed, explosionRadius, this, 50, fuseTime, 0, 1);
            getWorld().addObject(missile, getX(), getY());
            AudioManager.playSpecialSFX(new GreenfootSound("missileLaunchSmall.mp3"));
        }
    }

}
