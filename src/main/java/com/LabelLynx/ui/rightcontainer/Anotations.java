package com.LabelLynx.ui.rightcontainer;

import javax.swing.*;
import java.awt.*;

public class Anotations extends JPanel {
    protected Anotations(){
        super.setLayout(new BorderLayout());
        add(new Label("Anotaciones"), BorderLayout.NORTH);
        TextArea textArea = new TextArea("Vacío");
        textArea.setEditable(false);
        add(textArea, BorderLayout.CENTER);
        setMaximumSize(new Dimension(150, Integer.MAX_VALUE));
        setPreferredSize(new Dimension(150, 800));
        setMinimumSize(new Dimension(50, 250));
        setBorder(BorderFactory.createEtchedBorder());
    }
}
