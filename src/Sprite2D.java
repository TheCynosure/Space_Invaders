import java.awt.*;

/**
 * Created by Jack on 3/19/2016.
 */
public abstract class Sprite2D {
    protected int x, y;
    protected int vx, vy;

    public Sprite2D(int x, int y) {
        this.x = x;
        this.y = y;
        vx = 0;
        vy = 0;
    }

    public void move() {
        x += vx;
        y += vy;
    }

    public void setVelocities(int vx, int vy) {
        this.vx = vx;
        this.vy = vy;
    }

    abstract void draw(Graphics graphics);
}
