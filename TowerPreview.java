import greenfoot.*;

public class TowerPreview extends Actor {
    private String towerType;

    public TowerPreview(String towerType) {
        this.towerType = towerType;
        GreenfootImage img;
        
        try {
            img = new GreenfootImage(towerType + "_preview.png");
            if (img.getWidth() <= 1 || img.getHeight() <= 1) {
                throw new Exception("Image not found or invalid size");
            }
        } catch (Exception e) {
            // fallback preview
            img = new GreenfootImage(40, 40);
            img.setColor(Color.GREEN);
            img.fill();
        }
    
        img.scale(50, 50);
        img.setTransparency(100); // semi-transparent
        setImage(img);
    }


    public void act() {
        MouseInfo mouse = Greenfoot.getMouseInfo();
        if (mouse != null) {
            setLocation(mouse.getX(), mouse.getY());
        }
    }

    public String getTowerType() {
        return towerType;
    }
}
