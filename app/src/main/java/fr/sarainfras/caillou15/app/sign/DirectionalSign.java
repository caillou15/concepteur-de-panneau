package fr.sarainfras.caillou15.app.sign;

import fr.sarainfras.caillou15.app.SignException;
import fr.sarainfras.caillou15.app.Utils;
import fr.sarainfras.caillou15.app.events.sign.SignInitiater;
import org.apache.commons.lang3.NotImplementedException;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import java.awt.event.*;
import java.util.*;

import static fr.sarainfras.caillou15.app.sign.Font.SignFont.*;

public class DirectionalSign extends Sign {

    private ArrayList<Mention> mentions;
    private Font.SignFont font = L1serre;
    private int lineNumber = 1;
    private int numero_gamme = 0;
    boolean with_symbol = false;
    private DirectionalSignDirection signDirection;
    private double longueur_supplementaire = 0;
    private boolean with_distance = false;
    private boolean with_ideogram = false;
    private SignSymbol signSymbol;

    public static final int[] gammes = {100, 125, 160, 200};

    private boolean computed = false;

    int hauteur_base = 100;
    private int hauteur_composition = 0;

    private double longueur = 0;
    private double longueur_pointe_fleche = 0;
    private double longueur_texte = 0;
    private double hauteur = 0;
    int hauteur_texte = 0;
    private int largeur_listel = 20;
    boolean isLong = false;


    public DirectionalSign() {
        this(SignID.D21);
    }

    public DirectionalSign(Sign.SignID signID) {
        this.setSignID(signID);
        this.setType(SignType.DIRECTIONAL);
        this.mentions = new ArrayList<>();
        this.mentions.add(new Mention());
        this.color = DirectionalSignColor.WHITE;
        this.signSymbol = new SignSymbol();
        this.signDirection = DirectionalSignDirection.RIGHT;
    }

    @Override
    public String[] SignTypesForID_names() {
        return new String[]{
            "D21",
            "D29"
        };
    }

    @Override
    public LinkedHashMap<String, JComponent> getPropertiesGUIComponents(SignInitiater signInitiater) {
        return getPropertiesGUIComponents(signInitiater, false);
    }

    public LinkedHashMap<String, JComponent>
    getPropertiesGUIComponents(SignInitiater signInitiater, boolean new_sign) {

        DirectionalSign this_sign = this;
        LinkedHashMap<String, JComponent> componentsArray = new LinkedHashMap<>();
        // bouton éditeur de mentions
        JButton mention_editor_button = new JButton("Editeur de mentions...");
        mention_editor_button.addActionListener(e -> {
            MentionEditorDialog dialog = new MentionEditorDialog(this, signInitiater);
        });
        // couleur
        String[] signColorArray = Utils.getNames(DirectionalSignColor.class);
        signColorArray = Utils.addElementAtFirstinStringArray(signColorArray);
        JComboBox<String> color_jComboBox =
                new JComboBox<>(signColorArray);
        color_jComboBox.addActionListener(e -> {
            if (color_jComboBox.getSelectedIndex() != 0) {
                color = DirectionalSignColor.values()[color_jComboBox.getSelectedIndex()-1];
                switch (color) {
                    case WHITE, YELLOW -> font = L1serre;
                    case GREEN, BLUE, BROWN, BLACK, RED -> font = L2serre;
                }
                signInitiater.signSet(this_sign, false);
            }
        });
        if (!new_sign) {
            color_jComboBox.setSelectedItem(this.color.name());
        }
        // direction du panneau
        String[] signDirectionArray = Utils.getNames(DirectionalSignDirection.class);
        signDirectionArray = Utils.addElementAtFirstinStringArray(signDirectionArray);
        JComboBox<String> direction_jComboBox =
                new JComboBox<>(/*DirectionalSignColor.values()*/signDirectionArray);
        direction_jComboBox.addActionListener(e -> {
            if (direction_jComboBox.getSelectedIndex() != 0) {
                signDirection = DirectionalSignDirection.values()[direction_jComboBox.getSelectedIndex()-1];
                signInitiater.signSet(this_sign, false);
            }
        });
        if (!new_sign) {
            direction_jComboBox.setSelectedItem(this.signDirection.name());
        }
        // longueur supplementaire
        JSpinner long_supp_spinner = new JSpinner(new SpinnerNumberModel());
        long_supp_spinner.addChangeListener(e -> {
            SpinnerNumberModel spinnerModel = (SpinnerNumberModel) long_supp_spinner.getModel();
            this_sign.setLongueur_supplementaire(spinnerModel.getNumber().doubleValue());
            signInitiater.signSet(this_sign, false);
        });
        if (!new_sign) {
            long_supp_spinner.setValue(this.longueur_supplementaire);
        }
        // activation distances
        JCheckBox distances_checkBox = new JCheckBox();
        distances_checkBox.addItemListener(e -> {
            this_sign.setWith_distance(!this_sign.isWith_distance());
            if (distances_checkBox.isSelected()!=this.with_distance)
                distances_checkBox.setSelected(this.with_distance);
            signInitiater.signSet(this_sign, false);
        });
        if (!new_sign) {
            distances_checkBox.setSelected(this.with_distance);
        }
        // symbole
        String[] signSymbolArray = Utils.getNames(SignSymbol.SignSymbolType.class);
        JComboBox<String> symbol_jComboBox = new JComboBox<>(signSymbolArray);
        symbol_jComboBox.addActionListener(e -> {
            getSignSymbol().type = SignSymbol.SignSymbolType.values()[symbol_jComboBox.getSelectedIndex()];
            signInitiater.signSet(this_sign, false);
        });
        // retour
        componentsArray.put("mentions", mention_editor_button);
        componentsArray.put("couleur", color_jComboBox);
        componentsArray.put("direction", direction_jComboBox);
        componentsArray.put("long. supp.", long_supp_spinner);
        componentsArray.put("distances ?", distances_checkBox);
        componentsArray.put("symbole", symbol_jComboBox);
        return componentsArray;
    }

