import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

public class GameWorld extends World {
    private int wave = 1;
    private int enemiesSpawned = 0;
    private int enemiesToSpawn = 1;
    private int spawnDelay = 60; // delay between spawns (60 = 1 second at 60 FPS)
    private int spawnTimer = 0;
    private int spawnBatchSize = 3; // how many to spawn per cycle
    private List<Integer> usedYPositions = new ArrayList<>();

    public GameWorld() {
        super(600, 400, 1);
    }

    public void act() {
        spawnTimer++;
    
        if (enemiesSpawned < enemiesToSpawn && spawnTimer >= spawnDelay) {
            int remaining = enemiesToSpawn - enemiesSpawned;
            int batch = Math.min(spawnBatchSize, remaining);
    
            for (int i = 0; i < batch; i++) {
                spawnEnemy();
                enemiesSpawned++;
            }
    
            spawnTimer = 0;
        }
    
        if (enemiesSpawned == enemiesToSpawn && getObjects(Enemy.class).isEmpty()) {
            nextWave();
        }
        newTower();
    }


    private void spawnEnemy() {
        int y = getUniqueYPosition();
        addObject(new Enemy(), 0, y);
    }


    private void nextWave() {
        wave++;
        enemiesToSpawn *= 2;
        enemiesSpawned = 0;
        spawnTimer = 0;
        usedYPositions.clear(); // reset used positions for next wave
    }

    public void newTower() {
        if (Greenfoot.mouseClicked(this)) {
            MouseInfo mi = Greenfoot.getMouseInfo();
            addObject(new Tower(), mi.getX(), mi.getY());
        }
    }
    private int getUniqueYPosition() {
        int y;
        int attempts = 0;
        do {
            y = 100 + Greenfoot.getRandomNumber(200); // from 100 to 300
            attempts++;
        } while (usedYPositions.contains(y) && attempts < 10);
    
        usedYPositions.add(y);
        return y;
    }


    
    
}


