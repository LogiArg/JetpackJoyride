package is.vidmot;

import javafx.scene.media.AudioClip;

public class SoundManager {
    private static AudioClip coinSoundClip;

    public static void initialize() {
        String soundPath = SoundManager.class.getResource("/is/vidmot/sounds/coin.wav").toString();
        coinSoundClip = new AudioClip(soundPath);
        coinSoundClip.setVolume(0.5);

        // Pre-fetch the AudioClip by playing it with zero volume
        coinSoundClip.play(0);
    }

    public static void playCoinSound() {
        coinSoundClip.play();
    }
}
