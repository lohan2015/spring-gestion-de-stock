package com.kinart.paie.business.services.calcul;

import java.io.Serializable;
import java.util.Date;



/** @author Hibernate CodeGenerator */
public class ClsElementVariableCongeClonePK implements Serializable {

    /** identifier field */
    private String cdos;

    /** identifier field */
    private String aamm;

    /** identifier field */
    private String nmat;

    /** identifier field */
    private Integer nbul;

    /** identifier field */
    private Date ddeb;

    /** full constructor */
    public ClsElementVariableCongeClonePK(String cdos, String aamm, String nmat, Integer nbul, Date ddeb) {
        this.cdos = cdos;
        this.aamm = aamm;
        this.nmat = nmat;
        this.nbul = nbul;
        this.ddeb = ddeb;
    }

    /** default constructor */
    public ClsElementVariableCongeClonePK() {
    }

    public String getCdos() {
        return this.cdos;
    }

    public void setCdos(String cdos) {
        this.cdos = cdos;
    }

    public String getAamm() {
        return this.aamm;
    }

    public void setAamm(String aamm) {
        this.aamm = aamm;
    }

    public String getNmat() {
        return this.nmat;
    }

    public void setNmat(String nmat) {
        this.nmat = nmat;
    }

    public Integer getNbul() {
        return this.nbul;
    }

    public void setNbul(Integer nbul) {
        this.nbul = nbul;
    }

    public Date getDdeb() {
        return this.ddeb;
    }

    public void setDdeb(Date ddeb) {
        this.ddeb = ddeb;
    }

    public String toString() {
        return null;
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof ClsElementVariableCongeClonePK) ) return false;
        ClsElementVariableCongeClonePK castOther = (ClsElementVariableCongeClonePK) other;
        return true;
    }

    public int hashCode() {
        return 0;
    }

}
