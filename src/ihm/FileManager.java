package ihm;

import javafx.scene.paint.Color;
import javafx.stage.FileChooser;

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
    private static boolean asBeenSaved=false;

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
