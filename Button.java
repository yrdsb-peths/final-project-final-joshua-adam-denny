import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Button UI
 * 
 * @author Denny Ung
 * @version Version 1.0.0
 */
public class Button extends UI
{
    private int buttonWidth = 0;
    private int buttonHeight = 0;
    private GreenfootImage[] buttonImage;
    private GreenfootImage currentButton;
    private boolean pressed = false;
    private boolean active = false;
    private int leftSide = 0;
    private int rightSide = 0;
    private int topSide = 0;
    private int bottomSide = 0;
    private boolean hasHighlighted = false;
    
    
    
    public Button(boolean isActive, GreenfootImage[] buttonImages, int width, int height)
    {
        active = isActive;
        buttonWidth = width;
        buttonHeight = height;
        
        buttonImage = buttonImages;
        
        buttonImage[0].scale(width,height);
        buttonImage[1].scale(width,height);
        
        currentButton = buttonImage[0];
        setImage(currentButton);
    }
    
    @Override
    protected void addedToWorld(World w) {
        leftSide = getX() - (buttonWidth/2);
        rightSide = getX() + (buttonWidth/2);
        topSide = getY() - (buttonHeight/2);
        bottomSide = getY() + (buttonHeight/2);
    }
    
    
    
    //Getter functions
    
    public boolean isPressed()
    {
        return pressed;
    }
    
    //setter functions
    
    public void setActive(boolean activeSet)
    {
        active = activeSet;
        currentButton = (active) ? buttonImage[0] : buttonImage[1];
        
        setImage(currentButton);
    }

    
    public void setTransparency(int alpha)
    {
        alpha = (int)Utils.clamp(alpha,0,255);
        for(GreenfootImage button : buttonImage)
        {  
            button.setTransparency(alpha);
        }
        
    }
    
    public void setButtons(GreenfootImage[] buttonImages)
    {
        buttonImage = buttonImages;
    
    }
    public void resetPressedState() {
        pressed = false;
        currentButton = buttonImage[0];
        setImage(currentButton);
    }

    public void setPressed(boolean isPressed) {
        this.pressed = isPressed;
        currentButton = isPressed ? buttonImage[1] : buttonImage[0];
        setImage(currentButton);
    }

    public boolean isHovering() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return false;
        return (mouse.getX() >= leftSide && mouse.getX() <= rightSide &&
                mouse.getY() >= topSide && mouse.getY() <= bottomSide);
    }
    
    public void act()
    {
        if (active)
        {
            if (Greenfoot.mousePressed(this)) {
                pressed = true;
                currentButton = buttonImage[1];
            }
    
            if (pressed && Greenfoot.mouseDragEnded(null)) {
                // Reset if mouse drag ends (anywhere) while we were pressed
                pressed = false;
                currentButton = buttonImage[0];
            }
    
            if (Greenfoot.mouseClicked(this)) {
                pressed = false;
                currentButton = buttonImage[0];
            }
        } 
        else
        {
            currentButton = buttonImage[1];
        }
    
        setImage(currentButton);
    }

    
    
}
