package Engine;

/**
 * Created by Jack on 3/23/2016.
 */
public class FlatRectangle {
    static float[] textureCoords = {
            0,0,
            0,1,
            1,1,
            1,0
    };

    static float[] vertices = {
            -0.5f, 0.5f, 0,
            -0.5f, -0.5f, 0,
            0.5f, -0.5f, 0,
            0.5f, 0.5f, 0f
    };

    static int[] indices = {
            0,1,3,
            3,1,2
    };
}
