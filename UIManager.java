import greenfoot.*;
/**
 * Head class for UI, Oversees all UI elements on screen/world
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
    private Button pauseButton;
    
    public UIManager()
    {
        GreenfootImage[] pauseButtonImages = new GreenfootImage[2];

        pauseButtonImages[0] = new GreenfootImage("ui/button-pause.png");
        pauseButtonImages[1] = new GreenfootImage("ui/button-pause-pressed.png");
        pauseButton = new Button(true, pauseButtonImages, 40,40);
    }
    

    public static UIManager getInstance()
    {
        if (_instance == null) {
            _instance = new UIManager();
        }
        return _instance;
    }



    @Override
    protected void addedToWorld(World world)
    {
        world.addObject(transition, world.getWidth() / 2, world.getHeight() / 2);
        world.addObject(sideBar, world.getWidth() - 80, world.getHeight() / 2);
        world.addObject(healthBar, world.getWidth()/2 - 190, 0 + 30);
        world.addObject(pauseButton,world.getWidth() - 160 - 25, 25);
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
    
    public boolean isPauseButtonPressed()
    {
        return pauseButton.isPressed();
    }
}
