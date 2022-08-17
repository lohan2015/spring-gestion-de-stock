package com.kinart.organisation.business.services.competence;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.kinart.paie.business.services.cloture.ClsNomenclature;
import org.hibernate.Session;


@SuppressWarnings("serial")
public class ComputeCompetencePosteVO implements Serializable
{

	CompetencesPosteVO comp;

	public ComputeCompetencePosteVO(CompetencesPosteVO competences)
	{

		this.comp = competences;

	}

	public void computeAllCompetences()
	{
		List<CompetencePosteVO> liste = this._loadInformationsMap(_getGenericQueryString("formation", ClsTypeCompetence.FORMATION, ClsNomenclature.FORMATION));
		comp.setOformations(comp.getSortableList(liste));

		liste = this._loadInformationsMap(_getFonctionQueryString());
		comp.setOfonctions(comp.getSortableList(liste));

		liste = this._loadInformationsMap(_getGenericQueryString("diplome", ClsTypeCompetence.DIPLOME, ClsNomenclature.DIPLOME));
		comp.setOdiplomes(comp.getSortableList(liste));

		liste = this._loadInformationsMap(_getGenericQueryString("documents", ClsTypeCompetence.DOCUMENT, ClsNomenclature.NATURE_DOCUMENT));
		comp.setOdocuments(comp.getSortableList(liste));

		liste = this._loadInformationsMap(_getLangueQueryString());
		comp.setOlangues(comp.getSortableList(liste));

		liste = this._loadInformationsMap(_getSavoirFaireQueryString());
		comp.setOthemes(comp.getSortableList(liste));

//		liste = this._loadInformationsMap(_getGenericQueryString("tache", ClsTypeCompetence.TACHE, ClsNomenclature.TACHE));
		liste = this._loadInformationsMap(_getTacheQueryString());
		comp.setOtaches(comp.getSortableList(liste));
//		System.out.println("TAILLE LISTE INIT:"+liste.size());
//		System.out.println("TAILLE LISTE FINALE:"+comp.getOtaches().getListe().size());

		liste = this._loadInformationsMap(_getInterimPosteQueryString());
		comp.setOinterims(comp.getSortableList(liste));

		liste = this._loadInformationsMap(_getClientPosteQueryString());
		comp.setOposteclients(comp.getSortableList(liste));

		liste = this._loadInformationsMap(_getGenericQueryString("avantagenature", ClsTypeCompetence.AVANTAGE_NATURE, ClsNomenclature.AVANTAGE_EN_NATURE));
		comp.setOavantagenatures(comp.getSortableList(liste));

		liste = this._loadInformationsMap(_getGenericQueryString("avantagehorspaie", ClsTypeCompetence.AVANTAGE_HORS_PAIE, ClsNomenclature.AVANTAGE_HORS_PAIE));
		comp.setOavantagehorspaies(comp.getSortableList(liste));

		liste = this._loadInformationsMap(_getRubriqueQueryString());
		comp.setOrubriques(comp.getSortableList(liste));

		liste = this._loadInformationsMap(_getSavoirEtreQueryString());
		comp.setOsavoiretres(comp.getSortableList(liste));

		liste = this._loadInformationsMap(_getKeyAreaQueryString());
		comp.setOkeyareas(comp.getSortableList(liste));

		liste = this._loadInformationsMap(_getMissionQueryString());
		comp.setOmissions(comp.getSortableList(liste));
		
//		liste = this._loadInformationsMap(_getGenericQueryString("savoir", ClsTypeCompetence.SAVOIR, ClsNomenclature.SAVOIR));
		liste = this._loadInformationsMap(_getSavoirQueryString());
		comp.setOsavoir(comp.getSortableList(liste));
		liste = this._loadInformationsMap(_getGenericQueryString("relinternes", ClsTypeCompetence.RELATIONS_INTERNES, ClsNomenclature.RELATIONS_INTERNES));
		comp.setOrelationsinternes(comp.getSortableList(liste));
		liste = this._loadInformationsMap(_getGenericQueryString("relexternes", ClsTypeCompetence.RELATIONS_EXTERNES, ClsNomenclature.RELATIONS_EXTERNES));
		comp.setOrelationsexternes(comp.getSortableList(liste));
		liste = this._loadInformationsMap(_getSuccessionPosteQueryString());
		comp.setOplansuccession(comp.getSortableList(liste));
		
		// Chargement des moyens n�cessaires
		liste = this._loadInformationsMap(_getMoyenNeccessaireQueryString());
		comp.setOmoyens(comp.getSortableList(liste));
	}

