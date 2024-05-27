package com.LabelLynx.ui.rightcontainer.contenteditor;

import com.LabelLynx.controlers.CsvSeparator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.text.BadLocationException;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@EqualsAndHashCode(callSuper = true)
@Data
public class FileTableCustom extends JTable {
    private static final Logger logger = LogManager.getLogger(FileTableCustom.class);
    private DefaultTableCellRenderer defaultTableCellRenderer;
    private CsvSeparator csvSeparator;

    private CustomTableModel customTableModel;
    public FileTableCustom(File file){
        customTableModel = new CustomTableModel();
        setModel(customTableModel);
        CustomTableModel model = (CustomTableModel) getModel();
        if(file != null) {
            try {
                csvSeparator = new CsvSeparator(",");
                csvSeparator.analiseLines();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            Object[][] data = getContentMatriz();

            String[] columnNames = csvSeparator.getEncabezados().toArray(new String[0]);

        }
        setShowGrid(true);
        setCustomTable();
    }

    private Object[][] getContentMatriz() {
        Object[][] datos = new Object[csvSeparator.getContenido().size()][csvSeparator.getEncabezados().size()];
        for(int i = 0; i < csvSeparator.getContenido().size(); i++){
            for(int e = 0; e < csvSeparator.getEncabezados().size(); e++){
                if(null != csvSeparator.getContenido().get(i).get(e)){
                    datos[i][e] = csvSeparator.getContenido().get(i).get(e);
                }
            }
        }

        return datos;
    }

    public void setColumnNames(Object[] nameColumns){
        DefaultTableModel model = (DefaultTableModel) getModel();
        for(Object name : nameColumns){
            model.addColumn(name);
        }
    }

    public void addRow(Object[] fila){
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(fila);
    }

    private void setCustomTable(){
        JTableHeader header = getTableHeader();
        header.setFont(new Font(getFont().getName(), Font.BOLD, 15));
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        setDefaultRenderer(Object.class, centerRenderer);
    }

    public static class CustomTableModel extends DefaultTableModel{

        private boolean editable;
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex){
            return editable;
        }

        public void setEditable(boolean editable) {
            this.editable = editable;
        }
        public boolean getEditable(){
            return this.editable;
        }
    }
}
