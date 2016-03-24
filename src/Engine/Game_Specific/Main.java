package Engine.Game_Specific;

import Engine.Camera;
import Engine.ResourceHandler;
import Engine.Shader;
import Engine.Sprite;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL30;
import org.lwjgl.util.vector.Vector4f;

/**
 * Created by Jack on 3/19/2016.
 */
public class Main {

    private static boolean cameraLock = true;

    public static void main(String[] args) {
        ResourceHandler.AUDIO_THREAD.start();
        ModelHandler modelHandler = new ModelHandler();
        SpriteHandler spriteHandler = new SpriteHandler(modelHandler);

        //Class that controls the movement of the player
        PlayerController playerController = new PlayerController(spriteHandler, modelHandler);

        //Macs require you to have a VAO bound when making the shader, I have no clue why.
        GL30.glBindVertexArray(spriteHandler.getSpaceShip().getModel().getVaoID());
        Shader shader = new Shader("shaders/vertexShader.vshr", "shaders/fragmentShader.fshr");
        GL30.glBindVertexArray(0);
        //End of that Mac Crap

        //Creates a Engine.Camera that will control the View Matrix
        Camera camera = new Camera(shader);

        System.out.println("LowX: " + spriteHandler.getSpaceShip().getLowX());
        System.out.println("LowY: " + spriteHandler.getSpaceShip().getLowY());
        System.out.println("HighX: " + spriteHandler.getSpaceShip().getHighX());
        System.out.println("HighY: " + spriteHandler.getSpaceShip().getHighY());

        while(!Display.isCloseRequested()) {
            //Making use of the Depth Buffer.
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            //Clear and prep the Engine.Window for Rendering.
            ResourceHandler.WINDOW.clean();
            //Start the Engine.Shader, this way everything will be handled by this shader.
            shader.start();
            //Checks the LifeSpan of the Explosion, if not expired then it will draw them.
            spriteHandler.getExplosions().check(shader, camera);
            //Drawing the Secret and the Player separately so that they do not have collision applied to them.
            spriteHandler.getSecret().render(shader, camera);
            if(!spriteHandler.getSpaceShip().dead) {
                spriteHandler.getSpaceShip().render(shader, camera);
            }
            //Moving the player.
            spriteHandler.getSpaceShip().move();
            //Bullet checking and collision handling.
            for (int i = 0; i < spriteHandler.getPlayerBullets().size(); i++) {
                Sprite bullet = spriteHandler.getPlayerBullets().get(i);
                bullet.move();
                if (bullet.getPosition().y > 20) {
                    //If off the screen then remove them
                    spriteHandler.getPlayerBullets().remove(i);
                    i--;
                } else if (spriteHandler.getPlayerBullets().size() > 0) {
                    //If on the screen then draw them
                    bullet.render(shader, camera);
                    for (int j = 0; j < spriteHandler.getGameObjects().size(); j++) {
                        //If inside the gameObject
                        if(spriteHandler.getGameObjects().get(j).checkCollision(bullet)) {
                            // Make an explosion
                            //Noise
                            ResourceHandler.AUDIO_THREAD.explode();
                            //3D Explosion Particles
                            spriteHandler.getExplosions().addExplosion(modelHandler.getProjectile(), new Vector4f(spriteHandler.getGameObjects().get(j).color.x,spriteHandler.getGameObjects().get(j).color.y, spriteHandler.getGameObjects().get(j).color.z, 0.25f), spriteHandler.getGameObjects().get(j).getPosition().x, spriteHandler.getGameObjects().get(j).getPosition().y, spriteHandler.getGameObjects().get(j).getPosition().z);
                            //Remove object - Dead
                            spriteHandler.getGameObjects().remove(j);
                            //Remove Bullet Useless
                            spriteHandler.getPlayerBullets().remove(i);
                            //End the Check loop for this bullet because it is gone.
                            break;
                        }
                    }
                }
            }
            //Render loop for non bullets
            for(int i = 0; i < spriteHandler.getGameObjects().size(); i++) {
                //Move based on Velocities
                spriteHandler.getGameObjects().get(i).move();
                //Draw Them
                spriteHandler.getGameObjects().get(i).render(shader, camera);
                if(spriteHandler.getGameObjects().get(i) instanceof SpaceInvader) {
                    ((SpaceInvader) spriteHandler.getGameObjects().get(i)).fireCheck(spriteHandler, modelHandler);
                }
            }

            if(!spriteHandler.getSpaceShip().dead) {
                for (int i = 0; i < spriteHandler.getEnemyBullets().size(); i++) {
                    Sprite bullet = spriteHandler.getEnemyBullets().get(i);
                    bullet.move();
                    if (bullet.getPosition().y > 20) {
                        //If off the screen then remove them
                        spriteHandler.getEnemyBullets().remove(i);
                        i--;
                    } else if (spriteHandler.getEnemyBullets().size() > 0) {
                        //If on the screen then draw them
                        bullet.render(shader, camera);
                        if (spriteHandler.getSpaceShip().checkCollision(bullet)) {
                            System.out.println("Hit");
                            // Make an explosion
                            //Noise
                            ResourceHandler.AUDIO_THREAD.explode();
                            //3D Explosion Particles
                            spriteHandler.getExplosions().addExplosion(modelHandler.getProjectile(), spriteHandler.getSpaceShip().color, spriteHandler.getSpaceShip().getPosition().x, spriteHandler.getSpaceShip().getPosition().y, spriteHandler.getSpaceShip().getPosition().z);
                            //Remove object - Dead
                            spriteHandler.getSpaceShip().dead = true;
                        }
                    }
                }
            }
            //Stop the Rendering process.
            shader.stop();
            //Update the Engine.Window and switch the buffers.
            ResourceHandler.WINDOW.update();
            //Poll for events
            while(Keyboard.next()) {
                if(Keyboard.getEventKeyState()) {
                    if(Keyboard.getEventKey() == Keyboard.KEY_E) {
                        if(cameraLock) {
                            cameraLock = false;
                        } else {
                            cameraLock = true;
                            camera.center();
                        }
                    } else {
                        playerController.playerKeyDown();
                    }
                } else {
                    playerController.playerKeyUp();
                }
            }
            while(Mouse.next()) {
                if(!cameraLock) {
                    camera.rotate(-Mouse.getDY() / 3, Mouse.getDX() / 3, 0);
                }
            }
        }
        shader.destroy();
        Display.destroy();
        AudioThread.currentThread().interrupt();
    }
}
