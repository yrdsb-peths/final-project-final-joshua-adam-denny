import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * MainMenuSideButton - UI - USED FOR PAINT/RENDERING QUEUE
 * Used to make sure stuff is rendered in the correct order
 * 
 * @author Denny Ung
 * @version Version 1.0
 */
public class MainMenuSideButton extends Button
{
    public MainMenuSideButton(boolean isActive, GreenfootImage[] buttonImages, int width, int height)
    {
        super(isActive,buttonImages,width,height);
    }
}