    public void computeLengths() throws NotImplementedException {
        switch (getSignID()) {
            case D21 -> computeLenghts_D21_new();
            default -> throw new NotImplementedException();
        }
        width = longueur;
        computed = true;
    }

    public void computeLenghts_D21_new() throws NotImplementedException {
        lineNumber = mentions.size();
        if (lineNumber == 0 || lineNumber > 4)
            JOptionPane.showMessageDialog(null, "le nombre de ligne est incorrect ou supérieur à 4");
        else {
            hauteur_base = gammes[getNumero_gamme()];
            longueur_texte = 0;
            longueur = 0;
            longueur_pointe_fleche = 0;
            hauteur = 0;

            switch (getColor()) {
                case BLUE, GREEN, BROWN, BLACK, RED -> hauteur_composition = gammes[numero_gamme+1];
                case WHITE, YELLOW -> hauteur_composition = gammes[numero_gamme];
            }
            for (int i = 0; i < mentions.size(); i++) {
                double longueur_ligne = Font.getTextLength(mentions.get(i).nom, mentions.get(i).font,getNumero_gamme(), true);
                if (getMentions().get(i).ideogram.type != SignIdeogram.SignIdeogramType.NONE)
                    longueur_ligne += SignIdeogram.getSize(hauteur_composition);
                if (longueur_ligne > longueur_texte) longueur_texte = longueur_ligne;
            }

            longueur_texte = longueur_texte + longueur_supplementaire;
            if (isWith_distance()) {
                double longueur_distance = 0;
                for (int i = 0; i < getMentions().size(); i++) {
                    double longueur_ligne = Font.getTextLength(String.valueOf(getMentions().get(i).distance),
                            getFont(), getNumero_gamme(), true);
                    if (longueur_ligne > longueur_distance) longueur_distance = longueur_ligne;
                }
                longueur_texte = longueur_texte + longueur_distance + 2*hauteur_composition;
            }

            double longueur_sans_pointe = 0.0;

            if (getSignSymbol().type != SignSymbol.SignSymbolType.NONE) {
                longueur_sans_pointe += 2.5*hauteur_composition + hauteur_composition;
            } else
            if (with_ideogram) {
                longueur_sans_pointe += 1.5*hauteur_composition + hauteur_composition/2.0;
            }

            longueur_sans_pointe += longueur_texte + hauteur_composition + largeur_listel;

            isLong = longueur_sans_pointe + 1.4 * getHauteur_composition() - 7 > 2500;

            largeur_listel = isLong ? 40 : 20;

            longueur_pointe_fleche = hauteur_composition;

            switch (getLineNumber()) {
                case 1 -> {
                    longueur_pointe_fleche *= with_ideogram ? 2.1 : 1.4;
                    longueur_pointe_fleche += isLong ? -7 : 21;

                    // calcul hauteur
                    hauteur = 2 * largeur_listel + ((signSymbol.type != SignSymbol.SignSymbolType.NONE)
                            ? 3 * hauteur_composition : 2 * hauteur_composition);
                }
                case 2 -> {
                    longueur_pointe_fleche *= 2.45;
                    // calcul hauteur
                    hauteur = 3.5 * getHauteur_composition() + 2 * getLargeur_listel();
                }
                case 3 -> {
                    longueur_pointe_fleche *= 3.5;
                    // calcul hauteur
                    hauteur = 5 * getHauteur_composition() + getLargeur_listel() * 2;
                }
                case 4 -> {
                    JOptionPane.showMessageDialog(JOptionPane.getRootFrame(),
                            "le panneau à 4 ligne n'a pas encore été implémenté");
                    throw new NotImplementedException();}
                default -> throw new IllegalStateException();
            }
            longueur_pointe_fleche += isLong ? -7 : 21;

            //longueur_pointe_fleche = (hauteur/2 - largeur_listel/2) / Math.tan(37.5) - largeur_listel;

            longueur = longueur_sans_pointe + longueur_pointe_fleche;
        }
    }

