import greenfoot.*;
/**
 * Head class for managign UI elements and storing class instances
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class UIManager extends Actor
{

    public static UIManager _instance = null;
    private Transition transition = Transition.getInstance();
    private Sidebar sidebar = Sidebar.getInstance();

    public UIManager()
    {

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
        world.addObject(sidebar, world.getWidth() - 80, world.getHeight() / 2);
    }
    
    /**
     * Returns the sidebar instance.
     * 
     * @return The Sidebar instance.
     */
    public Sidebar getSidebar()
    {
        return sidebar;
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
}
