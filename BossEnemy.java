import greenfoot.*;
/**
 * Enemy/BossEnemy, this will blow up someones house
 * the boss enemy that spawns at wave 20, has the highest hp, lowest speed, and creates enemies when killed
 * @author Joshua Stevens, Denny Ung
 * @version Version 3.0.5 (June 7, 2025)
 */
public class BossEnemy extends Enemy {

    public BossEnemy(int speed, int health, int moneyDeath) {
        super(speed, health, moneyDeath);
        GreenfootImage img = new GreenfootImage("Boss.png");
        img.scale(360, 160);
        setImage(img);
        if (!isDead) {
            this.movement = new GreenfootSound("rollingTank.mp3");
        }
    }

    @Override
    public void takeDamage(int amount) {
        if (isDead) return;
        health -= amount;
        if (health <= 0) {
            super.isDead = true;
            GameWorld gw = (GameWorld) getWorld();
            gw.addMoney(1000); 

            // Spawn additional enemies on death    
            spawnMinions(gw);

        }
    }

    private void spawnMinions(GameWorld world) {
        int wave = world.getWave(); 
        int x = getX();
        int y = getY();
    
        // Decide number of enemies to spawn based on wave
        int numMinions = 3 + wave / 5; // e.g. wave 10 → 5 minions
    
        for (int i = 0; i < numMinions; i++) {
            int offsetY = Greenfoot.getRandomNumber(60) - 30;
    
            int typeRoll = Greenfoot.getRandomNumber(100);
            Enemy minion;
    
            if (typeRoll < 40) {
                minion = new TankEnemy(
                    world.getEnemySpeed("Tank"),
                    world.getEnemyHealth("Tank"),
                    world.getEnemyMoneyDeath("Tank")
                );
            } else if (typeRoll < 80) {
                minion = new FastEnemy(
                    world.getEnemySpeed("Fast"), 
                    world.getEnemyHealth("Fast"),
                    world.getEnemyMoneyDeath("Fast")
                );
            } else {
                minion = new BigEnemy(
                    world.getEnemySpeed("Big"), 
                    world.getEnemyHealth("Big"),
                    world.getEnemyMoneyDeath("Big")
                );
            }
            
    
            world.addObject(minion, x, y + offsetY);
        }
    }


    @Override
    public int getLifeDamage() {
        return 100;
    }
}
