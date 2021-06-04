package ihm;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

import java.util.ArrayList;
import java.util.List;

public class PreviewRenderer {

    private static ArrayList<Double> values=new ArrayList<>();;

    public static void updateRandomValues(){

        for (int i=0;i<Main.colorList.getItems().size()-values.size();i++){
            values.add(Math.random()*0.75+0.25);
        }

        for (int i=0;i<values.size()-Main.colorList.getItems().size();i++){
            values.remove(values.size()-1);
        }
    }

    public static void render(){
        PreviewRenderer.updateRandomValues();

        List<Color> colors=new ArrayList<>();
        for (ColorLine line : Main.colorList.getItems()){
            colors.add(line.getColor());


        }

        PreviewRenderer.renderBars(colors,Main.beforeCanvas);

        colors=new ArrayList<>();
        for (ColorLine line : Main.colorList.getItems()){
            colors.add(ColorLine.toGrayscale(line.getColor()));
        }

        PreviewRenderer.renderBars(colors,Main.afterCanvas);
    }

    private static void renderBars(List<Color> colors,Canvas canvas){

        double barWidth=canvas.getWidth()/colors.size();
        double marge=barWidth*0.1;

        PreviewRenderer.renderBackgroundGrid(canvas,new Color(0.95,0.95,0.95,1),new Color(0.75,0.75,0.75,1),barWidth,10);

        //System.out.println(barWidth);
        GraphicsContext gc=canvas.getGraphicsContext2D();

        //System.out.println(colors);

        for (int i=0;i<colors.size();i++){
            gc.setFill(colors.get(i));
            double height=values.get(i)*canvas.getHeight();

            gc.fillRect(i*barWidth+marge,canvas.getHeight()-height,barWidth-2*marge,height);
        }
    }

    private static void renderBackgroundGrid(Canvas canvas,Color bgColor, Color gridColor, double gridSizeX, double gridSizeY){
        GraphicsContext gc=canvas.getGraphicsContext2D();

        gc.setFill(bgColor);
        gc.fillRect(0,0,canvas.getWidth(),canvas.getHeight());

        gc.setStroke(gridColor);
        for (int y=0;y<canvas.getHeight();y+=gridSizeY){
            gc.strokeLine(0,y,canvas.getWidth(),y);
        }

        for (int x=0;x<canvas.getWidth();x+=gridSizeX){
            gc.strokeLine(x,0,x,canvas.getHeight());
        }
    }
}
