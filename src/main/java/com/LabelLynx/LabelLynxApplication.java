package com.LabelLynx;

import com.LabelLynx.ui.ContainerApp;
import com.LabelLynx.ui.menus.TopMenu;
import com.LabelLynx.utils.CustomFonts;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

import static com.LabelLynx.utils.CustomFonts.setFontRecursively;

public class LabelLynxApplication {

    private static final Logger logger = LogManager.getLogger(LabelLynxApplication.class);
    public static JFrame ventana;
    
    public static void main(String[] args) {
        logger.debug("Inicio de la aplicación");
        FlatMacLightLaf.setup();
        ventana = new JFrame("LabelLynx");
        ventana.setJMenuBar(new TopMenu());
        ventana.setContentPane(new ContainerApp());
        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setPreferredSize(new Dimension(950, 550));
        ventana.pack();
        ventana.setLocationRelativeTo(null);
        setFontRecursively(ventana, CustomFonts.defaultFont);
        ventana.setVisible(true);
    }


}
