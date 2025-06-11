import greenfoot.*;
import java.util.List;
import java.util.ArrayList;
import java.lang.Math;
/*
 * Main GameWorld
 * 
 * 
 * @author Joshua Stevens, Denny Ung, Adam Fung.
 * @version 7.0.1 (June 7, 2025)
 */

public class GameWorld extends World {
    
    // Final Variables
    private static final int WORLD_WIDTH = 1160;
    private static final int WORLD_HEIGHT = 600;
    private static final int CENTER_X = WORLD_WIDTH/2;
    private static final int CENTER_Y = WORLD_HEIGHT/2;
    private static final int PLAY_AREA_WIDTH = 1000;
    private static final int UPGRADE_MENU_OFFSET_X = 80;
    
    // Tower spawnning variables
    private boolean towerPlacedThisClick = false;
    
    public GreenfootSound themeMusic = new GreenfootSound("themeMusic.mp3");
    
    // Sniper Special Ability @ lvl 3
    private List<Integer> sniperBoostTimers = new ArrayList<>();
    private int sniperAbilitiesUnlocked = 0;
    private CustomLabel sniperAbilitiesAvailableLabel = null;
    private CustomLabel sniperCooldownLabel = null;
    private SniperAbility sniperIcon = null;
    private int maxLevelSnipersCount = 0;
    private boolean sniperKeyPreviouslyDown = false;
    private boolean autoActivateSniper = false;   // toggles auto activate on/off
    private boolean activateToggleKeyPreviouslyDown = false; // track key press edge
    private boolean pauseButtonPreviouslyPressed = false;
    private boolean helpButtonPreviouslyPressed = false;

    // Wave/Money Variables
    private boolean autoNextWave = false;
    private int wave = 0;
    private int enemiesSpawned = 0;
    private int enemiesToSpawn = 0;
    private int spawnDelay = 60;
    private int spawnTimer = 0;
    private int spawnBatchSize = 3;
    private List<Integer> usedYPositions = new ArrayList<>();
    private int money = 100;
    private long moneyTotal = money;
    private int phase = 0;
    private boolean waitingForNextWave = true;
    
    // Label/UI Variables
    private CustomLabel moneyLabel;
    private CustomLabel waveLabel;
    private CustomLabel wavePrompt;
    private TowerPreview towerPreview = null;
    private UIManager uiManager;
    private UpgradeMenu currentMenu = null;
    
    // Animation Variables
    private long phaseStartTime;
    private List<Long> elapsed = new ArrayList<>();
    private ImageActor overlay = new ImageActor(WORLD_WIDTH,WORLD_HEIGHT);
    
    // Misc Variables
    private boolean keyHeld = false;
    private int lives = 100;
    private boolean firstAct = false;
    private boolean firstTime = false;
    private boolean onPausePage = false;
    private boolean onHelpPage = false;
    

    public Class<?>[] defaultPaintOrder = {
        NukeMissile.class,
        DDCRender.class,
        CustomLabel.class,
        PauseButton.class,
        PauseMenu.class,
        Button.class,
        EndGamePopup.class,
        Transition.class, 
        Sidebar.class,
        UI.class, 
        ExplosionEffect.class,
        Tower.class, 
        Bullet.class,
        Enemy.class,
        ImageActor.class
    };

    public enum Status {
        RUNNING,
        PAUSED,
        GAMEOVER,
        HELPCONTROLS;
    }
    private Status status = Status.RUNNING;

