import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class EndGameLabel here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class EndGameLabel extends Label
{
    public EndGameLabel(int value, int fontSize)
    {
        super(Integer.toString(value), fontSize);
    }
    
    /**
     * Create a new label, initialise it with the needed text and the font size 
     */
    public EndGameLabel(String value, int fontSize)
    {
        super(value,fontSize);
    }
}