    public int getNumero_gamme() {
        return numero_gamme;
    }

    public boolean isComputed() {
        return computed;
    }

    public double getLongueur() {
        return longueur;
    }

    public double getHauteur() {
        return hauteur;
    }

    public double getLongueur_pointe_fleche() {
        return longueur_pointe_fleche;
    }

    public Font.SignFont getFont() {
        return font;
    }

    public double getLargeur_listel() {
        return largeur_listel;
    }

    public int getLineNumber() {
        return lineNumber;
    }

    public int getHauteur_composition() {
        return hauteur_composition;
    }

    public DirectionalSignColor getColor() {
        return color;
    }

    public void setColor(DirectionalSignColor color) {
        this.color = color;
    }

    public void setColor(String color_string) throws SignException {
        for (DirectionalSignColor signColor:DirectionalSignColor.values()) {
            if (Objects.equals(color_string, signColor.toString())) {
                setColor(color);
                return;
            }
        }
        throw new SignException("couleur inexistante");
    }

    public static String getColorHex(DirectionalSignColor color) {
        return switch (color) {
            case WHITE -> "#FFFFFF";
            case GREEN -> "#008048";
            case BLUE -> "#0066CC";
            case YELLOW -> "#ffc800";
            case BROWN -> "#a05a00";
            case BLACK -> "#000000";
            case RED -> "#ff0000";
        };
    }

    public static DirectionalSignColor getSecondColor(DirectionalSignColor primaryColor) {
        return switch (primaryColor) {
            case WHITE, YELLOW -> DirectionalSignColor.BLACK;
            case BROWN, BLUE, GREEN, BLACK, RED -> DirectionalSignColor.WHITE;
        };
    }

    public DirectionalSignDirection getSignDirection() {
        return signDirection;
    }

    public void setSignDirection(DirectionalSignDirection signDirection) throws SignException {
        if ( (getSignID() == SignID.D21 || getSignID() == SignID.D29)
                && signDirection == DirectionalSignDirection.FRONT)
            throw new SignException("mauvaise direction de panneau");

        this.signDirection = signDirection;
    }

    public double getLongueur_supplementaire() {
        return longueur_supplementaire;
    }

    public void setWith_distance(boolean with_distance) {
        this.with_distance = with_distance;
    }

    public boolean isWith_distance() {
        return with_distance;
    }

    public SignSymbol getSignSymbol() {
        return signSymbol;
    }

    public double getLongueur_texte() {
        return longueur_texte;
    }

    public void setLongueur_supplementaire(double longueur_supplementaire) {
        this.longueur_supplementaire = longueur_supplementaire;
    }

    public ArrayList<Mention> getMentions() {
        return mentions;
    }

    public void setMentions(ArrayList<Mention> mentions) {
        this.mentions = mentions;
    }

    public boolean isWith_ideogram() {
        return with_ideogram;
    }

    public void setWith_ideogram(boolean with_ideogram) {
        this.with_ideogram = with_ideogram;
    }

    public enum DirectionalSignColor {
        WHITE, GREEN, BLUE, YELLOW, BROWN, BLACK, RED
    }

    public enum DirectionalSignDirection {
        LEFT, FRONT, RIGHT
    }

    @Override
    public String toString() {
        return mentions.get(0).toString();
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof DirectionalSign) return equals((DirectionalSign) o);
        else return false;
    }

    @Override
    public int hashCode() {
        int hash = 0;

        hash += font.hashCode();
        hash += longueur_supplementaire;
        hash += numero_gamme;
        hash += signDirection.hashCode();
        hash += signSymbol.hashCode();
        hash += computed ? 1 : 2;
        hash += with_distance ? 1 : 2;
        hash += with_symbol ? 1 : 2;
        hash += with_ideogram ? 1 : 2;

        return hash;
    }

    public boolean equals(DirectionalSign other_dir_sign) {
        boolean result = true;
        result &= (this.computed == other_dir_sign.computed);
        result &= (this.with_distance == other_dir_sign.with_distance);
        result &= (this.with_symbol == other_dir_sign.with_symbol);
        result &= (this.font == other_dir_sign.font);
        result &= (this.lineNumber == other_dir_sign.lineNumber);
        result &= (this.longueur_supplementaire == other_dir_sign.longueur_supplementaire);
        result &= (this.numero_gamme == other_dir_sign.numero_gamme);
        result &= (this.signDirection == other_dir_sign.signDirection);
        result &= (this.signSymbol == other_dir_sign.signSymbol);
        result &= (this.color == other_dir_sign.color);
        return result;
    }
}
