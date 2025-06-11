import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Button - UI
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
    private boolean hoverMode = false;
    


    
    /**
     * Constructor for Button.
     * 
     * @param isActive Whether the button is active or not.
     * @param buttonImages Array of images for the button states (normal, pressed, hover).
     * @param width Width of the button.
     * @param height Height of the button.
     */
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
        
        if (buttonImage.length > 2)
        {
            hoverMode = true;
            leftSide   = getX() - (buttonWidth/2);
            rightSide  = getX() + (buttonWidth/2);
            topSide    = getY() - (buttonHeight/2);
            bottomSide = getY() + (buttonHeight/2);
        }
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

    
    // Sets the transparency of the button images.
    public void setTransparency(int alpha)
    {
        alpha = (int)Utils.clamp(alpha,0,255);
        for(GreenfootImage button : buttonImage)
        {  
            button.setTransparency(alpha);
        }
        
    }
    
    // Sets the button images to the provided array of images.
    public void setButtons(GreenfootImage[] buttonImages)
    {
        buttonImage = buttonImages;
        if (pressed)
        {
            currentButton = buttonImage[1];
        }
        else
        {
            currentButton = buttonImage[0];
        }
        setImage(currentButton);
    }

    // Resets the pressed state of the button.
    public void resetPressedState() {
        pressed = false;
        currentButton = buttonImage[0];
        setImage(currentButton);
    }


    // Sets the pressed state of the button and updates the current button image.
    public void setPressed(boolean isPressed) {
        this.pressed = isPressed;
        currentButton = isPressed ? buttonImage[1] : buttonImage[0];
        setImage(currentButton);
    }

    // Checks if the mouse is hovering over the button.
    public boolean isHovering() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse == null) return false;
        return (mouse.getX() >= leftSide && mouse.getX() <= rightSide &&
                mouse.getY() >= topSide && mouse.getY() <= bottomSide);
    }
    
    public void act()
    {
        // If the button is not active, we don't need to do anything
        if (active)
        {
            if (isHovering() && hoverMode)
            {
                currentButton = buttonImage[2];
            }
            
            
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
            
            if (pressed && Greenfoot.mouseClicked(null) && !Greenfoot.mouseClicked(this)) {
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
