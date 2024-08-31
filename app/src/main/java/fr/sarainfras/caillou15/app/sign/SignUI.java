package fr.sarainfras.caillou15.app.sign;

import fr.sarainfras.caillou15.app.AppWindow;
import fr.sarainfras.caillou15.app.Constants;
import fr.sarainfras.caillou15.app.EditorUI;
import fr.sarainfras.caillou15.app.render.DirectionalSignRenderer;
import fr.sarainfras.caillou15.app.render.Renderer;
import org.w3c.dom.Document;

import javax.swing.*;
import java.awt.*;

public class SignUI extends EditorUI {
    AppWindow main_frame;
    Renderer renderer;
    public DirectionalSignRenderer directionalSignRenderer;
    Document svg_document;
    DirectionalSign sign = null;

    /**
     * Constructeur à utiliser dans le cas de l'édition d'un unique panneau.
     * Si il s'agit d'éditer un ensemble de panneaux, il utiliser l'interface {@link fr.sarainfras.caillou15.app.signgroup.SignGroupUI} pour le groupe,
     * et l'autre constructeur de {@link fr.sarainfras.caillou15.app.sign.SignUI} pour l'éditeur de panneau simple.
     *
     * @param main_frame fenêtre dans laquelle est affiché l'interface {@code SignUI}
     */
    public SignUI(AppWindow main_frame) {
        this.main_frame = main_frame;
        renderer = new Renderer(main_frame);
        directionalSignRenderer = new DirectionalSignRenderer(main_frame);
        initGUI();
    }

    private SignUI(AppWindow main_frame, DirectionalSign dir_sign) {
        this.main_frame = main_frame;
        this.sign = dir_sign;
    }

    /**
     * Constructeur à utiliser dans le cas de l'édition d'un panneau dans un groupe.
     * Si il s'agit d'éditer un ensemble de panneaux, il utiliser l'interface {@link fr.sarainfras.caillou15.app.signgroup.SignGroupUI} pour le groupe,
     * et l'autre constructeur de {@link fr.sarainfras.caillou15.app.sign.SignUI} pour l'éditeur de panneau simple.
     *
     * @param main_frame fenêtre dans laquelle afficher l'interface {@code SignUI}
     * @param dir_sign panneau à éditer
     * @param mainUI interface principale (ou "parente") dans laquelle est édité le groupe de panneaux
     */
    public SignUI(AppWindow main_frame, DirectionalSign dir_sign, boolean mainUI) {
        this(main_frame, dir_sign);
        if (mainUI) {
            renderer = new Renderer(main_frame);
            directionalSignRenderer = new DirectionalSignRenderer(main_frame);
        } else {
            renderer = new Renderer(dir_sign, main_frame, this);
            directionalSignRenderer = new DirectionalSignRenderer(dir_sign, main_frame, this);
        }
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

        right_panel.setBackground(Constants.BACKGROUND_GREEN);
        right_panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        right_panel.add(renderer, constraints);

        setSplitPane(new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, left_panel, right_panel));

        if (sign != null) left_panel.add(new SignPropertyPane(main_frame, sign, this));
        else left_panel.add(new SignPropertyPane(main_frame, this));
        //left_panel.add(new Box.Filler(new Dimension(0, 0), new Dimension(0, 2000), new Dimension(0, 2000)));

        getSplitPane().setDividerLocation(getSplitPane().getMinimumDividerLocation());

        this.add(getSplitPane());
    }

    @Override
    public Document getSvgDocument() {
        return renderer.getSvg_document();
    }
}
