import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
/**
 * 3D Positioned Camera/Player for Greenfoot
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
    

    public Camera() {
        
    }

    public void act() {
        CreditWorld world = (CreditWorld) getWorld();

        if (firstAct) {
            standHeight = PolyRender.Y_Pos;
            groundY = standHeight;
            crouchHeight = standHeight/2;
            grounded = true;
            firstAct = false;
        }
        double yawRad = Math.toRadians(PolyRender.Y_Rot);

        // Movement input
        if (Greenfoot.isKeyDown("w")) 
        {
            xAcc -= Math.sin(yawRad) * speed;
            zAcc += Math.cos(yawRad) * speed;
        }
        if (Greenfoot.isKeyDown("s")) 
        {
            xAcc += Math.sin(yawRad) * speed;
            zAcc -= Math.cos(yawRad) * speed;
        }
        if (Greenfoot.isKeyDown("a")) 
        {
            xAcc += Math.cos(yawRad) * speed;
            zAcc += Math.sin(yawRad) * speed;
        }
        if (Greenfoot.isKeyDown("d")) 
        {
            xAcc -= Math.cos(yawRad) * speed;
            zAcc -= Math.sin(yawRad) * speed;
        }
        
        if (Greenfoot.isKeyDown("space") && grounded) {
            yVel = jumpStrength;    // launch up
            grounded = false;
        }
        
        if (!Greenfoot.isKeyDown("c"))
        {
            notHoldingC = true;
        }
        
        if (Greenfoot.isKeyDown("c") && notHoldingC)
        {
            notHoldingC = false;
            crouching = !crouching;
            groundY = crouching ? crouchHeight : standHeight;
        
            PolyRender.Y_Pos -= groundY;
            yVel = 0;
        }
        // apply gravity every frame
        yVel += gravity;
        PolyRender.Y_Pos += yVel;

        // hit the ground?
        if (PolyRender.Y_Pos >= groundY) {
            PolyRender.Y_Pos = groundY;
            yVel = 0;
            grounded = true;
        }

        int dx = (int)Math.round(xAcc);
        int dz = (int)Math.round(zAcc);
        
        if (dx != 0 || dz != 0) {
            PolyRender.X_Pos += dx;
            PolyRender.Z_Pos += dz;
            xAcc -= dx;
            zAcc -= dz;
        }

        // Rotation input
        if (Greenfoot.isKeyDown("right")) {
            PolyRender.Y_Rot += turnSpeed;
        } else if (Greenfoot.isKeyDown("left")) {
            PolyRender.Y_Rot -= turnSpeed;
        }

        if (Greenfoot.isKeyDown("up")) {
            PolyRender.X_Rot -= turnSpeed; 
        } else if (Greenfoot.isKeyDown("down")) {
            PolyRender.X_Rot += turnSpeed;
        }
        
    }
}
