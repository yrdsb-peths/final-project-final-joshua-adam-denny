import java.io.*;
import java.nio.file.*;
import java.util.*;
/**
 * OBJ Parser for Java, outputs to a 3D double array. 
 * [Face][vert][x,y,z]
 * 
 * This parser reads a Wavefront OBJ file and extracts vertices and faces, scaling the coordinates by a specified factor.
 * 
 * Built for ScuffedEngine
 * @author Denny Ung
 * @version Version 1.1.0 (May 17, 2025)
 */
public class ObjParser{



    /**
     * Parses an OBJ file and returns the vertices and faces as a 3D double array.
     * 
     * @param path The path to the OBJ file.
     * @param scale The scale factor to apply to the vertex coordinates.
     * @return A 3D double array where each face contains its vertices' coordinates.
     * @throws IOException If an error occurs while reading the file.
     */
    public static double[][][] parseObj(String path, int scale) throws IOException {
        List<double[]> vertices = new ArrayList<>();
        List<double[][]> faces   = new ArrayList<>();

        try (BufferedReader br = Files.newBufferedReader(Paths.get(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                
        
                if (line.startsWith("v ")) {
                    // parse vertex line, into x, y, z coords lolololololol
                    String[] tokens = line.substring(2).split("\\s+");
                    double x = Double.parseDouble(tokens[0]) * scale;
                    double y = Double.parseDouble(tokens[1]) * scale;
                    double z = Double.parseDouble(tokens[2]) * scale;
                    vertices.add(new double[]{x, y, z});

                } else if (line.startsWith("f ")) {
                    
                    // parse face, brain fart
                    String[] tokens = line.substring(2).split("\\s+");
                    double[][] face = new double[tokens.length][3];

                    for (int vi = 0; vi < tokens.length; vi++) {
                        // support "f v", "f v/vt", or "f v/vt/vn"
                        String idxToken = tokens[vi].split("/")[0];
                        int vertIndex = Integer.parseInt(idxToken) - 1;  // OBJ is 1-based
                        
                        face[vi] = vertices.get(vertIndex);
                        
                        //double[] v = vertices.get(vertIndex);
                        //face[vi] = new double[]{ v[0], v[1], v[2] };
                    }
                    faces.add(face);
                }
            }
        }

        // Convert to array-of-arrays
        return faces.toArray(new double[faces.size()][][]);
    }
}
