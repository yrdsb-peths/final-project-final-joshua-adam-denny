import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * HelpButton - UI - USED FOR PAINT/RENDERING QUEUE
 * Used to make sure stuff is rendered in the correct order
 * 
 * @author Denny Ung
 * @version Version 1.0
 */
public class HelpButton extends Button
{

    /**
     * Constructor for HelpButton
     * 
     * @param isActive Whether the button is active or not
     * @param buttonImages Array of images for the button states
     * @param width Width of the button
     * @param height Height of the button
     */
    public HelpButton(boolean isActive, GreenfootImage[] buttonImages, int width, int height)
    {
        super(isActive,buttonImages,width,height);
    }
}
