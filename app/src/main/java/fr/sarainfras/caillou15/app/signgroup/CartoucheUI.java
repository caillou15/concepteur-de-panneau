package fr.sarainfras.caillou15.app.signgroup;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class CartoucheUI extends JPanel {

    int selectedCartouche = 0;

    Cartouche cartouche;
    public CartoucheUI(DirectionalSignGroup dir_grp, int selectedCartouche) {
        this.selectedCartouche = selectedCartouche;
        cartouche = dir_grp.cartoucheArrayList.get(selectedCartouche);

        JLabel typeLabel = new JLabel("Type : ");
        JComboBox<Cartouche.CartoucheType> typeComboBox = new JComboBox<>(Cartouche.CartoucheType.values());
        typeComboBox.addActionListener(e ->
                cartouche.type = Cartouche.CartoucheType.values()[typeComboBox.getSelectedIndex()]);

        JLabel refLabel = new JLabel("Numéro : ");
        JTextField refTextField = new JTextField();
        refTextField.setColumns(5);
        refTextField.setText(String.valueOf(cartouche.getNumero()));
        refTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                cartouche.numero = Integer.parseInt(refTextField.getText());
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                cartouche.numero = Integer.parseInt(refTextField.getText());
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                cartouche.numero = Integer.parseInt(refTextField.getText());
            }
        });
        this.add(typeLabel);
        this.add(typeComboBox);
        this.add(refLabel);
        this.add(refTextField);
    }
}
