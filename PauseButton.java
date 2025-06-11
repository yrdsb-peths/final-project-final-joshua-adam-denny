import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * PauseButton - UI - USED FOR PAINT/RENDERING QUEUE
 * Used to make sure stuff is rendered in the correct order
 * 
 * @author Denny Ung
 * @version version 1.0
 */
public class PauseButton extends Button
{
    public PauseButton(boolean isActive, GreenfootImage[] buttonImages, int width, int height)
    {
        super(isActive,buttonImages,width,height);
    }
}
