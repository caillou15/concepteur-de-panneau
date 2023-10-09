package fr.sarainfras.caillou15.app.sign;

public class SignIdeogram {

    public SignIdeogramType type;

    public SignIdeogram() {
        type = SignIdeogramType.NONE;
    }

    public double getSize(int hc) { return 1.5*hc; }

    public double getColoredBorderSize(int hc) { return 1.4*hc; }

    public double getInternalSize(int hc) { return 1.2*hc; }

    public enum SignIdeogramType {
        NONE, SOMEETHING
    }
}
