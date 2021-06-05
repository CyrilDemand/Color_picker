package ihm;

import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class HistoryManager {


    public static ArrayList<ArrayList<Color>> history=new ArrayList<ArrayList<Color>>();
    public static int currentPos=-1;

    public static void clear(){
        history.clear();
        currentPos=-1;
        //System.out.println("reset");
    }

    public static void undo(){
        if (currentPos>0){
            currentPos--;
            loadHistory();
            /*System.out.println("current pos : "+currentPos);
            System.out.println("history : "+history);
            System.out.println("current state "+getCurrentHistoryData()+" saved state "+FileManager.getSavedState());*/

            if (!isDifferent(FileManager.getSavedState())){
                FileManager.noChanges(false);
            }else{
                FileManager.setHasBeenSaved(false);
                Main.mainStage.setTitle("Color Picker *");
            }
        }
    }
    public static void redo(){
        if (currentPos<history.size()){
            currentPos++;
            loadHistory();
            /*System.out.println("current pos : "+currentPos);
            System.out.println("history : "+history);
            System.out.println("current state "+getCurrentHistoryData()+" saved state "+FileManager.getSavedState());*/

            if (!isDifferent(FileManager.getSavedState())){
                FileManager.noChanges(false);
            }else{
                FileManager.setHasBeenSaved(false);
                Main.mainStage.setTitle("Color Picker *");
            }
        }
    }

    public static void loadHistory(){
        if (currentPos>=0 && currentPos<history.size()){
            Main.colorList.getItems().clear();
            for (Color c : history.get(currentPos)){
                Main.colorList.getItems().add(new ColorLine(c));
            }
        }

        Main.lockButton();
        PreviewRenderer.render();
    }

    public static void save(){
        if (currentPos>=0 &&currentPos<history.size()-1) {
            if (history.size() > currentPos + 1) {
                history.subList(currentPos + 1, history.size()).clear();
            }
        }


        ArrayList<Color> data=getCurrentHistoryData();
        history.add(data);
        currentPos++;
        /*System.out.println("added to history : "+data);
        System.out.println("current pos : "+currentPos);
        System.out.println("history : "+history);*/

    }


    public static ArrayList<Color> getCurrentHistoryData(){
        ArrayList<Color> colors=new ArrayList<>();
        for (ColorLine line:Main.colorList.getItems()){
            colors.add(line.getColor());
        }
        return colors;
    }

    public static boolean isCurrentDifferent(){
        if (history==null || history.size()==0 || currentPos<0)return true;
        return isDifferent(history.get(currentPos));
    }

    public static boolean isDifferent(List<Color> other){
        if (other==null)return false;
        List<Color> current=getCurrentHistoryData();
        if (current.size()!=other.size())return true;

        for (int i=0;i<current.size();i++){
            Color c1=current.get(i),c2=other.get(i);

            if (Math.abs(c1.getRed()-c2.getRed())>0.01 || Math.abs(c1.getGreen()-c2.getGreen())>0.01 || Math.abs(c1.getBlue()-c2.getBlue())>0.01)return true;
        }
        return false;
    }
}
