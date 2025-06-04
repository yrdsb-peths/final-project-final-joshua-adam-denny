import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 *
 * 
 * @author Denny Ung
 * @version (a version number or a date)
 */
public class ImageActor extends Actor {
    public ImageActor(String filename) {
        setImage(new GreenfootImage(filename));
    }
    
    public ImageActor(GreenfootImage img ) {
        setImage(new GreenfootImage(img));
    }
    
    public ImageActor(int w, int h) {
        GreenfootImage img = new GreenfootImage(w, h);
        setImage(img);
    }
    
    /////////
    
    public int getWidth() {
        return getImage().getWidth();
    }

    public int getHeight() {
        return getImage().getHeight();
    }

    public void clear() {
        getImage().clear();
    }

    public void drawImage(GreenfootImage other, int x, int y) {
        getImage().drawImage(other, x, y);
    }

    public void drawLine(int x1, int y1, int x2, int y2) {
        getImage().drawLine(x1, y1, x2, y2);
    }

    public void drawRect(int x, int y, int width, int height) {
        getImage().drawRect(x, y, width, height);
    }

    public void drawPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        getImage().drawPolygon(xPoints, yPoints, nPoints);
    }

    public void drawString(String str, int x, int y) {
        getImage().drawString(str, x, y);
    }

    public void fill() {
        getImage().fill();
    }

    public void fillRect(int x, int y, int width, int height) {
        getImage().fillRect(x, y, width, height);
    }

    public void fillPolygon(int[] xPoints, int[] yPoints, int nPoints) {
        getImage().fillPolygon(xPoints, yPoints, nPoints);
    }

    public void mirrorHorizontally() {
        getImage().mirrorHorizontally();
    }

    public void mirrorVertically() {
        getImage().mirrorVertically();
    }

    public void rotate(int degrees) {
        getImage().rotate(degrees);
    }

    public void scale(int newWidth, int newHeight) {
        getImage().scale(newWidth, newHeight);
    }

    public void setColor(Color color) {
        getImage().setColor(color);
    }

    public void setColorAt(int x, int y, Color color) {
        getImage().setColorAt(x, y, color);
    }

    public void setFont(Font font) {
        getImage().setFont(font);
    }

    public void setTransparency(int t) {
        getImage().setTransparency(t);
    }

    public int getTransparency() {
        return getImage().getTransparency();
    }

    public Color getColor() {
        return getImage().getColor();
    }

    public Color getColorAt(int x, int y) {
        return getImage().getColorAt(x, y);
    }

    public Font getFont() {
        return getImage().getFont();
    }

    @Override
    public String toString() {
        // Provide a textual description of this Actor’s image
        return getImage().toString();
    }

    @Override
    public int hashCode() {
        return getImage().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) return true;
        if (!(obj instanceof ImageActor)) return false;
        ImageActor other = (ImageActor) obj;
        return this.getImage().equals(other.getImage());
    }
}
