import greenfoot.*;
/**
 * Write a description of class Base here.
 * 
 * @Joshua Stevens
 * @version (a version number or a date)
 */
public class FlameThrowerTower extends Tower {
    private int coneProjectiles = 3;  // Start with 3 projectiles
    private final int maxProjectiles = 6;  // Max projectiles at max upgrade
    private int coneAngle = 60;  // Total cone angle in degrees (spread)
    private int shots = 0;
    private GreenfootSound flameSound = new GreenfootSound("flameShooting.mp3");
    private int soundCooldown = 0;
    private final int SOUND_STOP_DELAY = 10; // frames after last shot before stopping

    public FlameThrowerTower() {
        GreenfootImage img = new GreenfootImage("FlameThrower_tower.png");
        img.scale(80, 80);
        setImage(img);

        cooldownTime = 7;    // Much faster firing rate
        range = 100;         // Shorter range for flame thrower
        damage = 1;
        bulletSpeed = 15;    // Speed of flame projectiles

        baseCost = 4500;
        upgradeCostPerLevel = 900;
        upgradeCost = upgradeCostPerLevel;
        totalInvested = baseCost;
    }

    @Override
    public boolean upgrade() {
        GameWorld world = (GameWorld) getWorld();
        if (level < maxLevel && world.spendMoney(upgradeCost)) {
            level++;
            damage = Math.max(3, damage + 1);
            cooldownTime = Math.max(3, cooldownTime - 1);  // Faster firing with upgrades
            // Increase coneProjectiles gradually up to maxProjectiles
            if (coneProjectiles < maxProjectiles) {
                coneProjectiles++;
            }
            totalInvested += upgradeCost;
            upgradeCost += 300;
            updateImage();
            return true;
        }
        return false;
    }

    @Override
    protected void updateImage() {
        GreenfootImage img = new GreenfootImage("FlameThrower_tower.png");
        img.scale(80, 80);
        setImage(img);
        super.updateImage();
    }

    @Override
    protected Enemy getEnemyInRange() {
        return super.getEnemyInRange();
    }

    @Override
    protected void shoot(Enemy target) {
        if (target == null) return;
    
        shots++;
    
        double baseAngle = Math.atan2(target.getY() - getY(), target.getX() - getX());
        int baseRotation = (int) Math.toDegrees(baseAngle);
        setRotation(baseRotation);
    
        for (int i = 0; i < coneProjectiles; i++) {
            double offset = ((double)i / (coneProjectiles - 1) - 0.5) * coneAngle;
            double projectileAngle = Math.toDegrees(baseAngle) + offset;
    
            FlameProjectile fp = new FlameProjectile(target, damage, bulletSpeed, 50, 20, level, this);
            fp.setRotation((int) projectileAngle);
            getWorld().addObject(fp, getX(), getY());
        }
    
        if (shots == 25) {
            shots = 0;
            if (level >= maxLevel) {
                getWorld().addObject(new FlameRing(200, 5, 10), getX(), getY());
                AudioManager.playSpecialSFX(new GreenfootSound("FlameRing.mp3"));
            }
        }
    
        // sound logic
        soundCooldown = SOUND_STOP_DELAY; // Reset delay every time a shot happens
        if (!flameSound.isPlaying()) {
            flameSound.playLoop();
        }
    }

    @Override
    public void act() {
        super.act(); // Ensure superclass behavior runs
        if (soundCooldown > 0) {
            soundCooldown--;
        } else {
            if (flameSound.isPlaying()) {
                flameSound.stop();
            }
        }
    }

    
}
