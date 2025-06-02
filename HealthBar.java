import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Write a description of class HealthBar here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class HealthBar extends UI
{
    public static HealthBar _instance;
    private int  maxHealth;
    private int barWidth = 110;
    private int barHeight = 25;
    private int offsetY = 75;
    private final int totalWidth  = 120;
    private final int totalHeight = 30;
    private Label livesLabel;
    
    private HealthBar()
    {
        
        GreenfootImage img = new GreenfootImage(totalWidth, totalHeight);
        setImage(img);
    
    }
    
    public void act()
    {
        redrawBar();
    }
    
    @Override
    protected void addedToWorld(World world)
    {
        GameWorld gameWorld = (GameWorld) world;
        maxHealth = gameWorld.getLives();
        livesLabel = new Label(maxHealth, 30);
        gameWorld.addObject(livesLabel, getX(), getY());
    }
    
    public static HealthBar getInstance()
    {
        if (_instance == null) {
            _instance = new HealthBar();
        }
        return _instance;
    }
    
    private void redrawBar() {
        GameWorld gw = (GameWorld)getWorld();
        if (gw == null) return;
        
        int currentLives = gw.getLives();
        if (maxHealth <= 0) return; // avoid divide‐by‐zero
        
        livesLabel.setValue(currentLives);
        
        // start with a fresh black background
        GreenfootImage img = new GreenfootImage(totalWidth, totalHeight);
        img.setColor(Color.BLACK);
        img.fillRect(0, 0, totalWidth, totalHeight);

        // compute how wide the green “fill” should be
        double ratio = (double)currentLives / maxHealth;
        int fillWidth = (int)(ratio * barWidth);
        if (fillWidth < 0) fillWidth = 0;
        if (fillWidth > barWidth) fillWidth = barWidth;

        // draw the green portion centered horizontally
        int xOffset = (totalWidth - barWidth) / 2;
        int yOffset = (totalHeight - barHeight) / 2;
        img.setColor(Color.GREEN);
        img.fillRect(xOffset, yOffset, fillWidth, barHeight);

        setImage(img);
    }
}
