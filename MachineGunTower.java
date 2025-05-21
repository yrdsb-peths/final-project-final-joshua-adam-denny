import greenfoot.*;

public class MachineGunTower extends Tower {
    public MachineGunTower() {
        //GreenfootImage img = new GreenfootImage("machine_gun_tower.png");
        //img.scale(50, 50);
        //setImage(img);

        cooldownTime = 10;             
        range = 300;                   
        damage = 1;                   
        bulletSpeed = 5;

        upgradeCost = 200; // starting upgrade cost
    }

    @Override
    public boolean upgrade() {
        GameWorld world = (GameWorld) getWorld();
        if (level < maxLevel && world.spendMoney(upgradeCost)) {
            level++;
            damage += 1;                  // small damage increase
            cooldownTime = Math.max(5, cooldownTime - 2); // faster firing rate
            upgradeCost += 50;            // increase upgrade cost
            updateImage();
            return true;
        }
        return false;
    }

    @Override
    protected void updateImage() {
        // You can add code to change image per level, e.g.
        // setImage("machine_gun_tower_level" + level + ".png");
    }

    @Override
    protected Enemy getEnemyInRange() {
        return super.getEnemyInRange();
    }

    @Override
    protected void shoot(Enemy target) {
        getWorld().addObject(new Bullet(target, damage, bulletSpeed), getX(), getY());
    }
}
