package fr.sarainfras.caillou15.app.render;

import fr.sarainfras.caillou15.app.AppWindow;
import fr.sarainfras.caillou15.app.Constants;
import fr.sarainfras.caillou15.app.EditorUI;
import fr.sarainfras.caillou15.app.SignException;
import fr.sarainfras.caillou15.app.events.sign.SignListener;
import fr.sarainfras.caillou15.app.sign.*;
import fr.sarainfras.caillou15.app.sign.Font;
import fr.sarainfras.caillou15.app.signgroup.Cartouche;
import fr.sarainfras.caillou15.app.signgroup.DirectionalSignGroup;
import org.apache.batik.anim.dom.SAXSVGDocumentFactory;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.JSVGComponent;
import org.apache.batik.swing.svg.SVGDocumentLoaderAdapter;
import org.apache.batik.swing.svg.SVGDocumentLoaderEvent;
import org.apache.batik.swing.svg.SVGUserAgentAdapter;
import org.apache.batik.util.XMLResourceDescriptor;
import org.apache.commons.lang3.NotImplementedException;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.io.IOException;
import java.net.URL;

public class Renderer extends JPanel implements SignListener {

    private Sign sign;
    protected JSVGCanvas svgCanvas;
    protected Document svg_document;
    private final AppWindow mainFrame;
    protected EditorUI editorUI = null;

    protected boolean renderedSign = false;
    public boolean firstRender = true;

    DOMImplementation dom_impl = SVGDOMImplementation.getDOMImplementation();
    String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;

    public Renderer(AppWindow frame) {
        mainFrame = frame;
        init();
    }

    public Renderer(Sign sign, AppWindow frame) {
        this(frame);
        this.sign = sign;
    }

    public Renderer(Sign sign, AppWindow frame, EditorUI editorUI) {
        this(sign, frame);
        this.editorUI = editorUI;
    }

    protected void init() {
        this.setBackground(Constants.BACKGROUND_GREEN);
        this.svgCanvas = new JSVGCanvas(new SVGUserAgentAdapter(), true, false);

        this.mainFrame.getSignInitiater().addListener(this);

        AffineTransform af = new AffineTransform();
        af.scale(0.25, 0.25);
        svgCanvas.setRenderingTransform(af);

        svgCanvas.setRecenterOnResize(true);
        if (getSvg_document() == null) setSvg_document(dom_impl.createDocument(svgNS, "svg",  null));
    }

    public void render(Sign sign) throws SignException {
        switch (sign.getType()) {
            case PRIORITY -> { this.add(this.svgCanvas);

                svgCanvas.addSVGDocumentLoaderListener(new SVGDocumentLoaderAdapter() {
                    @Override
                    public void documentLoadingCompleted(SVGDocumentLoaderEvent e) {
                        super.documentLoadingCompleted(e);
                        svgCanvas.setBackground(getBackground());
                        //svgCanvas.setDisableInteractions(true);
                        svgCanvas.setPreferredSize(svgCanvas.getSize());
                        svgCanvas.setMaximumSize(svgCanvas.getSize());
                        Dimension d = svgCanvas.getSize();
                        svgCanvas.getSVGDocument().getDocumentElement().setAttribute("width", String.valueOf(d.width));
                        svgCanvas.getSVGDocument().getDocumentElement().setAttribute("height", String.valueOf(d.height));

                    }

                    @Override
                    public void documentLoadingFailed(SVGDocumentLoaderEvent e) {
                        JOptionPane.showMessageDialog(mainFrame, "echec du chargement : " + e.toString());
                    }
                });

                URL url = Sign.class.getResource("France_road_sign_" + sign.getSignID() +".svg");
                this.svgCanvas.setURI(url.toString());

                Document doc = null;

                try {
                    String parser = XMLResourceDescriptor.getXMLParserClassName();
                    SAXSVGDocumentFactory f = new SAXSVGDocumentFactory(parser);
                    String uri = url.toString();
                    doc = f.createDocument(uri);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }

                svgCanvas.setDocument(doc);

                repaint();
                revalidate();
            }
            case DIRECTIONAL -> {
                DirectionalSign dir_sign = (DirectionalSign) sign;
                SignUI signUI = (SignUI) getEditorUI();

                dir_sign.computeLengths();

                this.setLayout(new GridBagLayout());
                GridBagConstraints constraints = new GridBagConstraints();
                constraints.fill = GridBagConstraints.BOTH;
                constraints.weightx = 1.0;
                constraints.weighty = 1.0;
                this.add(this.svgCanvas, constraints);

                svgCanvas.setDocumentState(JSVGComponent.ALWAYS_DYNAMIC);
                if (getSvg_document() == null) setSvg_document(dom_impl.createDocument(svgNS, "svg",  null));
                else {
                    while (getSvg_document().getDocumentElement().hasChildNodes()) {
                        getSvg_document().getDocumentElement().removeChild(getSvg_document().getDocumentElement().getFirstChild());
                    }
                }
                svgCanvas.setDocument(getSvg_document());
                svgCanvas.setBackground(Constants.BACKGROUND_GREEN);
                svgCanvas.setEnableZoomInteractor(true);
                svgCanvas.setEnableImageZoomInteractor(true);

                Element svgRoot = getSvg_document().getDocumentElement();
                svgRoot.setAttribute("width", String.valueOf(dir_sign.getLongueur()));
                svgRoot.setAttribute("height", String.valueOf(dir_sign.getHauteur()));
                Element sign_e = signUI.directionalSignRenderer.render(dir_sign, svg_document);
                svg_document.getDocumentElement().appendChild(sign_e);
                svgCanvas.setDocument(getSvg_document());
                revalidate();
                repaint();
            }
        }
    }

