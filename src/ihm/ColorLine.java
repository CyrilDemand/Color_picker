package ihm;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Orientation;
import javafx.scene.Node;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;

import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.util.Collections;

public class ColorLine extends HBox{
    private static final int CANVAS_WIDTH=100;

    public ColorLine(Color defaultColor){

        Button bUp=new Button("\uD83E\uDC09");
        bUp.addEventHandler(ActionEvent.ACTION, e->{
            this.moveUp();
        });
        Button bDown=new Button("\uD83E\uDC0B");
        bDown.addEventHandler(ActionEvent.ACTION, e->{
            this.moveDown();
        });
        Button bDelete=new Button("\uD83D\uDDD1");
        bDelete.addEventHandler(ActionEvent.ACTION, e->{
            delete();
        });

        Separator line1=new Separator();
        line1.setOrientation(Orientation.VERTICAL);
        line1.setStyle("-fx-padding: 0 10 0 10;");

        ColorPicker colorPicker=new ColorPicker(defaultColor);
        colorPicker.setOnAction(new EventHandler() {
            public void handle(Event t) {
                update(true);
            }
        });
        TextField colorField=new TextField();
        colorField.setEditable(false);
        colorField.getStyleClass().add("copyable-label");

        Button copyButton=new Button("Copy");
        copyButton.addEventHandler(ActionEvent.ACTION, e->{
            String myString = ((TextField)this.getChildren().get(5)).getText();
            //System.out.println(myString);
            StringSelection stringSelection = new StringSelection(myString);
            Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
            clipboard.setContents(stringSelection, null);
        });

        Separator line2=new Separator();
        line2.setOrientation(Orientation.VERTICAL);
        line2.setStyle("-fx-padding: 0 10 0 10;");

        Canvas canvasColor=new Canvas(CANVAS_WIDTH,25);
        Canvas canvasGrayscale=new Canvas(CANVAS_WIDTH,25);

        colorPicker.getStyleClass().add("button");
        this.getChildren().addAll(bUp,bDown,bDelete,line1,colorPicker,colorField,copyButton,line2,canvasColor,canvasGrayscale);

        for (Node n : this.getChildren()){
            n.focusedProperty().addListener(new ChangeListener<Boolean>(){
                @Override
                public void changed(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
                    if (newPropertyValue){
                        //System.out.println("Textfield on focus");
                        selectCorrespondingListViewItem();
                    }
                }
            });
        }

        this.update(false);
    }

    public void moveUp(){
        try {
            Collections.swap(Main.colorList.getItems(), getColorListIndex(), getColorListIndex() - 1);
            selectCorrespondingListViewItem();
            FileManager.changeMade(true);
        }catch (IndexOutOfBoundsException err){
            //System.out.println("already at the top");
        }
    }

    public void moveDown(){
        try {
            Collections.swap(Main.colorList.getItems(), getColorListIndex(), getColorListIndex() + 1);
            selectCorrespondingListViewItem();
            FileManager.changeMade(true);
        }catch (IndexOutOfBoundsException err){
            //System.out.println("already at the bottom");
        }
    }

    public void selectCorrespondingListViewItem(){
        int i=Main.colorList.getItems().indexOf(this);
        //System.out.println(i);
        Main.colorList.getFocusModel().focus(i);
        Main.colorList.getSelectionModel().select(i);
    }

    public void update(boolean change){
        this.setTextField();
        this.setCanvas();
        PreviewRenderer.render();
        if (change)
        FileManager.changeMade(true);
    }

    public void delete(){
        Main.colorList.getItems().remove(this);
        Main.lockButton();
        PreviewRenderer.render();
        FileManager.changeMade(true);
    }

    public ColorLine(Main main){
        this(new Color(1,1,1,1));
    }

    public TextField getTextField(){
        return (TextField) this.getChildren().get(5);
    }
    public void setTextField() {
        getTextField().setText(ColorConverter.colorToString(getColor(), Main.colorMode.getValue()));
    }
    public Canvas getCanvasColor(){
        return (Canvas)this.getChildren().get(8);
    }
    public Canvas getCanvasGrayscale(){
        return (Canvas)this.getChildren().get(9);
    }
    public void setCanvas(){
        GraphicsContext gc=this.getCanvasColor().getGraphicsContext2D();
        gc.setFill(this.getColor());
        gc.fillRect(0,0,CANVAS_WIDTH,25);
        gc.setStroke(new Color(0,0,0,1));
        gc.strokeRect(0,0,CANVAS_WIDTH,25);
        gc=this.getCanvasGrayscale().getGraphicsContext2D();
        gc.setFill(ColorConverter.toGrayscale(this.getColor()));
        gc.fillRect(0,0,CANVAS_WIDTH,25);
        gc.setStroke(new Color(0,0,0,1));
        gc.strokeRect(0,0,CANVAS_WIDTH,25);
    }

    public Color getColor(){
        return ((ColorPicker)(this.getChildren().get(4))).getValue();
    }

    public void setColor(Color bestColor) {
        ((ColorPicker)this.getChildren().get(4)).setValue(bestColor);
        this.setCanvas();
        this.setTextField();
    }

    public int getColorListIndex(){
        return Main.colorList.getItems().indexOf(this);
    }
}
