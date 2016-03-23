import com.sun.javafx.tk.Toolkit;
import javafx.scene.media.AudioClip;
import java.io.File;

/**
 * Created by Jack on 3/19/2016.
 */
public class AudioThread extends Thread {

    private AudioClip laser;
    private AudioClip explosion;

    public AudioThread() {
        Toolkit.getToolkit().init();
        laser = new AudioClip(new File("res/Laser.wav").toURI().toString());
        laser.setVolume(0.2);
        explosion = new AudioClip(new File("res/blast.wav").toURI().toString());
        explosion.setVolume(0.5);
    }

    public void run() {
        AudioClip clip = new AudioClip(new File("res/techno.wav").toURI().toString());
        clip.setVolume(0.5);
        clip.setCycleCount(AudioClip.INDEFINITE);
        clip.play();
    }

    public void laser() {
        laser.play();
    }

    public void explode() {
        explosion.play();
    }
}
