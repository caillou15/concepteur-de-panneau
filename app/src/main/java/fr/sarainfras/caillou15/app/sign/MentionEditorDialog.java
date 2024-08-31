package fr.sarainfras.caillou15.app.sign;

import fr.sarainfras.caillou15.app.events.sign.SignInitiater;

import javax.swing.*;
import java.awt.*;

import static java.awt.Frame.getFrames;

public class MentionEditorDialog extends JDialog {

    public MentionEditorDialog(DirectionalSign dir_sign,
                            SignInitiater signInitiater) {
        super(getFrames()[0], true);
        this.setMinimumSize(new Dimension(400, 200));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new MentionUI(dir_sign));
        JButton bouton_envoyer = new JButton("envoyer");
        bouton_envoyer.addActionListener(e -> {
            this.dispose();
            signInitiater.signSet(dir_sign, true);
        });
        panel.add(bouton_envoyer);
        this.add(panel);
        this.setVisible(true);
    }
}
