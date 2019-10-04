import java.io.*;
import java.awt.image.BufferedImage;

import javafx.application.Application;
import javafx.animation.*;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.*;
import javafx.geometry.*;
import javax.imageio.ImageIO;
import javafx.scene.*;
import javafx.stage.*;
import javafx.scene.input.*;
import javafx.scene.paint.*;
import javafx.scene.shape.*;
import javafx.scene.canvas.*;
import javafx.scene.control.*;
import javafx.scene.text.*;
import javafx.scene.layout.*;
import javafx.scene.image.*;
import javafx.util.*;

import javafx.fxml.*;
import javafx.fxml.FXMLLoader;


public class paint extends Application {
    boolean isOnField;
    int w = 900;
    int h = 440;
    Color mainColor = Color.BLACK;
    int lineWidth = 5;
    Color penColor;

    ColorPicker colorPicker = new ColorPicker();

    @FXML
    Button saveCanvasValuesBtn;
    @FXML
    TextField textFieldH;
    @FXML
    TextField textFieldW;

    
    Canvas canvas = new Canvas(w, h);
    GraphicsContext context = canvas.getGraphicsContext2D();


    @FXML
    public void saveCanvasValues(){
        context.setFill(Color.GREY);
        context.fillRect(0,0,w,h);
        h = Integer.parseInt(textFieldH.getText());
        w = Integer.parseInt(textFieldW.getText());
        System.out.println(h);
        System.out.println(w);
        canvas.setHeight(h);
        canvas.setWidth(w);
        context.setFill(Color.WHITE);
        context.fillRect(0,0,w,h);
        // stageSize.close();
        Color c = Color.LIGHTSTEELBLUE;
        System.out.println(c);
        saveCanvasValuesBtn.setText("You've clicked!");
    }

    public void chooseColor(){
        Color c = colorPicker.getValue();
        penColor = c;
        mainColor= c;
    }


    @Override
    public void start(Stage stage) throws Exception {
        canvas.setLayoutX(40);
        canvas.setLayoutY(40);

        context.setFill(Color.WHITE);
        context.fillRect(0,0,w,h);

        Button buttonSize = new Button("size");

        Button buttonEras = new Button("eraser");
        buttonEras.setLayoutX(40);

        Button buttonPen = new Button("pen");
        buttonPen.setLayoutX(95);

        Button buttonSave = new Button("save");
        buttonSave.setLayoutX(255);

        
        colorPicker.getStyleClass().add("button");
        colorPicker.setLayoutX(135);

        
        Group group = new Group(); 
        Scene scene = new Scene(group, 940, 480);
        scene.setFill(Color.GREY); 

        WritableImage wim = new WritableImage(w, h);
        
        File file = new File("CanvasImage.png");

        colorPicker.setOnAction((ActionEvent e) ->{
            chooseColor();
        });
        
        Parent rootSize = FXMLLoader.load(paint.class.getResource("saveWin.fxml")); 
        Stage stageSize = new Stage();
        Scene sceneSize = new Scene(rootSize);

        buttonSize.setOnMouseClicked((MouseEvent event) ->{
            stageSize.setScene(sceneSize);
            stageSize.show(); 
            stageSize.setTitle("Size");
            
        });
        

        buttonPen.setOnMouseClicked((MouseEvent even) ->{
            mainColor = penColor;
            lineWidth = 5;
        });

        buttonEras.setOnMouseClicked((MouseEvent even) ->{
            mainColor = Color.WHITE;
            lineWidth = 10;
        });

        canvas.setOnMousePressed((MouseEvent event) -> {
            isOnField = true;
            
            context.beginPath();
            context.setLineWidth(lineWidth);
            context.moveTo(event.getX(), event.getY());
            context.lineTo(event.getX(), event.getY());
            context.setStroke(mainColor);
            context.setLineCap(StrokeLineCap.ROUND);

            canvas.setOnMouseDragged((MouseEvent e) -> {
                if (!isOnField)
                    return;
                if (e.getX() < 0 || e.getY() < 0 || 
                    e.getX() > w || e.getY() > h)
                    isOnField = false;
                context.lineTo(e.getX(), e.getY());
                context.stroke();
            });
            context.closePath();
        });
        
        buttonSave.setOnMouseClicked((MouseEvent even) ->{
            canvas.snapshot(null, wim);
             try {
                ImageIO.write(SwingFXUtils.fromFXImage(wim, null), "png", file);
            } catch (Exception s) {
            }
        });

        stage.setTitle("MyPaint");

        group.getChildren().add(canvas);
        group.getChildren().add(buttonSize);
        group.getChildren().add(buttonEras);
        group.getChildren().add(buttonPen);
        group.getChildren().add(colorPicker);
        group.getChildren().add(buttonSave);

        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
