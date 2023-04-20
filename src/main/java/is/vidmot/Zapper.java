package is.vidmot;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Zapper extends Pane {
    private ImageView imageView;
    private Rectangle rectangle;
    private int currentImageIndex = 1;
    private Timeline zapperAnimation;

    public Zapper(double x, double y, double angle) {
        Image zapperImage = new Image(getClass().getResourceAsStream("/is/vidmot/pics/zap/zap1.png"));
        imageView = new ImageView(zapperImage);
        imageView.setFitWidth(55);
        imageView.setFitHeight(165);
        getChildren().add(imageView);
        rectangle = new Rectangle(55 * 0.8, 165 * 0.8); // Adjust hitbox size here
        rectangle.setTranslateX(0.1 * 55); // Adjust hitbox X position
        rectangle.setTranslateY(0.1 * 165); // Adjust hitbox Y position
        rectangle.setVisible(false);
        getChildren().add(rectangle);
        setTranslateX(x);
        setTranslateY(y);
        setRotate(angle);

        // Start the zapper animation
        startZapperAnimation();
    }

    public Rectangle getRectangle() {
        return rectangle;
    }

    private void startZapperAnimation() {
        zapperAnimation = new Timeline(new KeyFrame(Duration.millis(200), event -> {
            currentImageIndex++;
            if (currentImageIndex > 5) {
                currentImageIndex = 1;
            }
            Image newZapperImage = new Image(getClass().getResourceAsStream("/is/vidmot/pics/zap/zap" + currentImageIndex + ".png"));
            imageView.setImage(newZapperImage);
        }));
        zapperAnimation.setCycleCount(Timeline.INDEFINITE);
        zapperAnimation.play();
    }
}
