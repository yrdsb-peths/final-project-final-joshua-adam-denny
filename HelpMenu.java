import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Pause Menu UI
 * 
 * @author Denny Ung and Joshua Stevens
 * @version Version 1.0.0 (June 7, 2025)
 */
public class HelpMenu extends UI
{
    public static HelpMenu _instance; 
    private static final int WORLD_WIDTH = 1160;
    private static final int WORLD_HEIGHT = 600;

    private int timeToPopup = 250;
    private int timeToFade = 250;

    private SimpleTimer deltaTime = new SimpleTimer();
    private GreenfootImage image;
    private int startY;
    private int targetY;
    private boolean lockedIn = false; 
    
    private HelpButton controlsObjectiveButton;
    
    private List<Long> elapsed = new ArrayList<>();
    
    private boolean isControlMenu = false;
    
    private ImageActor blackOverlay;
    private boolean swapButtonPreviouslyPressed = false;

    public HelpMenu()
    {
        // Initialize the image for the popup
        image = new GreenfootImage("ui/objectivePage.png");
        image.scale(500, (int)(500*0.625));
        image.setTransparency(0);
        setImage(image);
        AudioManager.stopAllSFX();
        AudioManager.stopAllLoopingSFX();
        deltaTime.mark();
    }
    
    @Override
    protected void addedToWorld(World w) {
        startY = getY();
        targetY = w.getHeight() / 2;
        GreenfootImage[] controlsObjectiveButtonImages = new GreenfootImage[2];
        controlsObjectiveButtonImages[0] = new GreenfootImage("ui/button-short-help.png");
        controlsObjectiveButtonImages[1] = new GreenfootImage("ui/button-short-help-pressed.png");
        controlsObjectiveButton = new HelpButton(false,controlsObjectiveButtonImages,50,50);
        controlsObjectiveButton.setTransparency(0);
        w.addObject(controlsObjectiveButton,WORLD_WIDTH/2, WORLD_HEIGHT/2);
        
        blackOverlay = new ImageActor(WORLD_WIDTH,WORLD_HEIGHT);
        blackOverlay.setTransparency(0);
        blackOverlay.setColor(new Color(0,0,0));
        blackOverlay.fill();
        w.addObject(blackOverlay,WORLD_WIDTH/2, WORLD_HEIGHT/2);

        AudioManager.stopAllSFX();
        AudioManager.stopAllLoopingSFX();

    }
    
    public static HelpMenu getInstance() {
        if (_instance == null) {
            _instance = new HelpMenu();
        }
        return _instance;
    }
    
    public void removeSelf()
    {
        World world = getWorld();
        if (world != null) {
            world.removeObject(this);
            world.removeObject(blackOverlay);
            world.removeObject(controlsObjectiveButton);
        }
        
        lockedIn = false;
        startY = 0;
        targetY = 0;
        elapsed.clear();
    
        deltaTime.mark();
        
        controlsObjectiveButton = null;
        
        image.setTransparency(0);
        setImage(image);
    

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
        controlsObjectiveButton.setTransparency(alpha);
        image.setTransparency(alpha);
        setImage(image);

        // update the position of the popup/actor
        int newY = startY + (int) (ease * (targetY - startY));
        setLocation(getX(), newY);
        controlsObjectiveButton.setLocation(getX() + 215, newY - 125);
        
        
        // lock tf IN.)
        if (dtActive >= timeToFade) {
            setLocation(getX(), targetY);
            
            lockedIn = true;
            controlsObjectiveButton.setActive(true);

            image.setTransparency(255);
        }

        AudioManager.stopAllSFX();
        AudioManager.stopAllLoopingSFX();

    }
    
    private void swap()
    {
        if (isControlMenu)
        {
            GreenfootImage img = new GreenfootImage("ui/objectivePage.png");
            img.scale(500, (int)(500*0.625));
            setImage(img);
        }
        else
        {
            GreenfootImage img = new GreenfootImage("ui/controlsPage.png");
            img.scale(500, (int)(500*0.625));
            setImage(img);
        }
        isControlMenu = !isControlMenu;
    }
    
    

    public void act() {
        if (!lockedIn) {
            start();
        }
        boolean swapPressedNow = controlsObjectiveButton.isPressed();
        if (swapPressedNow && !swapButtonPreviouslyPressed)
        {
            swap();
        }
        swapButtonPreviouslyPressed = swapPressedNow;
        
        
        
        
    }


}
