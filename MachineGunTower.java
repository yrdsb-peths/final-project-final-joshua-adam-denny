import greenfoot.*;

public class MachineGunTower extends Tower {
    public String imageName = ("MachineGun_tower.png");
    private int shotNum = 0;
    int fuseTime = 15;
    public MachineGunTower() {
        GreenfootImage img = new GreenfootImage(imageName);
        img.scale(75, 53);
        setImage(img);

        cooldownTime = 10;             
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
            damage += 2;
            cooldownTime = Math.max(4, cooldownTime - 2);
            totalInvested += upgradeCost; // track what's actually spent
            upgradeCost += 150;
            if (level == 1) {
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
        GreenfootSound shoot = new GreenfootSound("gunShotSmall.mp3");
        shoot.setVolume(30);  // Optional: Set volume from 0–100
        shoot.play(); 
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
            int nukeDamage = 20;
            int missileSpeed = 15;
            int explosionRadius = 50;

            NukeMissile missile = new NukeMissile(target, nukeDamage, missileSpeed, explosionRadius, this, 50, fuseTime, 0, 1);
            getWorld().addObject(missile, getX(), getY());
            GreenfootSound launch = new GreenfootSound("missileLaunchSmall.mp3");
            launch.setVolume(45);  // Optional: Set volume from 0–100
            launch.play(); 
        }
    }

}
