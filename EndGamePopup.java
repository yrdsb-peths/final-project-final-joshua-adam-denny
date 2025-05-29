import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class EndGamePopup here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EndGamePopup extends UI
{   
    private int wave;
    private int moneyEarned;
    private int moneySpent;

    private int timeToPopup = 1000;
    private int timeToFade = 2000; 

    private SimpleTimer deltaTime = new SimpleTimer();
    private GreenfootImage image;
    private int startY;
    private int targetY;
        

    /**
     * Constructor for objects of class EndGamePopup.
     * 
     * @param wave The wave number reached in the game.
     * @param moneyEarned The total money earned during the game.
     * @param moneySpent The total money spent during the game.
     * @param timeToPopup The time in milliseconds before the popup appears.
     */
    public EndGamePopup(int wave, int moneyEarned, int moneySpent, int timeToPopup)
    {   
        this.wave = wave;
        this.moneyEarned = moneyEarned;
        this.moneySpent = moneySpent;
        this.timeToPopup = timeToPopup;

        // Initialize the image for the popup
        image = new GreenfootImage("ui/EndGamePopUp.png");
        image.scale(300, 300);
        image.setTransparency(0);
        setImage(image);


        deltaTime.mark();
    }

    @Override
    protected void addedToWorld(World w) {
        startY = getY();
        targetY = w.getHeight()/2; // always center to it to the middle of the world.
    }


    public void act()
    {
        int elapsed = deltaTime.millisElapsed();

        // wait for the transition class to finish up its thing
        if (elapsed < timeToPopup) {
            return;
        }

        // deltaTime after popup has started
        int dtActive = elapsed - timeToPopup;
        if (dtActive > timeToFade) {
            dtActive = timeToFade;
        }


        //cos curve easing type sh, love smoothness frfr
        double progress = dtActive / (double)timeToFade;
        double base = (1 - Math.cos(progress * Math.PI)) * 0.5;
        double exponent = 3;  
        double ease = Math.pow(base, exponent);

        // update transparency of the image
        int alpha = (int)(ease * 255);
        image.setTransparency(alpha);
        setImage(image);

        // update the position of the popup/actor
        int newY = startY + (int)(ease * (targetY - startY));
        setLocation(getX(), newY);


        //lock tf IN.)
        if (dtActive >= timeToFade) {
            image.setTransparency(255);
            setLocation(getX(), targetY);
        }
    }
}
