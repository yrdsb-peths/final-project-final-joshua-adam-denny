import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class Button here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Button extends UI
{
    private int buttonWidth = 0;
    private int buttonHeight = 0;
    private GreenfootImage[] buttonImage = new GreenfootImage[2];
    private boolean pressed = false;
    private boolean active = false;
    private int leftSide = 0;
    private int rightSide = 0;
    private int topSide = 0;
    private int bottomSide = 0;
    
    
    
    public Button(boolean isActive, GreenfootImage buttonReleased, GreenfootImage buttonPressed, int width, int height)
    {
        active = isActive;
        buttonWidth = width;
        buttonHeight = height;
        
        buttonImage[0] = buttonReleased;
        buttonImage[1] = buttonPressed;
        
        buttonImage[0].scale(width,height);
        buttonImage[1].scale(width,height);
        
        setImage(buttonImage[0]);
    }
    
    @Override
    protected void addedToWorld(World w) {
        leftSide = getX() - (buttonWidth/2);
        rightSide = getX() + (buttonWidth/2);
        topSide = getY() - (buttonHeight/2);
        bottomSide = getY() + (buttonHeight/2);
    }
    
    public boolean isPressed()
    {
        return pressed;
    }
    
    public void setActive(boolean activeSet)
    {
        active = activeSet;
    }
    
    private void HandlePressing()
    {
        MouseInfo mi = Greenfoot.getMouseInfo();
        if (mi != null) {
            int xData = mi.getX();
            int yData = mi.getY();
            
            if (yData >= topSide && yData <= bottomSide)
            {
                System.out.println("y side");
                if (xData >= leftSide && xData <= rightSide)
                {
                    System.out.println("x side");
                    if (mi.getButton() == 0)
                    {
                        System.out.println("holy fuck im cumming");
                        pressed = true;
                        setImage(buttonImage[1]);
                    } else
                    {
                        pressed = false;
                        setImage(buttonImage[0]);
                    }
                }
            }
        }
        
    }
    
    public void act()
    {
        if (active)
        {
            if (Greenfoot.mousePressed(this)) {
                pressed = true;
                setImage(buttonImage[1]);
            }
            // when they release the mouse button _over this actor_
            if (Greenfoot.mouseClicked(this)) {
                pressed = false;
                setImage(buttonImage[0]);
                // here you could also fire your “button action”
            }
        }
    }
    
    
}
