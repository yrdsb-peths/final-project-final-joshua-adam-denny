import greenfoot.*; // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

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
    private int moneyEarned;
    private int moneySpent;

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

    private int phase = 0;
    private long phaseStartTime;
    private List<Long> elapsed = new ArrayList<>();
    private ImageActor blackOverlay;

    /**
     * Constructor for objects of class EndGamePopup.
     * 
     * @param wave        The wave number reached in the game.
     * @param moneyEarned The total money earned during the game.
     * @param moneySpent  The total money spent during the game.
     * @param timeToPopup The time in milliseconds before the popup appears.
     */
    public EndGamePopup(int wave, int moneyEarned, int moneySpent, int timeToPopup) {
        this.wave = wave;
        this.moneyEarned = moneyEarned;
        this.moneySpent = moneySpent;
        this.timeToPopup = timeToPopup;

        // Initialize the image for the popup
        image = new GreenfootImage("ui/EndGamePopUp.png");
        image.scale(500, 350);
        image.setTransparency(0);
        setImage(image);
        phaseStartTime = System.currentTimeMillis();

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
        setImage(image);

        // update the position of the popup/actor
        int newY = startY + (int) (ease * (targetY - startY));
        setLocation(getX(), newY);
        restartButton.setLocation(getX(), newY + 100); // bottom center
        creditsButton.setLocation(getX() - 150, newY + 100); // bottom left
        continueButton.setLocation(getX() + 150, newY + 100); // bottom right
        mainMenuButton.setLocation(getX() - 200, newY - 125); // Top left

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
            EndGameButton.class,
            EndGamePopup.class,
            NukeMissile.class,
            DDCRender.class,
            Label.class,
            Button.class,
            Transition.class, 
            Sidebar.class,
            UI.class, 
            ExplosionEffect.class,
            Bullet.class,
            Tower.class, 
            Enemy.class
        );
        this.phase = newPhase;
        phaseStartTime = System.currentTimeMillis(); 
    }

    public void act() {
        if (!lockedIn) {
            start();
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
