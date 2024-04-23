package com.LabelLynx.ui.menus;

import com.LabelLynx.ui.rightcontainer.TabsEditor;

import javax.swing.*;
import java.awt.*;

import static com.LabelLynx.ui.rightcontainer.ContainerEditorAnotations.tabsEditor;
import static com.LabelLynx.ui.rightcontainer.Editor.*;

public class TopMenu extends JMenuBar{

    public TopMenu (){
        JMenu fileMenu = new JMenu("Archivo");
        JMenu editMenu = new JMenu("Editar");
        JMenu tools = new JMenu("Herramientas");
        JMenu help = new JMenu("Ayuda");
        JMenu view = new JMenu("Vista");

        JMenuItem newItem = new JMenuItem("Nuevo");
        JMenuItem openItem = new JMenuItem("Abrir");
        JMenuItem saveItem = new JMenuItem("Guardar");
        JMenuItem exitItem = new JMenuItem("Salir");

        fileMenu.add(newItem);
        fileMenu.add(openItem);
        fileMenu.add(saveItem);
        fileMenu.addSeparator();
        fileMenu.add(exitItem);

        JMenuItem undoItem = new JMenuItem("Deshacer");
        JMenuItem cut = new JMenuItem("Cortar");

        editMenu.add(undoItem);
        editMenu.add(cut);

        JMenuItem dobleView = new JMenuItem("Vista dividida");
        dobleView.addActionListener((e) -> {
            tabsEditor.changeViewEditor(0);
        });
        JMenuItem textView = new JMenuItem("Vista solo texto");
        textView.addActionListener((e) -> {
            tabsEditor.changeViewEditor(1);
        });
        JMenuItem onlyReadView = new JMenuItem("Vista solo lectura");
        onlyReadView.addActionListener((e) -> {
            tabsEditor.changeViewEditor(2);
        });

        view.add(dobleView);
        view.add(textView);
        view.add(onlyReadView);

        this.add(fileMenu);
        this.add(editMenu);
        this.add(view);
        this.add(tools);
        this.add(help);
    }
}
