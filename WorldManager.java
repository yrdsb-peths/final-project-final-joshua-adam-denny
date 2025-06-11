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
    
    private static void inital()
    {
        PolyRender.reset();
    }
    
    public static void setWorld(World world)
    {
        inital();
        Greenfoot.setWorld(world);
    }
    
    public static String getFontName()
    {
        return fontName;
    }
    
    static {
        try {
            // 1) Load the TTF from your JAR (in /fonts)
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
