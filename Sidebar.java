import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Sidebar UI 
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (May 30th, 2025)
 */
public class Sidebar extends UI
{
    
    public static Sidebar _instance = null;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    
    public Sidebar()
    {
        GreenfootImage img = new GreenfootImage("ui/sidebar.png");
        setImage(img);
        
        
    }
    
    @Override
    protected void addedToWorld(World w) {
        
    }
    
    

    public static Sidebar getInstance()
    {
        if (_instance == null) {
            _instance = new Sidebar();
        }
        return _instance;
    }

    public void act()
    {
        // Add your action code here.
    }
}
