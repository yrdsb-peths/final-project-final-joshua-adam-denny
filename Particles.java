import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math;
/**
 * Particles for Greenfoot.
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
    
    public Particles(double[] pos, double angle, double speed, Color color)
    {
        this.pos = pos;
        this.angleDEG = angle;
        this.speed = speed;
        this.color = color;
        
        renderScreen = new GreenfootImage(640, 359);
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
        renderScreen.fillPolygon(xRender, yRender, xRender.length);
        setImage(renderScreen);
    }
}
