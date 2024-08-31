package fr.sarainfras.caillou15.app.sign;

import fr.sarainfras.caillou15.app.Utils;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;

public class MentionUI extends JPanel {

    int selectedMentionIndex = 0;

    JPanel right_panel;
    JList<Mention> mentionsJList;
    DefaultListModel<Mention> mentionListModel;

    public MentionUI(DirectionalSign dir_sign) {
        this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        this.add(init_left_panel(dir_sign));
        right_panel = init_right_panel(dir_sign);
        this.add(right_panel);
    }

    public JPanel init_left_panel(DirectionalSign dir_sign) {
        JPanel panneau_gauche = new JPanel();
        panneau_gauche.setLayout(new GridBagLayout());

        // liste des mentions
        mentionListModel = new DefaultListModel<>();
        mentionsJList = new JList<>(mentionListModel);
        mentionsJList.setLayoutOrientation(JList.VERTICAL);
        mentionsJList.setVisibleRowCount(4);
        mentionsJList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        mentionsJList.setFixedCellWidth(100);
        int cellHeight = 20;
        mentionsJList.setFixedCellHeight(cellHeight);
        mentionsJList.setPreferredSize(new Dimension(mentionsJList.getFixedCellWidth(),cellHeight * 4));
        mentionListModel.addAll(dir_sign.getMentions());
        //mentionsJList.setModel(mentionListModel);
        mentionsJList.addListSelectionListener(e -> {
            selectedMentionIndex = mentionsJList.getSelectedIndex();

            if (selectedMentionIndex >= 0) {
                this.remove(right_panel);
                this.revalidate();
                right_panel = init_right_panel(dir_sign);
            } else right_panel.removeAll();
        });

        // contrôle de la liste des mentions
        JPanel mentions_list_control_panel = new JPanel();
        mentions_list_control_panel.setLayout(new GridLayout(1, 0));
        mentions_list_control_panel.setMinimumSize(
                new Dimension(110, 20));
        mentions_list_control_panel.setPreferredSize(
                new Dimension(110, 20));
        JButton bouton_ajouter = new JButton("+");
        bouton_ajouter.setMargin(new Insets(bouton_ajouter.getMargin().top, 3,
                bouton_ajouter.getMargin().bottom, 3));
        bouton_ajouter.addActionListener(e -> {
            dir_sign.getMentions().add(new Mention());
            mentionListModel.clear();
            mentionListModel.addAll(dir_sign.getMentions());
            //mentionsJList.setModel(mentionListModel);
            updateList(dir_sign, false);
        });
        JButton bouton_suppr = new JButton("-");
        bouton_suppr.setMargin(new Insets(bouton_suppr.getMargin().top, 3, bouton_suppr.getMargin().bottom, 3));
        bouton_suppr.addActionListener(e -> {
            if (mentionsJList.getSelectedIndex() > -1) {
                dir_sign.getMentions().remove(mentionsJList.getSelectedIndex());
                mentionListModel.clear();
                mentionListModel.addAll(dir_sign.getMentions());
                updateList(dir_sign, false);
                selectedMentionIndex = 0;
            }
        });

        GridBagConstraints mention_jlist_constaints = new GridBagConstraints();
        mention_jlist_constaints.gridx = 0;
        mention_jlist_constaints.gridy = 0;
        mention_jlist_constaints.gridheight = 1;
        mention_jlist_constaints.gridwidth = 2;
        mention_jlist_constaints.fill = GridBagConstraints.HORIZONTAL;
        panneau_gauche.add(mentionsJList, mention_jlist_constaints);

        GridBagConstraints bouton_plus_constaints = new GridBagConstraints();
        bouton_plus_constaints.gridx = 0;
        bouton_plus_constaints.gridy = 1;
        bouton_plus_constaints.fill = GridBagConstraints.HORIZONTAL;
        bouton_plus_constaints.weightx = 0.5;
        panneau_gauche.add(bouton_ajouter, bouton_plus_constaints);

        GridBagConstraints bouton_suppr_constaints = new GridBagConstraints();
        bouton_suppr_constaints.gridx = 1;
        bouton_suppr_constaints.gridy = 1;
        bouton_suppr_constaints.fill = GridBagConstraints.HORIZONTAL;
        bouton_suppr_constaints.weightx = 0.5;
        panneau_gauche.add(bouton_suppr, bouton_suppr_constaints);

        return panneau_gauche;
    }

