import greenfoot.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
import java.io.IOException;
public class GameWorld extends World {
    private static final int WORLD_WIDTH = 1160;
    private static final int WORLD_HEIGHT = 600;
    
    private boolean draggingTower = false;
    private boolean wasOutsideArea = true;
    private int insideCount = 0;
    private List<Integer> sniperBoostTimers = new ArrayList<>();
    private int sniperBoostTimer = 0;
    private int sniperAbilitiesUnlocked = 0;
    private Label sniperAbilitiesAvailableLabel = null;
    private Label sniperCooldownLabel = null;
    private boolean sniperBoostActive = false;
    private SniperAbility sniperIcon = null;
    private int maxLevelSnipersCount = 0;
    private boolean sniperKeyPreviouslyDown = false;
    private boolean autoActivateSniper = false;   // toggles auto activate on/off
    private boolean activateToggleKeyPreviouslyDown = false; // track key press edge


    
    private int wave = 0;
    private int enemiesSpawned = 0;
    private int enemiesToSpawn = 0;
    private int spawnDelay = 60;
    private int spawnTimer = 0;
    private int spawnBatchSize = 3;
    private List<Integer> usedYPositions = new ArrayList<>();
    private int money = 100000;

    private Label moneyLabel;
    private Label waveLabel;
    private Label wavePrompt;
    private TowerPreview towerPreview = null;
    
    private int phase = 0;
    private long phaseStartTime;
    private List<Long> elapsed = new ArrayList<>();
    private ImageActor overlay = new ImageActor(WORLD_WIDTH,WORLD_HEIGHT);

    private boolean waitingForNextWave = true;
    private boolean keyHeld = false;
    private boolean towerPlacedThisClick = false;
    private int lives = 100;
    private UpgradeMenu currentMenu = null;
    
    ScuffedAPI client = ScuffedAPI.getInstance();

    private String status = "running"; // "running", "paused", "gameover"

    public GameWorld() {
        super(WORLD_WIDTH, WORLD_HEIGHT, 1);
        setBackground("grass.png");

        moneyLabel = new Label("Money: $" + money, 30);
        waveLabel = new Label("Wave: " + wave, 30);
        wavePrompt = new Label("Press SPACE to start first wave", 24);
        wavePrompt.setLineColor(Color.BLACK);

        //addObject(new DDCRender(), WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        addObject(UIManager.getInstance(),WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        addObject(ParticleManager.getInstance(),WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
        
        Base base = new Base();
        addObject(base, 925, 300);
        
        addObject(moneyLabel, 100, 30);
        addObject(waveLabel, 250, 30);
        addObject(wavePrompt, WORLD_WIDTH / 2, WORLD_HEIGHT - 30);
        
        
        
        phase = 0;
        phaseStartTime = System.currentTimeMillis();
        
        setPaintOrder(
            ImageActor.class,
            NukeMissile.class,
            DDCRender.class,
            Label.class,
            Button.class,
            EndGamePopup.class,
            Transition.class, 
            Sidebar.class,
            UI.class, 
            ExplosionEffect.class,
            
            Bullet.class,
            Tower.class, 
            Enemy.class
        );
        
        
        
        overlay.setColor(new Color(0,0,0));
        overlay.fill();
        overlay.setTransparency(255);
        addObject(overlay,WORLD_WIDTH/2, WORLD_HEIGHT/2);
    }

    double rotation = 0;
    double position  = 0;

    public void act() {
        handleAnimations();
        handleEnemySpawning();
        handleWaveProgression();
        handleTowerDragging();
        handleTowerClickUpgrade();
        handleSniperBoost();
        resetInputFlags();
    }
    
