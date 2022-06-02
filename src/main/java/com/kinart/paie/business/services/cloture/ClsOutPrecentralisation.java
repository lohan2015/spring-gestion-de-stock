package com.kinart.paie.business.services.cloture;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.services.utils.ClsResultat;
import com.kinart.paie.business.services.utils.ParameterUtil;
import com.kinart.paie.business.services.utils.StringUtil;
import org.springframework.util.StringUtils;

public class ClsOutPrecentralisation
{
	public String cdos;

	public String aamm;

	public String nbul;

	public String cuti;

	public Integer nddd;

	public Date date_comptable;

	public DateFormat dateFormat;

	public String clang;

	public String comp;

	public boolean deltacom;

	public boolean centra_dos;
	
	public String v_dos;

	public String numdos_centra;

	public String Num_Piece;

	public String Libelle_Ecriture;

	public String Rbq_NAP;

	public String Rbq_BRUT;

	public String Code_Journal;

	public String Code_Etb_Commun;
	
	public String Code_Etablissement;

	public String Dest2_Commune;

	public String Libelle_Abrege;
	
	public String Devise_Dossier;
	
	public Integer NbreBul;
	public Integer NbreRubCompta;
	public Integer NbreBulNonEdite;


	public boolean interface_gl;

	public Integer num_tab_gl;

	public String cle_tab_gl;

	public String[] destination_gl = new String[] { null, null, null, null, null, null, null, null, null };

	public HttpServletRequest request;
	
	public String uniquementsierreur="N";

	public List<ClsLog> log = new ArrayList<ClsLog>();

	public ClsResultat message;
	
	public boolean verifok = true;
	
	public List<Salarie> listeSalaries = new ArrayList<Salarie>();
	
	public List<ClsSalarieCentralisation> listeSalariesCentralisation = new ArrayList<ClsSalarieCentralisation>();
	
	public List<Salarie> ListeSalarieNationaliteInexistante = new ArrayList<Salarie>();
	
	public List<Salarie> ListeSalarieGradeInexistant = new ArrayList<Salarie>();
	
	public ClsSalarieCentralisation salarie;
	
	public char generationFichiers;
	
	public String dossierGenerationFichiers = null;
	
