package com.LabelLynx.ui.customcomponents;

import com.LabelLynx.utils.Icons;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;

import static com.LabelLynx.ui.rightcontainer.ContainerEditorAnotations.tabsEditor;

@EqualsAndHashCode(callSuper = true)
@Data
public class CloseTabsPanel extends JPanel {
    private static final Logger logger = LogManager.getLogger(CloseTabsPanel.class);
    private JLabel tabName;
    private JButton closeButton;
    private final String idNum;
    public CloseTabsPanel(String name, String id){
        this.idNum = id;
        super.setLayout(new FlowLayout(FlowLayout.LEFT));
        setOpaque(false);
        tabName = new JLabel(name);
        closeButton = new JButton(Icons.getSVGIcon("close.svg", 50));
        closeButton.setBorderPainted(false);
        super.add(tabName);
        super.add(closeButton);

        closeButton.addActionListener(e -> {
            logger.info("Pestaña a cerrar {}", idNum);
            tabsEditor.removeTabAt(tabsEditor.getEditorByHash(id));
            tabsEditor.getEditors().remove(tabsEditor.getEditorByHash(id));
        });
    }
}
