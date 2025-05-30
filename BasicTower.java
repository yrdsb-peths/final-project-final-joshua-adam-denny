import greenfoot.*;

public class BasicTower extends Tower {
    public String imageName = "Basic_tower.png";
    public BasicTower() {
        GreenfootImage img = new GreenfootImage(imageName);
        img.scale(50, 50);
        setImage(img);

        cooldownTime = 60;             
        range = 200;                  
        damage = 2;                  
        bulletSpeed = 10;

        baseCost = 50;
        upgradeCostPerLevel = 10;
        upgradeCost = upgradeCostPerLevel;
        totalInvested = baseCost;
    }

    @Override
    public boolean upgrade() {
        GameWorld world = (GameWorld) getWorld(); // ✅ correct cast
        if (level < maxLevel && world.spendMoney(upgradeCost)) {
            level++;
            damage += 2;
            range += 15;
            cooldownTime = Math.max(30, cooldownTime - 10);
            totalInvested += upgradeCost; // track what's actually spent
            upgradeCost += 10;
            //if(level == 1) {
            //    imageName = "Basic_tower_1.png";
            //}

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


}
