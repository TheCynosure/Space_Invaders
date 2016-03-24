package Engine.Game_Specific;

import Engine.ShapeData;
import Engine.Sprite;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;

/**
 * Created by Jack on 3/23/2016.
 */
public class SpriteHandler {

    private Explosions explosions;
    private ArrayList<Sprite> bullets;
    private ArrayList<Sprite> gameObjects;
    private Sprite spaceShip, secret;

    public SpriteHandler(ModelHandler modelHandler) {
        explosions = new Explosions();
        bullets = new ArrayList<Sprite>();
        gameObjects = new ArrayList<Sprite>();
        spaceInvaderSetup(modelHandler);
        spaceShip = new Sprite(modelHandler.getPlayer());
        spaceShipSetup();
        secret = new Sprite(ShapeData.getFlatSquareModel("res/textures/secret.png", "PNG"));
        secretSetup();
    }

    private void spaceInvaderSetup(ModelHandler modelHandler) {
        modelHandler.scaleEnemies(0.025f);
        for(int rows = 0; rows < 3; rows++) {
            for (float i = -19; i + 0.25f + modelHandler.getEnemies()[0].getWidth() <= 20; i += 0.25 + modelHandler.getEnemies()[0].getWidth()) {
                float v = 0.25f;
                if(rows % 2 == 0) {
                    v = -v;
                }
                SpaceInvader spaceInvader1 = new SpaceInvader(modelHandler.getEnemies()[rows], i, 10 - rows * modelHandler.getEnemies()[0].getHeight(), -30, v, 0);
                spaceInvader1.scale(0.025f);
                spaceInvader1.color = new Vector4f(1, 0, 0, 1);
                gameObjects.add(spaceInvader1);
            }
        }
    }

    private void spaceShipSetup() {
        spaceShip.scale(0.15f);
        spaceShip.getModel().scaleXY(0.15f);
        spaceShip.translate(0, -15, -30);
        spaceShip.rotate(0, 90, 0);
        spaceShip.color = new Vector4f(1f, 1f, 1f, 1);
    }

    private void secretSetup() {
        secret.translate(-1.5f, 0, -1);
        secret.rotate(0, 65, 0);
    }

    public Explosions getExplosions() {
        return explosions;
    }

    public ArrayList<Sprite> getBullets() {
        return bullets;
    }

    public ArrayList<Sprite> getGameObjects() {
        return gameObjects;
    }

    public Sprite getSpaceShip() {
        return spaceShip;
    }

    public Sprite getSecret() {
        return secret;
    }
}
