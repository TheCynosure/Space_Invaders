import org.newdawn.slick.opengl.TextureLoader;

import java.io.FileInputStream;
import java.io.IOException;

/**
 * Created by john_bachman on 3/15/16.
 */
public class Texture {

    int textureID;

    public Texture(String fileName, String type) {
        getTextureID(fileName, type);
    }

    private void getTextureID(String fileName, String type) {
        try {
            textureID = TextureLoader.getTexture(type, new FileInputStream(fileName)).getTextureID();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public int getTextureID() {
        return textureID;
    }
}
