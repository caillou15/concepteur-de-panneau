package fr.sarainfras.caillou15.app.sign;

import fr.sarainfras.caillou15.app.events.sign.SignInitiater;

import javax.swing.*;
import java.util.LinkedHashMap;
import java.util.Objects;

public abstract class Sign {

    private SignType type;
    private SignID signID;
    DirectionalSign.DirectionalSignColor color;

    double width;

    protected String[] propertiesNameArray;

    protected Sign() {
    }

    public SignType getType() {
        return type;
    }

    public void setType(SignType type) {
        this.type = type;

    }

    public SignID getSignID() {
        return signID;
    }

    public void setSignID(SignID signID) {
        this.signID = signID;
    }

    public void setSignID(String signIDString) {
        for (SignID currentSignID:SignID.values()) {
            if (Objects.equals(signIDString, currentSignID.name())) {
                setSignID(currentSignID);
                return;
            }
        }
    }

    public String[] getPropertiesNameArray() {
        return propertiesNameArray;
    }

    public double getWidth() {
        return width;
    }

    public void setWidth(double width) {
        this.width = width;
    }

    public enum SignType {
        CIRCLE, TRIANGLE, SQUARED, DIRECTIONAL, PRIORITY, OTHER
    }

    public enum SignID {
        AB1, AB2, AB3a, AB3b, AB4, AB5, AB6, AB7, AB25,
        D21, D29
    }

    public abstract String[] SignTypesForID_names();

    public abstract LinkedHashMap<String, JComponent> getPropertiesGUIComponents(SignInitiater signInitiater);

}
