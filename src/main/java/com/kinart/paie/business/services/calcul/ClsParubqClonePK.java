package com.kinart.paie.business.services.calcul;

import java.io.Serializable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;


/** @author Hibernate CodeGenerator */
public class ClsParubqClonePK implements Serializable {

    /** identifier field */
    private String cdos;

    /** identifier field */
    private String crub;

    /** full constructor */
    public ClsParubqClonePK(String cdos, String crub) {
        this.cdos = cdos;
        this.crub = crub;
    }

    /** default constructor */
    public ClsParubqClonePK() {
    }

    public String getCdos() {
        return this.cdos;
    }

    public void setCdos(String cdos) {
        this.cdos = cdos;
    }

    public String getCrub() {
        return this.crub;
    }

    public void setCrub(String crub) {
        this.crub = crub;
    }

    public String toString() {
        return new ToStringBuilder(this)
            .append("cdos", getCdos())
            .append("crub", getCrub())
            .toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof ClsParubqClonePK) ) return false;
        ClsParubqClonePK castOther = (ClsParubqClonePK) other;
        return new EqualsBuilder()
            .append(this.getCdos(), castOther.getCdos())
            .append(this.getCrub(), castOther.getCrub())
            .isEquals();
    }

    public int hashCode() {
        return new HashCodeBuilder()
            .append(getCdos())
            .append(getCrub())
            .toHashCode();
    }

}
