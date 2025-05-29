/**
 * Write a description of class utils here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
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
}
