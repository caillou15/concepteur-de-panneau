package fr.sarainfras.caillou15.app.events.group;

import fr.sarainfras.caillou15.app.signgroup.DirectionalSignGroup;

public interface SignGroupChangeListener{
    void signGroupChangeSet(DirectionalSignGroup new_sign);
    void signGroupUpdate();
}
