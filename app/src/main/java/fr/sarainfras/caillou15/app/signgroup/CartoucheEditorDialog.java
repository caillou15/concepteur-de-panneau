package fr.sarainfras.caillou15.app.signgroup;

import fr.sarainfras.caillou15.app.events.group.SignGroupChangeInitiater;

import javax.swing.*;

import java.awt.*;

import static java.awt.Frame.getFrames;

public class CartoucheEditorDialog extends JDialog {
    public CartoucheEditorDialog(DirectionalSignGroup dir_grp, int selectedCartouche, SignGroupChangeInitiater sign_grp_init) {
        super(getFrames()[0],true);
        this.setMinimumSize(new Dimension(250, 100));
        this.setLayout(new BoxLayout(this.getContentPane(), BoxLayout.Y_AXIS));
        this.add(new CartoucheUI(dir_grp, selectedCartouche));
        JButton bouton_envoyer = new JButton("envoyer");
        bouton_envoyer.addActionListener(e -> {
            this.dispose();
            sign_grp_init.signGroupChangeSet(dir_grp);
            sign_grp_init.signGroupUpdate();
        });
        this.add(bouton_envoyer);
        this.setVisible(true);
    }
}
