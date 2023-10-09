package fr.sarainfras.caillou15.app.sign;

public class SignSymbol {
    public SignSymbolType type;

    public SignSymbol() {
        this.type = SignSymbolType.NONE;
    }

    public double getSize(int hc) { return 2.5*hc; }

    public double getBlueSquareSize(int hc) { return 2.38*hc; }

    public double getRedCircleSize(int hc) { return getBlueSquareSize(hc); }

    public double getWhiteInnerCircleSize(int hc) { return 1.88*hc;}

    public enum SignSymbolType {
        NONE, INTERDICTION, INDICATION
    }
}
