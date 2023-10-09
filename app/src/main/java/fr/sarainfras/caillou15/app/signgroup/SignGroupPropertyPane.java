package fr.sarainfras.caillou15.app.signgroup;

import fr.sarainfras.caillou15.app.AppWindow;
import fr.sarainfras.caillou15.app.events.group.SignGroupChangeInitiater;
import fr.sarainfras.caillou15.app.events.group.SignGroupChangeListener;
import fr.sarainfras.caillou15.app.sign.DirectionalSign;
import fr.sarainfras.caillou15.app.sign.Sign;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.util.ArrayList;

public class SignGroupPropertyPane extends JPanel implements SignGroupChangeListener {

    AppWindow mainframe;
    DirectionalSignGroup directionalSignGroup;
    private SignGroupUI signGroupUI;
    private SignGroupChangeInitiater signGroupChangeInitiater;

    JPanel label_panel;
    JPanel property_panel;

    ArrayList<String> signNameArrayList;
    JList<String> signList;

    BoxLayout boxLayout;

    public SignGroupPropertyPane(AppWindow frame, SignGroupUI signGroupUI, DirectionalSignGroup directionalSignGroup,
                                 SignGroupChangeInitiater signGroupChangeInitiater) {
        this.mainframe = frame;
        this.directionalSignGroup = directionalSignGroup;
        signNameArrayList = new ArrayList<>();
        this.signGroupUI = signGroupUI;
        this.signGroupChangeInitiater = signGroupChangeInitiater;
        init();
    }

    public void init() {
        signGroupUI.getSignGroupChangeInitiater().addListener(this);
        init_gui();
    }

