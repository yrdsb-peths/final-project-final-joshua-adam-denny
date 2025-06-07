import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * A checkbox, UI
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (June 7, 2025)
 */

public class CheckButton extends UI {
    private final GreenfootImage uncheckedImg;
    private final GreenfootImage checkedImg;
    private boolean checked = false;

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
    
    public void setTransparency(int alpha)
    {
        alpha = (int)Utils.clamp(alpha,0,255);
        uncheckedImg.setTransparency(alpha);
        checkedImg.setTransparency(alpha);
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean value) {
        checked = value;
        setImage( checked ? checkedImg : uncheckedImg );
    }
}
