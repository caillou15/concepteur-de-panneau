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
    JList<String> cartoucheList;
    ArrayList<String> cartoucheRefArrayList;

    BoxLayout boxLayout;

    public SignGroupPropertyPane(AppWindow frame, SignGroupUI signGroupUI, DirectionalSignGroup directionalSignGroup,
                                 SignGroupChangeInitiater signGroupChangeInitiater) {
        this.mainframe = frame;
        this.directionalSignGroup = directionalSignGroup;
        signNameArrayList = new ArrayList<>();
        cartoucheRefArrayList = new ArrayList<>();
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
        init_cartouche_list();
        JLabel label_cartouche = new JLabel("cartouche");
        label_cartouche.setBorder(new EmptyBorder(0, 5, 0, 5));
        cartouches_bouton_panel.add(label_cartouche);
        cartouches_bouton_panel.setMaximumSize(
                new Dimension(120, 20));
        cartouches_bouton_panel.setPreferredSize(new Dimension(100, 20));
        this.add(cartouches_bouton_panel);
        this.add(init_cartouche_list_control_panel());
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
        this.add(init_sign_list_control_panel());

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

    public void update_sign_list() {
        signNameArrayList.clear();
        signNameArrayList.addAll(directionalSignGroup.directionalSignArrayList.stream().map(Object::toString).toList());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addAll(signNameArrayList);
        signList.setModel(listModel);
    }

    public JPanel init_sign_list_control_panel() {
        JPanel sign_list_control_panel = new JPanel();
        sign_list_control_panel.setLayout(new GridLayout(1, 0));
        sign_list_control_panel.setMaximumSize(
                new Dimension(110, 20));
        sign_list_control_panel.setPreferredSize(
                new Dimension(signList.getWidth(), 20));
        JButton bouton_ajouter_sign = new JButton("+");
        bouton_ajouter_sign.setMargin(new Insets(bouton_ajouter_sign.getMargin().top, 3,
                bouton_ajouter_sign.getMargin().bottom, 3));
        bouton_ajouter_sign.addActionListener(e -> {
            signNameArrayList.add("VILLE");
            DirectionalSign dir_sign = new DirectionalSign(Sign.SignID.D21);
            dir_sign.computeLengths();
            directionalSignGroup.directionalSignArrayList.add(dir_sign);
            update_sign_list();
            signGroupUI.getSignGroupChangeInitiater().signGroupUpdate();
        });
        JButton bouton_suppr_sign = new JButton("-");
        bouton_suppr_sign.setMargin(new Insets(bouton_suppr_sign.getMargin().top, 3, bouton_suppr_sign.getMargin().bottom, 3));
        bouton_suppr_sign.addActionListener(e -> {
            if (signList.getSelectedIndex() > -1) {
                signNameArrayList.remove(signList.getSelectedIndex());
                directionalSignGroup.directionalSignArrayList.remove(signList.getSelectedIndex());
                update_sign_list();
                signGroupUI.getSignGroupChangeInitiater().signGroupUpdate();
                directionalSignGroup.directionalSignArrayList.forEach(sign -> {if (!sign.isComputed()) sign.computeLengths();});
            }
        });
        JButton bouton_edit_sign = new JButton("Edit");
        bouton_edit_sign.setMargin(new Insets(bouton_edit_sign.getMargin().top, 3, bouton_edit_sign.getMargin().bottom, 3));
        bouton_edit_sign.addActionListener(e -> {
            if (signList.getSelectedIndex() >= 0) {
                SignEditorDialog signEditorDialog;
                signEditorDialog = new SignEditorDialog(mainframe,
                        directionalSignGroup.directionalSignArrayList.get(signList.getSelectedIndex()),
                        signGroupUI.getSignGroupChangeInitiater());
                signEditorDialog.setVisible(true);
            }
        });
        sign_list_control_panel.add(bouton_ajouter_sign);
        sign_list_control_panel.add(bouton_suppr_sign);
        sign_list_control_panel.add(bouton_edit_sign);
        return sign_list_control_panel;
    }

    public void init_cartouche_list() {
        directionalSignGroup.cartoucheArrayList.forEach(cartouche -> cartoucheRefArrayList.add(cartouche.toString()));
        DefaultListModel<String> listModel = new DefaultListModel<>();
        cartoucheList = new JList<>(listModel);
        cartoucheList.setLayoutOrientation(JList.VERTICAL);
        cartoucheList.setVisibleRowCount(6);
        cartoucheList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        cartoucheList.setFixedCellWidth(100);
        int cellHeight = 20;
        cartoucheList.setFixedCellHeight(cellHeight);
        cartoucheList.setMinimumSize(new Dimension(cartoucheList.getFixedCellWidth(), cellHeight * 6));
        JPanel panel = new JPanel();
        panel.add(cartoucheList);
        this.add(panel);
    }

    public void update_cartouche_list() {
        cartoucheRefArrayList.clear();
        cartoucheRefArrayList.addAll(directionalSignGroup.cartoucheArrayList.stream().map(Object::toString).toList());
        DefaultListModel<String> listModel = new DefaultListModel<>();
        listModel.addAll(cartoucheRefArrayList);
        cartoucheList.setModel(listModel);
    }

    public JPanel init_cartouche_list_control_panel() {
        JPanel cartouche_list_control_panel = new JPanel();
        cartouche_list_control_panel.setLayout(new GridLayout(1, 0));
        cartouche_list_control_panel.setMaximumSize(
                new Dimension(110, 20));
        cartouche_list_control_panel.setPreferredSize(
                new Dimension(cartoucheList.getWidth(), 20));
        JButton bouton_ajouter_cartouche = new JButton("+");
        bouton_ajouter_cartouche.setMargin(new Insets(bouton_ajouter_cartouche.getMargin().top, 3,
                bouton_ajouter_cartouche.getMargin().bottom, 3));
        bouton_ajouter_cartouche.addActionListener(e -> {
            if (directionalSignGroup.getSignList().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Pour pouvoir ajouter un cartouche, il faut avoir aumoins un panneau.");
                return;
            }
            cartoucheRefArrayList.add("D 1");
            Cartouche cartouche = new Cartouche(Cartouche.CartoucheType.D, 1);
            cartouche.computeLengths();
            directionalSignGroup.cartoucheArrayList.add(cartouche);
            update_cartouche_list();
            signGroupUI.getSignGroupChangeInitiater().signGroupUpdate();
        });
        JButton bouton_suppr_cartouche = new JButton("-");
        bouton_suppr_cartouche.setMargin(new Insets(bouton_suppr_cartouche.getMargin().top, 3, bouton_suppr_cartouche.getMargin().bottom, 3));
        bouton_suppr_cartouche.addActionListener(e -> {
            if (cartoucheList.getSelectedIndex() > -1) {
                cartoucheRefArrayList.remove(cartoucheList.getSelectedIndex());
                directionalSignGroup.cartoucheArrayList.remove(cartoucheList.getSelectedIndex());
                update_cartouche_list();
                signGroupUI.getSignGroupChangeInitiater().signGroupUpdate();
                directionalSignGroup.cartoucheArrayList.forEach(cartouche -> {if (!cartouche.isComputed()) cartouche.computeLengths();});
            }
        });
        JButton bouton_edit_cartouche = new JButton("Edit");
        bouton_edit_cartouche.setMargin(new Insets(bouton_edit_cartouche.getMargin().top, 3, bouton_edit_cartouche.getMargin().bottom, 3));
        bouton_edit_cartouche.addActionListener(e -> {
            if (cartoucheList.getSelectedIndex() >= 0) {
                CartoucheEditorDialog cartoucheEditorDialog;
                cartoucheEditorDialog = new CartoucheEditorDialog(directionalSignGroup, cartoucheList.getSelectedIndex(),
                        signGroupUI.getSignGroupChangeInitiater());
                update_cartouche_list();
            }
        });
        cartouche_list_control_panel.add(bouton_ajouter_cartouche);
        cartouche_list_control_panel.add(bouton_suppr_cartouche);
        cartouche_list_control_panel.add(bouton_edit_cartouche);
        return cartouche_list_control_panel;
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
