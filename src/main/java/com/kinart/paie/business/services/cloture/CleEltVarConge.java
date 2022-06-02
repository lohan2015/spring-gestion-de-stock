package com.kinart.paie.business.services.cloture;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

public class CleEltVarConge {
    private Integer idEntreprise;
    private String aamm;
    private String nmat;
    private Integer nbul;
    private Date ddeb;

    public CleEltVarConge() {
    }

    public CleEltVarConge(Integer idEntreprise, String aamm, String nmat, Integer nbul, Date ddeb) {
        this.idEntreprise = idEntreprise;
        this.aamm = aamm;
        this.nmat = nmat;
        this.nbul = nbul;
        this.ddeb = ddeb;
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

    public Date getDdeb() {
        return ddeb;
    }

    public void setDdeb(Date ddeb) {
        this.ddeb = ddeb;
    }
}
