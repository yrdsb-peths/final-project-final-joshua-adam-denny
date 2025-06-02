import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

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
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    
    public Sidebar()
    {
        GreenfootImage img = new GreenfootImage("ui/sidebar.png");
        setImage(img);
        
        
    }
    
    @Override
    protected void addedToWorld(World w) 
    {
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
        
        // --- Button 1 ---
        {
            GreenfootImage up1   = new GreenfootImage("ui/button-sidebar.png");
            GreenfootImage down1 = new GreenfootImage("ui/button-sidebar-pressed.png");
            button1 = new Button(true, new GreenfootImage[]{ up1, down1 }, btnWidth, btnHeight);
            int y1 = startY + 0 * (btnHeight + spacing);
            w.addObject(button1, sbX, y1);
        }
        
        // --- Button 2 ---
        {
            GreenfootImage up2   = new GreenfootImage("ui/button-sidebar.png");
            GreenfootImage down2 = new GreenfootImage("ui/button-sidebar-pressed.png");
            button2 = new Button(true, new GreenfootImage[]{ up2, down2 }, btnWidth, btnHeight);
            int y2 = startY + 1 * (btnHeight + spacing);
            w.addObject(button2, sbX, y2);
        }
        
        // --- Button 3 ---
        {
            GreenfootImage up3   = new GreenfootImage("ui/button-sidebar.png");
            GreenfootImage down3 = new GreenfootImage("ui/button-sidebar-pressed.png");
            button3 = new Button(true, new GreenfootImage[]{ up3, down3 }, btnWidth, btnHeight);
            int y3 = startY + 2 * (btnHeight + spacing);
            w.addObject(button3, sbX, y3);
        }
        
        // --- Button 4 ---
        {
            GreenfootImage up4   = new GreenfootImage("ui/button-sidebar.png");
            GreenfootImage down4 = new GreenfootImage("ui/button-sidebar-pressed.png");
            button4 = new Button(true, new GreenfootImage[]{ up4, down4 }, btnWidth, btnHeight);
            int y4 = startY + 3 * (btnHeight + spacing);
            w.addObject(button4, sbX, y4);
        }
        
        // --- Button 5 ---
        {
            GreenfootImage up5   = new GreenfootImage("ui/button-sidebar.png");
            GreenfootImage down5 = new GreenfootImage("ui/button-sidebar-pressed.png");
            button5 = new Button(true, new GreenfootImage[]{ up5, down5 }, btnWidth, btnHeight);
            int y5 = startY + 4 * (btnHeight + spacing);
            w.addObject(button5, sbX, y5);
        }
        
        double[] vertex_1 = { -125, -125, -125 };
        double[] vertex_2 = { 125, -125, -125 };
        double[] vertex_3 = { 125, 125, -125 };
        double[] vertex_4 = { -125, 125, -125 };
        
        double[] vertex_5 = { -125, -125, 125 };
        double[] vertex_6 = { 125, -125, 125 };
        double[] vertex_7 = { 125, 125, 125 };
        double[] vertex_8 = { -125, 125, 125 };
        
        double[][] poly_1 = {vertex_1,vertex_2,vertex_3,vertex_4}; // Front Facing
        double[][] poly_2 = {vertex_5,vertex_1,vertex_4,vertex_8}; // Left Facing
        double[][] poly_3 = {vertex_2,vertex_6,vertex_7,vertex_3}; // Right Facing
        double[][] poly_4 = {vertex_5,vertex_6,vertex_2,vertex_1}; // Top Facng
        double[][] poly_5 = {vertex_4,vertex_3,vertex_7,vertex_8}; // Bottom Facing
        double[][] poly_6 = {vertex_6,vertex_5,vertex_8, vertex_7}; // Back Facing
        
        double[][][] cube = {poly_1, poly_2, poly_3, poly_4, poly_5, poly_6};
        
        PolyRender poly2 = new PolyRender(cube, 1000, 1000);
        int halfWidth = getX();
        int halfHeight = getY();
            
        float rotation = 0;
        float position = 0;
        
        rotation+= 0.5;
        position+= 0.05;
        
        if (rotation > 360) rotation = 0;
        
        poly2.rotate(Math.toRadians(180), Math.toRadians(rotation), 0);

        poly2.position(0,0,200);
        poly2.setScale(0.01);
        
    }
    
    

    public static Sidebar getInstance()
    {
        if (_instance == null) {
            _instance = new Sidebar();
        }
        return _instance;
    }

    public void act()
    {
        GameWorld gw = (GameWorld) getWorld();
        
        if (button1.isPressed()) {
            gw.startDraggingTower("Basic");
            
        } 
        else if (button2.isPressed()) {
            gw.startDraggingTower("Sniper");
            
        } 
        else if (button3.isPressed()) {
            gw.startDraggingTower("MachineGun");
            
        } 
        else if (button4.isPressed()) {
            gw.startDraggingTower("FlameThrower");
            
        } 
        else if (button5.isPressed()) {
            gw.startDraggingTower("Nuke");
            
        }
    }
}
