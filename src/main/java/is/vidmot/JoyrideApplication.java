package is.vidmot;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

/**
 * The type Joyride application.
 */
public class JoyrideApplication extends Application {
    /**
     * The entry point of application.
     *
     * @param args the input arguments
     */
    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(JoyrideApplication.class.getResource("joyride-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 461);
        stage.setTitle("Jetpack Joyride");
        stage.setScene(scene);
        stage.show();
        SoundManager.initialize();
    }
}
