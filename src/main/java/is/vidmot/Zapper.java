package is.vidmot;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Rectangle;

public class Zapper extends Pane {

    private ImageView imageView;
    private Rectangle rectangle;

    public Zapper(double x, double y, double angle) {
        Image zapperImage = new Image(getClass().getResourceAsStream("/is/vidmot/pics/tempzap/v1zap.png"));
        imageView = new ImageView(zapperImage);
        imageView.setFitWidth(87);
        imageView.setFitHeight(237);
        getChildren().add(imageView);

        rectangle = new Rectangle(87, 237);
        rectangle.setVisible(false);
        getChildren().add(rectangle);

        setTranslateX(x);
        setTranslateY(y);
        setRotate(angle);
    }

    public Rectangle getRectangle() {
        return rectangle;
    }
}