    public Element renderDirectionalSign(DirectionalSign dir_sign) {
        Element sign_e = getSvg_document().createElementNS(svgNS, "g");

        switch (dir_sign.getSignDirection()) {
            case RIGHT -> sign_e = renderRight(dir_sign, svg_document);
            case LEFT -> sign_e = renderLeft(dir_sign, svg_document);
        }
        return sign_e;
    }

    public Element renderRight(DirectionalSign dir_sign, Document svg_doc) {
        Element sign_e = svg_doc.createElementNS(svgNS, "g");
        this.setSvg_document(svg_doc);

        Element listel_haut = renderRectangle(dir_sign.getLargeur_listel()*1.5, 0,
                (dir_sign.getLongueur() - dir_sign.getLongueur_pointe_fleche() - dir_sign.getLargeur_listel()*1.5),
                dir_sign.getLargeur_listel(), DirectionalSign.getSecondColor(dir_sign.getColor()));
        Element listel_bas = renderRectangle(dir_sign.getLargeur_listel()*1.5,
                dir_sign.getHauteur() - dir_sign.getLargeur_listel(),
                (dir_sign.getLongueur() - dir_sign.getLongueur_pointe_fleche() - dir_sign.getLargeur_listel()*1.5),
                dir_sign.getLargeur_listel(), DirectionalSign.getSecondColor(dir_sign.getColor()));
        Element listel_gauche = renderRectangle(0, dir_sign.getLargeur_listel()*1.5,
                dir_sign.getLargeur_listel()*1.5, dir_sign.getHauteur() - dir_sign.getLargeur_listel()*3,
                DirectionalSign.getSecondColor(dir_sign.getColor()));
        Element rond_coin_haut = renderCircle(dir_sign.getLargeur_listel()*1.5, dir_sign.getLargeur_listel()*1.5,
                dir_sign.getLargeur_listel()*1.5, DirectionalSign.getSecondColor(dir_sign.getColor()));
        Element rond_coin_bas = renderCircle(dir_sign.getLargeur_listel()*1.5,
                dir_sign.getHauteur()-dir_sign.getLargeur_listel()*1.5, dir_sign.getLargeur_listel()*1.5,
                DirectionalSign.getSecondColor(dir_sign.getColor()));
        Element pointe_de_fleche = renderPointeFleche(dir_sign);
        Element texte = getSvg_document().createElementNS(svgNS, "g");
        Element fond_blanc = renderRectangle(dir_sign.getLargeur_listel()*1.5, dir_sign.getLargeur_listel(),
                dir_sign.getLongueur()-dir_sign.getLongueur_pointe_fleche()-dir_sign.getLargeur_listel(),
                dir_sign.getHauteur()-2*dir_sign.getLargeur_listel(), dir_sign.getColor());
        Element fond_blanc_bord_bout = renderRectangle(dir_sign.getLargeur_listel(), dir_sign.getLargeur_listel()*1.5,
                dir_sign.getLargeur_listel()*1.5, dir_sign.getHauteur()-3*dir_sign.getLargeur_listel(), dir_sign.getColor());
        Element fond_bland_bord_arrondi_haut = renderCircle(
                dir_sign.getLargeur_listel()*1.5, dir_sign.getLargeur_listel()*1.5,
                dir_sign.getLargeur_listel()/2, dir_sign.getColor());
        Element fond_bland_bord_arrondi_bas = renderCircle(
                dir_sign.getLargeur_listel()*1.5, dir_sign.getHauteur()-dir_sign.getLargeur_listel()*1.5,
                dir_sign.getLargeur_listel()/2, dir_sign.getColor());
        fond_blanc.setAttributeNS(null, "style",
                "fill:"+DirectionalSign.getColorHex(dir_sign.getColor()));

        sign_e.appendChild(listel_haut);
        sign_e.appendChild(listel_bas);
        sign_e.appendChild(listel_gauche);
        sign_e.appendChild(rond_coin_haut);
        sign_e.appendChild(rond_coin_bas);
        sign_e.appendChild(fond_blanc);
        sign_e.appendChild(fond_blanc_bord_bout);
        sign_e.appendChild(fond_bland_bord_arrondi_haut);
        sign_e.appendChild(fond_bland_bord_arrondi_bas);
        sign_e.appendChild(texte);
        sign_e.appendChild(pointe_de_fleche);

        int gamme_actuelle = (dir_sign.getFont() == Font.SignFont.L2serre)
                ?DirectionalSign.gammes[dir_sign.getNumero_gamme() + 1]
                :DirectionalSign.gammes[dir_sign.getNumero_gamme()];
        // ideogramme si utilisé - @TODO à supprimer (aussi dans renderLeft)
        /*if (dir_sign.getSignIdeogram().type != SignIdeogram.SignIdeogramType.NONE)
            sign_e.appendChild( renderEmplacementIdeogeogram(dir_sign) );*/
        // symbole si utilisé
        if (dir_sign.getSignSymbol().type == SignSymbol.SignSymbolType.INDICATION) {
            double z = dir_sign.getLargeur_listel() + 0.5*gamme_actuelle;
            double y = dir_sign.getHauteur()/2.0 - SignSymbol.getSize(gamme_actuelle)/2.0;
            //carré blanc
            sign_e.appendChild(renderRectangle(z, y, SignSymbol.getSize(gamme_actuelle),
                    SignSymbol.getSize(gamme_actuelle), DirectionalSign.DirectionalSignColor.WHITE));
            //carré bleu
            z += 0.06*gamme_actuelle;
            y += 0.06*gamme_actuelle;
            sign_e.appendChild(renderRectangle(z, y, SignSymbol.getBlueSquareSize(gamme_actuelle),
                    SignSymbol.getBlueSquareSize(gamme_actuelle),
                    DirectionalSign.DirectionalSignColor.BLUE));
        } else
        if (dir_sign.getSignSymbol().type == SignSymbol.SignSymbolType.INTERDICTION) {
            double z = dir_sign.getLargeur_listel() + 0.5*gamme_actuelle;
            double y = dir_sign.getHauteur()/2.0;
            //rond bord blanc
            sign_e.appendChild(renderCircle(z +
                            SignSymbol.getSize(gamme_actuelle)/2.0, y,
                    SignSymbol.getSize(gamme_actuelle)/2.0, DirectionalSign.DirectionalSignColor.WHITE));
            //rond rouge
            z += 0.06*gamme_actuelle;
            sign_e.appendChild(renderCircle(z +
                            SignSymbol.getRedCircleSize(gamme_actuelle)/2.0,
                    y, SignSymbol.getRedCircleSize(gamme_actuelle)/2.0,
                    DirectionalSign.DirectionalSignColor.RED));
            //rond blanc interieur
            z += 0.25*gamme_actuelle;
            sign_e.appendChild(renderCircle(z +
                            SignSymbol.getWhiteInnerCircleSize(gamme_actuelle)/2.0,
                    y, SignSymbol.getWhiteInnerCircleSize(gamme_actuelle)/2.0,
                    DirectionalSign.DirectionalSignColor.WHITE));
        }

        // texte et symbole par mention
        double decalage;

        for (int j = 0; j < dir_sign.getMentions().size(); j++) {
            //rendu par mention
            Font.SignFont font =
                    dir_sign.getMentions().get(j).font == Font.SignFont.L4serre ?
                            Font.SignFont.L4serre : dir_sign.getFont();
            //calcul du décalage
            decalage = gamme_actuelle/2.0 + dir_sign.getLargeur_listel();
            if (dir_sign.isWith_ideogram())
                decalage += SignIdeogram.getSize(dir_sign.getHauteur_composition()) + gamme_actuelle/2.0;
            if (dir_sign.getSignSymbol().type != SignSymbol.SignSymbolType.NONE)
                decalage += SignSymbol.getSize(dir_sign.getHauteur_composition()) + gamme_actuelle;
            //nom
            if (dir_sign.getSignSymbol().type != SignSymbol.SignSymbolType.NONE && dir_sign.getLineNumber() == 1)
                sign_e.appendChild( renderText(decalage,  dir_sign.getLargeur_listel() + gamme_actuelle*2,
                        dir_sign.getNumero_gamme(), dir_sign.getMentions().get(j).toString(), font,
                        DirectionalSign.getSecondColor(dir_sign.getColor()), true));
            else sign_e.appendChild( renderText(decalage,  dir_sign.getLargeur_listel() + gamme_actuelle/2.0*(j+1)
                            + gamme_actuelle*(j+1), dir_sign.getNumero_gamme(), dir_sign.getMentions().get(j).toString(),
                    font, DirectionalSign.getSecondColor(dir_sign.getColor()), true)
            );
            // distance
            if (dir_sign.isWith_distance()) {
                String digit_string;
                if (dir_sign.getMentions().get(j).distance ==
                        (int) dir_sign.getMentions().get(j).distance ) digit_string =
                        String.valueOf((int) dir_sign.getMentions().get(j).distance);
                else digit_string = String.valueOf(dir_sign.getMentions().get(j).distance);

                decalage = dir_sign.getLongueur()-dir_sign.getLongueur_pointe_fleche() -
                        Font.getTextLength(digit_string,
                                dir_sign.getFont(), dir_sign.getNumero_gamme(), true)-gamme_actuelle/2.0;

                if (dir_sign.getSignSymbol().type != SignSymbol.SignSymbolType.NONE && dir_sign.getMentions().size() == 1) {
                    sign_e.appendChild(
                            renderText(decalage,
                                    dir_sign.getLargeur_listel() + gamme_actuelle*2,
                                    dir_sign.getNumero_gamme(), digit_string, font,
                                    DirectionalSign.getSecondColor(dir_sign.getColor()),
                                    true)
                    );
                } else {
                    sign_e.appendChild(
                            renderText(decalage,
                                    dir_sign.getLargeur_listel() + gamme_actuelle/2.0*(j+1) + gamme_actuelle*(j+1),
                                    dir_sign.getNumero_gamme(), digit_string, font,
                                    DirectionalSign.getSecondColor(dir_sign.getColor()),
                                    true)
                    );
                }


            }
        }

        return sign_e;
    }

