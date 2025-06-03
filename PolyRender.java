import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.lang.Math.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Comparator;

/**
 * 3D Renderer for Greenfoot
 * 
 * @author Denny Ung
 * @version Version 1.0.1 (May 30 2025)
 */



public class PolyRender extends DDCRender
{
    World world;
    GreenfootImage renderScreen;
    SimpleTimer deltaTime = new SimpleTimer();
    
    // default values
    private double fov = Math.toRadians(90);
    private double near = 0.1;
    private double far = 10000; 
    private int width = 1160;
    private int height = 600;
    private double aspect = (double) width/height;
    private double f = 1.0/Math.tan(fov/2.0);
    public double[][][] model = new double[0][][];
    
    private double modelX=0, modelY=0, modelZ=0;
    private double rotX=0, rotY=0, rotZ=0;  // in radians
    private double modelScale = 1.0;
    
    private double[][][] originalModel;
    
    public static int global_count = 0; 
    private int id;
    private boolean useScreen = false;
    
    public double X_Pos = 0;
    public double Y_Pos = 0; // Spawn one meter above ground zero iwjngoiejgoejrgoiermfioregmeorigmeroi
    public double Z_Pos = 0;
    public double X_Rot = 0;
    public double Y_Rot = 0;
    public double Z_Rot = 0;

    
    private double[][] proj = {
        { f/aspect,    0,                              0,                              0 },
        { 0,           f,                              0,                              0 },
        { 0,           0,    (far+near)/(near-far),   (2*far*near)/(near-far)       },
        { 0,           0,                             -1,                              0 }
    };
    
    
    public PolyRender(double[][][] model3d) {
        useScreen = true;
        global_count++;
        id = global_count;
        this.originalModel = deepCopy(model3d);
        this.renderScreen  = new GreenfootImage(width, height);
    }
    
    public PolyRender(double[][][] model3d, int widthCustom, int heightCustom) {
        
        global_count++;
        id = global_count;
        this.originalModel = deepCopy(model3d);
        this.renderScreen  = new GreenfootImage(widthCustom, heightCustom);
        this.width = widthCustom;
        this.height = heightCustom;
        aspect = (double) widthCustom/heightCustom;
        f = 1.0/Math.tan(fov/2.0);
        proj = new double[][]{
            { f/aspect,    0,                              0,                              0 },
            { 0,           f,                              0,                              0 },
            { 0,           0,    (far+near)/(near-far),   (2*far*near)/(near-far)       },
            { 0,           0,                             -1,                              0 }
        };

         
    }
    
    public PolyRender(double[][][] model3d, int widthCustom, int heightCustom, int newFOV) {
        global_count++;
        id = global_count;
        this.originalModel = deepCopy(model3d);
        this.renderScreen  = new GreenfootImage(widthCustom, heightCustom);
        
        this.width = widthCustom;
        this.height = heightCustom;
        this.fov = Math.toRadians(newFOV);
        aspect = (double) widthCustom/heightCustom;
        f = 1.0/Math.tan(fov/2.0);
        proj = new double[][]{
            { f/aspect,    0,                              0,                              0 },
            { 0,           f,                              0,                              0 },
            { 0,           0,    (far+near)/(near-far),   (2*far*near)/(near-far)       },
            { 0,           0,                             -1,                              0 }
        };
    }
    
    @Override
    protected void addedToWorld(World w)
    {
        if (useScreen)
        {
            width = w.getWidth();
            height = w.getHeight();
            aspect = (double) width/height;
            f = 1.0/Math.tan(fov/2.0);
            proj = new double[][]{
                { f/aspect,    0,                              0,                              0 },
                { 0,           f,                              0,                              0 },
                { 0,           0,    (far+near)/(near-far),   (2*far*near)/(near-far)       },
                { 0,           0,                             -1,                              0 }
            };
        }
    
    }
    
    public GreenfootImage getGreenfootImage()
    {
        return renderScreen;
    }
    
    private double[][][] deepCopy(double[][][] src) {
        double[][][] dst = new double[src.length][][];
        for (int f = 0; f < src.length; f++) {
            dst[f] = new double[src[f].length][3];
            for (int i = 0; i < src[f].length; i++) {
                System.arraycopy(src[f][i], 0, dst[f][i], 0, 3);
            }
        }
        return dst;
    }
    