    public GameWorld() {
        super(WORLD_WIDTH, WORLD_HEIGHT, 1);
        setBackground("ui/grass.png");

        moneyLabel = new CustomLabel("Money: $" + money, 30);
        moneyLabel.setFont(new greenfoot.Font(WorldManager.getFontName(), false, false, 30));
        waveLabel = new CustomLabel("Wave: " + wave, 30);
        waveLabel.setFont(new greenfoot.Font(WorldManager.getFontName(), false, false, 30));
        wavePrompt = new CustomLabel("Press SPACE to start first wave", 24);
        wavePrompt.setFont(new greenfoot.Font(WorldManager.getFontName(), false, false, 24));

        wavePrompt.setLineColor(Color.BLACK);
        uiManager = UIManager.getInstance();
        //addObject(new DDCRender(), CENTER_X, CENTER_Y);
        addObject(ParticleManager.getInstance(),CENTER_X, CENTER_Y);
        
        Base base = new Base();
        addObject(base, 925, 300);
        
        addObject(moneyLabel, 100, 30);
        addObject(waveLabel, 250, 30);
        addObject(wavePrompt, CENTER_X, WORLD_HEIGHT - 30);
        phase = 0;
        phaseStartTime = System.currentTimeMillis();
        
        setPaintOrder(ImageActor.class);
        
        overlay.setColor(new Color(0,0,0));
        overlay.fill();
        overlay.setTransparency(255);
        addObject(overlay,CENTER_X, CENTER_Y);
        AudioManager.stopMusic();
    }

    public void act() {
        if (!firstAct) // this is for my sanity of not listening to thiss music every SINGLE TIME 
        {
            firstAct = true;
            addObject(uiManager,CENTER_X, CENTER_Y); // To prevent deletion upon reloading the world
            AudioManager.stopMusic();
            AudioManager.playMusic(themeMusic);
            if (PlayerPrefs.getData("FirstTime",true))
            {
                firstTime = true;
                PlayerPrefs.setData("FirstTime",false);
            }
        }
        switch(status) {
            case RUNNING:
                handleAnimations();
                handleEnemySpawning();
                handleWaveProgression();
                handleTowerDragging();
                handleTowerClickUpgrade();
                handleSniperBoost();
                resetInputFlags();
                handlePauseButton();
                handleHelpButton();
                break;
            case PAUSED:
                handlePauseButton();
                break;
            case GAMEOVER:
                // hawk
                break;
            case HELPCONTROLS:
                handleHelpButton();
                break;
        }
    }
    
    private void handleHelpButton()
    {
        boolean helpPressedNow = uiManager.isHelpButtonPressed();
        if (helpPressedNow && !helpButtonPreviouslyPressed || firstTime || (Greenfoot.isKeyDown("escape") && onHelpPage)) {
            firstTime = false;
            if (status == Status.RUNNING) {
                onHelpPage = true;
                uiManager.toggleHelpMenu(); 
                status = Status.HELPCONTROLS;
                clearUpgradeMenu();
                cancelDragging();
                setPaintOrder( // HELP MENU PAINT ORDER
                    HelpButton.class,
                    HelpMenu.class,
                    Transition.class, 
                    NukeMissile.class,
                    DDCRender.class,
                    CustomLabel.class,
                    Button.class,
                    Sidebar.class,
                    UI.class, 
                    ExplosionEffect.class,
                    Bullet.class,
                    Tower.class, 
                    Enemy.class,
                    ImageActor.class
                );
            }
            else if (status == Status.HELPCONTROLS) {
                onHelpPage = false;
                uiManager.toggleHelpMenu();   
                status = Status.RUNNING;
                setPaintOrder(defaultPaintOrder);
            }
    
        }
        
        helpButtonPreviouslyPressed = helpPressedNow;
    }
    
