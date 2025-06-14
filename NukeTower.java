import greenfoot.*;
/**
 * NukeTower Class is a powerful tower that launches a nuke missile at enemies within its range.
 * Ability to upgrade to increase damage, explosion radius, and reduce cooldown time.
 * 
 * 
 * @author Joshua Stevens
 * @version Version 1.0
 */
public class NukeTower extends Tower {
    private int missileCooldown = 0;
    private int missileCooldownTime = 2700;
    private int nukeDamage = 500;
    private int missileSpeed = 2;
    private int explosionRadius = 300;

    // Animation fields
    private String currentImage;
    private GreenfootImage[] fireFrames;
    private int animationIndex = 0;
    private int animationTimer = 0;
    private boolean isAnimating = false;
    private boolean hasFired = false;
    private int frameDelay = 5;
    int fuseTime = 300;
    private Enemy lockedTarget = null; // Stores the enemy to fire at during animation
    
    /**
     * Constructor for NukeTower.
     * Initializes the tower with its base cost, upgrade cost, and other properties.
     */
    public NukeTower() {
        updateImage();

        baseCost = 10000;
        upgradeCostPerLevel = 2000;
        upgradeCost = upgradeCostPerLevel;

        level = 0;
        maxLevel = 3;
        
        loadAnimationFrames();
        range = 200;
        totalInvested = baseCost;
    }
    
    private void loadAnimationFrames() {
        fireFrames = new GreenfootImage[12];
        for (int i = 0; i < fireFrames.length; i++) {
            if(level == 3) {
                GreenfootImage img = new GreenfootImage("Nuke_tower/Nuke_tower" + i + "_1.png"); // Note the folder name
                img.scale(150, 150);
                fireFrames[i] = img;
            } else {
                GreenfootImage img = new GreenfootImage("Nuke_tower/Nuke_tower" + i + ".png"); // Note the folder name
                img.scale(150, 150);
                fireFrames[i] = img;
            }
        }
    }


    @Override
    public void act() {
        
        if (isAnimating && gw.getStatus() == GameWorld.Status.RUNNING){
            animationTimer++;
            if (animationTimer % frameDelay == 0) {
                if (animationIndex < fireFrames.length) {
                    setImage(fireFrames[animationIndex]);

                    if (animationIndex == 7 && !hasFired && lockedTarget != null) {
                        launchNuke(lockedTarget); // fire during frame 7
                        hasFired = true;
                    }

                    animationIndex++;
                } else {
                    isAnimating = false;
                    hasFired = false;
                    lockedTarget = null;
                    updateImage(); // revert to base image
                }
            }
            return; // Skip firing logic during animation
        }

        if (missileCooldown > 0) {
            missileCooldown--;
        } else {
            Enemy target = getEnemyInRange();
            if (target != null) {
                startFiringAnimation(target);
                missileCooldown = missileCooldownTime;
            }
        }
    }

    private void startFiringAnimation(Enemy target) {
        isAnimating = true;
        animationIndex = 0;
        animationTimer = 0;
        hasFired = false;
        lockedTarget = target;
    }

    private void launchNuke(Enemy target) {
        if (getWorld() != null) {
            NukeMissile missile = new NukeMissile(target, nukeDamage, missileSpeed, explosionRadius, this, 130, fuseTime, level, 2);
            getWorld().addObject(missile, getX(), getY());
            AudioManager.playSFX(new GreenfootSound("missileLaunchBig.mp3"));
        }
    }

    @Override
    public boolean upgrade() {
        GameWorld world = (GameWorld) getWorld();
        if (level < maxLevel && world.spendMoney(upgradeCost)) {
            level++;
            totalInvested += upgradeCost; // track what's actually spent
            upgradeCost += 2000;
            missileSpeed += 1;
            missileCooldownTime = missileCooldownTime - 700;
            nukeDamage += 250;
            explosionRadius += 50;
            updateImage();
            loadAnimationFrames();
            return true;
        }
        return false;
    }

    @Override
    protected void updateImage() {
        if (level == 3) {
            currentImage = ("Nuke_tower/Nuke_tower0_1.png");
        } else {
            currentImage = ("Nuke_tower/Nuke_tower0.png");
        }
        GreenfootImage baseImg = new GreenfootImage(currentImage);
        
        baseImg.scale(150, 150);

        if (level == 1) {
            setImage(createOutlinedImage(baseImg, Color.GREEN));
        } else if (level == 2) {
            setImage(createOutlinedImage(baseImg, Color.RED));
        } else if (level == 3) {
            setImage(createOutlinedImage(baseImg, Color.BLUE));
        } else {
            setImage(baseImg);
        }
    }

    private GreenfootImage createOutlinedImage(GreenfootImage baseImg, Color outlineColor) {
        int w = baseImg.getWidth();
        int h = baseImg.getHeight();
        GreenfootImage outline = new GreenfootImage(w + 4, h + 4);
        outline.clear();

        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                GreenfootImage colored = new GreenfootImage(baseImg);
                replaceColor(colored, outlineColor);
                outline.drawImage(colored, dx + 2, dy + 2);
            }
        }

        outline.drawImage(baseImg, 2, 2);
        return outline;
    }
    
    private void replaceColor(GreenfootImage img, Color outlineColor) {
        int width = img.getWidth();
        int height = img.getHeight();
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color pixel = img.getColorAt(x, y);
                if (pixel.getAlpha() > 0) {
                    img.setColorAt(x, y, outlineColor);
                }
            }
        }
    }
}
