package com.LabelLynx.ui.rightcontainer;

import com.LabelLynx.controlers.CsvSeparator;
import com.LabelLynx.ui.rightcontainer.contenteditor.FileTableCustom;
import com.LabelLynx.ui.rightcontainer.contenteditor.TextEditor;
import lombok.Getter;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.MatteBorder;
import javax.swing.event.CaretEvent;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.*;
import java.awt.*;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.*;
import java.util.List;

public class EventosDocumento extends JPanel
        implements CaretListener, DocumentListener, PropertyChangeListener {

    private static final Logger logger = LogManager.getLogger(EventosDocumento.class);
    public final static float LEFT = 0.0f;
    public final static float CENTER = 0.5f;
    public final static float RIGHT = 1.0f;
    private Color color1 = new Color(7,86,100);
    private final static Border OUTER = new MatteBorder(0, 0, 0, 2,Color.BLACK);
    private final static int HEIGHT = Integer.MAX_VALUE - 1000000;
    private TextEditor editor;
    private FileTableCustom scrollPaneTable;
    @Setter
    private boolean updateFont;
    @Getter
    private int borderGap;
    @Setter
    private Color currentLineForeground;
    private float digitAlignment;
    private int minimumDisplayDigits;
    private int lastDigits;
    private int lastHeight;
    private int lastLine;
    private int previousNumberLines;
    private HashMap<String, FontMetrics> fonts;
    private boolean lock;
    //Los nombres de la ultima fina añadida por columnas ejemplo [5,texto,500]
    private List<String> lastColumnNames;
    //Almacena las lineas que contengan mas columnas que las definidas en la tabla ejemplo tabla con 3 columnas y contenido 4 columnas [5,texto,500,relleno]
    private List<Object[]> linesUpperLength;


    public EventosDocumento(TextEditor editor) {
        this(editor, 3, null);
    }


    public EventosDocumento(TextEditor editor, int minimumDisplayDigits, FileTableCustom scrollPaneTable) {
        this.editor = editor;
        this.scrollPaneTable = scrollPaneTable;
        this.lock = false;
        this.lastColumnNames = new ArrayList<>();
        this.linesUpperLength = new ArrayList<>();
        this.previousNumberLines = 1;

        setFont(editor.getFont());

        setBorderGap(5);
        setCurrentLineForeground(color1);
        setDigitAlignment(RIGHT);
        setMinimumDisplayDigits(minimumDisplayDigits);

        editor.getDocument().addDocumentListener(this);
        editor.addCaretListener(this);
        editor.addPropertyChangeListener("font", this);
    }

    public void setBorderGap(int borderGap) {
        this.borderGap = borderGap;
        Border inner = new EmptyBorder(0, borderGap, 0, borderGap);
        setBorder(new CompoundBorder(OUTER, inner));
        lastDigits = 0;
        setPreferredWidth();
    }

    public Color getCurrentLineForeground() {
        return currentLineForeground == null ? getForeground() : currentLineForeground;
    }

    public void setDigitAlignment(float digitAlignment) {
        this.digitAlignment
                = digitAlignment > 1.0f ? 1.0f : digitAlignment < 0.0f ? -1.0f : digitAlignment;
    }

    public void setMinimumDisplayDigits(int minimumDisplayDigits) {
        this.minimumDisplayDigits = minimumDisplayDigits;
        setPreferredWidth();
    }

    private void setPreferredWidth() {
        Element root = editor.getDocument().getDefaultRootElement();
        int lines = root.getElementCount();
        int digits = Math.max(String.valueOf(lines).length(), minimumDisplayDigits);

        if (lastDigits != digits) {
            lastDigits = digits;
            FontMetrics fontMetrics = getFontMetrics(getFont());
            int width = fontMetrics.charWidth('0') * digits;
            Insets insets = getInsets();
            int preferredWidth = insets.left + insets.right + width;

            Dimension d = getPreferredSize();
            d.setSize(preferredWidth, HEIGHT);
            setPreferredSize(d);
            setSize(d);
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);

        FontMetrics fontMetrics = editor.getFontMetrics(editor.getFont());
        Insets insets = getInsets();
        int availableWidth = getSize().width - insets.left - insets.right;

        Rectangle clip = g.getClipBounds();
        int rowStartOffset = editor.viewToModel2D(new Point(0, clip.y));
        int endOffset = editor.viewToModel2D(new Point(0, clip.y + clip.height));

        while (rowStartOffset <= endOffset) {
            try {
                if (isCurrentLine(rowStartOffset)) {
                    g.setColor(getCurrentLineForeground());
                } else {
                    g.setColor(getForeground());
                }

                String lineNumber = getTextLineNumber(rowStartOffset);
                int stringWidth = fontMetrics.stringWidth(lineNumber);
                int x = getOffsetX(availableWidth, stringWidth) + insets.left;
                int y = getOffsetY(rowStartOffset, fontMetrics);
                g.drawString(lineNumber, x, y);
                rowStartOffset = Utilities.getRowEnd(editor, rowStartOffset) + 1;
            } catch (Exception e) {
                break;
            }
        }
    }

    private boolean isCurrentLine(int rowStartOffset) {
        int caretPosition = editor.getCaretPosition();
        Element root = editor.getDocument().getDefaultRootElement();

        if (root.getElementIndex(rowStartOffset) == root.getElementIndex(caretPosition)) {
            return true;
        } else {
            return false;
        }
    }

    protected String getTextLineNumber(int rowStartOffset) {
        Element root = editor.getDocument().getDefaultRootElement();
        int index = root.getElementIndex(rowStartOffset);

        Element line = root.getElement(index);
        int startOffset = line.getStartOffset();
        int endOffset = line.getEndOffset();
        int lineNumber = index + 1;

        return String.valueOf(lineNumber);
    }


    /*
     *  Determine the X offset to properly align the line number when drawn
     */
    private int getOffsetX(int availableWidth, int stringWidth) {
        return (int) ((availableWidth - stringWidth) * digitAlignment);
    }

    /*
     *  Determine the Y offset for the current row
     */
    private int getOffsetY(int rowStartOffset, FontMetrics fontMetrics)
            throws BadLocationException {
        //  Get the bounding rectangle of the row

        Rectangle r = editor.modelToView(rowStartOffset);
        int lineHeight = fontMetrics.getHeight();
        int y = r.y + r.height;
        int descent = 0;

        //  The text needs to be positioned above the bottom of the bounding
        //  rectangle based on the descent of the font(s) contained on the row.
        if (r.height == lineHeight) // default font is being used
        {
            descent = fontMetrics.getDescent();
        } else // We need to check all the attributes for font changes
        {
            if (fonts == null) {
                fonts = new HashMap<String, FontMetrics>();
            }

            Element root = editor.getDocument().getDefaultRootElement();
            int index = root.getElementIndex(rowStartOffset);
            Element line = root.getElement(index);

            for (int i = 0; i < line.getElementCount(); i++) {
                Element child = line.getElement(i);
                AttributeSet as = child.getAttributes();
                String fontFamily = (String) as.getAttribute(StyleConstants.FontFamily);
                Integer fontSize = (Integer) as.getAttribute(StyleConstants.FontSize);
                String key = fontFamily + fontSize;

                FontMetrics fm = fonts.get(key);

                if (fm == null) {
                    Font font = new Font(fontFamily, Font.PLAIN, fontSize);
                    fm = editor.getFontMetrics(font);
                    fonts.put(key, fm);
                }

                descent = Math.max(descent, fm.getDescent());
            }
        }

        return y - descent;
    }

    //
    //  Implement CaretListener interface
    //
    @Override
    public void caretUpdate(CaretEvent e) {
        //  Get the line the caret is positioned on

        int caretPosition = editor.getCaretPosition();
        Element root = editor.getDocument().getDefaultRootElement();
        int currentLine = root.getElementIndex(caretPosition);

        //  Need to repaint so the correct line number can be highlighted
        if (lastLine != currentLine) {
            repaint();
            lastLine = currentLine;
        }
    }

    //
    //  Implement DocumentListener interface
    //
    @Override
    public void changedUpdate(DocumentEvent e) {
        addNumberLine();
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        addNumberLine();
        updateTableContentWhenInsert(e);
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        addNumberLine();
        updateTableContentWhenDelete(e);
    }

    private void updateTableContentWhenDelete(DocumentEvent e) {
        //TODO REVISAR EL BUG AL BORRAR
        if(!lock) {
            lock = true;
            Thread h1 = new Thread(() -> {
                Element root = editor.getDocument().getDefaultRootElement();
                int lines = root.getElementCount();
                int rows = scrollPaneTable.getRowCount(); //Son filas sin contar los nombres de las columnas
                int header = scrollPaneTable.getTableHeader().getColumnModel().getColumnCount();
                //System.out.println("Numero de lineas: " + lines);
                //System.out.println("Numero de lineas anterior: " + previousNumberLines);

                if(lines < previousNumberLines){
                    System.out.println("Linea a borrar: " + lastLine);
                    System.out.println("Linea a borrar: " + scrollPaneTable.getRowCount());
                    scrollPaneTable.deleteRow(lastLine);
                }
                previousNumberLines = lines;
                lock = false;
                updateTableContentWhenInsert(e);
                lock = true;
            });
            h1.start();
        }
    }

    private void updateTableContentWhenInsert(DocumentEvent e) {
        if(!lock) {
            lock = true;
            Thread h1 = new Thread(() -> {
                CsvSeparator csvSeparator = editor.getCsvSeparator();
                List<String> listaNombresColumnas = null;

                try {
                    listaNombresColumnas = csvSeparator.analiseLine(getLine(e.getDocument(), lastLine));
                    fillWithWhiteSpace(scrollPaneTable.getColumnCount()-listaNombresColumnas.size(), listaNombresColumnas);
                } catch (BadLocationException ex) {
                    logger.error("Error al recuperar el texto del editor");
                }

                if (lastLine == 0) {
                    if(listaNombresColumnas.size() < scrollPaneTable.getColumnCount()) {
                        listaNombresColumnas.addAll(getListWithNEmptyStrings(scrollPaneTable.getColumnCount()- listaNombresColumnas.size()));
                    }

                    scrollPaneTable.addColumnNames(listaNombresColumnas.toArray());
                    lastColumnNames = listaNombresColumnas;
                } else {
                    try {
                        if((null != listaNombresColumnas && !lastColumnNames.equals(listaNombresColumnas) || listaNombresColumnas.isEmpty())){
                            scrollPaneTable.setRow(lastLine, listaNombresColumnas.toArray());
                            lastColumnNames = listaNombresColumnas;
                        }


                    }catch (Exception ex){
                        //Guardar fila para añadirla cuando haya suficientes columnas o por lo menos una mas
                        linesUpperLength.add(listaNombresColumnas.toArray());
                        logger.error("No se pudo insertar la linea -> {}", ex.toString());
                    }
                }
                lock = false;
            });
            h1.start();
        }
    }

    private void fillWithWhiteSpace(int numberSpace, List<String> listaNombresColumnas){
        for(int i = 0; i < numberSpace; i++){
            listaNombresColumnas.add("");
        }
    }
    public ArrayList<String> getListWithNEmptyStrings(int numColumns){
        ArrayList<String> emptyArray = new ArrayList<>();
        for(int i = 0; i < numColumns; i++){
            emptyArray.add("");
        }
        return emptyArray;
    }
    public String getLine(Document doc, int lineIndex) throws BadLocationException {
        int inicio = getLineLengthInit(doc, lineIndex);
        int finalPos = getLineLengthEnd(doc, lineIndex);
        return doc.getText(inicio, finalPos-inicio).trim();
    }

    public int getLineLengthInit(Document doc, int lineIndex) {
        Element rootElement = doc.getDefaultRootElement();

        // Verificar que el índice de línea esté dentro del rango
        if (lineIndex < 0 || lineIndex >= rootElement.getElementCount()) {
            logger.error("Índice de línea fuera de rango");
        }

        Element lineElement = rootElement.getElement(lineIndex);

        // Calcular la longitud de la línea
        return lineElement.getStartOffset(); // restar 1 para excluir el carácter de salto de línea
    }

    public int getLineLengthEnd(Document doc, int lineIndex) {
        Element rootElement = doc.getDefaultRootElement();

        // Verificar que el índice de línea esté dentro del rango
        if (lineIndex < 0 || lineIndex >= rootElement.getElementCount()) {
            throw new IllegalArgumentException("Índice de línea fuera de rango");
        }

        Element lineElement = rootElement.getElement(lineIndex);

        // Calcular la longitud de la línea
        return lineElement.getEndOffset(); // restar 1 para excluir el carácter de salto de línea
    }

    /*
     *  A document change may affect the number of displayed lines of text.
     *  Therefore the lines numbers will also change.
     */
    private void addNumberLine() {
        //  View of the component has not been updated at the time
        //  the DocumentEvent is fired

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    int endPos = editor.getDocument().getLength();
                    Rectangle rect = editor.modelToView(endPos);

                    if (rect != null && rect.y != lastHeight) {
                        setPreferredWidth();
                        repaint();
                        lastHeight = rect.y;
                    }
                } catch (BadLocationException ex) {
                    /* nothing to do */ }
            }
        });
    }

    @Override
    public void propertyChange(PropertyChangeEvent evt) {

    }
}
