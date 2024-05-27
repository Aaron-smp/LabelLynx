package com.LabelLynx.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class CustomFonts {
    private static final Logger logger = LogManager.getLogger(CustomFonts.class);
    private static final String FONTS_PATH = "/fonts/";
    public static Font actualFont = getFontFromResources("Cabin.tff");
    public static Font defaultFont = getFontFromResources("JetBrainsMono-Regular.ttf");

    private CustomFonts(){
    }

    public static Font getFontFromResources(String fontName){
        Font font = null;
        try{
            InputStream is = CustomFonts.class.getResourceAsStream(FONTS_PATH + fontName);
            assert is != null;
            font = Font.createFont(Font.TRUETYPE_FONT, is);
            font = font.deriveFont(14.0f);
        }catch (IOException | FontFormatException e) {
            logger.error("No ha sido posible cargar la fuente: {}", fontName);
        }

        actualFont = font;
        return null == font ? Font.getFont("Arial") : font;
    }

    public static Font getFontFromResources(String fontName, int size, int style){
        Font fuenteNueva = getFontFromResources(fontName);
        return fuenteNueva.deriveFont(style, size);
    }

    public static void setFontRecursively(Component component, Font font) {
        component.setFont(font);
        if (component instanceof Container) {
            Component[] components = ((Container) component).getComponents();
            for (Component child : components) {
                setFontRecursively(child, font);
            }
        }
    }

    public static float getContractRatio(Color color1, Color color2){
        float color1Ratio = getIndividualRatio(color1);
        float color2Ratio = getIndividualRatio(color2);

        return (float) ((color1Ratio+0.05) / (color2Ratio+0.05));
    }

    private static float getIndividualRatio(Color color){
        float ratio = 0.0f;
        float red = (float) color.getRed() /255;
        float blue = (float) color.getBlue() /255;
        float green = (float) color.getGreen() /255;

        red = red <= 0.03928f ? (float) (red/12.92) : (float) Math.pow(((red + 0.055)/1.055), 2.4f);
        blue = blue <= 0.03928f ? (float) (blue/12.92) : (float) Math.pow(((blue + 0.055)/1.055), 2.4f);
        green = green <= 0.03928f ? (float) (green/12.92) : (float) Math.pow(((green + 0.055)/1.055), 2.4f);

        ratio = (float) (red*0.2126 + blue*0.0722 + green*0.7152);

        return ratio;
    }

    public static ArrayList<String> listCustomFonts(){
        return new ArrayList<>(Arrays.asList("Cabin.ttf", "Greek-Freak.ttf", "Hey Comic.ttf", "Hey Comic.otf", "Jersey15-Regular.ttf", "JetBrainsMono-Regular.ttf", "LibreBaskerville-Regular.ttf", "MarioDS.ttf", "Mom.ttf", "Poppins-Regular.ttf", "Roboto-Regular.ttf"));
    }
}
