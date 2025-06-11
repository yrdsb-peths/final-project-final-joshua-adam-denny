import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A checkbox, UI element that allows users to toggle a checked state.
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (June 7, 2025)
 */

public class CheckButton extends UI {
    private final GreenfootImage uncheckedImg;
    private final GreenfootImage checkedImg;
    private boolean checked = false;


    /**
     * Constructor for CheckButton.
     * 
     * @param size The size of the checkbox in pixels.
     */
    public CheckButton(int size) {
        uncheckedImg = new GreenfootImage("ui/checkbox.png");
        checkedImg = new GreenfootImage("ui/checkboxchecked.png");
        uncheckedImg.scale(size,size);
        checkedImg.scale(size,size);
        setImage(uncheckedImg);
    }

    @Override
    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            checked = !checked;
        }
        
        setImage( checked ? checkedImg : uncheckedImg );
    }
    

    /**
     * Sets the transparency of the checkbox images.
     * 
     * @param alpha The alpha value (0-255) for transparency.
     */
    public void setTransparency(int alpha)
    {
        alpha = (int)Utils.clamp(alpha,0,255);
        uncheckedImg.setTransparency(alpha);
        checkedImg.setTransparency(alpha);
    }


    /**
     * Checks if the checkbox is currently checked.
     * 
     * @return true if checked, false otherwise.
     */
    public boolean isChecked() {
        return checked;
    }


    /**
     * Sets the checked state of the checkbox.
     * 
     * @param value true to check the checkbox, false to uncheck it.
     */
    public void setChecked(boolean value) {
        checked = value;
        setImage( checked ? checkedImg : uncheckedImg );
    }
}
