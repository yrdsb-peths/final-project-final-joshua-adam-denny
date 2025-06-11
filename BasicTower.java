import greenfoot.*;
/**
 * BasicTower, AKA first tower of the game. A single man with a gun, upgradable with the ability to shoot normal bullets and slow field bullets at max level.
 * 
 * @author Joshua Stevens
 * @version Version 3.2
 */
public class BasicTower extends Tower {
    public String imageName = "Basic_tower.png";
    private int slowShotCounter = 0;
    private final int slowShotInterval = 5; // Fire slow bullet every 5 shots
    private static int SHOT_POOL_SIZE = 10;
    private static GreenfootSound[] SHOT_POOL = new GreenfootSound[SHOT_POOL_SIZE];
    
    // testing GreenfootSound pool, as lag can occur when loading sounds repeatedly. whihc sucks.
    static {
        for (int i = 0; i < SHOT_POOL_SIZE; i++) {
            SHOT_POOL[i] = new GreenfootSound("gunShotSmall.mp3");
        }
    }
    

    /**
     * Constructor for BasicTower.
     * Initializes the tower with a base cost, upgrade cost per level, and sets the image.
     */
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
            
        if (level == 2) {
            imageName = "Basic_tower_1.png";
        }
            
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
        
        for (GreenfootSound soundThing: SHOT_POOL)
        {
            if (!soundThing.isPlaying())
            {
                AudioManager.playSFX(soundThing);
                break;
            }
        }
        
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
