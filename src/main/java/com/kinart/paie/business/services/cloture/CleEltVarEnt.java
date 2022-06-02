package com.kinart.paie.business.services.cloture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CleEltVarEnt {
    private Integer idEntreprise;
    private String aamm;
    private String nmat;
    private Integer nbul;

    public CleEltVarEnt() {
    }

    public CleEltVarEnt(Integer idEntreprise, String aamm, String nmat, Integer nbul) {
        this.idEntreprise = idEntreprise;
        this.aamm = aamm;
        this.nmat = nmat;
        this.nbul = nbul;
    }

    public Integer getIdEntreprise() {
        return idEntreprise;
    }

    public void setIdEntreprise(Integer idEntreprise) {
        this.idEntreprise = idEntreprise;
    }

    public String getAamm() {
        return aamm;
    }

    public void setAamm(String aamm) {
        this.aamm = aamm;
    }

    public String getNmat() {
        return nmat;
    }

    public void setNmat(String nmat) {
        this.nmat = nmat;
    }

    public Integer getNbul() {
        return nbul;
    }

    public void setNbul(Integer nbul) {
        this.nbul = nbul;
    }
}
