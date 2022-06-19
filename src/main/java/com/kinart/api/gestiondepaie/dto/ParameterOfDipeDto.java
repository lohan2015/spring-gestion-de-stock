package com.kinart.api.gestiondepaie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ParameterOfDipeDto
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
}
