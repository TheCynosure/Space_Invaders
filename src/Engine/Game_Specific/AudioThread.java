package Engine.Game_Specific;

import com.sun.javafx.tk.Toolkit;
import javafx.scene.media.AudioClip;
import java.io.File;

/**
 * Created by Jack on 3/19/2016.
 */
public class AudioThread extends Thread {

    private final AudioClip laser;
    private final AudioClip explosion;

    public AudioThread() {
        Toolkit.getToolkit().init();
        laser = new AudioClip(new File("res/sound/laser.wav").toURI().toString());
        laser.setVolume(0.15);
        explosion = new AudioClip(new File("res/sound/blast.wav").toURI().toString());
        explosion.setVolume(0.15);
    }

    public void run() {
        AudioClip clip = new AudioClip(new File("res/sound/techno.wav").toURI().toString());
        clip.setVolume(0.25);
        clip.setCycleCount(AudioClip.INDEFINITE);
        clip.play();
    }

    public void laser() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                laser.play();
            }
        });
        thread.start();
    }

    public void explode() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                explosion.play();
            }
        });
        thread.start();
    }
}
