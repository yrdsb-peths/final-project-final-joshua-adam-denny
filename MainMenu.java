import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;
import java.util.ArrayList;
import java.util.List;
import java.awt.GraphicsEnvironment;
/**
 * Init world
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (June 3, 2025)
 */
public class MainMenu extends World
{

    private static final int WORLD_WIDTH = 1160;
    private static final int WORLD_HEIGHT = 600;
    

    private static final int CENTERX = WORLD_WIDTH/2;
    private static final int CENTERY = WORLD_HEIGHT/2;
    
    private int phase = 0;
    private long phaseStartTime;
    private List<Long> elapsed = new ArrayList<>();
    
    private GreenfootImage original = new GreenfootImage("ui/titlescreen-nofeds-notitle.png");;
    private GreenfootImage blurred = BlurHelper.fastBlur(original, 0.001);
    private ImageActor overlay = new ImageActor(WORLD_WIDTH,WORLD_HEIGHT);

    private GreenfootImage phase2_fedsImg = new GreenfootImage("ui/titlescreen-feds.png");
    private ImageActor phase2_feds = new ImageActor(phase2_fedsImg);
    private ImageActor phase2_title = new ImageActor("ui/titlescreen-title.png");
    
    
    private double fed_locationY_diffNum = 0.0;
    private boolean leaderboardButtonPreviouslyPressed;
    private boolean settingsButtonPreviouslyPressed;
    private boolean onLeaderboardPage;
    private boolean onSettingsPage;
    
    private int padding = 40;
    private Button leaderboardButton;
    private Button settingsButton;
    private Button creditsButton;
    private Button startButton;
    private Button editNameButton;
    private LeaderboardPage leaderboardPage = null;
    private SettingsPage settingsPage = null;
    private boolean editNameButtonPreviouslyPressed;

    


    /**
     * Constructor for objects of class MainMenu.
     * Initializes the world with a specific width and height.
     */
    public MainMenu()
    {    
        super(WORLD_WIDTH, WORLD_HEIGHT, 1);
        Greenfoot.setSpeed(50);
        ScuffedAPI.getInstance().connect();
        setBackground(blurred);
        
        phase2_feds.setTransparency(0);
        phase2_title.setTransparency(0);
        addObject(phase2_feds, CENTERX, CENTERY);
        addObject(phase2_title, CENTERX, CENTERY);
        
        phase = 0;
        phaseStartTime = System.currentTimeMillis();
        
        // Create the title screen buttons
        GreenfootImage startButtonImage = new GreenfootImage("ui/button-start.png");
        GreenfootImage startButtonPressedImage = new GreenfootImage("ui/button-start-pressed.png");
        startButton = new Button(
            true, 
            new GreenfootImage[]{
                startButtonImage,
                startButtonPressedImage
            }, 
            220, 
            70
        );
        
        
        GreenfootImage leaderboardImage = new GreenfootImage("ui/button-short-leaderboard.png");
        GreenfootImage leaderboardPressedImage = new GreenfootImage("ui/button-short-leaderboard-pressed.png");
        leaderboardButton = new Button(
            true, 
            new GreenfootImage[]{
                leaderboardImage,
                leaderboardPressedImage
            }, 
            70, 
            70
        );
        GreenfootImage settingsImage = new GreenfootImage("ui/button-pause.png");
        GreenfootImage settingsPressedImage = new GreenfootImage("ui/button-pause-pressed.png");
        settingsButton = new Button(
            true, 
            new GreenfootImage[]{
                settingsImage,
                settingsPressedImage
            }, 
            70, 
            70
        );
        
        GreenfootImage editNameIcon = new GreenfootImage("ui/button-short-editName.png");
        GreenfootImage editNameIconPressed = new GreenfootImage("ui/button-short-editName-pressed.png");
        
        editNameButton = new Button(
            true, 
            new GreenfootImage[]{
                editNameIcon,
                editNameIconPressed
            },
            70, 
            70
        );
        
        GreenfootImage creditImage = new GreenfootImage("ui/button-short-credits.png");
        GreenfootImage creditImagePressed = new GreenfootImage("ui/button-short-credits-pressed.png");
        creditsButton = new Button(
            true, 
            new GreenfootImage[]{
                creditImage,
                creditImagePressed
            }, 
            70, 
            70
        );
        
        
        startButton.setTransparency(0);
        leaderboardButton.setTransparency(0);
        settingsButton.setTransparency(0);
        creditsButton.setTransparency(0);
        editNameButton.setTransparency(0);
        

        // Add the buttons to the world
        addObject(startButton, CENTERX, CENTERY + 150);
        addObject(leaderboardButton, 
                CENTERX - padding - creditImage.getWidth() - 10,
                CENTERY + creditImage.getHeight() + 150 + 10);
        addObject(editNameButton,
                CENTERX - padding, 
                CENTERY + creditImage.getHeight() + 150 + 10);
                
        addObject(settingsButton,
                CENTERX + padding + creditImage.getWidth() + 10, 
                CENTERY + creditImage.getHeight() + 150 + 10);
                
        addObject(creditsButton,
                CENTERX + padding, 
                CENTERY + creditImage.getHeight() + 150 + 10);
        

                // Add the title overlay
        overlay.setColor(new Color(0,0,0));
        overlay.fill();
        overlay.setTransparency(255);
        addObject(overlay,WORLD_WIDTH/2, WORLD_HEIGHT/2);
        
        //Add all buttons down here
    }
    
