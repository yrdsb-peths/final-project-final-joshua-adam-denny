import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A actor that represents the base in the game.
 * 
 * @author Adam fung
 * @version Version 1.1;
 */
public class Base extends Actor
{
    // Base class for the game, representing the player's base
    public Base() 
    {
        
        GreenfootImage img = new GreenfootImage("Tower.png");
        img.scale(180, 315);
        setImage(img);
        
        GreenfootImage dude = new GreenfootImage("whiteDude.png");
        dude.scale(50, 50);
        img.drawImage(dude, 30, 110);
    }
}