    /** subtract camera pos, then apply yaw/pitch/roll */
    private double[] worldToView(double[] v) {
        double x = v[0];
        double y = v[1];
        double z = v[2];

        //SCALE
        x *= modelScale;
        y *= modelScale;
        z *= modelScale;

        //ROTATION
        // yaw = rotY, pitch = rotX, roll = rotZ
        double c, s;
        // Yaw (Y axis)
        c = Math.cos(rotY);  s = Math.sin(rotY);
        double x1 = x*c + z*s;
        double z1 = -x*s + z*c;
        double y1 = y;
        
        // Pitch (X axis)
        c = Math.cos(rotX);  s = Math.sin(rotX);
        double y2 = y1*c - z1*s;
        double z2 = y1*s + z1*c;
        double x2 = x1;
        
        // Roll (Z axis)
        c = Math.cos(rotZ);  s = Math.sin(rotZ);
        double x3 = x2*c - y2*s;
        double y3 = x2*s + y2*c;
        double z3 = z2;

        // world translation
        x3 += modelX;
        y3 -= modelY;
        z3 += modelZ;

        // view transform
        double cx = x3 - X_Pos;
        double cy = y3 - Y_Pos;
        double cz = z3 - Z_Pos;

        double yaw = Math.toRadians(Y_Rot);
        double pitch = Math.toRadians(X_Rot);
        double roll = Math.toRadians(Z_Rot);

        // yaw
        double xx = cx*Math.cos(yaw) + cz*Math.sin(yaw);
        double zz = -cx*Math.sin(yaw) + cz*Math.cos(yaw);
        double yy = cy;
        
        // pitch
        double yy2 = yy*Math.cos(pitch) - zz*Math.sin(pitch);
        double zz2 = yy*Math.sin(pitch) + zz*Math.cos(pitch);
        double xx2 = xx;
        
        // roll
        double xx3 = xx2*Math.cos(roll) - yy2*Math.sin(roll);
        double yy3 = xx2*Math.sin(roll) + yy2*Math.cos(roll);
        double zz3 = zz2;

        return new double[]{ xx3, yy3, zz3, 1 };
    }
    
        
    
    /** 
     * apply 4x4 projection matrix to [x,y,z,1]
     */
    private int[] project(double[] hv) {
        // multiply M×hv
        double[] r = new double[4];
        for (int i = 0; i < 4; i++) {
            r[i] = proj[i][0]*hv[0]
                 + proj[i][1]*hv[1]
                 + proj[i][2]*hv[2]
                 + proj[i][3]*hv[3];
        }
        
        // perspective divide
        if (r[3] == 0) r[3] = 0.0001;
        double ndcX =  r[0]/r[3];
        double ndcY =  r[1]/r[3];
        
        // map to screen
        System.out.println(width);
        System.out.println(height);
        int sx = (int)(( ndcX + 1)*0.5*width);
        int sy = (int)(((1 - ndcY)*0.5)*height);
        return new int[]{sx, sy};
    }
    
    public void setScale(double scale) 
    {
        this.modelScale = scale;
    }
    
    public void position(double x, double y, double z) 
    {

        this.modelX = x;  
        this.modelY = y;  
        this.modelZ = z;
    }
    public void rotate(double xRad, double yRad, double zRad) 
    {
        this.rotX = xRad; 
        this.rotY = yRad; 
        this.rotZ = zRad;
    }
    
    
    /**
     * Clip a convex polygon (in view‑space coords [x,y,z,1]) against the plane z = near.
     * Returns a new list of vertices (each a double[4]) with z >= near.
     */
    private List<double[]> clipAgainstNearPlane(List<double[]> inPoly) {
        List<double[]> outPoly = new ArrayList<>();
        int count = inPoly.size();
        for (int i = 0; i < count; i++) {
            double[] A = inPoly.get(i);
            double[] B = inPoly.get((i+1) % count);
    
            boolean inA = A[2] >= near;
            boolean inB = B[2] >= near;
    
            // if A is inside, keep it
            if (inA) {
                outPoly.add(A);
            }
            // if edge crosses the plane, compute intersection
            if (inA ^ inB) {
                // t = (near − zA) / (zB − zA)
                double t = (near - A[2]) / (B[2] - A[2]);
                double[] I = new double[4];
                for (int k = 0; k < 4; k++) {
                    I[k] = A[k] + t * (B[k] - A[k]);
                }
                // force w=1 just in case
                I[3] = 1;
                outPoly.add(I);
            }
        }
        return outPoly;
    }
    
