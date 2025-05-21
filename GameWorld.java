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
    private int money = 200;
    private Label moneyLabel;
    private Label waveLabel;
    private Label wavePrompt;
    private TowerPreview towerPreview = null;
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

        if (waitingForNextWave && Greenfoot.isKeyDown("space")) {
            nextWave();
            waitingForNextWave = false;
            wavePrompt.setValue("");
            if (spawnBatchSize < 5) spawnBatchSize++;
        }

        if (!waitingForNextWave && enemiesSpawned == enemiesToSpawn && getObjects(Enemy.class).isEmpty()) {
            waitingForNextWave = true;
            wavePrompt.setValue("Press SPACE to start next wave");
        }

        // --- Tower Drag Placement ---
        if (towerPreview == null) {
            if (Greenfoot.isKeyDown("1")) {
                startDraggingTower("Basic");
            } else if (Greenfoot.isKeyDown("2")) {
                startDraggingTower("Sniper");
            }
        } else {
            MouseInfo mi = Greenfoot.getMouseInfo();
            if (mi != null) {
                towerPreview.setLocation(mi.getX(), mi.getY());

                // Left-click to place
                if (Greenfoot.mouseClicked(null)) {
                    placeTower(towerPreview.getTowerType(), mi.getX(), mi.getY());
                }

                // Right-click to cancel
                if (Greenfoot.mouseClicked(null) && mi.getButton() == 3) {
                    cancelDragging();
                }
            }

            // ESC to cancel
            if (Greenfoot.isKeyDown("escape")) {
                cancelDragging();
            }

            // Switch tower type while dragging
            if (Greenfoot.isKeyDown("1") && !towerPreview.getTowerType().equals("Basic")) {
                startDraggingTower("Basic");
            } else if (Greenfoot.isKeyDown("2") && !towerPreview.getTowerType().equals("Sniper")) {
                startDraggingTower("Sniper");
            }
        }

        // --- Upgrade Only When Not Dragging ---
        if (towerPreview == null && Greenfoot.mouseClicked(null)) {
            MouseInfo mi = Greenfoot.getMouseInfo();
            if (mi != null) {
                Actor clicked = getObjectsAt(mi.getX(), mi.getY(), Tower.class).stream().findFirst().orElse(null);
                if (clicked != null) {
                    Tower tower = (Tower) clicked;
                    if (!tower.upgrade()) {
                        System.out.println("Not enough money or max level reached!");
                    }
                }
            }
        }
    }

    private void spawnEnemy() {
        int y = getUniqueYPosition();
        int speed = getEnemySpeed();

        if (enemiesSpawned > 0 && enemiesSpawned % 10 == 0) {
            addObject(new BigEnemy(speed - 1), 0, y);
        } else {
            int type = Greenfoot.getRandomNumber(100);
            if (type < 60) {
                addObject(new Enemy(speed), 0, y);
            } else if (type < 85) {
                addObject(new FastEnemy(speed), 0, y);
            } else {
                addObject(new TankEnemy(speed), 0, y);
            }
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

    private int getEnemySpeed() {
        return 1 + wave;
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
        if (money >= cost) {
            money -= cost;
            updateMoneyLabel();
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
            case "Sniper": return 100;
            case "Basic": return 50;
            default: return 50;
        }
    }

    private Tower createTower(String towerType) {
        switch (towerType) {
            case "Sniper": return new SniperTower();
            case "Basic": return new BasicTower();
            default: return new BasicTower();
        }
    }
}