    private void handleAnimations()
    {
        long now = System.currentTimeMillis();
        elapsed.add(0, now - phaseStartTime);
        switch (phase) {
            
            case 0:
                if (elapsed.get(phase) < 250) {
                    
                    int progressAlpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 250, 255, 0)
                    );
                    progressAlpha = (int) Utils.clamp(progressAlpha,0,255);
                    overlay.setTransparency(progressAlpha);
                }
                else
                {
                    
                    overlay.setTransparency(0);
                    removeObject(overlay);
                    phase = 1;
                    setPaintOrder(
                        NukeMissile.class,
                        DDCRender.class,
                        Label.class,
                        Button.class,
                        EndGamePopup.class,
                        Transition.class, 
                        Sidebar.class,
                        UI.class, 
                        ExplosionEffect.class,
                        Bullet.class,
                        Tower.class, 
                        Enemy.class,
                        ImageActor.class
                    );
                }
                break;
        }
    
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
        // Special boss wave logic
        if (wave % 10 == 0 && wave != 10 && enemiesSpawned == 0) {
            int hp = getEnemyHealth("Boss");
            int speed = getEnemySpeed("Boss");
            addObject(new BossEnemy(speed, hp,1000), 0, WORLD_HEIGHT / 2);
            return;
        }

        int y = getUniqueYPosition();
        int type = Greenfoot.getRandomNumber(100);
        int hp;
        int speed;
        int moneyDeath;

        if (wave <= 2) {
            hp = getEnemyHealth("Basic");
            speed = getEnemySpeed("Basic");
            moneyDeath = getEnemyMoneyDeath("Basic");
            addObject(new BasicEnemy(speed, hp, moneyDeath), 0, y);
        } else if (wave < 7) {
            if (type < 70) {
                hp = getEnemyHealth("Basic");
                speed = getEnemySpeed("Basic");
                moneyDeath = getEnemyMoneyDeath("Basic");
                addObject(new BasicEnemy(speed, hp, moneyDeath), 0, y);
            } else {
                hp = getEnemyHealth("Fast");
                speed = getEnemySpeed("Fast");
                moneyDeath = getEnemyMoneyDeath("Fast");
                addObject(new FastEnemy(speed, hp, moneyDeath), 0, y);
            }
        } else if (wave < 18) {
            if (type < 50) {
                hp = getEnemyHealth("Basic");
                speed = getEnemySpeed("Basic");
                moneyDeath = getEnemyMoneyDeath("Basic");
                addObject(new BasicEnemy(speed, hp, moneyDeath), 0, y);
            } else if (type < 85) {
                hp = getEnemyHealth("Fast");
                speed = getEnemySpeed("Fast");
                moneyDeath = getEnemyMoneyDeath("Fast");
                addObject(new FastEnemy(speed, hp, moneyDeath), 0, y);
            } else {
                hp = getEnemyHealth("Tank");
                speed = getEnemySpeed("Tank");
                moneyDeath = getEnemyMoneyDeath("Tank");
                addObject(new TankEnemy(speed, hp, moneyDeath), 0, y);
            }
        } else {
            if (type < 40) {
                hp = getEnemyHealth("Basic");
                speed = getEnemySpeed("Basic");
                moneyDeath = getEnemyMoneyDeath("Basic");
                addObject(new BasicEnemy(speed, hp, moneyDeath), 0, y);
            } else if (type < 70) {
                hp = getEnemyHealth("Fast");
                speed = getEnemySpeed("Fast");
                moneyDeath = getEnemyMoneyDeath("Fast");
                addObject(new FastEnemy(speed, hp, moneyDeath), 0, y);
            } else if (type < 90) {
                hp = getEnemyHealth("Tank");
                speed = getEnemySpeed("Tank");
                moneyDeath = getEnemyMoneyDeath("Tank");
                addObject(new TankEnemy(speed, hp, moneyDeath), 0, y);
            } else {
                hp = getEnemyHealth("Big");
                speed = getEnemySpeed("Big");
                moneyDeath = getEnemyMoneyDeath("Big");
                addObject(new BigEnemy(speed, hp, moneyDeath), 0, y);
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

    public int getEnemyBaseHealth(String type) {
        switch (type) {
            case "Basic": return 4;
            case "Fast": return 3;
            case "Tank": return 30;
            case "Big": return 60;
            case "Boss": return 500;  // VERY TOUGH
            default: return 1;
        }
    }
    
    public int getEnemyMoneyDeath(String type) {
        switch (type) {
            case "Basic": return 10;
            case "Fast": return 20;
            case "Tank": return 50;
            case "Big": return 100;
            case "Boss": return 1000;  // VERY TOUGH
            default: return 10;
        }
    }

    public int getEnemyHealth(String type) {
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
            case "Boss": multiplier = 1.15; break;
            default:      multiplier = 1.02; break;
        }

        int wavesOver20 = wave - 20;
        return (int)(base * (1 + wavesOver20 * 0.05) * Math.pow(multiplier, wavesOver20 * 0.1));

    }

    public int getEnemySpeed(String type) {
        int base = 1 + wave / 5;
        switch (type) {
            case "Fast": return base + 2;
            case "Tank": return Math.max(1, base - 1);
            case "Boss": return 1;
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
        MouseInfo mi = Greenfoot.getMouseInfo();
    
        // When no tower is being previewed
        if (towerPreview == null) {
            if (!keyHeld) {
                trySwitchPreview("1", "Basic");
                trySwitchPreview("2", "Sniper");
                trySwitchPreview("3", "MachineGun");
                trySwitchPreview("4", "FlameThrower");
                trySwitchPreview("5", "Nuke");
                keyHeld = true; // Prevent repeat switching
            }
        } else {
            // Tower is being previewed
            if (mi != null) {
                int x = mi.getX();
                int y = mi.getY();
    
                // Clamp x to stay within world bounds
                if (x > 1000) x = 1000;
                towerPreview.setLocation(x, y);
                MouseInfo mouse = Greenfoot.getMouseInfo();
                int mouseX = mouse.getX();
                
                if (Greenfoot.mouseClicked(null) && !towerPlacedThisClick) {
                    if (mi.getButton() == 1 && mouseX < 1000) {
                        placeTower(towerPreview.getTowerType(), x, y);
                        towerPlacedThisClick = true;
                    } else if (mouseX >= 1000) {
                        cancelDragging();
                        towerPlacedThisClick = true;
                    }
                }


                

            }
    
            // Escape key cancels dragging
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
    
        // Reset keyHeld once no keys are pressed
        if (!Greenfoot.isKeyDown("1") && !Greenfoot.isKeyDown("2") &&
            !Greenfoot.isKeyDown("3") && !Greenfoot.isKeyDown("4") &&
            !Greenfoot.isKeyDown("5")) {
            keyHeld = false;
        }
    
        // Reset towerPlacedThisClick after mouse released
        if (!Greenfoot.mousePressed(null)) {
            towerPlacedThisClick = false;
        }
    }


    public void startDraggingTower(String towerType) {
        if (towerPreview != null) {
            towerPreview.removePreview();  // safely remove old preview and circle
        }
        // pass the tower range to TowerPreview constructor:
        Tower tower = createTower(towerType);
        towerPreview = new TowerPreview(towerType, tower.getRange());
        addObject(towerPreview, WORLD_WIDTH / 2, WORLD_HEIGHT / 2);
    }

    private void placeTower(String towerType, int x, int y) {
        int cost = getTowerCost(towerType);
        if (spendMoney(cost)) {
            Tower tower = createTower(towerType);
            addObject(tower, x, y);

            if (towerPreview != null) {
                towerPreview.removePreview();  // removes preview + range circle
                towerPreview = null;
            }
        } 
    }

    private void cancelDragging() {
        if (towerPreview != null) {
            towerPreview.removePreview();  // removes preview + range circle
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

    private Tower selectedTower = null;

    private void handleTowerClickUpgrade() {
        if (towerPreview == null && Greenfoot.mouseClicked(null) && !towerPlacedThisClick) {
            MouseInfo mi = Greenfoot.getMouseInfo();
            if (mi != null) {
                int mouseX = mi.getX();
                int mouseY = mi.getY();

                // Check if clicked inside an open menu
                if (currentMenu != null && currentMenu.contains(mouseX, mouseY)) {
                    currentMenu.handleClick(mouseX, mouseY);
                    return;
                }

                // Check if clicked a tower
                List<Tower> towers = getObjectsAt(mouseX, mouseY, Tower.class);
                if (!towers.isEmpty()) {
                    Tower tower = towers.get(0);

                    // If same tower is clicked, toggle menu off
                    if (tower == selectedTower && currentMenu != null) {
                        currentMenu.closeMenu();
                        removeObject(currentMenu);
                        currentMenu = null;
                        selectedTower = null;
                        return;
                    }

                    // Close old menu
                    if (currentMenu != null) {
                        currentMenu.closeMenu();
                        removeObject(currentMenu);
                    }

                    // Create new menu
                    currentMenu = new UpgradeMenu(tower);
                    addObject(currentMenu, tower.getX() + 80, tower.getY());
                    selectedTower = tower;

                    // SHOW RANGE CIRCLE AFTER MENU IS ADDED
                    currentMenu.showRangeCircle();
                } else {
                    // Clicked elsewhere, remove any open menu
                    if (currentMenu != null) {
                        currentMenu.closeMenu();
                        removeObject(currentMenu);
                        currentMenu = null;
                        selectedTower = null;
                    }
                }
            }
        }
    }


    public void clearUpgradeMenu() {
        currentMenu = null;
    }

    private void resetInputFlags() {
        if (!Greenfoot.mousePressed(null)) {
            towerPlacedThisClick = false;
        }
    }

    public int getMoney() {
        return money;
    }

    public int getLives() {
        return lives;
    }

    public int getWave() {
        return wave;
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
        lives = Math.max(lives-amount, 0);
        if (lives <= 0 && status.equals("running")) {
            gameOver();
            status = "gameover";
        }
    }


    private void gameOver() {
        int time = 500; // 500ms for fade in
        setPaintOrder(
            NukeMissile.class,    
            EndGameLabel.class,
            EndGameButton.class,
            EndGamePopup.class,
            Transition.class, 
            Label.class,
            Button.class,
            PolyRender.class, 
            UI.class, 
            Tower.class, 
            Enemy.class
        );
        
        
        
        if (client.isConnected()) {
            try {
                int place = client.sendScore(money, wave);
                System.out.println("You placed: " + place);
                client.getLeaderboard().forEach(System.out::println);
            } catch (IOException e) {
                // some handling here idfk im too tired
            }
        }
        UIManager.getInstance().fadeIn(155, time);
        EndGamePopup endPopup = new EndGamePopup(wave, money, money, time);
        addObject(endPopup, WORLD_WIDTH / 2, 0);
        
    }
    
    public void unlockSniperAbility() {
        sniperAbilitiesUnlocked++;
    
        if (sniperIcon == null) {
            sniperIcon = new SniperAbility();
            addObject(sniperIcon, 50, 540);
        }
    
        if (sniperCooldownLabel != null) {
            removeObject(sniperCooldownLabel);
            sniperCooldownLabel = null;
        }
        if (sniperAbilitiesAvailableLabel != null) {
            removeObject(sniperAbilitiesAvailableLabel);
            sniperAbilitiesAvailableLabel = null;
        }
    
        updateSniperAbilityLabels();
    }
    
    public boolean isSniperAbilityUnlocked() {
        return sniperAbilitiesUnlocked > 0;
    }
    
    public int getSniperAbilitiesUnlockedCount() {
        return sniperAbilitiesUnlocked;
    }
    
    public boolean isSniperBoostActive() {
        return false; // No longer needed globally
    }
    
    private void updateSniperAbilityLabels() {
        if (maxLevelSnipersCount > 0 && sniperAbilitiesUnlocked > 0) {
            // Ensure icon is present
            if (sniperIcon == null) {
                sniperIcon = new SniperAbility();
                addObject(sniperIcon, 50, 540);
            }
    
            // Update available label
            if (sniperAbilitiesAvailableLabel == null) {
                sniperAbilitiesAvailableLabel = new Label(String.valueOf(sniperAbilitiesUnlocked), 30);
                addObject(sniperAbilitiesAvailableLabel, 50, 570);
            } else {
                sniperAbilitiesAvailableLabel.setValue(String.valueOf(sniperAbilitiesUnlocked));
            }
    
            // Remove cooldown label while unused
            if (sniperCooldownLabel != null) {
                removeObject(sniperCooldownLabel);
                sniperCooldownLabel = null;
            }
    
        } else if (maxLevelSnipersCount > 0 && sniperAbilitiesUnlocked == 0 && !sniperBoostTimers.isEmpty()) {
            // Show cooldown when no abilities are currently available, but timer is still running
            if (sniperAbilitiesAvailableLabel != null) {
                removeObject(sniperAbilitiesAvailableLabel);
                sniperAbilitiesAvailableLabel = null;
            }
    
            if (sniperCooldownLabel == null) {
                sniperCooldownLabel = new Label("", 30);
                addObject(sniperCooldownLabel, 50, 570);
            }
    
            if (!sniperBoostTimers.isEmpty()) {
                sniperCooldownLabel.setValue((sniperBoostTimers.get(0) / 60) + "s");
            } else {
                sniperCooldownLabel.setValue("");
            }
    
        } else {
            // No snipers or abilities — clean up everything
            if (sniperIcon != null) {
                removeObject(sniperIcon);
                sniperIcon = null;
            }
    
            if (sniperCooldownLabel != null) {
                removeObject(sniperCooldownLabel);
                sniperCooldownLabel = null;
            }
    
            if (sniperAbilitiesAvailableLabel != null) {
                removeObject(sniperAbilitiesAvailableLabel);
                sniperAbilitiesAvailableLabel = null;
            }
        }
    }

    
    private void handleSniperBoost() {
        // Update cooldown timers
        for (int i = 0; i < sniperBoostTimers.size(); i++) {
            int timeLeft = sniperBoostTimers.get(i) - 1;
            sniperBoostTimers.set(i, timeLeft);
        }
    
        // Remove expired timers and refund ability (clamped to max level sniper count)
        sniperBoostTimers.removeIf(timer -> {
            if (timer <= 0) {
                if (sniperAbilitiesUnlocked < maxLevelSnipersCount) {
                    sniperAbilitiesUnlocked++;
                    updateSniperAbilityLabels();
                }
                return true; // Always remove expired timer
            }
            return false;
        });
    
        // Handle mouse click on icon to trigger boost
        if (Greenfoot.mouseClicked(null)) {
            MouseInfo mouse = Greenfoot.getMouseInfo();
            if (mouse != null && sniperIcon != null) {
                int mouseX = mouse.getX();
                int mouseY = mouse.getY();
    
                int iconX = sniperIcon.getX();
                int iconY = sniperIcon.getY();
    
                int clickableRadius = 30;
    
                if (Math.abs(mouseX - iconX) <= clickableRadius && Math.abs(mouseY - iconY) <= clickableRadius) {
                    if (sniperAbilitiesUnlocked > 0) {
                        SniperTower tower = chooseSniperTowerToBoost();
                        if (tower != null) {
                            tower.activateSpeedBoost();
                            sniperAbilitiesUnlocked--;
                            sniperBoostTimers.add(20 * 60); // 20 seconds
                            System.out.println("Sniper boost activated for tower at (" + tower.getX() + ", " + tower.getY() + ")");
                            updateSniperAbilityLabels();
                        } else {
                            System.out.println("No valid sniper tower near cursor to boost.");
                        }
                    }
                }
            }
        }
    
        // Toggle auto activate on key press (with simple edge detection)
        if (Greenfoot.isKeyDown("A") && !autoActivateSniper) {
            autoActivateSniper = true;
            System.out.println("Auto sniper activate ON");
        } else if (Greenfoot.isKeyDown("S") && autoActivateSniper) {
            autoActivateSniper = false;
            System.out.println("Auto sniper activate OFF");
        }
    
        // Use auto activate if enabled
        if (autoActivateSniper && sniperAbilitiesUnlocked > 0) {
            SniperTower tower = chooseSniperTowerToBoost();
            if (tower != null) {
                tower.activateSpeedBoost();
                sniperAbilitiesUnlocked--;
                sniperBoostTimers.add(20 * 60);
                System.out.println("Auto sniper boost activated for tower at (" + tower.getX() + ", " + tower.getY() + ")");
                updateSniperAbilityLabels();
            }
        }
    
        updateSniperAbilityLabels();
    }

    
    // Helper method to activate a single sniper boost near the mouse cursor
    private void activateSingleSniperBoost() {
        if (sniperAbilitiesUnlocked > 0) {
            SniperTower tower = chooseSniperTowerToBoost();
            if (tower != null) {
                tower.activateSpeedBoost();
                sniperAbilitiesUnlocked--;
                sniperBoostTimers.add(20 * 60); // 20 seconds cooldown
                System.out.println("Sniper boost activated for tower at (" + tower.getX() + ", " + tower.getY() + ")");
                updateSniperAbilityLabels();
            } else {
                System.out.println("No valid sniper tower near cursor to boost.");
            }
        }
    }




    
    public void activateSniperBoost() {
        if (sniperAbilitiesUnlocked > 0) {
            List<SniperTower> snipers = getObjects(SniperTower.class);
            for (SniperTower sniper : snipers) {
                if (!sniper.isBoostActive()) {
                    sniper.activateSpeedBoost(); // safely ignores if already boosted
                    sniperAbilitiesUnlocked--;
                    sniperBoostTimers.add(20 * 60); // optional: track usage
                    updateSniperAbilityLabels();
                    return;
                }
            }
    
            // If no unboosted snipers found
            System.out.println("No unboosted sniper tower found to activate ability.");
        }
    }


    
    private SniperTower chooseSniperTowerToBoost() {
        List<SniperTower> snipers = getObjects(SniperTower.class);
        if (snipers.isEmpty()) return null;
    
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return null;
    
        SniperTower closest = null;
        int minDist = Integer.MAX_VALUE;
    
        for (SniperTower sniper : snipers) {
            if (sniper.isBoostActive()) continue;  // ❗ Skip boosted towers
    
            int dx = sniper.getX() - mouse.getX();
            int dy = sniper.getY() - mouse.getY();
            int dist = dx * dx + dy * dy;
    
            if (dist < minDist) {
                minDist = dist;
                closest = sniper;
            }
        }
    
        return closest;
    }
    
    public void incrementMaxLevelSnipers() {
        maxLevelSnipersCount++;
        unlockSniperAbility();
    }
    
    public void decrementMaxLevelSnipers() {
        if (maxLevelSnipersCount > 0) {
            maxLevelSnipersCount--;
            sniperAbilitiesUnlocked = Math.max(0, sniperAbilitiesUnlocked - 1);
            updateSniperAbilityLabels();
    
            if (maxLevelSnipersCount == 0) {
                // Remove sniper ability icon
                if (sniperIcon != null) {
                    removeObject(sniperIcon);
                    sniperIcon = null;
                }
    
                // Remove cooldown label
                if (sniperCooldownLabel != null) {
                    removeObject(sniperCooldownLabel);
                    sniperCooldownLabel = null;
                }
    
                // Remove available abilities label
                if (sniperAbilitiesAvailableLabel != null) {
                    removeObject(sniperAbilitiesAvailableLabel);
                    sniperAbilitiesAvailableLabel = null;
                }
    
                // Clear boost timers so they don't keep refreshing UI
                sniperBoostTimers.clear();
            }
        }
    }

    
    public int getMaxLevelSnipersCount() {
        return maxLevelSnipersCount;
    }




    
}