package com.kinart.paie.business.services.calcul;

import com.kinart.paie.business.model.ElementVariableConge;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * Représente la table des éléments variables congé. Etant donné que les éléments variables congé historisés
 * ont la méme structure que ceux qui ne sont pas historisés, cette classe a été créée pour garder cette structure commune.
 * @author e.etoundi
 */
public class ClsElementVariableCongeClone implements Serializable {

    /** identifier field */
    private ClsElementVariableCongeClonePK comp_id;

    /** nullable persistent field */
    private Date dfin;

    /** nullable persistent field */
    private BigDecimal nbjc;

    /** nullable persistent field */
    private BigDecimal nbja;

    /** nullable persistent field */
    private String motf;

    /** nullable persistent field */
    private BigDecimal mont;

    /** nullable persistent field */
    private String cuti;

    /**
     * full constructor
     * @param evconge élément variable sur la base duquel cet objet est construit
     */
    public ClsElementVariableCongeClone(ElementVariableConge evconge) {
    	this.comp_id = new ClsElementVariableCongeClonePK();
        this.comp_id.setAamm(evconge.getAamm());
        this.comp_id.setCdos(String.valueOf(evconge.getIdEntreprise()));
        this.comp_id.setDdeb(evconge.getDdeb());
        this.comp_id.setNbul(evconge.getNbul());
        this.comp_id.setNmat(evconge.getNmat());
        this.dfin = evconge.getDfin();
        this.nbjc = evconge.getNbjc();
        this.nbja = evconge.getNbja();
        this.motf = evconge.getMotf();
        this.mont = evconge.getMont();
    }

    /**
     * full constructor
     * @param evconge élément variable sur la base duquel cet objet est construit
     */
//    public ClsElementVariableCongeClone(Rhthevcg evconge) {
//    	this.comp_id = new ClsElementVariableCongeClonePK();
//        this.comp_id.setAamm(evconge.getAamm());
//        this.comp_id.setCdos(evconge.getCdos());
//        this.comp_id.setDdeb(evconge.getDdeb());
//        this.comp_id.setNbul(evconge.getNbul());
//        this.comp_id.setNmat(evconge.getNmat());
//        this.dfin = evconge.getDfin();
//        this.nbjc = evconge.getNbjc();
//        this.nbja = evconge.getNbja();
//        this.motf = evconge.getMotf();
//        this.mont = evconge.getMont();
//        this.cuti = evconge.getCuti();
//    }
    /**
     * Crée un nouvel objet qui est exactement la copie de cette instance.
     * @param conge l'objet é dupliquer
     * @return objet de méme type que la classe
     * @throws Exception
     */
    public static ClsElementVariableCongeClone clone(Object conge)throws Exception {
    	ClsElementVariableCongeClone rubaconge = null;
    	if(conge instanceof ElementVariableConge)
    		rubaconge = new ClsElementVariableCongeClone((ElementVariableConge) conge);
//    	else if(conge instanceof Rhthevcg)
//    		rubaconge = new ClsElementVariableCongeClone((Rhthevcg) conge);
    	else 
    		throw new Exception("Type of element variable mismatch.");
    	//
    	return rubaconge;
    }

    /** default constructor */
    public ClsElementVariableCongeClone() {
    }

    /** minimal constructor */
    public ClsElementVariableCongeClone(ClsElementVariableCongeClonePK comp_id) {
        this.comp_id = comp_id;
    }

    public ClsElementVariableCongeClonePK getComp_id() {
        return this.comp_id;
    }

    public void setComp_id(ClsElementVariableCongeClonePK comp_id) {
        this.comp_id = comp_id;
    }

    public Date getDfin() {
        return this.dfin;
    }

    public void setDfin(Date dfin) {
        this.dfin = dfin;
    }

    public BigDecimal getNbjc() {
        return this.nbjc;
    }

    public void setNbjc(BigDecimal nbjc) {
        this.nbjc = nbjc;
    }

    public BigDecimal getNbja() {
        return this.nbja;
    }

    public void setNbja(BigDecimal nbja) {
        this.nbja = nbja;
    }

    public String getMotf() {
        return this.motf;
    }

    public void setMotf(String motf) {
        this.motf = motf;
    }

    public BigDecimal getMont() {
        return this.mont;
    }

    public void setMont(BigDecimal mont) {
        this.mont = mont;
    }

    public String getCuti() {
        return this.cuti;
    }

    public void setCuti(String cuti) {
        this.cuti = cuti;
    }

    public String toString() {
        return null;//new ToStringBuilder(this).append("comp_id", getComp_id()).toString();
    }

    public boolean equals(Object other) {
        if ( (this == other ) ) return true;
        if ( !(other instanceof ClsElementVariableCongeClone) ) return false;
        ClsElementVariableCongeClone castOther = (ClsElementVariableCongeClone) other;
        return true;//new EqualsBuilder().append(this.getComp_id(), castOther.getComp_id()).isEquals();
    }

    public int hashCode() {
        return 0;//new HashCodeBuilder().append(getComp_id()).toHashCode();
    }

}