    private void handlePauseButton()
    {
        boolean pausePressedNow = uiManager.isPauseButtonPressed();

        if (pausePressedNow && !pauseButtonPreviouslyPressed || (Greenfoot.isKeyDown("escape") && onPausePage)) {
            
            if (status == Status.RUNNING) {
                onPausePage = true;
                uiManager.togglePauseMenu(); 
                status = Status.PAUSED;
                clearUpgradeMenu();
                cancelDragging();
                setPaintOrder( // PAUSE MENU PAINT ORDER
                    EndGamePopup.class,
                    Slider.class,
                    CheckButton.class,
                    PauseButton.class,
                    PauseMenu.class,
                    Transition.class, 
                    NukeMissile.class,
                    DDCRender.class,
                    
                    CustomLabel.class,
                    Button.class,
                    Sidebar.class,
                    UI.class, 
                    ExplosionEffect.class,
                    Bullet.class,
                    Tower.class, 
                    Enemy.class,
                    ImageActor.class
                );
            }
            else if (status == Status.PAUSED) {
                onPausePage = false;
                uiManager.togglePauseMenu();   
                status = Status.RUNNING;
                setPaintOrder(defaultPaintOrder);
            }
        }

        // 3) Remember for next frame
        pauseButtonPreviouslyPressed = pausePressedNow;
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
                    setPaintOrder(defaultPaintOrder);
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
            addObject(new BossEnemy(speed, hp,1000), 0, CENTER_Y);
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
        // Base money values by type
        int baseAmount;
        switch (type) { 
            case "Basic": baseAmount = 5; break;
            case "Fast": baseAmount = 15; break;
            case "Tank": baseAmount = 40; break;
            case "Big": baseAmount = 75; break;
            case "Boss": baseAmount = 300; break;  // VERY TOUGH
            default: baseAmount = 10; break;
        }
        
        if (wave <= 20) {
            return baseAmount;
        } else {
            // Calculate waves past 20
            int wavesOver20 = (wave - 20);
            double rampFactor = Math.pow(0.9, wavesOver20); //when round is over 20 base amount is multiplied by 90% per round
            rampFactor = Math.max(rampFactor, 0.2);    //the least amount it can reach is 20%
        
            return (int)(baseAmount * rampFactor);
        }
    }
    public int getEnemyHealth(String type) {
        int base = getEnemyBaseHealth(type);
    
        if (wave <= 20) {
            return base;
        }
    
        double multiplier;
        switch (type) {
            case "Basic": multiplier = 1.05; break;  // increased from 1.02
            case "Fast":  multiplier = 1.07; break;  // increased from 1.03
            case "Tank":  multiplier = 1.12; break;  // increased from 1.07
            case "Big":   multiplier = 1.15; break;  // increased from 1.10
            case "Boss": multiplier = 1.20; break;   // increased from 1.15
            default:      multiplier = 1.05; break;
        }
    
        int wavesOver20 = wave - 20;
        // Increase linear ramp to 0.1 (previously 0.05) and exponent multiplier factor to 0.2 (previously 0.1)
        return (int)(base * (1 + wavesOver20 * 0.10) * Math.pow(multiplier, wavesOver20 * 0.2));
    }


    public int getEnemySpeed(String type) {
        int base;
        int wavesOver20 = Math.max(0, wave - 20);
    
        if (wave <= 20) {
            // Small steady growth before wave 20
            base = 1 + wave / 10;  // slow increase
        } else {
            // Faster growth after wave 20
            base = 3 + wavesOver20 / 2 + 20 / 10; 
        }
    
        // Calculate tankSpeed first for Boss speed capping
        int tankSpeed = Math.max(1, base - (wave <= 20 ? 1 : 3));
    
        switch (type) {
            case "Fast":
                return base + (wave <= 20 ? 2 : 6);  // small early boost, big late boost
            case "Tank":
                return tankSpeed;
            case "Boss":
                // Boss speed increases slowly but never reaches tankSpeed
                int bossSpeed = 1 + (wave <= 20 ? wave / 20 : wavesOver20 / 5);
                return Math.min(bossSpeed, tankSpeed - 1);
            default:
                return base;
        }
    }
    private void handleWaveProgression() {
        autoNextWave = (boolean) PlayerPrefs.getData("AutoStart", false);
            // If waiting for next wave and autoNextWave is ON, start automatically
        if (waitingForNextWave && (autoNextWave || Greenfoot.isKeyDown("space"))) {
            nextWave();
            waitingForNextWave = false;
            wavePrompt.setValue("");
            if (spawnBatchSize < 5) spawnBatchSize++;
        }
    
        // When wave ends, prompt player to start next wave or wait for auto mode
        if (!waitingForNextWave && enemiesSpawned == enemiesToSpawn && getObjects(BasicEnemy.class).isEmpty()) {
            waitingForNextWave = true;
            wavePrompt.setValue(autoNextWave ? "Auto next wave: ON" : "Press SPACE to start next wave");
            addMoney(Math.max(200, 200 * (wave/5)));
            
        }
    }

