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

/**
 * Created by Jack on 3/19/2016.
 */
public class Main {

    private static boolean cameraLock = true;

    public static void main(String[] args) {
        ResourceHandler.AUDIO_THREAD.start();
        ModelHandler modelHandler = new ModelHandler();
        SpriteHandler spriteHandler = new SpriteHandler(modelHandler);
        //Macs require you to have a VAO bound when making the shader, I have no clue why.
        GL30.glBindVertexArray(spriteHandler.getSecret().getModel().getVaoID());
        Shader shader = new Shader("shaders/vertexShader.vshr", "shaders/fragmentShader.fshr");
        GL30.glBindVertexArray(0);
        //End of that Mac Crap

        //Creates a Engine.Camera that will control the View Matrix
        Camera camera = new Camera(shader);

//        float openglWidth = (float)(Math.sin(90 - camera.getFOV()) / (Math.sin(camera.getFOV() / 2) / -30));

        System.out.println("Finished!");
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
            spriteHandler.getSpaceShip().render(shader, camera);
            //Moving the player.
            spriteHandler.getSpaceShip().move();
            //Bullet checking and collision handling.
            for (int i = 0; i < spriteHandler.getBullets().size(); i++) {
                Sprite bullet = spriteHandler.getBullets().get(i);
                bullet.move();
                if (bullet.getPosition().y > 20) {
                    //If off the screen then remove them
                    spriteHandler.getBullets().remove(i);
                    i--;
                } else if (spriteHandler.getBullets().size() > 0) {
                    //If on the screen then draw them
                    bullet.render(shader, camera);
                    for (int j = 0; j < spriteHandler.getGameObjects().size(); j++) {
                        //Check for collision
                        float x = bullet.getPosition().x;
                        float y = bullet.getPosition().y;
                        //If inside the gameObject
                        if (x >= spriteHandler.getGameObjects().get(j).getLowX() && x <= spriteHandler.getGameObjects().get(j).getHighX() && y <= spriteHandler.getGameObjects().get(j).getHighY() && y >= spriteHandler.getGameObjects().get(j).getLowY()) {
                            //Make an explosion
                            //Noise
                            ResourceHandler.AUDIO_THREAD.explode();
                            //3D Explosion Particles
                            spriteHandler.getExplosions().addExplosion(modelHandler.getProjectile(), spriteHandler.getGameObjects().get(j).color, spriteHandler.getGameObjects().get(j).getPosition().x, spriteHandler.getGameObjects().get(j).getPosition().y, spriteHandler.getGameObjects().get(j).getPosition().z);
                            //Remove object - Dead
                            spriteHandler.getGameObjects().remove(j);
                            //Remove Bullet Useless
                            spriteHandler.getBullets().remove(i);
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
            }
            //Stop the Rendering process.
            shader.stop();
            //Update the Engine.Window and switch the buffers.
            ResourceHandler.WINDOW.update();
            //Poll for events
            while(Keyboard.next()) {
                if(Keyboard.getEventKeyState()) {
                    if (Keyboard.getEventKey() == Keyboard.KEY_D) {
                        spriteHandler.getSpaceShip().setVelocities(0.5f, 0, 0);
                        spriteHandler.getSpaceShip().rotate(0, 15, 0);
                    } else if (Keyboard.getEventKey() == Keyboard.KEY_A) {
                        spriteHandler.getSpaceShip().setVelocities(-0.5f, 0, 0);
                        spriteHandler.getSpaceShip().rotate(0, -15, 0);
                    }
                    if (Keyboard.getEventKey() == Keyboard.KEY_SPACE) {
                        ResourceHandler.AUDIO_THREAD.laser();
                        Sprite bullet = new Sprite(modelHandler.getProjectile(), spriteHandler.getSpaceShip().getPosition().x, spriteHandler.getSpaceShip().getPosition().y, spriteHandler.getSpaceShip().getPosition().z);
                        bullet.setVelocities(0, 2.5f, 0);
                        bullet.scale(0.25f);
                        spriteHandler.getBullets().add(bullet);
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
                        spriteHandler.getSpaceShip().setVelocities(0, 0, 0);
                        spriteHandler.getSpaceShip().rotate(0, -15, 0);
                    } else if (Keyboard.getEventKey() == Keyboard.KEY_A) {
                        spriteHandler.getSpaceShip().setVelocities(0, 0, 0);
                        spriteHandler.getSpaceShip().rotate(0, 15, 0);
                    }
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
