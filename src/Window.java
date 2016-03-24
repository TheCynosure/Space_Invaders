import org.lwjgl.LWJGLException;
import org.lwjgl.opengl.*;

/**
 * Created by john_bachman on 3/11/16.
 */
public class Window {

    private int WIDTH, HEIGHT;

    public Window(int WIDTH, int HEIGHT, String title) {
        this.WIDTH = WIDTH;
        this.HEIGHT = HEIGHT;
        initDisplay(title);
    }

    private void initDisplay(String title) {
        try {
            ContextAttribs contextAttribs = new ContextAttribs(3, 2).withForwardCompatible(true).withProfileCore(true);
            Display.setDisplayMode(new DisplayMode(WIDTH, HEIGHT));
            Display.setTitle(title);
            Display.setSwapInterval(1);
            Display.create(new PixelFormat(), contextAttribs);
            //Initializing the OpenGL and Background Color.
            GL11.glViewport(0, 0, WIDTH, HEIGHT);
            GL11.glClearColor(1.0f, 0.0f, 0.0f, 1.0f);
        } catch (LWJGLException e) {
            e.printStackTrace();
        }
    }

    public void clean() {
        GL11.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
    }

    public void update() {
        Display.sync(60);
        Display.update();
    }

    public void setClearColor(float r, float b, float g, float a) {
        GL11.glClearColor(r, b, g, a);
    }

}
