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

public class Coin extends Pane {
    private ImageView imageView;
    private boolean animationPlaying = false;

    public Coin(double x, double y) {
        Image coinImage = new Image(getClass().getResourceAsStream("/is/vidmot/pics/coins/coin1.png"));
        imageView = new ImageView(coinImage);
        imageView.setFitWidth(24);
        imageView.setFitHeight(24);
        getChildren().add(imageView);
        setTranslateX(x);
        setTranslateY(y);
    }

    public static List<List<Coin>> loadCoinPattern(String fileName) {
        List<List<Coin>> pattern = new ArrayList<>();
        try {
            InputStream is = Coin.class.getResourceAsStream(fileName);
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

    public void animate() {
        animationPlaying = true;
        Timeline animation = new Timeline();
        for (int i = 1; i <= 6; i++) {
            Image coinImage = new Image(getClass().getResourceAsStream("/is/vidmot/pics/coins/coin" + i + ".png"));
            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * 100), e -> imageView.setImage(coinImage));
            animation.getKeyFrames().add(keyFrame);
        }
        animation.setOnFinished(e -> animationPlaying = false);
        animation.setCycleCount(1);
        animation.play();
    }

    public boolean animationPlaying() {
        return animationPlaying;
    }
}
