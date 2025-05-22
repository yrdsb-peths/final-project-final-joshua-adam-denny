import greenfoot.*;

public class BasicEnemy extends Enemy {

    public BasicEnemy(int speed, int health) {
        super(speed, health+1000);
        GreenfootImage img = new GreenfootImage("man.png");
        img.scale(80, 80);
        setBaseImage(img); // sets baseImage and sets actor image
    }
}
