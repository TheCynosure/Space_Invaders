import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.*;
import org.lwjgl.util.vector.Vector3f;
import org.lwjgl.util.vector.Vector4f;

import java.util.ArrayList;
import java.util.Scanner;

/**
 * Created by john_bachman on 3/11/16.
 */
public class Test {
    static boolean paused = false;

    public static void main(String[] args) {
        ArrayList<Sprite> projectiles = new ArrayList<Sprite>();
        Window window = new Window(1000 , 600, "Game Testing");
        OBJLoader loader = new OBJLoader();
        Sprite ship = new Sprite(loader.loadObjModel("res/CAR.obj", "res/Orange_Texture.png", "PNG"));
        Sprite shield = new Sprite(loader.loadObjModel("res/Sphere.obj", "res/Orange_Texture.png", "PNG"));
        Model projectileModel = loader.loadObjModel("res/Sphere.obj",  "res/Orange_Texture.png", "PNG");
        GL30.glBindVertexArray(ship.getModel().getVaoID());
        Shader shader = new Shader("shaders/vertexShader.vshr", "shaders/fragmentShader.fshr");
        GL30.glBindVertexArray(0);
        Camera camera = new Camera(shader);
        ship.translate(0, -7, -20);
        shield.translate(0, -7, -20);
        shield.scale(3f);
        ship.scale(0.25f);
        ship.rotate(90, 0, 0);
        shield.color = new Vector4f(0, 0, 1, 0.5f);
        int projectileCount = 0;
        float fieldStrength = 0.5f;
        while(!Display.isCloseRequested()) {
            if(!paused) {
                GL11.glEnable(GL11.GL_NORMALIZE);
                GL11.glEnable(GL11.GL_CULL_FACE);
                GL11.glEnable(GL11.GL_BLEND);
                GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                GL11.glDepthMask(true);
                if(projectileCount > 0) {
                    projectileCount--;
                }
                window.clean();
                shader.start();
                for(int i = 0; i < projectiles.size(); i++) {
                    Sprite projectile = projectiles.get(i);
                    projectile.translate(projectile.getVelocities().x, projectile.getVelocities().y, projectile.getVelocities().z);
                    projectile.render(shader, camera);
                    if(projectile.getPosition().z < -100) {
                        projectiles.remove(i);
                    }
                }
                fieldStrength -= 0.01;
                shield.color = new Vector4f(0, 0, 1, fieldStrength);
                ship.render(shader, camera);
                shield.render(shader, camera);
                shield.translate(ship.getPosition().x - shield.getPosition().x, ship.getPosition().y - shield.getPosition().y, 0);
                //Rendering between the Shader Calls
                shader.stop();
                GL11.glDisable(GL11.GL_BLEND);
            }
            window.update();
            if(Keyboard.isKeyDown(Keyboard.KEY_W)) {
//                camera.move(0,0,-1);
                if(ship.getPosition().y < 7.25) {
                    ship.translate(0, 0.25f, 0);
                }
            } else if(Keyboard.isKeyDown(Keyboard.KEY_S)) {
//                camera.move(0,0,1);
                if(ship.getPosition().y > -9.25) {
                    ship.translate(0, -0.25f, 0);
                }
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_A)) {
//                camera.move(-1,0,0);
                if(ship.getPosition().x > -10) {
                    ship.translate(-0.25f, 0, 0);
                }
            } else if(Keyboard.isKeyDown(Keyboard.KEY_D)) {
//                camera.move(1,0,0);
                if(ship.getPosition().x < 10) {
                    ship.translate(0.25f, 0, 0);
                }
            }
            if(Keyboard.isKeyDown(Keyboard.KEY_SPACE)) {
                if(projectileCount <= 30) {
                    projectileCount+= 10;
                    Sprite projectile = new Sprite(projectileModel, ship.getPosition().x, ship.getPosition().y - 0.5f, ship.getPosition().z);
                    projectile.setVelocities(0, 0, -1);
                    projectile.scale(0.25f);
                    projectile.color = new Vector4f(0, 0, 1, 1f);
                    projectiles.add(projectile);
                }
            }
            while(Keyboard.next()) {
                if(Keyboard.isKeyDown(Keyboard.KEY_E)) {
                    fieldStrength = 0.5f;
                } else if(Keyboard.isKeyDown(Keyboard.KEY_P)) {
                    if (paused) {
                        paused = false;
                    } else {
                        paused = true;
                    }
                }
            }

            while(Mouse.next()) {
                if(!paused) {
//                    camera.rotate((float)(Mouse.getDY() * 0.5), (float)(Mouse.getDX() *0.5), 0);
                }


            }
        }
        loader.destroy();
        shader.destroy();
        Display.destroy();
    }
}
