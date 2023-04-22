package is.vidmot;

import javafx.animation.AnimationTimer;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
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
    private List<Zapper> zappers = new ArrayList<>();
    private Random random = new Random();
    private BooleanProperty isGameOver = new SimpleBooleanProperty(false);
    private List<Coin> coinsToRemove = new ArrayList<>();
    private List<Missile> missiles = new ArrayList<>();
    private long lastSpawnTime = 0;
    private IntegerProperty coinCount = new SimpleIntegerProperty(0);
    private IntegerProperty meterCount = new SimpleIntegerProperty(0);
    private int backgroundPosX = 0;

    private ImageView background1;
    private ImageView background2;
    private double backgroundSpeed = 3;
    private BackgroundStyle currentBackgroundStyle;
    private int currentBackgroundIndex;

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

        if (barry != null) {
            getChildren().remove(barry);
        }

        barry = new Barry(this, isGameOver);
        barry.setFitWidth(48);
        barry.setFitHeight(56);
        barry.setX(150);
        barry.setTranslateY(barry.getMaxY());
        getChildren().add(barry);
        setFocusTraversable(true);
        barry.toggleAnimation(true);

        gameLoop = new AnimationTimer() {
            @Override
            public void handle(long now) {
                updateBackground();
                barry.update();
                checkCollision();
                spawnRandomItem(now);
                updateZappers();
                updateCoins();
                updateMissiles();
                double pixelsPerMeter = 6000.0 / 500.0;
                int metersTraveled = (int) ((backgroundPosX % 6000) / pixelsPerMeter);
                meterCount.set(metersTraveled);
                updateBackgroundSpeed();
            }
        };
        gameLoop.start();
    }

    private void updateBackgroundSpeed() {
        double maxSpeed = 10;
        double minSpeed = 3;
        int maxMeters = 5000;

        if (meterCount.get() < maxMeters) {
            backgroundSpeed = minSpeed + (maxSpeed - minSpeed) * meterCount.get() / maxMeters;
        } else {
            backgroundSpeed = maxSpeed;
        }
    }

    private void createBackground() {
        currentBackgroundStyle = BackgroundStyle.SECTOR;
        currentBackgroundIndex = 2;
        loadImageAndSetBackgrounds();
    }

    private void loadImageAndSetBackgrounds() {
        String backgroundPath = String.format("/is/vidmot/pics/background/%s%d.png", currentBackgroundStyle, currentBackgroundIndex);
        Image backgroundImage = new Image(getClass().getResourceAsStream(backgroundPath));
        if (background1 == null || background2 == null) {
            background1 = new ImageView(backgroundImage);
            background2 = new ImageView(backgroundImage);
            background2.setTranslateX(background1.getImage().getWidth());
            getChildren().addAll(background1, background2);
        } else {
            if (background1.getTranslateX() < background2.getTranslateX()) {
                background2.setImage(backgroundImage);
            } else {
                background1.setImage(backgroundImage);
            }
        }
    }

    private void updateBackground() {
        background1.setTranslateX(background1.getTranslateX() - backgroundSpeed);
        background2.setTranslateX(background2.getTranslateX() - backgroundSpeed);
        backgroundPosX += backgroundSpeed;
        if (background1.getTranslateX() <= -background1.getImage().getWidth()) {
            background1.setTranslateX(background2.getTranslateX() + background2.getImage().getWidth());
            nextBackground();
        } else if (background2.getTranslateX() <= -background2.getImage().getWidth()) {
            background2.setTranslateX(background1.getTranslateX() + background1.getImage().getWidth());
            nextBackground();
        }
    }

    private void nextBackground() {
        currentBackgroundIndex++;

        if (currentBackgroundIndex > 3) {
            currentBackgroundIndex = 1;
            BackgroundStyle[] styles = BackgroundStyle.values();
            BackgroundStyle nextStyle;
            do {
                nextStyle = styles[new Random().nextInt(styles.length)];
            } while (nextStyle == currentBackgroundStyle);
            currentBackgroundStyle = nextStyle;
        }

        loadImageAndSetBackgrounds();
    }

    private void spawnRandomItem(long now) {
        double currentTimeInSeconds = System.currentTimeMillis() / 1000.0;
        double lastSpawnTimeInSeconds = lastSpawnTime / 1000.0;
        if (currentTimeInSeconds - lastSpawnTimeInSeconds >= random.nextInt(5) + 3) {
            int choice = random.nextInt(3);
            switch (choice) {
                case 0:
                    spawnMissiles(now);
                    break;
                case 1:
                    spawnZappers(now);
                    break;
                case 2:
                    spawnCoins(now);
                    break;
            }
            lastSpawnTime = System.currentTimeMillis();
        }
    }

    private void spawnZappers(long now) {
        double y = random.nextDouble() * (431 - 15 - 237) + 15;
        double angle = random.nextInt(4) * 90;
        if (angle == 180) {
            angle = 45;
        }
        Zapper newZapper = new Zapper(840, y, angle);
        zappers.add(newZapper);
        getChildren().add(newZapper);
    }

    private void updateZappers() {
        for (Zapper zapper : zappers) {
            zapper.setTranslateX(zapper.getTranslateX() - backgroundSpeed);
            // despawn
            if (zapper.getTranslateX() + zapper.getChildren().get(0).getBoundsInParent().getWidth() < -240) {
                getChildren().remove(zapper);
            }
        }
        zappers.removeIf(zapper -> zapper.getTranslateX() + zapper.getChildren().get(0).getBoundsInParent().getWidth() < -240);
    }

    private void spawnCoins(long now) {
        int randomPatternNumber = random.nextInt(11) + 1;
        String patternPath = "/is/vidmot/coin_patterns/pattern" + randomPatternNumber + ".txt";

        List<List<Coin>> pattern = Coin.loadCoinPattern(patternPath);
        for (List<Coin> row : pattern) {
            for (Coin coin : row) {
                coin.setTranslateX(coin.getTranslateX() + 840);
                getChildren().add(coin);
            }
        }
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
                        coin.getBoundsInParent().getMinX() + 3,
                        coin.getBoundsInParent().getMinY() + 3,
                        coin.getBoundsInParent().getWidth() - 8,
                        coin.getBoundsInParent().getHeight() - 8
                );
                if (barry.getBoundsInParent().intersects(coinHitbox.getBoundsInParent()) && !coin.isCollected()) {
                    coin.playCoinSound();
                    coin.playCoinCollectedAnimation();
                    coin.setCollected(true);
                    coinCount.set(coinCount.get() + 1);
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

    private void spawnMissiles(long now) {
        double y = random.nextDouble() * (431 - 15 - 237) + 15;
        ImageView warningView = new ImageView();
        warningView.setFitWidth(50);
        warningView.setFitHeight(50);
        warningView.setX(800 - 50);
        warningView.setY(y);
        getChildren().add(warningView);

        AnimationTimer trackingTimer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                double deltaY = barry.getTranslateY() - warningView.getY();
                warningView.setY(warningView.getY() + deltaY * 0.01);
            }
        };

        Timeline warningTimeline = new Timeline();
        for (int i = 0; i < 10; i++) {
            Image warningImage1 = new Image(getClass().getResourceAsStream("/is/vidmot/pics/missile/warning1.png"));
            Image warningImage2 = new Image(getClass().getResourceAsStream("/is/vidmot/pics/missile/warning2.png"));
            KeyFrame keyFrame1 = new KeyFrame(Duration.millis(i * 250), e -> warningView.setImage(warningImage1));
            KeyFrame keyFrame2 = new KeyFrame(Duration.millis(i * 250 + 125), e -> warningView.setImage(warningImage2));
            warningTimeline.getKeyFrames().addAll(keyFrame1, keyFrame2);
        }
        warningTimeline.setCycleCount(1);
        warningTimeline.setOnFinished(e -> trackingTimer.stop());
        warningTimeline.play();
        trackingTimer.start();

        Timeline imminentWarningTimeline = new Timeline();
        for (int i = 1; i <= 3; i++) {
            Image imminentWarningImage = new Image(getClass().getResourceAsStream("/is/vidmot/pics/missile/incoming" + i + ".png"));
            KeyFrame keyFrame = new KeyFrame(Duration.millis(i * 224), e -> warningView.setImage(imminentWarningImage));
            imminentWarningTimeline.getKeyFrames().add(keyFrame);
        }
        KeyFrame keyFrame = new KeyFrame(Duration.millis(3 * 240 - 670), e -> SoundManager.playWarningSound());
        imminentWarningTimeline.getKeyFrames().add(keyFrame);
        imminentWarningTimeline.setOnFinished(e -> {
            Missile missile = new Missile(840, warningView.getY());
            getChildren().add(missile);
            missiles.add(missile);
            getChildren().remove(warningView);
            SoundManager.playMissileLaunchSound();
        });
        imminentWarningTimeline.setDelay(Duration.seconds(2.5));
        imminentWarningTimeline.setCycleCount(1);
        imminentWarningTimeline.play();
    }

    private void updateMissiles() {
        for (Missile missile : missiles) {
            missile.setTranslateX(missile.getTranslateX() - backgroundSpeed * 2);
            if (missile.getTranslateX() + missile.getImageView().getFitWidth() < 0) {
                getChildren().remove(missile);
            }
        }
        missiles.removeIf(missile -> missile.getTranslateX() + missile.getImageView().getFitWidth() < 0);
    }

    private boolean checkCollision() {
        for (Zapper zapper : zappers) {
            Rectangle zapperRectangle = zapper.getRectangle();
            Rectangle barryBoundsRect = new Rectangle(barry.getBoundsInParent().getMinX(), barry.getBoundsInParent().getMinY(), barry.getBoundsInParent().getWidth(), barry.getBoundsInParent().getHeight() - 10);
            Shape intersection = Shape.intersect(barryBoundsRect, zapperRectangle);
            if (intersection.getBoundsInLocal().getWidth() != -1) {
                return gameOver();
            }
        }

        for (Missile missile : missiles) {
            Rectangle missileHitbox = missile.getHitbox();
            Rectangle barryBoundsRect = new Rectangle(barry.getBoundsInParent().getMinX(), barry.getBoundsInParent().getMinY(), barry.getBoundsInParent().getWidth(), barry.getBoundsInParent().getHeight() - 10);
            Shape intersection = Shape.intersect(barryBoundsRect, missileHitbox);
            if (intersection.getBoundsInLocal().getWidth() != -1) {
                return gameOver();
            }
        }

        return false;
    }

    private boolean gameOver() {
        gameLoop.stop();
        GameOver gameOver = new GameOver(() -> restartGame(), () -> System.exit(0));
        getChildren().add(gameOver);
        barry.toggleAnimation(false);
        return true;
    }

    private void restartGame() {
        getChildren().removeAll(zappers);
        zappers.clear();

        removeCoins();

        for (Missile missile : missiles) {
            getChildren().remove(missile);
        }
        missiles.clear();

        backgroundSpeed = 3;
        coinCount.set(0);
        meterCount.set(0);
        backgroundPosX = 0;
        barry.setTranslateY(0);
        resetBackground();

        getChildren().removeIf(node -> node instanceof GameOver);

        startGame();
    }

    private void removeCoins() {
        List<Coin> coins = new ArrayList<>();
        for (Node node : getChildren()) {
            if (node instanceof Coin) {
                coins.add((Coin) node);
            }
        }
        getChildren().removeAll(coins);
    }

    private void resetBackground() {
        currentBackgroundStyle = BackgroundStyle.SECTOR;
        currentBackgroundIndex = 2;
        loadImageAndSetBackgrounds();
        background1.setTranslateX(0);
        background2.setTranslateX(background1.getImage().getWidth());
    }

    public IntegerProperty coinCountProperty() {
        return coinCount;
    }

    public double getBackgroundSpeed() {
        return backgroundSpeed;
    }

    public IntegerProperty meterCountProperty() {
        return meterCount;
    }
}
