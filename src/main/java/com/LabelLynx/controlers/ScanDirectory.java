package com.LabelLynx.controlers;

import com.LabelLynx.utils.CustomFonts;

import javax.swing.*;
import java.io.File;

public class ScanDirectory {
    public static File scanDirectory(){
        JFileChooser fileChooser = new JFileChooser();
        CustomFonts.setFontRecursively(fileChooser, CustomFonts.getFontFromResources("JetBrainsMono-Regular.ttf", 12, 0));
        fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int result = fileChooser.showOpenDialog(null);
        return result == JFileChooser.APPROVE_OPTION && fileChooser.getSelectedFile().isDirectory() ? fileChooser.getSelectedFile() : null;
    }
}
