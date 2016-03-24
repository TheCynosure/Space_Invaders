package Engine.Game_Specific;

import Engine.MatrixUtils;
import Engine.Model;
import Engine.ResourceHandler;
import Engine.Sprite;
import org.lwjgl.util.vector.Matrix4f;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

/**
 * Created by Jack on 3/20/2016.
 */
public class SpaceInvader extends Sprite {

    int fireTime = (int)(Math.random() * 500);

    public SpaceInvader(Model model) {
        super(model);
        setVelocities(0.2f, 0, 0);
    }

    public SpaceInvader(Model model, float x, float y, float z) {
        super(model, x, y, z);
        setVelocities(0.2f, 0, 0);
    }

    public SpaceInvader(Model model, float x, float y, float z, float xV, float yV) {
        super(model, x, y, z);
        setVelocities(xV, yV, 0);
    }

    public SpaceInvader(Model model, float x, float y, float z, float rx, float ry, float rz) {
        super(model, x, y, z, rx, ry, rz);
        setVelocities(0.2f, 0, 0);
    }

    @Override
    protected Matrix4f transformMatrix() {
        Matrix4f transformMat;
        transformMat = MatrixUtils.createTransformMatrix(new Vector3f(getPosition().x, getPosition().y, getPosition().z), getRx(), getRy(), getRz(), getScale());
        return transformMat;
    }

    public void fireCheck(SpriteHandler spriteHandler, ModelHandler modelHandler) {
        fireTime--;
        if(fireTime <= 0) {
            fireTime = (int)(Math.random() * 500);
            Sprite bullet = new Sprite(modelHandler.getProjectile(), getPosition().x, getPosition().y, getPosition().z);
            bullet.setVelocities(0, -2.5f, 0);
            bullet.scale(0.15f);
            bullet.color = new Vector4f(0,1,1,1);
            spriteHandler.getEnemyBullets().add(bullet);
        }
    }

    public void move() {
        if(getPosition().x - getModel().getWidth() / 2 <= -21 || getPosition().x + getModel().getWidth() / 2 >= 21) {
            translate(0, -getModel().getHeight(), 0);
            setVelocities(-getVelocities().x, 0, 0);
        }
        super.move();
    }
}
