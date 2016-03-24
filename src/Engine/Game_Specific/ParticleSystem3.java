package Engine.Game_Specific;

import Engine.Camera;
import Engine.Model;
import Engine.Shader;
import Engine.Sprite;
import org.lwjgl.util.vector.Vector4f;

/**
 * Created by Jack on 3/19/2016.
 */
public class ParticleSystem3 {
    private Sprite[] particles;
    public int life = 25;

    public ParticleSystem3(Model baseModel, Vector4f color, int numOfParts, float startX, float startY, float startZ) {
        particles = new Sprite[numOfParts];
        for(int i =0; i < numOfParts; i++) {
            particles[i] = new Sprite(baseModel, startX, startY, startZ);
        }
        for(Sprite sprite: particles) {
            sprite.color = color;
            sprite.scale(0.25f);
            sprite.setVelocities((float)(Math.random() * 2 - 1),(float)( Math.random() * 2 -1),(float)( Math.random() * 2 - 1));
        }
    }

    public void check(Shader shader, Camera camera) {
        life--;
        for(Sprite sprite: particles) {
            sprite.move();
            sprite.render(shader, camera);
        }
    }
}
