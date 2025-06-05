import greenfoot.*;

public class MachineGunTower extends Tower {
    public String imageName = ("MachineGun_tower.png");
    private int shotNum = 0;
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
        if (shotNum > 40 && level == 3)
        {
            launchMissle(target);
            shotNum = 0;
        }
        else
        {
            getWorld().addObject(new Bullet(target, damage, bulletSpeed), getX(), getY());
            shotNum++;
        }
    }
    
    private void launchMissle(Enemy target) {
        if (getWorld() != null) {
            int nukeDamage = 20;
            int missileSpeed = 1;
            int explosionRadius = 75;

            NukeMissile missile = new NukeMissile(target, nukeDamage, missileSpeed, explosionRadius);
            getWorld().addObject(missile, getX(), getY());
        }
    }

    /*@Override
    protected Enemy getEnemyInRange() {
        return super.getEnemyInRange();
    }

    

  
    public void sell() {

    }*/
}
