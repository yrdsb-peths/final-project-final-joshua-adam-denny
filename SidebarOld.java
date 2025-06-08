import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;
import java.io.IOException;

/**
 * Sidebar UI 
 * 
 * @author Denny Ung and Joshua Stevens
 * @version Version 1.0.0 (May 30th, 2025)
 */
public class SidebarOld extends UI
{
    public static SidebarOld _instance = null;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    
    private PolyRender button1Icon;
    private PolyRender button2Icon;
    private PolyRender button3Icon;
    private PolyRender button4Icon;
    private PolyRender button5Icon;
    
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

    public SidebarOld()
    {
        setImage(new GreenfootImage("ui/sidebar.png"));
    }
    
    private void initalPoly(PolyRender poly)
    {
        poly.position(0,0,500);
        poly.setScale(-1);
        poly.rotate(Math.toRadians(35.0), Math.toRadians(45.0),0);
        poly.setRenderVersion(1);
        poly.act();
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
        
        double[][][] button1IconModel = new double[0][][];
        double[][][] button2IconModel = new double[0][][];
        double[][][] button3IconModel = new double[0][][];
        double[][][] button4IconModel = new double[0][][];
        double[][][] button5IconModel = new double[0][][];        
        
        try {
            button1IconModel = ObjParser.parseObj("3dModels/basicTower.obj", 100);
            button2IconModel = ObjParser.parseObj("3dModels/sniperTower.obj", 100);
            button3IconModel = ObjParser.parseObj("3dModels/machineGunTower.obj", 100);
            button4IconModel = ObjParser.parseObj("3dModels/flameThrowerTower.obj", 100);
            button5IconModel = ObjParser.parseObj("3dModels/nukeTower.obj", 100);
        } catch(IOException balls) {
            
        }
        
        button1Icon = new PolyRender(button1IconModel, 600, 600, 150);
        button1Icon.setVersionOneRender_MinMaxLighting(10.0,255.0);
        initalPoly(button1Icon);
        
        button2Icon = new PolyRender(button2IconModel, 600, 600, 150);
        initalPoly(button2Icon);
        
        button3Icon = new PolyRender(button3IconModel, 600, 600, 150);
        initalPoly(button3Icon);
        
        button4Icon = new PolyRender(button4IconModel, 600, 600, 150);
        initalPoly(button4Icon);
        
        button5Icon = new PolyRender(button5IconModel, 600, 600, 150);
        initalPoly(button5Icon);
        
        // --- Button 1: Basic ---
        up1   = new GreenfootImage(buttonBaseImage);
        down1 = new GreenfootImage(buttonBaseImagePressed);
        GreenfootImage icon1 = button1Icon.getGreenfootImage();
        up1.drawImage(icon1,(up1.getWidth() - icon1.getWidth()) / 2,(up1.getHeight() - icon1.getHeight()) / 2);
        down1.drawImage(icon1,(down1.getWidth() - icon1.getWidth()) / 2, (down1.getHeight() - icon1.getHeight()) / 2);
        button1 = new Button(true, new GreenfootImage[]{ up1, down1 }, btnWidth, btnHeight);
        int y1 = startY + 0 * (btnHeight + spacing);
        w.addObject(button1, sbX, y1);
        
        
        // --- Button 2: Sniper ---
        up2   = new GreenfootImage(buttonBaseImage);
        down2 = new GreenfootImage(buttonBaseImagePressed);
        GreenfootImage icon2 = button2Icon.getGreenfootImage();
        up2.drawImage(icon2, (up2.getWidth() - icon2.getWidth()) / 2, (up2.getHeight() - icon2.getHeight()) / 2);
        down2.drawImage(icon2, (down2.getWidth() - icon2.getWidth()) / 2, (down2.getHeight() - icon2.getHeight()) / 2);
        button2 = new Button(true, new GreenfootImage[]{ up2, down2 }, btnWidth, btnHeight);
        int y2 = startY + 1 * (btnHeight + spacing);
        w.addObject(button2, sbX, y2);
        
        
        // --- Button 3: MachineGun ---
        up3   = new GreenfootImage(buttonBaseImage);
        down3 = new GreenfootImage(buttonBaseImagePressed);
        GreenfootImage icon3 = button3Icon.getGreenfootImage();
        up3.drawImage(icon3, (up3.getWidth() - icon3.getWidth()) / 2, (up3.getHeight() - icon3.getHeight()) / 2);
        down3.drawImage(icon3, (down3.getWidth() - icon3.getWidth()) / 2, (down3.getHeight() - icon3.getHeight()) / 2);
        button3 = new Button(true, new GreenfootImage[]{ up3, down3 }, btnWidth, btnHeight);
        int y3 = startY + 2 * (btnHeight + spacing);
        w.addObject(button3, sbX, y3);
        
        
        
        
        
        // --- Button 4: FlameThrower ---
        up4   = new GreenfootImage(buttonBaseImage);
        down4 = new GreenfootImage(buttonBaseImagePressed);
        GreenfootImage icon4 = button4Icon.getGreenfootImage();
        up4.drawImage(icon4, (up4.getWidth() - icon4.getWidth()) / 2, (up4.getHeight() - icon4.getHeight()) / 2);
        down4.drawImage(icon4, (down4.getWidth() - icon4.getWidth()) / 2, (down4.getHeight() - icon4.getHeight()) / 2);
        button4 = new Button(true, new GreenfootImage[]{ up4, down4 }, btnWidth, btnHeight);
        int y4 = startY + 3 * (btnHeight + spacing);
        w.addObject(button4, sbX, y4);
        
        
        // --- Button 5: Nuke ---
        up5   = new GreenfootImage(buttonBaseImage);
        down5 = new GreenfootImage(buttonBaseImagePressed);
        GreenfootImage icon5 = button5Icon.getGreenfootImage();
        up5.drawImage(icon5, (up5.getWidth() - icon5.getWidth()) / 2, (up5.getHeight() - icon5.getHeight()) / 2);
        down5.drawImage(icon5, (down5.getWidth() - icon5.getWidth()) / 2, (down5.getHeight() - icon5.getHeight()) / 2);
        button5 = new Button(true, new GreenfootImage[]{ up5, down5 }, btnWidth, btnHeight);
        int y5 = startY + 4 * (btnHeight + spacing);
        w.addObject(button5, sbX, y5);
        
    }
    
