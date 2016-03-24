package Engine.Game_Specific;

import Engine.Camera;
import Engine.Model;
import Engine.Shader;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;

/**
 * Created by Jack on 3/19/2016.
 */
public class Explosions {
    private ArrayList<ParticleSystem3> explosions;

    public Explosions() {
        explosions = new ArrayList<ParticleSystem3>();
    }

    public void addExplosion(Model baseModel, Vector4f color, float x, float y, float z) {
        explosions.add(new ParticleSystem3(baseModel, color, x, y, z));
    }

    public void check(Shader shader, Camera camera) {
        for(int i = 0; i < explosions.size(); i++) {
            if(explosions.get(i).life <= 0) {
                explosions.remove(i);
            } else {
                explosions.get(i).check(shader, camera);
            }
        }
    }
}
