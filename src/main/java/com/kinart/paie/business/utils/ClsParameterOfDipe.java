package com.kinart.paie.business.utils;

import com.kinart.paie.business.services.utils.GeneriqueConnexionService;

import java.io.File;
import java.math.BigDecimal;
import java.util.Date;

public class ClsParameterOfDipe
{

	public String cdos;

	public String clang;
	
	public String annee;

	public String aamm;
	
	public String cuti;	

	public Integer nbul;

	public String ville1;

	public String ville2;

	public int typeDipe = 0;

	public Date ddex;

	public Date dfex;
	
	public Integer typact=0;
	
	public Integer maxNbrJours = 99;
	
	public boolean inclureEnfantsEtNombreParts = false;

	public String cheminDipeMensuel;

	public String cheminDipeDebutExercice;

	public String cheminDipeFinExercice;

	public String cheminDipeTemporaire;

	public String cheminDipeEmbauche;
	
	public String cheminDipeMensuel2;

	public String cheminDipeDebutExercice2;

	public String cheminDipeFinExercice2;

	public String cheminDipeTemporaire2;

	public String cheminDipeEmbauche2;
	

	public String cheminDipeCD10;

	BigDecimal regCNPS = new BigDecimal(0);

	String ville;

	String quartier;

	String bp;

	String telephone;

	String numCompte;

	BigDecimal PLAFOND_CNPS = new BigDecimal(0);

	BigDecimal TAUX_PENS_VIEIL = new BigDecimal(0);

	BigDecimal TAUX_PREST_FAM = new BigDecimal(0);

	BigDecimal TAUX_ACC_TRAV = new BigDecimal(0);

	String dnii;

	String ncontribuable;

	int feuille;

	String aamm_deb;

	String aamm_fin;

	Date date;

	String date_c;

	String adexe_c;

	String afexe_c;

	String codenreg;

