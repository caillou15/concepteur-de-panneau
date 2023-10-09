package fr.sarainfras.caillou15.app.events.group;

import fr.sarainfras.caillou15.app.signgroup.DirectionalSignGroup;

import java.util.ArrayList;
import java.util.List;

public class SignGroupChangeInitiater {
    private final List<SignGroupChangeListener> listeners = new ArrayList<>();

    public void addListener(SignGroupChangeListener toAdd) {
        listeners.add(toAdd);
    }

    /**
     * distribue l'évènement
     * @param sign_grp nouveau groupe de panneau
     */
    public void signGroupChangeSet(DirectionalSignGroup sign_grp) {
        for (SignGroupChangeListener hl : listeners)
            hl.signGroupChangeSet(sign_grp);
    }

    public void signGroupUpdate() {
        for (SignGroupChangeListener sgl : listeners)
            sgl.signGroupUpdate();

    }
}
