import greenfoot.*;

public class MenuButton extends Actor {
    private String label;

    public MenuButton(String label) {
        this.label = label;
        GreenfootImage img = new GreenfootImage(100, 30);
        img.setColor(Color.WHITE);
        img.fill();
        img.setColor(Color.BLACK);
        img.drawRect(0, 0, 99, 29);
        img.drawString(label, 10, 20);
        setImage(img);
    }

    public String getLabel() {
        return label;
    }
}
