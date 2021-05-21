package ihm;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Separator;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.text.DecimalFormat;

public class ColorLine extends HBox {

    Main main;
    public ColorLine(Main main,Color defaultColor){
        this.main=main;

        Button bUp=new Button("\uD83E\uDC09");
        Button bDown=new Button("\uD83E\uDC0B");
        Button bDelete=new Button("\uD83D\uDDD1");
        bDelete.addEventHandler(ActionEvent.ACTION, e->{
            delete();
        });

        Separator line1=new Separator();
        line1.setOrientation(Orientation.VERTICAL);
        ColorPicker colorPicker=new ColorPicker(defaultColor);
        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                setTextField();
            }
        });
        TextField colorField=new TextField();
        colorField.setEditable(false);
        colorField.getStyleClass().add("copyable-label");

        Separator line2=new Separator();
        line2.setOrientation(Orientation.VERTICAL);
        Canvas canvasColor=new Canvas();
        Canvas canvasGrayscale=new Canvas();

        colorPicker.getStyleClass().add("button");
        this.getChildren().addAll(bUp,bDown,bDelete,line1,colorPicker,colorField,line2,canvasColor,canvasGrayscale);

        this.setTextField();
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
    public void setTextField(){
        getTextField().setText(ColorLine.colorToString(getColor(),this.main.colorMode.getValue()));
    }



    public Color getColor(){
        return ((ColorPicker)(this.getChildren().get(4))).getValue();
    }

    public static String colorToString(Color c,String colorMode){
        String res="";

        double r=c.getRed(),
                g=c.getGreen(),
                b=c.getBlue(),
                h=c.getHue(),
                s=c.getSaturation(),
                br=c.getBrightness();


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
        }

        return res;
    }


}
