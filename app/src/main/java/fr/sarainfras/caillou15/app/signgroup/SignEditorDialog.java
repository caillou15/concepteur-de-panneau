package fr.sarainfras.caillou15.app.signgroup;

import fr.sarainfras.caillou15.app.AppWindow;
import fr.sarainfras.caillou15.app.events.group.SignGroupChangeInitiater;
import fr.sarainfras.caillou15.app.sign.DirectionalSign;
import fr.sarainfras.caillou15.app.sign.SignUI;

import javax.swing.*;
import java.awt.*;

public class SignEditorDialog extends JDialog {

    public SignEditorDialog(AppWindow frame, DirectionalSign dir_sign,
                            SignGroupChangeInitiater signGroupChangeInitiater) {
        super(frame, true);
        this.setMinimumSize(new Dimension(500, 400));
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new SignUI(frame, dir_sign, false));
        JButton bouton_envoyer = new JButton("envoyer");
        bouton_envoyer.addActionListener(e -> {
            this.dispose();
            signGroupChangeInitiater.signGroupUpdate();
        });
        panel.add(bouton_envoyer);
        this.add(panel);
    }


}
