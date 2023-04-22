package is.vidmot;

import javafx.scene.media.AudioClip;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class SoundManager {
    private static AudioClip coinSoundClip;
    private static MediaPlayer warningMediaPlayer;
    private static MediaPlayer missileIncomingMediaPlayer;

    public static void initialize() {
        String soundPath = SoundManager.class.getResource("/is/vidmot/sounds/coin.wav").toString();
        coinSoundClip = new AudioClip(soundPath);
        coinSoundClip.setVolume(0.5);
        coinSoundClip.play(0);

        soundPath = SoundManager.class.getResource("/is/vidmot/sounds/warning.mp3").toString();
        Media warningMedia = new Media(soundPath);
        warningMediaPlayer = new MediaPlayer(warningMedia);
    }

    public static void playCoinSound() {
        coinSoundClip.play();
    }

    public static void playWarningSound() {
        warningMediaPlayer.stop();
        warningMediaPlayer.play();
    }

    public static void playMissileLaunchSound() {
        String soundPath = SoundManager.class.getResource("/is/vidmot/sounds/missile_launch.wav").toString();
        Media missileIncomingMedia = new Media(soundPath);
        missileIncomingMediaPlayer = new MediaPlayer(missileIncomingMedia);
        missileIncomingMediaPlayer.setOnEndOfMedia(() -> missileIncomingMediaPlayer.dispose());
        missileIncomingMediaPlayer.play();
    }
}
