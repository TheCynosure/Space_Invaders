package Engine;

import Engine.Game_Specific.AudioThread;
import Engine.ModelLoader;
import Engine.OBJLoader;
import Engine.Window;
import org.lwjgl.opengl.Display;

/**
 * Created by Jack on 3/23/2016.
 * Universal Class used to handle all of the loaders for the program.
 * Meaning every class will use the same loaders, easier de-allocation of data.
 */

public class ResourceHandler {
    public static final OBJLoader OBJ_LOADER = new OBJLoader();
    public static final ModelLoader MODEL_LOADER = new ModelLoader();
    public static final AudioThread AUDIO_THREAD = new AudioThread();
    public static final Window WINDOW = new Window(800, 600, "Space Invaders - Jack");

    public static void startAudio() {
        AUDIO_THREAD.start();
    }

    public static void clean() {
        //Ending the Audio Thread first because it takes time to stop.
        AUDIO_THREAD.interrupt();
        //Clearing the Data out of the Engine.Model Loader which will destroy all of the model data that has been loaded.
        MODEL_LOADER.clearPointers();
        //Destroying the Engine.Window last because it will take the OpenGL functionality / context with it.
        Display.destroy();
    }

}