    public void init_gui() {
        boxLayout = new BoxLayout(this, BoxLayout.Y_AXIS);
        this.setLayout(boxLayout);
        // propriétés du groupe de panneaux
        // boutons de cartouche
        JPanel cartouches_bouton_panel = new JPanel();
        cartouches_bouton_panel.setLayout(new BoxLayout(cartouches_bouton_panel, BoxLayout.X_AXIS));
        JButton bouton_cartouche_1 = new JButton(" 1 ");
        bouton_cartouche_1.setMargin(new Insets(
                bouton_cartouche_1.getMargin().top, 3,
                bouton_cartouche_1.getMargin().bottom, 3
        ));
        bouton_cartouche_1.setSize(bouton_cartouche_1.getMinimumSize());
        bouton_cartouche_1.setEnabled(false);
        JButton bouton_cartouche_2 = new JButton(" 2 ");
        bouton_cartouche_2.setMargin(new Insets(
                bouton_cartouche_2.getMargin().top, 3,
                bouton_cartouche_2.getMargin().bottom, 3
        ));
        bouton_cartouche_2.setEnabled(false);
        JLabel label_cartouche = new JLabel("cartouche");
        label_cartouche.setBorder(new EmptyBorder(0, 5, 0, 5));
        cartouches_bouton_panel.add(label_cartouche);
        cartouches_bouton_panel.add(bouton_cartouche_1);
        cartouches_bouton_panel.add(bouton_cartouche_2);
        cartouches_bouton_panel.setMaximumSize(
                new Dimension(120, 20));
        cartouches_bouton_panel.setPreferredSize(new Dimension(100, 20));
        this.add(cartouches_bouton_panel);
        // case à cocher - alignement des panneaux
        JCheckBox alignement_checkbox = new JCheckBox("Alignement longueur");
        alignement_checkbox.addItemListener(e -> {
            directionalSignGroup.alignement_panneaux = e.getStateChange() == ItemEvent.SELECTED;
            signGroupChangeInitiater.signGroupUpdate();
            for (DirectionalSign dir_sign : directionalSignGroup.directionalSignArrayList) {
                signGroupUI.main_frame.getSignInitiater().signSet(dir_sign, true);
            }

            directionalSignGroup.compute_lenghts();
            signGroupChangeInitiater.signGroupUpdate();
        });
        this.add(alignement_checkbox);
        // liste de panneaux
        init_sign_list();
        // boutons de contrôle de la liste
        JPanel sign_list_control_panel = new JPanel();
        sign_list_control_panel.setLayout(new GridLayout(1, 0));
        sign_list_control_panel.setMaximumSize(
                new Dimension(110, 20));
        sign_list_control_panel.setPreferredSize(
                new Dimension(signList.getWidth(), 20));
        JButton bouton_ajouter = new JButton("+");
        bouton_ajouter.setMargin(new Insets(bouton_ajouter.getMargin().top, 3,
                bouton_ajouter.getMargin().bottom, 3));
        bouton_ajouter.addActionListener(e -> {
            signNameArrayList.add("VILLE");
            directionalSignGroup.directionalSignArrayList.add(new DirectionalSign("VILLE", Sign.SignID.D21));
            update_sign_list();
        });
        JButton bouton_suppr = new JButton("-");
        bouton_suppr.setMargin(new Insets(bouton_suppr.getMargin().top, 3, bouton_suppr.getMargin().bottom, 3));
        bouton_suppr.addActionListener(e -> {
            if (signList.getSelectedIndex() > -1) {
                signNameArrayList.remove(signList.getSelectedIndex());
                directionalSignGroup.directionalSignArrayList.remove(signList.getSelectedIndex());
                update_sign_list();
            }
        });
        JButton bouton_edit = new JButton("Edit");
        bouton_edit.setMargin(new Insets(bouton_edit.getMargin().top, 3, bouton_edit.getMargin().bottom, 3));
        bouton_edit.addActionListener(e -> {
            if (signList.getSelectedIndex() >= 0) {
                SignEditorDialog signEditorDialog;
                signEditorDialog = new SignEditorDialog(mainframe,
                        directionalSignGroup.directionalSignArrayList.get(signList.getSelectedIndex()),
                        signGroupUI.getSignGroupChangeInitiater());
                signEditorDialog.setVisible(true);
            }
        });
        sign_list_control_panel.add(bouton_ajouter);
        sign_list_control_panel.add(bouton_suppr);
        sign_list_control_panel.add(bouton_edit);
        this.add(sign_list_control_panel);

        signGroupUI.getSplitPane().setMinimumSize(signGroupUI.getSplitPane().getMinimumSize());
        signGroupUI.getSplitPane().setDividerLocation(signGroupUI.getSplitPane().getMinimumDividerLocation());
    }

    public void init_sign_list() {
        directionalSignGroup.directionalSignArrayList.forEach(directionalSign -> signNameArrayList.add(directionalSign.toString()));
        DefaultListModel<String> listModel = new DefaultListModel<>();
        signList = new JList<>(listModel);
        signList.setLayoutOrientation(JList.VERTICAL);
        signList.setVisibleRowCount(6);
        signList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        signList.setFixedCellWidth(100);
        int cellHeight = 20;
        signList.setFixedCellHeight(cellHeight);
        signList.setMinimumSize(new Dimension(signList.getFixedCellWidth(), cellHeight * 6));
        JPanel panel = new JPanel();
        panel.add(signList);
        this.add(panel);
    }

    public void update_sign_list_ui() {
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addAll(signNameArrayList);
        signList.setModel(listModel);
    }

    public void update_sign_list() {
        signNameArrayList.clear();
        signNameArrayList.addAll(directionalSignGroup.directionalSignArrayList.stream().map(sign -> sign.getText()[0]).toList());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addAll(signNameArrayList);
        signList.setModel(listModel);
    }

    public Component add(JComponent component) {
        if (!(component instanceof JTextArea || component instanceof JSpinner)) component.setMaximumSize(component.getMinimumSize());
        component.setAlignmentX(CENTER_ALIGNMENT);
        return super.add(component);
    }

    @Override
    public void signGroupChangeSet(DirectionalSignGroup new_sign) {
        update_sign_list();
    }

    @Override
    public void signGroupUpdate() {
        this.signGroupChangeSet(directionalSignGroup);
    }
}
