import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Created by Jack on 3/14/2016.
 */
public class Sprite {
    private Model model;
    private Vector3f position = new Vector3f(0,0,0);
    private Vector3f velocities = new Vector3f(0,0,0);
    private float rx = 0, ry = 0, rz = 0;
    private float scale = 1;
    public Vector4f color = new Vector4f(1,1,1,1);

    public Sprite(Model model) {
        this.model = model;
    }

    public Sprite(Model model, float x, float y, float z) {
        this.model = model;
        position.x = x;
        position.y = y;
        position.z = z;
    }

    public Sprite(Model model, float x, float y, float z, float rx, float ry, float rz) {
        this.model = model;
        position.x = x;
        position.y = y;
        position.z = z;
        this.rx = rx;
        this.ry = ry;
        this.rz = rz;
    }

    public Vector3f getVelocities() {
        return velocities;
    }

    public void setVelocities(float x, float y, float z) {
        velocities = new Vector3f(x, y, z);
    }

    public void translate(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }
    
    public void rotate(float rx, float ry, float rz) {
        this.rx += rx;
        this.ry += ry;
        this.rz += rz;
    }

    public void scale(float scale) {
        this.scale = scale;
    }

    public float getScale() {
        return scale;
    }
    
    public void render(Shader shader, Camera camera) {
        shader.loadVector4f(shader.colorID, color);
        shader.loadMatrix(shader.viewMatrixID, camera.createViewMatrix());
        shader.loadMatrix(shader.transformMatrixID, MatrixUtils.createTransformMatrix(position, rx, ry, rz, scale));
        shader.loadFloat(shader.scaleID, scale);
        model.render();
    }

    public Vector3f getPosition() {
        return position;
    }

    public Model getModel() {
        return model;
    }
}
