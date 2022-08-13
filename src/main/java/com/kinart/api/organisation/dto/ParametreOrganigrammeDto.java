package com.kinart.api.organisation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParametreOrganigrammeDto {
   /** The cellule depart. */
    private String celluleDepart;

    private String libelleCelluleDepart;

    /** The site. */
    private String site;

    /** The dossier. */
    private String dossier = "45";

    /** The niveau arrive. */
    private String niveauArrive;

    /** The type diagramme. */
    private String typeDiagramme;

    /** The affichage. */
    private String affichage;

    /** The compter cellules affichees uniquement. */
    private boolean compterCellulesAfficheesUniquement = false;

    /** The organigramme prestataire. */
    private boolean organigrammePrestataire = false;

    /** The afficher postes prestataires. */
    private boolean afficherPostesPrestataires = false;

    /** The afficher lignes fictive. */
    private boolean afficherLignesFictive = false;

    /** The afficher lignes fictive. */
    private boolean showlien = false;

    /** The afficher lignes fictive. */
    private boolean showliengen = false;

}
