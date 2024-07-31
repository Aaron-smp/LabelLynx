package com.LabelLynx.ui.dialog;

import com.LabelLynx.ui.rightcontainer.Editor;
import com.LabelLynx.ui.rightcontainer.contenteditor.FileTableCustom;
import com.LabelLynx.ui.rightcontainer.contenteditor.TextEditor;
import com.LabelLynx.utils.CustomFonts;

import javax.swing.*;
import java.awt.*;

import static com.LabelLynx.ui.rightcontainer.ContainerEditorAnotations.tabsEditor;
import static com.LabelLynx.utils.CustomFonts.getFontFromResources;
import static java.awt.GridBagConstraints.NONE;

public class DialogCustomEditor extends JDialog {
    private Color fondo;
    private static Color texto;
    public DialogCustomEditor(JFrame frame){
        super(frame, "Modificar texto editor", true);
        fondo = null;
        texto = null;
        setResizable(true);
        setMinimumSize(new Dimension(500, 400));
        inicializeUI(frame);
        setSize(500, 400);
        setLocationRelativeTo(frame);
        CustomFonts.setFontRecursively(this, getFontFromResources("JetBrainsMono-Regular.ttf", 14, 0));
        setVisible(true);
    }

    public void inicializeUI(JFrame frame){
        GridBagLayout gridBagLayout = new GridBagLayout();
        JPanel contentPane = new JPanel(gridBagLayout);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JLabel fontLabel = new JLabel("Fuentes:");
        contentPane.add(fontLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel colorFont = new JLabel("ABCDEFGHI01234");
        colorFont.setHorizontalAlignment(SwingConstants.LEFT);
        colorFont.setOpaque(true);
        contentPane.add(colorFont, gbc);

        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        JLabel colorRatio = new JLabel("Contraste: " + CustomFonts.getContractRatio(colorFont.getBackground(), colorFont.getForeground()));
        colorRatio.setHorizontalAlignment(SwingConstants.RIGHT);
        contentPane.add(colorRatio, gbc);

        gbc.gridx = 1;
        gbc.gridy = 1;
        gbc.gridwidth = 1;
        JComboBox<String> fuentes = new JComboBox<>();
        DefaultComboBoxModel<String> modelFonts = new DefaultComboBoxModel<>();
        modelFonts.addAll(CustomFonts.listCustomFonts());
        modelFonts.setSelectedItem(CustomFonts.actualFont.getName());
        fuentes.setModel(modelFonts);
        fuentes.addActionListener((e) -> {
            Font currentFont = colorFont.getFont();
            Font newFont = getFontFromResources(String.valueOf(fuentes.getSelectedItem()), currentFont.getSize(), currentFont.getStyle());
            colorFont.setFont(newFont);
            colorRatio.setText("Contraste: " + CustomFonts.getContractRatio(colorFont.getBackground(), colorFont.getForeground()));
        });
        contentPane.add(fuentes, gbc);

        gbc.gridx = 0;
        gbc.gridy = 2;
        gbc.gridwidth = 1;
        JLabel sizeLabel = new JLabel("Tamaño:");
        contentPane.add(sizeLabel, gbc);

        gbc.gridx = 1;
        gbc.gridy = 2;
        gbc.gridwidth = 2;
        gbc.fill = GridBagConstraints.BOTH;
        JSpinner fontSize = getSpinnerSizeFont(colorFont, frame.getFont().getSize2D());
        contentPane.add(fontSize, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = NONE;
        JButton colorButton = new JButton("Color del fondo");
        JColorChooser colorChooser = new JColorChooser();
        colorButton.addActionListener((e) -> {
            int resultado = JOptionPane.showConfirmDialog(null, colorChooser, "Selecciona un color", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(resultado == 0){
                colorFont.setBackground(colorChooser.getColor());
                fondo = colorChooser.getColor();
                colorRatio.setText("Contraste: " + CustomFonts.getContractRatio(colorFont.getBackground(), colorFont.getForeground()));
            }
        });
        contentPane.add(colorButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        JButton colorText = getColorButton(colorChooser, colorFont, colorRatio);
        contentPane.add(colorText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        JButton acceptButton = new JButton("Aceptar");

        acceptButton.addActionListener((e) -> {
            setEditorsFont(colorFont.getFont(), fondo, texto);
            CustomFonts.actualFont = colorFont.getFont();
        });

        contentPane.add(acceptButton, gbc);

        setContentPane(contentPane);
    }

    private void setEditorsFont(Font fuente, Color fondo, Color texto){
        System.out.println("Numero editores: " + tabsEditor.getEditors());
        for (Editor editor : tabsEditor.getEditors()){
            JTextPane textPaneEditor = (JTextPane) editor.getScrollPaneText().getViewport().getView();
            JTable tableEditor = (JTable) editor.getScrollPaneTable().getViewport().getView();

            if(textPaneEditor instanceof TextEditor){
                textPaneEditor.setBackground(fondo);
                textPaneEditor.setForeground(texto);
                textPaneEditor.setFont(fuente);
            }

            if(tableEditor instanceof FileTableCustom){
                ((FileTableCustom) tableEditor).getDefaultTableCellRenderer().setBackground(fondo);
                ((FileTableCustom) tableEditor).getDefaultTableCellRenderer().setForeground(texto);
                tableEditor.getTableHeader().setBackground(fondo);
                tableEditor.getTableHeader().setForeground(texto);
                tableEditor.setFont(fuente);
                tableEditor.repaint();
            }
        }
    }

    private static JButton getColorButton(JColorChooser colorChooser, JLabel colorFont, JLabel colorRatio) {
        JButton colorText = new JButton("Color del texto");

        colorText.addActionListener((e) -> {
            int resultado = JOptionPane.showConfirmDialog(null, colorChooser, "Selecciona un color", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(resultado == 0){
                colorFont.setForeground(colorChooser.getColor());
                texto = colorChooser.getColor();
                colorRatio.setText("Contraste: " + CustomFonts.getContractRatio(colorFont.getBackground(), colorFont.getForeground()));
            }
        });
        return colorText;
    }

    private static JSpinner getSpinnerSizeFont(JLabel colorFont, float value) {
        JSpinner fontSize = new JSpinner();
        SpinnerModel model = new SpinnerNumberModel(value, 1.0, 40.0, 1.0);
        fontSize.setModel(model);
        fontSize.addChangeListener((e) -> {
            Font currentFont = colorFont.getFont(); // Obtener la fuente actual
            float newSize = Float.parseFloat(fontSize.getValue().toString()); // Obtener el nuevo tamaño de fuente del spinner
            Font newFont = currentFont.deriveFont(newSize); // Crear una nueva fuente con el nuevo tamaño
            colorFont.setFont(newFont);
        });
        return fontSize;
    }
}
