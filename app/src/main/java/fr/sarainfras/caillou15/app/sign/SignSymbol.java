package fr.sarainfras.caillou15.app.sign;

public class SignSymbol {
    public SignSymbolType type;

    public SignSymbol() {
        this.type = SignSymbolType.NONE;
    }

    public static double getSize(double hc) { return 2.5*hc; }

    public static double getBlueSquareSize(double hc) { return 2.38*hc; }

    public static double getRedCircleSize(double hc) { return getBlueSquareSize(hc); }

    public static double getWhiteInnerCircleSize(double hc) { return 1.88*hc;}

    public enum SignSymbolType {
        NONE, INTERDICTION, INDICATION
    }
}
