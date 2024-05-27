package com.LabelLynx.ui.rightcontainer.contenteditor;

import com.LabelLynx.controlers.CsvSeparator;
import com.LabelLynx.utils.CustomFonts;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class TextEditor extends JTextPane {
    private static final Logger logger = LogManager.getLogger(TextEditor.class);
    private SimpleAttributeSet attributeSet;
    private HashMap<String, Object> configurationText;
    private CsvSeparator csvSeparator;
    public TextEditor(File file) {
        setMinimumSize(new Dimension(300, 250));
        configurationText();
        try {
            if(file != null){
                csvSeparator = new CsvSeparator(",");
                String[] textFile = csvSeparator.getFileSeparate(file);
                csvSeparator.analiseLines();
                try{
                    addHeaders(textFile);
                }catch (BadLocationException e){
                    logger.error("Error al meter el contenido {}", e.toString());
                }
            };
        } catch (IOException e) {
            logger.error("No ha sido posible analizar el texto");
        }
    }

    public void addHeaders(String[] textFile) throws BadLocationException {
        Thread h1 = new Thread(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            attributeSet = new SimpleAttributeSet();
            StyleConstants.setForeground(attributeSet, (Color) configurationText.get("Color.headers"));
            StyleConstants.setFontSize(attributeSet, (Integer) configurationText.get("Font.size.headers"));
            Document document = getStyledDocument();
            try {
                document.insertString(0, textFile[0], attributeSet);
            } catch (BadLocationException e) {
                StyleConstants.setItalic(attributeSet, true);
                try {
                    document.insertString(0, "<Error al insertar los headers>", attributeSet);
                } catch (BadLocationException ex) {
                    throw new RuntimeException(ex);
                }
                logger.error("Fallo en al insertar los headers");
            }

            try {
                addContent(textFile);
            } catch (BadLocationException e) {
                throw new RuntimeException(e);
            }
        });

        h1.start();
    }

    public void addContent(String[] textFile) throws BadLocationException {
        Thread h1 = new Thread(() -> {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            attributeSet = new SimpleAttributeSet();
            StyleConstants.setForeground(attributeSet, (Color) configurationText.get("Color.content"));
            StyleConstants.setFontSize(attributeSet, (Integer) configurationText.get("Font.size.content"));


            StyledDocument styledDocument = getStyledDocument();
            try{
                styledDocument.insertString(styledDocument.getLength(), textFile[1], attributeSet);
            }catch (BadLocationException e){
                try {
                    StyleConstants.setItalic(attributeSet, true);
                    styledDocument.insertString(0,"<Error al insertar los headers>", attributeSet);
                } catch (BadLocationException ex) {
                    logger.error("Error en el texto: {}", ex.toString());
                }
                logger.error("Fallo al insertar el contenido");
            }

            HashMap<Integer, String> fallos = csvSeparator.getWrongLines();

            for (Map.Entry<Integer, String> entry : fallos.entrySet()) {
                int inicio = csvSeparator.getLineInit(entry.getKey());
                int fin = csvSeparator.getLineFinish(entry.getKey());
                String lineErrorText = textFile[1].substring(inicio, fin);
                StyleConstants.setUnderline(attributeSet, true);
                logger.info("Inicio de linea " + entry.getKey() + ": " + csvSeparator.getLineInit(entry.getKey()));
                logger.info("Tamaño encabezado: " + csvSeparator.getSizeStringEncabezado());
                logger.info("Longitud linea de error: " + lineErrorText.length());
                logger.info("Error: " + entry.getValue());
                addErrorUnderLine(csvSeparator.getLineInit(entry.getKey()), lineErrorText.length());
                logger.info("Linea erronea subrayada: {}", lineErrorText);
            }
        });
        h1.start();
    }

    private void configurationText(){
        configurationText = new HashMap<>();
        configurationText.put("Font", CustomFonts.actualFont);
        configurationText.put("Color.headers", new Color(0, 1, 47));
        configurationText.put("Color.content", new Color(82, 136, 66));
        configurationText.put("Color.separator", new Color(249, 115, 0));
        configurationText.put("Font.size.headers", 17);
        configurationText.put("Font.size.content", 17);
    }

    public void addErrorUnderLine(int inicio, int longitud){
        Highlighter highlighter = getHighlighter();
        Highlighter.HighlightPainter painter = new WavyUnderlineHighlightPainter(Color.RED);

        try {
            highlighter.addHighlight(inicio, inicio + longitud, painter);
        } catch (BadLocationException e) {
            logger.error("Error al subrayar el error en el carácter: {}, {}", inicio, inicio+longitud);
        }
    }

    static class WavyUnderlineHighlightPainter extends LayeredHighlighter.LayerPainter {
        private final Color color;

        public WavyUnderlineHighlightPainter(Color color) {
            this.color = color;
        }

        @Override
        public void paint(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c) {
            // No implementado, solo se usa paintLayer
        }

        @Override
        public Shape paintLayer(Graphics g, int offs0, int offs1, Shape bounds, JTextComponent c, View view) {
            g.setColor(color);

            try {
                Shape shape = view.modelToView(offs0, Position.Bias.Forward, offs1, Position.Bias.Backward, bounds);
                Rectangle rect = (shape instanceof Rectangle) ? (Rectangle) shape : shape.getBounds();

                int x = rect.x;
                int y = rect.y + rect.height - 2;
                int waveHeight = 2;

                for (int i = x; i < x + rect.width; i += waveHeight * 2) {
                    g.drawLine(i, y, i + waveHeight, y + waveHeight);
                    g.drawLine(i + waveHeight, y + waveHeight, i + waveHeight * 2, y);
                }

                return rect;
            } catch (BadLocationException e) {
                logger.error("Fallo al renderizar la línea de error");
            }
            return null;
        }
    }
}
