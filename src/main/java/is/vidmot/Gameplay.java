package is.vidmot;

import javafx.animation.AnimationTimer;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Gameplay extends Pane {
    private Barry barry;
    private AnimationTimer gameLoop;
    private ImageView background1;
    private ImageView background2;
    private double backgroundSpeed = 5;
    private long lastZapperSpawnTime = 0;
    private List<Zapper> zappers = new ArrayList<>();
    private Random random = new Random();
    private static final double ZAPPER_SCALE = 0.75;
    private BooleanProperty isGameOver = new SimpleBooleanProperty(false);

    private ImageView quitButton;
    private ImageView retryButton;

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
        barry = new Barry(this, isGameOver);
        barry.setFitWidth(48);
        barry.setFitHeight(56);
        barry.setX(150);
        getChildren().add(barry);
        setFocusTraversable(true);
        lastZapperSpawnTime = System.currentTimeMillis();
        barry.toggleAnimation(true);
        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateBackground();
                barry.update();
                spawnZappers(now);
                if (checkCollision()) {
                    gameLoop.stop();
                    showGameOverScreen();
                }
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

    private void spawnZappers(long now) {
        double currentTimeInSeconds = System.currentTimeMillis() / 1000.0;
        double lastSpawnTimeInSeconds = lastZapperSpawnTime / 1000.0;

        if (currentTimeInSeconds - lastSpawnTimeInSeconds >= random.nextInt(2) + 3) { // 3-5 secs
            double y = random.nextDouble() * (431 - 15 - 237 * ZAPPER_SCALE) + 15; // Random y
            double angle = random.nextInt(4) * 90; // Random angle (0°, 90°, 180°, 270°)
            if (angle == 180) {
                angle = 45;
            }
            Zapper zapper = new Zapper(840, y, angle);
            zapper.setScaleX(ZAPPER_SCALE);
            zapper.setScaleY(ZAPPER_SCALE);
            zappers.add(zapper);
            getChildren().add(zapper);
            lastZapperSpawnTime = System.currentTimeMillis();
        }

        for (Zapper zapper : zappers) {
            zapper.setTranslateX(zapper.getTranslateX() - backgroundSpeed);
            // despawn
            if (zapper.getTranslateX() + zapper.getChildren().get(0).getBoundsInParent().getWidth() * ZAPPER_SCALE < -240) {
                getChildren().remove(zapper);
            }
        }

        zappers.removeIf(zapper -> zapper.getTranslateX() + zapper.getChildren().get(0).getBoundsInParent().getWidth() * ZAPPER_SCALE < -240);
    }

    private boolean checkCollision() {
        for (Zapper zapper : zappers) {
            Rectangle zapperRectangle = zapper.getRectangle();
            Bounds zapperBounds = zapper.localToScene(zapperRectangle.getBoundsInLocal());
            Rectangle barryBoundsRect = new Rectangle(barry.getBoundsInParent().getMinX(), barry.getBoundsInParent().getMinY(), barry.getBoundsInParent().getWidth(), barry.getBoundsInParent().getHeight() - 10);
            Shape intersection = Shape.intersect(barryBoundsRect, zapperRectangle);
            if (intersection.getBoundsInLocal().getWidth() != -1) {
                return true;
            }
        }
        return false;
    }

    private void showGameOverScreen() {
        isGameOver.set(true);
        Pane overlayPane = new Pane();
        overlayPane.setStyle("-fx-background-color: rgba(0, 0, 0, 0.35);");
        overlayPane.setPrefSize(800, 461);
        getChildren().add(overlayPane);

        Text gameOverText = new Text("Want to go again?");
        gameOverText.setFont(Font.loadFont(getClass().getResourceAsStream("/is/vidmot/fonts/New Athletic M54.ttf"), 40));
        gameOverText.setFill(Color.WHITE);
        gameOverText.setStroke(Color.BLACK);
        gameOverText.setStrokeWidth(2.5);
        gameOverText.setX(240);
        gameOverText.setY(153 + 40);
        overlayPane.getChildren().add(gameOverText);

        quitButton = new ImageView(new Image(getClass().getResourceAsStream("/is/vidmot/pics/quit.png")));
        quitButton.setFitWidth(144);
        quitButton.setFitHeight(50);
        quitButton.setLayoutX(208);
        quitButton.setLayoutY(307);
        quitButton.setOnMouseClicked(event -> System.exit(0));
        overlayPane.getChildren().add(quitButton);

        retryButton = new ImageView(new Image(getClass().getResourceAsStream("/is/vidmot/pics/retry.png")));
        retryButton.setFitWidth(144);
        retryButton.setFitHeight(50);
        retryButton.setLayoutX(448);
        retryButton.setLayoutY(307);
        retryButton.setOnMouseClicked(event -> restartGame());
        overlayPane.getChildren().add(retryButton);

        barry.toggleAnimation(false);
    }

    private void restartGame() {
        getChildren().remove(quitButton);
        getChildren().remove(retryButton);

        background1.setTranslateX(0);
        background2.setTranslateX(background1.getImage().getWidth());

        for (Zapper zapper : zappers) {
            getChildren().remove(zapper);
        }
        zappers.clear();

        barry.setTranslateY(0);

        startGame();
    }
}
