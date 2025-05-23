import greenfoot.*;

public class NukeTower extends Tower {
    private int missileCooldown = 0;
    private int missileCooldownTime = 3600; // 60 seconds (60 FPS)
    private int nukeDamage = 500;
    private int missileSpeed = 2;
    private int explosionRadius = 300;

    public NukeTower() {
        GreenfootImage img = new GreenfootImage("Nuke_tower.png");
        img.scale(80, 80);
        setImage(img);

        baseCost = 10000;
        upgradeCostPerLevel = 2000;
        upgradeCost = upgradeCostPerLevel;

        level = 0;
        maxLevel = 3;
    }

    @Override
    public void act() {
        if (missileCooldown > 0) {
            missileCooldown--;
        } else {
            Enemy target = getEnemyInRange();
            if (target != null) {
                launchNuke(target);
                missileCooldown = missileCooldownTime;
            }
        }
    }

    private void launchNuke(Enemy target) {
        NukeMissile missile = new NukeMissile(target, nukeDamage, missileSpeed, explosionRadius);
        getWorld().addObject(missile, getX(), getY());
    }

    @Override
    public boolean upgrade() {
        if (level >= maxLevel) return false;

        GameWorld world = (GameWorld) getWorld();
        int cost = upgradeCost + (level * upgradeCostPerLevel);

        if (world.getMoney() >= cost) {
            world.addMoney(-cost);
            level++;
            totalInvested += cost;

            // Apply upgrades
            missileCooldownTime = Math.max(600, missileCooldownTime - 900); // -10 seconds (600 frames)
            nukeDamage += 250;
            explosionRadius += 50;

            updateImage();
            return true;
        }
        return false;
    }

    @Override
    protected void updateImage() {
        GreenfootImage baseImg = new GreenfootImage("Nuke_tower.png");
        baseImg.scale(60, 60);

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

    // Method to create an image outline with a specified color
    private GreenfootImage createOutlinedImage(GreenfootImage baseImg, Color outlineColor) {
        int w = baseImg.getWidth();
        int h = baseImg.getHeight();

        GreenfootImage outline = new GreenfootImage(w + 4, h + 4); // Adding extra space for the outline
        outline.clear();

        // Draw outline in 8 directions around center
        for (int dx = -1; dx <= 1; dx++) {
            for (int dy = -1; dy <= 1; dy++) {
                if (dx == 0 && dy == 0) continue;
                GreenfootImage colored = new GreenfootImage(baseImg);
                replaceColor(colored, outlineColor);
                outline.drawImage(colored, dx + 2, dy + 2); // Offset by 2 to leave space for outline
            }
        }

        outline.drawImage(baseImg, 2, 2); // Draw the base image centered

        return outline;
    }

    // Helper method to replace color in the image
    private void replaceColor(GreenfootImage img, Color outlineColor) {
        int width = img.getWidth();
        int height = img.getHeight();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                Color pixel = img.getColorAt(x, y);
                if (pixel.getAlpha() > 0) { // Only change non-transparent pixels
                    img.setColorAt(x, y, outlineColor);
                }
            }
        }
    }
}
