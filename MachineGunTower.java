import greenfoot.*;

public class MachineGunTower extends Tower {
    public MachineGunTower() {
        GreenfootImage img = new GreenfootImage("MachineGun_tower.png");
        img.scale(50, 50);
        setImage(img);

        cooldownTime = 10;             
        range = 300;                   
        damage = 1;                   
        bulletSpeed = 5;

        baseCost = 750;
        upgradeCostPerLevel = 200;
        upgradeCost = upgradeCostPerLevel;
    }

    @Override
    public boolean upgrade() {
        GameWorld world = (GameWorld) getWorld();
        if (level < maxLevel && world.spendMoney(upgradeCost)) {
            level++;
            damage += 1;
            cooldownTime = Math.max(5, cooldownTime - 2);
            totalInvested += upgradeCost; // track what's actually spent
            upgradeCost += 150;
            updateImage();
            return true;
        }
        return false;
    }

    @Override
    protected void updateImage() {
        //GreenfootImage img = new GreenfootImage("sniper_tower_level" + level + ".png");
        GreenfootImage img = new GreenfootImage("MachineGun_tower.png");
        img.scale(50, 50);
        setImage(img);
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
