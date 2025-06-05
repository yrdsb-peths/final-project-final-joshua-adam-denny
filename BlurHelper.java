import greenfoot.*;

/**
 * Fast blur, uses block blur instead of gaussian blur cuz it takes forever to use :(
 * Built for ScuffedEngine
 * @author Denny Ung
 * @version Version 2.0.0 (June 4, 2025)
 */

public class BlurHelper
{
    /**
     * Return a “blurred” copy of src by shrinking it by factor, then re‑expanding.
     *
     * @param src     The original GreenfootImage to blur.
     * @param factor  How much to shrink before re‑expanding. 
     *                Must be between (0,1).  Smaller = stronger blur.
     * @return        A new GreenfootImage, same size, but blurred.
     */
    public static GreenfootImage fastBlur(GreenfootImage src, double factor)
    {
        int w = src.getWidth();
        int h = src.getHeight();
        // If factor is outside (0,1), just return an unmodified copy:
        if (factor <= 0 || factor >= 1.0) {
            return new GreenfootImage(src);
        }

        int smallW = Math.max(1, (int)Math.round(w * factor));
        int smallH = Math.max(1, (int)Math.round(h * factor));

        GreenfootImage temp = new GreenfootImage(src);
        temp.scale(smallW, smallH);

        temp.scale(w, h);

        return temp;
    }
}
