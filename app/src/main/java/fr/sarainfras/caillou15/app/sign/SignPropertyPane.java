package fr.sarainfras.caillou15.app.sign;

import fr.sarainfras.caillou15.app.AppWindow;
import fr.sarainfras.caillou15.app.EditorUI;
import fr.sarainfras.caillou15.app.Utils;
import fr.sarainfras.caillou15.app.events.sign.SignInitiater;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import static fr.sarainfras.caillou15.app.Utils.getNames;

public class SignPropertyPane extends JPanel {
    AppWindow mainFrame;
    EditorUI editorUI;

    SignInitiater signInitiater;
    Sign sign = null;
    boolean newSign = true;

    boolean isIDShown;

    // panneaux principaux
    JPanel label_panel;
    JPanel property_panel;

    JPanel grid_label_and_property_panel;
    int property_index = 0;

    // composants de propriétés
    JComboBox<String> comboBoxType;
    JComboBox<String> comboBoxID;

    BoxLayout boxLayout;

    public SignPropertyPane(AppWindow currentFrame, EditorUI editorUI) {
        this.mainFrame = currentFrame;
        this.editorUI = editorUI;
        this.isIDShown = false;
        init();
    }

    public SignPropertyPane(AppWindow currentFrame, Sign sign, EditorUI editorUI) {
        this.mainFrame = currentFrame;
        this.editorUI = editorUI;
        this.setSign(sign);
        this.isIDShown = false;
        this.newSign = false;
        init();
    }

    private void init() {
        signInitiater = mainFrame.getSignInitiater();
        init_gui();
    }

    private void init_gui() {
        grid_label_and_property_panel = new JPanel();
        grid_label_and_property_panel.setLayout(new GridBagLayout());

        label_panel = new JPanel();
        label_panel.setLayout(new BoxLayout(label_panel, BoxLayout.Y_AXIS));
        label_panel.add(Box.createVerticalStrut(3));
        property_panel = new JPanel();
        GridLayout gridLayout = new GridLayout(0,1);
        gridLayout.setVgap(4);
        property_panel.setLayout(gridLayout);
        boxLayout = new BoxLayout(this, BoxLayout.X_AXIS);
        //this.add(label_panel);
        //this.add(property_panel);
        this.add(grid_label_and_property_panel);
        init_combo_type();
    }

    private void init_combo_type() {
        String[] signTypeArray = getNames(Sign.SignType.class);
        signTypeArray = Utils.addElementAtFirstinStringArray(signTypeArray);
        comboBoxType = new JComboBox<>(signTypeArray);
        this.add(comboBoxType, "type", property_index);
        if (!newSign) {
            comboBoxType.setSelectedItem(sign.getType().name());
            comboBoxType.setEnabled(false);
            init_combo_id();
        } else {
            comboBoxType.addActionListener(e -> {
                if (comboBoxType.getSelectedIndex() != 0) {
                    Sign.SignType signType = Sign.SignType.values()[comboBoxType.getSelectedIndex()-1];
                    switch (signType) {
                        //case CIRCLE -> setSign(new CircleSign());
                        case PRIORITY -> setSign(new PrioritySign());
                        case DIRECTIONAL -> setSign(new DirectionalSign());
                        default -> { JOptionPane.showMessageDialog((JComponent)e.getSource(), "Ce type de panneaux n'a pas encore été implémenté."); return; }
                    }
                    sign.setType(Sign.SignType.values()[comboBoxType.getSelectedIndex()-1]);
                    comboBoxType.setEnabled(false);
                    init_combo_id();
                    repaint();
                    revalidate();

                }
            });
        }
    }

    private void init_combo_id() {
        String[] signIDArray = sign.SignTypesForID_names();
        signIDArray = Utils.addElementAtFirstinStringArray(signIDArray);
        comboBoxID = new JComboBox<>(signIDArray);
        if (!newSign) {
            comboBoxID.setSelectedItem(sign.getSignID().name());
            comboBoxID.setEnabled(false);
        } else {
            if (!isIDShown) {
                comboBoxID.addActionListener(e -> {
                    if (comboBoxID.getSelectedIndex() != 0) {
                        comboBoxID.setEnabled(false);
                        sign.setSignID((String)comboBoxID.getSelectedItem());
                        if (sign.getSignID() == Sign.SignID.D29)
                            JOptionPane.showMessageDialog(mainFrame, "non implémenté");

                        signInitiater.signSet(sign, true);
                        editorUI.revalidate();
                    }
                });
            }
        }
        this.add(comboBoxID, "ID", property_index);
        this.isIDShown = true;
        init_properties();
    }

    private void init_properties() {
        Map<String, JComponent> componentArray;
        if (sign instanceof DirectionalSign dir_sign) {
            if (newSign) componentArray = dir_sign.getPropertiesGUIComponents(signInitiater, true);
            else componentArray = sign.getPropertiesGUIComponents(signInitiater);
        } else componentArray = new HashMap<>();

        componentArray.forEach((name, component) -> {
            component.setSize(component.getPreferredSize());
            this.add(component, name, property_index);
        });
        editorUI.getSplitPane().setDividerLocation(editorUI.getSplitPane().getMinimumDividerLocation());
        editorUI.getSplitPane().getRightComponent().revalidate();
    }

    public void setSign(Sign sign) {
        this.sign = sign;

    }

    public Component add(JComponent component, String name, int pos) {
        if (!(component instanceof JTextArea || component instanceof JSpinner)) component.setMaximumSize(component.getMinimumSize());
        component.setAlignmentX(CENTER_ALIGNMENT);
        JLabel jLabel = new JLabel(name);
        //jLabel.setBorder(new EmptyBorder(6, 0, 6, 0));
        label_panel.add(jLabel);
        label_panel.add(Box.createVerticalStrut(2));
        GridBagConstraints label_constraints = new GridBagConstraints();
        label_constraints.gridx = 0;
        label_constraints.gridy = pos;
        label_constraints.anchor = GridBagConstraints.LINE_START;
        label_constraints.insets = new Insets(0, 0, 0, 5);
        grid_label_and_property_panel.add(jLabel, label_constraints);
        GridBagConstraints comp_constraints = new GridBagConstraints();
        comp_constraints.gridx = 1;
        comp_constraints.gridy = pos;
        comp_constraints.anchor = GridBagConstraints.LINE_START;
        comp_constraints.insets = new Insets(0, 0, 3, 0);
        comp_constraints.fill = GridBagConstraints.BOTH;
        grid_label_and_property_panel.add(component, comp_constraints);
        //Component result = property_panel.add(component);
        //component.setSize(component.getMaximumSize());
        //this.setSize(getWidth()+20, getHeight());

        property_index++;
        return null/*result*/;
    }
}
