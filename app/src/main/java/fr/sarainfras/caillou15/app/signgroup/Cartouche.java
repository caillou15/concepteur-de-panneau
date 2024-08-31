package fr.sarainfras.caillou15.app.signgroup;

import fr.sarainfras.caillou15.app.sign.DirectionalSign;
import fr.sarainfras.caillou15.app.sign.Font;

public class Cartouche {

    // @TODO faire le rendu des cartouches

    protected int numero;
    protected CartoucheType type;
    boolean computed = false;

    //dimensions pour le rendu
    private double longueur = 0;
    private double hauteur = 0;
    int numeroGamme = 0;
    private int largeurListel = 20;

    public int getNumero() {
        return numero;
    }

    public CartoucheType getType() {
        return type;
    }

    public double getLongueur() {
        return longueur;
    }

    public double getHauteur() {
        return hauteur;
    }

    public int getLargeurListel() {
        return largeurListel;
    }

    public enum CartoucheType {
        A, N, E, D, C, R, F, P
    }

    public Cartouche() {};

    public Cartouche(CartoucheType type, int ref) {
        this(type, ref, 0);
    }

    public Cartouche(CartoucheType type, int ref, int numeroGamme) {
        this.type = type;
        this.numero = ref;
        this.numeroGamme = numeroGamme;
    }

    public static DirectionalSign.DirectionalSignColor getColor(CartoucheType type) {
        return switch (type) {
            case A, N -> DirectionalSign.DirectionalSignColor.RED;
            case D -> DirectionalSign.DirectionalSignColor.YELLOW;
            case C, R, P -> DirectionalSign.DirectionalSignColor.WHITE;
            case E, F -> DirectionalSign.DirectionalSignColor.GREEN;
        };
    }

    public void computeLengths() {
        Font.SignFont font = switch (getType()) {
            case A, N, E, F -> Font.SignFont.L2serre;
            case D, C, R, P -> Font.SignFont.L1serre;
        };

        longueur = 0.5*DirectionalSign.gammes[numeroGamme] + Font.getTextLength(this.toString(), font, numeroGamme, false) + 2*largeurListel;
        hauteur = 1.5*DirectionalSign.gammes[numeroGamme] + 2*largeurListel;
        computed = true;
    }

    public void setLongueurListelFromDirSignGroup(DirectionalSignGroup dir_sign_grp) {
        int longueur_listel = 20;
        for (DirectionalSign dir_sign : dir_sign_grp.directionalSignArrayList) {
            if (dir_sign.getLargeur_listel() == 40) {longueur_listel = 40; return;}
        }
    }

    @Override
    public String toString() {
        return getType().toString() + " " + getNumero();
    }

    public boolean isComputed() {
        return computed;
    }
}
