package ihm;

import javafx.application.Application;
import javafx.beans.InvalidationListener;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.awt.*;
import java.io.File;
import java.util.Collections;
import java.util.Comparator;

/*
TO DO
    - Ameliorate optimisation de couleur (teinte ET LUMINOSITE)
    - new/open/save

 */

public class Main extends Application {

    public static Stage mainStage;
    public static ListView<ColorLine> colorList;
    public static ComboBox<String> colorMode;
    public static Button addColorButton;
    public static Canvas beforeCanvas;
    public static Canvas afterCanvas;

    class AddColorButtonEvent implements EventHandler<ActionEvent>{
        public void handle(ActionEvent event){
            addNewColor();
        }
    }
    static class ColorModeChangeEvent implements EventHandler<ActionEvent>{
        public void handle(ActionEvent event){
            for (ColorLine line : colorList.getItems()){
                line.setTextField();
            }
        }
    }

    public  static void addNewColor(){
        addNewColor(new Color(1,1,1,1));
        FileManager.changeMade();
    }

    public static void addNewColor(Color color){
        if (colorList.getItems().size()<10) {
            ColorLine ligneTest = new ColorLine(color);
            colorList.getItems().add(ligneTest);
            lockButton();
            PreviewRenderer.render();
            FileManager.changeMade();
        }
    }

    public void removeSelectedColor(){
        if (colorList.getItems().size()>1){
            colorList.requestFocus();
            colorList.getSelectionModel().getSelectedItems().get(0).delete();
            colorList.getSelectionModel().select(-1);
            FileManager.changeMade();
        }
    }

