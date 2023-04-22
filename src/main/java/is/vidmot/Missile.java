package is.vidmot;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;
import javafx.util.Duration;

public class Missile extends Pane {
    private ImageView imageView;

    public Missile(double x, double y) {
        Image missileImage = new Image(getClass().getResourceAsStream("/is/vidmot/pics/missile/missile1.png"));
        imageView = new ImageView(missileImage);
        imageView.setFitWidth(85);
        imageView.setFitHeight(65);
        getChildren().add(imageView);
        setTranslateX(x);
        setTranslateY(y);
        Timeline animation = new Timeline();
        for (int i = 1; i <= 14; i++) {
            Image image = new Image(getClass().getResourceAsStream("/is/vidmot/pics/missile/missile" + i + ".png"));
            KeyFrame keyFrame = new KeyFrame(Duration.seconds(i * 0.05), e -> imageView.setImage(image));
            animation.getKeyFrames().add(keyFrame);
        }
        animation.setCycleCount(Animation.INDEFINITE);
        animation.play();
    }

    public ImageView getImageView() {
        return imageView;
    }

    public Rectangle getHitbox() {
        Bounds bounds = imageView.getBoundsInParent();
        double reducedWidth = bounds.getWidth() * 0.7;
        double reducedHeight = bounds.getHeight() * 0.6;

        return new Rectangle(
                getTranslateX() + (bounds.getWidth() - reducedWidth) / 2,
                getTranslateY() + (bounds.getHeight() - reducedHeight) / 2,
                reducedWidth,
                reducedHeight
        );
    }
}
