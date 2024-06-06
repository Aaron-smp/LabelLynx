package com.LabelLynx.ui.rightcontainer;

import com.LabelLynx.ui.dialog.DialogCustomEditor;
import com.LabelLynx.ui.rightcontainer.contenteditor.FileTableCustom;
import com.LabelLynx.ui.rightcontainer.contenteditor.TextEditor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

import static com.LabelLynx.LabelLynxApplication.ventana;

@EqualsAndHashCode(callSuper = true)
@Data
public class Editor extends JPanel {
    /*Esta clase representa la vista de una pestaña en el editor*/
    private static final Logger logger = LogManager.getLogger(Editor.class);
    private final String identifier;
    private TextEditor textPane;
    private JScrollPane scrollPaneTable;
    private JScrollPane scrollPaneText;
    private JPanel panelCardLayout;
    public static final String DIVIDED_VIEW = "dividedView";
    public static final String TEXT_VIEW = "textView";
    public static final String TABLE_VIEW = "tableView";
    private FileTableCustom tablaFile;
    private File file;
    private JPanel panelConfigEditor;

    /* Botones configuración de editor*/
    private JToggleButton editTable;
    private JButton configEditorFont;

    public Editor() {
        super(new BorderLayout());
        this.identifier = idInit(null);
        initializeUI();
        initView(DIVIDED_VIEW);
        logger.info("Nueva pestaña abierta.");
    }

    public Editor(String path) {
        super(new BorderLayout());
        this.identifier = idInit(path);
        file = new File(path);
        initializeUI();
        initView(DIVIDED_VIEW);

        logger.info("Nueva pestaña abierta.");
    }

    private void initializeUI(){
        panelCardLayout = new JPanel(new CardLayout());
        this.add(panelCardLayout, BorderLayout.CENTER);
        //Panel donde se pinta el texto del fichero
        textPane = new TextEditor(file);
        scrollPaneText = new JScrollPane(textPane);
        scrollPaneText.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPaneText.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPaneTable = new JScrollPane(tablaFile = new FileTableCustom(file));
        scrollPaneTable.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        scrollPaneTable.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        EventosDocumento eventosDocumento = new EventosDocumento(textPane, 3, tablaFile);
        scrollPaneText.setRowHeaderView(eventosDocumento);

        panelConfigEditor = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        panelConfigEditor.add(configEditorFont = new JButton("Ajustes editor"));
        panelConfigEditor.add(editTable = new JToggleButton("Editar"));
        configEditorFont.addActionListener(e -> {
            DialogCustomEditor dialog = new DialogCustomEditor(ventana);
        });
        editTable.addActionListener((e) -> {
            tablaFile.getCustomTableModel().setEditable(!tablaFile.getCustomTableModel().getEditable());
        });
        add(panelConfigEditor, BorderLayout.NORTH);
    }
    public void initView(String view){
        CardLayout cl = (CardLayout)(panelCardLayout.getLayout());

        if(view.equals(DIVIDED_VIEW)){
            addDividedView(cl);
            editTable.setVisible(true);
        }else {
            panelCardLayout.removeAll();
            if(view.equals(TEXT_VIEW)){
                showTextView();
                editTable.setVisible(false);
            }else{
                editTable.setVisible(true);
                showTableView();
            }
            cl.show(panelCardLayout, view);
        }

        panelCardLayout.revalidate();
        panelCardLayout.repaint();
    }
    public void showTextView() {
        panelCardLayout.add(TEXT_VIEW, scrollPaneText);
    }
    public void showTableView() {
        panelCardLayout.add(TABLE_VIEW, scrollPaneTable);
    }
    public void addDividedView(CardLayout cl){
        JSplitPane vistaDividida = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scrollPaneText, scrollPaneTable);
        panelCardLayout.removeAll();
        panelCardLayout.add(DIVIDED_VIEW, vistaDividida);
        vistaDividida.setDividerLocation(0.5);
        vistaDividida.setDividerLocation(300);
        cl.show(panelCardLayout, DIVIDED_VIEW);
    }
    public String idInit(String path) {
        MessageDigest messageDigest = null;
        byte[] hash = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
            messageDigest.update(path == null ? String.valueOf(System.nanoTime()).getBytes(StandardCharsets.UTF_8) : path.getBytes(StandardCharsets.UTF_8));
            hash = messageDigest.digest();
        } catch (NoSuchAlgorithmException e) {
            logger.error("Error al generar el hash de la pestaña");
        }
        assert hash != null;
        return HexFormat.of().formatHex(hash);
    }
}