    public static SidebarOld getInstance()
    {
        if (_instance == null) {
            _instance = new SidebarOld();
        }
        return _instance;
    }
    
    
    

    
    private double balls = 45.0;

    public void act()
    {
        GameWorld gw = (GameWorld) getWorld();
        balls +=0.5;
        
        
        button5Icon.rotate(Math.toRadians(35.0), Math.toRadians(balls),0);
        button5Icon.act();
        button4Icon.rotate(Math.toRadians(35.0), Math.toRadians(balls),0);
        button4Icon.act();
        button3Icon.rotate(Math.toRadians(35.0), Math.toRadians(balls),0);
        button3Icon.act();
        button2Icon.rotate(Math.toRadians(35.0), Math.toRadians(balls),0);
        button2Icon.act();
        button1Icon.rotate(Math.toRadians(35.0), Math.toRadians(balls),0);
        button1Icon.act();
        
        
        
        up1 = new GreenfootImage(buttonBaseImage);
        down1 = new GreenfootImage(buttonBaseImagePressed);
        GreenfootImage icon1 = button1Icon.getGreenfootImage();
        up1.drawImage(icon1,(up1.getWidth() - icon1.getWidth()) / 2,(up1.getHeight() - icon1.getHeight()) / 2);
        down1.drawImage(icon1,(down1.getWidth() - icon1.getWidth()) / 2, (down1.getHeight() - icon1.getHeight()) / 2);
        button1.setButtons( new GreenfootImage[]{up1, down1} );  
        
        
        // --- Button 2: Sniper ---
        up2 = new GreenfootImage(buttonBaseImage);
        down2 = new GreenfootImage(buttonBaseImagePressed);
        GreenfootImage icon2 = button2Icon.getGreenfootImage();
        up2.drawImage(icon2, (up2.getWidth() - icon2.getWidth()) / 2, (up2.getHeight() - icon2.getHeight()) / 2);
        down2.drawImage(icon2, (down2.getWidth() - icon2.getWidth()) / 2, (down2.getHeight() - icon2.getHeight()) / 2);
        button2.setButtons( new GreenfootImage[]{up2, down2} );  
        
        
        // --- Button 3: MachineGun ---
        up3 = new GreenfootImage(buttonBaseImage);
        down3 = new GreenfootImage(buttonBaseImagePressed);
        GreenfootImage icon3 = button3Icon.getGreenfootImage();
        up3.drawImage(icon3, (up3.getWidth() - icon3.getWidth()) / 2, (up3.getHeight() - icon3.getHeight()) / 2);
        down3.drawImage(icon3, (down3.getWidth() - icon3.getWidth()) / 2, (down3.getHeight() - icon3.getHeight()) / 2);
        button3.setButtons( new GreenfootImage[]{up3, down3} );  
        
        
        up4 = new GreenfootImage(buttonBaseImage);
        down4 = new GreenfootImage(buttonBaseImagePressed);
        GreenfootImage icon4 = button4Icon.getGreenfootImage();
        up4.drawImage(icon4, (up4.getWidth() - icon4.getWidth()) / 2, (up4.getHeight() - icon4.getHeight()) / 2);
        down4.drawImage(icon4, (down4.getWidth() - icon4.getWidth()) / 2, (down4.getHeight() - icon4.getHeight()) / 2);
        button4.setButtons( new GreenfootImage[]{up4, down4} );  
        
        
        up5   = new GreenfootImage(buttonBaseImage);
        down5 = new GreenfootImage(buttonBaseImagePressed);
        GreenfootImage icon5 = button5Icon.getGreenfootImage();
        up5.drawImage(icon5, (up5.getWidth() - icon5.getWidth()) / 2, (up5.getHeight() - icon5.getHeight()) / 2);
        down5.drawImage(icon5, (down5.getWidth() - icon5.getWidth()) / 2, (down5.getHeight() - icon5.getHeight()) / 2);
        button5.setButtons( new GreenfootImage[]{up5, down5} );        
        
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