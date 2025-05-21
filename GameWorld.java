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
    private int money = 100; // starting money
    private Label moneyLabel;
    private Label waveLabel;

    public GameWorld() {
        super(600, 400, 1);
        moneyLabel = new Label("Money: $" + money, 30);
        waveLabel = new Label("Wave: " + wave, 30);
        addObject(waveLabel, 250, 30);
        addObject(moneyLabel, 100, 30);
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
            spawnBatchSize++;
        }
    
        if (Greenfoot.mouseClicked(this)) {
            MouseInfo mi = Greenfoot.getMouseInfo();
            if (spendMoney(50)) { // tower costs $50
                addObject(new Tower(), mi.getX(), mi.getY());
            } else {
                System.out.println("Not enough money to place tower!");
            }
        }
    }



    private void spawnEnemy() {
    int y = getUniqueYPosition();
    int speed = getEnemySpeed();

    if (enemiesSpawned > 0 && enemiesSpawned % 5 == 0) {
        addObject(new BigEnemy(speed - 1), 0, y); // slower but stronger-looking
    } else {
        addObject(new Enemy(speed), 0, y);
    }
    }




    private void nextWave() {
        wave++;
        enemiesToSpawn *= 2;
        enemiesSpawned = 0;
        spawnTimer = 0;
        usedYPositions.clear(); // reset used positions for next wave
        updateWaveLabel();
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
    
    public int getMoney() {
        return money;
    }

    public void addMoney(int amount) {
        money += amount;
        updateMoneyLabel();
    }

    public boolean spendMoney(int amount) {
        if (money >= amount) {
            money -= amount;
            updateMoneyLabel();
            return true;
        }
        return false;
    }


    private void updateMoneyLabel() {
        moneyLabel.setValue("Money: $" + money);
    }
    private void updateWaveLabel() 
    {
        waveLabel.setValue("Wave: " + wave);
    }
    private int getEnemySpeed() {
    return 1 + wave; // Speed increases each wave (1, 2, 3, ...)
    }

}


