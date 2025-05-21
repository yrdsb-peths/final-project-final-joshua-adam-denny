import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

public class GameWorld extends World {
    private int wave = 0;
    private int enemiesSpawned = 0;
    private int enemiesToSpawn = 0;
    private int spawnDelay = 60; // delay between spawns (60 = 1 second at 60 FPS)
    private int spawnTimer = 0;
    private int spawnBatchSize = 3; // how many to spawn per cycle
    private List<Integer> usedYPositions = new ArrayList<>();
    private int money = 200; // starting money
    private Label moneyLabel;
    private Label waveLabel;
    private Label wavePrompt;

    private boolean waitingForNextWave = true;


    public GameWorld() {
        super(600, 400, 1);
        
        moneyLabel = new Label("Money: $" + money, 30);
        waveLabel = new Label("Wave: " + wave, 30);
        wavePrompt = new Label("Press SPACE to start first wave", 24);
        wavePrompt.setLineColor(Color.BLACK);
    
        addObject(moneyLabel, 100, 30);
        addObject(waveLabel, 250, 30);
        addObject(wavePrompt, getWidth() / 2, getHeight() - 30);
    
        waitingForNextWave = true;  // Wait for space to begin wave 1
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
    
        // Start wave when space is pressed
        if (waitingForNextWave && Greenfoot.isKeyDown("space")) {
            nextWave();
            waitingForNextWave = false;
            wavePrompt.setValue(""); // Hide prompt
            if (spawnBatchSize < 5) spawnBatchSize++;
        }
        
        // Spawn enemies during an active wave
        if (!waitingForNextWave && enemiesSpawned < enemiesToSpawn) {
            spawnTimer++;
        
            if (spawnTimer >= spawnDelay) {
                int remaining = enemiesToSpawn - enemiesSpawned;
                int batch = Math.min(spawnBatchSize, remaining);
        
                for (int i = 0; i < batch; i++) {
                    spawnEnemy();
                    enemiesSpawned++;
                }
        
                spawnTimer = 0;
            }
        }
        
        // After all enemies are defeated, wait for space to trigger next wave
        if (!waitingForNextWave && enemiesSpawned == enemiesToSpawn && getObjects(Enemy.class).isEmpty()) {
            waitingForNextWave = true;
            wavePrompt.setValue("Press SPACE to start next wave");
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

    if (enemiesSpawned > 0 && enemiesSpawned % 20 == 0) {
        addObject(new BigEnemy(speed - 1), 0, y); // slower but stronger-looking
    } else {
        addObject(new Enemy(speed), 0, y);
    }
    }




    private void nextWave() {
        wave++;
    
        // Slow early ramping, fast late game ramp
        enemiesToSpawn = (int)(Math.pow(wave, 1.5) + 2); // e.g. wave 1 = 3, wave 2 = 4, wave 5 = 13, wave 10 = 33
    
        enemiesSpawned = 0;
        spawnTimer = 0;
        usedYPositions.clear();
        updateWaveLabel();
    
        // Optional: Increase batch size gradually
        if (wave % 3 == 0 && spawnBatchSize < 6) {
            spawnBatchSize++;
        }
    
        // Optional: Decrease spawn delay (faster waves later)
        if (wave % 5 == 0 && spawnDelay > 20) {
            spawnDelay -= 5;
        }
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


