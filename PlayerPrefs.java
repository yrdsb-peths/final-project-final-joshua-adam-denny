import greenfoot.*;
import java.util.HashMap;
import java.util.Map;
/**
 * Allows persistant data across worlds
 * similar to Unity's PlayerPrefs.
 * 
 * @author Denny Ung
 * @version v1.0.0 (June 7th 2025)
 */

public class PlayerPrefs
{
    private static Map<String, Object> prefs = new HashMap<>();

    
    public static void setData(String key, int value) {
        prefs.put(key, value);
    }

    public static void setData(String key, long value) {
        prefs.put(key, value);
    }

    public static void setData(String key, boolean value) {
        prefs.put(key, value);
    }

    public static void setData(String key, String value) {
        prefs.put(key, value);
    }
    

    
    
    public static int getData(String key, int defaultValue) {
        Object val = prefs.get(key);
        return (val instanceof Integer) ? (Integer)val : defaultValue;
    }

    public static long getData(String key, long defaultValue) {
        Object val = prefs.get(key);
        return (val instanceof Long) ? (Long)val : defaultValue;
    }

    public static boolean getData(String key, boolean defaultValue) {
        Object val = prefs.get(key);
        return (val instanceof Boolean) ? (Boolean)val : defaultValue;
    }

    public static String getData(String key, String defaultValue) {
        Object val = prefs.get(key);
        return (val instanceof String) ? (String)val : defaultValue;
    }
}