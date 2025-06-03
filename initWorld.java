import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.io.IOException;
import java.lang.Math;
/**
 * Init world
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (June 3, 2025)
 */
public class initWorld extends World
{
    
    private SimpleTimer dt = new SimpleTimer();
    private int frame = 0;
    
    
    // [frame][Type(0 = position, 1 = rotation)][0,0,0];
    private double[][][] qwe = new double[][][]{
        {},
        {},
    
    
    
    };
    
    private PolyRender polyText;
    public initWorld()
    {    
        super(1160, 600, 1);
        dt.mark();
        
        
        
        double[][][] text = new double[0][][];
        try {
            text = ObjParser.parseObj("3dModels/text.obj", 100);
        } catch(IOException balls) {
            
        }
        
        polyText = new PolyRender(text);
        polyText.position(0,0,250);
        polyText.rotate(Math.toRadians(35.0),Math.toRadians(35.0),Math.toRadians(35.0));
        polyText.setRenderVersion(1);
        polyText.setVersionOneRender_MinMaxLighting(50.0,150.0);
        addObject(polyText, getWidth()/2,getHeight()/2);
    }
    
    double rotate1 = 0;
    double rotate2 = 0;
    double rotate3 = 0;
    public void act()
    {
        
    
        if (dt.millisElapsed() > 17)
        {
            dt.mark();
            frame++;
        }
        
        rotate1 += 0.5;
        rotate2 += 0.2;
        rotate3 += 0.8;
        
        polyText.rotate(Math.toRadians(rotate1),Math.toRadians(rotate2),Math.toRadians(rotate3));
    
    
    }
}
