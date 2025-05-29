import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Transition here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Transition extends UI
{
    private int timeToFade;
    private int toOpacity;


    public Transition(int timeToFade, int toOpacity)
    {   
        SimpleTimer deltaTime = new SimpleTimer();
        deltaTime.mark();

        this.timeToFade = timeToFade;
        this.toOpacity = toOpacity;

        
        GreenfootImage image = new GreenfootImage(1000, 600);
        image.setTransparency(0); // Start fully transparent
        image.setColor(new Color(0, 0, 0)); // Set color to black
        setImage(image);
    }
    
    public void fadeIn()
    {
        GreenfootImage image = getImage();
        int currentOpacity = image.getTransparency();
        int opacityStep = (toOpacity - currentOpacity) / Math.max(1, timeToFade);

        if (currentOpacity < toOpacity) {
            image.setTransparency(Math.min(currentOpacity + opacityStep, toOpacity));
            setImage(image);
        }
    }
    
    public void fadeOut()
    {

    
    
    }
}
