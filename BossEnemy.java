import greenfoot.*;

public class BossEnemy extends Enemy {
    public BossEnemy(int speed, int health, int moneyDeath) {
        super(speed, health, moneyDeath);
        GreenfootImage img = new GreenfootImage("Boss.png");
        img.scale(360, 160);
        setImage(img);
    }

    @Override
    public void takeDamage(int amount) {
        health -= amount;
        if (health <= 0 && getWorld() != null) {
            GameWorld world = (GameWorld) getWorld();
            world.addMoney(1000); 

            // Spawn additional enemies on death
            spawnMinions(world);

            world.removeObject(this);
        }
    }

    private void spawnMinions(GameWorld world) {
        int wave = world.getWave(); // Assuming you have a getWave() method
        int x = getX();
        int y = getY();
    
        // Decide number of enemies to spawn based on wave
        int numMinions = 3 + wave / 5; // e.g. wave 10 → 5 minions
    
        for (int i = 0; i < numMinions; i++) {
            int offsetY = Greenfoot.getRandomNumber(60) - 30;
    
            int typeRoll = Greenfoot.getRandomNumber(100);
            Enemy minion;
    
            if (wave < 7) {
                // Early waves: Mostly Basic and some Fast
                if (typeRoll < 70) {
                    minion = new BasicEnemy(    
                        world.getEnemySpeed("Basic"), 
                        world.getEnemyHealth("Basic"),
                        world.getEnemyMoneyDeath("Basic")
                        );
                } else {
                    minion = new FastEnemy(
                        world.getEnemySpeed("Fast"), 
                        world.getEnemyHealth("Fast"),
                        world.getEnemyMoneyDeath("Fast")
                        );
                }
            } else if (wave < 15) {
                // Mid game: Mix in Tank
                if (typeRoll < 50) {
                    minion = new FastEnemy(
                        world.getEnemySpeed("Fast"), 
                        world.getEnemyHealth("Fast"),
                        world.getEnemyMoneyDeath("Fast")
                        );
                } else if (typeRoll < 85) {
                    minion = new TankEnemy(
                        world.getEnemySpeed("Tank"), 
                        world.getEnemyHealth("Tank"),
                        world.getEnemyMoneyDeath("Tank")
                        );
                } else {
                    minion = new BasicEnemy(
                        world.getEnemySpeed("Basic"), 
                        world.getEnemyHealth("Basic"),
                        world.getEnemyMoneyDeath("Basic")
                        );
                }
            } else {
                // Late game: Mix in Big enemies
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
            }
    
            world.addObject(minion, x, y + offsetY);
        }
    }


    @Override
    public int getLifeDamage() {
        return 100;
    }
}
