import greenfoot.*;
import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.io.InputStream;
/**
 * Handles world loading. And maybe a lil font loading :3
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (June 9, 2025)
 */
public class WorldManager  
{
    private static String fontName = "SansSerif";
    

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


    // Loads the font from a file when the class is loaded.
    static {
        try {
            // 1) Load the TTF in /fonts
            InputStream is = LeaderboardPage.class.getResourceAsStream("fonts/Jersey15.ttf");
            java.awt.Font awtFont = java.awt.Font.createFont(
                java.awt.Font.TRUETYPE_FONT, is
            );
            // 2) Register it with the GraphicsEnvironment
            GraphicsEnvironment ge =
                GraphicsEnvironment.getLocalGraphicsEnvironment();
            ge.registerFont(awtFont);

            // 3) Grab its family name for use in Greenfoot.Font
            String family= awtFont.getFamily();
            fontName = family;
        }
        catch (Exception e) {
            e.printStackTrace();
            // fall back to a logical font
        }
    }
}
