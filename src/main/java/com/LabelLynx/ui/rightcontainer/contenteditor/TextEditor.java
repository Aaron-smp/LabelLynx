package com.LabelLynx.ui.rightcontainer.contenteditor;

import com.LabelLynx.controlers.CsvSeparator;
import com.LabelLynx.utils.CustomFonts;
import lombok.Getter;
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

    @Getter
    private CsvSeparator csvSeparator;
    public TextEditor(CsvSeparator csvSeparator) {
        setMinimumSize(new Dimension(300, 250));
        this.csvSeparator = csvSeparator;
        String[] textFile = csvSeparator.getFichero();
        try{
            addHeaders(textFile);
        }catch (BadLocationException e){
            logger.error("Error al meter el contenido {}", e.toString());
        }
    }

    /*
    *
    *   Función que se usa una vez al abrir un archivo para escribir los indices con diferente fuente.
    *
     */
    public void addHeaders(String[] textFile) throws BadLocationException {
        Thread h1 = new Thread(() -> {
            try {
                Thread.sleep(50);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            attributeSet = new SimpleAttributeSet();
            StyleConstants.setFontFamily(attributeSet, CustomFonts.getTextFont().getFamily());
            StyleConstants.setForeground(attributeSet, CustomFonts.getColorHeaders());
            StyleConstants.setFontSize(attributeSet, CustomFonts.getSizeHeaders());
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

    /*
     *
     *   Función que se usa una vez al abrir un archivo para escribir el contenido con diferente fuente.
     *
     */
    public void addContent(String[] textFile) throws BadLocationException {
        Thread h1 = new Thread(() -> {
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            attributeSet = new SimpleAttributeSet();
            StyleConstants.setFontFamily(attributeSet, CustomFonts.getTextFont().getFamily());
            StyleConstants.setForeground(attributeSet, CustomFonts.getColorContent());
            StyleConstants.setFontSize(attributeSet, CustomFonts.getSizeContent());


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

            underlineWrongText(fallos, textFile[1]);

        });
        h1.start();
    }

    /*
    *
    * Función que se ejecuta una vez al leer el texto de un archivo abierto
    *
    */
    private void underlineWrongText(HashMap<Integer, String> fallos, String fallo){
        for (Map.Entry<Integer, String> entry : fallos.entrySet()) {
            int inicio = getLineLengthInit(entry.getKey());
            int fin = getLineLengthEnd(entry.getKey());
            String lineErrorText = fallo.substring(inicio, fin);
            logger.info("Inicio de linea " + entry.getKey() + ": " + inicio);
            logger.info("Tamaño encabezado: " + csvSeparator.getSizeStringEncabezado());
            logger.info("Longitud linea de error: " + lineErrorText.length());
            logger.info("Error: " + entry.getValue());
            addErrorUnderLine(getLineLengthInit(entry.getKey()), lineErrorText.length());
            logger.info("Linea errónea subrayada: {}", lineErrorText);
        }
    }

    /*
    *
    * Función para subrayar por posicion en el
    * texto solo se ejecuta en la lectura al abrir el fichero
    *
    */
    public void addErrorUnderLine(int inicio, int longitud){
        Highlighter highlighter = getHighlighter();
        Highlighter.HighlightPainter painter = new WavyUnderlineHighlightPainter(Color.RED);

        try {
            highlighter.addHighlight(inicio, inicio + longitud, painter);
        } catch (BadLocationException e) {
            logger.error("Error al subrayar el error en el carácter: {}, {}", inicio, inicio+longitud);
        }
    }


    /*
    *
    *   Clase utilizada para crear la raya que subraya el texto
    *
    */
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
                int waveHeight = 4;

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

    public int getLineLengthInit(int lineIndex) {
        Document doc = getDocument();
        Element rootElement = doc.getDefaultRootElement();

        // Verificar que el índice de línea esté dentro del rango
        if (lineIndex < 0 || lineIndex >= rootElement.getElementCount()) {
            throw new IllegalArgumentException("Índice de línea fuera de rango");
        }

        Element lineElement = rootElement.getElement(lineIndex);

        // Calcular la longitud de la línea
        return lineElement.getStartOffset(); // restar 1 para excluir el carácter de salto de línea
    }

    public int getLineLengthEnd(int lineIndex) {
        Document doc = getDocument();
        Element rootElement = doc.getDefaultRootElement();

        // Verificar que el índice de línea esté dentro del rango
        if (lineIndex < 0 || lineIndex >= rootElement.getElementCount()) {
            throw new IllegalArgumentException("Índice de línea fuera de rango");
        }

        Element lineElement = rootElement.getElement(lineIndex);

        // Calcular la longitud de la línea
        return lineElement.getEndOffset(); // restar 1 para excluir el carácter de salto de línea
    }

    public String getLine(int lineIndex){
        return getText().substring(getLineLengthInit(lineIndex), getLineLengthEnd(lineIndex));
    }
}
