import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Head Processor for 3D rendering.
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (May 27, 2025)
 */
public class DDCRender extends Actor
{
    
    public static DDCRender _instance = null;
    Camera camera;
    PolyRender poly2;
    int halfWidth = 0;
    int halfHeight = 0;
    

    

    public DDCRender() {
        _instance = this;
    }

    protected void addedToWorld(World world)
    {

    }

    public static DDCRender getInstance() {
        return _instance;
    }
    
    public void act()
    {

    }
}
