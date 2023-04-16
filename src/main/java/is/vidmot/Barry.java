package is.vidmot;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Barry extends ImageView {
    private static final double GRAVITY = 0.25;
    private static final double JETPACK_FORCE = -0.5;
    private double velocityY = 0;
    private Map<KeyCode, Boolean> pressedKeys = new HashMap<>();
    private Timeline animation;
    private Image barry1;
    private Image barry2;

    public Barry(Pane gamePane) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("barry.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        barry1 = new Image(getClass().getResourceAsStream("/is/vidmot/pics/Barry1.png"));
        barry2 = new Image(getClass().getResourceAsStream("/is/vidmot/pics/Barry2.png"));

        setImage(barry1);

        gamePane.setOnKeyPressed(event -> pressedKeys.put(event.getCode(), true));
        gamePane.setOnKeyReleased(event -> pressedKeys.put(event.getCode(), false));

        animation = new Timeline(
                new KeyFrame(Duration.millis(150), event -> switchImage())
        );
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
    }

    public void update() {
        double minY = 15;
        double maxY = 461 - 30 - getFitHeight();
        if (pressedKeys.getOrDefault(KeyCode.SPACE, false)) {
            velocityY += JETPACK_FORCE;
        } else {
            velocityY += GRAVITY;
        }
        double newY = getTranslateY() + velocityY;
        if (newY <= minY) {
            newY = minY;
            velocityY = 0;
        } else if (newY >= maxY) {
            newY = maxY;
            velocityY = 0;
        }
        setTranslateY(newY);
    }

    private void switchImage() {
        if (getImage() == barry1) {
            setImage(barry2);
        } else {
            setImage(barry1);
        }
    }
}
