package is.vidmot;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;
import javafx.util.Duration;

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
    private BooleanProperty isGameOver = new SimpleBooleanProperty(false);
    private long lastPatternSpawnTime = 0;
    private List<Coin> coinsToRemove = new ArrayList<>();

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
                checkCollision();
                updateCoins();
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

        if (currentTimeInSeconds - lastSpawnTimeInSeconds >= random.nextInt(2) + 3) {
            double y = random.nextDouble() * (431 - 15 - 237) + 15;
            double angle = random.nextInt(4) * 90;
            if (angle == 180) {
                angle = 45;
            }
            Zapper zapper = new Zapper(840, y, angle);
            zappers.add(zapper);
            getChildren().add(zapper);
            lastZapperSpawnTime = System.currentTimeMillis();
        }

        for (Zapper zapper : zappers) {
            zapper.setTranslateX(zapper.getTranslateX() - backgroundSpeed);
            // despawn
            if (zapper.getTranslateX() + zapper.getChildren().get(0).getBoundsInParent().getWidth() < -240) {
                getChildren().remove(zapper);
            }
        }
        currentTimeInSeconds = System.currentTimeMillis() / 1000.0;
        lastSpawnTimeInSeconds = lastPatternSpawnTime / 1000.0;
        if (currentTimeInSeconds - lastSpawnTimeInSeconds >= random.nextInt(5) + 5) { // 5-10 seconds
            List<List<Coin>> pattern = Coin.loadCoinPattern("/is/vidmot/coin_patterns/pattern1.txt");
            for (List<Coin> row : pattern) {
                for (Coin coin : row) {
                    coin.setTranslateX(coin.getTranslateX() + 840);
                    getChildren().add(coin);
                }
            }
            lastPatternSpawnTime = System.currentTimeMillis();
        }

        zappers.removeIf(zapper -> zapper.getTranslateX() + zapper.getChildren().get(0).getBoundsInParent().getWidth() < -240);
    }

    private void updateCoins() {
        for (Node node : getChildren()) {
            if (node instanceof Coin) {
                Coin coin = (Coin) node;
                coin.setTranslateX(coin.getTranslateX() - backgroundSpeed);

                if (coin.getTranslateX() <= 650 && !coin.animationPlaying() && !coin.animationPlayed()) {
                    coin.animate();
                }
                // smaller coin hitbox
                Rectangle coinHitbox = new Rectangle(
                        coin.getBoundsInParent().getMinX() + 5,
                        coin.getBoundsInParent().getMinY() + 5,
                        coin.getBoundsInParent().getWidth() - 10,
                        coin.getBoundsInParent().getHeight() - 10
                );
                if (barry.getBoundsInParent().intersects(coinHitbox.getBoundsInParent())) {
                    coin.playCoinCollectedAnimation();
                    Timeline removeCoinTimeline = new Timeline(new KeyFrame(Duration.millis(800), e -> {
                        coinsToRemove.add(coin);
                        getChildren().removeAll(coinsToRemove);
                        coinsToRemove.clear();
                    }));
                    removeCoinTimeline.setCycleCount(1);
                    removeCoinTimeline.play();
                }
                if (coin.getTranslateX() + coin.getWidth() < 0) {
                    coinsToRemove.add(coin);
                }
            }
        }
        getChildren().removeAll(coinsToRemove);
        coinsToRemove.clear();
    }


    private boolean checkCollision() {
        for (Zapper zapper : zappers) {
            Rectangle zapperRectangle = zapper.getRectangle();
            Rectangle barryBoundsRect = new Rectangle(barry.getBoundsInParent().getMinX(), barry.getBoundsInParent().getMinY(), barry.getBoundsInParent().getWidth(), barry.getBoundsInParent().getHeight() - 10);
            Shape intersection = Shape.intersect(barryBoundsRect, zapperRectangle);
            if (intersection.getBoundsInLocal().getWidth() != -1) {
                gameLoop.stop();
                GameOver gameOver = new GameOver(() -> restartGame(), () -> System.exit(0));
                getChildren().add(gameOver);
                barry.toggleAnimation(false);
                return true;
            }
        }
        return false;
    }

    private void restartGame() {
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
