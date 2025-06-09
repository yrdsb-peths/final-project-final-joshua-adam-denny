import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

/**
 * Head Processor for 3D rendering.
 * 
 * @author Denny Ung
 * @version Version 1.0.0 (May 27, 2025)
 */
public class DDCRender extends Actor
{
    
    public static DDCRender _instance = null;
    Camera camera;
    PolyRender poly2;
    int halfWidth = 0;
    int halfHeight = 0;
    

    

    public DDCRender() {
        _instance = this;
    }

    protected void addedToWorld(World world)
    {
        // i literally hate greenfoot so much, // stupid handling of static variables
        if (this.getClass() == DDCRender.class) {
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
        
            poly2 = new PolyRender(cube);
            int halfWidth = getX();
            int halfHeight = getY();
            world.addObject(poly2, halfWidth + 100,halfHeight);
        }
    }

    public static DDCRender getInstance() {
        return _instance;
    }
    int rotation = 0;
    int position  = 0;
    public void act()
    {
        rotation+= 0.5;
        position+= 0.05;
        
        if (rotation > 360) rotation = 0;
        
        poly2.rotate(Math.toRadians(180), Math.toRadians(rotation), 0);

        poly2.position(0,Math.sin(position)*30 + 250,200);
        poly2.position(0,0,200);
        poly2.setScale(0.5);
    }
}
