package ihm;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class FileManager {

    private static String path=null;
    private static boolean hasBeenSaved=true;
    private static Stage popup = new Stage();

    public static void changeMade(){
        hasBeenSaved=false;
        Main.mainStage.setTitle("Color Picker *");
    }
    public static void noChanges(){
        hasBeenSaved=true;
        Main.mainStage.setTitle("Color Picker");
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

    public static void openCustomPopup(String msg,Button... buttons){
        popup = new Stage();

        VBox root=new VBox();
        Label message=new Label(msg);
        message.setWrapText(true);
        message.setStyle("-fx-font-size: 10pt;");
        message.setPadding(new Insets(5,5,5,5));
        Separator line = new Separator();
        line.setOrientation(Orientation.HORIZONTAL);
        line.setPadding(new Insets(10,0,10,0));

        HBox buttonsRoot=new HBox();

        buttonsRoot.getChildren().addAll(buttons);

        for (Node b : buttonsRoot.getChildren()){
            HBox.setMargin(b,new Insets(10,10,10,10));
        }

        root.getChildren().addAll(message,line,buttonsRoot);

        popup.initModality(Modality.APPLICATION_MODAL);
        popup.initOwner(Main.mainStage);
        popup.setResizable(false);
        popup.setTitle("Popup");
        popup.getIcons().add(new Image(File.separator+"ressources"+File.separator+"icon.png"));
        Scene dialogScene = new Scene(root, 300, 100);
        popup.setScene(dialogScene);
        popup.show();
    }

    public static void newFilePopup(){
        if (!hasBeenSaved && Main.showWarningPopups.isSelected()){
            Button save=new Button("Save");
            save.setOnAction(e->{
                popup.close();
                FileManager.saveFile();
                FileManager.newFile();

            });
            Button dontsave=new Button("Don't save");
            dontsave.setOnAction(e->{
                popup.close();
                FileManager.newFile();
            });
            Button cancel=new Button("Cancel");
            cancel.setOnAction(e->{
                popup.close();
            });
            openCustomPopup("Do you want to save your palette before starting a new one ?",save,dontsave,cancel);
        }else{
            FileManager.newFile();
        }

    }

    public static void newFile(){
        Main.colorList.getItems().clear();
        Main.addNewColor();
        FileManager.noChanges();
    }

    public static void saveFile(){
        if (path==null){
            FileManager.saveAsFile();
            return;
        }

        File file =new File(path);
        if (file!=null){
            FileManager.saveTextToFile(getColorsAsText(),file);
            FileManager.noChanges();
        }
    }

    public static void saveAsFile(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Color palette files (*.colors)", "*.colors");
        fileChooser.getExtensionFilters().add(extFilter);
        if (path!=null)fileChooser.setInitialDirectory(new File(path+File.separator+".."));
        File file = fileChooser.showSaveDialog(Main.mainStage);
        if (file!=null){
            path=file.getPath();
            FileManager.saveTextToFile(getColorsAsText(),file);
            FileManager.noChanges();
        }
    }

    public static void openFilePopup(){
        if (!hasBeenSaved && Main.showWarningPopups.isSelected()){
            Button save=new Button("Save");
            save.setOnAction(e->{
                popup.close();
                FileManager.saveFile();
                FileManager.openFile();

            });
            Button dontsave=new Button("Don't save");
            dontsave.setOnAction(e->{
                popup.close();
                FileManager.openFile();
            });
            Button cancel=new Button("Cancel");
            cancel.setOnAction(e->{
                popup.close();
            });
            openCustomPopup("Do you want to save your palette before open up a new one ?",save,dontsave,cancel);
        }else{
            FileManager.openFile();
        }
    }

    public static void openFile(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("Color palette files (*.colors)", "*.colors");
        fileChooser.getExtensionFilters().add(extFilter);
        if (path!=null)fileChooser.setInitialDirectory(new File(path+File.separator+".."));
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
            FileManager.noChanges();
        }
    }

    public static void exitPopup(){
        if (!hasBeenSaved && Main.showWarningPopups.isSelected()){
            Button save=new Button("Save");
            save.setOnAction(e->{
                popup.close();
                FileManager.saveFile();
                Platform.exit();
            });
            Button dontsave=new Button("Don't save");
            dontsave.setOnAction(e->{
                popup.close();
                Platform.exit();
            });
            Button cancel=new Button("Cancel");
            cancel.setOnAction(e->{
                popup.close();
            });
            openCustomPopup("Do you want to save your palette before exiting ?",save,dontsave,cancel);
        }else{
            Platform.exit();
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
