package com.LabelLynx.ui;

import com.LabelLynx.controlers.ScanDirectory;
import com.LabelLynx.ui.leftcontainer.NavigationBar;
import com.LabelLynx.ui.leftcontainer.ToolBar;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import java.awt.*;
import java.io.File;

@EqualsAndHashCode(callSuper = true)
@Data
public class LeftContainer extends JPanel {
    private final NavigationBar navigationBar;
    public LeftContainer(){
        super(new BorderLayout());
        add(navigationBar = new NavigationBar(), BorderLayout.CENTER);
        setBorder(BorderFactory.createEmptyBorder(3, 3, 0, 3));

        navigationBar.getButtonsNavigationBar().getOpenDirectoryButton().addActionListener((e) -> {
            File archivos = ScanDirectory.scanDirectory();
            if (null != archivos) navigationBar.updateListFiles(archivos);

        });

        navigationBar.getButtonsNavigationBar().getHideTabButton().addActionListener((e) -> {
            this.setVisible(!navigationBar.isVisible());
        });
    }
}
