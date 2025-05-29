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

    public void fadeIn(int targetOpacity, int targetTime)
    {
        transition.fadeIn(targetOpacity, targetTime);
    }

    public void fadeOut(int targetOpacity, int targetTime)
    {
        transition.fadeOut(targetOpacity, targetTime);
    }
}
