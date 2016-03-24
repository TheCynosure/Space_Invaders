package Engine.Game_Specific;

import Engine.ResourceHandler;
import Engine.Sprite;
import org.lwjgl.input.Keyboard;

/**
 * Created by john_bachman on 3/24/16.
 */
public class PlayerController {

    private SpriteHandler spriteHandler;
    private ModelHandler modelHandler;
    boolean A = false, D = false;

    public PlayerController(SpriteHandler spriteHandler, ModelHandler modelHandler) {
        this.spriteHandler = spriteHandler;
        this.modelHandler = modelHandler;
    }

    public void playerKeyDown() {
        if(!spriteHandler.getSpaceShip().dead) {
            if (Keyboard.getEventKey() == Keyboard.KEY_D) {
                spriteHandler.getSpaceShip().setVelocities(0.5f, 0, 0);
                spriteHandler.getSpaceShip().rotate(0, 15, 0);
                D = true;
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_A) {
                spriteHandler.getSpaceShip().setVelocities(-0.5f, 0, 0);
                spriteHandler.getSpaceShip().rotate(0, -15, 0);
                A= true;
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
                ResourceHandler.AUDIO_THREAD.laser();
                Sprite bullet = new Sprite(modelHandler.getProjectile(), spriteHandler.getSpaceShip().getPosition().x, spriteHandler.getSpaceShip().getPosition().y, spriteHandler.getSpaceShip().getPosition().z);
                bullet.setVelocities(0, 2.5f, 0);
                bullet.scale(0.15f);
                spriteHandler.getPlayerBullets().add(bullet);
            }
        }
    }

    public void playerKeyUp() {
        if(!spriteHandler.getSpaceShip().dead) {
            if (Keyboard.getEventKey() == Keyboard.KEY_D) {
                spriteHandler.getSpaceShip().setVelocities(spriteHandler.getSpaceShip().getVelocities().x - 0.5f, 0, 0);
                spriteHandler.getSpaceShip().rotate(0, -15, 0);
                D = false;
            }
            if (Keyboard.getEventKey() == Keyboard.KEY_A) {
                spriteHandler.getSpaceShip().setVelocities(spriteHandler.getSpaceShip().getVelocities().x + 0.5f, 0, 0);
                spriteHandler.getSpaceShip().rotate(0, 15, 0);
                A = false;
            }
            if (!A && !D) {
                zero();
            }
        }
    }

    private void zero() {
        if(!spriteHandler.getSpaceShip().dead) {
            spriteHandler.getSpaceShip().setVelocities(0, 0, 0);
        }
    }
}
