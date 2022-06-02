package com.kinart.paie.business.services.cloture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

public class CleCumulPaie {
    private Integer idEntreprise;
    private String aamm;
    private String nmat;
    private Integer nbul;
    private String rubq;

    public CleCumulPaie() {
    }

    public CleCumulPaie(Integer idEntreprise, String nmat, String aamm, String rubq, Integer nbul) {
        this.idEntreprise = idEntreprise;
        this.aamm = aamm;
        this.nmat = nmat;
        this.nbul = nbul;
        this.rubq = rubq;
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

    public String getRubq() {
        return rubq;
    }

    public void setRubq(String rubq) {
        this.rubq = rubq;
    }
}