    public Element renderLeft(DirectionalSign dir_sign, Document svg_doc) {
        Element sign_e = svg_doc.createElementNS(svgNS, "g");
        this.setSvg_document(svg_doc);

        Element listel_haut = renderRectangle(dir_sign.getLongueur_pointe_fleche(), 0,
                (dir_sign.getLongueur() - dir_sign.getLongueur_pointe_fleche() - dir_sign.getLargeur_listel()),
                dir_sign.getLargeur_listel(), DirectionalSign.getSecondColor(dir_sign.getColor()));
        Element listel_bas = renderRectangle(dir_sign.getLongueur_pointe_fleche(),
                dir_sign.getHauteur() - dir_sign.getLargeur_listel(),
                (dir_sign.getLongueur() - dir_sign.getLongueur_pointe_fleche() - dir_sign.getLargeur_listel()),
                dir_sign.getLargeur_listel(), DirectionalSign.getSecondColor(dir_sign.getColor()));
        Element listel_gauche = renderRectangle(
                dir_sign.getLongueur()-dir_sign.getLargeur_listel(), dir_sign.getLargeur_listel(),
                dir_sign.getLargeur_listel(), dir_sign.getHauteur() - dir_sign.getLargeur_listel()*2,
                DirectionalSign.getSecondColor(dir_sign.getColor()));
        Element rond_coin_haut = renderCircle(
                dir_sign.getLongueur() - dir_sign.getLargeur_listel(), dir_sign.getLargeur_listel(),
                dir_sign.getLargeur_listel(), DirectionalSign.getSecondColor(dir_sign.getColor()));
        Element rond_coin_bas = renderCircle(dir_sign.getLongueur() - dir_sign.getLargeur_listel(),
                dir_sign.getHauteur()-dir_sign.getLargeur_listel(), dir_sign.getLargeur_listel(),
                DirectionalSign.getSecondColor(dir_sign.getColor()));
        Element pointe_de_fleche = renderPointeFleche(dir_sign);
        Element texte = svg_document.createElementNS(svgNS, "g");
        Element fond_blanc = renderRectangle(dir_sign.getLongueur_pointe_fleche(), dir_sign.getLargeur_listel(),
                dir_sign.getLongueur()-dir_sign.getLongueur_pointe_fleche()-dir_sign.getLargeur_listel(),
                dir_sign.getHauteur()-2*dir_sign.getLargeur_listel(), dir_sign.getColor());
        fond_blanc.setAttributeNS(null, "style",
                "fill:"+DirectionalSign.getColorHex(dir_sign.getColor()));

        sign_e.appendChild(listel_haut);
        sign_e.appendChild(listel_bas);
        sign_e.appendChild(listel_gauche);
        sign_e.appendChild(rond_coin_haut);
        sign_e.appendChild(rond_coin_bas);
        sign_e.appendChild(fond_blanc);
        sign_e.appendChild(texte);
        sign_e.appendChild(pointe_de_fleche);

        int gamme_actuelle;
        if (dir_sign.getFont() == Font.SignFont.L2serre)
            gamme_actuelle = DirectionalSign.gammes[dir_sign.getNumero_gamme() + 1];
        else gamme_actuelle = DirectionalSign.gammes[dir_sign.getNumero_gamme()];
        // symbole si utilisé
        if (dir_sign.getSignSymbol().type == SignSymbol.SignSymbolType.INDICATION) {
            /*double longueur_texte = 0;
            for (int i = 0; i < dir_sign.getText().length; i++) {
                double longueur_ligne = Font.getTextLength(dir_sign.getText()[i],
                        dir_sign.getFont(), dir_sign.getNumero_gamme());
                if (longueur_ligne > longueur_texte) longueur_texte = longueur_ligne;
            }*/
            double x = dir_sign.getLongueur() - dir_sign.getLargeur_listel()
                    - SignSymbol.getSize(gamme_actuelle) - 0.5*gamme_actuelle;
            double y = dir_sign.getHauteur()/2.0 - SignSymbol.getSize(gamme_actuelle)/2.0;
            //carré blanc
            sign_e.appendChild(renderRectangle(x, y,
                    SignSymbol.getSize(gamme_actuelle),
                    SignSymbol.getSize(gamme_actuelle),
                    DirectionalSign.DirectionalSignColor.WHITE));
            //carré bleu
            x += 0.06*gamme_actuelle;
            y += 0.06*gamme_actuelle;

            sign_e.appendChild(renderRectangle(x, y,
                    SignSymbol.getBlueSquareSize(gamme_actuelle),
                    SignSymbol.getBlueSquareSize(gamme_actuelle),
                    DirectionalSign.DirectionalSignColor.BLUE));
        } else
        if (dir_sign.getSignSymbol().type == SignSymbol.SignSymbolType.INTERDICTION) {
            double longueur_texte = 0;
            for (int i = 0; i < dir_sign.getMentions().size(); i++) {
                double longueur_ligne = Font.getTextLength(dir_sign.getMentions().get(i).nom,
                        dir_sign.getFont(), dir_sign.getNumero_gamme(), true);
                if (longueur_ligne > longueur_texte) longueur_texte = longueur_ligne;
            }
            double x = dir_sign.getLongueur() - dir_sign.getLargeur_listel()
                    - SignSymbol.getSize(gamme_actuelle) - 0.5*gamme_actuelle;
            double y = dir_sign.getHauteur()/2.0;
            //rond bord blanc
            sign_e.appendChild(renderCircle(x +
                            SignSymbol.getSize(gamme_actuelle)/2.0,
                    y,
                    SignSymbol.getSize(gamme_actuelle)/2.0,
                    DirectionalSign.DirectionalSignColor.WHITE));
            //rond rouge
            x += 0.06*gamme_actuelle;
            sign_e.appendChild(renderCircle(x +
                            SignSymbol.getRedCircleSize(gamme_actuelle)/2.0,
                    y,
                    SignSymbol.getRedCircleSize(gamme_actuelle)/2.0,
                    DirectionalSign.DirectionalSignColor.RED));
            //rond blanc interieur
            x += 0.25*gamme_actuelle;
            sign_e.appendChild(renderCircle(x +
                            SignSymbol.getWhiteInnerCircleSize(gamme_actuelle)/2.0,
                    y,
                    SignSymbol.getWhiteInnerCircleSize(gamme_actuelle)/2.0,
                    DirectionalSign.DirectionalSignColor.WHITE));
        }

        // texte et idéogramme par mention
        double decalage;
        for (int j = 0; j < dir_sign.getMentions().size(); j++) {
                //rendu par mention
                Font.SignFont font =
                        dir_sign.getMentions().get(j).font == Font.SignFont.L4serre ?
                        Font.SignFont.L4serre : dir_sign.getFont();
                //calcul du décalage
                decalage = dir_sign.getLongueur() - dir_sign.getLargeur_listel() - gamme_actuelle/2.0 -
                        Font.getTextLength(dir_sign.getMentions().get(j).toString(), font, dir_sign.getNumero_gamme(), true);
                if (dir_sign.getMentions().get(j).ideogram.type != SignIdeogram.SignIdeogramType.NONE)
                    decalage += SignIdeogram.getSize(dir_sign.getHauteur_composition()) + gamme_actuelle/2.0;
                if (dir_sign.getSignSymbol().type != SignSymbol.SignSymbolType.NONE)
                    decalage += SignSymbol.getSize(dir_sign.getHauteur_composition()) + gamme_actuelle/2.0;
                //nom
                if (dir_sign.getSignSymbol().type != SignSymbol.SignSymbolType.NONE && dir_sign.getLineNumber() == 1)
                    sign_e.appendChild( renderText(decalage,  dir_sign.getLargeur_listel() + gamme_actuelle*2,
                            dir_sign.getNumero_gamme()*(j-1), dir_sign.getMentions().get(j).toString(), font,
                            DirectionalSign.getSecondColor(dir_sign.getColor()), true));
                else sign_e.appendChild( renderText(decalage,  dir_sign.getLargeur_listel() + gamme_actuelle/2.0*(j+1)
                                + gamme_actuelle*(j+1), dir_sign.getNumero_gamme(), dir_sign.getMentions().get(j).toString(),
                        font, DirectionalSign.getSecondColor(dir_sign.getColor()), true)
                );
                //ideogramme
                if (dir_sign.isWith_ideogram()) {
                    sign_e.appendChild( renderEmplacementIdeogeogram(dir_sign));
                }
                // distance
                if (dir_sign.isWith_distance()) {
                    String digit_string;
                    if (dir_sign.getMentions().get(j).distance ==
                            (int) dir_sign.getMentions().get(j).distance ) digit_string =
                            String.valueOf((int) dir_sign.getMentions().get(j).distance);
                    else digit_string = String.valueOf(dir_sign.getMentions().get(j).distance);

                    decalage = dir_sign.getLongueur_pointe_fleche() + 0.5*gamme_actuelle;

                    sign_e.appendChild(
                            renderText(decalage,
                                    dir_sign.getLargeur_listel() + gamme_actuelle/2.0*(j+1) + gamme_actuelle*(j+1),
                                    dir_sign.getNumero_gamme(), digit_string, font,
                                    DirectionalSign.getSecondColor(dir_sign.getColor()),
                                    true)
                    );
                }
            }

        return sign_e;
    }

