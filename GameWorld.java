import greenfoot.*;
import java.util.List;
import java.util.ArrayList;

public class GameWorld extends World {
    private int wave = 0;
    private int enemiesSpawned = 0;
    private int enemiesToSpawn = 0;
    private int spawnDelay = 60;
    private int spawnTimer = 0;
    private int spawnBatchSize = 3;
    private List<Integer> usedYPositions = new ArrayList<>();
    private int money = 30000;

    private Label moneyLabel;
    private Label waveLabel;
    private Label wavePrompt;
    private TowerPreview towerPreview = null;

    private boolean waitingForNextWave = true;
    private boolean keyHeld = false;
    private boolean towerPlacedThisClick = false;
    private int lives = 100; // or however many you want
    private Label livesLabel;

    public GameWorld() {
        super(1000, 600, 1);

        moneyLabel = new Label("Money: $" + money, 30);
        waveLabel = new Label("Wave: " + wave, 30);
        wavePrompt = new Label("Press SPACE to start first wave", 24);
        wavePrompt.setLineColor(Color.BLACK);
        livesLabel = new Label("Lives: " + lives, 30);
        
        addObject(livesLabel, 400, 30); // Position as you like
        addObject(moneyLabel, 100, 30);
        addObject(waveLabel, 250, 30);
        addObject(wavePrompt, getWidth() / 2, getHeight() - 30);
    }

    public void act() {
        handleEnemySpawning();
        handleWaveProgression();
        handleTowerDragging();
        handleTowerClickUpgrade();
        resetInputFlags();
    }

    // === Enemy Spawning Logic ===
    private void handleEnemySpawning() {
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
    }

    // Updated spawnEnemy method ONLY:
    private void spawnEnemy() {
        int y = getUniqueYPosition();
        int speed = getEnemySpeed();

        int type = Greenfoot.getRandomNumber(100);

        if (wave <= 2) {
            // Waves 1-2: Only normal enemies
            addObject(new Enemy(speed), 0, y);
        } else if (wave >= 3 && wave < 7) {
            // Waves 3-6: Normal and Fast enemies
            if (type < 70) {
                addObject(new Enemy(speed), 0, y);
            } else {
                addObject(new FastEnemy(speed), 0, y);
            }
        } else if (wave >= 7 && wave < 18) {
            // Waves 7-17: Normal, Fast, and Tank enemies
            if (type < 50) {
                addObject(new Enemy(speed), 0, y);
            } else if (type < 85) {
                addObject(new FastEnemy(speed), 0, y);
            } else {
                addObject(new TankEnemy(speed), 0, y);
            }
        } else {
            // Waves 18+: All enemy types including BigEnemy
            if (type < 40) {
                addObject(new Enemy(speed), 0, y);
            } else if (type < 70) {
                addObject(new FastEnemy(speed), 0, y);
            } else if (type < 90) {
                addObject(new TankEnemy(speed), 0, y);
            } else {
                addObject(new BigEnemy(speed), 0, y);
            }
        }
    }

    // Updated getEnemySpeed method ONLY:
    private int getEnemySpeed() {
        // Slower speed ramping — increase speed slowly with wave number
        return 1 + wave / 5;
    }

    private int getUniqueYPosition() {
        int y;
        int attempts = 0;
        do {
            y = 100 + Greenfoot.getRandomNumber(200);
            attempts++;
        } while (usedYPositions.contains(y) && attempts < 10);
        usedYPositions.add(y);
        return y;
    }

    // === Wave Control ===
    private void handleWaveProgression() {
        if (waitingForNextWave && Greenfoot.isKeyDown("space")) {
            nextWave();
            waitingForNextWave = false;
            wavePrompt.setValue("");
            if (spawnBatchSize < 5) spawnBatchSize++;
        }

        if (!waitingForNextWave && enemiesSpawned == enemiesToSpawn && getObjects(Enemy.class).isEmpty()) {
            waitingForNextWave = true;
            wavePrompt.setValue("Press SPACE to start next wave");
            addMoney(200); // Give $200 for surviving the wave
        }
    }

    private void nextWave() {
        wave++;
        enemiesToSpawn = (int)(Math.pow(wave, 1.5) + 2);
        enemiesSpawned = 0;
        spawnTimer = 0;
        usedYPositions.clear();
        updateWaveLabel();

        if (wave % 3 == 0 && spawnBatchSize < 6) {
            spawnBatchSize++;
        }
        if (wave % 5 == 0 && spawnDelay > 20) {
            spawnDelay -= 5;
        }
    }

