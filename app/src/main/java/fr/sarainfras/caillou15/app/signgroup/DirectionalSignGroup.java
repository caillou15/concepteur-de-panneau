package fr.sarainfras.caillou15.app.signgroup;

import fr.sarainfras.caillou15.app.sign.DirectionalSign;

import java.util.ArrayList;

public class DirectionalSignGroup {
    ArrayList<DirectionalSign> directionalSignArrayList;

    ArrayList<Cartouche> cartoucheArrayList;

    boolean alignement_panneaux = false;

    double longueur = 0;
    double hauteur = 0;

    public DirectionalSignGroup() {
        directionalSignArrayList = new ArrayList<>();
        cartoucheArrayList = new ArrayList<>();
    }

    public void addSign(DirectionalSign dir_sign) {
        directionalSignArrayList.add(dir_sign);
    }

    public void editSign(int position, DirectionalSign new_dir_sign) {
        if (!directionalSignArrayList.get(position).equals(new_dir_sign))
            directionalSignArrayList.set(position, new_dir_sign);
    }

    public DirectionalSign getSign(int position) {
        return directionalSignArrayList.get(position);
    }

    public int getSignNumber() {
        return directionalSignArrayList.size();
    }

    public ArrayList<DirectionalSign> getSignList() {
        return directionalSignArrayList;
    }

    public ArrayList<Cartouche> getCartoucheList() {return cartoucheArrayList;}

    /**
     * déplace le panneau spécifié dans la position originale vers la position de destination spécifiée
     * en réalité, inverse juste les éléments aux deux positions fournies.
     * @param position_origine
     * @param position_destination
     */
    public void moveSign(int position_origine, int position_destination) {
        DirectionalSign temp = directionalSignArrayList.get(position_origine);
        directionalSignArrayList.set(position_origine,
                directionalSignArrayList.get(position_destination));
        directionalSignArrayList.set(position_destination, temp);
    }

    public void deleteSign(int position) {
        directionalSignArrayList.remove(position);
    }

    public void compute_lenghts() {
        double new_longueur = 0.0;
        double new_hauteur = 0.0;

        // longueur
        for (DirectionalSign dir_sign : directionalSignArrayList) {
            if (new_longueur < dir_sign.getLongueur())
                new_longueur = dir_sign.getLongueur();
        }

        if (alignement_panneaux) {
            for (DirectionalSign dir_sign : directionalSignArrayList) {
                if (dir_sign.getLongueur() != new_longueur) dir_sign.setLongueur_supplementaire(new_longueur-dir_sign.getLongueur());
            }
        } else {
            for (DirectionalSign dir_sign : directionalSignArrayList) {
                dir_sign.setLongueur_supplementaire(0);
            }
        }

        // new_hauteur
        for (int i = 0; i < directionalSignArrayList.size(); i++) {
            new_hauteur += directionalSignArrayList.get(i).getHauteur();
            if (i < directionalSignArrayList.size() && directionalSignArrayList.size() > 1)
                new_hauteur += getEspaceEntrePanneauMemeDirection(
                        DirectionalSign.gammes[directionalSignArrayList.get(0).getNumero_gamme()]);
        }

        for (int i = 0; i < cartoucheArrayList.size();i++) {
            new_hauteur += cartoucheArrayList.get(i).getHauteur();
            if (i < cartoucheArrayList.size() && cartoucheArrayList.size() > 0)
                new_hauteur += getEspaceEntrePanneauMemeDirection(
                        DirectionalSign.gammes[directionalSignArrayList.get(0).getNumero_gamme()]);
        }

        this.longueur = new_longueur;
        this.hauteur = new_hauteur;
    }

    public double getEspaceEntrePanneauMemeDirection(int Hb) { return Hb/4.0; }

    public double getEspaceEntrePanneauDirectionOpposee(int Hb) { return Hb/2.0; }

}
