package fr.sarainfras.caillou15.app.events.sign;

import fr.sarainfras.caillou15.app.sign.Sign;

import java.util.ArrayList;
import java.util.List;

public class SignInitiater {
    private final List<SignListener> listeners = new ArrayList<>();

    public void addListener(SignListener toAdd) {
        listeners.add(toAdd);
    }

    /**
     * distribue l'évènement du changement de panneau
     * @param sign nouveau type de panneau
     */
    public void signSet(Sign sign, boolean firstRender) {
        for (SignListener hl : listeners)
            hl.signSet(sign, firstRender);
    }
}
