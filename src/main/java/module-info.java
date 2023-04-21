module is.vidmot {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.media;

    opens is.vidmot to javafx.fxml;
    exports is.vidmot;
}
