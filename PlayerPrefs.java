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

    
    /**
     * Sets the data for the given key with the specified value.
     * 
     * @param key The key to set the data for.
     * @param value The value to set for the key.
     */
    public static void setData(String key, int value) {
        prefs.put(key, value);
    }

    /**
     * Sets the data for the given key with the specified value.
     * 
     * @param key The key to set the data for.
     * @param value The value to set for the key.
     */
    public static void setData(String key, long value) {
        prefs.put(key, value);
    }

    /**
     * Sets the data for the given key with the specified value.
     * 
     * @param key The key to set the data for.
     * @param value The value to set for the key.
     */
    public static void setData(String key, boolean value) {
        prefs.put(key, value);
    }

    /**
     * Sets the data for the given key with the specified value.
     * 
     * @param key The key to set the data for.
     * @param value The value to set for the key.
     */
    public static void setData(String key, String value) {
        prefs.put(key, value);
    }
    

    
    
    /**
     * Retrieves the data for the given key, returning the default value if the key does not exist or the value is of a different type.
     * 
     * @param key The key to retrieve the data for.
     * @param defaultValue The default value to return if the key does not exist or the value is of a different type.
     * @return The value associated with the key, or the default value if the key does not exist or the value is of a different type.
     */
    public static int getData(String key, int defaultValue) {
        Object val = prefs.get(key);
        return (val instanceof Integer) ? (Integer)val : defaultValue;
    }

    /**
     * Retrieves the data for the given key, returning the default value if the key does not exist or the value is of a different type.
     * 
     * @param key The key to retrieve the data for.
     * @param defaultValue The default value to return if the key does not exist or the value is of a different type.
     * @return The value associated with the key, or the default value if the key does not exist or the value is of a different type.
     */
    public static long getData(String key, long defaultValue) {
        Object val = prefs.get(key);
        return (val instanceof Long) ? (Long)val : defaultValue;
    }

    /**
     * Retrieves the data for the given key, returning the default value if the key does not exist or the value is of a different type.
     * 
     * @param key The key to retrieve the data for.
     * @param defaultValue The default value to return if the key does not exist or the value is of a different type.
     * @return The value associated with the key, or the default value if the key does not exist or the value is of a different type.
     */
    public static boolean getData(String key, boolean defaultValue) {
        Object val = prefs.get(key);
        return (val instanceof Boolean) ? (Boolean)val : defaultValue;
    }


    /**
     * Retrieves the data for the given key, returning the default value if the key does not exist or the value is of a different type.
     * 
     * @param key The key to retrieve the data for.
     * @param defaultValue The default value to return if the key does not exist or the value is of a different type.
     * @return The value associated with the key, or the default value if the key does not exist or the value is of a different type.
     */
    public static String getData(String key, String defaultValue) {
        Object val = prefs.get(key);
        return (val instanceof String) ? (String)val : defaultValue;
    }
}