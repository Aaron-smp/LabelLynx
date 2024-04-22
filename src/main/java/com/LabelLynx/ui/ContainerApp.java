package com.LabelLynx.ui;

import com.LabelLynx.ui.leftcontainer.NavigationBar;
import com.LabelLynx.ui.leftcontainer.ToolBar;
import com.LabelLynx.ui.rightcontainer.ContainerEditorAnotations;
import com.LabelLynx.ui.rightcontainer.FooterInfo;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import static javax.swing.JSplitPane.HORIZONTAL_SPLIT;

@EqualsAndHashCode(callSuper = true)
@Data
public class ContainerApp extends JPanel {
    private static ToolBar toolBar;
    private static JSplitPane panel;
    private static FooterInfo footerInfo;
    private static RightContainer rightContainer;
    private static  LeftContainer leftContainer;
    public ContainerApp(){
        super(new BorderLayout());
        panel = new JSplitPane(HORIZONTAL_SPLIT, leftContainer = new LeftContainer(), rightContainer = new RightContainer());
        panel.setDividerSize(5);
        add(panel, BorderLayout.CENTER);
        add(toolBar = new ToolBar(), BorderLayout.WEST);
        add(footerInfo = new FooterInfo(), BorderLayout.SOUTH);

        toolBar.getHideShowTabButton().addActionListener((e) -> {
            panel.getLeftComponent().setVisible(!panel.getLeftComponent().isVisible());
            panel.setDividerLocation(0.3);
            panel.revalidate();
            panel.repaint();
            toolBar.flipIconHideShow(!panel.getLeftComponent().isVisible());
        });

        NavigationBar navigationBar = ((LeftContainer) panel.getLeftComponent()).getNavigationBar();
        navigationBar.getButtonsNavigationBar().getHideTabButton().addActionListener((e) -> {
            toolBar.flipIconHideShow(true);
        });

        navigationBar.getListFiles().addMouseListener(new MouseAdapter(){
            @Override
            public void mousePressed(MouseEvent e) {
                super.mousePressed(e);
                if(e.getClickCount() == 2) {
                    TreePath selPath = navigationBar.getListFiles().getPathForLocation(e.getX(), e.getY());
                    if(selPath != null){
                        if(!navigationBar.getFileToOpen().isDirectory()){
                            ContainerEditorAnotations.tabsEditor.addTabWithText(navigationBar.getFileToOpen());
                        }
                    }
                }
            }
        });
    }
}
