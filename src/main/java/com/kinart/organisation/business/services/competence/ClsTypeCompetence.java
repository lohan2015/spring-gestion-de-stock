package com.kinart.organisation.business.services.competence;

import com.kinart.paie.business.services.cloture.ClsNomenclature;
import org.apache.commons.lang3.StringUtils;

public class ClsTypeCompetence
{

	public static final String FORMATION = "FORM";

	public static final String FONCTION = "FUNC";

	public static final String LANGUE = "LANG";

	public static final String SAVOIR_FAIRE = "THEME";

	public static final String SAVOIR_FAIRE_FAIRE = "SAVFAIRE";

	public static final String DOCUMENT = "DOC";

	public static final String DIPLOME = "DIP";

	public static final String TACHE = "TASK";

	public static final String INTERIM_POSTE = "INTER";

	public static final String AVANTAGE_NATURE = "AVNAT";

	public static final String AVANTAGE_HORS_PAIE = "AVHPAIE";

	public static final String RUBRIQUE = "RUB";

	public static final String SAVOIR_ETRE = "SAVETRE";

	public static final String DOMAINE_CLE = "KEY_AREA";

	public static String CLIENT_POSTE = "CL_POST";

	public static String MISSION = "MISSION";
	
	public static final String FAMILLE      = "FAMILY";
	
	public static final String SAVOIR = "SAV";
	
	public static final String RELATIONS_INTERNES = "RELINT";
	
	public static final String RELATIONS_EXTERNES = "RELEXT";
	
	public static final String PLAN_SUCCESSION = "PLASUC";
	
	public static final String MOYEN_NECCESSAIRE = "MOYNEC";
	
	public static String getTableCodeFromInformationType(String typeInformation)
	{

		if (FORMATION.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.FORMATION;
		if (FONCTION.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.FONCTION;
		if (DIPLOME.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.DIPLOME;
		if (DOCUMENT.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.NATURE_DOCUMENT;
		if (LANGUE.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.LANGUE;
		if (SAVOIR_FAIRE.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.THEME;
		if (TACHE.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.TACHE;
		if (AVANTAGE_NATURE.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.AVANTAGE_EN_NATURE;
		if (AVANTAGE_HORS_PAIE.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.AVANTAGE_HORS_PAIE;
		if (SAVOIR_ETRE.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.SAVOIR_ETRE;
		if (DOMAINE_CLE.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.DOMAINE_CLE;
		if (MISSION.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.MISSION;
		if (SAVOIR.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.SAVOIR;
		if (RELATIONS_INTERNES.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.RELATIONS_INTERNES;
		if (RELATIONS_EXTERNES.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.RELATIONS_EXTERNES;
		if (MOYEN_NECCESSAIRE.equalsIgnoreCase(typeInformation))
			return "294";
		return null;
	}

	public static String getTableCodeFromInformationType2(String typeInformation)
	{

		if (FONCTION.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.TYPE_FONCTION;
		if (LANGUE.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.NIVEAUX_LANGUE;
		if (SAVOIR_FAIRE.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.DOMAINE_COMPETENCE;
		if (INTERIM_POSTE.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.DISPONIBILITE;
		if (MOYEN_NECCESSAIRE.equalsIgnoreCase(typeInformation))
			return "294";
		return null;
	}

	public static String getTableCodeFromInformationType3(String typeInformation)
	{

		if (SAVOIR_FAIRE.equalsIgnoreCase(typeInformation))
			return ClsNomenclature.NIVEAU_THEME;
		return null;
	}

	public static String getTypeInfo(String ctab)
	{

		if (StringUtils.equals(ctab, ClsNomenclature.FORMATION))
			return FORMATION;
		if (StringUtils.equals(ctab, ClsNomenclature.FONCTION))
			return FONCTION;
		if (StringUtils.equals(ctab, ClsNomenclature.DIPLOME))
			return DIPLOME;
		if (StringUtils.equals(ctab, ClsNomenclature.NATURE_DOCUMENT))
			return DOCUMENT;
		if (StringUtils.equals(ctab, ClsNomenclature.LANGUE))
			return LANGUE;
		if (StringUtils.equals(ctab, ClsNomenclature.THEME))
			return SAVOIR_FAIRE;
		if (StringUtils.equals(ctab, ClsNomenclature.TACHE))
			return TACHE;
		if (StringUtils.equals(ctab, ClsNomenclature.AVANTAGE_EN_NATURE))
			return AVANTAGE_NATURE;
		if (StringUtils.equals(ctab, ClsNomenclature.AVANTAGE_HORS_PAIE))
			return AVANTAGE_HORS_PAIE;
		if (StringUtils.equals(ctab, ClsNomenclature.SAVOIR_ETRE))
			return SAVOIR_ETRE;
		if (StringUtils.equals(ctab, ClsNomenclature.DOMAINE_CLE))
			return DOMAINE_CLE;
		if (StringUtils.equals(ctab, ClsNomenclature.MISSION))
			return MISSION;
		if (StringUtils.equals(ctab, ClsNomenclature.OBJECTIF_INTERMEDIAIRE))
			return DOMAINE_CLE;
		if (StringUtils.equals(ctab, "294"))
			return MOYEN_NECCESSAIRE;
		return null;
	}

}
