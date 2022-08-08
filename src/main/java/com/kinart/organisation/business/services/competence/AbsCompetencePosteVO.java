package com.kinart.organisation.business.services.competence;

public class AbsCompetencePosteVO
{
	// Classe : 8, Filiere:106, Specialite:107, Type formation:77, niv Form:78, Cate-Ech:32, Horaires:110, Dispo:111, Mobilite:112, Contraintes:113
	//
	public static String STR_CODE_TABLE_FORMATION = "115";

	public static String STR_CODE_TABLE_FUNCTION = "7";

	public static String STR_CODE_TABLE_LANGUAGE = "72";

	public static String STR_CODE_TABLE_LEVEL = "73";// Niveau langue.

	public static String STR_CODE_TABLE_THEME = "101";// Savoir faire.

	public static String STR_CODE_TABLE_AREA = "102";

	public static String STR_CODE_TABLE_THEME_LEVEL = "209";//

	public static String STR_CODE_TABLE_DOCUMENT = "71";

	public static String STR_CODE_TABLE_CERTIFICATE = "16";

	public static String STR_CODE_TABLE_TASK = "119";

	public static String STR_CODE_TABLE_NIVEAU_1 = "1";

	public static String STR_CODE_TABLE_NIVEAU_2 = "2";

	public static String STR_CODE_TABLE_NIVEAU_3 = "3";

	public static String STR_CODE_TABLE_AV_NATURE = "36";

	public static String STR_CODE_TABLE_AV_HORS_PAIE = "235";

	public static String STR_CODE_TABLE_SAVOIR_ETRE = "218";

	public static String STR_CODE_TABLE_OBJ_INTERMEDIAIRES = "227";

	public static String STR_CODE_TABLE_STAND_PERFORMANCE = "228";

	public static String STR_CODE_TABLE_KEY_AREA = "226";

	public static String STR_CODE_TABLE_MISSION = "291";

	public static String STR_ABSENCE_REASON = "22";

	public static String STR_CODE_TABLE_FORMATION_THEME = "245";

	public static String STR_CODE_TABLE_SAVOIR_FAIRE_FAIRE = "272";

	public static String STR_TYPE_INFO_FORM = "FORM";

	public static String STR_TYPE_INFO_FUNC = "FUNC";

	public static String STR_TYPE_INFO_LANG = "LANG";

	public static String STR_TYPE_INFO_THEME = "THEME";

	public static String STR_TYPE_INFO_THEME_LEVEL = "TH_LEVEL";

	public static String STR_TYPE_INFO_LEVEL = "LEVEL";

	public static String STR_TYPE_INFO_DOCUMENT = "DOC";

	public static String STR_TYPE_INFO_CERTIFICATE = "DIP";

	public static String STR_TYPE_INFO_TASK = "TASK";

	public static String STR_TYPE_INFO_INTERIM = "INTER";

	public static String STR_TYPE_INFO_AV_NATURE = "AVNAT";

	public static String STR_TYPE_INFO_AV_HORS_PAIE = "AVHPAIE";

	public static String STR_TYPE_INFO_RUBRIQUE = "RUB";

	public static String STR_TYPE_INFO_SAV_ETRE = "SAVETRE";

	public static String STR_TYPE_INFO_KEY_AREA = "KEY_AREA";

	public static String STR_TYPE_INFO_MISSION = "MISSION";

	public static String STR_TYPE_INFO_AREA = "AREA";

	public static String STR_TYPE_INFO_OBJ_INTERMEDIAIRES = "OBJ_INTER";

	public static String STR_TYPE_INFO_STAND_PERFORMANCE = "STAND_PERF";

	public static String STR_TYPE_INFO_NIVEAU_1 = "NIV_1";

	public static String STR_TYPE_INFO_NIVEAU_2 = "NIV_2";

	public static String STR_TYPE_INFO_NIVEAU_3 = "NIV_3";

	public static String STR_TYPE_INFO_ABSENCE_REASON = "ABS_RS";

	public static String STR_TYPE_INFO_POSTE_CLIENT = "CL_POST";

	public static String STR_TYPE_INFO_SAVOIR_FAIRE_FAIRE = "SAVFAIRE";
	
	public static String STR_CODE_TABLE_SAVOIR= "797";
	
	public static String STR_CODE_TABLE_RELATIONS_INSTERNES = "798";
	
	public static String STR_CODE_TABLE_RELATIONS_EXTERNES = "799";

	public SortableListVO<CompetencePosteVO> oformations;

	public SortableListVO<CompetencePosteVO> ofonctions;

	// Data of skill
	public SortableListVO<CompetencePosteVO> olangues;

	// Data of skill
	public SortableListVO<CompetencePosteVO> othemes;

	// Data of skill
	public SortableListVO<CompetencePosteVO> odiplomes;

	// Data of skill
	public SortableListVO<CompetencePosteVO> odocuments;

	public SortableListVO<CompetencePosteVO> otaches;

	public SortableListVO<CompetencePosteVO> ointerims;

	public SortableListVO<CompetencePosteVO> oposteclients;

	public SortableListVO<CompetencePosteVO> oavantagenatures;

	// Data of skill
	public SortableListVO<CompetencePosteVO> oavantagehorspaies;

	public SortableListVO<CompetencePosteVO> orubriques;

	public SortableListVO<CompetencePosteVO> osavoiretres;