    public Element renderRectangle(double x1, double y1, double x2, double y2,
                                   DirectionalSign.DirectionalSignColor color) {
        Element rectangle = getSvg_document().createElementNS(svgNS, "rect");
        rectangle.setAttributeNS(null, "x", String.valueOf(x1));
        rectangle.setAttributeNS(null, "y", String.valueOf(y1));
        rectangle.setAttributeNS(null, "width", String.valueOf(x2));
        rectangle.setAttributeNS(null, "height", String.valueOf(y2));
        rectangle.setAttributeNS(null, "style", "fill:"+ DirectionalSign.getColorHex(color));

        return rectangle;
    }

    public Element renderCircle(double cx, double cy, double r,
                                DirectionalSign.DirectionalSignColor color) {
        Element circle = getSvg_document().createElementNS(svgNS, "circle");
        circle.setAttributeNS(null, "cx", String.valueOf(cx));
        circle.setAttributeNS(null, "cy", String.valueOf(cy));
        circle.setAttributeNS(null, "r", String.valueOf(r));
        circle.setAttributeNS(null, "style", "fill:"+ DirectionalSign.getColorHex(color));
        return circle;
    }

    public Element renderText(double x, double y, int numeroGamme, String text, Font.SignFont font,
                              DirectionalSign.DirectionalSignColor color, boolean L2grand) {
        Element textElement = svg_document.createElementNS(svgNS, "g");
        double decalage = x;

        int gamme_actuelle;
        if (L2grand && (font == Font.SignFont.L2serre ||
                (font == Font.SignFont.L4serre && color == DirectionalSign.DirectionalSignColor.WHITE)))
            gamme_actuelle = DirectionalSign.gammes[numeroGamme + 1];
        else gamme_actuelle = DirectionalSign.gammes[numeroGamme];

        for (int i = 0; i < text.length(); i++) {
            Element lettre = getSvg_document().createElementNS(svgNS, "text");
            textElement.appendChild(lettre);
            lettre.setAttributeNS(null, "style", "font-size:" + gamme_actuelle
                    + "pt;font-family:Caracteres " + font.name() + ";fill:" +
                    DirectionalSign.getColorHex(color));
            lettre.setAttributeNS(null, "x", String.valueOf(decalage));
            lettre.setAttributeNS(null, "y", String.valueOf(y));

            lettre.setTextContent(String.valueOf(text.charAt(i)));
            if (i < text.length() - 1) {

                decalage += Font.getGapLengthBetweenLetter(text.charAt(i),
                        text.charAt(i+1), font, numeroGamme)
                        + Font.getLetterLength(text.charAt(i), font, gamme_actuelle);
            }
        }
        return textElement;
    }