	public List<CompetencePosteVO> computeSavoir()
	{
		return this._loadInformationsMap(_getSavoirQueryString());
	}

	public List<CompetencePosteVO> computeSavoirEtre()
	{
		return this._loadInformationsMap(_getSavoirEtreQueryString());
	}

	public List<CompetencePosteVO> computeSavoirFaire()
	{
		return this._loadInformationsMap(_getSavoirFaireQueryString());
	}

	public List<CompetencePosteVO> computeFonction()
	{
		return this._loadInformationsMap(_getFonctionQueryString());
	}

	public List<CompetencePosteVO> computeLanguage()
	{
		return this._loadInformationsMap(_getLangueQueryString());
	}

	public List<CompetencePosteVO> computeDiplome()
	{
		return this._loadInformationsMap(_getGenericQueryString("diplome", ClsTypeCompetence.DIPLOME, ClsNomenclature.DIPLOME));
	}

	public List<CompetencePosteVO> computeFormation()
	{
		return this._loadInformationsMap(_getGenericQueryString("formation", ClsTypeCompetence.FORMATION, ClsNomenclature.FORMATION));
	}

	private String _getBeginQuery()
	{
		String queryString = "select competenceinfo.id as identifiant, competenceinfo.codeinfo1 as codeinfo1,competenceinfo.typeinfo as typeinfo,competenceinfo.codeinfo2 as codeinfo2, "
				+ "competenceinfo.codeinfo3 as codeinfo3, competenceinfo.valminfo1 as valminfo1, competenceinfo.coeff as coef ";

		return queryString;
	}

	private String _getEndQuery(String typeInfo)
	{
		String queryString = " where	competenceinfo.identreprise='" + comp.dossier + "'" + " AND  competenceinfo.codeposte='" + comp.codeEntite + "'" + " AND competenceinfo.typeinfo = '" + typeInfo + "'";
		return queryString;
	}

	private String _getGenericQueryString(String libelleInfo, String typeInfo, String table)
	{

		String queryString = _getBeginQuery() + "," + libelleInfo + ".vall as libelle" + libelleInfo + " from Orgposteinfo competenceinfo " + "left join ParamData " + libelleInfo + " on (" + libelleInfo
				+ ".identreprise=competenceinfo.identreprise and " + libelleInfo + ".cacc=competenceinfo.codeinfo1 and " + libelleInfo + ".nume=1 and " + libelleInfo + ".ctab='" + table + "') "
				+ _getEndQuery(typeInfo);

		return queryString;
	}

	private String _getFonctionQueryString()
	{
		String queryString = _getBeginQuery() + "," + " fonction.vall as libellefonction, fonctionie.vall as libellefonctionie " + " from Orgposteinfo competenceinfo "
				+ "left join ParamData fonction on (fonction.identreprise=competenceinfo.identreprise and fonction.cacc=competenceinfo.codeinfo1 and fonction.nume=1 and fonction.ctab='" + ClsNomenclature.FONCTION
				+ "' and competenceinfo.typeinfo='" + ClsTypeCompetence.FONCTION + "') "
				+ "left join ParamData fonctionie on (fonctionie.identreprise=competenceinfo.identreprise and fonctionie.cacc=competenceinfo.codeinfo2 and fonctionie.nume=1 and fonctionie.ctab='"
				+ ClsNomenclature.TYPE_FONCTION + "') " + _getEndQuery(ClsTypeCompetence.FONCTION);

		return queryString;
	}

	private String _getLangueQueryString()
	{
		String queryString = _getBeginQuery() + "," + " langue.vall as libellelangue, niveaulangue.vall as libelleniveaulange " + " from Orgposteinfo competenceinfo "
				+ "left join ParamData langue on (langue.identreprise=competenceinfo.identreprise and langue.cacc=competenceinfo.codeinfo1 and langue.nume=1 and langue.ctab='" + ClsNomenclature.LANGUE
				+ "' and competenceinfo.typeinfo='" + ClsTypeCompetence.LANGUE + "') "
				+ "left join ParamData niveaulangue on (niveaulangue.identreprise=competenceinfo.identreprise and niveaulangue.cacc=competenceinfo.codeinfo2 and niveaulangue.nume=1 and niveaulangue.ctab='"
				+ ClsNomenclature.NIVEAUX_LANGUE + "') " + _getEndQuery(ClsTypeCompetence.LANGUE);
		return queryString;
	}

