package fr.sarainfras.caillou15.app.signgroup;

import fr.sarainfras.caillou15.app.AppWindow;
import fr.sarainfras.caillou15.app.Constants;
import fr.sarainfras.caillou15.app.SignException;
import fr.sarainfras.caillou15.app.events.group.SignGroupChangeListener;
import fr.sarainfras.caillou15.app.render.DirectionalSignRenderer;
import fr.sarainfras.caillou15.app.sign.DirectionalSign;
import org.apache.batik.anim.dom.SVGDOMImplementation;
import org.apache.batik.bridge.CSSFontFace;
import org.apache.batik.bridge.CSSUtilities;
import org.apache.batik.swing.JSVGCanvas;
import org.apache.batik.swing.JSVGScrollPane;
import org.apache.batik.swing.gvt.GVTTreeRendererAdapter;
import org.apache.batik.swing.gvt.GVTTreeRendererEvent;
import org.apache.batik.swing.svg.JSVGComponent;
import org.apache.batik.swing.svg.SVGUserAgentAdapter;
import org.w3c.dom.DOMImplementation;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.Map;

public class DirectionalSignGroupRenderer extends JPanel implements SignGroupChangeListener {
    AppWindow main_frame;
    SignGroupUI signGroupUI;

    private DirectionalSignGroup directionalSignGroup;
    private JSVGCanvas svgCanvas;
    private Document svg_document;
    private DirectionalSignRenderer renderer;

    boolean renderedSignGroup = false;

    DOMImplementation dom_impl = SVGDOMImplementation.getDOMImplementation();
    String svgNS = SVGDOMImplementation.SVG_NAMESPACE_URI;

    public DirectionalSignGroupRenderer(AppWindow main_frame, SignGroupUI signGroupUI) {
        this.main_frame = main_frame;
        this.signGroupUI = signGroupUI;
        init();
    }

    private void init() {
        this.setBackground(Constants.BACKGROUND_GREEN);
        this.svgCanvas = new JSVGCanvas(new SVGUserAgentAdapter(), true, false);
        this.renderer = new DirectionalSignRenderer(null, main_frame, signGroupUI, svg_document);
        signGroupUI.getSignGroupChangeInitiater().addListener(this);
        this.directionalSignGroup = signGroupUI.directionalSignGroup;
        if (svg_document == null) svg_document = dom_impl.createDocument(svgNS, "svg",  null);

        AffineTransform af = new AffineTransform();
        af.scale(0.25, 0.25);
        svgCanvas.setRenderingTransform(af);
    }

    public void render(DirectionalSignGroup dir_sign_grp) {
        this.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        this.add(this.svgCanvas, constraints);

        svgCanvas.setDocumentState(JSVGComponent.ALWAYS_DYNAMIC);

        if (getSvg_document() == null) svg_document = dom_impl.createDocument(svgNS, "svg",  null);
        else {
            while (getSvg_document().getDocumentElement().hasChildNodes()) {
                getSvg_document().getDocumentElement().removeChild(getSvg_document().getDocumentElement().getFirstChild());
            }
        }

        svgCanvas.setBackground(Constants.BACKGROUND_GREEN);
        svgCanvas.setEnableZoomInteractor(true);
        svgCanvas.setEnableImageZoomInteractor(true);

        dir_sign_grp.compute_lenghts();

        svg_document.getDocumentElement().setAttribute("width", String.valueOf(dir_sign_grp.longueur));
        svg_document.getDocumentElement().setAttribute("height", String.valueOf(dir_sign_grp.hauteur));

        Dimension display_size = new Dimension((int) (dir_sign_grp.longueur / 2), (int) (dir_sign_grp.hauteur / 2));

        svgCanvas.setSize(display_size);

        renderer.setSvg_document(svg_document);

        Element sign_group_e = svg_document.createElementNS(svgNS, "g");
        double hauteur_accumulee = 0.0;

        for (int i = 0; i < directionalSignGroup.directionalSignArrayList.size(); i++) {
            DirectionalSign dir_sign = directionalSignGroup.directionalSignArrayList.get(i);
            Element sign_e = renderer.renderDirectionalSign(dir_sign);

            if (i != 0) {
                //décalage
                sign_e.setAttribute("transform", "translate(0.0," + hauteur_accumulee/1.0 + ")");
            }
            sign_group_e.appendChild(sign_e);

            hauteur_accumulee += dir_sign.getHauteur() + 0*dir_sign.getLargeur_listel();
            hauteur_accumulee += dir_sign_grp.getEspaceEntrePanneauMemeDirection(
                    DirectionalSign.gammes[dir_sign_grp.getSign(i).getNumero_gamme()]);
        }

        svg_document.getDocumentElement().appendChild(sign_group_e);

        svgCanvas.setRecenterOnResize(true);
        CSSUtilities.convertTextRendering(svg_document.getDocumentElement(), null);

        svgCanvas.setDocument(getSvg_document());

        revalidate();
        repaint();
    }

    public Document getSvg_document() {
        return svg_document;
    }

    @Override
    public void signGroupChangeSet(DirectionalSignGroup new_sign) {
        svgCanvas.addGVTTreeRendererListener(new GVTTreeRendererAdapter() {
            @Override
            public void gvtRenderingCompleted(GVTTreeRendererEvent e) {
                if (svgCanvas.getUpdateManager() != null)
                    svgCanvas.getUpdateManager().getUpdateRunnableQueue().invokeLater(() -> {
                        if (!renderedSignGroup) {
                            render(new_sign);
                            renderedSignGroup = true;
                        } else {
                            AffineTransform af = new AffineTransform();
                            af.scale(0.25, 0.25);
                            af.translate(50, 50);
                            svgCanvas.setRenderingTransform(af);
                        }
                        revalidate();
                    });
            }

            @Override
            public void gvtRenderingFailed(GVTTreeRendererEvent e) {
                JOptionPane.showMessageDialog(main_frame, "échec de rendu");
            }
        });
    }

    @Override
    public void signGroupUpdate() {
        signGroupChangeSet(directionalSignGroup);
        render(directionalSignGroup);
    }
}
