package is.vidmot;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.util.Objects;

/**
 * The type Game over.
 */
public class GameOver extends Pane {
    /**
     * The Quit button.
     */
    private ImageView quitButton;
    /**
     * The Retry button.
     */
    private ImageView retryButton;

    /**
     * Instantiates a new Game over.
     *
     * @param onRestart the on restart
     * @param onQuit    the on quit
     */
    public GameOver(Runnable onRestart, Runnable onQuit) {
        showGameOverScreen(onRestart, onQuit);
    }

    /**
     * Show game over screen.
     *
     * @param onRestart the on restart
     * @param onQuit    the on quit
     */
    private void showGameOverScreen(Runnable onRestart, Runnable onQuit) {
        Pane overlayPane = new Pane();
        overlayPane.setStyle("-fx-background-color:rgba(0,0,0,0.35);");
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

        quitButton = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/is/vidmot/pics/quit.png"))));
        quitButton.setFitWidth(144);
        quitButton.setFitHeight(50);
        quitButton.setLayoutX(208);
        quitButton.setLayoutY(307);
        quitButton.setOnMouseClicked(event -> onQuit.run());
        overlayPane.getChildren().add(quitButton);

        retryButton = new ImageView(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/is/vidmot/pics/retry.png"))));
        retryButton.setFitWidth(144);
        retryButton.setFitHeight(50);
        retryButton.setLayoutX(448);
        retryButton.setLayoutY(307);
        retryButton.setOnMouseClicked(event -> onRestart.run());
        overlayPane.getChildren().add(retryButton);
    }
}
