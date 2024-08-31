package fr.sarainfras.caillou15.app.sign;

public class SignIdeogram {

    public SignIdeogramType type;

    public SignIdeogram() {
        type = SignIdeogramType.NONE;
    }

    public static double getSize(int hc) { return 1.5*hc; }

    public static double getColoredBorderSize(int hc) { return 1.4*hc; }

    public static double getInternalSize(int hc) { return 1.2*hc; }

    public enum SignIdeogramType {
        NONE, SOMETHING
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof SignIdeogram) return equals((SignIdeogram) o);
        else return false;
    }

    public boolean equals(SignIdeogram other_ideogram) {
        boolean result = true;

        result &= (this.type == other_ideogram.type);

        return result;
    }

    @Override
    public int hashCode() {
        int hash = 0;

        hash += type.hashCode();

        return hash;
    }
}