    // === Tower Drag-and-Drop ===
    private void handleTowerDragging() {
        if (towerPreview == null) {
            if (!keyHeld) {
                if (Greenfoot.isKeyDown("1")) {
                    startDraggingTower("Basic");
                    keyHeld = true;
                } else if (Greenfoot.isKeyDown("2")) {
                    startDraggingTower("Sniper");
                    keyHeld = true;
                } else if (Greenfoot.isKeyDown("3")) {
                    startDraggingTower("MachineGun");
                    keyHeld = true;
                } else if (Greenfoot.isKeyDown("4")) {
                    startDraggingTower("Nuke");
                    keyHeld = true;
                }
            }
        } else {
            MouseInfo mi = Greenfoot.getMouseInfo();
            if (mi != null) {
                towerPreview.setLocation(mi.getX(), mi.getY());

                if (Greenfoot.mouseClicked(null) && !towerPlacedThisClick) {
                    if (mi.getButton() == 1) {
                        placeTower(towerPreview.getTowerType(), mi.getX(), mi.getY());
                        towerPlacedThisClick = true;
                    } else if (mi.getButton() == 3) {
                        cancelDragging();
                        towerPlacedThisClick = true;
                    }
                }
            }

            if (Greenfoot.isKeyDown("escape")) {
                cancelDragging();
            }

            if (Greenfoot.isKeyDown("1") && !towerPreview.getTowerType().equals("Basic")) {
                startDraggingTower("Basic");
            } else if (Greenfoot.isKeyDown("2") && !towerPreview.getTowerType().equals("Sniper")) {
                startDraggingTower("Sniper");
            } else if (Greenfoot.isKeyDown("3") && !towerPreview.getTowerType().equals("MachineGun")) {
                startDraggingTower("MachineGun");
            } else if (Greenfoot.isKeyDown("4") && !towerPreview.getTowerType().equals("Nuke")) {
                startDraggingTower("Nuke");
            }
        }

        if (!Greenfoot.isKeyDown("1") && !Greenfoot.isKeyDown("2") && !Greenfoot.isKeyDown("3") && !Greenfoot.isKeyDown("4")) {
            keyHeld = false;
        }
    }

    private void startDraggingTower(String towerType) {
        if (towerPreview != null) {
            removeObject(towerPreview);
        }
        towerPreview = new TowerPreview(towerType);
        addObject(towerPreview, getWidth() / 2, getHeight() / 2);
    }

    private void placeTower(String towerType, int x, int y) {
        int cost = getTowerCost(towerType);
        if (spendMoney(cost)) {
            Tower tower = createTower(towerType);
            addObject(tower, x, y);
            removeObject(towerPreview);
            towerPreview = null;
        } else {
            System.out.println("Not enough money!");
        }
    }

    private void cancelDragging() {
        if (towerPreview != null) {
            removeObject(towerPreview);
            towerPreview = null;
        }
    }

    private int getTowerCost(String towerType) {
        switch (towerType) {
            case "Sniper": return 300;
            case "Basic": return 50;
            case "MachineGun": return 750;
            case "Nuke": return 5000;
            default: return 0;
        }
    }

    private Tower createTower(String towerType) {
        switch (towerType) {
            case "Sniper": return new SniperTower();
            case "MachineGun": return new MachineGunTower();
            case "Basic": return new BasicTower();
            case "Nuke": return new NukeTower();
            default: return new BasicTower();
        }
    }

    // === Tower Upgrade ===
    private void handleTowerClickUpgrade() {
        if (towerPreview == null && Greenfoot.mouseClicked(null) && !towerPlacedThisClick) {
            MouseInfo mi = Greenfoot.getMouseInfo();
            if (mi != null) {
                Actor clicked = getObjectsAt(mi.getX(), mi.getY(), Tower.class).stream().findFirst().orElse(null);
                if (clicked != null) {
                    Tower tower = (Tower) clicked;
                    if (mi.getButton() == 1) {
                        if (!tower.upgrade()) {
                            System.out.println("Not enough money or max level reached!");
                        }
                    } else if (mi.getButton() == 3) {
                        tower.sell();
                    }
                }
            }
        }
    }

    // === Reset Mouse Flags ===
    private void resetInputFlags() {
        if (!Greenfoot.mousePressed(null)) {
            towerPlacedThisClick = false;
        }
    }

    // === Money/Wave UI ===
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

    private void updateWaveLabel() {
        waveLabel.setValue("Wave: " + wave);
    }
    
    public void loseLife(int amount) {
        lives -= amount;
        updateLivesLabel();
        
        if (lives <= 0) {
            gameOver();
        }
    }

    private void updateLivesLabel() {
        livesLabel.setValue("Lives: " + lives);
    }

    private void gameOver() {
        showText("Game Over!", getWidth() / 2, getHeight() / 2);
        Greenfoot.stop(); // Stops the game
    }

}
