package com.kinart.organisation.business.vo;

public class RechercheNiveauOrganigrammeVO
{
    public String libelle;

    public String code;

    public String cdos;

    public String clang;

    public RechercheNiveauOrganigrammeVO(String cdos, String clang)
    {
        super();
        this.cdos = cdos;
        this.clang = clang;
    }

    public String getLibelle()
    {
        return libelle;
    }

    public void setLibelle(String libelle)
    {
        this.libelle = libelle;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public String getCdos()
    {
        return cdos;
    }

    public void setCdos(String cdos)
    {
        this.cdos = cdos;
    }

}

