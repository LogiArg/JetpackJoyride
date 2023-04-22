package is.vidmot;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
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
    private Image barryRising;
    private Image barryFalling;
    private BooleanProperty isGameOver;

    public Barry(Pane gamePane, BooleanProperty isGameOver) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("barry.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        barry1 = new Image(getClass().getResourceAsStream("/is/vidmot/pics/barry1.png"));
        barry2 = new Image(getClass().getResourceAsStream("/is/vidmot/pics/barry2.png"));
        barryRising = new Image(getClass().getResourceAsStream("/is/vidmot/pics/barry_rising.png"));
        barryFalling = new Image(getClass().getResourceAsStream("/is/vidmot/pics/barry_falling.png"));
        setImage(barry1);

        gamePane.setOnKeyPressed(event -> pressedKeys.put(event.getCode(), true));
        gamePane.setOnKeyReleased(event -> pressedKeys.put(event.getCode(), false));

        animation = new Timeline(
                new KeyFrame(Duration.ZERO, event -> switchImage()),
                new KeyFrame(Duration.millis(150))
        );
        animation.setCycleCount(Timeline.INDEFINITE);
        animation.play();
        this.isGameOver = isGameOver;
    }

    private void updateImage() {
        double maxY = 461 - 30 - getFitHeight();
        if (getTranslateY() < maxY) {
            if (velocityY < 0) {
                setImage(barryRising);
            } else {
                setImage(barryFalling);
            }
        } else {
            if (getImage() != barry1 && getImage() != barry2) {
                setImage(barry1);
            }
        }
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
        updateImage();
    }

    private void switchImage() {
        double maxY = 461 - 30 - getFitHeight();
        if (getImage() == barry1 && getTranslateY() == maxY) {
            setImage(barry2);
        } else if (getImage() == barry2 && getTranslateY() == maxY) {
            setImage(barry1);
        }
    }

    public void toggleAnimation(boolean play) {
        if (!isGameOver.get()) {
            if (play) {
                animation.play();
            } else {
                animation.pause();
            }
        } else {
            animation.pause();
        }
    }

    public double getMaxY() {
        return 461 - 30 - getFitHeight();
    }
}
