package ihm;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.text.DecimalFormat;
import java.util.Collections;

public class ColorLine extends HBox{

    Main main;
    public ColorLine(Main main,Color defaultColor){
        this.main=main;

        Button bUp=new Button("\uD83E\uDC09");
        bUp.addEventHandler(ActionEvent.ACTION, e->{
            try {
                Collections.swap(main.colorList.getItems(), getColorListIndex(), getColorListIndex() - 1);
            }catch (IndexOutOfBoundsException err){
                System.out.println("already at the top");
            }
        });
        Button bDown=new Button("\uD83E\uDC0B");
        bDown.addEventHandler(ActionEvent.ACTION, e->{
            try {
                Collections.swap(main.colorList.getItems(), getColorListIndex(), getColorListIndex() + 1);
            }catch (IndexOutOfBoundsException err){
                System.out.println("already at the bottom");
            }
        });
        Button bDelete=new Button("\uD83D\uDDD1");
        bDelete.addEventHandler(ActionEvent.ACTION, e->{
            delete();
        });

        Separator line1=new Separator();
        line1.setOrientation(Orientation.VERTICAL);
        ColorPicker colorPicker=new ColorPicker(defaultColor);
        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                update();
            }
        });
        TextField colorField=new TextField();
        colorField.setEditable(false);
        colorField.getStyleClass().add("copyable-label");
        //colorField.setTooltip(new Tooltip("Click to copy"));

        Separator line2=new Separator();
        line2.setOrientation(Orientation.VERTICAL);
        Canvas canvasColor=new Canvas(200,25);
        Canvas canvasGrayscale=new Canvas(200,25);

        colorPicker.getStyleClass().add("button");
        this.getChildren().addAll(bUp,bDown,bDelete,line1,colorPicker,colorField,line2,canvasColor,canvasGrayscale);




        this.update();
    }

    public void update(){
        this.setTextField();
        this.setCanvas();
    }

    public void delete(){
        main.colorList.getItems().remove(this);
    }

    public ColorLine(Main main){
        this(main,new Color(1,1,1,1));
    }

    public TextField getTextField(){
        return (TextField) this.getChildren().get(5);
    }
    public void setTextField() {
        getTextField().setText(ColorLine.colorToString(getColor(), this.main.colorMode.getValue()));
    }
    public Canvas getCanvasColor(){
        return (Canvas)this.getChildren().get(7);
    }
    public Canvas getCanvasGrayscale(){
        return (Canvas)this.getChildren().get(8);
    }
    public void setCanvas(){
        GraphicsContext gc=this.getCanvasColor().getGraphicsContext2D();
        gc.setFill(this.getColor());
        gc.fillRect(0,0,200,25);
        gc.setStroke(new Color(0,0,0,1));
        gc.strokeRect(0,0,200,25);
        gc=this.getCanvasGrayscale().getGraphicsContext2D();
        gc.setFill(ColorLine.toGrayscale(this.getColor()));
        gc.fillRect(0,0,200,25);
        gc.setStroke(new Color(0,0,0,1));
        gc.strokeRect(0,0,200,25);
    }

    public Color getColor(){
        return ((ColorPicker)(this.getChildren().get(4))).getValue();
    }

    public int getColorListIndex(){
        return this.main.colorList.getItems().indexOf(this);
    }

    public static double grayScaleLevel(Color c){
        return 0.3*c.getRed()+0.59*c.getGreen()+0.11*c.getBlue();
    }

    public static Color toGrayscale(Color c){
        double grayScale=ColorLine.grayScaleLevel(c);
        return new Color(grayScale,grayScale,grayScale,1);
    }

    public static String colorToString(Color c,String colorMode){
        String res="";

        double r=c.getRed(),
                g=c.getGreen(),
                b=c.getBlue(),
                h=c.getHue(),
                s=c.getSaturation(),
                br=c.getBrightness();
        String hexCode=c.toString();

        if (colorMode.equals("r,g,b")){
            res+=(int)(r*255)+", "+(int)(g*255)+", "+(int)(b*255);
        }else if (colorMode.equals("r,g,b (1)")){
            DecimalFormat f = new DecimalFormat("0.00");
            res+=(Math.round(r * 1000.00) / 1000.00)+", "+(Math.round(g * 1000.00) / 1000.00)+", "+(Math.round(b * 1000.00) / 1000.00);
        }else if (colorMode.equals("h,s,b")){
            res+=(int)(h)+", "+(int)(s*255)+", "+(int)(br*255);
        }else if (colorMode.equals("h,s,b (1)")){
            DecimalFormat f = new DecimalFormat("0.00");
            res+=(Math.round(h * 1000.00) / 1000.00)+", "+(Math.round(s * 1000.00) / 1000.00)+", "+(Math.round(br * 1000.00) / 1000.00);
        }else if (colorMode.equals("#rrggbb")){
            res+="#"+hexCode.substring(2,hexCode.length()-2);
        }else if (colorMode.equals("0xrrggbb")){
            res+=hexCode.substring(0,hexCode.length()-2);
        }

        return res;
    }
}
