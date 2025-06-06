import greenfoot.*;
import java.util.*;

/**
 * Particle Manager, replacement to the Particles Class. 
 * Manages Particles.
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (June 3, 2025)
 */
public class ParticleManager extends Actor 
{
    private static ParticleManager _instance;
    private static class Particle
    {
        double x;
        double y;
        double angleRad;
        double speed;
        Color color;
        
        Particle(double x, double y,double angleDeg, double speed, Color color)
        {
            this.x = x;
            this.y = y;
            this.angleRad = Math.toRadians(angleDeg);
            this.speed = speed;
            this.color = color;
        }
    }

    private List<Particle> particles = new ArrayList<>();
    private GreenfootImage buffer;
    private int width;
    private int height;
    public static ParticleManager getInstance()
    {
        if (_instance == null) {
            _instance = new ParticleManager();
        }
        return _instance;
    }
    
    public void addParticle(double x, double y, double angleDeg, double speed, Color color)
    {
        if (particles.size() < 1000)
        {
            particles.add(new Particle(x, y, angleDeg, speed, color));
        }
    }
    
    @Override
    protected void addedToWorld(World w)
    {
        this.width = w.getWidth();
        this.height = w.getHeight();
        buffer = new GreenfootImage(this.width, this.height);
        buffer.setTransparency(255);
        setImage(buffer);
    
    }
    
    
    public void act()
    {
        if (buffer == null) return;
        
        for (int i = particles.size() - 1; i >= 0; i--)
        {
            Particle p = particles.get(i);
            p.x += Math.cos(p.angleRad) * p.speed;
            p.y += Math.cos(p.angleRad) * p.speed;
            p.speed = Math.max(0, p.speed-0.1);
            
            if (p.speed == 0)
            {
                particles.remove(i);
            }
        }
        
        
        buffer.clear();
        
        for (Particle p : particles)
        {
            drawParticle(p);
        }
        
        setImage(buffer);
    }
    
    private void drawParticle(Particle p)
    {
        double px = p.x;
        double py = p.y;
        double s = p.speed;
        double a = p.angleRad;
        double[] xRenderPoints = {
            px + Math.cos(a)*s*3,
            px + Math.cos(a + Math.PI *0.5)*s*0.5,
            px + Math.cos(a+Math.PI)*s*3,
            px + Math.cos(a - Math.PI *0.5)*s*0.5
        };
        double[] yRenderPoints = {
            py + Math.sin(a)*s*3,
            py + Math.sin(a+Math.PI*0.5)*s*0.5,
            py + Math.sin(a+Math.PI)*s*3,
            py - Math.sin(a+Math.PI*0.5)*s*0.5
        };
        
        int[] xRender = new int[xRenderPoints.length];
        int[] yRender = new int[yRenderPoints.length];
        
        for (int i = 0; i < xRenderPoints.length; i++) {
            xRender[i] = (int) Math.round(xRenderPoints[i]);
            yRender[i] = (int) Math.round(yRenderPoints[i]);
        }
        
        buffer.setColor(p.color);
        buffer.fillPolygon(xRender,yRender, xRender.length);
        
    }
}
