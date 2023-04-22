package is.vidmot;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

import java.util.Objects;

/**
 * The type Zapper.
 */
public class Zapper extends Pane {
    /**
     * The Image view.
     */
    private final ImageView imageView;
    /**
     * The Rectangle.
     */
    private final Rectangle rectangle;
    /**
     * The Current image index.
     */
    private int currentImageIndex = 1;
    /**
     * The Zapper animation.
     */
    private Timeline zapperAnimation;

    /**
     * Instantiates a new Zapper.
     *
     * @param x     the x
     * @param y     the y
     * @param angle the angle
     */
    public Zapper(double x, double y, double angle) {
        Image zapperImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/is/vidmot/pics/zap/zap1.png")));
        imageView = new ImageView(zapperImage);
        imageView.setFitWidth(55);
        imageView.setFitHeight(165);
        getChildren().add(imageView);
        rectangle = new Rectangle(55 * 0.8, 165 * 0.8);
        rectangle.setTranslateX(0.1 * 55);
        rectangle.setTranslateY(0.1 * 165);
        rectangle.setVisible(false);
        getChildren().add(rectangle);
        setTranslateX(x);
        setTranslateY(y);
        setRotate(angle);

        startZapperAnimation();
    }

    /**
     * Gets rectangle.
     *
     * @return the rectangle
     */
    public Rectangle getRectangle() {
        return rectangle;
    }

    /**
     * Start zapper animation.
     */
    private void startZapperAnimation() {
        zapperAnimation = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            currentImageIndex++;
            if (currentImageIndex > 5) {
                currentImageIndex = 1;
            }
            Image newZapperImage = new Image(Objects.requireNonNull(getClass().getResourceAsStream("/is/vidmot/pics/zap/zap" + currentImageIndex + ".png")));
            imageView.setImage(newZapperImage);
        }));
        zapperAnimation.setCycleCount(Timeline.INDEFINITE);
        zapperAnimation.play();
    }
}