	private String _getSavoirEtreQueryString()
	{
		String queryString = _getBeginQuery()
				+ ","
				+ " savoiretre.vall as libellesavoiretre, niveausavoiretre.vall as libelleniveausavoiretre "
				+ " from Orgposteinfo competenceinfo "
				+ "left join ParamData savoiretre on (savoiretre.identreprise=competenceinfo.identreprise and savoiretre.cacc=competenceinfo.codeinfo1 and savoiretre.nume=1 and savoiretre.ctab='"
				+ ClsNomenclature.SAVOIR_ETRE
				+ "' and competenceinfo.typeinfo='"
				+ ClsTypeCompetence.SAVOIR_ETRE
				+ "') "
				+ "left join ParamData niveausavoiretre on (niveausavoiretre.identreprise=competenceinfo.identreprise and niveausavoiretre.cacc=competenceinfo.codeinfo2 and niveausavoiretre.nume=1 and niveausavoiretre.ctab='"
				+ ClsNomenclature.NIVEAU_THEME + "') " + _getEndQuery(ClsTypeCompetence.SAVOIR_ETRE);
		return queryString;
	}
	
	private String _getSavoirQueryString()
	{
		String queryString = _getBeginQuery()
				+ ","
				+ " savoiretre.vall as libellesavoir, niveausavoiretre.vall as libelleniveausavoir"
				+ " from Orgposteinfo competenceinfo "
				+ "left join ParamData savoiretre on (savoiretre.identreprise=competenceinfo.identreprise and savoiretre.cacc=competenceinfo.codeinfo1 and savoiretre.nume=1 and savoiretre.ctab='"
				+ ClsNomenclature.SAVOIR
				+ "' and competenceinfo.typeinfo='"
				+ ClsTypeCompetence.SAVOIR
				+ "') "
				+ "left join ParamData niveausavoiretre on (niveausavoiretre.identreprise=competenceinfo.identreprise and niveausavoiretre.cacc=competenceinfo.codeinfo2 and niveausavoiretre.nume=1 and niveausavoiretre.ctab='"
				+ ClsNomenclature.NIVEAU_THEME + "') " + _getEndQuery(ClsTypeCompetence.SAVOIR);
		return queryString;
	}
	
	private String _getTacheQueryString()
	{
		String queryString = _getBeginQuery()
				+ ","
				+ " savoiretre.vall as libellesavoir, niveausavoiretre.vall as libelleniveausavoir"
				+ " from Orgposteinfo competenceinfo "
				+ "left join ParamData savoiretre on (savoiretre.identreprise=competenceinfo.identreprise and savoiretre.cacc=competenceinfo.codeinfo1 and savoiretre.nume=1 and savoiretre.ctab='"
				+ ClsNomenclature.TACHE
				+ "' and competenceinfo.typeinfo='"
				+ ClsTypeCompetence.TACHE
				+ "') "
				+ "left join ParamData niveausavoiretre on (niveausavoiretre.identreprise=competenceinfo.identreprise and niveausavoiretre.cacc=competenceinfo.codeinfo2 and niveausavoiretre.nume=1 and niveausavoiretre.ctab='208') " + _getEndQuery(ClsTypeCompetence.TACHE);
//		System.out.println("REQUETE: "+queryString);
		return queryString;
	}

	private String _getMissionQueryString()
	{
		String queryString = _getBeginQuery()
				+ ","
				+ " mission.vall as libellemission, domaineclemission.vall as libelledomaineclemission "
				+ " from Orgposteinfo competenceinfo "
				+ "left join ParamData mission on (mission.identreprise=competenceinfo.identreprise and mission.cacc=competenceinfo.codeinfo1 and mission.nume=1 and mission.ctab='"
				+ ClsNomenclature.MISSION
				+ "' and competenceinfo.typeinfo='"
				+ ClsTypeCompetence.MISSION
				+ "') "
				+ "left join ParamData domaineclemission on (domaineclemission.identreprise=competenceinfo.identreprise and domaineclemission.cacc=competenceinfo.codeinfo2 and domaineclemission.nume=1 and domaineclemission.ctab='"
				+ ClsNomenclature.DOMAINE_CLE + "') " + _getEndQuery(ClsTypeCompetence.MISSION);
		return queryString;
	}
	
