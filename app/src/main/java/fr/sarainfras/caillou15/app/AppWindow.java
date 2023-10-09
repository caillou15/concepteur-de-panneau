package fr.sarainfras.caillou15.app;

import fr.sarainfras.caillou15.app.events.sign.SignInitiater;
import fr.sarainfras.caillou15.app.sign.Sign;
import fr.sarainfras.caillou15.app.sign.SignUI;
import fr.sarainfras.caillou15.app.signgroup.SignGroupUI;

import javax.swing.*;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.awt.*;
import java.io.File;
import java.util.Arrays;

public class AppWindow extends JFrame {

    private JMenuBar appMenuBar;

    private Sign sign;
    private EditorUI editorUI;
    private SignInitiater signInitiater;

    public AppWindow() {
        init();

        this.setSize(400, 300);
        this.setVisible(true);
    }

    private void init() {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            repaint();
            revalidate();
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | UnsupportedLookAndFeelException e) {
            e.printStackTrace();
        }
        signInitiater = new SignInitiater();
        if (!(Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()).anyMatch(font -> font.getName().equals("Caracteres L1 sserre"))
        || Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()).anyMatch(font -> font.getName().equals("Caracteres L2"))
        || Arrays.stream(GraphicsEnvironment.getLocalGraphicsEnvironment().getAllFonts()).anyMatch(font -> font.getName().equals("Caracteres L4"))
        )) {
            JOptionPane.showMessageDialog(this, "les polices L1, L2 et/ou L4 nécessaires ne sont pas disponibles");
            return;
        }

        init_gui();
    }

    private void init_gui() {
        init_menu();
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.pack();
    }

    private void init_menu() {
        appMenuBar = new JMenuBar();

        // fichier

        JMenu menu_fichier = new JMenu("Fichier");
        JMenuItem menu_item_nouveau = new JMenuItem("Nouveau panneau");
        menu_item_nouveau.addActionListener(e -> new_sign());
        menu_fichier.add(menu_item_nouveau);
        JMenuItem menu_item_nouveau_groupe = new JMenuItem("Nouveau groupe de panneaux");
        menu_item_nouveau_groupe.addActionListener(e -> new_sign_group());
        menu_fichier.add(menu_item_nouveau_groupe);
        menu_fichier.add(new JMenuItem("Ouvrir..."));
        JMenuItem menu_item_enregistrer = new JMenuItem("Enregistrer en SVG");
        menu_item_enregistrer.addActionListener(e -> {
            if (getEditorUI().getSvgDocument() == null) {
                JOptionPane.showMessageDialog((JComponent)e.getSource(), "Il n'y a pas de document à enregistrer."); return;
            }
            Transformer transformer = null;
            try {
                transformer = TransformerFactory.newInstance().newTransformer();
            } catch (TransformerConfigurationException exception) {
                exception.printStackTrace();
            }
            Result output = new StreamResult(new File(System.getProperty("user.dir") + "\\output.svg"));
            Source input = new DOMSource(getEditorUI().getSvgDocument());
            try {
                transformer.transform(input, output);
            } catch (TransformerException exception) {
                exception.printStackTrace();
            }
        });
        menu_fichier.add(menu_item_enregistrer);
        menu_fichier.addSeparator();
        JMenuItem menu_item_quitter = new JMenuItem("Quitter");
        menu_item_quitter.addActionListener(e -> exit());
        menu_fichier.add(menu_item_quitter);

        // edition

        JMenu menu_edition = new JMenu("Edition");
        menu_edition.add(new JMenuItem("Paramètres"));

        // aide

        JMenu menu_aide = new JMenu("Aide");
        menu_aide.add(new JMenuItem("Aide"));
        JMenuItem aboutMenuItem = new JMenuItem("A propos de...");
        aboutMenuItem.addActionListener(e -> JOptionPane.showMessageDialog(
                this, "Version : " + Constants.getVersionString()));
        menu_aide.add(aboutMenuItem);

        // ajout a la barre de menu
        appMenuBar.add(menu_fichier);
        appMenuBar.add(menu_edition);
        appMenuBar.add(menu_aide);
        this.setJMenuBar(appMenuBar);

    }

    public void new_sign() {
        setEditorUI(new SignUI(this));
        this.setContentPane(getEditorUI());
        getEditorUI().setVisible(true);
        getEditorUI().repaint();
        getEditorUI().revalidate();
    }

    public void new_sign_group() {
        setEditorUI(new SignGroupUI(this));
        this.setContentPane(getEditorUI());
        getEditorUI().setVisible(true);
        getEditorUI().revalidate();
    }

    public void exit() {
        System.exit(0);
    }

    public SignInitiater getSignInitiater() {
        return signInitiater;
    }

    public EditorUI getEditorUI() {
        return editorUI;
    }

    public void setEditorUI(EditorUI editorUI) {
        this.editorUI = editorUI;
    }

    public Sign getSign() {
        return sign;
    }

    public void setSign(Sign sign) {
        this.sign = sign;
    }

    public enum UIType {
        SIGN, GROUP
    }
}
