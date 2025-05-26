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

    /**
     * Constructor for objects of class EndGamePopup.
     * 
     * @param wave The wave number reached in the game.
     * @param moneyEarned The total money earned during the game.
     * @param moneySpent The total money spent during the game.
     */
    public EndGamePopup(int wave, int moneyEarned, int moneySpent)
    {   
        this.wave = wave;
        this.moneyEarned = moneyEarned;
        this.moneySpent = moneySpent;


        GreenfootImage image = new GreenfootImage("ui/EndGamePopUp.png");
        setImage(image);
        image.scale(300, 300);
    }



    public void act()
    {
        // Add your action code here.
    }
}
