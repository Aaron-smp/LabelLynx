package com.LabelLynx.ui.rightcontainer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;

@EqualsAndHashCode(callSuper = true)
@Data
public class Editor extends JPanel {
    private static final Logger logger = LogManager.getLogger(Editor.class);
    private final String identifier;
    private JTextPane textPane;
    private JScrollPane scrollPane;
    private JPanel panelCardLayout;
    public static final String DIVIDED_VIEW = "dividedView";
    public static final String TEXT_VIEW = "textView";
    public static final String TABLE_VIEW = "tableView";
    private JTable tablaCSV;
    public Editor() {
        super(new BorderLayout());
        this.identifier = idInit(null);
        panelCardLayout = new JPanel(new CardLayout());
        this.add(panelCardLayout, BorderLayout.CENTER);
        textPane = new JTextPane();
        scrollPane = new JScrollPane(tablaCSV = new JTable());
        init(DIVIDED_VIEW);
        textPane.setText("Escribe Aquí");

        logger.info("Nueva pestaña abierta.");
    }

    public Editor(String texto, String path) {
        this.identifier = idInit(path);
        textPane = new JTextPane();
        scrollPane = new JScrollPane(tablaCSV = new JTable());
        init(DIVIDED_VIEW);
        textPane.setText(texto);

        logger.info("Nueva pestaña abierta.");
    }

    public void init(String view){
        CardLayout cl = (CardLayout)(panelCardLayout.getLayout());

        if(view.equals(DIVIDED_VIEW)){
            addDividedView(cl);
        }else {
            panelCardLayout.removeAll();
            if(view.equals(TEXT_VIEW)){
                showTextView();
            }else{
                showTableView();
            }
            cl.show(panelCardLayout, view);
        }

        panelCardLayout.revalidate();
        panelCardLayout.repaint();
    }

    public void showTextView() {
        panelCardLayout.add(TEXT_VIEW, textPane);
    }

    public void showTableView() {
        panelCardLayout.add(TABLE_VIEW, scrollPane);
    }
    public void addDividedView(CardLayout cl){
        JSplitPane vistaDividida = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, textPane, scrollPane);
        panelCardLayout.removeAll();
        panelCardLayout.add(DIVIDED_VIEW, vistaDividida);
        System.out.println(panelCardLayout.getComponentCount());

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
