import greenfoot.*;

public class Player extends Actor {
    public Player() {
        //("elephant.png"); // Use your own image file
    }

    public void act() {
        // Movement keys (no actual movement — world scrolls instead)
        MyWorld world = (MyWorld) getWorld();
        int dx = 0, dy = 0;

        if (Greenfoot.isKeyDown("right")) dx = -5;
        if (Greenfoot.isKeyDown("left")) dx = 5;
        if (Greenfoot.isKeyDown("up")) dy = 5;
        if (Greenfoot.isKeyDown("down")) dy = -5;

        world.scrollWorld(dx, dy);
    }
}