	public SortableListVO<CompetencePosteVO> okeyareas;

	public SortableListVO<CompetencePosteVO> omissions;
	
	public SortableListVO<CompetencePosteVO> osavoir;
	
	public SortableListVO<CompetencePosteVO> orelationsinternes;
	
	public SortableListVO<CompetencePosteVO> orelationsexternes;
	
	public SortableListVO<CompetencePosteVO> oplansuccession;
	
	public SortableListVO<CompetencePosteVO> omoyens;

	public SortableListVO<CompetencePosteVO> getOformations()
	{
		return oformations;
	}

	public void setOformations(SortableListVO<CompetencePosteVO> oformations)
	{
		this.oformations = oformations;
	}

	public SortableListVO<CompetencePosteVO> getOfonctions()
	{
		return ofonctions;
	}

	public void setOfonctions(SortableListVO<CompetencePosteVO> ofonctions)
	{
		this.ofonctions = ofonctions;
	}

	public SortableListVO<CompetencePosteVO> getOlangues()
	{
		return olangues;
	}

	public void setOlangues(SortableListVO<CompetencePosteVO> olangues)
	{
		this.olangues = olangues;
	}

	public SortableListVO<CompetencePosteVO> getOthemes()
	{
		return othemes;
	}

	public void setOthemes(SortableListVO<CompetencePosteVO> othemes)
	{
		this.othemes = othemes;
	}

	public SortableListVO<CompetencePosteVO> getOdiplomes()
	{
		return odiplomes;
	}

	public void setOdiplomes(SortableListVO<CompetencePosteVO> odiplomes)
	{
		this.odiplomes = odiplomes;
	}

	public SortableListVO<CompetencePosteVO> getOdocuments()
	{
		return odocuments;
	}

	public void setOdocuments(SortableListVO<CompetencePosteVO> odocuments)
	{
		this.odocuments = odocuments;
	}

	public SortableListVO<CompetencePosteVO> getOtaches()
	{
		return otaches;
	}

	public void setOtaches(SortableListVO<CompetencePosteVO> otaches)
	{
		this.otaches = otaches;
	}

	public SortableListVO<CompetencePosteVO> getOinterims()
	{
		return ointerims;
	}

	public void setOinterims(SortableListVO<CompetencePosteVO> ointerims)
	{
		this.ointerims = ointerims;
	}

	public SortableListVO<CompetencePosteVO> getOposteclients()
	{
		return oposteclients;
	}

	public void setOposteclients(SortableListVO<CompetencePosteVO> oposteclients)
	{
		this.oposteclients = oposteclients;
	}

	public SortableListVO<CompetencePosteVO> getOavantagenatures()
	{
		return oavantagenatures;
	}

	public void setOavantagenatures(SortableListVO<CompetencePosteVO> oavantagenatures)
	{
		this.oavantagenatures = oavantagenatures;
	}

	public SortableListVO<CompetencePosteVO> getOavantagehorspaies()
	{
		return oavantagehorspaies;
	}

	public void setOavantagehorspaies(SortableListVO<CompetencePosteVO> oavantagehorspaies)
	{
		this.oavantagehorspaies = oavantagehorspaies;
	}

	public SortableListVO<CompetencePosteVO> getOrubriques()
	{
		return orubriques;
	}

	public void setOrubriques(SortableListVO<CompetencePosteVO> orubriques)
	{
		this.orubriques = orubriques;
	}

	public SortableListVO<CompetencePosteVO> getOsavoiretres()
	{
		return osavoiretres;
	}

	public void setOsavoiretres(SortableListVO<CompetencePosteVO> osavoiretres)
	{
		this.osavoiretres = osavoiretres;
	}

	public SortableListVO<CompetencePosteVO> getOkeyareas()
	{
		return okeyareas;
	}

	public void setOkeyareas(SortableListVO<CompetencePosteVO> okeyareas)
	{
		this.okeyareas = okeyareas;
	}

	public SortableListVO<CompetencePosteVO> getOmissions()
	{
		return omissions;
	}

	public void setOmissions(SortableListVO<CompetencePosteVO> omissions)
	{
		this.omissions = omissions;
	}

	public SortableListVO<CompetencePosteVO> getOsavoir() {
		return osavoir;
	}

	public void setOsavoir(SortableListVO<CompetencePosteVO> osavoir) {
		this.osavoir = osavoir;
	}

	public SortableListVO<CompetencePosteVO> getOrelationsinternes() {
		return orelationsinternes;
	}

	public void setOrelationsinternes(
			SortableListVO<CompetencePosteVO> orelationsinternes) {
		this.orelationsinternes = orelationsinternes;
	}

	public SortableListVO<CompetencePosteVO> getOrelationsexternes() {
		return orelationsexternes;
	}

	public void setOrelationsexternes(
			SortableListVO<CompetencePosteVO> orelationsexternes) {
		this.orelationsexternes = orelationsexternes;
	}

	public SortableListVO<CompetencePosteVO> getOplansuccession() {
		return oplansuccession;
	}

	public void setOplansuccession(SortableListVO<CompetencePosteVO> oplansuccession) {
		this.oplansuccession = oplansuccession;
	}

	public SortableListVO<CompetencePosteVO> getOmoyens() {
		return omoyens;
	}

	public void setOmoyens(SortableListVO<CompetencePosteVO> omoyens) {
		this.omoyens = omoyens;
	}
	
}
