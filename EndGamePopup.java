import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.io.IOException;

/**
 * End game popup UI
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (May 30, 2025)
 */
public class EndGamePopup extends UI {
    private static final int WORLD_WIDTH = 1160;
    private static final int WORLD_HEIGHT = 600;
    
    private int wave;
    private long moneyEarned;

    private int timeToPopup = 1000;
    private int timeToFade = 2000;

    private SimpleTimer deltaTime = new SimpleTimer();
    private GreenfootImage image;
    private int startY;
    private int targetY;
    private boolean lockedIn = false;

    private final int buttonSize = 80;

    private Button restartButton;
    private Button creditsButton;
    private Button continueButton;
    private Button mainMenuButton;
    
    private CustomLabel waveLabel;
    private CustomLabel moneyEarnedLabel;
    private CustomLabel uploadStatusLabel;
    private int phase = 0;
    private long phaseStartTime;
    private List<Long> elapsed = new ArrayList<>();
    private ImageActor blackOverlay;
    private boolean notUploaded = true;
    private ScuffedAPI client = ScuffedAPI.getInstance();

    /**
     * Constructor for objects of class EndGamePopup.
     * 
     * @param wave        The wave number reached in the game.
     * @param moneyEarned The total money earned during the game.
     * @param moneySpent  The total money spent during the game.
     * @param timeToPopup The time in milliseconds before the popup appears.
     */
    public EndGamePopup(int wave, long moneyEarned, int timeToPopup) {
        this.wave = wave;
        this.moneyEarned = moneyEarned;
        this.timeToPopup = timeToPopup;
        
        waveLabel = new CustomLabel("Wave: " + wave, 25);
        waveLabel.setFont(new greenfoot.Font(WorldManager.getFontName(), false, false, 25));
        moneyEarnedLabel = new CustomLabel("Score: " + moneyEarned, 25);
        moneyEarnedLabel.setFont(new greenfoot.Font(WorldManager.getFontName(), false, false, 25));
        
        uploadStatusLabel = new CustomLabel("If you see this, \nload _initWorld First! (ScuffedAPI)",15);
        uploadStatusLabel.setFont(new greenfoot.Font(WorldManager.getFontName(), false, false, 15));
        // Initialize the image for the popup
        image = new GreenfootImage("ui/EndGamePopUp.png");
        image.scale(500, 350);
        image.setTransparency(0);
        setImage(image);
        phaseStartTime = System.currentTimeMillis();
        AudioManager.stopAllSFX();
        AudioManager.stopAllLoopingSFX();
        deltaTime.mark();
    }

    @Override
    protected void addedToWorld(World w) {
        
        startY = getY();
        targetY = w.getHeight() / 2; // always center to it to the middle of the world.
        GreenfootImage[] restartButtonImages = new GreenfootImage[2];
        GreenfootImage[] creditsButtonImages = new GreenfootImage[2];
        GreenfootImage[] continueButtonImages = new GreenfootImage[2];
        GreenfootImage[] mainMenuButtonImages = new GreenfootImage[2];

        restartButtonImages[0] = new GreenfootImage("ui/button-restart.png");
        restartButtonImages[1] = new GreenfootImage("ui/button-restart-pressed.png");

        creditsButtonImages[0] = new GreenfootImage("ui/button-credits.png");
        creditsButtonImages[1] = new GreenfootImage("ui/button-credits-pressed.png");

        continueButtonImages[0] = new GreenfootImage("ui/button-continue.png");
        continueButtonImages[1] = new GreenfootImage("ui/button-continue-pressed.png");

        mainMenuButtonImages[0] = new GreenfootImage("ui/button-mainMenu.png");
        mainMenuButtonImages[1] = new GreenfootImage("ui/button-mainMenu-pressed.png");

        restartButton = new EndGameButton(false, restartButtonImages, buttonSize, buttonSize);
        creditsButton = new EndGameButton(false, creditsButtonImages, buttonSize, buttonSize);
        continueButton = new EndGameButton(false, continueButtonImages, buttonSize, buttonSize);
        mainMenuButton = new EndGameButton(false, mainMenuButtonImages, 40, 40);

        restartButton.setTransparency(0);
        creditsButton.setTransparency(0);
        continueButton.setTransparency(0);
        mainMenuButton.setTransparency(0);
        waveLabel.setTransparency(0);
        moneyEarnedLabel.setTransparency(0);
        uploadStatusLabel.setTransparency(0);
        
        
        w.addObject(waveLabel, getX(), getY());
        w.addObject(moneyEarnedLabel, getX(), getY());
        w.addObject(uploadStatusLabel, getX(), getY());
        w.addObject(restartButton, getX(), getY());
        w.addObject(creditsButton, getX() - 150, getY());
        w.addObject(continueButton, getX() + 150, getY());
        w.addObject(mainMenuButton, getX() - 200, getY()); // Top left
        
        
        
        
        blackOverlay = new ImageActor(WORLD_WIDTH,WORLD_HEIGHT);
        blackOverlay.setTransparency(0);
        blackOverlay.setColor(new Color(0,0,0));
        blackOverlay.fill();
        w.addObject(blackOverlay,WORLD_WIDTH/2, WORLD_HEIGHT/2);
    }

