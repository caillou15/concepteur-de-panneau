package fr.sarainfras.caillou15.app.events.sign;

import fr.sarainfras.caillou15.app.sign.Sign;

public interface SignListener {
    void signSet(Sign sign, boolean firstRender);
}
