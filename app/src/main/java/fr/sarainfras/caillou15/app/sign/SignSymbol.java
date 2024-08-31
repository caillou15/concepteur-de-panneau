package fr.sarainfras.caillou15.app.sign;

public class SignSymbol {
    public SignSymbolType type;

    public SignSymbol() {
        this.type = SignSymbolType.NONE;
    }

    public static double getSize(int hc) { return 2.5*hc; }

    public static double getBlueSquareSize(int hc) { return 2.38*hc; }

    public static double getRedCircleSize(int hc) { return getBlueSquareSize(hc); }

    public static double getWhiteInnerCircleSize(int hc) { return 1.88*hc;}

    public enum SignSymbolType {
        NONE, INTERDICTION, INDICATION
    }
}
