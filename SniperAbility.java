import greenfoot.*;
import java.awt.Font;
public class SniperAbility extends Actor {
    private int cooldown = 3600; // 10 seconds at 60 FPS
    private int timer = 0;
    private boolean used = false;

    private GreenfootImage iconImage;
    private GreenfootImage textImage;

    public SniperAbility() {
        iconImage = new GreenfootImage("Icon.png");
        iconImage.scale(40, 40);
        setImage(iconImage);
    }

    public void act() {
        if (used) {
            timer--;
            if (timer <= 0) {
                used = false;
                timer = 0;
            }
            updateCooldownText();
        } else {
            // Reset the image to normal icon if ready
            setImage(iconImage);
        }

        checkClick();
    }

    private void checkClick() {
        if (Greenfoot.mouseClicked(this) && !used) {
            GameWorld world = (GameWorld) getWorld();
            world.activateSniperBoost();
            used = true;
            timer = cooldown;
        }
    }

    private void updateCooldownText() {
        GreenfootImage combined = new GreenfootImage(iconImage.getWidth(), iconImage.getHeight() + 20);
        combined.setColor(Color.BLACK);
        
        String text = (timer / 60 + 1) + "s";
        combined.drawImage(iconImage, 0, 20);
        combined.drawString(text, (combined.getWidth() - (text.length() * 7)) / 2, 14);

        setImage(combined);
    }
}