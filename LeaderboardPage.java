import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;
import java.io.IOException;


/**
 * Write a description of class LeaderboardPage here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

public class LeaderboardPage extends UI
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
    
    private mainMenuSideButton mainMenuButton;
    
    private int phase = 0;
    private long phaseStartTime;
    private List<Long> elapsed = new ArrayList<>();
    private List<ScuffedAPI.LeaderboardEntry> entries;
    
    private ImageActor blackOverlay;
    private GreenfootImage[] mainMenuButtonImages = new GreenfootImage[2];
    private List<CustomLabel> entryLabels = new ArrayList<>(10);
    ScuffedAPI client = ScuffedAPI.getInstance();
    
    private static Font customFont;
    private static String fontName;

    public LeaderboardPage()
    {
        // Initialize the image for the popup
        image = new GreenfootImage("ui/LeaderboardPage.png");
        image.scale(1000, 400);
        image.setTransparency(0);
        setImage(image);
        phaseStartTime = System.currentTimeMillis();
        deltaTime.mark();
        
        for(int i = 0; i < 10; i++) {
            CustomLabel lbl = new CustomLabel("If you see this,\n you aren't connected to ScuffedAPI!", 20);
            lbl.setTransparency(0);
            lbl.setFont(new greenfoot.Font(WorldManager.getFontName(), false,false,20));
            lbl.setFillColor(Color.WHITE);
            entryLabels.add(lbl);
        }
        
        if (client.isConnected()) {
            try {
                entries = client.getLeaderboard();  
                for(int i = 0; i < entryLabels.size(); i++) {
                    CustomLabel lbl = entryLabels.get(i);
                    String e = entries.get(i).toString();
                    lbl.setValue(e);
                }
            } catch (IOException e) {
                // some handling here idfk im too tired
            }
        }
    }
    
    @Override
    protected void addedToWorld(World w) {
        startY = getY();
        targetY = w.getHeight() / 2; // always center to it to the middle of the world.
        
        mainMenuButtonImages[0] = new GreenfootImage("ui/button-mainMenu.png");
        mainMenuButtonImages[1] = new GreenfootImage("ui/button-mainMenu-pressed.png");
        
        mainMenuButton = new mainMenuSideButton(false, mainMenuButtonImages, buttonSize, buttonSize);
        mainMenuButton.setTransparency(0);
        w.addObject(mainMenuButton, getX() - 200, getY());
        
        for(CustomLabel lbl : entryLabels) {
            w.addObject(lbl, getX(), getY());
        }
        
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
        mainMenuButton.setTransparency(alpha);
        setImage(image);
        for(CustomLabel lbl : entryLabels) {
            lbl.getImage().setTransparency(alpha);
        }
        positionEntryLabels();

        // update the position of the popup/actor
        int newY = startY + (int) (ease * (targetY - startY));
        setLocation(getX(), newY);
        
        mainMenuButton.setLocation(getX() - image.getWidth()/2 + mainMenuButtonImages[0].getWidth()/2, newY - image.getHeight()/2 + mainMenuButtonImages[1].getWidth()/2); // Top left

        // lock tf IN.)
        if (dtActive >= timeToFade) {
            setLocation(getX(), targetY);
            lockedIn = true;
            image.setTransparency(255);
            mainMenuButton.setTransparency(255);
            mainMenuButton.setActive(true);
        }

    }
    
    private void positionEntryLabels() {
        int px = getX(), py = getY();
        int iw = image.getWidth(), ih = image.getHeight();
        int leftX  = px - iw/2 +  250;  
        int rightX = px + iw/2 -  250;  
        int rows   = 5;
        int spacing = 380 / rows;        // e.g. 400/5 = 80px per row
        int startY  = py - ih/2 + spacing/2; // first row center
        
        CustomLabel lbl0 = entryLabels.get(0);
        lbl0.setLocation(px - 250, py - 140);
        
        CustomLabel lbl1  = entryLabels.get(1);
        lbl1.setLocation(px - 250, py - 70);
        CustomLabel lbl2  = entryLabels.get(2);
        lbl2.setLocation(px - 250, py);
        
        CustomLabel lbl3  = entryLabels.get(3);
        lbl3.setLocation(px - 250, py + 70);
        
        CustomLabel lbl4  = entryLabels.get(4);
        lbl4.setLocation(px - 250, py + 140);
        
        CustomLabel lbl5  = entryLabels.get(5);
        lbl5.setLocation(px + 230, py - 140);
        
        CustomLabel lbl6  = entryLabels.get(6);
        lbl6.setLocation(px + 230, py - 70);
        
        CustomLabel lbl7  = entryLabels.get(7);
        lbl7.setLocation(px + 230, py);
        
        CustomLabel lbl8  = entryLabels.get(8);
        lbl8.setLocation(px + 230, py + 70);
        
        CustomLabel lbl9  = entryLabels.get(9);
        lbl9.setLocation(px + 230, py + 140);
        
    }
    
    public void removeSelf()
    {
        MainMenu world = (MainMenu)getWorld();
        if (world != null) {
            world.removeObject(this);
            world.removeObject(mainMenuButton);
            world.removeObject(blackOverlay);
        }
        world.enterPhase(99);
        lockedIn = false;
        startY = 0;
        targetY = 0;
        phase = 0;
        phaseStartTime = System.currentTimeMillis();
        elapsed.clear();
    
        deltaTime.mark();
    
        image.setTransparency(0);
        setImage(image);

        // Clear overlay reference
        blackOverlay = null;
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
    
    private void enterPhase(int newPhase)
    {
        this.phase = newPhase;
        phaseStartTime = System.currentTimeMillis(); 
    }
    
    
    public void act() {
        if (!lockedIn) {
            start();
        }
    
        if (mainMenuButton != null && mainMenuButton.isPressed()) {
            enterPhase(1);
        }

        
        handleAnimation();
    }
}
