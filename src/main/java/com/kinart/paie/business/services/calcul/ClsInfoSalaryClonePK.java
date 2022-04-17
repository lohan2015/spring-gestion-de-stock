package com.kinart.paie.business.services.calcul;

import java.io.Serializable;



/** @author Hibernate CodeGenerator */
public class ClsInfoSalaryClonePK implements Serializable {

    /** identifier field */
    private String cdos;

    /** identifier field */
    private String nmat;

    /** full constructor */
    public ClsInfoSalaryClonePK(String cdos, String nmat) {
        this.cdos = cdos;
        this.nmat = nmat;
    }

    /** default constructor */
    public ClsInfoSalaryClonePK() {
    }

    public String getCdos() {
        return this.cdos;
    }

    public void setCdos(String cdos) {
        this.cdos = cdos;
    }

    public String getNmat() {
        return this.nmat;
    }

    public void setNmat(String nmat) {
        this.nmat = nmat;
    }

    public String toString() {
        return null;//new ToStringBuilder(this).append("cdos", getCdos()).append("nmat", getNmat()).toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof ClsInfoSalaryClonePK) ) return false;
        ClsInfoSalaryClonePK castOther = (ClsInfoSalaryClonePK) other;
        return true;//new EqualsBuilder().append(this.getCdos(), castOther.getCdos()).append(this.getNmat(), castOther.getNmat()).isEquals();
    }

    public int hashCode() {
        return 0;//new HashCodeBuilder().append(getCdos()).append(getNmat()).toHashCode();
    }

}
