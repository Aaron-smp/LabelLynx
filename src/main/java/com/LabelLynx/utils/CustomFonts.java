package com.LabelLynx.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class CustomFonts {
    private static final Logger logger = LogManager.getLogger(CustomFonts.class);
    private static final String FONTS_PATH = "/fonts/";
    public static Font actualFont = getFontFromResources("Cabin.tff");
    public static Font defaultFont = getFontFromResources("JetBrainsMono-Regular.ttf");
    private static HashMap<ConfigText, Object> configurationText;

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

    private enum ConfigText{
        FONT, COLOR_HEADERS, COLOR_CONTENT, COLOR_SEPARATOR, FONT_SIZE_HEADERS, FONT_SIZE_CONTENT;
    }
    private static void configurationText(){
        configurationText = new HashMap<>();
        configurationText.put(ConfigText.FONT, CustomFonts.actualFont);
        configurationText.put(ConfigText.COLOR_HEADERS, new Color(0, 1, 47));
        configurationText.put(ConfigText.COLOR_CONTENT, new Color(82, 136, 66));
        configurationText.put(ConfigText.COLOR_SEPARATOR, new Color(249, 115, 0));
        configurationText.put(ConfigText.FONT_SIZE_HEADERS, 15);
        configurationText.put(ConfigText.FONT_SIZE_CONTENT, 15);
    }

    public static Font getTextFont(){
        if (configurationText != null) {
            return (Font) configurationText.get(ConfigText.FONT);
        }else{
            configurationText();
            return getTextFont();
        }
    }

    public static Color getColorHeaders(){
        if (configurationText != null) {
            return (Color) configurationText.get(ConfigText.COLOR_HEADERS);
        }else{
            configurationText();
            return getColorHeaders();
        }
    }

    public static Color getColorContent(){
        if (configurationText != null) {
            return (Color) configurationText.get(ConfigText.COLOR_CONTENT);
        }else{
            configurationText();
            return getColorContent();
        }
    }

    public static Color getColorSeparator(){
        if (configurationText != null) {
            return (Color) configurationText.get(ConfigText.COLOR_SEPARATOR);
        }else{
            configurationText();
            return getColorSeparator();
        }
    }

    public static Integer getSizeHeaders(){
        if (configurationText != null) {
            return (Integer) configurationText.get(ConfigText.FONT_SIZE_HEADERS);
        }else{
            configurationText();
            return getSizeHeaders();
        }
    }

    public static Integer getSizeContent(){
        if (configurationText != null) {
            return (Integer) configurationText.get(ConfigText.FONT_SIZE_CONTENT);
        }else{
            configurationText();
            return getSizeContent();
        }
    }

    public static void setTextFont(Font font){
        if (configurationText != null) {
            configurationText.put(ConfigText.FONT, font);
        }else{
            configurationText();
            setTextFont(font);
        }
    }
    public static void setColorHeaders(Color colorHeaders){
        if (configurationText != null) {
            configurationText.put(ConfigText.COLOR_HEADERS, colorHeaders);
        }else{
            configurationText();

        }
    }
    public static void setColorContent(Color colorContent){
        if (configurationText != null) {
            configurationText.put(ConfigText.COLOR_CONTENT, colorContent);
        }else{
            configurationText();

        }
    }
    public static void setColorSeparator(Color colorSeparator){
        if (configurationText != null) {
            configurationText.put(ConfigText.COLOR_SEPARATOR, colorSeparator);
        }else{
            configurationText();

        }
    }
    public static void setSizeHeaders(Integer sizeHeaders){
        if (configurationText != null) {
            configurationText.put(ConfigText.FONT_SIZE_HEADERS, sizeHeaders);
        }else{
            configurationText();

        }
    }
    public static void setSizeContent(Integer sizeContent){
        if (configurationText != null) {
            configurationText.put(ConfigText.FONT_SIZE_CONTENT, sizeContent);
        }else{
            configurationText();

        }
    }
}
