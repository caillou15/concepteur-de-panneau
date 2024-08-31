package fr.sarainfras.caillou15.app.sign;

public class Mention {
    public String nom = "MENTION";
    public Font.SignFont font = Font.SignFont.L1serre;
    public SignIdeogram ideogram = new SignIdeogram();
    public int distance = 0;

    @Override
    public String toString() { return nom; }

    @Override
    public int hashCode() {
        int hash = 0;

        hash += nom.hashCode();
        hash += font.hashCode();
        hash += ideogram.hashCode();
        hash += distance;

        return hash;
    }
}
