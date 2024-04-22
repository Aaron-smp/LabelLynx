package com.LabelLynx.ui.leftcontainer;

import com.LabelLynx.LabelLynxApplication;
import com.LabelLynx.utils.Icons;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.Objects;

@EqualsAndHashCode(callSuper = true)
@Data
public class NavigationBar extends JPanel {

    private static final Logger logger = LogManager.getLogger(NavigationBar.class);
    private JScrollPane scrollPane;
    private JTree listFiles;
    private ButtonsNavigationBar buttonsNavigationBar;
    private DefaultTreeModel model;
    private DefaultMutableTreeNode root;
    private File fileToOpen;
    public NavigationBar() {
        super(new BorderLayout());
        setDoubleBuffered(true);
        root = new DefaultMutableTreeNode("Selecciona directorio");
        model = new DefaultTreeModel (root);
        add(scrollPane = new JScrollPane(listFiles = new JTree(model)), BorderLayout.CENTER);

        add(buttonsNavigationBar = new ButtonsNavigationBar(), BorderLayout.NORTH);
        setMinimumSize(new Dimension(150, 400));
        setPreferredSize(new Dimension(200, 600));
        setBorder(BorderFactory.createEtchedBorder());

        actualizarIconos(1);

        MouseListener ml = new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                int selRow = listFiles.getRowForLocation(e.getX(), e.getY());
                TreePath selPath = listFiles.getPathForLocation(e.getX(), e.getY());
                if(selRow != -1) {
                    if(e.getClickCount() == 2) {
                        if(selPath != null){
                            DefaultMutableTreeNode node = (DefaultMutableTreeNode) selPath.getLastPathComponent();
                            fileToOpen = convertNodeToFile(node);
                            logger.info("Archivo seleccionado: " + fileToOpen.getAbsolutePath());
                        }
                    }
                }
            }
        };
        listFiles.addMouseListener(ml);
    }

    public File convertNodeToFile(DefaultMutableTreeNode node){
        if(node.isLeaf()){
            StringBuilder sb = new StringBuilder();
            String CONTRABAR = "\\";
            for(int i = 0; i < node.getPath().length; i++){
                if(i == 0){
                    sb.append(node.getPath()[0]);
                }else{
                    sb.append(CONTRABAR).append(node.getPath()[i]);
                }
            }
            return new File(sb.toString());
        }
        return null;
    }
    public void updateListFiles(File directorio){
        DefaultMutableTreeNode hijo;
        DefaultMutableTreeNode padre = new DefaultMutableTreeNode(directorio.getPath());
        for(File file : Objects.requireNonNull(directorio.listFiles())){

            if(file.isDirectory()){
                hijo = fillDirectory(file);
                model.insertNodeInto(hijo, padre, 0);
            }else{
                hijo = new DefaultMutableTreeNode(file.getName());
                model.insertNodeInto(hijo, padre, 0);
            }


        }

        root = padre;
        model.setRoot(root);
        listFiles.setModel(model);

        actualizarIconos(0);
    }

    private DefaultMutableTreeNode fillDirectory(File directory){
        DefaultMutableTreeNode padre = new DefaultMutableTreeNode(directory.getName());
        File[] fileArrayList = directory.listFiles();
        if (fileArrayList != null){
            for (File f : fileArrayList) {
                if (f.isDirectory()) {
                    DefaultMutableTreeNode child = fillDirectory(f);
                    padre.add(child);
                    model.nodeChanged(child);
                } else {
                    padre.add(new DefaultMutableTreeNode(f.getName()));
                }
                model.nodeChanged(padre);
            }
        }

        return padre;
    }
    public void actualizarIconos(int seleccion){
        DefaultTreeCellRenderer render= (DefaultTreeCellRenderer) listFiles.getCellRenderer();
        if(seleccion == 0){
            render.setLeafIcon(Icons.getSVGIcon("document.svg", 3));
            render.setOpenIcon(Icons.getSVGIcon("folder.svg", 2));
            render.setClosedIcon(Icons.getSVGIcon("folder.svg", 2));
        }else{
            render.setLeafIcon(Icons.getSVGIcon("warning.svg", 70));
            render.setOpenIcon(Icons.getSVGIcon("warning.svg", 70));
            render.setClosedIcon(Icons.getSVGIcon("warning.svg", 70));
        }

    }
}
