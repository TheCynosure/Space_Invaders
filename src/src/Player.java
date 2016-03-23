import java.awt.*;

/**
 * Created by Jack on 3/19/2016.
 */
public class Player extends Sprite2D {
    public Player(int x, int y) {
        super(x, y);
    }

    @Override
    void draw(Graphics graphics) {
        graphics.setColor(Color.GREEN);
        graphics.fillRect(x, y, 30, 30);
    }


}
