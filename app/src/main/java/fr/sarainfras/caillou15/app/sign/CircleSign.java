package fr.sarainfras.caillou15.app.sign;

import fr.sarainfras.caillou15.app.events.sign.SignInitiater;

import javax.swing.*;
import java.util.LinkedHashMap;

public class CircleSign extends Sign{
    @Override
    public String[] SignTypesForID_names() {
        return new String[]{};
    }

    @Override
    public LinkedHashMap<String, JComponent> getPropertiesGUIComponents(SignInitiater signInitiater) {
        return new LinkedHashMap<>();
    }

}
