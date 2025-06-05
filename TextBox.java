import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class TextBox here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
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
        if (Greenfoot.mouseClicked(null)) {
            if (Greenfoot.mouseClicked(this)) {
                isSelected = true;
            } else {
                isSelected = false;
            }
            updateImage();
        }

        if (isSelected) {
            String key = Greenfoot.getKey();
            if (key != null) {
                if (key.equals("backspace")) {
                    if (!text.isEmpty()) {
                        text = text.substring(0, text.length() - 1);
                    }
                } else if (key.length() == 1) {
                    text += key;
                }
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
