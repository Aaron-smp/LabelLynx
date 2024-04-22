package com.LabelLynx.ui.rightcontainer;

import javax.swing.*;
import java.awt.*;

public class StatusMain extends JTabbedPane{
    public StatusMain(){
        super.add("Status", new JTable());
        super.add("Problems", new JTable());
        super.add("Logs", new JTable());
        setPreferredSize(new Dimension(400, 300));
        setBorder(BorderFactory.createEtchedBorder());
    }
}
