import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;
import java.io.IOException;

/**
 * Sidebar UI 
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (May 30th, 2025)
 */
public class Sidebar extends UI
{
    public static Sidebar _instance = null;
    private Button button1;
    private PolyRender buttonIcon1;
    
    private Button button2;
    private PolyRender buttonIcon2;
    
    private Button button3;
    private PolyRender buttonIcon3;
    
    private Button button4;
    private PolyRender buttonIcon4;
    
    private Button button5;
    private PolyRender buttonIcon5;
    
    private boolean dragging1 = false;
    private boolean dragging2 = false;
    private boolean dragging3 = false;
    private boolean dragging4 = false;
    private boolean dragging5 = false;

    // Moved up/down images to private fields so the entire class can access them:
    private GreenfootImage up1, down1;
    private GreenfootImage up2, down2;
    private GreenfootImage up3, down3;
    private GreenfootImage up4, down4;
    private GreenfootImage up5, down5;
    
    private GreenfootImage buttonBaseImage;
    private GreenfootImage buttonBaseImagePressed; 

    public Sidebar()
    {
        GreenfootImage img = new GreenfootImage("ui/sidebar.png");
        setImage(img);
    }
    
    @Override
    protected void addedToWorld(World w) 
    {
        buttonBaseImage = new GreenfootImage("ui/button-sidebar.png");
        buttonBaseImagePressed = new GreenfootImage("ui/button-sidebar-pressed.png");
        int sbX = getX();
        int sbY = getY();
        
        // Button size (you can tweak these values as needed)
        int btnWidth  = 75;
        int btnHeight = 100;
        int spacing   = 10;  // vertical space between buttons
        
        // Total height occupied by 5 buttons + 4 spacings
        int totalHeight = 5 * btnHeight + 4 * spacing;
        // Compute the Y coordinate for the first (top) button:
        // center the stack around sbY, then move up by half of totalHeight and down by half a button
        int startY = sbY - totalHeight / 2 + btnHeight / 2;
        
        // --- Button 1: Basic ---
        {
            up1   = new GreenfootImage(buttonBaseImage);
            down1 = new GreenfootImage(buttonBaseImagePressed);
            GreenfootImage icon1 = new GreenfootImage("images/Basic_tower.png");
            icon1.scale(48, 48);
        
            up1.drawImage(icon1, (up1.getWidth() - icon1.getWidth()) / 2, (up1.getHeight() - icon1.getHeight()) / 2);
            down1.drawImage(icon1, (down1.getWidth() - icon1.getWidth()) / 2, (down1.getHeight() - icon1.getHeight()) / 2);
        
            button1 = new Button(true, new GreenfootImage[]{ up1, down1 }, btnWidth, btnHeight);
            int y1 = startY + 0 * (btnHeight + spacing);
            w.addObject(button1, sbX, y1);
        }
        
        // --- Button 2: Sniper ---
        {
            up2   = new GreenfootImage(buttonBaseImage);
            down2 = new GreenfootImage(buttonBaseImagePressed);
            GreenfootImage icon2 = new GreenfootImage("images/Sniper_tower.png");
            icon2.scale(48, 48);
        
            up2.drawImage(icon2, (up2.getWidth() - icon2.getWidth()) / 2, (up2.getHeight() - icon2.getHeight()) / 2);
            down2.drawImage(icon2, (down2.getWidth() - icon2.getWidth()) / 2, (down2.getHeight() - icon2.getHeight()) / 2);
        
            button2 = new Button(true, new GreenfootImage[]{ up2, down2 }, btnWidth, btnHeight);
            int y2 = startY + 1 * (btnHeight + spacing);
            w.addObject(button2, sbX, y2);
        }
        
        // --- Button 3: MachineGun ---
        {
            up3   = new GreenfootImage(buttonBaseImage);
            down3 = new GreenfootImage(buttonBaseImagePressed);
            GreenfootImage icon3 = new GreenfootImage("images/MachineGun_tower.png");
            icon3.scale(48, 48);
        
            up3.drawImage(icon3, (up3.getWidth() - icon3.getWidth()) / 2, (up3.getHeight() - icon3.getHeight()) / 2);
            down3.drawImage(icon3, (down3.getWidth() - icon3.getWidth()) / 2, (down3.getHeight() - icon3.getHeight()) / 2);
        
            button3 = new Button(true, new GreenfootImage[]{ up3, down3 }, btnWidth, btnHeight);
            int y3 = startY + 2 * (btnHeight + spacing);
            w.addObject(button3, sbX, y3);
        }
        
        
        double[][][] cube = new double[0][][];
        try {
            cube = ObjParser.parseObj("3dModels/cube.obj", 100);
        } catch(IOException balls) {
            
        }
        
        
        // --- Button 4: FlameThrower ---
        {
            buttonIcon4 = new PolyRender(cube, 600, 600, 150);
            buttonIcon4.position(0,0,500);
            buttonIcon4.setScale(1);
            buttonIcon4.rotate(Math.toRadians(35.0), Math.toRadians(45.0),0);
            buttonIcon4.act();
            up4   = new GreenfootImage(buttonBaseImage);
            down4 = new GreenfootImage(buttonBaseImagePressed);
            
            GreenfootImage icon4 = buttonIcon4.getGreenfootImage();
        
            up4.drawImage(icon4, (up4.getWidth() - icon4.getWidth()) / 2, (up4.getHeight() - icon4.getHeight()) / 2);
            down4.drawImage(icon4, (down4.getWidth() - icon4.getWidth()) / 2, (down4.getHeight() - icon4.getHeight()) / 2);
        
            button4 = new Button(true, new GreenfootImage[]{ up4, down4 }, btnWidth, btnHeight);
            int y4 = startY + 3 * (btnHeight + spacing);
            w.addObject(button4, sbX, y4);
        }
        
        // --- Button 5: Nuke ---
        {
            up5   = new GreenfootImage(buttonBaseImage);
            down5 = new GreenfootImage(buttonBaseImagePressed);
            
            GreenfootImage icon5 = new GreenfootImage("images/Nuke_tower.png");
            icon5.scale(48, 48);
        
            up5.drawImage(icon5, (up5.getWidth() - icon5.getWidth()) / 2, (up5.getHeight() - icon5.getHeight()) / 2);
            down5.drawImage(icon5, (down5.getWidth() - icon5.getWidth()) / 2, (down5.getHeight() - icon5.getHeight()) / 2);
        
            button5 = new Button(true, new GreenfootImage[]{ up5, down5 }, btnWidth, btnHeight);
            int y5 = startY + 4 * (btnHeight + spacing);
            w.addObject(button5, sbX, y5);
        }
    }
    
