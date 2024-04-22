package com.LabelLynx.ui.rightcontainer;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
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
    private JPanel panelCompuesto;
    public Editor() {
        this.identifier = idInit(null);
        textPane = new JTextPane();
        logger.info("Nueva pestaña abierta.");
        textPane.setText("Escribe Aquí");
    }

    public Editor(String texto, String path) {
        this.identifier = idInit(path);
        textPane = new JTextPane();
        logger.info("Nueva pestaña abierta.");
        textPane.setText(texto);
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
