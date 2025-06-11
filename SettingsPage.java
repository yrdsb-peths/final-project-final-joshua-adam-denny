import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class LeaderboardPage here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class SettingsPage extends UI
{
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
    
    private MainMenuSideButton mainMenuButton;
    private CheckButton preformanceModeCheckBox;
    private CheckButton autoStartCheckBox;
    private Slider volumeMusicSlider;
    private Slider volumeSFXSlider;
    private Slider volumeSpecialSFXSlider;
    
    
    private int phase = 0;
    private long phaseStartTime;
    private List<Long> elapsed = new ArrayList<>();
    
    private ImageActor blackOverlay;
    
    public SettingsPage()
    {
        // Initialize the image for the popup
        image = new GreenfootImage("ui/settingsPage.png");
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
        GreenfootImage[] mainMenuButtonImages = new GreenfootImage[2];

        mainMenuButtonImages[0] = new GreenfootImage("ui/button-mainMenu.png");
        mainMenuButtonImages[1] = new GreenfootImage("ui/button-mainMenu-pressed.png");
        
        mainMenuButton = new MainMenuSideButton(false, mainMenuButtonImages, buttonSize, buttonSize);
        autoStartCheckBox = new CheckButton(buttonSize- 20);
        preformanceModeCheckBox = new CheckButton(buttonSize- 20);
        volumeMusicSlider = new Slider(200, 20);  // 200px track, 40px tall
        volumeSFXSlider = new Slider(200, 20);
        volumeSpecialSFXSlider = new Slider(200, 20);
        
        autoStartCheckBox.setChecked((boolean) PlayerPrefs.getData("AutoStart", false));
        preformanceModeCheckBox.setChecked((boolean) PlayerPrefs.getData("PreformanceMode", false));
        
        
        mainMenuButton.setTransparency(0);
        preformanceModeCheckBox.setTransparency(0);
        autoStartCheckBox.setTransparency(0);
        volumeMusicSlider.setTransparency(0);
        volumeSFXSlider.setTransparency(0);
        volumeSpecialSFXSlider.setTransparency(0);
        
        
        volumeMusicSlider.setValue((int)PlayerPrefs.getData("VolumeMusic",30));
        volumeSFXSlider.setValue((int)PlayerPrefs.getData("VolumeSFX",30));
        volumeSpecialSFXSlider.setValue((int)PlayerPrefs.getData("VolumeSpecialSFX",45));
        
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
        
        
        mainMenuButton.setLocation(getX() - 200, newY - 125); // Top left

        // lock tf IN.)
        if (dtActive >= timeToFade) {
            setLocation(getX(), targetY);
            
            lockedIn = true;

            image.setTransparency(255);
            mainMenuButton.setTransparency(255);

            mainMenuButton.setActive(true);
        }

    }
    
    private void handleAnimation()
    {
        switch (phase) {
            case 0:
                elapsed.add(phase,phaseStartTime);
                blackOverlay.setTransparency(0);
                break;
            case 1: // Main Menu
                removeSelf();
                break;
                
        }

    }
    
    public void removeSelf()
    {
        MainMenu world = (MainMenu)getWorld();
        if (world != null) {
            world.removeObject(this);
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
    
        mainMenuButton = null;
        autoStartCheckBox = null;

        // Clear overlay reference
        blackOverlay = null;
    }
    
    private void enterPhase(int newPhase)
    {
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
    
        if (mainMenuButton != null && mainMenuButton.isPressed()) {
            enterPhase(1);
        }
    
        if (autoStartCheckBox != null) {
            PlayerPrefs.setData("AutoStart", autoStartCheckBox.isChecked());
        }
        
        handleSliders();
        handleAnimation();
    }
}