    @Override
    public void start(Stage stage){
        mainStage=stage;
        //ROOT
        VBox root = new VBox();

        //MENU BAR
        MenuBar menu=new MenuBar();
        MenuItem newPalette=new MenuItem("New Palette");
        newPalette.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN));
        newPalette.setOnAction(e -> {
            FileManager.newFile();
        });
        MenuItem openPalette=new MenuItem("Open Palette");
        openPalette.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openPalette.setOnAction(e -> {
            FileManager.openFile();
        });
        MenuItem savePalette=new MenuItem("Save Palette");
        savePalette.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        savePalette.setOnAction(e -> {
            FileManager.saveFile();
        });
        MenuItem savePaletteAs=new MenuItem("Save Palette As");
        savePaletteAs.setAccelerator(new KeyCodeCombination(KeyCode.N, KeyCombination.CONTROL_DOWN,KeyCombination.SHIFT_DOWN));
        savePaletteAs.setOnAction(e -> {
            FileManager.saveAsFile();
        });
        MenuItem exit=new MenuItem("Exit");

        Menu fileMenu=new Menu("_File");
        fileMenu.getItems().addAll(newPalette,openPalette,savePalette,savePaletteAs,new SeparatorMenuItem(),exit);

        Menu editMenu=new Menu("_Edit");
        MenuItem addColor=new MenuItem("Add a new color");
        addColor.setAccelerator(new KeyCodeCombination(KeyCode.P, KeyCombination.CONTROL_DOWN));
        addColor.setOnAction(e -> {
            addNewColor();
        });
        MenuItem removeColor=new MenuItem("Remove selected color");
        removeColor.setAccelerator(new KeyCodeCombination(KeyCode.DELETE));
        removeColor.setOnAction(e -> {
            removeSelectedColor();
        });

        MenuItem sortGrayscale=new MenuItem("Sort by grayscale");
        sortGrayscale.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN));
        sortGrayscale.setOnAction(e -> {
            sortByGrayscale();
        });
        MenuItem sortHue=new MenuItem("Sort by hue");
        sortHue.setAccelerator(new KeyCodeCombination(KeyCode.T, KeyCombination.CONTROL_DOWN,KeyCombination.SHIFT_DOWN));
        sortHue.setOnAction(e -> {
            sortByHue();
        });
        MenuItem optimizeColors=new MenuItem("Optimize Colors");
        optimizeColors.setAccelerator(new KeyCodeCombination(KeyCode.U, KeyCombination.CONTROL_DOWN));
        optimizeColors.setOnAction(e -> {
            optimizeColors();
        });
        MenuItem randomizeColors=new MenuItem("Randomize Colors");
        randomizeColors.setAccelerator(new KeyCodeCombination(KeyCode.R, KeyCombination.CONTROL_DOWN));
        randomizeColors.setOnAction(e -> {
            randomizeColors();
        });

        editMenu.getItems().addAll(addColor,removeColor,new SeparatorMenuItem(),sortGrayscale,sortHue,new SeparatorMenuItem(),optimizeColors,randomizeColors);

        Menu helpMenu=new Menu("_Help");
        MenuItem about=new MenuItem("About");
        about.setOnAction(e -> {
            Alert infoBox = new Alert(Alert.AlertType.INFORMATION);
            infoBox.setHeaderText("This application was created during a programming project with java and javafx\n" +
                                  "Application created by Noé Delcroix & Cyril Demand\n");
            infoBox.setContentText("2021 - All rights reserved");
            infoBox.setTitle("About");
            infoBox.show();
        });
        helpMenu.getItems().add(about);

        menu.getMenus().addAll(fileMenu,editMenu,helpMenu);

        //TOOL BAR
        ToolBar toolBar1=new ToolBar();

        addColorButton =new Button("Add a new color");


        addColorButton.addEventHandler(ActionEvent.ACTION, new AddColorButtonEvent());

        colorMode=new ComboBox<String>();
        colorMode.getItems().addAll("r,g,b","r,g,b (1)","h,s,b","h,s,b (1)","#rrggbb","0xrrggbb");
        colorMode.getSelectionModel().select(0);
        colorMode.addEventHandler(ActionEvent.ACTION, new ColorModeChangeEvent());

        toolBar1.getItems().addAll(addColorButton,new Label("Color Mode : "),colorMode);

        //COLOR LIST
        colorList=new ListView<ColorLine>();
        colorList.setMaxHeight(300);
        colorList.setMinHeight(300);

        //PREVIEW ZONE
        HBox rootPreview = new HBox();

        VBox before=new VBox();
        Label beforeTxt=new Label("Before : ");
        beforeCanvas=new Canvas(350,350);
        before.getChildren().addAll(beforeTxt,beforeCanvas);

        Separator line=new Separator();
        line.setOrientation(Orientation.VERTICAL);

        VBox after=new VBox();
        Label afterTxt=new Label("After : ");
        afterCanvas=new Canvas(350,350);
        after.getChildren().addAll(afterTxt,afterCanvas);

        rootPreview.getChildren().addAll(before,line,after);


        root.getChildren().addAll(menu,toolBar1,colorList,rootPreview);

        Scene scene = new Scene(root, 700, 700);
        stage.setResizable(false);
        stage.getIcons().add(new Image(File.separator+"ressources"+File.separator+"icon.png"));
        stage.setScene(scene);
        stage.setTitle("Grayscale Color Picker");
        stage.show();

        stage.setOnCloseRequest(event -> {
            System.out.println("Stage is closing");
            // Save file
        });

        addNewColor();
    }

    public void optimizeColors(){
        //System.out.println("Optimization : ");
        for (ColorLine line : colorList.getItems()){
            Color color=line.getColor();

            Color[] possibleColors=new Color[360];
            double[] grayScaleDistances=new double[360];
            double[] originalHueDistances=new double[360];

            for (int i=0;i<360;i++){
                Color testing=Color.hsb(i,color.getSaturation(),color.getBrightness());
                possibleColors[i]=testing;

                double minGrayScaleDist=1;
                for (ColorLine line2 : colorList.getItems()){
                    double dist=Math.abs(ColorLine.grayScaleLevel(line2.getColor())-ColorLine.grayScaleLevel(testing));
                    if (line!=line2 && dist<minGrayScaleDist){
                        minGrayScaleDist=dist;
                    }
                }
                grayScaleDistances[i]=minGrayScaleDist;
                originalHueDistances[i]=Math.abs(testing.getHue()-color.getHue());

                //System.out.println(testing+" gsl:"+ColorLine.grayScaleLevel(testing)+" dist:"+minGrayScaleDist);
            }

            double bestScore=-1;
            Color bestColor=null;

            for (int i=0;i<360;i++){
                double score=grayScaleDistances[i]*1000-originalHueDistances[i];
                //System.out.println(possibleColors[i]+" "+grayScaleDistances[i]+ " "+originalHueDistances[i]+" Score : "+score);
                if (score>bestScore){
                    bestScore=score;
                    bestColor=possibleColors[i];
                }
            }

            //System.out.println("Best color found : "+bestColor);
            line.setColor(bestColor);
        }
        PreviewRenderer.render();
        FileManager.changeMade();
    }

    public void randomizeColors(){
        for (ColorLine line : colorList.getItems()){
            line.setColor(new Color(Math.random(),Math.random(),Math.random(),1));
        }
        PreviewRenderer.render();
        FileManager.changeMade();
    }

    public void sortByHue(){
        Collections.sort(colorList.getItems(), new Comparator<ColorLine>() {
            @Override
            public int compare(ColorLine o1, ColorLine o2) {
                double h1=o1.getColor().getHue(),h2=o2.getColor().getHue();
                if (h1>h2)return 1;
                else if (h1==h2) return 0;
                else return -1;
            }
        });
        PreviewRenderer.render();
        FileManager.changeMade();
    }
    public void sortByGrayscale(){
        Collections.sort(colorList.getItems(), new Comparator<ColorLine>() {
            @Override
            public int compare(ColorLine o1, ColorLine o2) {
                double gs1=ColorLine.grayScaleLevel(o1.getColor()),gs2=ColorLine.grayScaleLevel(o2.getColor());
                if (gs1>gs2)return 1;
                else if (gs1==gs2) return 0;
                else return -1;
            }
        });
        PreviewRenderer.render();
        FileManager.changeMade();
    }

    public static void lockButton(){

        addColorButton.setDisable(colorList.getItems().size()>=10);

        for (ColorLine line : colorList.getItems()){
            line.getChildren().get(2).setDisable(colorList.getItems().size()<=1);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
