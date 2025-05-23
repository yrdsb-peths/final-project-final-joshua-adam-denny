import greenfoot.*;

public class TowerPreview extends Actor {
    private String towerType;

    public TowerPreview(String towerType) {
        this.towerType = towerType;
        GreenfootImage img;
        
        try {
            img = new GreenfootImage(towerType + "_tower.png");
            img.scale(60, 60);
            if (img.getWidth() <= 1 || img.getHeight() <= 1) {
                throw new Exception("Image not found or invalid size");
            }
        } catch (Exception e) {
            // fallback preview
            img = new GreenfootImage(40, 40);
            img.setColor(Color.GREEN);
            img.fill();
        }
    
        
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
