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
    private int frameCounter = 0; 
    private int currentKeyframe = 0; 
    private GreenfootImage original;
    private GreenfootImage blurred;
    private PolyRender polyText;
    
    
    // [frame][Type(0 = position, 1 = rotation)][0,0,0];
    private double[][][] qwe = new double[][][]{
        // Keyframe 0:
        {
            {   0.0,   0.0, 250.0 },    // type=0 → position (x, y, z)
            {  35.0,  35.0,  35.0 }     // type=1 → rotation (rotX, rotY, rotZ) in degrees
        },
        // Keyframe 1:
        {
            { 100.0,  50.0, 300.0 },    // move to (100, 50, 300)
            {  60.0, 120.0,   0.0 }     // rotate to (60°, 120°, 0°)
        },
        // Keyframe 2:
        {
            {  -50.0, 100.0, 200.0 },
            {   0.0, 180.0,  90.0 }
        }
        // …add as many keyframes as you like…
    };
    
    private void applyKeyframe(int k)
    {
        // Guard in case k is out of bounds
        if (k < 0 || k >= qwe.length) {
            return;
        }
        // type=0: position
        double[] pos = qwe[k][0];
        polyText.position(pos[0], pos[1], pos[2]);

        // type=1: rotation (in degrees → convert to radians if needed)
        double[] rot = qwe[k][1];
        // If your rotate(...) expects radians, convert here:
        double rx = Math.toRadians(rot[0]);
        double ry = Math.toRadians(rot[1]);
        double rz = Math.toRadians(rot[2]);
        polyText.rotate(rx, ry, rz);
    }
    
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
        
        original = new GreenfootImage("ui/titlescreen.png");

        // Apply a box‑blur with radius=2 (i.e. a 5×5 neighborhood)
        blurred = BlurHelper.blur(original, 25);

        // Set the blurred image as the world’s background
        setBackground(blurred);
    }
    
    double rotate1 = 0;
    double rotate2 = 0;
    double rotate3 = 0;
    public void act()
    {
        
    
        if (dt.millisElapsed() > 17)
        {
            dt.mark();
            frameCounter++;
            if (currentKeyframe >= qwe.length) {
                currentKeyframe = 0;  // wrap around
            }
            applyKeyframe(frameCounter);
        }
        
        rotate1 += 0.5;
        rotate2 += 0.2;
        rotate3 += 0.8;
        
        polyText.rotate(Math.toRadians(rotate1),Math.toRadians(rotate2),Math.toRadians(rotate3));
    
    
    }
}
