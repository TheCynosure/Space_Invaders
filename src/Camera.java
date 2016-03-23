import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector3f;

/**
 * Created by Jack on 3/15/2016.
 */
public class Camera {

    private final int FOV = 70;
    private final float nearPlane = 0.1f;
    private final float farPlane = 1000f;
    private Vector3f position = new Vector3f(0,0,0);
    private float pitch;
    private float yaw;
    private float roll;

    public Camera(Shader shader) {
        shader.start();
        shader.loadMatrix(shader.projectionMatrixID, createProjectionMatrix());
        shader.stop();
    }

    private Matrix4f createProjectionMatrix(){
        float aspectRatio = (float) Display.getWidth() / (float) Display.getHeight();
        float y_scale = (float) ((1f / Math.tan(Math.toRadians(FOV/2f))) * aspectRatio);
        float x_scale = y_scale / aspectRatio;
        float frustum_length = farPlane - nearPlane;

        Matrix4f projectionMatrix = new Matrix4f();
        projectionMatrix.m00 = x_scale;
        projectionMatrix.m11 = y_scale;
        projectionMatrix.m22 = -((farPlane + nearPlane) / frustum_length);
        projectionMatrix.m23 = -1;
        projectionMatrix.m32 = -((2 * nearPlane * farPlane) / frustum_length);
        projectionMatrix.m33 = 0;
        return projectionMatrix;
    }

    public Matrix4f createViewMatrix() {
       return MatrixUtils.createTransformMatrix(new Vector3f(-position.x, -position.y, -position.z), pitch, yaw, roll, 1);
    }

    public void move(float x, float y, float z) {
        position.x += x;
        position.y += y;
        position.z += z;
    }

    public void rotate(float rx, float ry, float rz) {
        pitch += rx;
        yaw += ry;
        roll += rz;
    }

}
