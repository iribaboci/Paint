package paint;

import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;


public class Paint extends Application {

    final static int CANVAS_WIDTH = 1100;
    final static int CANVAS_HEIGHT = 570;

    ColorPicker colorPicker;
    @Override
    public void start(final Stage primaryStage) {

        final Canvas canvas = new Canvas(CANVAS_WIDTH, CANVAS_HEIGHT);
        final GraphicsContext graphicsContext = canvas.getGraphicsContext2D();
       

        Draw(graphicsContext);



        canvas.addEventHandler(MouseEvent.MOUSE_PRESSED,
                event -> {
                    graphicsContext.beginPath();
                    graphicsContext.moveTo(event.getX(), event.getY());
                    graphicsContext.setStroke(colorPicker.getValue());
                    graphicsContext.stroke();
                });

        canvas.addEventHandler(MouseEvent.MOUSE_DRAGGED,
                event -> {
                    graphicsContext.lineTo(event.getX(), event.getY());
                    graphicsContext.setStroke(colorPicker.getValue());
                    graphicsContext.stroke();
                });

        canvas.addEventHandler(MouseEvent.MOUSE_RELEASED,
                event -> {

                });

        Group root = new Group();

        Button buttonSave = new Button("Save");
        buttonSave.setOnAction(t -> {
            FileChooser fileChooser = new FileChooser();

            //Set extension filter
            FileChooser.ExtensionFilter extFilter =
                    new FileChooser.ExtensionFilter("png files (*.png)", "*.png");
            fileChooser.getExtensionFilters().add(extFilter);

            //Show save file dialog
            File file = fileChooser.showSaveDialog(primaryStage);

            if(file != null){
                try {
                    WritableImage writableImage = new WritableImage(CANVAS_WIDTH, CANVAS_HEIGHT);
                    canvas.snapshot(null, writableImage);
                    RenderedImage renderedImage = SwingFXUtils.fromFXImage(writableImage, null);
                    ImageIO.write(renderedImage, "png", file);
                } catch (IOException ex) {


                }
            }
        });

        Button buttonOpen = new Button("Clear");
        buttonOpen.setOnAction(t-> {
            graphicsContext.clearRect(0,0,1100,600);
            
        });


        Slider slider = new Slider();
        slider.setMin(0);
        slider.setMax(100);
        slider.setValue(1);
        slider.setMinorTickCount(5);
        slider.setBlockIncrement(10);

        slider.valueProperty().addListener((ov, old_val, new_val) -> {
            slider.setValue(new_val.intValue());
            graphicsContext.setLineWidth(slider.getValue()/10);
        });
        
        HBox hBox = new HBox();
        hBox.setAlignment(Pos.CENTER);
        hBox.getChildren().addAll(colorPicker, buttonSave,buttonOpen,slider);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(canvas,hBox);
        root.getChildren().add(vBox);
        Scene scene = new Scene(root, 1000, 600);

        primaryStage.setTitle("Basic Paint program");
        primaryStage.setResizable(false);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
    private void Draw(GraphicsContext gc){

        colorPicker = new ColorPicker(Color.BLACK);
        gc.setFill(colorPicker.getValue());
        gc.setStroke(colorPicker.getValue());
        gc.setLineWidth(1);
    }
}