	public void deleteFileToGenerate()
	{
		try
		{
			File destination = null;
			switch (typeDipe)
			{
			case ClsTypeDipesMagnetiques.MENSUEL:
				destination = new File(cheminDipeMensuel);
				if (destination.exists())
					destination.delete();
				destination = new File(cheminDipeMensuel2);
				if (destination.exists())
					destination.delete();
				GeneriqueConnexionService._createFileFolder(cheminDipeMensuel, "\\");
				GeneriqueConnexionService._createFileFolder(cheminDipeMensuel2, "\\");
				break;

			case ClsTypeDipesMagnetiques.DEBUT_EXERCICE:
				destination = new File(cheminDipeDebutExercice);
				if (destination.exists())
					destination.delete();
				destination = new File(cheminDipeDebutExercice2);
				if (destination.exists())
					destination.delete();
				GeneriqueConnexionService._createFileFolder(cheminDipeDebutExercice, "\\");
				GeneriqueConnexionService._createFileFolder(cheminDipeDebutExercice2, "\\");
				break;

			case ClsTypeDipesMagnetiques.FIN_EXERCICE:
				destination = new File(cheminDipeFinExercice);
				if (destination.exists())
					destination.delete();
				destination = new File(cheminDipeFinExercice2);
				if (destination.exists())
					destination.delete();
				GeneriqueConnexionService._createFileFolder(cheminDipeFinExercice, "\\");
				GeneriqueConnexionService._createFileFolder(cheminDipeFinExercice2, "\\");
				break;

			case ClsTypeDipesMagnetiques.TEMPORAIRE:
				destination = new File(cheminDipeTemporaire);
				if (destination.exists())
					destination.delete();
				destination = new File(cheminDipeTemporaire2);
				if (destination.exists())
					destination.delete();
				GeneriqueConnexionService._createFileFolder(cheminDipeTemporaire, "\\");
				GeneriqueConnexionService._createFileFolder(cheminDipeTemporaire2, "\\");
				break;

			case ClsTypeDipesMagnetiques.EMBAUCHE:
				destination = new File(cheminDipeEmbauche);
				if (destination.exists())
					destination.delete();
				destination = new File(cheminDipeEmbauche2);
				if (destination.exists())
					destination.delete();
				GeneriqueConnexionService._createFileFolder(cheminDipeEmbauche, "\\");
				GeneriqueConnexionService._createFileFolder(cheminDipeEmbauche2, "\\");
				break;
				
			case ClsTypeDipesMagnetiques.CD10:
				destination = new File(cheminDipeCD10);
				if (destination.exists())
					destination.delete();			
				GeneriqueConnexionService._createFileFolder(cheminDipeCD10, "\\");
				break;

			default:
				break;
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

	}

	public String getCdos()
	{
		return cdos;
	}

	public void setCdos(String cdos)
	{
		this.cdos = cdos;
	}

	public String getClang()
	{
		return clang;
	}

	public void setClang(String clang)
	{
		this.clang = clang;
	}

	public String getAamm()
	{
		return aamm;
	}

	public void setAamm(String aamm)
	{
		this.aamm = aamm;
	}

	public String getVille1()
	{
		return ville1;
	}

	public void setVille1(String ville1)
	{
		this.ville1 = ville1;
	}

	public String getVille2()
	{
		return ville2;
	}

	public void setVille2(String ville2)
	{
		this.ville2 = ville2;
	}

	public int getTypeDipe()
	{
		return typeDipe;
	}

	public void setTypeDipe(int typeDipe)
	{
		this.typeDipe = typeDipe;
	}

	public Date getDdex()
	{
		return ddex;
	}

	public void setDdex(Date ddex)
	{
		this.ddex = ddex;
	}

	public Date getDfex()
	{
		return dfex;
	}

	public void setDfex(Date dfex)
	{
		this.dfex = dfex;
	}

	public String getCheminDipeMensuel()
	{
		return cheminDipeMensuel;
	}

	public void setCheminDipeMensuel(String cheminDipeMensuel)
	{
		this.cheminDipeMensuel = cheminDipeMensuel;
	}

	public String getCheminDipeDebutExercice()
	{
		return cheminDipeDebutExercice;
	}

	public void setCheminDipeDebutExercice(String cheminDipeDebutExercice)
	{
		this.cheminDipeDebutExercice = cheminDipeDebutExercice;
	}

	public String getCheminDipeFinExercice()
	{
		return cheminDipeFinExercice;
	}

	public void setCheminDipeFinExercice(String cheminDipeFinExercice)
	{
		this.cheminDipeFinExercice = cheminDipeFinExercice;
	}

	public String getCheminDipeTemporaire()
	{
		return cheminDipeTemporaire;
	}

	public void setCheminDipeTemporaire(String cheminDipeTemporaire)
	{
		this.cheminDipeTemporaire = cheminDipeTemporaire;
	}

	public String getCheminDipeEmbauche()
	{
		return cheminDipeEmbauche;
	}

	public void setCheminDipeEmbauche(String cheminDipeEmbauche)
	{
		this.cheminDipeEmbauche = cheminDipeEmbauche;
	}

	public BigDecimal getRegCNPS()
	{
		return regCNPS;
	}

	public void setRegCNPS(BigDecimal regCNPS)
	{
		this.regCNPS = regCNPS;
	}

	public String getVille()
	{
		return ville;
	}

	public void setVille(String ville)
	{
		this.ville = ville;
	}

	public String getQuartier()
	{
		return quartier;
	}

	public void setQuartier(String quartier)
	{
		this.quartier = quartier;
	}

	public String getBp()
	{
		return bp;
	}

	public void setBp(String bp)
	{
		this.bp = bp;
	}

	public String getTelephone()
	{
		return telephone;
	}

	public void setTelephone(String telephone)
	{
		this.telephone = telephone;
	}

	public String getNumCompte()
	{
		return numCompte;
	}

	public void setNumCompte(String numCompte)
	{
		this.numCompte = numCompte;
	}

	public BigDecimal getPLAFOND_CNPS()
	{
		return PLAFOND_CNPS;
	}

	public void setPLAFOND_CNPS(BigDecimal plafond_cnps)
	{
		PLAFOND_CNPS = plafond_cnps;
	}

	public BigDecimal getTAUX_PENS_VIEIL()
	{
		return TAUX_PENS_VIEIL;
	}

	public void setTAUX_PENS_VIEIL(BigDecimal taux_pens_vieil)
	{
		TAUX_PENS_VIEIL = taux_pens_vieil;
	}

	public BigDecimal getTAUX_PREST_FAM()
	{
		return TAUX_PREST_FAM;
	}

	public void setTAUX_PREST_FAM(BigDecimal taux_prest_fam)
	{
		TAUX_PREST_FAM = taux_prest_fam;
	}

	public BigDecimal getTAUX_ACC_TRAV()
	{
		return TAUX_ACC_TRAV;
	}

	public void setTAUX_ACC_TRAV(BigDecimal taux_acc_trav)
	{
		TAUX_ACC_TRAV = taux_acc_trav;
	}

	public String getDnii()
	{
		return dnii;
	}

	public void setDnii(String dnii)
	{
		this.dnii = dnii;
	}

	public String getNcontribuable()
	{
		return ncontribuable;
	}

	public void setNcontribuable(String ncontribuable)
	{
		this.ncontribuable = ncontribuable;
	}

	public int getFeuille()
	{
		return feuille;
	}

	public void setFeuille(int feuille)
	{
		this.feuille = feuille;
	}

	public String getAamm_deb()
	{
		return aamm_deb;
	}

	public void setAamm_deb(String aamm_deb)
	{
		this.aamm_deb = aamm_deb;
	}

	public String getAamm_fin()
	{
		return aamm_fin;
	}

	public void setAamm_fin(String aamm_fin)
	{
		this.aamm_fin = aamm_fin;
	}

	public Date getDate()
	{
		return date;
	}

	public void setDate(Date date)
	{
		this.date = date;
	}

	public String getDate_c()
	{
		return date_c;
	}

	public void setDate_c(String date_c)
	{
		this.date_c = date_c;
	}

	public String getAdexe_c()
	{
		return adexe_c;
	}

	public void setAdexe_c(String adexe_c)
	{
		this.adexe_c = adexe_c;
	}

	public String getAfexe_c()
	{
		return afexe_c;
	}

	public void setAfexe_c(String afexe_c)
	{
		this.afexe_c = afexe_c;
	}

	public String getCodenreg()
	{
		return codenreg;
	}

	public void setCodenreg(String codenreg)
	{
		this.codenreg = codenreg;
	}

	public String getAnnee()
	{
		return annee;
	}

	public void setAnnee(String annee)
	{
		this.annee = annee;
	}
}
