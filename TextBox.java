import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * TextBox Class is a UI component that allows users to input text.
 * Personally, I had a stroke while making this, i forgot greenfoot.ask() existed...
 * :(
 * 
 * @author Denny Ung
 * @version Version 1.0
 */

import greenfoot.*;

public class TextBox extends UI {
    private String text = "";
    private int width, height;
    private boolean isSelected;
    private int fontSize;
    private Color textColor;
    private Color bgColor;
    private Color cursorColor;

    public TextBox(int width, int height, int fontSize) {
        this.width = width;
        this.height = height;
        this.fontSize = fontSize;
        this.textColor = new Color(0, 0, 0);
        this.bgColor = new Color(255, 255, 255, 255); 
        this.cursorColor = new Color(0, 0, 200, 255);
        isSelected = false;
        updateImage();
    }

    private int getTextPixelWidth() {
        if (text.isEmpty()) {
            return 0;
        }
        GreenfootImage temp = new GreenfootImage(text, fontSize, textColor, new Color(0, 0, 0, 0));
        return temp.getWidth(); 
    }

    private void updateImage() {
        GreenfootImage img = new GreenfootImage(width, height);
        img.setFont(new Font(fontSize));
        if (isSelected) {
            img.setColor(new Color(230, 230, 255, 255));
        } else {
            img.setColor(bgColor);
        }
        img.fill();

        if (isSelected) {
            img.setColor(cursorColor);
        } else {
            img.setColor(new Color(0, 0, 0, 255));
        }
        img.drawRect(0, 0, width - 1, height - 1);

        img.setColor(textColor);
        int textBaselineY = height - 6;
        img.drawString(text, 5, textBaselineY);

        if (isSelected) {
            int textWidth = getTextPixelWidth();
            int cursorX = 5 + textWidth + 1;
            int cursorY1 = 4;
            int cursorY2 = height - 6;
            img.setColor(cursorColor);
            img.drawLine(cursorX, cursorY1, cursorX, cursorY2);
        }

        setImage(img);
    }

    
    public void act() {
        // Check if the mouse is clicked anywhere
        if (Greenfoot.mouseClicked(null)) {
            // If the mouse is clicked on this TextBox, select it
            if (Greenfoot.mouseClicked(this)) {
                isSelected = true;
            } else {
                // Otherwise, deselect the TextBox
                isSelected = false;
            }
            // Update the appearance of the TextBox
            updateImage();
        }

        // If the TextBox is selected, handle keyboard input
        if (isSelected) {
            String key = Greenfoot.getKey(); // Get the key pressed by the user
            if (key != null) {
                // Handle backspace key to delete the last character
                if (key.equals("backspace")) {
                    if (!text.isEmpty()) {
                        text = text.substring(0, text.length() - 1);
                    }
                } 
                // Append the key to the text if it's a single character
                else if (key.length() == 1) {
                    text += key;
                }
                // Update the appearance of the TextBox after text changes
                updateImage();
            }
        }
    }

    public String getText() {
        return text;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