    public JPanel init_right_panel(DirectionalSign directionalSign) {
        if (selectedMentionIndex < 0)
            return null;
        JPanel panel = new JPanel();

        panel.setLayout(new GridBagLayout());

        JLabel nom_label = new JLabel("Nom");
        GridBagConstraints nom_label_constraints = new GridBagConstraints();
        nom_label_constraints.gridx = 0;
        nom_label_constraints.gridy = 0;
        nom_label_constraints.insets = new Insets(0, 0, 0, 6);
        nom_label_constraints.anchor = GridBagConstraints.LINE_START;
        panel.add(nom_label, nom_label_constraints);

        //texte - nom mention
        JTextField nom_textfield = new JTextField();
        nom_textfield.setColumns(20);
        nom_textfield.setText(directionalSign.getMentions().get(selectedMentionIndex).nom);
        nom_textfield.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                directionalSign.getMentions().get(selectedMentionIndex).nom = nom_textfield.getText();
                updateList(directionalSign, true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                directionalSign.getMentions().get(selectedMentionIndex).nom = nom_textfield.getText();
                updateList(directionalSign, true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                directionalSign.getMentions().get(selectedMentionIndex).nom = nom_textfield.getText();
                updateList(directionalSign, true);
            }
        });
        nom_textfield.setEnabled(true);
        GridBagConstraints nom_textfield_constraints = new GridBagConstraints();
        nom_textfield_constraints.gridx = 1;
        nom_textfield_constraints.gridy = 0;
        panel.add(nom_textfield, nom_textfield_constraints);
        //L4 - police
        JLabel L4_label = new JLabel("L4");
        GridBagConstraints L4_label_constraints = new GridBagConstraints();
        L4_label_constraints.gridx = 0;
        L4_label_constraints.gridy = 1;
        L4_label_constraints.anchor = GridBagConstraints.LINE_START;
        panel.add(L4_label, L4_label_constraints);
        JCheckBox L4_checkbox = new JCheckBox();
        L4_checkbox.setEnabled(true);
        L4_checkbox.addActionListener(e -> {
            Font.SignFont font = directionalSign.getMentions().get(selectedMentionIndex).font;
            if (font == Font.SignFont.L4serre) {
                directionalSign.getMentions().get(selectedMentionIndex).font =
                        DirectionalSign.getSecondColor(directionalSign.color) ==
                        DirectionalSign.DirectionalSignColor.WHITE ?
                        Font.SignFont.L1serre : Font.SignFont.L2serre;
            } else { font = Font.SignFont.L4serre;
                directionalSign.getMentions().get(selectedMentionIndex).font = Font.SignFont.L4serre;
            }
        });
        GridBagConstraints L4_checkbox_constraints = new GridBagConstraints();
        L4_checkbox_constraints.gridx = 1;
        L4_checkbox_constraints.gridy = 1;
        panel.add(L4_checkbox, L4_checkbox_constraints);
        // ideogramme
        JLabel ideogram_label = new JLabel("Ideogramme");
        GridBagConstraints ideogram_label_constraints = new GridBagConstraints();
        ideogram_label_constraints.gridx = 0;
        ideogram_label_constraints.gridy = 2;
        panel.add(ideogram_label, ideogram_label_constraints);
        String[] signIdeogramArray = Utils.getNames(SignIdeogram.SignIdeogramType.class);
        JComboBox<String> ideogram_jComboBox = new JComboBox<>(signIdeogramArray);
        ideogram_jComboBox.addActionListener(e -> {
            directionalSign.getMentions().get(selectedMentionIndex).ideogram.type =
                    SignIdeogram.SignIdeogramType.values()[ideogram_jComboBox.getSelectedIndex()];
            directionalSign.setWith_ideogram(directionalSign.getMentions().get(selectedMentionIndex).ideogram.type
                    != SignIdeogram.SignIdeogramType.NONE);

        });
        GridBagConstraints ideogram_combobox_constraints = new GridBagConstraints();
        ideogram_combobox_constraints.gridx = 1;
        ideogram_combobox_constraints.gridy = 2;
        panel.add(ideogram_jComboBox, ideogram_combobox_constraints);
        // distance
        JLabel distance_label = new JLabel("Distance");
        GridBagConstraints distance_label_constraints = new GridBagConstraints();
        distance_label_constraints.gridx = 0;
        distance_label_constraints.gridy = 3;
        panel.add(distance_label, distance_label_constraints);
        JTextField distance_textField = new JTextField();
        distance_textField.setColumns(4);
        distance_textField.setText(String.valueOf(directionalSign.getMentions().get(selectedMentionIndex).distance));
        distance_textField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                directionalSign.getMentions().get(selectedMentionIndex).distance = Integer.parseInt(distance_textField.getText());
                updateList(directionalSign, true);
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                directionalSign.getMentions().get(selectedMentionIndex).distance = Integer.parseInt(distance_textField.getText());
                updateList(directionalSign, true);
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                directionalSign.getMentions().get(selectedMentionIndex).distance = Integer.parseInt(distance_textField.getText());
                updateList(directionalSign, true);
            }
        });
        distance_textField.setEnabled(true);
        GridBagConstraints distance_textField_constraints = new GridBagConstraints();
        distance_textField_constraints.gridx = 1;
        distance_textField_constraints.gridy = 3;
        panel.add(distance_textField, distance_textField_constraints);

        this.add(panel);

        return panel;
    }

    public void updateList(DirectionalSign dir_sign, boolean editing) {
        if (editing) {
            mentionsJList.revalidate();
        } else {
            mentionListModel.clear();
            mentionListModel.addAll(dir_sign.getMentions());
            mentionsJList.revalidate();
        }
    }
}
