import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Slider; UI element that allows the user to select a value within a range by dragging a knob along a track.
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (June 7, 2025)
 */
public class Slider extends UI {
    private int trackLength;
    private int trackHeight;
    private int knobRadius;
    
    private final int min = 0;
    private final int max = 100;
    private int value = 50;

    private boolean dragging = false;
    private int knobX;
    private int knobY; 
    private int alpha = 255;

    public Slider(int length, int height) {
        trackLength = length;
        trackHeight = height / 3; 
        knobRadius  = height / 3;
        setImage(new GreenfootImage(trackLength + knobRadius*2, height));
        knobY = height / 2;
        knobX = valueToX(value);
        updateVisual();
    }

    @Override
    public void act() {
        MouseInfo mi = Greenfoot.getMouseInfo();
        if (mi != null) {
            int localX = mi.getX() - getX() + getImage().getWidth()/2;
            int localY = mi.getY() - getY() + getImage().getHeight()/2;
            
            if (!dragging && Greenfoot.mousePressed(this)) {
                int dx = localX - knobX;
                int dy = localY - knobY;
                if (dx*dx + dy*dy <= knobRadius*knobRadius) {
                    dragging = true;
                }
            }
            
            if (dragging) {
                knobX = (int)Utils.clamp(localX, knobRadius, trackLength + knobRadius);
                value = (int)Utils.clamp(xToValue(knobX), min, max);
            }
            
            updateVisual();
            
            if (dragging && Greenfoot.mouseDragEnded(null)) {
                dragging = false;
            }
        }
    }
    
    private void updateVisual() {
        GreenfootImage img = getImage();
        img.clear();
        img.setTransparency(alpha);
        
        img.setColor(Color.GRAY);
        int y0 = knobY - trackHeight/2;
        img.fillRect(knobRadius, y0, trackLength, trackHeight);
        img.setColor(dragging ? Color.WHITE : Color.BLUE);
        
        
        img.fillOval(knobX - knobRadius, knobY - knobRadius, knobRadius*2, knobRadius*2);
        img.setColor(Color.BLACK);
        img.drawOval(knobX - knobRadius, knobY - knobRadius, knobRadius*2, knobRadius*2);
    }
    
    public void setTransparency(int alpha)
    {
        alpha = (int) Utils.clamp(alpha, 0, 255);
        this.alpha = alpha;
    }

    private int valueToX(int v) {
        double pct = (double)(v - min) / (max - min);
        return knobRadius + (int)(pct * trackLength);
    }

    private int xToValue(int x) {
        double pct = (double)(x - knobRadius) / trackLength;
        return (int)Math.round(min + pct * (max - min));
    }

    public int getValue() {
        return value;
    }

    public void setValue(int v) {
        value = (int)Utils.clamp(v, min, max);
        knobX = valueToX(value);
        updateVisual();
    }
}
