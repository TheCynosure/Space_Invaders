package Engine;

import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by john_bachman on 3/15/16.
 */
public class OBJLoader {

    public Model loadObjModel(String fileName, String texturePath, String type) {
        float lowestX = 0, highestX = 0,lowestY = 0, highestY = 0;
        FileReader fr = null;
        try {
            fr = new FileReader(new File(fileName));
        } catch(FileNotFoundException e) {
            System.out.println("Couldn't load file: " + fileName);
            e.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(fr);
        String line;

        List<Vector3f> vertices = new ArrayList<Vector3f>();
        List<Vector2f> textures = new ArrayList<Vector2f>();
        List<Vector3f> normals = new ArrayList<Vector3f>();
        List<Integer> indices = new ArrayList<Integer>();

        float[] verticesArray = null;
        float[] normalsArray = null;
        float[] textureArray= null;
        int[] indicesArray = null;

        try {
            while(true) {
                line = reader.readLine();
                String[] currentLine = line.split(" ");
                if(!line.isEmpty() && line.length() > 2) {
                    if (currentLine[0].equals("v")) {
                        float x = Float.parseFloat(currentLine[1]);
                        float y = Float.parseFloat(currentLine[2]);
                        if(x < lowestX) {
                            lowestX = x;
                        } else if(x > highestX) {
                            highestX = x;
                        }

                        if(y < lowestY) {
                            lowestY = y;
                        } else if(y > highestY) {
                            highestY = y;
                        }
                        Vector3f vertex = new Vector3f(x, y, Float.parseFloat(currentLine[3]));
                        vertices.add(vertex);
                    } else if (line.startsWith("vt ")) {
                        Vector2f texture = new Vector2f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]));
                        textures.add(texture);
                    } else if (line.startsWith("vn ")) {
                        Vector3f normal = new Vector3f(Float.parseFloat(currentLine[1]), Float.parseFloat(currentLine[2]), Float.parseFloat(currentLine[3]));
                        normals.add(normal);
                    } else if (line.startsWith("f ")) {
                        textureArray = new float[vertices.size() * 2];
                        normalsArray = new float[normals.size() * 3];
                        break;
                    }
                }
            }

            while(line != null) {
                if(!line.startsWith("f ")) {
                    line = reader.readLine();
                    continue;
                }
                String[] currentLine = line.split(" ");

                if(currentLine[1].contains("//")) {
                    String[] vertex1 = currentLine[1].split("//");
                    String[] vertex2 = currentLine[2].split("//");
                    String[] vertex3 = currentLine[3].split("//");

                    processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
                    processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
                    processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);
                }
                if(currentLine[1].contains("/")){

                    String[] vertex1 = currentLine[1].split("/");
                    String[] vertex2 = currentLine[2].split("/");
                    String[] vertex3 = currentLine[3].split("/");

                    processVertex(vertex1, indices, textures, normals, textureArray, normalsArray);
                    processVertex(vertex2, indices, textures, normals, textureArray, normalsArray);
                    processVertex(vertex3, indices, textures, normals, textureArray, normalsArray);

                } else {
                    indices.add(Integer.parseInt(currentLine[1]));
                    textureArray[Integer.parseInt(currentLine[1])] = 1 / Float.parseFloat(currentLine[1]);
                    indices.add(Integer.parseInt(currentLine[2]));
                    textureArray[Integer.parseInt(currentLine[2])] = 1 / Float.parseFloat(currentLine[2]);
                    indices.add(Integer.parseInt(currentLine[3]));
                    textureArray[Integer.parseInt(currentLine[3])] = 1 / Float.parseFloat(currentLine[3]);

                }
                line = reader.readLine();
            }
            reader.close();

        } catch (Exception e) {
            e.printStackTrace();
        }

        verticesArray = new float[vertices.size() * 3];
        indicesArray = new int[indices.size()];

        int vertexPointer = 0;
        for(Vector3f vertex: vertices) {
            verticesArray[vertexPointer++] = vertex.x;
            verticesArray[vertexPointer++] = vertex.y;
            verticesArray[vertexPointer++] = vertex.z;
        }

        for(int i = 0; i < indices.size(); i++) {
            indicesArray[i] = indices.get(i);
        }
        return ResourceHandler.MODEL_LOADER.createModel(verticesArray, textureArray, indicesArray, new Texture(texturePath, type), lowestX, lowestY, highestX, highestY);
    }

    private void processVertex(String[] vertexData, List<Integer> indices, List<Vector2f> textures, List<Vector3f> normals, float[] textureArray, float[] normalsArray) {
        int currentVertexPointer = Integer.parseInt(vertexData[0]) - 1;
        indices.add(currentVertexPointer);

        Vector2f currentTex = textures.get(Integer.parseInt(vertexData[1]) - 1);
        textureArray[currentVertexPointer * 2] = currentTex.x;
        textureArray[currentVertexPointer * 2 + 1] = 1 - currentTex.y;
    }
}
