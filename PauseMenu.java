import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Pause Menu UI
 * 
 * @author Denny Ung and Joshua Stevens
 * @version Version 1.0.0 (June 7, 2025)
 */
public class PauseMenu extends UI
{
    public static PauseMenu _instance; 
    private static final int WORLD_WIDTH = 1160;
    private static final int WORLD_HEIGHT = 600;

    private int timeToPopup = 250;
    private int timeToFade = 250;

    private SimpleTimer deltaTime = new SimpleTimer();
    private GreenfootImage image;
    private int startY;
    private int targetY;
    private boolean lockedIn = false;

    private final int buttonSize = 40;
    private final int primaryButtonSize = buttonSize + 20;
    
    private PauseButton restartButton;
    private PauseButton creditsButton;
    private PauseButton continueButton;
    private PauseButton mainMenuButton;
    CheckButton preformanceModeCheckBox;
    private CheckButton autoStartCheckBox;
    private Slider volumeMusicSlider;
    private Slider volumeSFXSlider;
    private Slider volumeSpecialSFXSlider;
    
    
    private int phase = 0;
    private long phaseStartTime;
    private List<Long> elapsed = new ArrayList<>();
    
    private ImageActor blackOverlay;
    private GameWorld gw;

    public PauseMenu()
    {
        // Initialize the image for the popup
        image = new GreenfootImage("ui/pausemenu.png");
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
        gw = (GameWorld)w;
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

        restartButton = new PauseButton(false, restartButtonImages, primaryButtonSize, primaryButtonSize);
        creditsButton = new PauseButton(false, creditsButtonImages, primaryButtonSize, primaryButtonSize);
        continueButton = new PauseButton(false, continueButtonImages, primaryButtonSize, primaryButtonSize);
        mainMenuButton = new PauseButton(false, mainMenuButtonImages, buttonSize, buttonSize);
        autoStartCheckBox = new CheckButton(buttonSize- 20);
        preformanceModeCheckBox = new CheckButton(buttonSize- 20);
        volumeMusicSlider = new Slider(200, 20);  // 200px track, 40px tall
        volumeSFXSlider = new Slider(200, 20);
        volumeSpecialSFXSlider = new Slider(200, 20);
        
        autoStartCheckBox.setChecked((boolean) PlayerPrefs.getData("AutoStart", false));
        preformanceModeCheckBox.setChecked((boolean) PlayerPrefs.getData("PreformanceMode", false));
        
        
        restartButton.setTransparency(0);
        creditsButton.setTransparency(0);
        continueButton.setTransparency(0);
        mainMenuButton.setTransparency(0);
        preformanceModeCheckBox.setTransparency(0);
        autoStartCheckBox.setTransparency(0);
        volumeMusicSlider.setTransparency(0);
        volumeSFXSlider.setTransparency(0);
        volumeSpecialSFXSlider.setTransparency(0);
        
        
        volumeMusicSlider.setValue((int)PlayerPrefs.getData("VolumeMusic",30));
        volumeSFXSlider.setValue((int)PlayerPrefs.getData("VolumeSFX",30));
        volumeSpecialSFXSlider.setValue((int)PlayerPrefs.getData("VolumeSpecialSFX",45));
        
        w.addObject(restartButton, getX(), getY());
        w.addObject(creditsButton, getX() - 150, getY());
        w.addObject(continueButton, getX() + 150, getY());
        w.addObject(mainMenuButton, getX() - 200, getY());
        
        w.addObject(preformanceModeCheckBox, getX(), getY());
        w.addObject(autoStartCheckBox, getX(), getY());
        
        w.addObject(volumeMusicSlider, getX(), getY());
        w.addObject(volumeSFXSlider, getX(), getY());
        w.addObject(volumeSpecialSFXSlider, getX(), getY());
        

        
        
        
        blackOverlay = new ImageActor(WORLD_WIDTH,WORLD_HEIGHT);
        blackOverlay.setTransparency(0);
        blackOverlay.setColor(new Color(0,0,0));
        blackOverlay.fill();
        w.addObject(blackOverlay,WORLD_WIDTH/2, WORLD_HEIGHT/2);

        AudioManager.stopAllSFX();
        AudioManager.stopAllLoopingSFX();

    }
    
