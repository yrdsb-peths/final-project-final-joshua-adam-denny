import greenfoot.*;
/**
 * Write a description of class Base here.
 * 
 * @Joshua Stevens
 * @version (a version number or a date)
 */
public class BasicTower extends Tower {
    public String imageName = "Basic_tower.png";
    private int slowShotCounter = 0;
    private final int slowShotInterval = 5; // Fire slow bullet every 5 shots
    public BasicTower() {
        
        GreenfootImage img = new GreenfootImage(imageName);
        img.scale(50, 50);
        setImage(img);

        cooldownTime = 60;
        range = 200;
        damage = 2;
        bulletSpeed = 10;

        baseCost = 50;
        upgradeCostPerLevel = 25;
        upgradeCost = upgradeCostPerLevel;
        totalInvested = baseCost;
    }

    @Override
    public boolean upgrade() {
        GameWorld world = (GameWorld) getWorld();
        if (level < maxLevel && world.spendMoney(upgradeCost)) {
            level++;
            damage += 2;
            range += 15;
            cooldownTime = Math.max(30, cooldownTime - 10);
            totalInvested += upgradeCost;
            upgradeCost += 10;
            updateImage();
            return true;
        }
        return false;
    }

    @Override
    protected void updateImage() {
        GreenfootImage img = new GreenfootImage(imageName);
        setImage(img);
        super.updateImage();
    }

    @Override
    protected void shoot(Enemy target) {
        int dx = target.getX() - getX();
        int dy = target.getY() - getY();
        double angle = Math.toDegrees(Math.atan2(dy, dx));
        setRotation((int) angle);
        AudioManager.playSFX(new GreenfootSound("gunShotSmall.mp3"));
        // Always fire a normal bullet
        getWorld().addObject(new Bullet(target, damage, bulletSpeed, this), getX(), getY());

        // Fire a slow field bullet every N shots, only at max level
        if (level == maxLevel) {
            slowShotCounter++;
            if (slowShotCounter >= slowShotInterval) {
                getWorld().addObject(new SlowFieldBullet(target, damage, bulletSpeed, this), getX(), getY());
                slowShotCounter = 0;
                AudioManager.playSpecialSFX(new GreenfootSound("netThrow.mp3"));
            }
        }
         
    }
}
