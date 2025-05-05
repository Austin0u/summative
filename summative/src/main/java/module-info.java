module summative {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.swing;
    requires transitive javafx.graphics; // addresses warnings

    opens summative to javafx.fxml;

    exports summative;
}
