package com.LabelLynx.ui.rightcontainer;

import javax.swing.*;
import java.awt.*;

public class FooterInfo extends JPanel {

    public FooterInfo(){
        super(new BorderLayout());
        JPanel infoPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        infoPanel.add(new Label("Prueba 1"));
        infoPanel.add(new Label("Prueba 2"));
        infoPanel.add(new Label("Prueba 3"));
        infoPanel.add(new Label("Prueba 4"));
        infoPanel.add(new Label("Prueba 5"));
        infoPanel.add(new Label("Hora: 21:50"));
        add(infoPanel, BorderLayout.CENTER);
        add(new JSeparator(), BorderLayout.NORTH);
        setMaximumSize(new Dimension(Integer.MAX_VALUE, 5));
    }
}
