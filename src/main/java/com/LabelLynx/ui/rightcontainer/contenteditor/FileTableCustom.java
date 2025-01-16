package com.LabelLynx.ui.rightcontainer.contenteditor;

import com.LabelLynx.controlers.CsvSeparator;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Setter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
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
    public FileTableCustom(CsvSeparator csvSeparator){
        customTableModel = new CustomTableModel();
        setModel(customTableModel);
        this.csvSeparator = csvSeparator;
        setShowGrid(true);
        setCustomTable();
    }

    public void addColumnNames(Object[] nameColumns){
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.setColumnIdentifiers(nameColumns);
    }

    public String[] getStringColumns(){
        String[] nameColumns = new String[getColumnCount()];

        for(int i = 0; i < getColumnCount(); i++){
            nameColumns[i] = getColumnName(i);
        }

        return nameColumns;
    }

    public void setContent(){
        Object[][] data = getContentMatriz();
        for(Object[] fila : data){
            addRow(fila);
        }
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

    public void setRow(int linea, Object[] datos){
        DefaultTableModel model = (DefaultTableModel) getModel();
        if(!Arrays.equals(datos, getStringColumns())) {
            if (linea-1 >= model.getRowCount()) {
                model.addRow(datos);
            } else {
                // Reemplazamos la fila existente
                if(datos.length == 0){
                    datos = new Object[getColumnCount()];
                    Arrays.fill(datos, "");
                }
                for (int i = 0; i < datos.length; i++) {
                    model.setValueAt(datos[i], linea-1, i);
                }
            }
        }

    }

    public void deleteRow(int line){
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.removeRow(line);
    }

    public void addRow(Object[] fila){
        DefaultTableModel model = (DefaultTableModel) getModel();
        model.addRow(fila);
    }

    private void setCustomTable(){
        JTableHeader header = getTableHeader();
        header.setFont(new Font(getFont().getName(), Font.BOLD, 15));
        defaultTableCellRenderer = new DefaultTableCellRenderer();
        defaultTableCellRenderer.setHorizontalAlignment(JLabel.CENTER);
        setDefaultRenderer(Object.class, defaultTableCellRenderer);
    }

    @Setter
    public static class CustomTableModel extends DefaultTableModel{

        private boolean editable;
        @Override
        public boolean isCellEditable(int rowIndex, int columnIndex){
            return editable;
        }

        public boolean getEditable(){
            return this.editable;
        }
    }
}
