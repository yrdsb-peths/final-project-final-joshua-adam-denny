import greenfoot.*;

/**
 * A simple “fast blur” for GreenfootImage that only uses scale(...).
 * You shrink the image, then stretch it back out to full size.
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

        // 1) Compute the “down‑scaled” dimensions
        int smallW = Math.max(1, (int)Math.round(w * factor));
        int smallH = Math.max(1, (int)Math.round(h * factor));

        // 2) Copy the source and scale that copy down to (smallW, smallH):
        GreenfootImage temp = new GreenfootImage(src);
        temp.scale(smallW, smallH);

        // 3) Now scale temp back up to full size (w, h). This produces the blur:
        temp.scale(w, h);

        // 4) Return that resulting image
        return temp;
    }
}
