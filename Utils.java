/**
 * Write a description of class utils here.
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (May 29th 2025)
 */
public class Utils  
{
    /**
     * Maps a value from one range to another.
     * @param x the value to map
     * @param inMin the minimum of the input range
     * @param inMax the maximum of the input range
     *  
     * @param outMin the minimum of the output range
     * @param outMax the maximum of the output range
     * @return the mapped value in the output range 
     */
    
    public static double map(double x, double inMin, double inMax, double outMin, double outMax) {
        return (x - inMin) * (outMax - outMin) / (inMax - inMin) + outMin;
    }
    
    /**
     * Clamps a value between a minimum and maximum.
     * @param value the value to clamp
     * @param min the minimum value
     * @param max the maximum value
     * @return the clamped value
     */
    public static double clamp(int value, int min, int max)
    {
        return Math.max(min, Math.min(value, max));
    }
}
