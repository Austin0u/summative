package summative;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class BrightnessSliderGUI extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) {
        VBox mainPane = new VBox();
        Scene scene = new Scene(mainPane, 400, 100);
        Slider number = new Slider(-1, 1, 0);
        Label label = new Label("Select Brightness Factor:");
        Label value = new Label("");

        number.setShowTickMarks(true);
        number.setShowTickLabels(true);
        number.setMajorTickUnit(0.5);
        number.setBlockIncrement(0.25);
        number.setSnapToTicks(true);

        number.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov, Number oldValue, Number newValue) {
                value.setText(newValue.toString());
            }
        });

        mainPane.getChildren().add(label);
        mainPane.getChildren().add(number);
        mainPane.getChildren().add(value);

        primaryStage.setTitle("Adjust Brightness");
        primaryStage.setScene(scene);
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon3.jpg")));
        primaryStage.show();
    }
}
