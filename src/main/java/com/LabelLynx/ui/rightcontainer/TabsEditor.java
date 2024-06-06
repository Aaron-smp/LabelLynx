package com.LabelLynx.ui.rightcontainer;

import com.LabelLynx.controlers.CsvSeparator;
import com.LabelLynx.ui.customcomponents.CloseTabsPanel;
import com.LabelLynx.utils.CustomFonts;
import com.LabelLynx.utils.Icons;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.ArrayList;

import static com.LabelLynx.ui.rightcontainer.Editor.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class TabsEditor extends JTabbedPane{
    private static final Logger logger = LogManager.getLogger(TabsEditor.class);
    private ArrayList<Editor> editors;
    public TabsEditor(){
        Editor nuevo1 = new Editor();
        editors = new ArrayList<>();
        editors.add(nuevo1);
        super.add("Nuevo 1", nuevo1);
        super.setTabComponentAt(0, new CloseTabsPanel("Nuevo 1", nuevo1.getIdentifier()));
        super.insertTab("+", new ImageIcon(), null, "Nuevo archivo", getTabCount());
        super.setTabComponentAt(1, new JLabel(Icons.getSVGIcon("add.svg", 70)));
        setMinimumSize(new Dimension(400, 400));
        setMaximumSize(new Dimension(Integer.MAX_VALUE, Integer.MAX_VALUE));
        setBorder(BorderFactory.createEtchedBorder());

        addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JTabbedPane tabRonda = (JTabbedPane) e.getSource();
                logger.info("Pestaña seleccionada: " + tabRonda.getSelectedIndex());
                if (tabRonda.getSelectedIndex() == getTabCount()-1 && tabRonda.getTitleAt(tabRonda.getSelectedIndex()).equals("+")) {
                    int tab = tabRonda.getTabCount();
                    Editor editor = new Editor();
                    editors.add(editor);
                    logger.info("Nuevo editor añadido por click, numero de editores {}", editors.size());
                    tabRonda.insertTab("Nuevo " + tab, null, editor, "Nuevo " + tab, tab-1);
                    tabRonda.setTabComponentAt(tab-1, new CloseTabsPanel("Nuevo " + tab, editor.getIdentifier()));
                    tabRonda.setSelectedIndex(getTabCount()-2);
                    CustomFonts.setFontRecursively(tabRonda, CustomFonts.actualFont);
                }
            }
        });


    }

    public void addTabWithText(File file){
        if(null != file){
            if (CsvSeparator.isCsvOrProp(file)) {
                Editor newEditor = new Editor(file.getAbsolutePath());
                if(getEditorByHash(newEditor.getIdentifier()) == -1){
                    editors.add(newEditor);
                    logger.info("Nuevo editor añadido desde archivo, numero de editores {}", editors.size());
                    int tab = getTabCount();
                    super.insertTab(file.getName(), null, newEditor, file.getName(), tab-1);
                    super.setTabComponentAt(tab-1, new CloseTabsPanel(file.getName(), newEditor.getIdentifier()));
                    super.setSelectedIndex(getTabCount()-2);
                    CustomFonts.setFontRecursively(this, CustomFonts.actualFont);
                }else {
                    JOptionPane.showMessageDialog(this, "El archivo ya está en el editor", "Aviso", JOptionPane.WARNING_MESSAGE);
                }

            }else {
                JOptionPane.showMessageDialog(this, "El archivo no es ni csv ni de propiedades", "Aviso", JOptionPane.WARNING_MESSAGE);
            }
        }
    }

    public int getEditorByHash(String hash){
        for(int i = 0; i < editors.size(); i++){
            if(editors.get(i).getIdentifier().equals(hash)){
                return i;
            }
        }
        return -1;
    }

    public void changeViewEditor(int view){
        String vista = switch (view) {
            case 0 -> DIVIDED_VIEW;
            case 1 -> TEXT_VIEW;
            case 2 -> TABLE_VIEW;
            default -> "";
        };

        for(Editor editor : editors){
            editor.initView(vista);
        }
    }
}
