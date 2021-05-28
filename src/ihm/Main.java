package ihm;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;

import java.util.Collections;
import java.util.Comparator;

/*
TO DO
    - new/open/save
    - automatically choose the best color when you add a new one
    - about menu
    - selection en cascade & suppr/del pour virer une ligne
 */

public class Main extends Application {

    public ListView<ColorLine> colorList;
    public ComboBox<String> colorMode;

    class AddColorButtonEvent implements EventHandler<ActionEvent>{
        public void handle(ActionEvent event){
            addNewColor();
        }
    }
    class ColorModeChangeEvent implements EventHandler<ActionEvent>{
        public void handle(ActionEvent event){
            for (ColorLine line : colorList.getItems()){
                line.setTextField();
            }
        }
    }

    public void addNewColor(){
        ColorLine ligneTest=new ColorLine(this);
        colorList.getItems().add(ligneTest);
    }

    public String getColorMode(){
        return this.colorMode.getValue();
    }

    public void updatePreview(){


    }

    @Override
    public void start(Stage stage){

        //ROOT
        VBox root = new VBox();

        //MENU BAR
        MenuBar menu=new MenuBar();
        MenuItem newEmptyPalette=new MenuItem("New Empty Palette");
        MenuItem newPalette=new MenuItem("New Palette");
        MenuItem openPalette=new MenuItem("Open Palette");
        MenuItem savePalette=new MenuItem("Save Palette");
        MenuItem savePaletteAs=new MenuItem("Save Palette As");
        MenuItem exit=new MenuItem("Exit");

        Menu fileMenu=new Menu("File");
        fileMenu.getItems().addAll(newEmptyPalette,newPalette,new SeparatorMenuItem(),openPalette,savePalette,savePaletteAs,new SeparatorMenuItem(),exit);

        Menu editMenu=new Menu("Edit");
        MenuItem sortGrayscale=new MenuItem("Sort by grayscale");
        sortGrayscale.setOnAction(e -> {
            Collections.sort(colorList.getItems(), new Comparator<ColorLine>() {
                @Override
                public int compare(ColorLine o1, ColorLine o2) {
                    double gs1=ColorLine.grayScaleLevel(o1.getColor()),gs2=ColorLine.grayScaleLevel(o2.getColor());
                    if (gs1>gs2)return 1;
                    else if (gs1==gs2) return 0;
                    else return -1;
                }
            });
        });
        MenuItem sortHue=new MenuItem("Sort by Hue");
        sortHue.setOnAction(e -> {
            Collections.sort(colorList.getItems(), new Comparator<ColorLine>() {
                @Override
                public int compare(ColorLine o1, ColorLine o2) {
                    double h1=o1.getColor().getHue(),h2=o2.getColor().getHue();
                    if (h1>h2)return 1;
                    else if (h1==h2) return 0;
                    else return -1;
                }
            });
        });
        editMenu.getItems().addAll(sortGrayscale,sortHue);

        Menu helpMenu=new Menu("Help");
        MenuItem about=new MenuItem("About");
        helpMenu.getItems().add(about);

        menu.getMenus().addAll(fileMenu,editMenu,helpMenu);

        //TOOL BAR
        ToolBar toolBar=new ToolBar();

        Button addColorButton =new Button("+ Add a new color");

        addColorButton.addEventHandler(ActionEvent.ACTION, new AddColorButtonEvent());

        colorMode=new ComboBox<String>();
        colorMode.getItems().addAll("r,g,b","r,g,b (1)","h,s,b","h,s,b (1)","#rrggbb","0xrrggbb");
        colorMode.getSelectionModel().select(0);
        colorMode.addEventHandler(ActionEvent.ACTION, new ColorModeChangeEvent());

        toolBar.getItems().addAll(addColorButton,new Label("Color Mode : "),colorMode);

        //COLOR LIST
        colorList=new ListView<ColorLine>();


        root.getChildren().addAll(menu,toolBar,colorList);

        Scene scene = new Scene(root, 1000, 800);
        stage.getIcons().add(new Image("https://i.imgur.com/N2pKXMG.png"));
        stage.setScene(scene);
        stage.setTitle("Grayscale Color Picker");
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
