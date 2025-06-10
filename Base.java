import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Base here.
 * 
 * @Adam fung
 * @version (a version number or a date)
 */
public class Base extends Actor
{
    /**
     * Act - do whatever the Base wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */
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
