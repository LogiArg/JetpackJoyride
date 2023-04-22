package is.vidmot;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

import java.util.Objects;

/**
 * The type Sound manager.
 */
public class SoundManager {
    /**
     * The constant coinSoundClip.
     */
    private static AudioClip coinSoundClip;
    /**
     * The constant warningMediaPlayer.
     */
    private static MediaPlayer warningMediaPlayer;
    /**
     * The constant missileIncomingMediaPlayer.
     */
    private static MediaPlayer missileIncomingMediaPlayer;

    /**
     * Initialize.
     */
    public static void initialize() {
        String soundPath = Objects.requireNonNull(SoundManager.class.getResource("/is/vidmot/sounds/coin.wav")).toString();
        coinSoundClip = new AudioClip(soundPath);
        coinSoundClip.setVolume(0.5);
        coinSoundClip.play(0);

        soundPath = Objects.requireNonNull(SoundManager.class.getResource("/is/vidmot/sounds/warning.mp3")).toString();
        Media warningMedia = new Media(soundPath);
        warningMediaPlayer = new MediaPlayer(warningMedia);
    }

    /**
     * Play coin sound.
     */
    public static void playCoinSound() {
        coinSoundClip.play();
    }

    /**
     * Play warning sound.
     */
    public static void playWarningSound() {
        warningMediaPlayer.stop();
        warningMediaPlayer.play();
    }

    /**
     * Play missile launch sound.
     */
    public static void playMissileLaunchSound() {
        String soundPath = Objects.requireNonNull(SoundManager.class.getResource("/is/vidmot/sounds/missile_launch.wav")).toString();
        Media missileIncomingMedia = new Media(soundPath);
        missileIncomingMediaPlayer = new MediaPlayer(missileIncomingMedia);
        missileIncomingMediaPlayer.setOnEndOfMedia(() -> missileIncomingMediaPlayer.dispose());
        missileIncomingMediaPlayer.play();
    }
}