    public void setAutoNextWave(boolean value) {
        autoNextWave = value;
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
    
        if (towerPreview == null) {
            // No tower preview: allow switching tower previews with keys, but only once per key press
            if (!keyHeld) {
                trySwitchPreview("1", "Basic");
                trySwitchPreview("2", "Sniper");
                trySwitchPreview("3", "MachineGun");
                trySwitchPreview("4", "FlameThrower");
                trySwitchPreview("5", "Nuke");
                keyHeld = true;
            }
        } else {
            // Tower preview active: move it with mouse
            if (mi != null) {
                int x = mi.getX();
                int y = mi.getY();
    
                // Clamp x position so preview stays inside play area horizontally
                if (x > PLAY_AREA_WIDTH) x = PLAY_AREA_WIDTH;
    
                towerPreview.setLocation(x, y);
    
                // Check for mouse click - only once per click (towerPlacedThisClick)
                if (Greenfoot.mouseClicked(null) && !towerPlacedThisClick) {
                    int mouseX = mi.getX();
                    if (mi.getButton() == 1) { // Left-click
                        if (mouseX < PLAY_AREA_WIDTH) {
                            placeTower(towerPreview.getTowerType(), x, y);
                            towerPlacedThisClick = true;
                        } else {
                            // Clicked outside play area - cancel dragging
                            cancelDragging();
                            towerPlacedThisClick = true;
                        }
                    }
                }
            }
    
            // Cancel dragging if Escape key pressed
            if (Greenfoot.isKeyDown("escape")) {
                cancelDragging();
            }
    
            // Allow switching tower preview while dragging (still guarded by keyHeld)
            trySwitchPreview("1", "Basic");
            trySwitchPreview("2", "Sniper");
            trySwitchPreview("3", "MachineGun");
            trySwitchPreview("4", "FlameThrower");
            trySwitchPreview("5", "Nuke");
        }
    
        // Reset keyHeld once all tower keys are released
        if (!Greenfoot.isKeyDown("1") && !Greenfoot.isKeyDown("2") &&
            !Greenfoot.isKeyDown("3") && !Greenfoot.isKeyDown("4") &&
            !Greenfoot.isKeyDown("5")) {
            keyHeld = false;
        }
    
        // Reset towerPlacedThisClick when mouse is released so next click can place again
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
        addObject(towerPreview, CENTER_X, CENTER_Y);
    }

