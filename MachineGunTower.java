import greenfoot.*;

public class MachineGunTower extends Tower {
    public MachineGunTower() {
        GreenfootImage img = new GreenfootImage("MachineGun_tower.png");
        img.scale(75, 53);
        setImage(img);

        cooldownTime = 10;             
        range = 300;                   
        damage = 3;                   
        bulletSpeed = 10;

        baseCost = 750;
        upgradeCostPerLevel = 150;
        upgradeCost = upgradeCostPerLevel;
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
            updateImage();
            return true;
        }
        return false;
    }

    @Override
    protected void updateImage() {
        GreenfootImage img = new GreenfootImage("MachineGun_tower.png");
        img.scale(75, 53);
        setImage(img);
    
        super.updateImage();  // This will add the outline based on level
    }

    /*@Override
    protected Enemy getEnemyInRange() {
        return super.getEnemyInRange();
    }

    @Override
    protected void shoot(Enemy target) {
        getWorld().addObject(new Bullet(target, damage, bulletSpeed), getX(), getY());
    }

  
    public void sell() {

    }*/
}
