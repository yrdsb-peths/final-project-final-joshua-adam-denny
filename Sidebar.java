import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;
import java.io.IOException;

/**
 * Sidebar UI with tower price labels
 * 
 * @author Denny Ung
 * @version Updated June 4, 2025
 */
public class Sidebar extends UI
{
    public static Sidebar _instance = null;

    private Button button1, button2, button3, button4, button5;
    private GreenfootImage up1, down1, up2, down2, up3, down3, up4, down4, up5, down5;
    private GreenfootImage icon2, icon3, icon4, icon5;

    private boolean dragging1 = false, dragging2 = false, dragging3 = false, dragging4 = false, dragging5 = false;

    // Tower prices
    private final int price1 = 50;
    private final int price2 = 300;
    private final int price3 = 750;
    private final int price4 = 4500;
    private final int price5 = 10000;
    
    private GreenfootImage buttonBaseImage = new GreenfootImage("ui/button-sidebar.png");
    private GreenfootImage buttonBaseImagePressed = new GreenfootImage("ui/button-sidebar-pressed.png");
    
    private PolyRender button1Icon;
    private PolyRender button2Icon;
    private PolyRender button3Icon;
    private PolyRender button4Icon;
    private PolyRender button5Icon;


    public Sidebar()
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
        int sbX = getX();
        int sbY = getY();
        int btnWidth = 75;
        int btnHeight = 100;
        int spacing = 10;
        int totalHeight = 5 * btnHeight + 4 * spacing;
        int startY = sbY - totalHeight / 2 + btnHeight / 2;
        int currentMoney = ((GameWorld) w).getMoney();
        double[][][] button1IconModel = new double[0][][];
        double[][][] button2IconModel = new double[0][][];
        double[][][] button3IconModel = new double[0][][];
        double[][][] button4IconModel = new double[0][][];
        double[][][] button5IconModel = new double[0][][];

        icon2 = new GreenfootImage("images/Sniper_tower.png");     icon2.scale(48, 48);
        icon3 = new GreenfootImage("images/MachineGun_tower.png"); icon3.scale(50, 35);
        icon4 = new GreenfootImage("images/FlameThrower_tower.png"); icon4.scale(48, 48);
        icon5 = new GreenfootImage("images/Nuke_tower.png");       icon5.scale(48, 48);

        
        
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
        
        
        
        
        up1 = createTowerButtonImage(buttonBaseImage, button1Icon.getGreenfootImage(), price1, currentMoney);
        down1 = createTowerButtonImage(buttonBaseImagePressed, button1Icon.getGreenfootImage(), price1, currentMoney);
        button1 = new Button(true, new GreenfootImage[]{ up1, down1 }, btnWidth, btnHeight);
        w.addObject(button1, sbX, startY + 0 * (btnHeight + spacing));

        up2 = createTowerButtonImage(buttonBaseImage, button2Icon.getGreenfootImage(), price2, currentMoney);
        down2 = createTowerButtonImage(buttonBaseImagePressed, button2Icon.getGreenfootImage(), price2, currentMoney);
        button2 = new Button(true, new GreenfootImage[]{ up2, down2 }, btnWidth, btnHeight);
        w.addObject(button2, sbX, startY + 1 * (btnHeight + spacing));

        up3 = createTowerButtonImage(buttonBaseImage, button3Icon.getGreenfootImage(), price3, currentMoney);
        down3 = createTowerButtonImage(buttonBaseImagePressed, button3Icon.getGreenfootImage(), price3, currentMoney);
        button3 = new Button(true, new GreenfootImage[]{ up3, down3 }, btnWidth, btnHeight);
        w.addObject(button3, sbX, startY + 2 * (btnHeight + spacing));
        
        
        up4 = createTowerButtonImage(buttonBaseImage, button4Icon.getGreenfootImage(), price4, currentMoney);
        down4 = createTowerButtonImage(buttonBaseImagePressed, button4Icon.getGreenfootImage(), price4, currentMoney);
        button4 = new Button(true, new GreenfootImage[]{ up4, down4 }, btnWidth, btnHeight);
        w.addObject(button4, sbX, startY + 3 * (btnHeight + spacing));