    // Draw a single horizontal scanline from x0->x1 at y, interpolating brightness c0->c1
    private void drawGradientScanline(int y, int x0, int x1, int c0, int c1) {
        if (x0 > x1) { int t=x0; x0=x1; x1=t; t=c0; c0=c1; c1=t; }
        int dx = x1 - x0;
        if (dx <= 0) return;
        for (int x = x0; x <= x1; x++) {
            double t = (double)(x - x0) / dx;
            int ci = (int)(c0 + (c1 - c0)*t);
            ci = Math.max(0, Math.min(255, ci));
            renderScreen.setColor(new Color(ci,ci,ci));
            renderScreen.drawLine(x, y, x, y);
        }
    }
    
    // Rasterize one triangle with per‑vertex brightness bv[0..2]
    private void rasterizeTriangle(int[] sx, int[] sy, int[] bv) {
        // sort vertices by y ascending (personally i have no idea what this means.)
        for (int i = 0; i < 2; i++) {
            for (int j = 0; j < 2 - i; j++) {
                if (sy[j] > sy[j+1]) {
                    // swap y
                    int ty = sy[j]; sy[j] = sy[j+1]; sy[j+1] = ty;
                    // swap x
                    int tx = sx[j]; sx[j] = sx[j+1]; sx[j+1] = tx;
                    // swap brightness
                    int tc = bv[j]; bv[j] = bv[j+1]; bv[j+1] = tc;
                }
            }
        }
        int x0 = sx[0], y0 = sy[0], c0 = bv[0];
        int x1 = sx[1], y1 = sy[1], c1 = bv[1];
        int x2 = sx[2], y2 = sy[2], c2 = bv[2];
    
        // avoid divides by precomputing type sh
        double invY01 = (y1==y0)?0:1.0/(y1-y0);
        double invY02 = (y2==y0)?0:1.0/(y2-y0);
        double invY12 = (y2==y1)?0:1.0/(y2-y1);
    
        // scan from y0 to y2
        for (int y = y0; y <= y2; y++) {
            if (y < y1) {
                // lower sub‐triangle (0->1) vs (0->2)
                double tA = (y - y0)*invY01;
                double tB = (y - y0)*invY02;
                int xA = (int)(x0 + (x1 - x0)*tA);
                int xB = (int)(x0 + (x2 - x0)*tB);
                int bA = (int)(c0 + (c1 - c0)*tA);
                int bB = (int)(c0 + (c2 - c0)*tB);
                drawGradientScanline(y, xA, xB, bA, bB);
            } else {
                // upper sub‐triangle (1->2) vs (0->2)
                double tA = (y - y1)*invY12;
                double tB = (y - y0)*invY02;
                int xA = (int)(x1 + (x2 - x1)*tA);
                int xB = (int)(x0 + (x2 - x0)*tB);
                int bA = (int)(c1 + (c2 - c1)*tA);
                int bB = (int)(c0 + (c2 - c0)*tB);
                drawGradientScanline(y, xA, xB, bA, bB);
            }
        }
    }
    
    // convert Ngons into tri cuz I HATE NGONs, (literally makes zero sense for a face to have 3+ vert)
    // and rasterize (wtff is this) triangles with per-vertex brightness with gouraud shading
    private void fillPolyGouraud(int[] sx, int[] sy, int[] bv) {
        int n = sx.length;
        for (int i = 1; i < n-1; i++) {
            int[] tx = { sx[0], sx[i],   sx[i+1]   };
            int[] ty = { sy[0], sy[i],   sy[i+1]   };
            int[] tc = { bv[0], bv[i],   bv[i+1]   };
            rasterizeTriangle(tx, ty, tc);
        }
    }


    
    Color[] anchors = {
        new Color(  0,   0, 255),  
        new Color(  0, 255, 255), 
        new Color(  0, 255,   0), 
        new Color(255, 255,   0),  
        new Color(255, 165,   0),
        new Color(255,   0,   0)
    };
    
