package com.LabelLynx.ui.dialog;

import com.LabelLynx.ui.rightcontainer.Editor;
import com.LabelLynx.utils.CustomFonts;

import javax.swing.*;
import java.awt.*;

import static com.LabelLynx.LabelLynxApplication.ventana;
import static com.LabelLynx.ui.rightcontainer.ContainerEditorAnotations.tabsEditor;
import static com.LabelLynx.utils.CustomFonts.getFontFromResources;
import static java.awt.GridBagConstraints.HORIZONTAL;
import static java.awt.GridBagConstraints.NONE;

public class DialogCustomEditor extends JDialog {
    public DialogCustomEditor(JFrame frame){
        super(frame, "Modificar texto editor", true);
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
        JSpinner fontSize = getjSpinner(colorFont, frame.getFont().getSize2D());
        contentPane.add(fontSize, gbc);

        gbc.gridx = 0;
        gbc.gridy = 3;
        gbc.gridwidth = 3;
        gbc.fill = NONE;
        JButton colorButton = new JButton("Color del fondo");
        JColorChooser colorChooser = new JColorChooser();
        colorButton.addActionListener((e) -> {
            int resultado = JOptionPane.showConfirmDialog(null, colorChooser, "Selecciona un color", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            System.out.println(resultado);
            if(resultado == 0){
                colorFont.setBackground(colorChooser.getColor());
                colorRatio.setText("Contraste: " + CustomFonts.getContractRatio(colorFont.getBackground(), colorFont.getForeground()));
            }
        });
        contentPane.add(colorButton, gbc);

        gbc.gridx = 0;
        gbc.gridy = 4;
        gbc.gridwidth = 3;
        JButton colorText = getjButton(colorChooser, colorFont, colorRatio);
        contentPane.add(colorText, gbc);

        gbc.gridx = 0;
        gbc.gridy = 5;
        gbc.gridwidth = 3;
        JButton acceptButton = new JButton("Aceptar");

        acceptButton.addActionListener((e) -> {
            setEditorsFont(colorFont.getFont());
            CustomFonts.actualFont = colorFont.getFont();
            System.out.println(CustomFonts.actualFont);
        });

        contentPane.add(acceptButton, gbc);

        setContentPane(contentPane);
    }

    private void setEditorsFont(Font fuente){
        for (Editor editor : tabsEditor.getEditors()){
            CustomFonts.setFontRecursively(editor.getScrollPaneTable(), fuente);
            CustomFonts.setFontRecursively(editor.getScrollPaneText(), fuente);
        }
    }

    private static JButton getjButton(JColorChooser colorChooser, JLabel colorFont, JLabel colorRatio) {
        JButton colorText = new JButton("Color del texto");

        colorText.addActionListener((e) -> {
            int resultado = JOptionPane.showConfirmDialog(null, colorChooser, "Selecciona un color", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
            if(resultado == 0){
                colorFont.setForeground(colorChooser.getColor());
                colorRatio.setText("Contraste: " + CustomFonts.getContractRatio(colorFont.getBackground(), colorFont.getForeground()));
            }
        });
        return colorText;
    }

    private static JSpinner getjSpinner(JLabel colorFont, float value) {
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
