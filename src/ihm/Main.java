package ihm;

import javafx.application.Application;
import javafx.geometry.Orientation;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {

    public ListView<HBox> colorList;

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

        Spinner<Integer> colorAmount=new Spinner<Integer>();
        SpinnerValueFactory<Integer> valueFactory = //
                new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10, 0);
        colorAmount.getStyleClass().add(Spinner.STYLE_CLASS_SPLIT_ARROWS_HORIZONTAL);
        colorAmount.setValueFactory(valueFactory);

        ComboBox<String> colorType=new ComboBox<String>();
        colorType.getItems().addAll("(R,G,B)","(H,S,V)","#HEX");
        toolBar.getChildren().addAll(colorAmount,colorType);
        colorType.getSelectionModel().select(0);
        rootSelection.setTop(toolBar);

        HBox ligneTest=new HBox();
        ColorPicker cp=new ColorPicker();
        ligneTest.getChildren().add(cp);

        colorList=new ListView<HBox>();
        colorList.getItems().add(ligneTest);
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
        stage.setScene(scene);
        stage.setTitle("Grayscale Color Picker");
        stage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
