package summative;

import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.EventHandler;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class PrimaryController {

    private Stage stage;
    private Image originalImage; // Use this to keep track of the original image
    private Image previousImage; // For previewing

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
    private MenuItem onSwirl;

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

    // Other
    private boolean isImageLoaded() { // check and popup error
        if (imageView.getImage() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("No Image Loaded");
            alert.setContentText("Please load an image first.");
            alert.showAndWait();
            return false;
        }
        return true;
    }

    private void restorePreviousImage() {
        if (previousImage != null) {
            imageView.setImage(previousImage);
        }
    }

    // Menu
    @FXML
    void onOpenImage(ActionEvent event) {
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
        // Image check
        if (!isImageLoaded()) {
            return;
        }

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
        // Image check
        if (!isImageLoaded()) {
            return;
        }

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
        // Image check
        if (!isImageLoaded()) {
            return;
        }

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
        // Image check
        if (!isImageLoaded()) {
            return;
        }
        
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        double cx = width / 2.0; 
        double cy = height / 2.0; 
        double angle = Math.toRadians(90); // -90 for counterclockwise 

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                double dx = x - cx; 
                double dy = y - cy; 
                int newX = (int) Math.round(dx * Math.cos(angle) - dy * Math.sin(angle) + cx); // fix was round?                                                      // better accuracy
                int newY = (int) Math.round(dx * Math.sin(angle) + dy * Math.cos(angle) + cy); 

                if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                    writer.setColor(newX, newY, reader.getColor(x, y));
                }
            }
        }

        imageView.setImage(writableImage);
    }

    // Alternative rotate, works with any non-square image:
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
        // Image check
        if (!isImageLoaded()) {
            return;
        }

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

                double gray = red * 0.21 + green * 0.71 + blue * 0.07; // sum
                Color newColor = new Color(gray, gray, gray, color.getOpacity());
                writer.setColor(x, y, newColor);
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void onSepiaFilter(ActionEvent event) {
        // Image check
        if (!isImageLoaded()) {
            return;
        }

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
                double newRed = Math.min(0.393 * red + 0.769 * green + 0.189 * blue,1.0);
                double newGreen = Math.min(0.349 * red + 0.686 * green + 0.168 * blue, 1.0);
                double newBlue = Math.min(0.272 * red + 0.534 * green + 0.131 * blue, 1.0);

                Color newColor = new Color(newRed, newGreen, newBlue, color.getOpacity());
                writer.setColor(x, y, newColor);
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void onInvert(ActionEvent event) {
        // Image check
        if (!isImageLoaded()) {
            return;
        }

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
    private void onBrightnessAdjust(ActionEvent event) {
        if (!isImageLoaded()) {
            return;
        }

        previousImage = imageView.getImage(); // Store previous image for previous

        // Elements
        Stage dialog = new Stage();
        dialog.setTitle("Adjust Brightness");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("icon3.jpg")));

        dialog.initModality(Modality.APPLICATION_MODAL); // lock interaction
        dialog.setResizable(false);

        Slider slider = new Slider(0.0, 2.0, 1.0); 
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(0.5);
        slider.setBlockIncrement(0.1);

        CheckBox previewToggle = new CheckBox("Preview");
        previewToggle.setSelected(false);

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        // Functionality
        dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                restorePreviousImage(); // Cancels if 'x' is clicked
                dialog.close();
            }
        });

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                restorePreviousImage(); // Reset back to original (so effect doesn't stack)
                adjustBrightness(slider.getValue());
                dialog.close();
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                restorePreviousImage(); // Restore to the image before if cancelled
                dialog.close();
            }
        });

        previewToggle.selectedProperty().addListener(new ChangeListener<Boolean>() { // shows effect without having to change slider
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                restorePreviousImage();

                if (newValue) {
                    adjustBrightness(slider.getValue()); 
                } 
            }
        });

        slider.valueProperty().addListener(new ChangeListener<Number>() { 
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                restorePreviousImage(); // reset

                if (previewToggle.isSelected()) {
                    adjustBrightness((double) newValue); 
                }
            }
        });

        // Arrangement
        VBox popup = new VBox(10, slider);
        popup.setAlignment(Pos.CENTER);
        popup.setPadding(new Insets(10));

        HBox options = new HBox(10, previewToggle, cancelButton, confirmButton);
        options.setAlignment(Pos.BOTTOM_RIGHT); 

        popup.getChildren().add(options);

        Scene scene = new Scene(popup, 300, 100);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void adjustBrightness(double sliderValue) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        double factor = sliderValue - 1; // since slider is 0-2

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);
                double red = color.getRed();
                double green = color.getGreen();
                double blue = color.getBlue();

                // clamp
                double newRed = Math.max(Math.min(red * (1 + factor), 1.0), 0.0);
                double newGreen = Math.max(Math.min(green * (1 + factor), 1.0), 0.0);
                double newBlue = Math.max(Math.min(blue * (1 + factor), 1.0), 0.0);

                Color newColor = new Color(newRed, newGreen, newBlue, color.getOpacity());
                writer.setColor(x, y, newColor);
            }
        }

        imageView.setImage(writableImage);
    }

    // Effects
    @FXML
    void onBulge(ActionEvent event) {
        if (!isImageLoaded()) {
            return;
        }

        previousImage = imageView.getImage(); // Store previous image for previous

        // Elements
        Stage dialog = new Stage();
        dialog.setTitle("Apply Bulge");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("icon3.jpg")));

        dialog.initModality(Modality.APPLICATION_MODAL); // lock interaction
        dialog.setResizable(false);

        Slider slider = new Slider(0.5, 2.5, 1.5);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(0.5);
        slider.setBlockIncrement(0.1);

        CheckBox previewToggle = new CheckBox("Preview");
        previewToggle.setSelected(false);

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        // Functionality
        dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                restorePreviousImage(); // Cancels if 'x' is clicked
                dialog.close();
            }
        });

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                restorePreviousImage(); // Reset back to original (so effect doesn't stack)
                applyBulge(slider.getValue());
                dialog.close();
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                restorePreviousImage(); // Restore to the image before if cancelled
                dialog.close();
            }
        });

        previewToggle.selectedProperty().addListener(new ChangeListener<Boolean>() { 
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                restorePreviousImage();

                if (newValue) {
                    applyBulge(slider.getValue());
                }
            }
        });

        slider.valueProperty().addListener(new ChangeListener<Number>() { // Cannot put Double?
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                restorePreviousImage(); // reset

                if (previewToggle.isSelected()) {
                    applyBulge((double) newValue);
                }
            }
        });

        // Arrangement
        VBox popup = new VBox(10, slider);
        popup.setAlignment(Pos.CENTER);
        popup.setPadding(new Insets(10));

        HBox options = new HBox(10, previewToggle, cancelButton, confirmButton);
        options.setAlignment(Pos.BOTTOM_RIGHT);

        popup.getChildren().add(options);

        Scene scene = new Scene(popup, 300, 100);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void applyBulge(double strength) {
        // Image check
        if (!isImageLoaded()) {
            return;
        }

        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        double cx = width / 2.0;
        double cy = height / 2.0;
        double maxRadius = Math.sqrt(cx * cx + cy * cy); // max distance from center

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double dx = x - cx;
                double dy = y - cy;
                double radius = Math.sqrt(dx * dx + dy * dy);
                double angle = Math.atan2(dy, dx);
                // double newRadius = Math.pow(radius, 1.6) / 30; // original

                double newRadius = Math.pow(radius, strength); // bulge effect
                newRadius *= maxRadius / Math.pow(maxRadius, strength); // correct the position (else it will "zooms")

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
    void onSwirl(ActionEvent event) {
        if (!isImageLoaded()) {
            return;
        }

        previousImage = imageView.getImage(); // Store previous image for previous

        // Elements
        Stage dialog = new Stage();
        dialog.setTitle("Apply Swirl");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("icon3.jpg")));

        dialog.initModality(Modality.APPLICATION_MODAL); // lock interaction
        dialog.setResizable(false);

        // Labels
        Label rotationLabel = new Label("Rotation:");
        Label strengthLabel = new Label("Strength:");
        Label radiusLabel = new Label("Radius:");

        // Rotation slider
        Slider rotationSlider = new Slider(-6, 6, 0);
        rotationSlider.setShowTickLabels(true);
        rotationSlider.setShowTickMarks(true);
        rotationSlider.setMajorTickUnit(1.5);
        rotationSlider.setBlockIncrement(0.5);

        // Strength Slider
        Slider strengthSlider = new Slider(-20, 20, 10);
        strengthSlider.setShowTickLabels(true);
        strengthSlider.setShowTickMarks(true);
        strengthSlider.setMajorTickUnit(5);
        strengthSlider.setBlockIncrement(1);

        // Radius Slider
        Slider radiusSlider = new Slider(0, 500, 150);
        radiusSlider.setShowTickLabels(true);
        radiusSlider.setShowTickMarks(true);
        radiusSlider.setMajorTickUnit(50);
        radiusSlider.setBlockIncrement(25);

        CheckBox previewToggle = new CheckBox("Preview");
        previewToggle.setSelected(false);

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        // Functionality
        dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                restorePreviousImage(); // Cancels if 'x' is clicked
                dialog.close();
            }
        });

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                restorePreviousImage(); // Reset back to original (so effect doesn't stack)
                applySwirl(rotationSlider.getValue(), strengthSlider.getValue(), radiusSlider.getValue());
                dialog.close();
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                restorePreviousImage(); // Restore to the image before if cancelled
                dialog.close();
            }
        });

        previewToggle.selectedProperty().addListener(new ChangeListener<Boolean>() { 
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                restorePreviousImage();

                if (newValue) {
                    applySwirl(rotationSlider.getValue(), strengthSlider.getValue(), radiusSlider.getValue());
                }
            }
        });

        // Sliders
        rotationSlider.valueProperty().addListener(new ChangeListener<Number>() { // Cannot put Double?
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                restorePreviousImage(); // reset

                if (previewToggle.isSelected()) {
                    applySwirl((double) newValue, strengthSlider.getValue(), radiusSlider.getValue());
                }
            }
        });

        strengthSlider.valueProperty().addListener(new ChangeListener<Number>() { // Cannot put Double?
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                restorePreviousImage(); // reset

                if (previewToggle.isSelected()) {
                    applySwirl(rotationSlider.getValue(), (double) newValue, radiusSlider.getValue());
                }
            }
        });

        radiusSlider.valueProperty().addListener(new ChangeListener<Number>() { // Cannot put Double?
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                restorePreviousImage(); // reset

                if (previewToggle.isSelected()) {
                    applySwirl(rotationSlider.getValue(), strengthSlider.getValue(), (double) newValue);
                }
            }
        });

        // Arrangement
        VBox popup = new VBox(10, rotationLabel, rotationSlider, strengthLabel, strengthSlider, radiusLabel, radiusSlider);
        popup.setAlignment(Pos.CENTER);
        popup.setPadding(new Insets(10));

        HBox options = new HBox(10, previewToggle, cancelButton, confirmButton);
        options.setAlignment(Pos.BOTTOM_RIGHT);

        popup.getChildren().add(options);

        Scene scene = new Scene(popup, 300,300);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void applySwirl(double rotation, double strength, double radius) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        double cx = width / 2.0;
        double cy = height / 2.0;
        double r = Math.log(2) * radius / 5;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                double dx = x - cx;
                double dy = y - cy;
                double p = Math.sqrt(dx * dx + dy * dy); // polar coordinate?
                double angle = Math.atan2(dy, dx);

                double newAngle = angle + rotation + strength * Math.pow(Math.E, -p / r);

                int newX = (int) (cx + p * Math.cos(newAngle));
                int newY = (int) (cy + p * Math.sin(newAngle));
                
                if (newX >= 0 && newX < width && newY >= 0 && newY < height) {
                    writer.setColor(x, y, reader.getColor(newX, newY));
                }
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void onColorOverlay(ActionEvent event) {
        if (!isImageLoaded()) {
            return;
        }

        previousImage = imageView.getImage(); // Store previous image for previous

        // Elements
        Stage dialog = new Stage();
        dialog.setTitle("Color Overlay");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("icon3.jpg")));

        dialog.initModality(Modality.APPLICATION_MODAL); // lock interaction
        dialog.setResizable(false);

        ColorPicker colorPicker = new ColorPicker(); 

        Label mixLabel = new Label("Mix %:");

        Slider mixSlider = new Slider(0.0, 100.0, 50.0); 
        mixSlider.setShowTickLabels(true);
        mixSlider.setShowTickMarks(true);
        mixSlider.setMajorTickUnit(25);
        mixSlider.setBlockIncrement(10);

        CheckBox previewToggle = new CheckBox("Preview");
        previewToggle.setSelected(false);

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        // Functionality
        dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                restorePreviousImage(); // Cancels if 'x' is clicked
                dialog.close();
            }
        });

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                restorePreviousImage(); // Reset back to original (so effect doesn't stack)
                applyColorOverlay(colorPicker.getValue(), mixSlider.getValue());
                dialog.close();
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                restorePreviousImage(); // Restore to the image before if cancelled
                dialog.close();
            }
        });

        previewToggle.selectedProperty().addListener(new ChangeListener<Boolean>() { 
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                restorePreviousImage();

                if (newValue) {
                    applyColorOverlay(colorPicker.getValue(), mixSlider.getValue());
                } 
            }
        });
        
        colorPicker.setOnAction(new EventHandler<ActionEvent>() {
            public void handle(ActionEvent event) {
                restorePreviousImage(); // reset

                if (previewToggle.isSelected()) {
                    applyColorOverlay(colorPicker.getValue(), mixSlider.getValue());
                }
            }
        });

        mixSlider.valueProperty().addListener(new ChangeListener<Number>() { 
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                restorePreviousImage(); // reset

                if (previewToggle.isSelected()) {
                    applyColorOverlay(colorPicker.getValue(), (double) newValue); 
                }
            }
        });

        // Arrangement
        VBox popup = new VBox(10, colorPicker, mixLabel, mixSlider);
        popup.setAlignment(Pos.CENTER);
        popup.setPadding(new Insets(10));

        HBox options = new HBox(10, previewToggle, cancelButton, confirmButton);
        options.setAlignment(Pos.BOTTOM_RIGHT); 

        popup.getChildren().add(options);

        Scene scene = new Scene(popup, 300, 175);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void applyColorOverlay(Color overlayColor, double mix) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        // Color overlayColor = new Color(0.36, 0.61, 0.61, 0.5); // Changing opacity doesn't do anything?

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = reader.getColor(x, y);

                Color newColour = color.interpolate(overlayColor, mix/100); // assume mix input is percent
                writer.setColor(x, y, newColour);
            }
        }

        imageView.setImage(writableImage);
    }
    
    @FXML
    void onPixelation(ActionEvent event) {
        if (!isImageLoaded()) {
            return;
        }

        previousImage = imageView.getImage(); // Store previous image for previous

        // Elements
        Stage dialog = new Stage();
        dialog.setTitle("Apply Pixelation");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("icon3.jpg")));
        dialog.initModality(Modality.APPLICATION_MODAL); // lock interaction
        dialog.setResizable(false);

        Spinner<Integer> spinner = new Spinner<>(1, 50, 10, 1); 
        spinner.setEditable(true);

        CheckBox previewToggle = new CheckBox("Preview");
        previewToggle.setSelected(false);

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        // 
        dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                restorePreviousImage(); // Cancels if 'x' is clicked
                dialog.close();
            }
        });

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                restorePreviousImage(); // Reset back to original (so effect doesn't stack)
                applyPixelation(spinner.getValue()); 
                dialog.close();
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                restorePreviousImage(); // Restore to the image before if cancelled
                dialog.close();
            }
        });

        previewToggle.selectedProperty().addListener(new ChangeListener<Boolean>() { // shows effect without having to change spinner
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                restorePreviousImage();

                if (newValue) {
                    applyPixelation(spinner.getValue());
                }
            }
        });

        spinner.valueProperty().addListener(new ChangeListener<Integer>() {
            @Override
            public void changed(ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) {
                restorePreviousImage(); // reset

                if (previewToggle.isSelected()) {
                    applyPixelation(newValue); 
                }
            }
        });

        // Arrangement
        VBox popup = new VBox(10, spinner);
        popup.setAlignment(Pos.CENTER);
        popup.setPadding(new Insets(10));

        HBox options = new HBox(10, previewToggle, cancelButton, confirmButton);
        options.setAlignment(Pos.BOTTOM_RIGHT); 

        popup.getChildren().add(options);

        Scene scene = new Scene(popup, 300, 100);
        dialog.setScene(scene);
        dialog.showAndWait();
    }

    private void applyPixelation(int blockSize) {
        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

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
        if (!isImageLoaded()) {
            return;
        }

        previousImage = imageView.getImage(); // Store previous image for previous

        // Elements
        Stage dialog = new Stage();
        dialog.setTitle("Apply Vignette");
        dialog.getIcons().add(new Image(getClass().getResourceAsStream("icon3.jpg")));

        dialog.initModality(Modality.APPLICATION_MODAL); // lock interaction
        dialog.setResizable(false);

        Slider slider = new Slider(0.0, 1.0, 0.5);
        slider.setShowTickLabels(true);
        slider.setShowTickMarks(true);
        slider.setMajorTickUnit(0.5);
        slider.setBlockIncrement(0.1);

        CheckBox previewToggle = new CheckBox("Preview");
        previewToggle.setSelected(false);

        Button confirmButton = new Button("Confirm");
        Button cancelButton = new Button("Cancel");

        // Functionality
        dialog.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                restorePreviousImage(); // Cancels if 'x' is clicked
                dialog.close();
            }
        });

        confirmButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                restorePreviousImage(); // Reset back to original (so effect doesn't stack)
                applyVignette(slider.getValue());
                dialog.close();
            }
        });

        cancelButton.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {
                restorePreviousImage(); // Restore to the image before if cancelled
                dialog.close();
            }
        });

        previewToggle.selectedProperty().addListener(new ChangeListener<Boolean>() { 
            @Override
            public void changed(ObservableValue<? extends Boolean> observable, Boolean oldValue, Boolean newValue) {
                restorePreviousImage();

                if (newValue) {
                    applyVignette(slider.getValue());
                }
            }
        });

        slider.valueProperty().addListener(new ChangeListener<Number>() { 
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
                restorePreviousImage(); // reset

                if (previewToggle.isSelected()) {
                    applyVignette((double) newValue);
                }
            }
        });

        // Arrangement
        VBox popup = new VBox(10, slider);
        popup.setAlignment(Pos.CENTER);
        popup.setPadding(new Insets(10));

        HBox options = new HBox(10, previewToggle, cancelButton, confirmButton);
        options.setAlignment(Pos.BOTTOM_RIGHT);

        popup.getChildren().add(options);

        Scene scene = new Scene(popup, 300, 100);
        dialog.setScene(scene);
        dialog.showAndWait();
    }
    
    private void applyVignette(double intensity) {
        // Image check
        if (!isImageLoaded()) {
            return;
        }

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
                double distance = Math.sqrt(Math.pow(x - cx, 2) + Math.pow(y - cy, 2));
                double brightnessFactor = Math.max(1.0 - intensity * (distance / maxDistance), 0.3); // intensity factor for control

                Color color = reader.getColor(x, y);
                Color newColor = color.deriveColor(0, 1, 1, brightnessFactor); 
                writer.setColor(x, y, newColor);
            }
        }

        imageView.setImage(writableImage);
    }

    @FXML
    void onEdgeDetection(ActionEvent event) {
        // Image check
        if (!isImageLoaded()) {
            return;
        }

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
                    for (int kx = 0; kx < kernelSize  && x + kx < width; kx++) {
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
        // Image check
        if (!isImageLoaded()) {
            return;
        }

        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        double[][] kernel = {
            { -2, -1, 0 },
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
                    for (int kx = 0; kx < kernelSize  && x + kx < width; kx++) {
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
    void onGaussianBlur(ActionEvent event) {
        if (!isImageLoaded()) {
            return;
        }

        int width = (int) imageView.getImage().getWidth();
        int height = (int) imageView.getImage().getHeight();

        WritableImage writableImage = new WritableImage(width, height);
        PixelReader reader = imageView.getImage().getPixelReader();
        PixelWriter writer = writableImage.getPixelWriter();

        // predefined 3x3 kernel
        // double[][] kernel = {
        //     { 1, 2, 1 },
        //     { 2, 4, 2 },
        //     { 1, 2, 1 }
        // }; 
        // predefined 5x5 kernel (would need gaussian distribution to make a custom kernel)
        double[][] kernel = {
            { 1, 4, 6, 4, 1 },
            { 4, 16, 24, 16, 4 },
            { 6, 24, 36, 24, 6 },
            { 4, 16, 24, 16, 4 },
            { 1, 4, 6, 4, 1 }
        }; 

        int kernelSize = 5;
        int offset = kernelSize / 2; // fixes the weird shift
        double kernelSum = 256; 
        
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                // Accumulators
                double red = 0;
                double green = 0;
                double blue = 0;

                for (int ky = 0; ky < kernelSize; ky++) {
                    for (int kx = 0; kx < kernelSize; kx++) {
                        int dx = Math.min(Math.max(x + kx - offset, 0), width - 1); // clamp to image width
                        int dy = Math.min(Math.max(y + ky - offset, 0), height - 1); // clamp to image height

                        Color kernelColor = reader.getColor(dx, dy);
                        red += kernelColor.getRed() * kernel[ky][kx];
                        green += kernelColor.getGreen() * kernel[ky][kx];
                        blue += kernelColor.getBlue() * kernel[ky][kx];
                    }
                }

                red = Math.max(0.0, Math.min(red / kernelSum, 1.0));
                green = Math.max(0.0, Math.min(green / kernelSum, 1.0));
                blue = Math.max(0.0, Math.min(blue / kernelSum, 1.0));

                Color newColor = new Color(red, green, blue, reader.getColor(x, y).getOpacity());
                writer.setColor(x, y, newColor);
            }
        }

        imageView.setImage(writableImage);
    }

    // DO NOT REMOVE THIS METHOD!
    public void setStage(Stage stage) {
        this.stage = stage;
    }
}