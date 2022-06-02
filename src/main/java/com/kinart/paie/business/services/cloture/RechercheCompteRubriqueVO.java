package com.kinart.paie.business.services.cloture;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.services.utils.*;
import org.apache.commons.lang3.StringUtils;

public class RechercheCompteRubriqueVO
{
	ClsRechercheCompteRubriqueVO search;
	HttpServletRequest request;
	GeneriqueConnexionService service;
	public void init(HttpServletRequest request, GeneriqueConnexionService service, String cdos)
	{
		this.service = service;
		this.request = request;
		search = new ClsRechercheCompteRubriqueVO(request, service, cdos);

		ClsResultat result = SaisieCompteRubriqueVO.initTables(search);
		
		mapNome = new HashMap<String, ParamData>();
		
		mapComptes = new HashMap<String, String>();
	}
	
	//Pour limiter les acc�s:
	Map<String, ParamData> mapNome = new HashMap<String, ParamData>();
	
	Map<String, String> mapComptes = new HashMap<String, String>();
	
	
	public String getCompte( String crub, String sens, Salarie agent)
	{
		String compte = StringUtils.EMPTY;
		String cdos = agent.getIdentreprise()+"";

		try
		{

			String[] valeurs = new String[] { StringUtils.EMPTY, StringUtils.EMPTY, StringUtils.EMPTY };
			String tmpVal = null;
			ParamData fnom = null;
			String str;
			
			String keyMapNome;
			String keyMapCompte;
			for (int i = 0; i < search.colCriteres.length; i++)
			{
				str = search.colCriteres[i];
				if (StringUtils.isNotBlank(str))
				{
					tmpVal = StringUtils.valueOf((char[]) MethodUtils.invokeExactMethod(agent, "get" + StringUtils.capitalize(str.toLowerCase()), null));
					tmpVal = StringUtil.nvl(tmpVal, StringUtils.EMPTY);
					if (StringUtils.equalsIgnoreCase(search.colsNome[i], "CACC"))
						valeurs[i] = tmpVal;
					else
					{
						if (search.ctabsAgent[i] != 0)
						{
							keyMapNome = search.ctabsAgent[i]+search.sep+tmpVal+search.sep+search.numeNome[i];
							if(mapNome.containsKey(keyMapNome))
								fnom = mapNome.get(keyMapNome);
							else
							{
								fnom = search.service.findAnyColumnFromNomenclature(search.cdos, "", search.ctabsAgent[i].intValue()+"", tmpVal, search.numeNome[i].intValue()+"");
								//fnom = (Rhfnom) search.service.get(Rhfnom.class, new RhfnomPK(search.cdos, search.ctabsAgent[i], tmpVal, search.numeNome[i]));
								mapNome.put(keyMapNome, fnom);
							}
							if (fnom != null)
							{
								if (StringUtils.equalsIgnoreCase(search.colsNome[i], "VALL"))
									valeurs[i] = fnom.getVall();
								else if (StringUtils.equalsIgnoreCase(search.colsNome[i], "VALM"))
									valeurs[i] = fnom.getValm() != null ? fnom.getValm().toString() : StringUtils.EMPTY;
								else if (StringUtils.equalsIgnoreCase(search.colsNome[i], "VALT"))
									valeurs[i] = fnom.getValt() != null ? fnom.getValt().toString() : StringUtils.EMPTY;
							}
							
						}
					}
				}
			}

			keyMapCompte = crub+search.sep+sens+search.sep+valeurs[0]+search.sep+valeurs[1]+search.sep+valeurs[2];
			if(mapComptes.containsKey(keyMapCompte))
				compte = mapComptes.get(keyMapCompte);
			else
			{
			
				String query = "Select compte From ElementSalaireCompte where idEntreprise ='" + cdos + "' and crub ='" + crub + "' and sens = '" + sens + "' ";
				
				
				
				query += " And ( ";
				//cas 0 : aucun crit�re n'est n�cessaire
				query += " (coalesce(critere1,'') = '' and coalesce(critere2,'')='' and coalesce(critere3,'')='' ) ";
				query += " Or ";
				
				//Cas 1 : crit1 non null, crit2 null,crit3 null
				query += " (coalesce(critere1,'') != '' and coalesce(critere2,'')='' and coalesce(critere3,'')='' and critere1 = '"+valeurs[0]+"') ";
				query += " Or ";
				query += " (coalesce(critere2,'') != '' and coalesce(critere1,'')='' and coalesce(critere3,'')='' and critere2 = '"+valeurs[1]+"') ";
				query += " Or ";
				query += " (coalesce(critere3,'') != '' and coalesce(critere1,'')='' and coalesce(critere2,'')='' and critere3 = '"+valeurs[2]+"') ";
				query += " Or ";
				
				query += " (coalesce(critere1,'') != '' and coalesce(critere2,'') !='' and coalesce(critere3,'')='' and "+ TypeBDUtil.contatener("coalesce(critere1,'')", "'" + search.sep + "'", "coalesce(critere2,'')")+" = '"+valeurs[0]+   search.sep   +valeurs[1]+"') ";
				query += " Or ";
				query += " (coalesce(critere1,'') != '' and coalesce(critere3,'') !='' and coalesce(critere2,'')='' and "+TypeBDUtil.contatener("coalesce(critere1,'')", "'" + search.sep + "'", "coalesce(critere3,'')")+" = '"+valeurs[0]+   search.sep   +valeurs[2]+"') ";
				query += " Or ";
				query += " (coalesce(critere2,'') != '' and coalesce(critere3,'') !='' and coalesce(critere1,'')='' and "+TypeBDUtil.contatener("coalesce(critere2,'')", "'" + search.sep + "'", "coalesce(critere3,'')")+" = '"+valeurs[1]+   search.sep   +valeurs[2]+"') ";
				query += " Or ";
				
				query += " (coalesce(critere1,'') != '' and coalesce(critere2,'') !='' and coalesce(critere3,'')!='' and "+TypeBDUtil.contatener("coalesce(critere1,'')", "'" + search.sep + "'", "coalesce(critere2,'')", "'" + search.sep + "'", "coalesce(critere3,'')")+" = '"+valeurs[0]+   search.sep   +valeurs[1]+   search.sep   +valeurs[2]+"') ";
	
				query+= " ) ";
				
				query += " order by nume ";
				List lst = service.find(query);
				if (lst.isEmpty())
					compte = StringUtils.EMPTY;
				else compte = (String)lst.get(0);
				
				mapComptes.put(keyMapCompte, compte);
			}
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}

		return compte;
	}
	
	public void clearObjects()
	{
		this.mapComptes = null;
		this.mapNome = null;
	}

}
