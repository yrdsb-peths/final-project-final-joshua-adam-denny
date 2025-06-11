import greenfoot.*;
/**
 * Head class for UI, Oversees all UI elements on screen/world within GameWorld Class.
 * 
 * @author Denny Ung
 * @version Version 1.0.5 (May 29, 2025)
 */
public class UIManager extends Actor
{


    public static UIManager _instance = null;
    private Transition transition = Transition.getInstance();
    private Sidebar sideBar = Sidebar.getInstance();
    private HealthBar healthBar = HealthBar.getInstance();
    private PauseButton pauseButton;
    private HelpButton helpButton;
    
    /**
     * Private constructor to prevent instantiation.
     * Initializes the UI elements such as transition, sidebar, health bar, pause button, and help button.
     */
    public UIManager()
    {
        GreenfootImage[] pauseButtonImages = new GreenfootImage[2];
        GreenfootImage[] helpButtonImages = new GreenfootImage[2];
        pauseButtonImages[0] = new GreenfootImage("ui/button-pause.png");
        pauseButtonImages[1] = new GreenfootImage("ui/button-pause-pressed.png");
        pauseButton = new PauseButton(true, pauseButtonImages, 40,40);
        
        helpButtonImages[0] = new GreenfootImage("ui/button-short-help.png");
        helpButtonImages[1] = new GreenfootImage("ui/button-short-help-pressed.png");
        helpButton = new HelpButton(true, helpButtonImages, 40,40);
    }
    

    /**
     * Returns the singleton instance of UIManager.
     * If the instance is null, it creates a new instance.
     * 
     * @return The singleton instance of UIManager.
     */
    public static UIManager getInstance()
    {
        if (_instance == null) {
            _instance = new UIManager();
        }
        return _instance;
    }



    /**
     * Called when the UIManager is added to the world.
     * Adds the transition, sidebar, health bar, pause button, and help button to the world.
     * Sets the image of the UIManager to a transparent image.
     * 
     * @param world The world to which the UIManager is added.
     */
    @Override
    protected void addedToWorld(World world)
    {
        world.addObject(transition, world.getWidth() / 2, world.getHeight() / 2);
        world.addObject(sideBar, world.getWidth() - 80, world.getHeight() / 2);
        world.addObject(healthBar, world.getWidth()/2 - 190, 0 + 30);
        world.addObject(pauseButton,world.getWidth() - 160 - 25, 25);
        world.addObject(helpButton,world.getWidth() - 160 - 85, 25);
        setImage(new GreenfootImage(1,1));
    }
    
    /**
     * Returns the sidebar instance.
     * 
     * @return The Sidebar instance.
     */
    public Sidebar getSidebar()
    {
        return sideBar;
    }

    /**
     * Returns the transition instance.
     * 
     * @return The Transition instance.
     */
    public Transition getTransition()
    {
        return transition;
    }

    /**
     * Starts a fadein transition to the specified target opacity over the specified time.
     * 
     * @param targetOpacity The target opacity (0-255)
     * @param targetTime The time in ms to complete the fadein
     */

    public void fadeIn(int targetOpacity, int targetTime)
    {
        transition.fadeIn(targetOpacity, targetTime);
    }

    /**
     * Starts a fadeout transition to the specified target opacity over the specified time.
     * 
     * @param targetOpacity The target opacity (0-255)
     * @param targetTime The time in ms to complete the fadeout
     */
    public void fadeOut(int targetOpacity, int targetTime)
    {
        transition.fadeOut(targetOpacity, targetTime);
    }
    
    /**
     * Returns the health bar isPressed function.
     * 
     * @return The HealthBar ispressedFunction .
     */
    public boolean isPauseButtonPressed()
    {
        return pauseButton.isPressed();
    }
    
    /**
     * Returns the help button instance.
     * 
     * @return The HelpButton isPressed.
     */
    public boolean isHelpButtonPressed()
    {
        return helpButton.isPressed();
    }


    
    /**
     * Toggles the pause menu based on the current game status.
     * If the game is running, it fades in the pause menu and adds it to the world.
     * If the game is paused, it removes the pause menu and fades out.
     * 
     * 
     */
    public void togglePauseMenu()
    {
        GameWorld gw = (GameWorld) getWorld();
        switch(gw.getStatus()) {
            case RUNNING:
                fadeIn(155, 250);
                PauseMenu pauseMenu = PauseMenu.getInstance();
                gw.addObject(pauseMenu, gw.getWidth()/2, 0);
                break;
            case PAUSED:
                  PauseMenu.getInstance().removeSelf();
                  fadeOut(0, 250);
                  gw.setPaintOrder(gw.defaultPaintOrder);
                break;
            case GAMEOVER:
                  // hawk
                break;
            case HELPCONTROLS:
                break;
        }
        
    }
    

    /**
     * Toggles the help menu based on the current game status.
     * If the game is running, it fades in the help menu and adds it to the world.
     * If the help menu is already open, it removes the help menu and fades out.
     * 
     */
    public void toggleHelpMenu()
    {
        GameWorld gw = (GameWorld) getWorld();
        switch(gw.getStatus()) {
            case RUNNING:
                fadeIn(155, 250);
                HelpMenu helpMenu = HelpMenu.getInstance();
                gw.addObject(helpMenu, gw.getWidth()/2, 0);
                break;
            case HELPCONTROLS:
                  HelpMenu.getInstance().removeSelf();
                  fadeOut(0, 250);
                  gw.setPaintOrder(gw.defaultPaintOrder);
                break;
            default:
                break;
        }
        
    }
}
