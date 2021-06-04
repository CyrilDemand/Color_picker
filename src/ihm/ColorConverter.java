package ihm;

import javafx.scene.paint.Color;

import java.text.DecimalFormat;

public class ColorConverter {
    public static double grayScaleLevel(Color c){
        return 0.3*c.getRed()+0.59*c.getGreen()+0.11*c.getBlue();
    }

    public static Color toGrayscale(Color c){
        double grayScale=ColorConverter.grayScaleLevel(c);
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
