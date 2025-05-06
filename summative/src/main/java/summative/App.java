package summative;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.image.Image;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Parent root = loader.load();

        PrimaryController controller = loader.getController();
        controller.setStage(primaryStage);

        primaryStage.setTitle("Silly Photo Editor");
        primaryStage.getIcons().add(new Image(getClass().getResourceAsStream("icon2.png")));
        primaryStage.setScene(new Scene(root, 750, 750));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
