import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;
/**
 * Particles for Greenfoot. (retired DO NOT USE, extremely laggy)
 * 
 * @author Denny ung
 * @version Version 1.0.0 (May 18th 2025)
 */
public class Particles extends Actor
{
    private double[] pos = new double[0]; 
    private double angleDEG = 0.0;
    private double speed = 0.0;
    private Color color = Color.RED;
    GreenfootImage renderScreen;
    
    /**
     * Constructor for Particles class.
     * Initializes the particle with position, angle, speed, and color.
     * 
     * @param pos The initial position of the particle as an array [x, y].
     * @param angle The angle of output in degrees, 360 will spray in a circle.
     * @param speed The initial speed of the particle.
     * @param color The color of the particle. using greenfoot weird ahh color system.
     */
    public Particles(double[] pos, double angle, double speed, Color color)
    {
        this.pos = pos;
        this.angleDEG = angle;
        this.speed = speed;
        this.color = color;
        
        renderScreen = new GreenfootImage(1160, 600);
        renderScreen.setColor(color);        
    }
    
    public void act()
    {
        double angleRAD = Math.toRadians(angleDEG);
        pos[0] += Math.cos(angleRAD) * speed;
        pos[1] += Math.sin(angleRAD) * speed;
        speed = Math.max(0, speed-0.1);
        
        if (speed == 0) 
        {
            getWorld().removeObject(this);
            return;
        }
        
        render();

    }
    
    /**
     * Renders the particle(s) on the screen
     * This method calculates the render points based on the position, angle, and speed,
     * and redraws a polygon representing the particle
     */
    public void render()
    {
        double angleRAD = Math.toRadians(angleDEG);
        double[] yRenderPoints = {
            pos[1] + Math.sin(angleRAD)*speed*3,
            pos[1] + Math.sin(angleRAD+Math.PI*0.5)*speed*0.5,
            pos[1] + Math.sin(angleRAD+Math.PI)*speed*3,
            pos[1] - Math.sin(angleRAD+Math.PI*0.5)*speed*0.5
        };
        
        double[] xRenderPoints = {
            pos[0] + Math.cos(angleRAD)*speed*3,
            pos[0] + Math.cos(angleRAD + Math.PI *0.5)*speed*0.5,
            pos[0] + Math.cos(angleRAD+Math.PI)*speed*3,
            pos[0] + Math.cos(angleRAD - Math.PI *0.5)*speed*0.5
        };
        
        
        int[] xRender = new int[xRenderPoints.length];
        int[] yRender = new int[yRenderPoints.length];

        
        for (int i = 0; i < xRenderPoints.length; i++) {
            xRender[i] = (int) Math.round(xRenderPoints[i]);
            yRender[i] = (int) Math.round(yRenderPoints[i]);
        }
        
        renderScreen.clear();
        renderScreen.setColor(color);
        renderScreen.fillPolygon(xRender, yRender, xRender.length);
        setImage(renderScreen);
    }
}
