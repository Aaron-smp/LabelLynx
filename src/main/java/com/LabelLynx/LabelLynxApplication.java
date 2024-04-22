package com.LabelLynx;

import com.LabelLynx.ui.ContainerApp;
import com.LabelLynx.ui.menus.TopMenu;
import com.formdev.flatlaf.themes.FlatMacLightLaf;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

public class LabelLynxApplication {

    private static final Logger logger = LogManager.getLogger(LabelLynxApplication.class);

    public static void main(String[] args) {
        logger.debug("Inicio de la aplicación");
        FlatMacLightLaf.setup();
        JFrame frame = new JFrame("LabelLynx");
        frame.setJMenuBar(new TopMenu());
        frame.setContentPane(new ContainerApp());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setPreferredSize(new Dimension(950, 550));
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }
}