    private void placeTower(String towerType, int x, int y) {
        int cost = getTowerCost(towerType);
        if (spendMoney(cost)) {
            Tower tower = createTower(towerType);
            addObject(tower, x, y);
    
            if (towerPreview != null) {
                towerPreview.removePreview();
                towerPreview = null;
            }
    
            // Reset flags after placing so dragging can start again
            towerPlacedThisClick = true;  // Keep true here because click just happened
            keyHeld = true;               // Prevent accidental multiple switches

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
                        clearUpgradeMenu();
                    }

                    // Close old menu
                    if (currentMenu != null) {
                        currentMenu.closeMenu();
                        removeObject(currentMenu);
                    }

                    // Create new menu
                    currentMenu = new UpgradeMenu(tower);
                    addObject(currentMenu, tower.getX() + UPGRADE_MENU_OFFSET_X, tower.getY());
                    selectedTower = tower;

                    // SHOW RANGE CIRCLE AFTER MENU IS ADDED
                    currentMenu.showRangeCircle();
                } else {
                    // Clicked elsewhere, remove any open menu
                    clearUpgradeMenu();
                }
            }
        }
    }
    public void clearUpgradeMenu() {
        if (currentMenu != null) {
            currentMenu.closeMenu();
            removeObject(currentMenu);
            currentMenu = null;
            selectedTower = null;
        }
    }

    private void resetInputFlags() {
        if (!Greenfoot.mousePressed(null)) {
            towerPlacedThisClick = false;
        }
    }
    
    
    public Status getStatus()
    {
        return status;
    }
    
    public void setStatus(Status newStatus)
    {
        this.status  = newStatus;
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
        moneyTotal+=amount;
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
        if (lives <= 0 && status == Status.RUNNING) {
            gameOver();
            status = Status.GAMEOVER;
        }
    }

    private void gameOver() {
        int time = 500; // 500ms for fade in
        setPaintOrder(  // GAME OVER PAINT ORDER
            EndGameButton.class,
            EndGamePopup.class,
            Transition.class, 
            HealthBar.class,
            CustomLabel.class,
            PauseButton.class,
            PauseMenu.class,
            ImageActor.class,
            CustomLabel.class,
            Button.class,
            PolyRender.class, 
            NukeMissile.class,  
            UI.class, 
            Sidebar.class,
            Tower.class, 
            Enemy.class
        );
        
        
        uiManager.fadeIn(155, time);
        EndGamePopup endPopup = new EndGamePopup(wave, (int) moneyTotal, time);
        addObject(endPopup, CENTER_X, 0);
        
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
                sniperAbilitiesAvailableLabel = new CustomLabel(String.valueOf(sniperAbilitiesUnlocked), 30);
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
                sniperCooldownLabel = new CustomLabel("", 30);
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
    
        boolean keyDownNow = Greenfoot.isKeyDown("s");
        MouseInfo mouse = Greenfoot.getMouseInfo();
    
        if (mouse != null && sniperIcon != null) {
            int mouseX = mouse.getX();
            int mouseY = mouse.getY();
    
            int iconX = sniperIcon.getX();
            int iconY = sniperIcon.getY();
    
            int clickableRadius = 30;
    
            boolean clickedOnIcon = false;
    
            if (Greenfoot.mouseClicked(null)) {
                int dx = mouseX - iconX;
                int dy = mouseY - iconY;
                if (dx * dx + dy * dy <= clickableRadius * clickableRadius) {
                    clickedOnIcon = true;
                }
            }
    
            if ((clickedOnIcon || (keyDownNow && !sniperKeyPreviouslyDown)) && sniperAbilitiesUnlocked > 0) {
                SniperTower tower = chooseSniperTowerToBoost();
                if (tower != null) {
                    tower.activateSpeedBoost();
                    sniperAbilitiesUnlocked--;
                    sniperBoostTimers.add(20 * 60); // 20 seconds
                    updateSniperAbilityLabels();
                }
            }
        }
    
        sniperKeyPreviouslyDown = keyDownNow; // Track key state
    
        // Toggle auto activate on key press (with simple edge detection)
        boolean aKeyDownNow = Greenfoot.isKeyDown("A");
        if (aKeyDownNow && !activateToggleKeyPreviouslyDown) {
            autoActivateSniper = !autoActivateSniper;
        }
        activateToggleKeyPreviouslyDown = aKeyDownNow;
    
        // Use auto activate if enabled
        if (autoActivateSniper && sniperAbilitiesUnlocked > 0) {
            SniperTower tower = chooseSniperTowerToBoost();
            if (tower != null) {
                tower.activateSpeedBoost();
                sniperAbilitiesUnlocked--;
                sniperBoostTimers.add(20 * 60);
                updateSniperAbilityLabels();
            }
        }
    
        updateSniperAbilityLabels();
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