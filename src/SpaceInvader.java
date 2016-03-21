/**
 * Created by Jack on 3/20/2016.
 */
public class SpaceInvader extends Sprite {

    public SpaceInvader(Model model) {
        super(model);
        setVelocities(0.2f, 0, 0);
    }

    public SpaceInvader(Model model, float x, float y, float z) {
        super(model, x, y, z);
        setVelocities(0.2f, 0, 0);
    }

    public SpaceInvader(Model model, float x, float y, float z, float rx, float ry, float rz) {
        super(model, x, y, z, rx, ry, rz);
        setVelocities(0.2f, 0, 0);
    }

    public void move() {
        super.move();
        if(getPosition().x - getModel().getWidth() / 2 <= -21 || getPosition().x + getModel().getWidth() / 2 >= 21) {
            translate(0, -getModel().getHeight(), 0);
            setVelocities(-getVelocities().x, 0, 0);
        }
    }
}
