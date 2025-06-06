import greenfoot.*;

/**
 * Handles world loading.
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (June 6, 2025)
 */
public class WorldManager  
{

    
    private static void inital()
    {
        PolyRender.X_Pos = 0;
        PolyRender.Y_Pos = 0; 
        PolyRender.Z_Pos = 0;
        PolyRender.X_Rot = 0;
        PolyRender.Y_Rot = 0;
        PolyRender.Z_Rot = 0;
    
    }
    
    public static void setWorld(World world)
    {
        inital();
        Greenfoot.setWorld(world);
    }
}
