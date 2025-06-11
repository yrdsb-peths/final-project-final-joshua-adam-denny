import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Healthbar UI
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (June 2, 2025)
 */
public class HealthBar extends UI
{
    public static HealthBar _instance;
    private int  maxHealth = 100;
    private int barWidth = 110;
    private int barHeight = 25;
    private final int totalWidth  = 120;
    private final int totalHeight = 30;
    private CustomLabel livesLabel;
    
    private HealthBar()
    {
        GreenfootImage img = new GreenfootImage(totalWidth, totalHeight);
        setImage(img);
    }
    
    @Override
    protected void addedToWorld(World world)
    {
        GameWorld gw = (GameWorld) world;
        maxHealth = gw.getLives();
        livesLabel = new CustomLabel(maxHealth, 30);
        livesLabel.setFont(new greenfoot.Font(WorldManager.getFontName(), false,false,30));
        livesLabel.setFillColor(Color.BLACK);
        gw.addObject(livesLabel, getX()-25, getY()-2);
    }
    
    public static HealthBar getInstance()
    {
        if (_instance == null) {
            _instance = new HealthBar();
        }
        return _instance;
    }
    
    public void act()
    {
        GameWorld gw = (GameWorld)getWorld();
        if (gw == null) return;
        
        int currentLives = gw.getLives();
        if (maxHealth <= 0) return; // avoid divide‐by‐zero
        if (currentLives <= 15)
        {
            livesLabel.setFillColor(Color.RED);
        }
        else if (currentLives <= 25)
        {
            livesLabel.setFillColor(Color.WHITE);
        }
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
