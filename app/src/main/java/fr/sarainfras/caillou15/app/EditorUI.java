package fr.sarainfras.caillou15.app;

import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.*;

public abstract class EditorUI extends JPanel {
    private JSplitPane splitPane;
    private BorderLayout borderLayout;

    public abstract Document getSvgDocument();

    public JSplitPane getSplitPane() {
        return splitPane;
    }

    public void setSplitPane(JSplitPane splitPane) {
        this.splitPane = splitPane;
    }

    public BorderLayout getBorderLayout() {
        return borderLayout;
    }

    public void setBorderLayout(BorderLayout borderLayout) {
        this.borderLayout = borderLayout;
    }
}
