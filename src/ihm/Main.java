package ihm;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.event.ActionEvent;


/*
TO DO
- automatically choose the best color when you add a new one
- working preview page
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

    @Override
    public void start(Stage stage){

        //ROOT GLOBALE ET ROOT POUR CHAQUE ONGLET
        VBox root = new VBox();
        BorderPane rootSelection=new BorderPane();
        HBox rootPreview=new HBox();

        //ONGLETS
        TabPane tabPane=new TabPane();
        tabPane.setTabClosingPolicy(TabPane.TabClosingPolicy.UNAVAILABLE);
        Tab tab1 = new Tab("Selection",rootSelection);
        Tab tab2 = new Tab("Preview",rootPreview);
        tabPane.getTabs().addAll(tab1,tab2);

        root.getChildren().add(tabPane);


        //PAGE SELECTION
        HBox toolBar=new HBox();

        Button addColorButton =new Button("+ Add a new color");
        addColorButton.addEventHandler(ActionEvent.ACTION, new AddColorButtonEvent());

        colorMode=new ComboBox<String>();
        colorMode.getItems().addAll("r,g,b","r,g,b (1)","h,s,b","h,s,b (1)","#rrggbb","0xrrggbb");
        colorMode.getSelectionModel().select(0);
        colorMode.addEventHandler(ActionEvent.ACTION, new ColorModeChangeEvent());

        toolBar.getChildren().addAll(addColorButton,new Label("Color type :"),colorMode);

        rootSelection.setTop(toolBar);

        colorList=new ListView<ColorLine>();
        rootSelection.setCenter(colorList);

        //PAGE PREVIEW
        CategoryAxis xAxis1=new CategoryAxis();
        xAxis1.setLabel("x-axis");
        NumberAxis yAxis1=new NumberAxis();
        yAxis1.setLabel("y-axis");
        BarChart<String,Number> graphColor=new BarChart<String,Number>(xAxis1,yAxis1);

        Separator line=new Separator();
        line.setOrientation(Orientation.VERTICAL);

        CategoryAxis xAxis2=new CategoryAxis();
        xAxis2.setLabel("x-axis");
        NumberAxis yAxis2=new NumberAxis();
        yAxis2.setLabel("y-axis");
        BarChart<String,Number> graphGrayscale=new BarChart<String,Number>(xAxis2,yAxis2);

        rootPreview.getChildren().addAll(graphColor,line,graphGrayscale);

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