	private String _getMoyenNeccessaireQueryString()
	{
		String queryString = _getBeginQuery()
				+ ","
				+ " mission.vall as libellemission, domaineclemission.vall as libelledomaineclemission "
				+ " from Orgposteinfo competenceinfo "
				+ "left join ParamData mission on (mission.identreprise=competenceinfo.identreprise and mission.cacc=competenceinfo.codeinfo1 and mission.nume=1 and mission.ctab='"
				+ 294
				+ "' and competenceinfo.typeinfo='"
				+ ClsTypeCompetence.MOYEN_NECCESSAIRE
				+ "') "
				+ "left join ParamData domaineclemission on (domaineclemission.identreprise=competenceinfo.identreprise and domaineclemission.cacc=competenceinfo.codeinfo2 and domaineclemission.nume=1 and domaineclemission.ctab='"
				+ 295 + "') " + _getEndQuery(ClsTypeCompetence.MOYEN_NECCESSAIRE);
		return queryString;
	}

	private String _getSavoirFaireFaireQueryString()
	{
		String queryString = _getBeginQuery()
				+ ","
				+ " savoirfairefaire.vall as libellesavoirfairefaire, niveausavoirfairefaire.vall as libelleniveausavoirfairefaire "
				+ " from Orgposteinfo competenceinfo "
				+ "left join ParamData savoirfairefaire on (savoirfairefaire.identreprise=competenceinfo.identreprise and savoirfairefaire.cacc=competenceinfo.codeinfo1 and savoirfairefaire.nume=1 and savoirfairefaire.ctab='"
				+ ClsNomenclature.SAVOIR_FAIRE_FAIRE
				+ "' and competenceinfo.typeinfo='"
				+ ClsTypeCompetence.SAVOIR_FAIRE_FAIRE
				+ "') "
				+ "left join ParamData niveausavoirfairefaire on (niveausavoirfairefaire.identreprise=competenceinfo.identreprise and niveausavoirfairefaire.cacc=competenceinfo.codeinfo2 and niveausavoirfairefaire.nume=1 and niveausavoirfairefaire.ctab='"
				+ ClsNomenclature.NIVEAU_THEME + "') " + _getEndQuery(ClsTypeCompetence.SAVOIR_FAIRE_FAIRE);
		return queryString;
	}

	private String _getSavoirFaireQueryString()
	{
		String queryString = _getBeginQuery() + "," + " theme.vall as libelletheme, domaine.vall as libelledomaine, niveautheme.vall as libelleniveautheme " + " from Orgposteinfo competenceinfo "
				+ "left join ParamData theme on (theme.identreprise=competenceinfo.identreprise and theme.cacc=competenceinfo.codeinfo1 and theme.nume=1 and theme.ctab='" + ClsNomenclature.THEME + "') "
				+ "left join ParamData domaine on (domaine.identreprise=competenceinfo.identreprise and domaine.cacc=competenceinfo.codeinfo2 and domaine.nume=1 and domaine.ctab='" + ClsNomenclature.DOMAINE_COMPETENCE
				+ "' and competenceinfo.typeinfo='" + ClsTypeCompetence.SAVOIR_FAIRE + "') "
				+ "left join ParamData niveautheme on (niveautheme.identreprise=competenceinfo.identreprise and niveautheme.cacc=competenceinfo.codeinfo3 and niveautheme.nume=1 and niveautheme.ctab='"
				+ ClsNomenclature.NIVEAU_THEME + "') " + _getEndQuery(ClsTypeCompetence.SAVOIR_FAIRE);

		return queryString;
	}

	private String _getInterimPosteQueryString()
	{
		String queryString = _getBeginQuery() + "," + " orginterimposte.libelle as libelleinterimposte, disponibilite.vall as libelledisponibilite " + " from Orgposteinfo competenceinfo "
				+ "left join Organigramme orginterimposte on (orginterimposte.identreprise=competenceinfo.identreprise and orginterimposte.codeposte=competenceinfo.codeinfo1 ) "
				+ "left join ParamData disponibilite on (disponibilite.identreprise=competenceinfo.identreprise and disponibilite.cacc=competenceinfo.codeinfo2 and disponibilite.nume=1 and disponibilite.ctab='"
				+ ClsNomenclature.DISPONIBILITE + "') " + _getEndQuery(ClsTypeCompetence.INTERIM_POSTE);

		return queryString;
	}
	
