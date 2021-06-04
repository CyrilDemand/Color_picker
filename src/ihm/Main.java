package ihm;

import javafx.application.Application;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;

/*
TO DO
    - CTRL+Z
 */

public class Main extends Application {

    public static Stage mainStage;
    public static ListView<ColorLine> colorList;
    public static ComboBox<String> colorMode;
    public static Button addColorButton;
    public static Canvas beforeCanvas;
    public static Canvas afterCanvas;

    public static CheckMenuItem limitColor;
    public static CheckMenuItem showWarningPopups;

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
            FileManager.newFilePopup();
        });
        MenuItem openPalette=new MenuItem("Open Palette");
        openPalette.setAccelerator(new KeyCodeCombination(KeyCode.O, KeyCombination.CONTROL_DOWN));
        openPalette.setOnAction(e -> {
            FileManager.openFilePopup();
        });
        MenuItem savePalette=new MenuItem("Save Palette");
        savePalette.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN));
        savePalette.setOnAction(e -> {
            FileManager.saveFile();
        });
        MenuItem savePaletteAs=new MenuItem("Save Palette As");
        savePaletteAs.setAccelerator(new KeyCodeCombination(KeyCode.S, KeyCombination.CONTROL_DOWN,KeyCombination.SHIFT_DOWN));
        savePaletteAs.setOnAction(e -> {
            FileManager.saveAsFile();
        });
        MenuItem exit=new MenuItem("Exit");
        exit.setOnAction(e -> {
            FileManager.exitPopup();
        });

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

        Menu optionsMenu=new Menu("_Options");
        limitColor = new CheckMenuItem("Limit to 10 colors");
        limitColor.setOnAction(e->{
            lockButton();
        });
        limitColor.setSelected(true);
        showWarningPopups = new CheckMenuItem("Show warning popups");
        showWarningPopups.setSelected(true);

        optionsMenu.getItems().addAll(limitColor,showWarningPopups);


        Menu helpMenu=new Menu("_Help");
        MenuItem about=new MenuItem("About");
        about.setOnAction(e -> {
            showAbout();
        });
        helpMenu.getItems().add(about);

        menu.getMenus().addAll(fileMenu,editMenu,optionsMenu,helpMenu);

        //TOOL BAR
        ToolBar toolBar1=new ToolBar();

        addColorButton =new Button("Add a new color");
        addColorButton.addEventHandler(ActionEvent.ACTION, new AddColorButtonEvent());

        Button optimizeColorsButton =new Button("Optimize Colors");
        optimizeColorsButton.setOnAction(e->{
            optimizeColors();
        });

        colorMode=new ComboBox<String>();
        colorMode.getItems().addAll("r,g,b","r,g,b (1)","h,s,b","h,s,b (1)","#rrggbb","0xrrggbb");
        colorMode.getSelectionModel().select(0);
        colorMode.addEventHandler(ActionEvent.ACTION, new ColorModeChangeEvent());

        toolBar1.getItems().addAll(addColorButton,new Label("Color Mode : "),colorMode,optimizeColorsButton);

        //COLOR LIST
        colorList=new ListView<ColorLine>();
        colorList.setMaxHeight(300);
        colorList.setMinHeight(300);

        //PREVIEW ZONE
        HBox rootPreview = new HBox();

        ToolBar toolBar2=new ToolBar();
        Label preview=new Label("Preview : ");
        preview.setStyle("-fx-font-size: 15pt;"+"-fx-font-weight: bold");
        toolBar2.getItems().add(preview);

        VBox before=new VBox();
        Label beforeTxt=new Label("Before");
        beforeTxt.setStyle("-fx-font-size: 12pt;");
        beforeCanvas=new Canvas(350,300);
        before.getChildren().addAll(beforeTxt,beforeCanvas);

        Separator line=new Separator();
        line.setOrientation(Orientation.VERTICAL);

        VBox after=new VBox();
        Label afterTxt=new Label("After");
        afterTxt.setStyle("-fx-font-size: 12pt;");
        afterCanvas=new Canvas(350,300);
        after.getChildren().addAll(afterTxt,afterCanvas);

        rootPreview.getChildren().addAll(before,line,after);


        root.getChildren().addAll(menu,toolBar1,colorList,toolBar2,rootPreview);

        Scene scene = new Scene(root, 700, 720);
        stage.setResizable(false);
        stage.getIcons().add(new Image(File.separator+"ressources"+File.separator+"icon.png"));
        stage.setScene(scene);
        stage.show();

        stage.setOnCloseRequest(event -> {
            event.consume();
            FileManager.exitPopup();
        });

        FileManager.newFile();
    }

    public static void showAbout(){
        VBox root=new VBox();
        Image img=new Image(File.separator+"ressources"+File.separator+"icon.png");
        ImageView imgView=new ImageView(img);
        imgView.setFitHeight(200);
        imgView.setFitWidth(200);
        imgView.setTranslateX(520/2-200/2);
        Label txt=new Label("This program as been created during a java/javafx project.\n"+
                                "Application made by Noé Delcroix & Cyril Demand.\n"+
                                "2021 - All rights reserved");
        txt.setWrapText(true);
        txt.setTextAlignment(TextAlignment.CENTER);
        txt.setStyle("-fx-font-size: 15pt;");

        root.getChildren().addAll(imgView,txt);
        final Stage about = new Stage();
        about.initModality(Modality.APPLICATION_MODAL);
        about.initOwner(Main.mainStage);
        about.setResizable(false);
        about.setTitle("About this program");
        about.getIcons().add(new Image(File.separator+"ressources"+File.separator+"icon.png"));
        Scene dialogScene = new Scene(root, 520, 300);
        about.setScene(dialogScene);
        about.show();
    }

    public  static void addNewColor(){
        addNewColor(new Color(1,1,1,1));
        FileManager.changeMade();
    }

    public static void addNewColor(Color color){
        if (colorList.getItems().size()<10 || !limitColor.isSelected()) {
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
            if (colorList.getSelectionModel().getSelectedItems().size()!=0) {
                colorList.getSelectionModel().getSelectedItems().get(0).delete();
                colorList.getSelectionModel().select(-1);
                FileManager.changeMade();
            }
        }
    }

    public void optimizeColors(){
        //System.out.println("Optimization : ");
        for (ColorLine line : colorList.getItems()){
            Color color=line.getColor();

            int precision=360;

            double bestDistance=-1;
            Color bestColor=null;

            for (double i=0;i<precision;i++){
                Color testing=Color.hsb(color.getHue(),color.getSaturation(),i/precision);
                if (ColorConverter.grayScaleLevel(testing)<0.1 || ColorConverter.grayScaleLevel(testing)>0.9){
                    continue;
                }

                double minGrayScaleDist=2;
                for (ColorLine line2 : colorList.getItems()){

                    double dist=Math.abs(ColorConverter.grayScaleLevel(line2.getColor())-ColorConverter.grayScaleLevel(testing));
                    //System.out.println("tested : "+ColorConverter.grayScaleLevel(line2.getColor())+" with "+ColorConverter.grayScaleLevel(testing)+" DIST : "+dist);
                    if (line!=line2 && dist<minGrayScaleDist){
                        minGrayScaleDist=dist;
                    }
                }

                if (minGrayScaleDist>bestDistance){
                    bestDistance=minGrayScaleDist;
                    bestColor=testing;
                }
                //System.out.println(testing+" gsl:"+ColorConverter.grayScaleLevel(testing)+" dist:"+minGrayScaleDist);


            }

            line.setColor(bestColor);
        }
        PreviewRenderer.render();
        FileManager.changeMade();
    }

    public void randomizeColors(){
        for (ColorLine line : colorList.getItems()){
            line.setColor(Color.hsb(Math.random()*360,1,1));
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
                double gs1=ColorConverter.grayScaleLevel(o1.getColor()),gs2=ColorConverter.grayScaleLevel(o2.getColor());
                if (gs1>gs2)return 1;
                else if (gs1==gs2) return 0;
                else return -1;
            }
        });
        PreviewRenderer.render();
        FileManager.changeMade();
    }

    public static void lockButton(){

        addColorButton.setDisable(colorList.getItems().size()>=10 && limitColor.isSelected());

        for (ColorLine line : colorList.getItems()){
            line.getChildren().get(2).setDisable(colorList.getItems().size()<=1);
        }

    }

    public static void main(String[] args) {
        launch(args);
    }
}