    public static PauseMenu getInstance() {
        if (_instance == null) {
            _instance = new PauseMenu();
        }
        return _instance;
    }
    
    public void removeSelf()
    {
        World world = getWorld();
        if (world != null) {
            world.removeObject(this);
            world.removeObject(restartButton);
            world.removeObject(creditsButton);
            world.removeObject(continueButton);
            world.removeObject(mainMenuButton);
            world.removeObject(preformanceModeCheckBox);
            world.removeObject(autoStartCheckBox);
            world.removeObject(volumeMusicSlider);
            world.removeObject(volumeSFXSlider);
            world.removeObject(volumeSpecialSFXSlider);
            world.removeObject(blackOverlay);
        }
        
        lockedIn = false;
        startY = 0;
        targetY = 0;
        phase = 0;
        phaseStartTime = System.currentTimeMillis();
        elapsed.clear();
    
        deltaTime.mark();
    
        image.setTransparency(0);
        setImage(image);
    
        restartButton = null;
        creditsButton = null;
        continueButton = null;
        mainMenuButton = null;
        autoStartCheckBox = null;

        // Clear overlay reference
        blackOverlay = null;
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
        preformanceModeCheckBox.setTransparency(alpha);
        autoStartCheckBox.setTransparency(alpha);
        restartButton.setTransparency(alpha);
        creditsButton.setTransparency(alpha);
        continueButton.setTransparency(alpha);
        mainMenuButton.setTransparency(alpha);
        volumeMusicSlider.setTransparency(alpha);
        volumeSFXSlider.setTransparency(alpha);
        volumeSpecialSFXSlider.setTransparency(alpha);
        setImage(image);

        // update the position of the popup/actor
        int newY = startY + (int) (ease * (targetY - startY));
        setLocation(getX(), newY);
        
        volumeMusicSlider.setLocation(getX() + 120, newY-60); 
        volumeSFXSlider.setLocation(getX() + 120, newY-20); 
        volumeSpecialSFXSlider.setLocation(getX() + 120, newY+20); 
        
        autoStartCheckBox.setLocation(getX()-20, newY-65); 
        preformanceModeCheckBox.setLocation(getX()-20, newY-25); 
        
        
        restartButton.setLocation(getX(), newY + 120); // bottom center
        creditsButton.setLocation(getX() - 150, newY + 120); // bottom left
        continueButton.setLocation(getX() + 150, newY + 120); // bottom right
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

        AudioManager.stopAllSFX();
        AudioManager.stopAllLoopingSFX();

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
            removeSelf();
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
                blackOverlay.setTransparency(0);
                phaseStartTime = System.currentTimeMillis(); 
                removeSelf();
                UIManager.getInstance().togglePauseMenu();
                gw.setStatus(GameWorld.Status.RUNNING);
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
    
    private void handleSliders()
    {
        PlayerPrefs.setData("VolumeMusic",volumeMusicSlider.getValue());
        PlayerPrefs.setData("VolumeSFX",volumeSFXSlider.getValue());
        PlayerPrefs.setData("VolumeSpecialSFX",volumeSpecialSFXSlider.getValue());
        
        AudioManager.setMusicVolume(volumeMusicSlider.getValue());
    }

    public void act() {
        if (!lockedIn) {
            start();
        }
    
        if (preformanceModeCheckBox != null) {
            PlayerPrefs.setData("PreformanceMode", preformanceModeCheckBox.isChecked());
        }
    
        if (restartButton != null && restartButton.isPressed()) {
            enterPhase(1);
        }
        if (creditsButton != null && creditsButton.isPressed()) {
            enterPhase(2);
        }
        if (continueButton != null && continueButton.isPressed()) {
            enterPhase(3);
        }
        if (mainMenuButton != null && mainMenuButton.isPressed()) {
            enterPhase(4);
        }
    
        if (autoStartCheckBox != null) {
            PlayerPrefs.setData("AutoStart", autoStartCheckBox.isChecked());
        }
    
        if (gw != null) {
            if (autoStartCheckBox != null) {
                gw.setAutoNextWave(autoStartCheckBox.isChecked());
            }
        }
        handleSliders();
        handleAnimation();
    }


}
