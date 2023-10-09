package fr.sarainfras.caillou15.app.sign;

import fr.sarainfras.caillou15.app.SignException;
import fr.sarainfras.caillou15.app.Utils;
import fr.sarainfras.caillou15.app.events.sign.SignInitiater;
import org.apache.commons.lang3.NotImplementedException;

import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.util.*;

import static fr.sarainfras.caillou15.app.sign.Font.SignFont.*;

public class DirectionalSign extends Sign {

    private String[] text;
    private Font.SignFont font = L1serre;
    private int lineNumber;
    private int numero_gamme = 0;
    boolean with_symbol = false;
    private DirectionalSignDirection signDirection;
    private double longueur_supplementaire = 0;
    private double[] distances_directions;
    private boolean with_distance = false;
    private final SignIdeogram signIdeogram;
    private final SignSymbol signSymbol;

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

    public DirectionalSign() {
        this("EXEMPLE");
    }

    public DirectionalSign(String text, SignID signID) {
        this(text);
        this.setSignID(signID);
    }

    public DirectionalSign(String text) {
        this(new String[]{text}, 1, new double[]{});
    }

    public DirectionalSign(String text, double distance) {
        this(new String[]{text}, 1, new double[]{distance});
    }

    public DirectionalSign(String[] texts, int lineNumber, double[] distaces){
        this.setType(SignType.DIRECTIONAL);
        this.text = texts;
        this.lineNumber = lineNumber;
        this.propertiesNameArray = new String[]{"text"};
        this.color = DirectionalSignColor.WHITE;
        this.distances_directions = distaces;
        this.signIdeogram = new SignIdeogram();
        this.signSymbol = new SignSymbol();
        try {
            setSignDirection(DirectionalSignDirection.RIGHT);
        } catch (SignException e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), e.getMessage());
        }
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

    public LinkedHashMap<String, JComponent> getPropertiesGUIComponents(SignInitiater signInitiater, boolean new_sign) {

        DirectionalSign this_sign = this;
        LinkedHashMap<String, JComponent> componentsArray = new LinkedHashMap<>();
        // texte
        JTextArea texte_textArea = new JTextArea("nom ville");
        if (!new_sign) texte_textArea.setText(Utils.monoLineText(this.getText()));
        texte_textArea.setMaximumSize(texte_textArea.getSize());
        texte_textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (this_sign.getFont()) {
                    case L1serre, L2serre ->
                            this_sign.text = (Character.isAlphabetic(e.getKeyChar()) ?
                                    texte_textArea.getText()+e.getKeyChar() : texte_textArea.getText()).toUpperCase(Locale.ROOT).split("\n");
                    case L4 ->
                            this_sign.text = (Character.isAlphabetic(e.getKeyChar()) ?
                                    texte_textArea.getText()+e.getKeyChar() : texte_textArea.getText()).split("\n");
                }

                signInitiater.signSet(this_sign, false);
            }
        });
        JScrollPane textScrollPane = new JScrollPane(texte_textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED , ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        textScrollPane.setMaximumSize(new Dimension(texte_textArea.getSize().width + 5, texte_textArea.getSize().height));
        textScrollPane.setPreferredSize(new Dimension(texte_textArea.getSize().width + 5, texte_textArea.getSize().height));
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
        distances_checkBox.addItemListener(e -> this_sign.setWith_distance(!this_sign.isWith_distance()));
        if (!new_sign) {
            distances_checkBox.setSelected(this.with_distance);
        }
        // distances indiquées
        JTextArea distances_textArea = new JTextArea("");
        distances_textArea.setMaximumSize(distances_textArea.getSize());
        distances_textArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (Character.isDigit(e.getKeyChar()) || Character.isAlphabetic(e.getKeyChar())) {
                    DirectionalSign this_dir_sign = this_sign;
                    String[] stringArray = (Character.isAlphabetic(e.getKeyChar()) ||
                            Character.isDigit(e.getKeyChar())
                            ? distances_textArea.getText()+e.getKeyChar() : distances_textArea.getText()).split("\n");
                    int nb = 0;
                    double[] distances = new double[0];
                    if (stringArray.length != 0) {
                        nb = stringArray.length;
                        distances = new double[nb];
                        for (int i = 0; i < nb; i ++) {
                            try {
                                distances[i] = Double.parseDouble(stringArray[i]);
                            } catch (NumberFormatException numberFormatException) {
                                numberFormatException.printStackTrace();
                                JOptionPane.showMessageDialog(JOptionPane.getRootFrame(), "les distances ne doivent contenir que des chiffres");
                            }
                        }
                    }

                    this_dir_sign.distances_directions = distances;

                    if (nb == this_dir_sign.getLineNumber() && this_dir_sign.isWith_distance()) signInitiater.signSet(this_dir_sign, false);
                    else if (this_dir_sign.isWith_distance()) JOptionPane.showMessageDialog(JOptionPane.getRootFrame(),
                            "le nombre de distances ne corrspond pas au nombre de lignes");
                }

            }
        });
        if (!new_sign && with_distance) {
            for (int i = 0; i < distances_directions.length; i++) {
                if (i == distances_directions.length - 1) {
                    distances_textArea.append(String.valueOf(distances_directions[i]));
                }
                distances_textArea.append(String.valueOf(distances_directions[i]) + "\n");
            }
        }
        JScrollPane distancesScrollPane = new JScrollPane(distances_textArea, ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED , ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        distancesScrollPane.setMaximumSize(new Dimension(distances_textArea.getSize().width + 5, distances_textArea.getSize().height));
        distancesScrollPane.setPreferredSize(new Dimension(distances_textArea.getSize().width + 5, distances_textArea.getSize().height));
        // ideogramme
        String[] signIdeogramArray = Utils.getNames(SignIdeogram.SignIdeogramType.class);
        JComboBox<String> ideogram_jComboBox = new JComboBox<>(signIdeogramArray);
        ideogram_jComboBox.addActionListener(e -> {
            getSignIdeogram().type = SignIdeogram.SignIdeogramType.values()[ideogram_jComboBox.getSelectedIndex()];
            signInitiater.signSet(this_sign, false);
        });
        // symbole
        String[] signSymbolArray = Utils.getNames(SignSymbol.SignSymbolType.class);
        JComboBox<String> symbol_jComboBox = new JComboBox<>(signSymbolArray);
        symbol_jComboBox.addActionListener(e -> {
            getSignSymbol().type = SignSymbol.SignSymbolType.values()[symbol_jComboBox.getSelectedIndex()];
            signInitiater.signSet(this_sign, false);
        });
        // retour
        componentsArray.put("texte", textScrollPane);
        componentsArray.put("couleur", color_jComboBox);
        componentsArray.put("direction", direction_jComboBox);
        componentsArray.put("long. supp.", long_supp_spinner);
        componentsArray.put("distances ?", distances_checkBox);
        componentsArray.put("distances", distancesScrollPane);
        componentsArray.put("idéogramme", ideogram_jComboBox);
        componentsArray.put("symbole", symbol_jComboBox);
        return componentsArray;
    }

    public void computeLengths() throws NotImplementedException {
        switch (getSignID()) {
            case D21 -> computeLengths_D21();
            case D29 -> throw new NotImplementedException();
            default -> throw new IllegalStateException("ID de panneau annormal");
        }
        width = longueur;
        computed = true;
    }

    public void computeLengths_D21() throws NotImplementedException {
        lineNumber = text.length;
        if (lineNumber == 0 || lineNumber > 4)
            JOptionPane.showMessageDialog(null, "le nombre de ligne est incorrect ou supérieur à 4");
        else {
            boolean isLong;
            hauteur_base = gammes[getNumero_gamme()];
            longueur_texte = 0;
            longueur = 0;
            longueur_pointe_fleche = 0;
            hauteur = 0;

            switch (getColor()) {
                case BLUE, GREEN, BROWN, BLACK, RED -> hauteur_composition = gammes[numero_gamme+1];
                case WHITE, YELLOW -> hauteur_composition = gammes[numero_gamme];
            }
            for (int i = 0; i < text.length; i++) {
                double longueur_ligne = Font.getTextLength(text[i], font, numero_gamme);
                if (longueur_ligne > longueur_texte) longueur_texte = longueur_ligne;
            }

            longueur_texte = longueur_texte + longueur_supplementaire;
            if (isWith_distance()) {
                double longueur_distance = 0;
                for (int i = 0; i < getDistances_directions().length; i++) {
                    double longueur_ligne = Font.getTextLength(String.valueOf(getDistances_directions()[i]),
                            getFont(), getNumero_gamme());
                    if (longueur_ligne > longueur_distance) longueur_distance = longueur_ligne;
                }
                longueur_texte = longueur_texte + longueur_distance;
            }

            double longueur_sans_pointe = 0.0;

            if (getSignSymbol().type != SignSymbol.SignSymbolType.NONE) {
                longueur_sans_pointe += 2.5*hauteur_composition + hauteur_composition;
            } else
            if (getSignIdeogram().type != SignIdeogram.SignIdeogramType.NONE) {
                longueur_sans_pointe += 1.5*hauteur_composition + hauteur_composition/2.0;
            }

            isLong = longueur_sans_pointe + 1.4 * getHauteur_composition() - 7 > 2500;

            largeur_listel = isLong ? 40 : 20;

            longueur_sans_pointe += longueur_texte + hauteur_composition + largeur_listel;

            longueur_pointe_fleche = hauteur_composition;

            switch (getLineNumber()) {
                case 1 -> {
                    longueur_pointe_fleche *= (signIdeogram.type != SignIdeogram.SignIdeogramType.NONE) ? 2.1 : 1.4;
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

    public String[] getText() {
        return text;
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

    public double[] getDistances_directions() {
        return distances_directions;
    }

    public void setWith_distance(boolean with_distance) {
        this.with_distance = with_distance;
    }

    public boolean isWith_distance() {
        return with_distance;
    }

    public SignIdeogram getSignIdeogram() {
        return signIdeogram;
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

    public enum DirectionalSignColor {
        WHITE, GREEN, BLUE, YELLOW, BROWN, BLACK, RED
    }

    public enum DirectionalSignDirection {
        LEFT, FRONT, RIGHT
    }

    @Override
    public String toString() {
        return text[0];
    }

    @Override
    public boolean equals(Object o) {

        if (o instanceof DirectionalSign) return equals((DirectionalSign) o);
        else return false;
    }

    @Override
    public int hashCode() {
        int hash = 0;

        if (computed) hash += 1;
        hash += Arrays.hashCode(distances_directions);
        hash += font.hashCode();
        hash += longueur_supplementaire;
        hash += numero_gamme;
        hash += signDirection.hashCode();
        hash += signIdeogram.hashCode();
        hash += signSymbol.hashCode();
        hash += Arrays.hashCode(text);
        hash += with_distance ? 1 : 2;
        hash += with_symbol ? 1 : 2;

        return hash;
    }

    public boolean equals(DirectionalSign other_dir_sign) {
        boolean result = true;
        result &= this.computed;
        result &= this.with_distance;
        result &= (this.distances_directions == other_dir_sign.distances_directions);
        result &= this.with_symbol;
        result &= (this.font == other_dir_sign.font);
        result &= (this.lineNumber == other_dir_sign.lineNumber);
        result &= (this.longueur_supplementaire == other_dir_sign.longueur_supplementaire);
        result &= (this.numero_gamme == other_dir_sign.numero_gamme);
        result &= (this.signDirection == other_dir_sign.signDirection);
        result &= (this.signIdeogram == other_dir_sign.signIdeogram);
        result &= (this.signSymbol == other_dir_sign.signSymbol);
        result &= Arrays.equals(this.text, other_dir_sign.text);
        result &= (this.color == other_dir_sign.color);
        return result;
    }
}
