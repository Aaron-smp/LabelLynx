package com.LabelLynx.ui.leftcontainer;

import com.LabelLynx.utils.Icons;
import com.formdev.flatlaf.extras.FlatSVGIcon;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class ToolBar extends JPanel {
    private JButton hideShowTabButton;
    private JButton toggleButton;
    public ToolBar(){
        super(new FlowLayout(FlowLayout.TRAILING));
        setPreferredSize(new Dimension(60, 200));
        hideShowTabButton = new JButton(Icons.getSVGIcon("hide.svg", 25));
        hideShowTabButton.setToolTipText("Ocultar directorio");
        hideShowTabButton.setPreferredSize(new Dimension(50, 50));

        toggleButton = new JButton(Icons.getSVGIcon("save.svg", 80));
        toggleButton.setToolTipText("Abrir fichero");
        toggleButton.setPreferredSize(new Dimension(50, 50));

        add(hideShowTabButton);
        add(toggleButton);
    }

    public void flipIconHideShow(boolean flip){
        if (flip){
            hideShowTabButton.setIcon(Icons.getSVGIcon("show.svg", 25));
            hideShowTabButton.setToolTipText("Mostrar directorio");
        }else{
            hideShowTabButton.setIcon(Icons.getSVGIcon("hide.svg", 25));
            hideShowTabButton.setToolTipText("Ocultar directorio");
        }
    }
}
