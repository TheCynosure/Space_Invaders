package Engine;

/**
 * Created by Jack on 3/23/2016.
 */
public class ShapeData {

    public static Model getFlatSquareModel(String texturePath, String textureType) {
        return ResourceHandler.MODEL_LOADER.createModel(FlatRectangle.vertices, FlatRectangle.textureCoords, FlatRectangle.indices, new Texture(texturePath, textureType));
    }
}
