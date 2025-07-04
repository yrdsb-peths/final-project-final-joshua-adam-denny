import greenfoot.*;

/**
 * Transition class for handling fade in between worlds, and game states.
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (May 29, 2025)
 */
public class Transition extends UI {

    public static Transition _instance;
    private int timeToFade = 1000; // time in ms, default is 1000ms (1 second)
    private int currentOpacity = 0;
    private int targetOpacity = 0;
    private SimpleTimer deltaTime = new SimpleTimer();
    private GreenfootImage screen;
    private String state = "none"; // "fadeIn", "fadeOut", or "none"

    /**
     * Constructor for Transition class.
     * Initializes the transition screen with default values.
     */
    public Transition() {
        deltaTime = new SimpleTimer();
        deltaTime.mark();
        screen = new GreenfootImage(1160, 600);
        screen.setTransparency(targetOpacity);
        screen.setColor(new Color(0, 0, 0));
        screen.fill();
        setImage(screen);
    }
    

    /**
     * Called when the Transition actor is added to the world.
     * Initializes the current opacity and sets the screen transparency.
     * 
     * @param w The world to which this actor is added.
     */
    @Override
    protected void addedToWorld(World w) 
    {
        currentOpacity = 0;
        screen.setTransparency(currentOpacity);
    }


    /**
     * Singleton instance getter for Transition class.
     * Ensures only one instance of Transition exists.
     * 
     * @return The single instance of Transition.
     */
    public static Transition getInstance() {
        if (_instance == null) {
            _instance = new Transition();
        }
        return _instance;
    }



    /**
     * Starts a fadein transition to the specified target opacity over the specified time.
     * @param targetOpacity (0-255)
     * @param targetTime time in ms to complete the fadein
     */
    public void fadeIn(int targetOpacity, int targetTime) {
        this.timeToFade = Math.max(targetTime, 0);
        this.targetOpacity = (int) Utils.clamp(targetOpacity, 0, 255);
        state = "fadeIn";
        deltaTime.mark();
    }

    /**
     * Starts a fadeout transition to the specified target opacity over the specified time.
     * @param targetOpacity (0-255)
     * @param targetTime time in ms to complete the fadeout
     */
    public void fadeOut(int targetOpacity, int targetTime) {
        this.timeToFade = Math.max(targetTime, 0);
        this.targetOpacity = (int) Utils.clamp(targetOpacity, 0, 255);
        state = "fadeOut";
        deltaTime.mark();
    }

    /**
     * Act method called by Greenfoot to update the transition state.
     * Handles the fade in and fade out transitions based on elapsed time.
     */
    public void act() {

        int elapsed = deltaTime.millisElapsed();
        if (!state.equals("none")) {
            int opacity = (int) Utils.map(elapsed,0,timeToFade, currentOpacity, targetOpacity);
            
            screen.setTransparency((int)Utils.clamp(opacity,0,255));
            screen.fill();
            setImage(screen);

            if (opacity >= targetOpacity && state.equals("fadeIn")) {
                currentOpacity = targetOpacity;
                state = "none"; 
            }

            if (opacity <= targetOpacity && state.equals("fadeOut")) {
                currentOpacity = targetOpacity;
                state = "none"; // Reset state after fade out completes
            }

        }

    }

}