    public void act() {
        if (renderScreen == null) return;
        renderScreen.clear();
        
        // parallel lists
        List<double[][]> viewFaces = new ArrayList<>();
        List<Double> avgZs = new ArrayList<>();

        //frustum culling secret :3
        for (double[][] face : originalModel) {
            int n = face.length;
            double[][] viewCoords = new double[n][];

            // 1) world -> view transforms into a list
            List<double[]> poly = new ArrayList<>();
            for (double[] vert : face) {
                poly.add(worldToView(vert));
            }
            
            // clip against near plane
            poly = clipAgainstNearPlane(poly);
            if (poly.size() < 3) continue;       // fully clipped away
            
            // convert list into 2d array
            n = poly.size();
            viewCoords = new double[n][];
            for (int i = 0; i < n; i++) {
                viewCoords[i] = poly.get(i);
            }
            
            // if face is further then far plane cutoff, get rid of the face from rendering
            boolean anyInFar = false;
            for (double[] vc : viewCoords) {
                if (vc[2] <= far) {
                    anyInFar = true;
                    break;
                }
            }
            if (!anyInFar) continue;

            boolean ndcOk = false;
            for (int i = 0; i < n; i++) {
                double[] r = new double[4];
                for (int row = 0; row < 4; row++) {
                    r[row] = proj[row][0]*viewCoords[i][0]
                           + proj[row][1]*viewCoords[i][1]
                           + proj[row][2]*viewCoords[i][2]
                           + proj[row][3]*viewCoords[i][3];
                }
                double w = (r[3] == 0 ? 1e-4 : r[3]);
                double ndcX = r[0]/w, ndcY = r[1]/w;
                if (ndcX >= -1 && ndcX <= 1 && ndcY >= -1 && ndcY <= 1) {
                    ndcOk = true;
                    break;
                }
            }
            if (!ndcOk) continue;

            //compute avg Z for sorting
            double sumZ = 0;
            for (int i = 0; i < n; i++) {
                sumZ += viewCoords[i][2];
            }
            viewFaces.add(viewCoords);
            avgZs.add(sumZ / n);
        }

        // Sort face indices by depth: farthest first
        List<Integer> indices = new ArrayList<>();
        for (int i = 0; i < viewFaces.size(); i++) {
            indices.add(i);
        }
        indices.sort((i, j) -> Double.compare(avgZs.get(j), avgZs.get(i)));

        
        // Draw in sorted order, both for fixing face overlapping and shading needs, (super useful af)
        int indicesSize = indices.size();
        for (int balls = 0; balls < indicesSize; balls++) {
            double[][] vc = viewFaces.get(indices.get(balls));
            int n = vc.length;
            
            int[] sx = new int[n], sy = new int[n];
            for (int i = 0; i < n; i++) {
                int[] scr = project(vc[i]);
                sx[i] = scr[0];
                sy[i] = scr[1];
            }
            
            
            //Render system v1 (Grayscale distance shading (per face))
            
            double DepthColorDoubleValue = Utils.map(balls, 0.0, indicesSize-1, 10.0, 245.0);
            int DepthColorInt = (int)DepthColorDoubleValue;
            Color depthColor = new Color(DepthColorInt, DepthColorInt, DepthColorInt);
            renderScreen.setColor(depthColor);
            renderScreen.fillPolygon(sx, sy, n);
            
           
           
           
            
           
            //Render system v2 (Multi-color distance shading)
            /*
            double t = (double)balls / (indicesSize - 1);
            // which segment of the gradient?
            double scaled = t * (anchors.length - 1);
            int   idx0   = (int)Math.floor(scaled);
            int   idx1   = Math.min(idx0 + 1, anchors.length - 1);
            double localT = scaled - idx0;
        
            Color c0 = anchors[idx0], c1 = anchors[idx1];
            int   r = (int)(c0.getRed()   + (c1.getRed()   - c0.getRed())   * localT);
            int   g = (int)(c0.getGreen() + (c1.getGreen() - c0.getGreen()) * localT);
            int   b = (int)(c0.getBlue()  + (c1.getBlue()  - c0.getBlue())  * localT);
        
            renderScreen.setColor(new Color(r, g, b));
            
            renderScreen.fillPolygon(sx, sy, n);
            */
            
            
            
            
            //Render system v3 (gouraud shading)
            /*
            int[] bv = new int[n];
            for (int i = 0; i < n; i++) {
                bv[i] = (int)map(vc[i][2], near, far, 255, 0);
            }

            fillPolyGouraud(sx, sy, bv);
            */
        }

        setImage(renderScreen);
    }

    public void information() {
        System.out.println("PolyRender ID: " + id);
        System.out.println("Model Scale: " + modelScale);
        System.out.println("Position: (" + modelX + ", " + modelY + ", " + modelZ + ")");
        System.out.println("Rotation: (" + Math.toDegrees(rotX) + ", " + Math.toDegrees(rotY) + ", " + Math.toDegrees(rotZ) + ")");
    }
}
