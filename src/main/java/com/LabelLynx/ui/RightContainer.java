package com.LabelLynx.ui;

import com.LabelLynx.ui.rightcontainer.ContainerEditorAnotations;
import com.LabelLynx.ui.rightcontainer.StatusMain;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class RightContainer extends JSplitPane {
    public static ContainerEditorAnotations containerEditorAnotations;
    public static StatusMain statusMain;
    public RightContainer(){
        super(VERTICAL_SPLIT);
        setLeftComponent(containerEditorAnotations = new ContainerEditorAnotations());
        setRightComponent(statusMain = new StatusMain());
        super.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 1));
        setBorder(BorderFactory.createEmptyBorder(3, 5, 3, 3));
        setDividerSize(5);
        setResizeWeight(0.50);
    }
}