    public void act()
    {
        long now = System.currentTimeMillis();
        
        elapsed.add(0, now - phaseStartTime);
        // Very long animation system, sorted in phases
        switch (phase) {
            
            case 0: // fade out
                if (elapsed.get(phase) < 250) {
                    
                    int progressAlpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 250, 255, 0)
                    );
                    progressAlpha = (int) Utils.clamp(progressAlpha,0,255);
                    overlay.setTransparency(progressAlpha);
                }
                else
                {
                    enterPhase(phase+1);
                    overlay.setTransparency(0);
                }
                break;
            case 1: // blur effect
                if (elapsed.get(phase) < 3000) {
                    double blurPower = Utils.map(elapsed.get(phase), 0, 5000, 0.0, 1.0);
                    
                    blurPower = Utils.clamp(blurPower, 0.0, 1.0);
                    double y = (1 - Math.cos(blurPower * Math.PI)) / 2.0;
                    
                    blurred = BlurHelper.fastBlur(original, y);
                    setBackground(blurred);
                } else {
                    enterPhase(phase+1);
                    overlay.setColor(Color.RED);
                    overlay.fill();
                }
                break;
            case 2: // delay function 
                delayPhase(phase,now,500);
                break;
            case 3: // fade in the red overlay
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) < 250)
                {
                    int progressAlpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 250, 0, 255)
                    );
                    progressAlpha = (int) Utils.clamp(progressAlpha,0,255);
                    overlay.setTransparency(progressAlpha);
                } else {
                    enterPhase(phase+1);
                    phase2_feds.setTransparency(255);
                }
                break;
            case 4:     // fade out the red overlay
                // where the feds appear
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) < 250)
                {
                    int progressAlpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 250, 255, 0)
                    );
                    progressAlpha = (int) Utils.clamp(progressAlpha,0,255);
                    overlay.setTransparency(progressAlpha);
                } else {

                    enterPhase(phase+1);
                    overlay.setColor(Color.BLUE);
                    overlay.fill();
                }
                break;
            case 5: // fade in the blue overlay
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) < 250)
                {
                    int progressAlpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 250, 0, 255)
                    );
                    progressAlpha = (int) Utils.clamp(progressAlpha,0,255);
                    overlay.setTransparency(progressAlpha);
                } else {
                    enterPhase(phase+1);
                    phase2_title.setTransparency(255);
                    startButton.setTransparency(255);
                    leaderboardButton.setTransparency(255);
                    creditsButton.setTransparency(255);
                    settingsButton.setTransparency(255);
                    editNameButton.setTransparency(255);
                }
                break;
            case 6: // fade out the blue overlay
                // where the title appears
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) < 250)
                {
                    int progressAlpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 250, 255, 0)
                    );
                    progressAlpha = (int) Utils.clamp(progressAlpha,0,255);
                    overlay.setTransparency(progressAlpha);
                } else {
                    enterPhase(phase+1);
                    setPaintOrder(UI.class);// The start button should be on top of everything.
                }
                break;
            case 69:
            // fade in the black overlay
                // where the game starts
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) < 500)
                {
                    int progressAlpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 500, 0,255)
                    );
                    progressAlpha = (int) Utils.clamp(progressAlpha,0,255);
                    overlay.setTransparency(progressAlpha);
                } else {
                    overlay.setTransparency(255);
                    enterPhase(phase+1);
                    WorldManager.setWorld(new GameWorld());
                }
                break;
            case 79: // leaderboard fade in
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) < 500)
                {
                    int progressAlpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 500, 0,155)
                    );
                    progressAlpha = (int) Utils.clamp(progressAlpha,0,155);
                    overlay.setTransparency(progressAlpha);
                } else {
                    overlay.setTransparency(155);
                    enterPhase(phase+1);
                }
                break;
            case 89: // settings
                // setings fade in
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) < 500)
                {
                    int progressAlpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 500, 0,155)
                    );
                    progressAlpha = (int) Utils.clamp(progressAlpha,0,155);
                    overlay.setTransparency(progressAlpha);
                } else {
                    overlay.setTransparency(155);
                    enterPhase(phase+1);
                }
                break;
            case 99: // exit setting/leaderboard
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) < 500)
                {
                    int progressAlpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 500, 155,0)
                    );
                    progressAlpha = (int) Utils.clamp(progressAlpha,0,155);
                    overlay.setTransparency(progressAlpha);
                } else {
                    overlay.setTransparency(0);
                    enterPhase(phase+1);
                    settingsPage = null;
                    leaderboardPage = null;
                    setPaintOrder(UI.class);
                }
                break;
            case 109:
            // credits fade in
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) < 500)
                {
                    int progressAlpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 500, 0,255)
                    );
                    progressAlpha = (int) Utils.clamp(progressAlpha,0,255);
                    overlay.setTransparency(progressAlpha);
                } else {
                    overlay.setTransparency(255);
                    enterPhase(phase+1);
                    WorldManager.setWorld(new CreditWorld());
                }
                break;
            case 119:
            // edit name fade in 
                elapsed.add(phase, now - phaseStartTime);
                if (elapsed.get(phase) < 500)
                {
                    int progressAlpha = (int) Math.round(
                        Utils.map(elapsed.get(phase), 0, 500, 0,155)
                    );
                    progressAlpha = (int) Utils.clamp(progressAlpha,0,155);
                    overlay.setTransparency(progressAlpha);
                } else {
                    overlay.setTransparency(155);
                    enterPhase(phase+1);
                    ScuffedAPI.setUsername(Greenfoot.ask("Set ScuffedAPI Leaderboard username: "));
                    enterPhase(99);
                }
                break;
                
        }
        
        handleKeystrokes();
        handleButtons();
        handleFedMovement();
    }
    
    private void handleKeystrokes()
    {
        if ((Greenfoot.isKeyDown("escape") && onLeaderboardPage) || (Greenfoot.isKeyDown("escape") && onSettingsPage)) 
        {
            if (onSettingsPage)
            {
                settingsPage.removeSelf();
            }
            if (onLeaderboardPage)
            {
                leaderboardPage.removeSelf();
            }
            onLeaderboardPage = false;
            onSettingsPage = false;
            enterPhase(99);
        }

    }
    
    private void handleButtons()
    {
        if (startButton.isPressed())
        {
            overlay.setColor(Color.BLACK);
            overlay.fill();
            setPaintOrder(ImageActor.class); // overlay on top of all.
            enterPhase(69);
        }
        
        if (creditsButton.isPressed())
        {
            overlay.setColor(Color.BLACK);
            overlay.fill();
            setPaintOrder(ImageActor.class); // overlay on top of all.
            enterPhase(109);
        }
        
    
        if (editNameButton.isPressed() && !editNameButtonPreviouslyPressed)
        {
            overlay.setColor(Color.BLACK);
            overlay.fill();
            setPaintOrder(ImageActor.class);
            enterPhase(119);
        }
        editNameButtonPreviouslyPressed = editNameButton.isPressed();
        
        if (leaderboardButton.isPressed() && !leaderboardButtonPreviouslyPressed) {
            setPaintOrder(
                CustomLabel.class,
                Slider.class,
                CheckButton.class,
                MainMenuSideButton.class,
                LeaderboardPage.class,
                Transition.class, 
                DDCRender.class,
                ImageActor.class,
                Button.class,
                UI.class
            );
            overlay.setColor(Color.BLACK);
            overlay.fill();
            enterPhase(79);
            onLeaderboardPage = true;
            leaderboardPage = new LeaderboardPage();
            addObject(leaderboardPage, CENTERX,CENTERY);
        }
        leaderboardButtonPreviouslyPressed = leaderboardButton.isPressed();
        
        if (settingsButton.isPressed() && !settingsButtonPreviouslyPressed) {
            setPaintOrder(
                Slider.class,
                CheckButton.class,
                MainMenuSideButton.class,
                SettingsPage.class,
                Transition.class, 
                DDCRender.class,
                CustomLabel.class,
                ImageActor.class,
                Button.class,
                UI.class
            );
            overlay.setColor(Color.BLACK);
            overlay.fill();
            enterPhase(89);
            onSettingsPage = true;
            settingsPage = new SettingsPage();
            addObject(settingsPage, CENTERX,CENTERY);
        }
        settingsButtonPreviouslyPressed = settingsButton.isPressed();
        
    }
    
    private void handleFedMovement()
    {
        fed_locationY_diffNum += 0.1;
        double fed_locationY_diff = 5 * Math.sin(fed_locationY_diffNum);
        int fed_locationY = (CENTERY+5) + (int) fed_locationY_diff;
        GreenfootImage shakingFed = BlurHelper.fastBlur(
            phase2_fedsImg,
            Utils.map(Math.sin(fed_locationY_diffNum), -1, 1, 0.3,1)
        );
        shakingFed.setTransparency(phase2_feds.getTransparency());
        phase2_feds.setImage(shakingFed);
        phase2_feds.setLocation(phase2_feds.getX(),fed_locationY);
    }    
    private void delayPhase(int phaseInt,long now, int ms)
    {
        elapsed.add(phase, now - phaseStartTime);
        if (elapsed.get(phase) > 1000)
        {
            enterPhase(phase+1);
        }
    }
    
    public void enterPhase(int phaseInt)
    {
        this.phase = phaseInt;
        phaseStartTime = System.currentTimeMillis(); 
    }
}
