package fr.sarainfras.caillou15.app.sign;

import fr.sarainfras.caillou15.app.events.sign.SignInitiater;

import javax.swing.*;
import java.util.LinkedHashMap;

public class PrioritySign extends Sign{
    @Override
    public String[] SignTypesForID_names() {
        return new String[]{
                "AB1", "AB2", "AB3a", "AB3b", "AB4", "AB5", "AB6", "AB7", "AB25"
        };
    }

    @Override
    public LinkedHashMap<String, JComponent> getPropertiesGUIComponents(SignInitiater signInitiater) {
        return new LinkedHashMap<>();
    }
}