    public Element renderPointeFleche(DirectionalSign dir_sign) {
        Element pointe_groupe = svg_document.createElementNS(svgNS, "g");
        Element pointe_de_fleche = svg_document.createElementNS(svgNS, "path");
        Element pointe_fond = svg_document.createElementNS(svgNS, "path");
        pointe_groupe.appendChild(pointe_de_fleche);
        pointe_groupe.appendChild(pointe_fond);

        boolean new_angle = false;

        double angle_pointe = 75;
        double angle_fond = new_angle ? 100 : 85;

        double angle_interieur = angle_fond/2; // angle entre horizontal et pente
        double angle_exterieur = angle_pointe/2;

        double angle_interieur_vert = 90 - angle_interieur;
        double angle_exterieur_vert = 90 - angle_exterieur;

        double fond_pointe_x;
        if (dir_sign.getSignSymbol().type != SignSymbol.SignSymbolType.NONE && dir_sign.getLineNumber() == 1)
            fond_pointe_x = Math.tan(Math.toRadians(angle_interieur_vert))*dir_sign.getHauteur_composition()*1.5;
        else fond_pointe_x = Math.tan(Math.toRadians(angle_interieur_vert))*dir_sign.getHauteur_composition()*dir_sign.getLineNumber();

        double longueur_pointe_virtuelle = Math.tan(Math.toRadians(angle_exterieur_vert))*(dir_sign.getHauteur()/2);
        double longueur_pointe_fond_pointe_listel_x = Math.tan(Math.toRadians(42.5))*longueur_pointe_virtuelle - fond_pointe_x;
        double rayon_arrondi_pointe = longueur_pointe_fond_pointe_listel_x *
                Math.sin(Math.toRadians(92.5)) / Math.sin(Math.toRadians(angle_interieur+92.5));

        double point_rayon_arrondi_pointe_x;
        double point_rayon_arrondi_pointe_y = Math.cos(Math.toRadians(angle_interieur_vert)) * rayon_arrondi_pointe*0.5;

        switch (dir_sign.getLineNumber()) {
            case 1 -> point_rayon_arrondi_pointe_x = Math.cos(Math.toRadians(angle_interieur_vert)) * rayon_arrondi_pointe * 1.0;
            case 2 -> {
                point_rayon_arrondi_pointe_x = Math.cos(Math.toRadians(angle_interieur_vert)) * rayon_arrondi_pointe * 1;
                longueur_pointe_virtuelle *= 1.1;
            }
            case 3 -> {
                point_rayon_arrondi_pointe_x = Math.cos(Math.toRadians(angle_interieur_vert)) * rayon_arrondi_pointe * 1.2;
                longueur_pointe_virtuelle *= 1.1;
            }
            case 4 -> throw new NotImplementedException("4ème ligne non implémentée");
            default -> throw new IllegalStateException("nombre de ligne incorrect : " + dir_sign.getLineNumber());
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
            default -> throw new IllegalStateException("direction incorrecte");
        }

        return pointe_groupe;
    }

