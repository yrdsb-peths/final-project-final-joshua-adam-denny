import greenfoot.*;

public class BasicTower extends Tower {
    public BasicTower() {
        GreenfootImage img = new GreenfootImage("Basic_tower.png");
        setImage(img);
        
        cooldownTime = 60;             
        range = 100;                  
        damage = 1;                  
        bulletSpeed = 5;
        upgradeCost = 50;
    }

    @Override
    public boolean upgrade() {
        GameWorld world = (GameWorld) getWorld();
        if (level < maxLevel && world.spendMoney(upgradeCost)) {
            level++;
            damage += 1;
            range += 15;
            cooldownTime = Math.max(30, cooldownTime - 10); // Fires faster
            upgradeCost += 50;
            updateImage();
            return true;
        }
        return false;
    }

    @Override
    protected void updateImage() {
        //GreenfootImage img = new GreenfootImage("sniper_tower_level" + level + ".png");
        GreenfootImage img = new GreenfootImage("Basic_tower.png");

        img.scale(50, 50);
        setImage(img);
    }

    @Override
    protected Enemy getEnemyInRange() {
        return super.getEnemyInRange(); // Use default targeting
    }

    @Override
    protected void shoot(Enemy target) {
        getWorld().addObject(new Bullet(target, damage, bulletSpeed), getX(), getY());
    }
}
