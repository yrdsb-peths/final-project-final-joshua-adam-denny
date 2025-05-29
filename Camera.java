import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * 3D Positioned Camera/Player for Greenfoot, modifed for final-project
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (May 21 2025)
 */
public class Camera extends DDCRender {
    public double speed = 15.0;
    public double turnSpeed = 1.5;
    private double xAcc = 0, zAcc = 0;
    private double yVel = 0;              
    private double gravity = 1.0;           
    private double jumpStrength = -20.0; 
    private double groundY;
    private boolean grounded = false;
    private boolean firstAct = true; 
    
    private boolean notHoldingC = false;
    private boolean crouching = false;
    private double standHeight;
    private double crouchHeight;
    private DDCRender ddcRender = DDCRender.getInstance();
    

    public Camera() { 
        
    }

    public void act() {
        
        
        // Debug output
    }
}