    public Element renderCartouche(Cartouche cartouche, DirectionalSignGroup dir_sign_grp) {
        cartouche.computeLengths();

        Element cartouche_groupe = svg_document.createElementNS(svgNS,"g");
        Element bord_haut = renderRectangle(cartouche.getLargeurListel()*1.5, 0,
                cartouche.getLongueur()-3*cartouche.getLargeurListel(), cartouche.getLargeurListel()*1.5,
                Cartouche.getColor(cartouche.getType()));
        Element bord_droit = renderRectangle(cartouche.getLongueur()-cartouche.getLargeurListel()*1.5,
                cartouche.getLargeurListel()*1.5, cartouche.getLargeurListel()*1.5,
                cartouche.getHauteur()-3*cartouche.getLargeurListel(),Cartouche.getColor(cartouche.getType()));
        Element bord_bas = renderRectangle(cartouche.getLargeurListel()*1.5,
                cartouche.getHauteur()-cartouche.getLargeurListel()*1.5,
                cartouche.getLongueur()-3*cartouche.getLargeurListel(),
                cartouche.getLargeurListel()*1.5, Cartouche.getColor(cartouche.getType()));
        Element bord_gauche = renderRectangle(0, cartouche.getLargeurListel()*1.5,
                cartouche.getLargeurListel()*1.5,cartouche.getHauteur()-3*cartouche.getLargeurListel(),
                Cartouche.getColor(cartouche.getType()));

        Element rond_haut_gauche = renderCircle(cartouche.getLargeurListel()*1.5,
                cartouche.getLargeurListel()*1.5, cartouche.getLargeurListel()*1.5,
                Cartouche.getColor(cartouche.getType()));
        Element rond_haut_droit = renderCircle(cartouche.getLongueur()-cartouche.getLargeurListel()*1.5,
                cartouche.getLargeurListel()*1.5, cartouche.getLargeurListel()*1.5,
                Cartouche.getColor(cartouche.getType()));
        Element rond_bas_gauche = renderCircle(cartouche.getLargeurListel()*1.5,
                cartouche.getHauteur()-cartouche.getLargeurListel()*1.5, cartouche.getLargeurListel()*1.5,
                Cartouche.getColor(cartouche.getType()));
        Element rond_bas_droit = renderCircle(cartouche.getLongueur()-cartouche.getLargeurListel()*1.5,
                cartouche.getHauteur()-cartouche.getLargeurListel()*1.5, cartouche.getLargeurListel()*1.5,
                Cartouche.getColor(cartouche.getType()));

        Element fond = renderRectangle(cartouche.getLargeurListel(), cartouche.getLargeurListel(),
                cartouche.getLongueur()-cartouche.getLargeurListel()*2,
                cartouche.getHauteur()-cartouche.getLargeurListel()*2,Cartouche.getColor(cartouche.getType()));

        DirectionalSign.DirectionalSignColor fontColor = DirectionalSign.getSecondColor(Cartouche.getColor(cartouche.getType()));
        Font.SignFont font = fontColor == DirectionalSign.DirectionalSignColor.WHITE
                ? Font.SignFont.L2serre : Font.SignFont.L1serre;

        int Hc = DirectionalSign.gammes[dir_sign_grp.getSign(0).getNumero_gamme()];

        Element texte = renderText(cartouche.getLargeurListel()+0.25*Hc, cartouche.getHauteur()-cartouche.getLargeurListel()-0.25*Hc,
                dir_sign_grp.getSign(0).getNumero_gamme(), cartouche.toString(), font, fontColor, false);

        cartouche_groupe.appendChild(bord_haut);
        cartouche_groupe.appendChild(bord_droit);
        cartouche_groupe.appendChild(bord_bas);
        cartouche_groupe.appendChild(bord_gauche);
        cartouche_groupe.appendChild(rond_haut_gauche);
        cartouche_groupe.appendChild(rond_haut_droit);
        cartouche_groupe.appendChild(rond_bas_droit);
        cartouche_groupe.appendChild(rond_bas_gauche);
        cartouche_groupe.appendChild(fond);
        cartouche_groupe.appendChild(texte);

        return cartouche_groupe;
    }

