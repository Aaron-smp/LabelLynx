package com.LabelLynx.ui.leftcontainer;

import com.LabelLynx.utils.Icons;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class ButtonsNavigationBar extends JPanel {
    private JButton openDirectoryButton;
    private JButton hideTabButton;
    public ButtonsNavigationBar(){
        super(new FlowLayout(FlowLayout.RIGHT));
        add(openDirectoryButton = new JButton(Icons.getSVGIcon("directory.svg", 17)));

        add(hideTabButton = new JButton(Icons.getSVGIcon("close.svg", 70)));
        setBorder(BorderFactory.createEtchedBorder());
    }
}
