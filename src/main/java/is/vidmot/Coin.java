package is.vidmot;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The type Coin.
 */
public class Coin extends Pane {
    /**
     * The Image view.
     */
    private final ImageView imageView;
    /**
     * The Animation playing.
     */
    private boolean animationPlaying = false;
    /**
     * The Animation played.
     */
    private boolean animationPlayed = false;
    /**
     * The Collected.
     */
    private boolean collected;

    /**
     * Instantiates a new Coin.
     *
     * @param x the x
     * @param y the y
     */
    public Coin(double x, double y) {
        Image coinImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/is/vidmot/pics/coins/coin1.png")));
        imageView = new ImageView(coinImage);
        imageView.setFitWidth(24);
        imageView.setFitHeight(24);
        getChildren().add(imageView);
        setTranslateX(x);
        setTranslateY(y);
    }

    /**
     * Load coin pattern list.
     *
     * @param fileName the file name
     *
     * @return the list
     */
    public static List<List<Coin>> loadCoinPattern(String fileName) {
        List<List<Coin>> pattern = new ArrayList<>();
        try {
            InputStream is = Coin.class.getResourceAsStream(fileName);
            assert is != null;
            InputStreamReader isr = new InputStreamReader(is);
            BufferedReader br = new BufferedReader(isr);

            String line;
            int rowIndex = 0;
            while ((line = br.readLine()) != null) {
                List<Coin> row = new ArrayList<>();
                for (int colIndex = 0; colIndex < line.length(); colIndex++) {
                    char cell = line.charAt(colIndex);
                    if (cell == '1') {
                        Coin coin = new Coin(24 * colIndex, 24 * rowIndex);
                        row.add(coin);
                    }
                }
                pattern.add(row);
                rowIndex++;
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return pattern;
    }

    /**
     * Animate.
     */
    public void animate() {
        animationPlaying = true;
        Timeline animation = new Timeline();
        for (int i = 1; i <= 6; i++) {
            Image coinImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/is/vidmot/pics/coins/coin" + i + ".png")));
            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * 100), e -> imageView.setImage(coinImage));
            animation.getKeyFrames().add(keyFrame);
        }
        Image coinImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/is/vidmot/pics/coins/coin1.png")));
        KeyFrame keyFrame = new KeyFrame(Duration.millis(700), e -> imageView.setImage(coinImage));
        animation.getKeyFrames().add(keyFrame);

        animation.setOnFinished(e -> {
            animationPlaying = false;
            animationPlayed = true;
        });
        animation.setCycleCount(1);
        animation.play();
    }

    /**
     * Play coin collected animation.
     */
    public void playCoinCollectedAnimation() {
        Timeline collectedAnimation = new Timeline();
        for (int i = 1; i <= 4; i++) {
            Image coinImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/is/vidmot/pics/coins/coinc" + i + ".png")));
            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * 66), e -> {
                imageView.setImage(coinImage);
                imageView.setFitWidth(34);
                imageView.setFitHeight(34);
            });
            collectedAnimation.getKeyFrames().add(keyFrame);
        }
        collectedAnimation.setOnFinished(e -> imageView.setImage(null));
        collectedAnimation.setCycleCount(1);
        collectedAnimation.play();
    }

    /**
     * Play coin sound.
     */
    public void playCoinSound() {
        SoundManager.playCoinSound();
    }

    /**
     * Animation playing boolean.
     *
     * @return the boolean
     */
    public boolean animationPlaying() {
        return animationPlaying;
    }

    /**
     * Animation played boolean.
     *
     * @return the boolean
     */
    public boolean animationPlayed() {
        return animationPlayed;
    }

    /**
     * Is collected boolean.
     *
     * @return the boolean
     */
    public boolean isCollected() {
        return collected;
    }

    /**
     * Sets collected.
     *
     * @param collected the collected
     */
    public void setCollected(boolean collected) {
        this.collected = collected;
    }
}