	public void printOutPrecentralisation()
	{
		ParameterUtil.println("---------------Ecriture dans le fichier ");
		if(generationFichiers == 'O')
		{
			StringUtil.printOutObject(StringUtil.toString(this), dossierGenerationFichiers+"\\centralisation\\paramtrage.sql");
			
			for (ClsSalarieCentralisation sal : listeSalariesCentralisation)
			{
				StringUtil.printOutObject(StringUtil.toString(sal), dossierGenerationFichiers+"\\centralisation\\"+sal.getNmat()+".sql");
			}
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

	public String getAamm()
	{
		return aamm;
	}

	public void setAamm(String aamm)
	{
		this.aamm = aamm;
	}

	public String getNbul()
	{
		return nbul;
	}

	public void setNbul(String nbul)
	{
		this.nbul = nbul;
	}

	public String getCuti()
	{
		return cuti;
	}

	public void setCuti(String cuti)
	{
		this.cuti = cuti;
	}

	public Integer getNddd()
	{
		return nddd;
	}

	public void setNddd(Integer nddd)
	{
		this.nddd = nddd;
	}

	public Date getDate_comptable()
	{
		return date_comptable;
	}

	public void setDate_comptable(Date date_comptable)
	{
		this.date_comptable = date_comptable;
	}

	public DateFormat getDateFormat()
	{
		return dateFormat;
	}

	public void setDateFormat(DateFormat dateFormat)
	{
		this.dateFormat = dateFormat;
	}

	public String getClang()
	{
		return clang;
	}

	public void setClang(String clang)
	{
		this.clang = clang;
	}

	public String getComp()
	{
		return comp;
	}

	public void setComp(String comp)
	{
		this.comp = comp;
	}

	public boolean isDeltacom()
	{
		return deltacom;
	}

	public void setDeltacom(boolean deltacom)
	{
		this.deltacom = deltacom;
	}

	public boolean isCentra_dos()
	{
		return centra_dos;
	}

	public void setCentra_dos(boolean centra_dos)
	{
		this.centra_dos = centra_dos;
	}

	public String getV_dos()
	{
		return v_dos;
	}

	public void setV_dos(String v_dos)
	{
		this.v_dos = v_dos;
	}

	public String getNumdos_centra()
	{
		return numdos_centra;
	}

	public void setNumdos_centra(String numdos_centra)
	{
		this.numdos_centra = numdos_centra;
	}

	public String getNum_Piece()
	{
		return Num_Piece;
	}

	public void setNum_Piece(String num_Piece)
	{
		Num_Piece = num_Piece;
	}

	public String getLibelle_Ecriture()
	{
		return Libelle_Ecriture;
	}

	public void setLibelle_Ecriture(String libelle_Ecriture)
	{
		Libelle_Ecriture = libelle_Ecriture;
	}

	public String getRbq_NAP()
	{
		return Rbq_NAP;
	}

	public void setRbq_NAP(String rbq_NAP)
	{
		Rbq_NAP = rbq_NAP;
	}

	public String getRbq_BRUT()
	{
		return Rbq_BRUT;
	}

	public void setRbq_BRUT(String rbq_BRUT)
	{
		Rbq_BRUT = rbq_BRUT;
	}

	public String getCode_Journal()
	{
		return Code_Journal;
	}

	public void setCode_Journal(String code_Journal)
	{
		Code_Journal = code_Journal;
	}

	public String getCode_Etb_Commun()
	{
		return Code_Etb_Commun;
	}

	public void setCode_Etb_Commun(String code_Etb_Commun)
	{
		Code_Etb_Commun = code_Etb_Commun;
	}

	public String getCode_Etablissement()
	{
		return Code_Etablissement;
	}

	public void setCode_Etablissement(String code_Etablissement)
	{
		Code_Etablissement = code_Etablissement;
	}

	public String getDest2_Commune()
	{
		return Dest2_Commune;
	}

	public void setDest2_Commune(String dest2_Commune)
	{
		Dest2_Commune = dest2_Commune;
	}

	public String getLibelle_Abrege()
	{
		return Libelle_Abrege;
	}

	public void setLibelle_Abrege(String libelle_Abrege)
	{
		Libelle_Abrege = libelle_Abrege;
	}

	public String getDevise_Dossier()
	{
		return Devise_Dossier;
	}

	public void setDevise_Dossier(String devise_Dossier)
	{
		Devise_Dossier = devise_Dossier;
	}

	public Integer getNbreBul()
	{
		return NbreBul;
	}

	public void setNbreBul(Integer nbreBul)
	{
		NbreBul = nbreBul;
	}

	public Integer getNbreRubCompta()
	{
		return NbreRubCompta;
	}

	public void setNbreRubCompta(Integer nbreRubCompta)
	{
		NbreRubCompta = nbreRubCompta;
	}

	public Integer getNbreBulNonEdite()
	{
		return NbreBulNonEdite;
	}

	public void setNbreBulNonEdite(Integer nbreBulNonEdite)
	{
		NbreBulNonEdite = nbreBulNonEdite;
	}

	public boolean isInterface_gl()
	{
		return interface_gl;
	}

	public void setInterface_gl(boolean interface_gl)
	{
		this.interface_gl = interface_gl;
	}

	public Integer getNum_tab_gl()
	{
		return num_tab_gl;
	}

	public void setNum_tab_gl(Integer num_tab_gl)
	{
		this.num_tab_gl = num_tab_gl;
	}

	public String getCle_tab_gl()
	{
		return cle_tab_gl;
	}

	public void setCle_tab_gl(String cle_tab_gl)
	{
		this.cle_tab_gl = cle_tab_gl;
	}

	public String[] getDestination_gl()
	{
		return destination_gl;
	}

	public void setDestination_gl(String[] destination_gl)
	{
		this.destination_gl = destination_gl;
	}

	public String getUniquementsierreur()
	{
		return uniquementsierreur;
	}

	public void setUniquementsierreur(String uniquementsierreur)
	{
		this.uniquementsierreur = uniquementsierreur;
	}

	public List<ClsLog> getLog()
	{
		return log;
	}

	public void setLog(List<ClsLog> log)
	{
		this.log = log;
	}

	public ClsResultat getMessage()
	{
		return message;
	}

	public void setMessage(ClsResultat message)
	{
		this.message = message;
	}

	public boolean isVerifok()
	{
		return verifok;
	}

	public void setVerifok(boolean verifok)
	{
		this.verifok = verifok;
	}

	public List<Salarie> getListeSalarieNationaliteInexistante()
	{
		return ListeSalarieNationaliteInexistante;
	}

	public void setListeSalarieNationaliteInexistante(List<Salarie> listeSalarieNationaliteInexistante)
	{
		ListeSalarieNationaliteInexistante = listeSalarieNationaliteInexistante;
	}

	public List<Salarie> getListeSalarieGradeInexistant()
	{
		return ListeSalarieGradeInexistant;
	}

	public void setListeSalarieGradeInexistant(List<Salarie> listeSalarieGradeInexistant)
	{
		ListeSalarieGradeInexistant = listeSalarieGradeInexistant;
	}
}
