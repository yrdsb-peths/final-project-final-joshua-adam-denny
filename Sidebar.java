import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Sidebar here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Sidebar extends UI
{
    /**
     * Act - do whatever the Sidebar wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
    public static Sidebar _instance = null;
    
    public Sidebar()
    {
        GreenfootImage img = new GreenfootImage("ui/sidebar.png");
        setImage(img);
        GreenfootImage balls1 = new GreenfootImage("bee2.png");
        GreenfootImage balls2 = new GreenfootImage("man.png");
        Button balls = new Button(true, balls1, balls2, 100, 100); 
        
    }
    
    @Override
    protected void addedToWorld(World w) {
        GreenfootImage balls1 = new GreenfootImage("bee2.png");
        GreenfootImage balls2 = new GreenfootImage("man.png");
        Button balls = new Button(true, balls1, balls2, 100, 100);
        w.addObject(balls,w.getWidth()/2,w.getHeight()/2);
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