    public static Sidebar getInstance()
    {
        if (_instance == null) {
            _instance = new Sidebar();
        }
        return _instance;
    }
    
    
    

    
    double balls = 45;
    double balls2 = 0;  

    public void act()
    {
        GameWorld gw = (GameWorld) getWorld();
        balls +=0.5;
        balls2 +=0.005;
        
        
        buttonIcon4.act();
        buttonIcon4.rotate(Math.toRadians(35.0), Math.toRadians(balls),0);
        
        
        
        GreenfootImage icon4 = buttonIcon4.getGreenfootImage();
        //icon4.scale(48,48);
        up4 = new GreenfootImage(buttonBaseImage);
        down4 = new GreenfootImage(buttonBaseImagePressed); 
        
        int xOff = (up4.getWidth()  - icon4.getWidth()) / 2;
        int yOff = (up4.getHeight() - icon4.getHeight())/ 2;
        up4.drawImage(icon4,   xOff, yOff);
        down4.drawImage(icon4, xOff, yOff);
        
        button4.setButtons( new GreenfootImage[]{up4, down4} );
        
        
        // Button 1 - Basic Tower
        if (button1.isPressed()) {
            if (!dragging1) {
                dragging1 = true;
                gw.startDraggingTower("Basic");
            }
        } else if (dragging1 && Greenfoot.mouseClicked(null)) {
            dragging1 = false;
            button1.resetPressedState();
        }
    
        // Button 2 - Sniper Tower
        if (button2.isPressed()) {
            if (!dragging2) {
                dragging2 = true;
                gw.startDraggingTower("Sniper");
            }
        } else if (dragging2 && Greenfoot.mouseClicked(null)) {
            dragging2 = false;
            button2.resetPressedState();
        }
    
        // Button 3 - MachineGun Tower
        if (button3.isPressed()) {
            if (!dragging3) {
                dragging3 = true;
                gw.startDraggingTower("MachineGun");
            }
        } else if (dragging3 && Greenfoot.mouseClicked(null)) {
            dragging3 = false;
            button3.resetPressedState();
        }
    
        // Button 4 - FlameThrower Tower
        if (button4.isPressed()) {
            if (!dragging4) {
                dragging4 = true;
                gw.startDraggingTower("FlameThrower");
            }
        } else if (dragging4 && Greenfoot.mouseClicked(null)) {
            dragging4 = false;
            button4.resetPressedState();
        }
    
        // Button 5 - Nuke Tower
        if (button5.isPressed()) {
            if (!dragging5) {
                dragging5 = true;
                gw.startDraggingTower("Nuke");
            }
        } else if (dragging5 && Greenfoot.mouseClicked(null)) {
            dragging5 = false;
            button5.resetPressedState();
        }
    }
}
