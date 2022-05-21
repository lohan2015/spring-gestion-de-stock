package com.kinart.paie.business.services.calcul;

import com.kinart.paie.business.model.CalculPaie;
import com.kinart.paie.business.model.DossierPaie;
import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.model.VirementSalarie;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.StringUtil;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class ClsCalculLancement  implements Serializable{
	
	private String datecomptable;
	private String typebulletin;
	private String typeopeaction;
	private String valtypeopeaction;
	private String classesalariemin;
	private String classesalariemax;
	private String categoriemin;
	private String categoriemax;
	private String valeurclassesalarie;
	private String modepaiement;
	private String valeurmodepaiement;
	private String periodepaie;
	private String numerobulletin;
	
	private String niveau1depart;
	private String valeurniveau1depart;
	
	private String niveau1arrive;
	private String valeurniveau1arrive;
	
	private String niveau2depart;
	private String valeurniveau2depart;
	
	private String niveau2arrive;
	private String valeurniveau2arrive;
	
	private String niveau3depart;
	private String valeurniveau3depart;
	
	private String niveau3arrive;
	private String valeurniveau3arrive;
	
	
	private String matriculedepart;
	private String valeurmatriculedepart;
	
	private String matriculearrive;
	private String valeurmatriculearrive;
	
	private String rubriquedepart;
	private String valeurrubriquedepart;
	
	private String rubriquearrive;
	private String valeurrubriquearrive;
	
	
	private String intervalle;
	private String valintervalle;
	private String matricule;
	
	private String valmatricule;
	private List<Salarie> listeAgents;
	private String listematricules = "'X'";
	private DossierPaie infodossier;
	private String periodecumul;
	
	public String cals="a.cals='O'";
	private String valeurCals="'O'";
	private String partialQuery;
	
	public String getValeurCals() {
		return valeurCals;
	}

	public void setValeurCals(String valeurCals) {
		this.valeurCals = valeurCals;
		this.cals="a.cals in ("+this.valeurCals+")";
	}

	public DossierPaie getInfodossier()
	{
		return infodossier;
	}

	public void setInfodossier(DossierPaie infodossier)
	{
		this.infodossier = infodossier;
	}

	public String getClassesalariemin() {
		return classesalariemin;
	}

	public void setClassesalariemin(String classesalariemin) {
		this.classesalariemin = classesalariemin;
	}

	public String getClassesalariemax() {
		return classesalariemax;
	}

	public void setClassesalariemax(String classesalariemax) {
		this.classesalariemax = classesalariemax;
	}

	public String getCategoriemin() {
		return categoriemin;
	}

	public void setCategoriemin(String categoriemin) {
		this.categoriemin = categoriemin;
	}

	public String getCategoriemax() {
		return categoriemax;
	}

	public void setCategoriemax(String categoriemax) {
		this.categoriemax = categoriemax;
	}

	public ClsCalculLancement()
	{
		infodossier = new DossierPaie();
		setListeAgents(new ArrayList<Salarie>());
	}

	public String getDatecomptable() {
		return datecomptable;
	}

	public void setDatecomptable(String datecomptable) {
		this.datecomptable = datecomptable;
	}

	public String getIntervalle() {
		return intervalle;
	}

	public void setIntervalle(String intervalle) {
		this.intervalle = intervalle;
	}

	public List<Salarie> getListeAgents() {
		return listeAgents;
	}

	public void setListeAgents(List<Salarie> listeAgents) {
		this.listeAgents = listeAgents;
	}

	public String getMatricule() {
		return matricule;
	}

	public void setMatricule(String matricule) {
		this.matricule = matricule;
	}

	public String getMatriculearrive() {
		return matriculearrive;
	}

	public void setMatriculearrive(String matriculearrive) {
		this.matriculearrive = matriculearrive;
	}

	public String getMatriculedepart() {
		return matriculedepart;
	}

	public void setMatriculedepart(String matriculedepart) {
		this.matriculedepart = matriculedepart;
	}

	public String getModepaiement() {
		return modepaiement;
	}

	public void setModepaiement(String modepaiement) {
		this.modepaiement = modepaiement;
	}

	public String getNiveau1arrive() {
		return niveau1arrive;
	}

	public void setNiveau1arrive(String niveau1arrive) {
		this.niveau1arrive = niveau1arrive;
	}

	public String getNiveau1depart() {
		return niveau1depart;
	}

	public void setNiveau1depart(String niveau1depart) {
		this.niveau1depart = niveau1depart;
	}

	public String getNiveau2arrive() {
		return niveau2arrive;
	}

	public void setNiveau2arrive(String niveau2arrive) {
		this.niveau2arrive = niveau2arrive;
	}

	public String getNiveau2depart() {
		return niveau2depart;
	}

	public void setNiveau2depart(String niveau2depart) {
		this.niveau2depart = niveau2depart;
	}

	public String getNiveau3arrive() {
		return niveau3arrive;
	}

	public void setNiveau3arrive(String niveau3arrive) {
		this.niveau3arrive = niveau3arrive;
	}

	public String getNiveau3depart() {
		return niveau3depart;
	}

	public void setNiveau3depart(String niveau3depart) {
		this.niveau3depart = niveau3depart;
	}

	public String getNumerobulletin() {
		return numerobulletin;
	}

	public void setNumerobulletin(String numerobulletin) {
		this.numerobulletin = numerobulletin;
	}

	public String getPeriodepaie() {
		return periodepaie;
	}

	public void setPeriodepaie(String periodepaie) {
		this.periodepaie = periodepaie;
	}

	public String getValeurclassesalarie() {
		return valeurclassesalarie;
	}

	public void setValeurclassesalarie(String valeurclassesalarie) {
		this.valeurclassesalarie = valeurclassesalarie;
	}

	public String getValeurmatriculearrive() {
		return valeurmatriculearrive;
	}

	public void setValeurmatriculearrive(String valeurmatriculearrive) {
		this.valeurmatriculearrive = valeurmatriculearrive;
	}

	public String getValeurmatriculedepart() {
		return valeurmatriculedepart;
	}

	public void setValeurmatriculedepart(String valeurmatriculedepart) {
		this.valeurmatriculedepart = valeurmatriculedepart;
	}

	public String getValeurmodepaiement() {
		return valeurmodepaiement;
	}

	public void setValeurmodepaiement(String valeurmodepaiement) {
		this.valeurmodepaiement = valeurmodepaiement;
	}

	public String getValeurniveau1arrive() {
		return valeurniveau1arrive;
	}

	public void setValeurniveau1arrive(String valeurniveau1arrive) {
		this.valeurniveau1arrive = valeurniveau1arrive;
	}

	public String getValeurniveau1depart() {
		return valeurniveau1depart;
	}

	public void setValeurniveau1depart(String valeurniveau1depart) {
		this.valeurniveau1depart = valeurniveau1depart;
	}

	public String getValeurniveau2arrive() {
		return valeurniveau2arrive;
	}

	public void setValeurniveau2arrive(String valeurniveau2arrive) {
		this.valeurniveau2arrive = valeurniveau2arrive;
	}

	public String getValeurniveau2depart() {
		return valeurniveau2depart;
	}

	public void setValeurniveau2depart(String valeurniveau2depart) {
		this.valeurniveau2depart = valeurniveau2depart;
	}

	public String getValeurniveau3arrive() {
		return valeurniveau3arrive;
	}

	public void setValeurniveau3arrive(String valeurniveau3arrive) {
		this.valeurniveau3arrive = valeurniveau3arrive;
	}

	public String getValeurniveau3depart() {
		return valeurniveau3depart;
	}

	public void setValeurniveau3depart(String valeurniveau3depart) {
		this.valeurniveau3depart = valeurniveau3depart;
	}

	public String getValintervalle() {
		return valintervalle;
	}

	public void setValintervalle(String valintervalle) {
		this.valintervalle = valintervalle;
	}

	public String getValmatricule() {
		return valmatricule;
	}

	public void setValmatricule(String valmatricule) {
		this.valmatricule = valmatricule;
	}

	public String getTypeopeaction() {
		return typeopeaction;
	}

	public void setTypeopeaction(String typeopeaction) {
		this.typeopeaction = typeopeaction;
	}

	public String getValtypeopeaction() {
		return valtypeopeaction;
	}

	public void setValtypeopeaction(String valtypeopeaction) {
		this.valtypeopeaction = valtypeopeaction;
	}

	public String getTypebulletin() {
		return typebulletin;
	}

	public void setTypebulletin(String typebulletin) {
		this.typebulletin = typebulletin;
	}
	GeneriqueConnexionService service;
	
	public List<Salarie> ListePlusAgent (ClsCalculLancement oCalcul,String cdos, String utilsateur,String activerSecuriteSalarie){
		String query = getFirstQuery(oCalcul, cdos, utilsateur, activerSecuriteSalarie);

		String listeMatricule="('0'";
		Salarie agentcourant = null;
		for (int i=0; i < oCalcul.getListeAgents().size();i++){
			agentcourant = oCalcul.getListeAgents().get(i);
			listeMatricule =listeMatricule +",'" + agentcourant.getNmat() +"'";
		}
		listeMatricule +=","+ listematricules;
		listeMatricule=listeMatricule +")";
		
		String queryListe = "";
		queryListe = " OR ((a.identreprise=" + "'" + cdos 
		+ "') AND (a.nmat in " + listeMatricule + "))";
		
		String requete = query + queryListe;	
		requete += StringUtils.isNotEmpty(partialQuery)?partialQuery:StringUtils.EMPTY;
		
		List<Salarie> Agents = this.getService().find(requete);

		return Agents; 
	}
	
	public List<Salarie> ListeMoinsAgent (ClsCalculLancement oCalcul,String cdos, String utilsateur,String activerSecuriteSalarie){
//		liste des matricules
		String query = getFirstQuery(oCalcul, cdos, utilsateur, activerSecuriteSalarie);
		//ClsParameter.println("requete first part  : "+ query);
		
		String listeMatricule="('0'";
		for (int i=0; i < oCalcul.getListeAgents().size();i++){
			Salarie agentcourant = oCalcul.getListeAgents().get(i);
			listeMatricule = listeMatricule + ",'" + agentcourant.getNmat() +"'";					
		}
		listeMatricule +=","+ listematricules;
		listeMatricule=listeMatricule +")";
		
		String queryListe = "";
		queryListe = " AND ((a.identreprise=" + "'" + cdos 
		+ "') AND (a.nmat not in " + listeMatricule + "))";
		
		
		String requete = query + queryListe;	
		requete += StringUtils.isNotEmpty(partialQuery)?partialQuery:StringUtils.EMPTY;
		List<Salarie> Agents = this.getService().find(requete);	

		return Agents;
	} 
	
public List<Salarie> ListeIntervalleAgent (ClsCalculLancement oCalcul,String cdos, String utilsateur, String activerSecuriteSalarie){
	String requete = getFirstQuery(oCalcul, cdos, utilsateur, activerSecuriteSalarie);

	//
	requete += StringUtils.isNotEmpty(partialQuery)?partialQuery:StringUtils.EMPTY;
	List<Salarie> Agents = this.getService().find(requete);	

	return Agents;
} 
	
	public List<Salarie> ListeListeAgent (ClsCalculLancement oCalcul,String cdos, String utilsateur, String activerSecuriteSalarie){
		
//		ClsAutorisationsUtilisateur auto = new ClsAutorisationsUtilisateur(	cdos, utilsateur, "b", ClsTypeProfilAgent.COLONNES,this.getService(),activerSecuriteSalarie);
//		auto.setUseHibernate(true);
//		auto.setLibelleColonneMatricule("nmat");
//		//	liste des matricules
//		String strAuto =  auto.getAutorisations();
//		ClsParameter.println("Autorisations = "+strAuto);
		String listeMatricule="('0'";
		for (int i=0; i < oCalcul.getListeAgents().size();i++){
			Salarie agentcourant = oCalcul.getListeAgents().get(i);
			listeMatricule =listeMatricule +",'" + agentcourant.getNmat() +"'";					
		}
		listeMatricule +=","+ listematricules;
		listeMatricule = listeMatricule +")";
		String queryListe = " and (a.nmat in " + listeMatricule + ")";
		String query = "From Salarie a where ( a.identreprise = '" + cdos + "' and "+cals+" and a.nmat in (select b.nmat from Salarie b where b.identreprise = '" + cdos + "')";
		query = query + " " + queryListe + ")";
		query += StringUtils.isNotEmpty(partialQuery)?partialQuery:StringUtils.EMPTY;
		//ClsParameter.println(">> query: " + query);
		List<Salarie> Agents = this.getService().find(query);	
	
		return Agents;
	}
	
	public void supprimerBulletinAgent( Salarie agent, String AnneeMois , String cdos) {
	//	traitement de suppression de bulletin;
		
//		r�cup�ration des rubriques
		String query="";		
		String matricule = agent.getNmat();
		
//		modification des rubriques du bulletin
		List<VirementSalarie> listeRubriques = new ArrayList<VirementSalarie>();
		query="From VirementSalarie where (nmat="+"'"+ matricule +"') AND (identreprise="+"'"+cdos+"') AND (AAMM="+"'"+AnneeMois+"')";
		//ClsParameter.println("requete de modification: " + query);
		listeRubriques=this.getService().find(query);
		for (int j =0; j < listeRubriques.size(); j++){
			VirementSalarie rub = listeRubriques.get(j);
			rub.setMntdb(null);
			rub.setMntdvd(null);
			rub.setAamm(null);
			this.getService().update(rub);
		}
		
//		suppression dans pacalc
		String requetedel= "";
		requetedel = "From CalculPaie where (nmat="+"'"+ matricule +"') AND (identreprise="+"'"+cdos+"') AND (AAMM="+"'"+AnneeMois+"')";
		//ClsParameter.println("requete de suppressiohn : " + requetedel);
		List<CalculPaie> lister = new ArrayList<CalculPaie>();
		lister=this.getService().find(requetedel);
		for (int k =0; k < lister.size(); k++){
			CalculPaie rubc = lister.get(k);
			this.getService().delete(rubc);
		}
			
	
	}
	
	public void annulationBulletinAgent( Salarie agent, String AnneeMois) {
		//	traitement de suppression de l'agent;
		//ClsParameter.println(" annulation bulletin agent" + agent.getNom());
		}
	
	public void blocageBulletinAgent( Salarie agent, String AnneeMois) {
		//	traitement de suppression de l'agent;
		//ClsParameter.println(" blocage bulletin agent" + agent.getNom());
		}
	
	public void lancementCalculBulletinAgent( Salarie agent, String AnneeMois) {
		//	traitement de suppression de l'agent;
		//ClsParameter.println(" lancement calcul bulletin agent" + agent.getNom());
		}
	
	public GeneriqueConnexionService getService() {
		return service;
	}

	public void setService(GeneriqueConnexionService service) {
		this.service = service;
	}
	
	private String getFirstQuery(ClsCalculLancement oCalcul,String cdos, String utilsateur, String activerSecuriteSalarie){

//		ClsAutorisationsUtilisateur auto = new ClsAutorisationsUtilisateur(	cdos, utilsateur, "b",ClsTypeProfilAgent.COLONNES, this.getService(), activerSecuriteSalarie);
////		String chaineauto = auto.getMatricules();
//		auto.setUseHibernate(true);
//		auto.setLibelleColonneMatricule("nmat");
		
		// requete pour la r�cup�ration des agents
		String query = "";
		
		String niveau1depart = oCalcul.getNiveau1depart();
		
		String niveau2depart = oCalcul.getNiveau2depart();
		
		String niveau3depart = oCalcul.getNiveau3depart();
		
		String matriculedepart = oCalcul.getMatriculedepart();
		

		String niveau1arrive = oCalcul.getNiveau1arrive();
		
		String niveau2arrive = oCalcul.getNiveau2arrive();
		
		String niveau3arrive = oCalcul.getNiveau3arrive();
	
		String matriculearrive = oCalcul.getMatriculearrive();
		
		
//		***********les champs optionnnelle *************************
		
//		les champs optionnelles
		
		String classesalariemin = oCalcul.getClassesalariemin();
		
		String classesalariemax = oCalcul.getClassesalariemax();

		String modepaiementmin = oCalcul.getModepaiement();

		String modepaiementmax = oCalcul.getModepaiement();
		
		// requete pour la r�cup�ration des agents
		
		query = "From Salarie a where ((a.identreprise=" + "'" + cdos + "') and ("+cals+") " ;
		
		if(! StringUtils.isEmpty(oCalcul.getMatriculedepart()))
			query+=" AND (a.nmat >=" + "'" + matriculedepart + "')";
		
		if(! StringUtils.isEmpty(oCalcul.getMatriculearrive()))
			query+=" AND ( a.nmat <=" + "'" + matriculearrive + "')";
		
		if(! StringUtils.isEmpty(oCalcul.getNiveau1depart()))
			query += " AND (a.niv1>= '" + niveau1depart + "')";
		
		if(! StringUtils.isEmpty(oCalcul.getNiveau1arrive()))
			query += "AND (a.niv1 <= '" + niveau1arrive + "')";
		
		if(! StringUtils.isEmpty(oCalcul.getNiveau2depart()))
			query +=" AND (a.niv2>= '" + niveau2depart + "')";
		
		if(! StringUtils.isEmpty(oCalcul.getNiveau2arrive()))
			query +=" AND (a.niv2 <= '" + niveau2arrive + "')";
		
		if(! StringUtils.isEmpty(oCalcul.getNiveau3depart()))
			query +=" AND (a.niv3>= '" + niveau3depart + "')";
		
		if(! StringUtils.isEmpty(oCalcul.getNiveau3arrive()))
			query +=" AND (a.niv3 <= '" + niveau3arrive + "')";
		
		if(! StringUtils.isEmpty(oCalcul.getCategoriemin()))
			query +=" AND (a.cat>= '" + oCalcul.getCategoriemin() + "')";
		
		if(! StringUtils.isEmpty(oCalcul.getCategoriemax()))
			query +=" AND (a.cat <= '" + oCalcul.getCategoriemax() + "')";
		
		if(! StringUtils.isEmpty(oCalcul.getClassesalariemin()))
			query +=" AND (a.clas >= '" + classesalariemin + "')";
		
		if(! StringUtils.isEmpty(oCalcul.getClassesalariemax()))
			query +=" AND (a.clas <= '" + oCalcul.getClassesalariemax() + "')";
		
		if(! StringUtils.isEmpty(oCalcul.getModepaiement()))
			query +=" AND (a.modp= '" + modepaiementmin + "')";
				
		String requete = query + " and a.nmat in (select b.nmat from Salarie b where b.identreprise = '" + cdos + "'))";
		
		//
		return requete;
	}

	public String getPeriodecumul()
	{
		return periodecumul;
	}

	public void setPeriodecumul(String periodecumul)
	{
		this.periodecumul = periodecumul;
	}

	public String getListematricules()
	{
		return listematricules;
	}

	public void setListematricules(String listematricules)
	{
		this.listematricules = listematricules;
	}

	public  String getRubriquedepart()
	{
		return rubriquedepart;
	}

	public  void setRubriquedepart(String rubriquedepart)
	{
		this.rubriquedepart = rubriquedepart;
	}

	public  String getValeurrubriquedepart()
	{
		return valeurrubriquedepart;
	}

	public  void setValeurrubriquedepart(String valeurrubriquedepart)
	{
		this.valeurrubriquedepart = valeurrubriquedepart;
	}

	public  String getRubriquearrive()
	{
		return rubriquearrive;
	}

	public  void setRubriquearrive(String rubriquearrive)
	{
		this.rubriquearrive = rubriquearrive;
	}

	public  String getValeurrubriquearrive()
	{
		return valeurrubriquearrive;
	}

	public  void setValeurrubriquearrive(String valeurrubriquearrive)
	{
		this.valeurrubriquearrive = valeurrubriquearrive;
	}

	public String getPartialQuery()
	{
		return partialQuery;
	}

	public void setPartialQuery(String partialQuery)
	{
		this.partialQuery = partialQuery;
	}
	
	
	
}