    private void start() {
        blackOverlay.setTransparency(0);
        int elapsed = deltaTime.millisElapsed();

        // wait for the transition class to finish up its thing
        if (elapsed < timeToPopup) {
            return;
        }

        // deltaTime after popup has started
        int dtActive = elapsed - timeToPopup;
        if (dtActive > timeToFade) {
            dtActive = timeToFade;
        }

        // cos curve easing type sh, love smoothness frfr
        double progress = dtActive / (double) timeToFade;
        double base = (1 - Math.cos(progress * Math.PI)) * 0.5;
        double exponent = 3;
        double ease = Math.pow(base, exponent);

        // update transparency of the image
        int alpha = (int) (ease * 255);
        alpha = (int) Utils.clamp(alpha, 0, 255);

        image.setTransparency(alpha);
        restartButton.setTransparency(alpha);
        creditsButton.setTransparency(alpha);
        continueButton.setTransparency(alpha);
        mainMenuButton.setTransparency(alpha);
        waveLabel.setTransparency(alpha);
        moneyEarnedLabel.setTransparency(alpha);
        uploadStatusLabel.setTransparency(alpha);
        setImage(image);

        // update the position of the popup/actor
        int newY = startY + (int) (ease * (targetY - startY));
        setLocation(getX(), newY);
        
        restartButton.setLocation(getX(), newY + 100); // bottom center
        creditsButton.setLocation(getX() - 150, newY + 100); // bottom left
        continueButton.setLocation(getX() + 150, newY + 100); // bottom right
        mainMenuButton.setLocation(getX() - 200, newY - 125); // Top left
        waveLabel.setLocation(getX() - 150, newY+25); // bottom right
        moneyEarnedLabel.setLocation(getX() - 150, newY); // Top left
        uploadStatusLabel.setLocation(getX() + 150, newY + 15);
        // lock tf IN.)
        if (dtActive >= timeToFade) {
            setLocation(getX(), targetY);
            
            lockedIn = true;

            image.setTransparency(255);
            restartButton.setTransparency(255);
            creditsButton.setTransparency(255);
            continueButton.setTransparency(255);
            mainMenuButton.setTransparency(255);

            restartButton.setActive(true);
            creditsButton.setActive(true);
            continueButton.setActive(true);
            mainMenuButton.setActive(true);
        }
    }

    private void fadeIn(World world) {
        long now = System.currentTimeMillis();
        elapsed.add(phase, now - phaseStartTime);
        if (elapsed.get(phase) < 500) {
            int progressAlpha = (int) Math.round(
                    Utils.map(elapsed.get(phase), 0, 500, 0, 255));
            progressAlpha = (int) Utils.clamp(progressAlpha, 0, 255);
            blackOverlay.setTransparency(progressAlpha);
        } else {
            if (world.getClass() != GameWorld.class)
            {
                AudioManager.stopMusic();
                AudioManager.playMusic(new GreenfootSound("waves-loop.mp3"));
            }
            WorldManager.setWorld(world);
            blackOverlay.setTransparency(0);
            phaseStartTime = System.currentTimeMillis(); 
        }
    }

    private void handleAnimation()
    {
        switch (phase) {
            case 0:
                elapsed.add(phase,phaseStartTime);
                blackOverlay.setTransparency(0);
                break;
            case 1: // Restart
                fadeIn(new GameWorld());
                break;
            case 2: // Credits
                fadeIn(new CreditWorld());
                break;
            case 3: // continue
                fadeIn(new GameWorld());
                break;
            case 4: // Main Menu
                fadeIn(new MainMenu());
                break;
        }

    }
    
    private void enterPhase(int newPhase)
    {
        getWorld().setPaintOrder(
            ImageActor.class,
            CustomLabel.class,
            EndGameButton.class,
            EndGamePopup.class
        );
        this.phase = newPhase;
        phaseStartTime = System.currentTimeMillis(); 
    }
    
    private void startUpload()
    {
        uploadStatusLabel.setValue("Uploading Score...");
        try {
            int balls = client.sendScore(moneyEarned,wave);
            uploadStatusLabel.setValue(" Score Uploaded! Rank: #" + balls);
            notUploaded = false;
        } catch (IOException err)
        {
        
        }
    }

    public void act() {
        if (!lockedIn) {
            start();
        }
        
        if (notUploaded && client.isConnected())
        {
            startUpload();
        }
    
        if (restartButton.isPressed()) {
            enterPhase(1);
        }
        if (creditsButton.isPressed()) {
            enterPhase(2);
        }
        if (continueButton.isPressed()) {
            enterPhase(3);
        }
        if (mainMenuButton.isPressed()) {
            enterPhase(4);
        }
        
        handleAnimation();
    }
}