	private String _getSuccessionPosteQueryString()
	{
		String queryString = _getBeginQuery() + "," + " orginterimposte.libelle as libelleplansuccession, disponibilite.vall as libelledisponibilite " + " from Orgposteinfo competenceinfo "
				+ "left join Orgposte orginterimposte on (orginterimposte.identreprise=competenceinfo.identreprise and orginterimposte.codeposte=competenceinfo.codeinfo1 ) "
				+ "left join ParamData disponibilite on (disponibilite.identreprise=competenceinfo.identreprise and disponibilite.cacc=competenceinfo.codeinfo2 and disponibilite.nume=1 and disponibilite.ctab='"
				+ ClsNomenclature.DISPONIBILITE + "') " + _getEndQuery(ClsTypeCompetence.PLAN_SUCCESSION);

		return queryString;
	}

	private String _getKeyAreaQueryString()
	{
		String queryString = _getBeginQuery()
				+ ","
				+ " standardperformance.vall as libellestandardperformance, objectifintermetdiaire.vall as libelleobjectifintermediaire, domainecle.vall as libelledomainecle "
				+ " from Orgposteinfo competenceinfo "
				+ "left join ParamData standardperformance on (standardperformance.identreprise=competenceinfo.identreprise and standardperformance.cacc=competenceinfo.codeinfo1 and standardperformance.nume=1 and standardperformance.ctab='"
				+ ClsNomenclature.STANDARD_PERFORMANCE
				+ "') "
				+ "left join ParamData objectifintermetdiaire on (objectifintermetdiaire.identreprise=competenceinfo.identreprise and objectifintermetdiaire.cacc=competenceinfo.codeinfo2 and objectifintermetdiaire.nume=1 and objectifintermetdiaire.ctab='"
				+ ClsNomenclature.OBJECTIF_INTERMEDIAIRE + "') "
				+ "left join ParamData domainecle on (domainecle.identreprise=competenceinfo.identreprise and domainecle.cacc=competenceinfo.codeinfo3 and domainecle.nume=1 and domainecle.ctab='"
				+ ClsNomenclature.DOMAINE_CLE + "') " + _getEndQuery(ClsTypeCompetence.DOMAINE_CLE);

		return queryString;
	}

	private String _getClientPosteQueryString()
	{
		String queryString = _getBeginQuery() + "," + "orgposteclient.libelle as libelleposteclient" + " from Orgposteinfo competenceinfo "
				+ "left join Organigramme orgposteclient on (orgposteclient.identreprise=competenceinfo.identreprise and orgposteclient.codeposte=competenceinfo.codeinfo1 ) "
				+ _getEndQuery(ClsTypeCompetence.CLIENT_POSTE);

		return queryString;
	}

	private String _getRubriqueQueryString()
	{
		String queryString = _getBeginQuery() + "," + "rubrique.lrub as libellerubrique" + " from Orgposteinfo competenceinfo "
				+ "left join ElementSalaire rubrique on (rubrique.identreprise=competenceinfo.identreprise and rubrique.crub=competenceinfo.codeinfo1) " + _getEndQuery(ClsTypeCompetence.RUBRIQUE);

		return queryString;
	}

