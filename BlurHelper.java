import greenfoot.*;

/**
 * Utility for fast box‑blur on a GreenfootImage, using a two‑pass algorithm.
 */
public class BlurHelper
{
    /**
     * Perform a two‑pass box‑blur on 'src' with radius 'r', returning a new blurred image.
     * 
     * Complexity: O(r · W · H) instead of O(r² · W · H).
     * 
     * @param src    The original GreenfootImage to blur.
     * @param radius The blur radius (averages a (2r+1)×(2r+1) box).
     * @return       A new GreenfootImage, same size, with box‑blur applied.
     */
    public static GreenfootImage blur(GreenfootImage src, int radius)
    {
        int w = src.getWidth();
        int h = src.getHeight();
        if (radius <= 0) {
            // No blur: return a copy
            return new GreenfootImage(src);
        }

        // 1) First pass: horizontal blur → store into temp
        GreenfootImage temp = new GreenfootImage(w, h);

        for (int y = 0; y < h; y++) {
            // Running sums for this row:
            int sumR = 0, sumG = 0, sumB = 0;
            int count = 0;

            // Initialize window [0..radius] (for x=0)
            for (int dx = -radius; dx <= radius; dx++) {
                int xx = dx;
                if (xx < 0 || xx >= w) continue;
                Color c = src.getColorAt(xx, y);
                sumR += c.getRed();
                sumG += c.getGreen();
                sumB += c.getBlue();
                count++;
            }
            // Write blurred pixel at (0, y):
            {
                int rAvg = sumR / count;
                int gAvg = sumG / count;
                int bAvg = sumB / count;
                temp.setColorAt(0, y, new Color(rAvg, gAvg, bAvg));
            }

            // Slide window across x = 1 .. w-1
            for (int x = 1; x < w; x++) {
                int left  = x - radius - 1;
                int right = x + radius;
                // Remove left pixel if in bounds
                if (left >= 0) {
                    Color cL = src.getColorAt(left, y);
                    sumR -= cL.getRed();
                    sumG -= cL.getGreen();
                    sumB -= cL.getBlue();
                    count--;
                }
                // Add right pixel if in bounds
                if (right < w) {
                    Color cR = src.getColorAt(right, y);
                    sumR += cR.getRed();
                    sumG += cR.getGreen();
                    sumB += cR.getBlue();
                    count++;
                }
                // Compute average and write to temp
                int rAvg = sumR / count;
                int gAvg = sumG / count;
                int bAvg = sumB / count;
                temp.setColorAt(x, y, new Color(rAvg, gAvg, bAvg));
            }
        }

        // 2) Second pass: vertical blur → store into dst
        GreenfootImage dst = new GreenfootImage(w, h);
        for (int x = 0; x < w; x++) {
            int sumR = 0, sumG = 0, sumB = 0;
            int count = 0;

            // Initialize window [0..radius] for y=0
            for (int dy = -radius; dy <= radius; dy++) {
                int yy = dy;
                if (yy < 0 || yy >= h) continue;
                Color c = temp.getColorAt(x, yy);
                sumR += c.getRed();
                sumG += c.getGreen();
                sumB += c.getBlue();
                count++;
            }
            // Write blurred pixel at (x, 0):
            {
                int rAvg = sumR / count;
                int gAvg = sumG / count;
                int bAvg = sumB / count;
                dst.setColorAt(x, 0, new Color(rAvg, gAvg, bAvg));
            }

            // Slide window down for y=1..h-1
            for (int y = 1; y < h; y++) {
                int top    = y - radius - 1;
                int bottom = y + radius;
                // Remove top pixel if in bounds
                if (top >= 0) {
                    Color cT = temp.getColorAt(x, top);
                    sumR -= cT.getRed();
                    sumG -= cT.getGreen();
                    sumB -= cT.getBlue();
                    count--;
                }
                // Add bottom pixel if in bounds
                if (bottom < h) {
                    Color cB = temp.getColorAt(x, bottom);
                    sumR += cB.getRed();
                    sumG += cB.getGreen();
                    sumB += cB.getBlue();
                    count++;
                }
                // Compute average and write to dst
                int rAvg = sumR / count;
                int gAvg = sumG / count;
                int bAvg = sumB / count;
                dst.setColorAt(x, y, new Color(rAvg, gAvg, bAvg));
            }
        }

        return dst;
    }
}
