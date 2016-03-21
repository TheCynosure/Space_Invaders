import com.sun.org.apache.bcel.internal.classfile.Utility;
import javafx.concurrent.Task;
import javafx.scene.input.KeyEvent;
import javafx.scene.media.MediaPlayer;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector4f;
import java.util.ArrayList;

/**
 * Created by Jack on 3/19/2016.
 */
public class Main {

    private static boolean cameraLock = true;

    public static void main(String[] args) {

        AudioThread audioThread = new AudioThread();
        audioThread.start();

        float[] textureCoords = {
                0,0,
                0,1,
                1,1,
                1,0
        };

        float[] vertices = {
                -0.5f, 0.5f, 0,
                -0.5f, -0.5f, 0,
                0.5f, -0.5f, 0,
                0.5f, 0.5f, 0f
        };

        int[] indices = {
                0,1,3,
                3,1,2
        };

        Window window = new Window(800, 600, "Space Invaders");
        Explosions explosions = new Explosions();
        ModelLoader modelLoader = new ModelLoader();
        OBJLoader loader = new OBJLoader();
        ArrayList<Sprite> bullets =  new ArrayList<Sprite>();
        Model bulletModel = loader.loadObjModel("res/Orange_Box.obj", "res/Orange_Texture.png", "PNG");
        ArrayList<Sprite> gameObjects = new ArrayList<Sprite>();
        for (int i = 0; i < 5; i++) {
            Model model = loader.loadObjModel("res/SIFixed.obj", "res/Orange_Texture.png", "PNG");
            SpaceInvader spaceInvader1 = new SpaceInvader(model);
            spaceInvader1.scale(0.025f);
            spaceInvader1.translate(i *  model.getWidth(), 10 - model.getHeight(), -30);
            spaceInvader1.color = new Vector4f((float)(Math.random()), (float)(Math.random()), (float)(Math.random()), 1);
            gameObjects.add(spaceInvader1);
        }
        Sprite spaceShip = new Sprite(loader.loadObjModel("res/ship.obj", "res/Orange_Texture.png", "PNG"));
        Sprite secret = new Sprite(modelLoader.createModel(vertices, textureCoords, indices, new Texture("res/secret.png", "PNG"), 0, 0, 0, 0));
        Shader shader = new Shader("shaders/vertexShader.vshr", "shaders/fragmentShader.fshr");
        gameObjects.add(spaceShip);
        Camera camera = new Camera(shader);
        spaceShip.scale(0.15f);
        spaceShip.translate(0,-15,-30);
        spaceShip.rotate(0, 90, 0);
        spaceShip.color = new Vector4f(1f, 1f, 1f, 1);
        secret.translate(-1.5f, 0, -1);
        secret.rotate(0, 65, 0);
        while(!Display.isCloseRequested()) {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            window.clean();
            shader.start();
            explosions.check(shader, camera);
            secret.render(shader, camera);
            for (int i = 0; i < bullets.size(); i++) {
                Sprite bullet = bullets.get(i);
                bullet.move();
                if (bullet.getPosition().y > 20) {
                    bullets.remove(i);
                } else {
                    bullet.render(shader, camera);
                    for (int j = 0; j < gameObjects.size(); j++) {
                        float x = bullet.getPosition().x;
                        float y = bullet.getPosition().y;
                        if (x >= gameObjects.get(j).getModel().getLowX() && x <= gameObjects.get(j).getModel().getHighX() && y <= gameObjects.get(j).getModel().getHighY() && y >= gameObjects.get(j).getModel().getLowY()) {
                            audioThread.explode();
                            explosions.addExplosion(bulletModel, gameObjects.get(j).color, gameObjects.get(j).getPosition().x, gameObjects.get(j).getPosition().y, gameObjects.get(j).getPosition().z);
                            gameObjects.remove(j);
                            bullets.remove(i);
                        }
                    }
                }
            }
            //Render loop for non bullets
            for(int i = 0; i < gameObjects.size(); i++) {
                gameObjects.get(i).move();
                gameObjects.get(i).render(shader, camera);
            }
            shader.stop();
            window.update();
            while(Keyboard.next()) {
                if(Keyboard.getEventKeyState()) {
                    if (Keyboard.getEventKey() == Keyboard.KEY_D) {
                        spaceShip.setVelocities(0.5f, 0, 0);
                    } else if (Keyboard.getEventKey() == Keyboard.KEY_A) {
                        spaceShip.setVelocities(-0.5f, 0, 0);
                    }
                    if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
                        audioThread.laser();
                        Sprite bullet = new Sprite(bulletModel, spaceShip.getPosition().x, spaceShip.getPosition().y, spaceShip.getPosition().z);
                        bullet.setVelocities(0, 2.5f, 0);
                        bullet.scale(0.25f);
                        bullets.add(bullet);
                    }
                    if(Keyboard.getEventKey() == Keyboard.KEY_E) {
                        if(cameraLock) {
                            cameraLock = false;
                        } else {
                            cameraLock = true;
                            camera.center();
                        }
                    }
                } else {
                    if (Keyboard.getEventKey() == Keyboard.KEY_D) {
                        spaceShip.setVelocities(0, 0, 0);
                    } else if (Keyboard.getEventKey() == Keyboard.KEY_A) {
                        spaceShip.setVelocities(0, 0, 0);
                    }
                }
            }
            while(Mouse.next()) {
                if(!cameraLock) {
                    camera.rotate(-Mouse.getDY() / 3, Mouse.getDX() / 3, 0);
                }
            }
        }
        modelLoader.clearPointers();
        shader.destroy();
        Display.destroy();
    }
}
