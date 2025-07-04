import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)


/**
 * A Label class that allows you to display a textual value on screen.
 * 
 * The Label is an actor, so you will need to create it, and then add it to the world
 * in Greenfoot.  If you keep a reference to the Label then you can change the text it
 * displays.  
 * 
 * Admendment v1.2:
 * Added Custom fonts, with dynamic sizing.
 * - Denny Ung
 *
 * @author Amjad Altadmri, Denny Ung
 * @version 1.2
 */
public class CustomLabel extends UI
{
    private String value;
    private int fontSize;
    private Color lineColor = Color.BLACK;
    private Color fillColor = Color.WHITE;
    private Font font;
    private int alpha = 255;
    private static final Color transparent = new Color(0,0,0,0);
    
    
    /**
     * Create a new label, initialise it with the int value to be shown and the font size 
     */
    public CustomLabel(int value, int fontSize)
    {
        this(Integer.toString(value), fontSize);
    }
    
    /**
     * Create a new label, initialise it with the needed text and the font size 
     */
    public CustomLabel(String value, int fontSize)
    {
        this.value = value;
        this.fontSize = fontSize;
        updateImage();
    }

    /**
     * Sets the value  as text
     * 
     * @param value the text to be show
     */
    public void setValue(String value)
    {
        this.value = value;
        updateImage();
    }
    
    /**
     * Sets the value as integer
     * 
     * @param value the value to be show
     */
    public void setValue(int value)
    {
        this.value = Integer.toString(value);
        updateImage();
    }
    
    /**
     * Sets the line color of the text
     * 
     * @param lineColor the line color of the text
     */
    public void setLineColor(Color lineColor)
    {
        this.lineColor = lineColor;
        updateImage();
    }
    
    /**
     * Sets the fill color of the text
     * 
     * @param fillColor the fill color of the text
     */
    public void setFillColor(Color fillColor)
    {
        this.fillColor = fillColor;
        updateImage();
    }
    
    public void setFont(Font font)
    {
        this.font = font;
        updateImage();
    }
    
    public void setTransparency(int alpha)
    {
        this.alpha = alpha;
        updateImage();
    }
    

    /**
     * Update the image on screen to show the current value.
     */
    private void updateImage()
    {
        GreenfootImage meas = new GreenfootImage(
            value, fontSize, fillColor, transparent, lineColor
        );
        int w = meas.getWidth()+ 20;
        int h = meas.getHeight() + 10;
        
        GreenfootImage label = new GreenfootImage(w, h);
        
        if (font != null) {
            label.setFont(font);
        }
        
        label.setColor(fillColor);
        label.drawString(value, 1, fontSize);
        
        label.setTransparency(alpha);
        setImage(label);
    }   
}