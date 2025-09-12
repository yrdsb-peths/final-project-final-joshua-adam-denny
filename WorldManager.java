import greenfoot.*;

/**
 * Handles world loading and manages font selection (Impact instead of Jersey15).
 * 
 * @author Denny Ung
 * @version Version 1.0.1 (Updated for HTML5 compatibility)
 */
public class WorldManager  
{
    // Set to "Impact" as a fallback for Jersey15
    private static String fontName = "Impact";

    /**
     * Resets the PolyRender to its initial state.
     * This method is called to ensure that the PolyRender is ready for a new world.
     */
    private static void inital()
    {
        PolyRender.reset();
    }

    /**
     * Sets the world to the given world instance.
     * This method resets the PolyRender and sets the world in Greenfoot.
     * 
     * @param world The world to set.
     */
    public static void setWorld(World world)
    {
        inital();
        Greenfoot.setWorld(world);
    }

    /**
     * Returns the name of the font used in the game.
     * 
     * @return The name of the font.
     */
    public static String getFontName()
    {
        return fontName;
    }

    /**
     * Sets the font name manually, if needed (e.g., to fallback to "SansSerif").
     */
    public static void setFontName(String name)
    {
        fontName = name;
    }

    // Static block left intact for consistency; no .ttf loading in HTML5 export
    static {
        try {
            // In HTML5 export, we can't load external fonts — fallback to "Impact"
            fontName = "Impact";  // Hardcoded font fallback
        }
        catch (Exception e) {
            e.printStackTrace();
            fontName = "SansSerif"; // Final fallback
        }
    }
}
