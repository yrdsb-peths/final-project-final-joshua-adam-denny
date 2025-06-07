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

    public CheckButton() {
        uncheckedImg = new GreenfootImage("ui/checkbutton.png");
        checkedImg   = new GreenfootImage("ui/checkbuttonChecked.png");
        setImage(uncheckedImg);
    }

    @Override
    public void act() {
        if (Greenfoot.mouseClicked(this)) {
            checked = !checked;
            setImage( checked ? checkedImg : uncheckedImg );
        }
    }

    public boolean isChecked() {
        return checked;
    }

    public void setChecked(boolean value) {
        checked = value;
        setImage( checked ? checkedImg : uncheckedImg );
    }
}
