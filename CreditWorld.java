import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;
/**
 * PolyRender Test World
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (May 21 2025)
 */
public class CreditWorld extends World
{    
    
    private Camera camera;
    private PolyRender poly;
    private PolyRender poly2;
    private PolyRender roomPoly3;  
    private PolyRender creditTitlePoly;
    private PolyRender creditTextPoly;
    
    public CreditWorld()
    {    
        // Create a new world with 600x400 cells with a cell size of 1x1 pixels.
        //super(1280,720, 1); 
        super(1160,600, 1); 
        //super(256,144, 1); 
        Greenfoot.setSpeed(50);
        
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
        
        double[][][] room3 = new double[0][][];
        double[][][] monke = new double[0][][];
        double[][][] creditTitle = new double[0][][];
        double[][][] creditText = new double[0][][];
        try 
        {
            monke = ObjParser.parseObj("3dModels/monkey.obj", 100);
            room3 = ObjParser.parseObj("3dModels/room4.obj", 100);
            creditTitle = ObjParser.parseObj("3dModels/Credits.obj", 100);
            creditText = ObjParser.parseObj("3dModels/CreditsText.obj", 100);
        } 
        catch(Exception err) 
        {
            
        }
        
        
        
        
        camera = new Camera();
        roomPoly3 = new PolyRender(room3);
        poly2 = new PolyRender(cube);
        poly = new PolyRender(monke);
        creditTitlePoly = new PolyRender(creditTitle);
        creditTextPoly = new PolyRender(creditText);
        
        
        creditTitlePoly.setVersionOneRender_MinMaxLighting(100,255);
        creditTextPoly.setVersionOneRender_MinMaxLighting(200,255);
        
        
        addObject(camera, 0,0);
        addObject(roomPoly3, getWidth()/2,getHeight()/2);
        addObject(creditTitlePoly, getWidth()/2,getHeight()/2);
        addObject(creditTextPoly, getWidth()/2,getHeight()/2);
        addObject(poly2, getWidth()/2,getHeight()/2);
        addObject(poly, getWidth()/2,getHeight()/2);
    }
    
    double rotation = 0.0;
    double scale = 1.0;
    double position = 0.0;
    double position2 = 0.0;
    double gravity = 5;
    public void act()
    {
        
        
        if (Greenfoot.isKeyDown("escape")) {
            WorldManager.setWorld(new MainMenu());
        }
        
        rotation+= 0.5;
        position+= 0.05;
        position2+=0.0005;
        if (rotation > 360) rotation = 0;
        
        creditTitlePoly.setScale(-15);
        creditTextPoly.setScale(-15);
        
        creditTitlePoly.position(0,2500,3700);
        creditTextPoly.position(0,3500*Math.sin(position2) -500,3700);
        
        
        
        creditTitlePoly.rotate(
                    Math.sin(Math.toRadians(rotation))*0.25, 
                    Math.sin(Math.toRadians(rotation))*0.25, 
                    Math.sin(Math.toRadians(rotation))*0.25
                );
        
        poly.position(0,Math.sin(position)*30,500);
        poly.rotate(Math.toRadians(180), Math.toRadians(rotation), 0);        
        poly.setScale(scale);
        
        poly2.rotate(0, 0, 0);
        poly2.position(0,-300,500);
        
        roomPoly3.setScale(-20);
        roomPoly3.rotate(0,0,0);
        roomPoly3.position(0,-500,0);
        
    }
}
