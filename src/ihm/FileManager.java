package ihm;

import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.Scanner;
import java.util.logging.Logger;

public class FileManager {

    private static String path=null;
    private static boolean asBeenSaved=true;

    public static void changeMade(){
        asBeenSaved=false;
        System.out.println("change made");
    }

    private static String getColorsAsText(){
        String res="";
        for (int i=0;i<Main.colorList.getItems().size();i++){
            String s=Main.colorList.getItems().get(i).getColor().toString().toUpperCase();
            res+=s.substring(2,s.length()-2);
            if (i != Main.colorList.getItems().size() - 1) {
                res+="\n";
            }
        }
        return res;
    }

    public static void newFile(){
        if (!asBeenSaved){
            VBox root=new VBox();
            Label message=new Label("Ton père le chauve");
            message.setStyle("-fx-font-size: 12pt;");
            message.setPadding(new Insets(10,10,10,10));
            Separator line = new Separator();
            line.setOrientation(Orientation.HORIZONTAL);
            line.setPadding(new Insets(10,0,10,0));

            HBox buttons=new HBox();
            buttons.getChildren().add(new Button("Save"));
            buttons.getChildren().add(new Button("Don't save"));
            buttons.getChildren().add(new Button("Cancel"));
            for (Node b : buttons.getChildren()){
                HBox.setMargin(b,new Insets(10,10,10,10));
            }


            root.getChildren().addAll(message,line,buttons);
            final Stage popup = new Stage();
            popup.initModality(Modality.APPLICATION_MODAL);
            popup.initOwner(Main.mainStage);
            popup.setResizable(false);
            popup.setTitle("Popup");
            popup.getIcons().add(new Image(File.separator+"ressources"+File.separator+"icon.png"));
            Scene dialogScene = new Scene(root, 500, 150);
            popup.setScene(dialogScene);
            popup.show();
        }


        Main.colorList.getItems().clear();
        Main.addNewColor();
        path=null;
        asBeenSaved=true;
    }

    public static void saveFile(){
        if (path==null){
            FileManager.saveAsFile();
            return;
        }

        File file =new File(path);
        if (file!=null){
            FileManager.saveTextToFile(getColorsAsText(),file);
            asBeenSaved=true;
        }
    }

    public static void saveAsFile(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Color palette files (*.colors)", "*.colors");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showSaveDialog(Main.mainStage);
        if (file!=null){
            path=file.getPath();
            FileManager.saveTextToFile(getColorsAsText(),file);
            asBeenSaved=true;
        }
    }

    public static void openFile(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Color palette files (*.colors)", "*.colors");
        fileChooser.getExtensionFilters().add(extFilter);
        File file = fileChooser.showOpenDialog(Main.mainStage);

        if (file != null) {
            path=file.getPath();
            Main.colorList.getItems().clear();
            Scanner scanner;
            try{
                scanner=new Scanner(file);
                while (scanner.hasNextLine()){
                    Main.addNewColor(Color.web(scanner.nextLine()));
                }
            } catch (FileNotFoundException ignored) {

            }
            asBeenSaved=true;
        }
    }

    private static void saveTextToFile(String content, File file) {
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.print(content);
            writer.close();
        } catch (IOException ignored) {

        }
    }


}
