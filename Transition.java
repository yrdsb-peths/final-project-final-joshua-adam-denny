import greenfoot.*;

/**
 * Write a description of class Transition here.
 * 
 * @author (your name)
 * @version (a version number or a date)
 */
public class Transition extends UI {

    public static Transition _instance;
    private int timeToFade = 1000; // time in ms, default is 1000ms (1 second)
    private int currentOpacity = 0;
    private int targetOpacity = 0;
    private SimpleTimer deltaTime = new SimpleTimer();
    private GreenfootImage screen;
    private String state = "none"; // "fadeIn", "fadeOut", or "none"

    public Transition() {
        deltaTime = new SimpleTimer();
        deltaTime.mark();
        screen = new GreenfootImage(1160, 600);
        screen.setTransparency(targetOpacity);
        screen.setColor(new Color(0, 0, 0));
        screen.fill();
        setImage(screen);
    }

    public static Transition getInstance() {
        if (_instance == null) {
            _instance = new Transition();
        }
        return _instance;
    }

    public void fadeIn(int targetOpacity, int targetTime) {
        this.timeToFade = Math.max(targetTime, 0);
        this.targetOpacity = Math.max(targetOpacity, 0);
        state = "fadeIn";
        deltaTime.mark();
    }

    public void fadeOut(int targetOpacity, int targetTime) {
        this.timeToFade = Math.max(targetTime, 0);
        this.targetOpacity = Math.min(targetOpacity, 255);
        state = "fadeOut";
        deltaTime.mark();
    }

    // public void act() {
    //     int elapsed = deltaTime.millisElapsed();
    //     if ("fadeIn".equals(state)) {
    //         int opacity = (int) Utils.map(elapsed,0,timeToFade, currentOpacity, targetOpacity);
    //         screen.setTransparency(opacity);
    //         screen.fill();
    //         setImage(screen);

    //         System.out.println("Current Opacity: " + opacity + " Target Opacity: " + targetOpacity);
    //         if (opacity >= targetOpacity) {
    //             currentOpacity = targetOpacity;

    //             state = "none"; // Reset state after fade in completes
    //         }

    //     } else if ("fadeOut".equals(state)) {
    //         int opacity = (int) Utils.map(elapsed,0,timeToFade, currentOpacity, targetOpacity);
    //         screen.setTransparency(opacity);
    //         screen.fill();
    //         setImage(screen);

    //         System.out.println("Current Opacity: " + opacity + " Target Opacity: " + targetOpacity);
    //         if (opacity <= targetOpacity) {
    //             currentOpacity = targetOpacity;
    //             state = "none"; // Reset state after fade out completes
    //         }
            
    //     }
    // }

}
