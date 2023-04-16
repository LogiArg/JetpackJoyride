package is.vidmot;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

public class Zapper extends ImageView {
    public enum ZapperType {
        HORIZONTAL,
        VERTICAL
    }

    public Zapper(ZapperType type) {
        Image zapperImage;
        if (type == ZapperType.HORIZONTAL) {
            zapperImage = new Image(getClass().getResourceAsStream("/is/vidmot/pics/d1zap.png"));
        } else {
            zapperImage = new Image(getClass().getResourceAsStream("/is/vidmot/pics/v1zap.png"));
        }
        setImage(zapperImage);
    }
}
