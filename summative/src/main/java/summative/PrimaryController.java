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

    // Main Menu
    @FXML
    private ImageView imageView;

    @FXML
    private MenuItem openImage;

    @FXML
    private MenuItem saveImage;

    @FXML
    private MenuItem restoreImage;

    @FXML
    private MenuItem exitProgram;

    // Transformations
    @FXML
    private MenuItem horizontalFlip;

    @FXML
    private MenuItem verticalFlip;

    @FXML
    private MenuItem onRotate;

    // Colors
    @FXML
    private MenuItem onGrayscale;

    @FXML
    private MenuItem onSepiaFilter;

    @FXML
    private MenuItem onInvert;

    @FXML
    private MenuItem onBrightnessAdjust;

    // Effects
    @FXML
    private MenuItem onBulge;

    @FXML
    private MenuItem onColorOverlay;

    @FXML
    private MenuItem onPixelation;

    @FXML
    private MenuItem onVignette;

    @FXML
    private MenuItem onEdgeDetection;

    @FXML
    private MenuItem onEmboss;

    @FXML
    private MenuItem onGaussianBlur;

    // Menu
    @FXML
    void onOpenImage(ActionEvent event) {
        Image defaultImage = new Image(getClass().getResourceAsStream("icon3.jpg")); // REMOVE LATER, FOR TESTING
        imageView.setImage(defaultImage); // REMOVE LATER, FOR TESTING
        originalImage = defaultImage; // REMOVE LATER, FOR TESTING

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
    void onRestoreImage(ActionEvent event) {
        imageView.setImage(originalImage);
    }

    @FXML
    void onExit(ActionEvent event) {
        javafx.application.Platform.exit();
    }

    // Transformations
    @FXML
    void onHorizontalFlip(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                writer.setColor(width - x - 1, y, reader.getColor(x, y));
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

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                writer.setColor(x, height - y - 1, reader.getColor(x, y));
            }
        }
        imageView.setImage(writableImage);
    }

    @FXML
    void onRotate(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        double cx = width / 2;
        double cy = height / 2;
        double angle = Math.toRadians(90);

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double dx = x - cx;
                double dy = y - cy;
                int newX = (int) (dx * Math.cos(angle) - dy * Math.sin(angle) + cx);
                int newY = (int) (dx * Math.sin(angle) + dy * Math.cos(angle) + cy);

                if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                    writer.setColor(newX, newY, reader.getColor(x, y));
                }
            }
        }

        imageView.setImage(writableImage);
    }

    // Original rotate, works with any non-square image:
    // @FXML
    // void onRotate(ActionEvent event) {
    //     int width = (int) imageView.getImage().getWidth();
    //     int height = (int) imageView.getImage().getHeight();

    //     WritableImage writableImage = new WritableImage(height, width);
    //     PixelReader reader = imageView.getImage().getPixelReader();
    //     PixelWriter writer = writableImage.getPixelWriter();

    //     for (int x = 0; x < width; x++) {
    //         for (int y = 0; y < height; y++) {
    //             writer.setColor(height - y - 1, x, reader.getColor(x, y));
    //         }
    //     }

    //     imageView.setImage(writableImage);
    // }

    // Adjustments
    @FXML
    void onGrayscale(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                double red = color.getRed() * 0.21;
                double green = color.getGreen() * 0.71;
                double blue = color.getBlue() * 0.07;

                double grayscale = red + green + blue;
                Color newColor = new Color(grayscale, grayscale, grayscale, color.getOpacity());
                writer.setColor(x, y, newColor);
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

                Color newColor = new Color(newRed, newGreen, newBlue, color.getOpacity());
                writer.setColor(x, y, newColor);
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

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                double red = color.getRed();
                double green = color.getGreen();
                double blue = color.getBlue();

                Color newColor = new Color(1.0 - red, 1.0 - green, 1.0 - blue, color.getOpacity());
                writer.setColor(x, y, newColor);
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void onBrightnessAdjust(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        double factor = 1.2; // adjust here

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);

                // clamp
                double red = Math.min(color.getRed() * factor, 1.0);
                double green = Math.min(color.getGreen() * factor, 1.0); 
                double blue = Math.min(color.getBlue() * factor, 1.0); 

                Color newColor = new Color(red, green, blue, color.getOpacity());
                writer.setColor(x, y, newColor);
            }
        }

        imageView.setImage(writableImage);
    }

    // Filters
    @FXML
    void onBulge(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        double cx = width / 2.0;
        double cy = height / 2.0;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double dx = x - cx;
                double dy = y - cy;
                double radius = Math.sqrt(dx * dx + dy * dy);
                double angle = Math.atan2(dy, dx);
                double newRadius = Math.pow(radius, 1.6) / 30;

                int newX = (int) (cx + newRadius * Math.cos(angle));
                int newY = (int) (cy + newRadius * Math.sin(angle));
                
                if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                    writer.setColor(x, y, reader.getColor(newX, newY));
                }
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void onColorOverlay(ActionEvent event) { // WIP
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        Color overlayColor = new Color(0.36, 0.61, 0.61, 0.5); // Changing opacity doesn't do anything?

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);

                Color newColour = color.interpolate(overlayColor, 0.5);
                writer.setColor(x, y, newColour);
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void onPixelation(ActionEvent event) { // WIP
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        int blockSize = 10; // Adjust for more pixelation effect

        // Lopp through pixels in blocks
        for (int y = 0; y < height; y += blockSize) {
            for (int x = 0; x < width; x += blockSize) {
                Color color = reader.getColor(x, y); // corner color

                // fills block with the corner color
                for (int i = 0; i < blockSize && y + i < height; i++) {
                    for (int j = 0; j < blockSize && x + j < width; j++) {
                        writer.setColor(x + j, y + i, color);
                    }
                }
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void onVignette(ActionEvent event) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        double cx = width / 2;
        double cy = height / 2;
        double maxDistance = Math.sqrt(cx * cx + cy * cy);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double dist = Math.sqrt(Math.pow(x - cx, 2) + Math.pow(y - cy, 2));
                double factor = 1 - (dist / maxDistance);

                factor = Math.max(0.3, factor); // clamping

                Color color = reader.getColor(x, y);
                Color newColor = Color.hsb(color.getHue(), color.getSaturation(), color.getBrightness() * factor, color.getOpacity()); // check this
                writer.setColor(x, y, newColor);
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

        double[][] kernel = {
                { 1, 1, 1 },
                { 1, -7, 1 },
                { 1, 1, 1 }
        };
        int kernelSize = 3;
        int offset = 0;

        for (int y = offset; y < height; y++) {
            for (int x = offset; x < width; x++) {
                double red = 0;
                double green = 0;
                double blue = 0;

                for (int ky = 0; ky < kernelSize && y + ky < height; ky++) {
                    for (int kx = 0; kx < kernelSize  && x + kx < height; kx++) {
                        Color kernelColor = reader.getColor(x + kx - offset, y + ky - offset);
                        red += kernelColor.getRed() * kernel[ky][kx];
                        green += kernelColor.getGreen() * kernel[ky][kx];
                        blue += kernelColor.getBlue() * kernel[ky][kx];
                    }
                }

                // clamp
                red = Math.max(0.0, Math.min(red, 1.0));
                green = Math.max(0.0, Math.min(green, 1.0));
                blue = Math.max(0.0, Math.min(blue, 1.0));

                Color newColor = new Color(red, green, blue, reader.getColor(x, y).getOpacity());

                writer.setColor(x, y, newColor);
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void onEmboss(ActionEvent event) { 
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        double[][] kernel = {
                { 2, -1, 0 },
                { -1, 1, 1 },
                { 0, 1, 2 }
        };

        int kernelSize = 3;
        int offset = 0; 

        for (int y = offset; y < height; y++) {
            for (int x = offset; x < width; x++) {
                double red = 0;
                double green = 0;
                double blue = 0;

                for (int ky = 0; ky < kernelSize && y + ky < height; ky++) {
                    for (int kx = 0; kx < kernelSize  && x + kx < height; kx++) {
                        Color kernelColor = reader.getColor(x + kx - offset, y + ky - offset);
                        red += kernelColor.getRed() * kernel[ky][kx];
                        green += kernelColor.getGreen() * kernel[ky][kx];
                        blue += kernelColor.getBlue() * kernel[ky][kx];
                    }
                }

                // clamp
                red = Math.max(0.0, Math.min(red, 1.0));
                green = Math.max(0.0, Math.min(green, 1.0));
                blue = Math.max(0.0, Math.min(blue, 1.0));

                Color newColor = new Color(red, green, blue, reader.getColor(x, y).getOpacity());

                writer.setColor(x, y, newColor);
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

    // DO NOT REMOVE THIS METHOD!
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}