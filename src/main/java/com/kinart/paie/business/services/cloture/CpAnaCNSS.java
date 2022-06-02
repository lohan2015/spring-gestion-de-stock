package com.kinart.paie.business.services.cloture;

import java.math.BigDecimal;

public class CpAnaCNSS
{
  private String cdos;
  private String pcea;
  private String cg;
  private String gesa;
  private String cana;
  private String ruba;
  private String sena;
  private BigDecimal mona;
  private String codesite;
  
  public String insertQuery()
  {
    String query = "Insert Into Cp_analyt Values('" + this.cdos + "','" + this.pcea + "','" + this.cg + "','" + this.gesa + "','" + this.cana + "','" + this.ruba + "','" + this.sena + "'," + this.mona + ",'" + this.codesite + "')";
    return query;
  }
  
  public String getCdos()
  {
    return this.cdos;
  }
  
  public void setCdos(String cdos)
  {
    this.cdos = cdos;
  }
  
  public String getPcea()
  {
    return this.pcea;
  }
  
  public void setPcea(String pcea)
  {
    this.pcea = pcea;
  }
  
  public String getCg()
  {
    return this.cg;
  }
  
  public void setCg(String cg)
  {
    this.cg = cg;
  }
  
  public String getGesa()
  {
    return this.gesa;
  }
  
  public void setGesa(String gesa)
  {
    this.gesa = gesa;
  }
  
  public String getCana()
  {
    return this.cana;
  }
  
  public void setCana(String cana)
  {
    this.cana = cana;
  }
  
  public String getRuba()
  {
    return this.ruba;
  }
  
  public void setRuba(String ruba)
  {
    this.ruba = ruba;
  }
  
  public String getSena()
  {
    return this.sena;
  }
  
  public void setSena(String sena)
  {
    this.sena = sena;
  }
  
  public BigDecimal getMona()
  {
    return this.mona;
  }
  
  public void setMona(BigDecimal mona)
  {
    this.mona = mona;
  }

public String getCodesite() {
	return codesite;
}

public void setCodesite(String codesite) {
	this.codesite = codesite;
}
  
}
