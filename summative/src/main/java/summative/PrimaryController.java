package summative;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

public class PrimaryController {

    private Stage stage;
    private Image originalImage; // Use this to keep track of the original image

    @FXML
    private ImageView imageView;

    @FXML
    private MenuItem openImage;

    @FXML
    private MenuItem saveImage;

    @FXML
    private MenuItem undoChanges;

    @FXML
    private MenuItem redoChanges;

    @FXML
    private MenuItem resetImage;

    @FXML
    private MenuItem horizontalFlip;

    @FXML
    private MenuItem verticalFlip;

    @FXML
    private MenuItem onClockwiseRotate;

    @FXML
    private MenuItem onCounterclockwiseRotate;

    @FXML
    private MenuItem on180DegreesRotate;

    @FXML
    private MenuItem onBrightnessAdjust;

    @FXML
    private MenuItem onSaturationAdjust;

    @FXML
    private MenuItem onGrayscale;

    @FXML
    private MenuItem onInvert;

    @FXML
    private MenuItem onWarmFilter;

    @FXML
    private MenuItem onSepiaFilter;

    @FXML
    private MenuItem onEdgeDetection;

    @FXML
    private MenuItem onGaussianBlur;

    @FXML
    private MenuItem onPixelate;

    // File
    @FXML
    void onOpenImage(ActionEvent event) {
        imageView.setImage(new Image(getClass().getResourceAsStream("icon3.jpg"))); // REMOVE LATER, FOR TESTING

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Open Image File");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.bmp", "*.gif"));

        try {
            File file = fileChooser.showOpenDialog(stage);
            if (file != null) {
                Image image = new Image(file.toURI().toString());
                originalImage = image;
                imageView.setImage(image);
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Image Load Failed");
            alert.setContentText("There was a problem opening your image");
            alert.showAndWait();
        }
    }

    @FXML
    public void onSaveImage(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Save Image");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("PNG files", "*.png"));
        File file = fileChooser.showSaveDialog(imageView.getScene().getWindow());

        if (file != null) {
            WritableImage writableImage = imageView.snapshot(null, null);
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(writableImage, null), "png", file);
            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Image Save Failed");
                alert.setContentText("There was a problem saving your image");
                alert.showAndWait();
            }
        }
    }

    @FXML
    void onUndo(ActionEvent event) { // WIP
        imageView.setImage(originalImage);
    }

    @FXML
    void onRedo(ActionEvent event) { // WIP
        imageView.setImage(originalImage);
    }

    @FXML
    void onResetImage(ActionEvent event) {
        imageView.setImage(originalImage);
    }

    // Transformations
    @FXML
    void onHorizontalFlip(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                writer.setColor(width - i - 1, j, reader.getColor(i, j));
            }
        }
        imageView.setImage(writableImage);
    }

    @FXML
    void onVerticalFlip(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                writer.setColor(i, height - j - 1, reader.getColor(i, j));
            }
        }
        imageView.setImage(writableImage);
    }

    @FXML
    void onClockwiseRotate(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(height, width);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                writer.setColor(height - j - 1, i, reader.getColor(i, j));
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void onCounterclockwiseRotate(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(height, width);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                writer.setColor(j, width - i - 1, reader.getColor(i, j));
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void on180DegreesRotate(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                writer.setColor(width - i - 1, height - j - 1, reader.getColor(i, j));
            }
        }
        imageView.setImage(writableImage);
    }

    // Adjustments
    @FXML
    void onBrightnessAdjust(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        double factor = 1.2;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color = reader.getColor(j, i);
                double hue = color.getHue();
                double saturation = color.getSaturation();
                double brightness = color.getBrightness();

                brightness = Math.min(1.0, Math.max(0.0, brightness * factor)); // brightness can only be from 0 to 1
                Color newColor = Color.hsb(hue, saturation, brightness, color.getOpacity());
                writer.setColor(j, i, newColor);
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void onSaturationAdjust(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        double factor = 1.2;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color = reader.getColor(j, i);
                double hue = color.getHue();
                double saturation = color.getSaturation();
                double brightness = color.getBrightness();

                saturation = Math.min(1.0, Math.max(0.0, saturation * factor)); // saturation can only be from 0 to 1
                Color newColor = Color.hsb(hue, saturation, brightness, color.getOpacity());
                writer.setColor(j, i, newColor);
            }
        }

        imageView.setImage(writableImage);
    }

    // Filters
    @FXML
    void onGreyscale(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color = reader.getColor(j, i);
                double red = color.getRed();
                double green = color.getGreen();
                double blue = color.getBlue();

                double grayscale = (red + green + blue) / 3.0;
                Color newColor = new Color(grayscale, grayscale, grayscale, color.getOpacity());
                writer.setColor(j, i, newColor);
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void onInvert(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                Color color = reader.getColor(j, i);
                double red = color.getRed();
                double green = color.getGreen();
                double blue = color.getBlue();

                Color newColor = new Color(1.0 - red, 1.0 - green, 1.0 - blue, color.getOpacity());
                writer.setColor(j, i, newColor);
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void onWarmFilter(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                double red = color.getRed();
                double green = color.getGreen();
                double blue = color.getBlue();

                // Sepia filter formula
                double newRed = Math.min(1.0, 1 * red + 0.8 * green + 0.8 * blue);
                double newGreen = Math.min(1.0, red + green + blue);
                double newBlue = Math.min(1.0, red + green + blue);

                writer.setColor(x, y, new Color(newRed, newGreen, newBlue, color.getOpacity()));
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void onSepiaFilter(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                double red = color.getRed();
                double green = color.getGreen();
                double blue = color.getBlue();

                // Sepia filter formula
                double newRed = Math.min(1.0, 0.393 * red + 0.769 * green + 0.189 * blue);
                double newGreen = Math.min(1.0, 0.349 * red + 0.686 * green + 0.168 * blue);
                double newBlue = Math.min(1.0, 0.272 * red + 0.534 * green + 0.131 * blue);

                writer.setColor(x, y, new Color(newRed, newGreen, newBlue, color.getOpacity()));
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void onGaussianBlur(ActionEvent event) { // WIP
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                writer.setColor(x, y, reader.getColor(x, y));
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void onEdgeDetection(ActionEvent event) { // WIP
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                writer.setColor(x, y, reader.getColor(x, y));
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void onPixelate(ActionEvent event) { // WIP
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                writer.setColor(x, y, reader.getColor(x, y));
            }
        }

        imageView.setImage(writableImage);
    }

    /*
      * Accessing a pixels colors
      * 
      * Color color = reader.getColor(x, y);
      * double red = color.getRed();
      * double green = color.getGreen();
      * double blue = color.getBlue();
      */
 
     /*
      * Modifying a pixels colors
      * 
      * Color newColor = new Color(1.0 - red, 1.0 - green, 1.0 - blue,
      * color.getOpacity());
      */

    // DO NOT REMOVE THIS METHOD!
    public void setStage(Stage stage) {
        this.stage = stage;

        // // Keybinds??
        // stage.setOnKeyPressed(new Eventhan -> {
        //     switch (event.getCode()) {
        //         case S: // Save (Ctrl+s)
        //             if (event.isControlDown()) {
        //                 onSaveImage(new ActionEvent());
        //             }
        //             break;
        //         default:
        //             break;
        //     }
        // });
    }
}
