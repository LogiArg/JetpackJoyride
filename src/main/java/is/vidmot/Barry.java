package is.vidmot;

import javafx.fxml.FXMLLoader;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.Pane;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Barry extends ImageView {
    private static final double GRAVITY = 0.25;
    private static final double JETPACK_FORCE = -0.5;

    private double velocityY = 0;
    private Map<KeyCode, Boolean> pressedKeys = new HashMap<>();

    public Barry(Pane gamePane) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("barry.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }

        gamePane.setOnKeyPressed(event -> pressedKeys.put(event.getCode(), true));
        gamePane.setOnKeyReleased(event -> pressedKeys.put(event.getCode(), false));
    }

    public void update() {
        double minY = 15; // Limit at the top
        double maxY = 461 - 30 - getFitHeight(); // Limit at the bottom

        if (pressedKeys.getOrDefault(KeyCode.SPACE, false)) {
            velocityY += JETPACK_FORCE;
        } else {
            velocityY += GRAVITY;
        }

        double newY = getTranslateY() + velocityY;

        // Limit the vertical position of Barry
        if (newY <= minY) {
            newY = minY;
            velocityY = 0;
        } else if (newY >= maxY) {
            newY = maxY;
            velocityY = 0;
        }

        setTranslateY(newY);
    }

}
