package com.kinart.paie.business.services.calcul;

import org.apache.commons.lang3.StringUtils;

public class Concateneur {
    public boolean valdefaut = false;
    public String valeur = StringUtils.EMPTY;

    public Concateneur() {
    }

    public Concateneur(boolean valdefaut, String valeur) {
        this.valdefaut = valdefaut;
        this.valeur = valeur;
    }
}
