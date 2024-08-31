package fr.sarainfras.caillou15.app.render;

import fr.sarainfras.caillou15.app.AppWindow;
import fr.sarainfras.caillou15.app.EditorUI;
import fr.sarainfras.caillou15.app.SignException;
import fr.sarainfras.caillou15.app.sign.*;
import fr.sarainfras.caillou15.app.sign.Font;
import org.apache.commons.lang3.NotImplementedException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DirectionalSignRenderer extends Renderer{
    DirectionalSign dir_sign;

    public DirectionalSignRenderer(AppWindow frame) {
        super(frame);
    }

    public DirectionalSignRenderer(DirectionalSign dir_sign, AppWindow frame) {
        super(dir_sign, frame);
        this.dir_sign = dir_sign;
    }

    public DirectionalSignRenderer(DirectionalSign sign, AppWindow frame, EditorUI editorUI) {
        this(sign, frame);
        this.editorUI = editorUI;
    }

    public DirectionalSignRenderer(DirectionalSign sign, AppWindow frame, EditorUI editorUI, Document svg_doc) {
        this(sign, frame, editorUI);
        this.svg_document = svg_doc;
    }

    @Override
    public void render(Sign sign) throws SignException {
        if (sign == null && svg_document == null) {
            setSvg_document(super.dom_impl.createDocument(svgNS, "svg", null));
            return;
        }

        if (sign == null) super.render(null);

        if (!(sign instanceof DirectionalSign))
            throw new SignException("la classe specialise de rendu de panneau de direction reçois un panneau d'un autre type");
        super.render(sign);
    }

    public Element render(DirectionalSign dir_sign, Document svg_doc) {
        Element sign_e = null;
        switch (dir_sign.getSignDirection()) {
            case RIGHT -> sign_e = renderRight(dir_sign, svg_doc);
            case LEFT -> sign_e = renderLeft(dir_sign, svg_doc);
            default -> throw new IllegalStateException("render(): direction invalide");
        }
        return sign_e;
    }

    @Override
    public Element renderPointeFleche(DirectionalSign dir_sign) {
        Element pointe_groupe = svg_document.createElementNS(svgNS, "g");
        Element pointe_de_fleche = svg_document.createElementNS(svgNS, "path");
        Element pointe_fond = svg_document.createElementNS(svgNS, "path");
        pointe_groupe.appendChild(pointe_de_fleche);
        pointe_groupe.appendChild(pointe_fond);

        double fond_pointe_x;
        if (dir_sign.getSignSymbol().type != SignSymbol.SignSymbolType.NONE && dir_sign.getLineNumber() == 1)
            fond_pointe_x = Math.tan(Math.toRadians(40))*dir_sign.getHauteur_composition()*1.5;
        else fond_pointe_x = Math.tan(Math.toRadians(40))*dir_sign.getHauteur_composition()*dir_sign.getLineNumber();

        double longueur_pointe_virtuelle = Math.tan(Math.toRadians(52.5))*(dir_sign.getHauteur()/2);
        double longueur_pointe_fond_pointe_listel_x = Math.tan(Math.toRadians(42.5))*longueur_pointe_virtuelle - fond_pointe_x;
        double rayon_arrondi_pointe = longueur_pointe_fond_pointe_listel_x *
                Math.sin(Math.toRadians(92.5)) / Math.sin(Math.toRadians(50+92.5));

        double point_rayon_arrondi_pointe_x = 0;
        double point_rayon_arrondi_pointe_y = Math.cos(Math.toRadians(50)) * rayon_arrondi_pointe*0.5;

        switch (dir_sign.getLineNumber()) {
            case 1 -> point_rayon_arrondi_pointe_x = Math.cos(Math.toRadians(50)) * rayon_arrondi_pointe * 0.5;
            case 2 -> {
                point_rayon_arrondi_pointe_x = Math.cos(Math.toRadians(50)) * rayon_arrondi_pointe * 1;
                longueur_pointe_virtuelle *= 1.1;
            }
            case 3 -> {
                point_rayon_arrondi_pointe_x = Math.cos(Math.toRadians(50)) * rayon_arrondi_pointe * 1.2;
                longueur_pointe_virtuelle *= 1.1;
            }
            case 4 -> throw new NotImplementedException("4e ligne non implémentée");
            default -> throw new IllegalStateException("Mauvais nombre de lignes dans renderPointeFleche(): " + dir_sign.getLineNumber());
        }

        pointe_de_fleche.setAttributeNS(null, "style", "fill:"+
                DirectionalSign.getColorHex(DirectionalSign.getSecondColor(dir_sign.getColor())));

        switch (dir_sign.getSignDirection()) {

            case RIGHT -> {
                pointe_de_fleche.setAttributeNS(null, "d", "M " +
                        (dir_sign.getLongueur() - dir_sign.getLongueur_pointe_fleche()) + ",0" + // haut - bord
                        " " + (dir_sign.getLongueur() - dir_sign.getLongueur_pointe_fleche() + fond_pointe_x // pointe-haut
                        + point_rayon_arrondi_pointe_x) +
                        "," + (dir_sign.getHauteur()/2 - point_rayon_arrondi_pointe_y) +
                        " C " + (dir_sign.getLongueur()-dir_sign.getLongueur_pointe_fleche()+
                        longueur_pointe_virtuelle) +// poignée pointe-haut
                        ", " + (dir_sign.getHauteur()/2) +
                        " " + (dir_sign.getLongueur() - dir_sign.getLongueur_pointe_fleche() + fond_pointe_x // pointe-bas
                        + point_rayon_arrondi_pointe_x) +
                        "," + (dir_sign.getHauteur()/2 + point_rayon_arrondi_pointe_y) +
                        " " + (dir_sign.getLongueur() - dir_sign.getLongueur_pointe_fleche() + fond_pointe_x // pointe-bas
                        + point_rayon_arrondi_pointe_x) +
                        "," + (dir_sign.getHauteur()/2 + point_rayon_arrondi_pointe_y) +
                        " L " + (dir_sign.getLongueur() - dir_sign.getLongueur_pointe_fleche()) + // bas - bord
                        "," + (dir_sign.getHauteur() ) +
                        " " + (dir_sign.getLongueur() - dir_sign.getLongueur_pointe_fleche()) + // bas - interieur
                        "," + (dir_sign.getHauteur() - dir_sign.getLargeur_listel()) +
                        " " + (dir_sign.getLongueur() - dir_sign.getLongueur_pointe_fleche() + fond_pointe_x) + // pointe-interieur
                        "," + (dir_sign.getHauteur()/2 ) +
                        " " + (dir_sign.getLongueur() - dir_sign.getLongueur_pointe_fleche()) + // haut - interieur
                        "," + dir_sign.getLargeur_listel() +
                        "Z" ); // haut - bord

                //fond de la pointe

                pointe_fond.setAttributeNS(null, "style", "fill:"+
                        DirectionalSign.getColorHex(dir_sign.getColor()));
                pointe_fond.setAttributeNS(null, "d",
                        "M " + (dir_sign.getLongueur() - dir_sign.getLongueur_pointe_fleche()) + // bas - interieur
                                "," + (dir_sign.getHauteur() - dir_sign.getLargeur_listel()) +
                                " " + (dir_sign.getLongueur() - dir_sign.getLongueur_pointe_fleche() + fond_pointe_x) + // pointe-interieur
                                "," + (dir_sign.getHauteur()/2.0 ) +
                                " " + (dir_sign.getLongueur() - dir_sign.getLongueur_pointe_fleche()) + // haut - interieur
                                "," + dir_sign.getLargeur_listel() +
                                "Z" // haut - interieur
                );
            }
            case LEFT -> {
                pointe_de_fleche.setAttributeNS(null, "d", "M " +
                        (dir_sign.getLongueur_pointe_fleche() )+ ",0" + // haut - bord
                        " " + (dir_sign.getLongueur_pointe_fleche() - fond_pointe_x // pointe-haut
                        - point_rayon_arrondi_pointe_x) +
                        "," + (dir_sign.getHauteur()/2 - point_rayon_arrondi_pointe_y) +
                        " C " + (dir_sign.getLongueur_pointe_fleche() -
                        longueur_pointe_virtuelle) +// poignée pointe-haut
                        ", " + (dir_sign.getHauteur()/2 ) +
                        " " + (dir_sign.getLongueur_pointe_fleche() - fond_pointe_x // pointe-bas
                        - point_rayon_arrondi_pointe_x) +
                        "," + (dir_sign.getHauteur()/2 + point_rayon_arrondi_pointe_y) +
                        " " + (dir_sign.getLongueur_pointe_fleche() - fond_pointe_x // pointe-bas
                        - point_rayon_arrondi_pointe_x) +
                        "," + (dir_sign.getHauteur()/2 + point_rayon_arrondi_pointe_y) +
                        " L " + (dir_sign.getLongueur_pointe_fleche()) + // bas - bord
                        "," + (dir_sign.getHauteur() ) +
                        " " + (dir_sign.getLongueur_pointe_fleche()) + // bas - interieur
                        "," + (dir_sign.getHauteur() - dir_sign.getLargeur_listel()) +
                        " " + (dir_sign.getLongueur_pointe_fleche() - fond_pointe_x) + // pointe-interieur
                        "," + (dir_sign.getHauteur()/2 ) +
                        " " + (dir_sign.getLongueur_pointe_fleche()) + // haut - interieur
                        "," + dir_sign.getLargeur_listel() +
                        "Z" ); // haut - bord

                //fond de la pointe

                pointe_fond.setAttributeNS(null, "style", "fill:"+
                        DirectionalSign.getColorHex(dir_sign.getColor()));
                pointe_fond.setAttributeNS(null, "d",
                        "M " + (dir_sign.getLongueur_pointe_fleche()) + // bas - interieur
                                "," + (dir_sign.getHauteur() - dir_sign.getLargeur_listel()) +
                                " " + (dir_sign.getLongueur_pointe_fleche() - fond_pointe_x) + // pointe-interieur
                                "," + (dir_sign.getHauteur()/2) +
                                " " + (dir_sign.getLongueur_pointe_fleche()) + // haut - interieur
                                "," + dir_sign.getLargeur_listel() +
                                "Z" // haut - interieur
                );
            }
            case FRONT -> throw new IllegalStateException("tentative de dessiner une pointe de flèche du côté FRONT");
        }

        return pointe_groupe;
    }

    @Override
    public void signSet(Sign sign, boolean firstRender) {
        // Cette classe n'a pas besoin de réagir à cet évènement.
    }
}
