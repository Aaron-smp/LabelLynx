package com.LabelLynx.ui.rightcontainer;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.swing.*;

@EqualsAndHashCode(callSuper = true)
@Data
public class ContainerEditorAnotations extends JSplitPane {
    public static TabsEditor tabsEditor;
    public static Anotations anotations;
    public ContainerEditorAnotations(){
        super(HORIZONTAL_SPLIT);
        setLeftComponent(tabsEditor = new TabsEditor());
        setRightComponent(anotations = new Anotations());

        setDividerSize(5);
        setResizeWeight(0.95);
    }
}