	@SuppressWarnings("deprecation")
	private List<CompetencePosteVO> _loadInformationsMap(String _strQueryString)
	{
		//System.out.println("Query for "+_strQueryString);
		List<CompetencePosteVO> liste = new ArrayList<CompetencePosteVO>();
		String queryString = _strQueryString;

		CompetencePosteVO _o_Data = null;

		//Session _o_Session = comp.service.getSession();
		String typeinfo = null;
		Connection oConnexion = null;
		Statement oStatement = null;
		ResultSet _o_Result = null;

		try
		{

			//_o_Session = comp.service.getSession();
			oConnexion = comp.service.getConnection();
			oStatement = oConnexion.createStatement();
			_o_Result = oStatement.executeQuery(queryString);

			while (_o_Result.next())
			{
				try
				{

					_o_Data = new CompetencePosteVO();
					_o_Data.setId(_o_Result.getBigDecimal("identifiant").intValue());
					typeinfo = _o_Result.getString("typeinfo");

					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.FORMATION))
						_o_Data.setLibelleinfo1(_o_Result.getString("libelleformation"));

					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.FONCTION))
					{
						_o_Data.setLibelleinfo1(_o_Result.getString("libellefonction"));
						_o_Data.setLibelleinfo2(_o_Result.getString("libellefonctionie"));
					}

					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.DIPLOME))
						_o_Data.setLibelleinfo1(_o_Result.getString("libellediplome"));

					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.DOCUMENT))
						_o_Data.setLibelleinfo1(_o_Result.getString("libelledocuments"));

					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.LANGUE))
					{
						_o_Data.setLibelleinfo1(_o_Result.getString("libellelangue"));
						_o_Data.setLibelleinfo2(_o_Result.getString("libelleniveaulange"));
					}

					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.SAVOIR_FAIRE))
					{
						_o_Data.setLibelleinfo1(_o_Result.getString("libelletheme"));
						_o_Data.setLibelleinfo2(_o_Result.getString("libelledomaine"));
						_o_Data.setLibelleinfo3(_o_Result.getString("libelleniveautheme"));
					}

					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.TACHE)){
//						_o_Data.setLibelleinfo1(_o_Result.getString("libelletache"));
						_o_Data.setLibelleinfo1(_o_Result.getString("libellesavoir"));
						_o_Data.setLibelleinfo2(_o_Result.getString("libelleniveausavoir"));
					}
					
					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.SAVOIR)){
						_o_Data.setLibelleinfo1(_o_Result.getString("libellesavoir"));
						_o_Data.setLibelleinfo2(_o_Result.getString("libelleniveausavoir"));
					}
					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.RELATIONS_INTERNES))
						_o_Data.setLibelleinfo1(_o_Result.getString("libellerelinternes"));
					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.RELATIONS_EXTERNES))
						_o_Data.setLibelleinfo1(_o_Result.getString("libellerelexternes"));
					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.PLAN_SUCCESSION))
						_o_Data.setLibelleinfo1(_o_Result.getString("libelleplansuccession"));

					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.INTERIM_POSTE))
					{
						_o_Data.setLibelleinfo1(_o_Result.getString("libelleinterimposte"));
						_o_Data.setLibelleinfo2(_o_Result.getString("libelledisponibilite"));
					}

					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.CLIENT_POSTE))
						_o_Data.setLibelleinfo1(_o_Result.getString("libelleposteclient"));

					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.AVANTAGE_NATURE))
						_o_Data.setLibelleinfo1(_o_Result.getString("libelleavantagenature"));

					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.AVANTAGE_HORS_PAIE))
						_o_Data.setLibelleinfo1(_o_Result.getString("libelleavantagehorspaie"));

					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.RUBRIQUE))
						_o_Data.setLibelleinfo1(_o_Result.getString("libellerubrique"));

					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.SAVOIR_ETRE))
					{
						_o_Data.setLibelleinfo1(_o_Result.getString("libellesavoiretre"));
						_o_Data.setLibelleinfo2(_o_Result.getString("libelleniveausavoiretre"));
					}

					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.DOMAINE_CLE))
					{
						_o_Data.setLibelleinfo3(_o_Result.getString("libelledomainecle"));
						_o_Data.setLibelleinfo2(_o_Result.getString("libelleobjectifintermediaire"));
						_o_Data.setLibelleinfo1(_o_Result.getString("libellestandardperformance"));
					}

					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.MISSION))
					{
						_o_Data.setLibelleinfo2(_o_Result.getString("libelledomaineclemission"));
						_o_Data.setLibelleinfo1(_o_Result.getString("libellemission"));
					}
					
					if (typeinfo.equalsIgnoreCase(ClsTypeCompetence.MOYEN_NECCESSAIRE))
					{
						_o_Data.setLibelleinfo2(_o_Result.getString("libelledomaineclemission"));
						_o_Data.setLibelleinfo1(_o_Result.getString("libellemission"));
					}

					_o_Data.setCodeinfo2(_o_Result.getString("codeinfo2"));
					_o_Data.setCodeinfo3(_o_Result.getString("codeinfo3"));
					_o_Data.setValminfo1(_o_Result.getBigDecimal("valminfo1"));
					_o_Data.setCoeff(_o_Result.getString("coef"));

					_o_Data.setCodeinfo1(_o_Result.getString("codeinfo1"));
					_o_Data.setTypeinfo(typeinfo);
					_o_Data.setCodeposte(comp.codeEntite);
					_o_Data.setIdEntreprise(new Integer(comp.dossier));

					//_o_Data.setMode(Mode.CONSULTATION);

					liste.add(_o_Data);

				}
				catch (Exception e)
				{
					e.printStackTrace();
					continue;
				}
				finally
				{
					_o_Data = null;
				}
			}

		}
		catch (Exception e)
		{
			// TODO: handle exception
			e.printStackTrace();
		}
		finally
		{
			try
			{
				if (_o_Result != null)
					_o_Result.close();
				if (oStatement != null)
					oStatement.close();
				//if (oConnexion != null)
					//oConnexion.close();
				//if (_o_Session != null)
					//_o_Session.close();
			}
			catch (Exception e)
			{
				e.printStackTrace();
			}

		}
		return liste;
	}

}
