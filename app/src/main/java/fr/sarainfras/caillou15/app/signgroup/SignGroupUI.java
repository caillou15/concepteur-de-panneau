package fr.sarainfras.caillou15.app.signgroup;

import fr.sarainfras.caillou15.app.AppWindow;
import fr.sarainfras.caillou15.app.Constants;
import fr.sarainfras.caillou15.app.EditorUI;
import fr.sarainfras.caillou15.app.events.group.SignGroupChangeInitiater;
import fr.sarainfras.caillou15.app.events.group.SignGroupChangeListener;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.*;

public class SignGroupUI extends EditorUI implements SignGroupChangeListener {
    public DirectionalSignGroup directionalSignGroup;
    public SignGroupPropertyPane signGroupPropertyPane;
    public DirectionalSignGroupRenderer renderer;
    AppWindow main_frame;

    private SignGroupChangeInitiater signGroupChangeInitiater;

    public SignGroupUI(AppWindow frame) {
        this.directionalSignGroup = new DirectionalSignGroup();
        this.main_frame = frame;
        this.signGroupChangeInitiater = new SignGroupChangeInitiater();
        init();
        this.signGroupPropertyPane =
                new SignGroupPropertyPane(frame, this, directionalSignGroup, signGroupChangeInitiater);

        Container left_container = (Container) getSplitPane().getLeftComponent();
        left_container.add(this.signGroupPropertyPane);

        getSplitPane().setDividerLocation(getSplitPane().getMinimumDividerLocation());
    }

    public void init() {
        initGUI();
    }

    public void initGUI() {
        setBorderLayout(new BorderLayout());
        this.setLayout(getBorderLayout());

        // propriétés à gauche

        JPanel left_panel = new JPanel();
        BoxLayout boxLayout = new BoxLayout(left_panel, BoxLayout.Y_AXIS);
        left_panel.setLayout(boxLayout);

        // rendu à droite

        JPanel right_panel = new JPanel();
        renderer = new DirectionalSignGroupRenderer(main_frame, this);
        right_panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        right_panel.add(renderer, constraints);
        right_panel.setBackground(Constants.BACKGROUND_GREEN);

        setSplitPane(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left_panel, right_panel));
        getSplitPane().setDividerLocation(getSplitPane().getMinimumDividerLocation());

        this.add(getSplitPane());
    }

    @Override
    public Document getSvgDocument() {
        return renderer.getSvg_document();
    }

    @Override
    public void signGroupChangeSet(DirectionalSignGroup new_sign) {}

    @Override
    public void signGroupUpdate() {
        signGroupChangeInitiater.signGroupChangeSet(directionalSignGroup);
    }

    public SignGroupChangeInitiater getSignGroupChangeInitiater() {
        return signGroupChangeInitiater;
    }
}
