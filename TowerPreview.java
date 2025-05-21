import greenfoot.*;

public class TowerPreview extends Actor {
    private String towerType;

    public TowerPreview(String towerType) {
        this.towerType = towerType;
        GreenfootImage img = new GreenfootImage(towerType + "_preview.png");
        img.scale(50, 50);
        if (img == null || img.getWidth() == 0) {
            img = new GreenfootImage(40, 40); // fallback square if image missing
            img.setColor(Color.GREEN);
            img.fill();
        }
        setImage(img);
        getImage().setTransparency(100); // semi-transparent
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
