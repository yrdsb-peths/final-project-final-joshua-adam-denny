import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Pause Menu UI
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (June 7, 2025)
 */
public class PauseMenu extends UI
{
    public static PauseMenu _instance;
    private Button PauseButton;
    
    
    private PauseMenu()
    {
        GreenfootImage img = new GreenfootImage(0, 0);
        setImage(img);
    }
    
    @Override
    protected void addedToWorld(World world)
    {
        
    }
    
    public static PauseMenu getInstance()
    {
        if (_instance == null) {
            _instance = new PauseMenu();
        }
        return _instance;
    }
    
    public void act()
    {
        
    }
}
