package is.vidmot;

import javafx.animation.AnimationTimer;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

/**
 * The type Joyride controller.
 */
public class JoyrideController implements Initializable {
    /**
     * The Fx gameplay.
     */
    public Gameplay fxGameplay;
    /**
     * The Meter count.
     */
    private int meterCount = 0;
    /**
     * The Coin count.
     */
    private final int coinCount = 0;
    /**
     * The Coin counter text.
     */
    private Text coinCounterText;
    /**
     * The Meter counter text.
     */
    private Text meterCounterText;
    /**
     * The Meter counter text property.
     */
    private final StringProperty meterCounterTextProperty = new SimpleStringProperty("0000 M");


    @Override
    public void initialize(URL url, ResourceBundle rb) {
        initCoinCounter();
        initMeterCounter();
        coinCounterText.textProperty().bind(Bindings.format("%03d", fxGameplay.coinCountProperty()));
        meterCounterText.textProperty().bind(Bindings.format("%04d M", fxGameplay.meterCountProperty()));

        AnimationTimer meterCountUpdater = new AnimationTimer() {
            @Override
            public void handle(long now) {
                meterCount += fxGameplay.getBackgroundSpeed() * 500 / 6000;
                meterCounterTextProperty.set(String.format("%04d M", meterCount));
            }
        };
        meterCountUpdater.start();
    }

    /**
     * Init coin counter.
     */
    private void initCoinCounter() {
        try {
            Font font = Font.loadFont(getClass().getResourceAsStream("/is/vidmot/fonts/New Athletic M54.ttf"), 32);
            coinCounterText = new Text(String.format("%03d", coinCount));
            coinCounterText.setFont(font);
            coinCounterText.setFill(Color.GOLD);
            coinCounterText.setStroke(Color.BLACK);
            coinCounterText.setStrokeWidth(2);

            Image coinImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/is/vidmot/pics/coins/coin1.png")));
            ImageView coinImageView = new ImageView(coinImage);
            coinImageView.setFitWidth(20);
            coinImageView.setFitHeight(20);

            HBox coinCounterBox = new HBox(coinCounterText, coinImageView);
            coinCounterBox.setAlignment(Pos.CENTER_RIGHT);
            coinCounterBox.setSpacing(5);
            coinCounterBox.setTranslateX(5);
            coinCounterBox.setTranslateY(45);

            fxGameplay.getChildren().add(coinCounterBox);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Init meter counter.
     */
    private void initMeterCounter() {
        try {
            Font font = Font.loadFont(getClass().getResourceAsStream("/is/vidmot/fonts/New Athletic M54.ttf"), 40);
            meterCounterText = new Text(String.format("%04d M", meterCount));
            meterCounterText.setFont(font);
            meterCounterText.setFill(Color.SILVER);
            meterCounterText.setStroke(Color.BLACK);
            meterCounterText.setStrokeWidth(2);
            HBox meterCounterBox = new HBox(meterCounterText);
            meterCounterBox.setAlignment(Pos.CENTER_RIGHT);
            meterCounterBox.setSpacing(5);
            meterCounterBox.setTranslateX(5);
            meterCounterBox.setTranslateY(5);
            fxGameplay.getChildren().add(meterCounterBox);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
