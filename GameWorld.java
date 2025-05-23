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
    private int money = 100;

    private Label moneyLabel;
    private Label waveLabel;
    private Label wavePrompt;
    private TowerPreview towerPreview = null;

    private boolean waitingForNextWave = true;
    private boolean keyHeld = false;
    private boolean towerPlacedThisClick = false;
    private int lives = 100;
    private Label livesLabel;

    public GameWorld() {
        super(1000, 600, 1);
        setBackground("grass.png");

        moneyLabel = new Label("Money: $" + money, 30);
        waveLabel = new Label("Wave: " + wave, 30);
        wavePrompt = new Label("Press SPACE to start first wave", 24);
        wavePrompt.setLineColor(Color.BLACK);
        livesLabel = new Label("Lives: " + lives, 30);

        Base base = new Base();
        addObject(base, 925, 300);

        addObject(livesLabel, 400, 30);
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

    private void spawnEnemy() {
        int y = getUniqueYPosition();
        int type = Greenfoot.getRandomNumber(100);
        int hp;
        int speed;

        if (wave <= 2) {
            hp = getEnemyHealth("Basic");
            speed = getEnemySpeed("Basic");
            addObject(new BasicEnemy(speed, hp), 0, y);
        } else if (wave >= 3 && wave < 7) {
            if (type < 70) {
                hp = getEnemyHealth("Basic");
                speed = getEnemySpeed("Basic");
                addObject(new BasicEnemy(speed, hp), 0, y);
            } else {
                hp = getEnemyHealth("Fast");
                speed = getEnemySpeed("Fast");
                addObject(new FastEnemy(speed, hp), 0, y);
            }
        } else if (wave >= 7 && wave < 18) {
            if (type < 50) {
                hp = getEnemyHealth("Basic");
                speed = getEnemySpeed("Basic");
                addObject(new BasicEnemy(speed, hp), 0, y);
            } else if (type < 85) {
                hp = getEnemyHealth("Fast");
                speed = getEnemySpeed("Fast");
                addObject(new FastEnemy(speed, hp), 0, y);
            } else {
                hp = getEnemyHealth("Tank");
                speed = getEnemySpeed("Tank");
                addObject(new TankEnemy(speed, hp), 0, y);
            }
        } else {
            if (type < 40) {
                hp = getEnemyHealth("Basic");
                speed = getEnemySpeed("Basic");
                addObject(new BasicEnemy(speed, hp), 0, y);
            } else if (type < 70) {
                hp = getEnemyHealth("Fast");
                speed = getEnemySpeed("Fast");
                addObject(new FastEnemy(speed, hp), 0, y);
            } else if (type < 90) {
                hp = getEnemyHealth("Tank");
                speed = getEnemySpeed("Tank");
                addObject(new TankEnemy(speed, hp), 0, y);
            } else {
                hp = getEnemyHealth("Big");
                speed = getEnemySpeed("Big");
                addObject(new BigEnemy(speed, hp), 0, y);
            }
        }
    }

    private int getUniqueYPosition() {
        int y;
        int attempts = 0;
        do {
            y = 100 + Greenfoot.getRandomNumber(500);
            attempts++;
        } while (usedYPositions.contains(y) && attempts < 10);
        usedYPositions.add(y);
        return y;
    }

    private int getEnemyBaseHealth(String type) {
        switch (type) {
            case "Basic": return 2;
            case "Fast": return 4;
            case "Tank": return 15;
            case "Big": return 50;
            default: return 1;
        }
    }

    private int getEnemyHealth(String type) {
        int base = getEnemyBaseHealth(type);

        if (wave <= 20) {
            return base;
        }

        double multiplier;
        switch (type) {
            case "Basic": multiplier = 1.02; break;
            case "Fast":  multiplier = 1.03; break;
            case "Tank":  multiplier = 1.07; break;
            case "Big":   multiplier = 1.10; break;
            default:      multiplier = 1.02; break;
        }

        int wavesOver20 = wave - 20;
        return (int)(base * (1 + wavesOver20 * 0.05) * Math.pow(multiplier, wavesOver20 * 0.1));

    }

    private int getEnemySpeed(String type) {
        int base = 1 + wave / 5;
        switch (type) {
            case "Fast": return base + 2;
            case "Tank": return Math.max(1, base - 1);
            default: return base;
        }
    }

    private void handleWaveProgression() {
        if (waitingForNextWave && Greenfoot.isKeyDown("space")) {
            nextWave();
            waitingForNextWave = false;
            wavePrompt.setValue("");
            if (spawnBatchSize < 5) spawnBatchSize++;
        }

        if (!waitingForNextWave && enemiesSpawned == enemiesToSpawn && getObjects(BasicEnemy.class).isEmpty()) {
            waitingForNextWave = true;
            wavePrompt.setValue("Press SPACE to start next wave");
            addMoney(200);
        }
    }

    private void nextWave() {
        wave++;
        enemiesToSpawn = (int)(Math.pow(wave, 1.5) + 2);
        enemiesSpawned = 0;
        spawnTimer = 0;
        usedYPositions.clear();
        updateWaveLabel();

        if (wave % 3 == 0 && spawnBatchSize < 6) spawnBatchSize++;
        if (wave % 5 == 0 && spawnDelay > 20) spawnDelay -= 5;
    }

    private void handleTowerDragging() {
        if (towerPreview == null) {
            if (!keyHeld) {
                trySwitchPreview("1", "Basic");
                trySwitchPreview("2", "Sniper");
                trySwitchPreview("3", "MachineGun");
                trySwitchPreview("4", "FlameThrower");
                trySwitchPreview("5", "Nuke");
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

            // Allow switching tower preview while dragging
            trySwitchPreview("1", "Basic");
            trySwitchPreview("2", "Sniper");
            trySwitchPreview("3", "MachineGun");
            trySwitchPreview("4", "FlameThrower");
            trySwitchPreview("5", "Nuke");
        }

        if (!Greenfoot.isKeyDown("1") && !Greenfoot.isKeyDown("2") &&
            !Greenfoot.isKeyDown("3") && !Greenfoot.isKeyDown("4") && !Greenfoot.isKeyDown("5")) {
            keyHeld = false;
        }
    }

    private void startDraggingTower(String towerType) {
        if (towerPreview != null) removeObject(towerPreview);
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

    private void trySwitchPreview(String key, String towerType) {
        if (Greenfoot.isKeyDown(key)) {
            if (towerPreview == null || !towerPreview.getTowerType().equals(towerType)) {
                startDraggingTower(towerType);
                keyHeld = true;
            }
        }
    }

    private int getTowerCost(String towerType) {
        switch (towerType) {
            case "Basic": return 50;
            case "Sniper": return 300;
            case "MachineGun": return 750;
            case "FlameThrower": return 4500;
            case "Nuke": return 10000;
            default: return 0;
        }
    }

    private Tower createTower(String towerType) {
        switch (towerType) {
            case "Basic": return new BasicTower();
            case "Sniper": return new SniperTower();
            case "MachineGun": return new MachineGunTower();
            case "FlameThrower": return new FlameThrowerTower();
            case "Nuke": return new NukeTower();
            default: return new BasicTower();
        }
    }

    private void handleTowerClickUpgrade() {
        if (towerPreview == null && Greenfoot.mouseClicked(null) && !towerPlacedThisClick) {
            MouseInfo mi = Greenfoot.getMouseInfo();
            if (mi != null) {
                List<Tower> towers = getObjectsAt(mi.getX(), mi.getY(), Tower.class);
                if (!towers.isEmpty()) {
                    Tower tower = towers.get(0);
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

    private void resetInputFlags() {
        if (!Greenfoot.mousePressed(null)) {
            towerPlacedThisClick = false;
        }
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
        Greenfoot.stop();
    }
}
