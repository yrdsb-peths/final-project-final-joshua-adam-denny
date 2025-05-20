import greenfoot.*;
import java.util.*;

public class MyWorld extends World {
    private Player player;

    public MyWorld() {
        super(600, 400, 1, false); // disable auto repaint
        player = new Player();
        addObject(player, getWidth() / 2, getHeight() / 2); // Centered

        // Add some objects to see scrolling
        for (int i = 0; i < 10; i++) {
            addObject(new Tree(), 100 + i * 100, 200); // Replace Tree with your Actor
        }
    }

    // Move the world instead of the player
    public void scrollWorld(int dx, int dy) {
        List<Actor> all = getObjects(null);
        for (Actor a : all) {
            if (a != player) {
                a.setLocation(a.getX() + dx, a.getY() + dy);
            }
        }
    }
}