    public Element renderEmplacementIdeogeogram(DirectionalSign dir_sign) {
        Element ideo_empl_e = svg_document.createElementNS(svgNS, "g");
        int gamme_actuelle = DirectionalSign.gammes[dir_sign.getNumero_gamme()];
        double z = dir_sign.getLargeur_listel() + 0.25*gamme_actuelle;
        // tour noir
        ideo_empl_e.appendChild(renderRectangle(z+0.30*gamme_actuelle,
                z + 0.05*gamme_actuelle,
                SignIdeogram.getColoredBorderSize(gamme_actuelle),
                SignIdeogram.getColoredBorderSize(gamme_actuelle),
                DirectionalSign.getSecondColor(dir_sign.getColor())));
        // fond blanc
        z += 0.1*gamme_actuelle;
        ideo_empl_e.appendChild(renderRectangle(z+0.30*gamme_actuelle,
                z + 0.05*gamme_actuelle,
                SignIdeogram.getInternalSize(gamme_actuelle),
                SignIdeogram.getInternalSize(gamme_actuelle),
                dir_sign.getColor()));
        return ideo_empl_e;
    }

    @Override
    public void signSet(Sign sign, boolean firstRender) throws NotImplementedException {
        //setSign(sign);
        renderedSign = false;
        svgCanvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {
            @Override
            public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
                if (svgCanvas.getUpdateManager() != null)
                    svgCanvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(() -> {
                        if (!renderedSign) {
                            try {
                                render(sign);
                            } catch (SignException ex) {
                                ex.printStackTrace();
                            }
                            renderedSign = true;
                        } else {
                            AffineTransform af = new AffineTransform();
                            af.scale(0.25, 0.25);
                            af.translate(50, 50);

                            svgCanvas.setRenderingTransform(af);
                            revalidate();
                        }
                    });
            }

            @Override
            public void gvtRenderingFailed(GVTTreeRendererEvent e) {
                JOptionPane.showMessageDialog(mainFrame, "échec de rendu");
            }
        });
        try {
            render(sign);
        } catch (SignException e) {
            e.printStackTrace();
        }
        svgCanvas.setDocument(getSvg_document());
        svgCanvas.setEnableZoomInteractor(true);
        revalidate();
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public Document getSvg_document() {
        return svg_document;
    }

    public EditorUI getEditorUI() {
        if (editorUI != null) return editorUI;
        else return mainFrame.getEditorUI();
    }

    public void setSvg_document(Document svg_document) {
        this.svg_document = svg_document;
    }
}
