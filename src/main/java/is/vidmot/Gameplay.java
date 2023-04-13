package is.vidmot;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class Gameplay extends Pane {
    private Barry barry;
    private AnimationTimer gameLoop;
    private ImageView background1;
    private ImageView background2;
    private double backgroundSpeed = 5;

    public Gameplay() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("gameplay-view.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);
        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException(exception);
        }
        startGame();
    }

    private void startGame() {
        createBackground();
        barry = new Barry(this);
        barry.setFitWidth(48);
        barry.setFitHeight(56);
        barry.setX(150);
        getChildren().add(barry);
        setFocusTraversable(true);
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateBackground();
                barry.update();
            }
        };
        gameLoop.start();
    }

    private void createBackground() {
        Image backgroundImage = new Image(getClass().getResourceAsStream("/is/vidmot/pics/SectorStart.png"));
        background1 = new ImageView(backgroundImage);
        background2 = new ImageView(backgroundImage);
        background2.setTranslateX(background1.getImage().getWidth());
        getChildren().addAll(background1, background2);
    }

    private void updateBackground() {
        background1.setTranslateX(background1.getTranslateX() - backgroundSpeed);
        background2.setTranslateX(background2.getTranslateX() - backgroundSpeed);

        if (background1.getTranslateX() <= -background1.getImage().getWidth()) {
            background1.setTranslateX(background2.getTranslateX() + background1.getImage().getWidth());
        }

        if (background2.getTranslateX() <= -background2.getImage().getWidth()) {
            background2.setTranslateX(background1.getTranslateX() + background2.getImage().getWidth());
        }
    }
}
