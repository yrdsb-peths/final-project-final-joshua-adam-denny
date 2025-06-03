import greenfoot.*;

public class BasicEnemy extends Enemy {

    public BasicEnemy(int speed, int health) {
        super(speed, health);
        GreenfootImage img = new GreenfootImage("man.png");
        img.scale(37, 75);
        setBaseImage(img); // sets baseImage and sets actor image
    }
}
