package com.LabelLynx.utils;

import com.LabelLynx.LabelLynxApplication;
import com.LabelLynx.ui.leftcontainer.ButtonsNavigationBar;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Objects;

public class Icons {
    public final static String ICONS_PATH = "/icons/ui/";
    private static final Logger logger = LogManager.getLogger(Icons.class);
    public static Icon getIconUI(String filename) {
        return new ImageIcon(Objects.requireNonNull(Icons.class.getResource(ICONS_PATH.concat(filename))).getFile());
    }

    public static FlatSVGIcon getSVGIcon(String filename, int size) {
        try {
            FlatSVGIcon flatSVGIcon = new FlatSVGIcon(new BufferedInputStream(Objects.requireNonNull(ButtonsNavigationBar.class.getResourceAsStream(ICONS_PATH.concat(filename)))));
            //TODO añadir el color del svg
            return flatSVGIcon.derive(size/100f);
        }catch (IOException e){
            logger.error("Error al cargar el archivo: ".concat(filename).concat(" con tamaño " + size));
        }
        return null;
    }
}