        up5 = createTowerButtonImage(buttonBaseImage, button5Icon.getGreenfootImage(), price5, currentMoney);
        down5 = createTowerButtonImage(buttonBaseImagePressed, icon5, price5, currentMoney);
        button5 = new Button(true, new GreenfootImage[]{ up5, down5 }, btnWidth, btnHeight);
        w.addObject(button5, sbX, startY + 4 * (btnHeight + spacing));
    }

    private GreenfootImage createTowerButtonImage(GreenfootImage buttonImage, GreenfootImage towerIcon, int price, int currentMoney) {
        GreenfootImage base = new GreenfootImage(buttonImage);
        int iconX = (base.getWidth() - towerIcon.getWidth()) / 2;
        int iconY = (base.getHeight() - towerIcon.getHeight())/ 2 - 10;
        base.drawImage(towerIcon, iconX, iconY);

        String priceText = "$" + price;
        greenfoot.Color color = (currentMoney >= price) ? greenfoot.Color.WHITE : greenfoot.Color.RED;
        greenfoot.Color transparent = new greenfoot.Color(0, 0, 0, 0);
        GreenfootImage label = new GreenfootImage(priceText, 16, color, transparent);
        int labelX = (base.getWidth() - label.getWidth()) / 2;
        int labelY = base.getHeight() - label.getHeight() - 4;
        base.drawImage(label, labelX, labelY);

        return base;
    }

    public static Sidebar getInstance()
    {
        if (_instance == null) {
            _instance = new Sidebar();
        }
        return _instance;
    }
    private double balls = 45.0;
    public void act()
    {
        GameWorld gw = (GameWorld) getWorld();
        if (gw == null) return;

        int money = gw.getMoney();

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
        
        // Redraw button images with icon + updated price
        redrawButtonImage(up1, buttonBaseImage, button1Icon.getGreenfootImage(), price1, money);
        redrawButtonImage(down1, buttonBaseImagePressed, button1Icon.getGreenfootImage(), price1, money);
        redrawButtonImage(up2, buttonBaseImage, button2Icon.getGreenfootImage(), price2, money);
        redrawButtonImage(down2, buttonBaseImagePressed, button2Icon.getGreenfootImage(), price2, money);
        redrawButtonImage(up3, buttonBaseImage, button3Icon.getGreenfootImage(), price3, money);
        redrawButtonImage(down3, buttonBaseImagePressed, button3Icon.getGreenfootImage(), price3, money);
        redrawButtonImage(up4, buttonBaseImage, button4Icon.getGreenfootImage(), price4, money);
        redrawButtonImage(down4, buttonBaseImagePressed, button4Icon.getGreenfootImage(), price4, money);
        redrawButtonImage(up5, buttonBaseImage, button5Icon.getGreenfootImage(), price5, money);
        redrawButtonImage(down5, buttonBaseImagePressed, button5Icon.getGreenfootImage(), price5, money);

        handleDrag(button1, "Basic", price1, gw, 1);
        handleDrag(button2, "Sniper", price2, gw, 2);
        handleDrag(button3, "MachineGun", price3, gw, 3);
        handleDrag(button4, "FlameThrower", price4, gw, 4);
        handleDrag(button5, "Nuke", price5, gw, 5);
    }

    private void redrawButtonImage(GreenfootImage target, GreenfootImage buttonImage, GreenfootImage towerIcon, int price, int money)
    {
        GreenfootImage base = new GreenfootImage(buttonImage);
        GreenfootImage iconCopy = new GreenfootImage(towerIcon);
        int iconX = (base.getWidth() - iconCopy.getWidth()) / 2;
        int iconY = (base.getHeight() - towerIcon.getHeight())/ 2 - 10;
        base.drawImage(iconCopy, iconX, iconY);

        greenfoot.Color color = (money >= price) ? greenfoot.Color.WHITE : greenfoot.Color.RED;
        GreenfootImage label = new GreenfootImage("$" + price, 16, color, new greenfoot.Color(0, 0, 0, 0));
        int labelX = (base.getWidth() - label.getWidth()) / 2;
        int labelY = base.getHeight() - label.getHeight() - 4;
        base.drawImage(label, labelX, labelY);

        target.clear();
        target.drawImage(base, 0, 0);
    }

    private void handleDrag(Button button, String towerName, int cost, GameWorld gw, int id)
    {
        boolean dragging = false;
        switch (id) {
            case 1: dragging = dragging1; break;
            case 2: dragging = dragging2; break;
            case 3: dragging = dragging3; break;
            case 4: dragging = dragging4; break;
            case 5: dragging = dragging5; break;
        }

        if (button.isPressed()) {
            if (!dragging && gw.getMoney() >= cost) {
                switch (id) {
                    case 1: dragging1 = true; break;
                    case 2: dragging2 = true; break;
                    case 3: dragging3 = true; break;
                    case 4: dragging4 = true; break;
                    case 5: dragging5 = true; break;
                }
                gw.startDraggingTower(towerName);
            }
        } else if (dragging && Greenfoot.mouseClicked(null)) {
            switch (id) {
                case 1: dragging1 = false; button.resetPressedState(); break;
                case 2: dragging2 = false; button.resetPressedState(); break;
                case 3: dragging3 = false; button.resetPressedState(); break;
                case 4: dragging4 = false; button.resetPressedState(); break;
                case 5: dragging5 = false; button.resetPressedState(); break;
            }
        }
    }
}
