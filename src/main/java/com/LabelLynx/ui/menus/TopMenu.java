package com.LabelLynx.ui.menus;

import javax.swing.*;
import java.awt.*;

public class TopMenu extends JMenuBar{

    public TopMenu (){
        JMenu fileMenu = new JMenu("Archivo");
        JMenu editMenu = new JMenu("Editar");
        JMenu tools = new JMenu("Herramientas");
        JMenu help = new JMenu("Ayuda");

        JMenuItem newItem = new JMenuItem("Nuevo");
        newItem.addActionListener((e) -> System.out.println("Nuevo"));
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

        this.add(fileMenu);
        this.add(editMenu);
        this.add(tools);
        this.add(help);
    }
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        frame.setSize(new Dimension(600, 300));
        frame.setLocationRelativeTo(null);
        frame.setJMenuBar(new TopMenu());
        frame.setVisible(true);
    }
}
