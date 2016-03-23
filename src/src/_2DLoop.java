import org.lwjgl.input.Keyboard;

import java.awt.*;
import java.awt.event.KeyEvent;

/**
 * Created by Jack on 3/19/2016.
 */
public class _2DLoop {

    private Player player;

    public Player getPlayer() {
        return player;
    }

    public _2DLoop() {
        player = new Player(0, 0);
    }

    public void draw(Graphics graphics) {
        player.draw(graphics);
    }

    public void update() {
        player.move();
    }

    public void processKeys(boolean keyUp, int key) {
        if(keyUp) {
            if (key == Keyboard.KEY_D) {
                player.setVelocities(5, 0);
            }
            if(key == Keyboard.KEY_A) {
                player.setVelocities(-5, 0);
            }
            if(key == Keyboard.KEY_W) {
                player.setVelocities(0, -5);
            }
            if(key == Keyboard.KEY_S) {
                player.setVelocities(0, 5);
            }
        } else {
            player.setVelocities(0, 0);
        }
    }
}
