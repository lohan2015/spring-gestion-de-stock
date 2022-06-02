package com.kinart.paie.business.services.cloture;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.services.calcul.ClsParameterOfPay;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.ClsObjectUtil;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;

/**
 * Classe contenant les param�tres du bulletin: num�ro et puis libell�.
 * @author c.mbassi
 *
 */
public class ClsBulletin {
	List<InfoBulletin> listOfBulletin = null;
	GeneriqueConnexionService service = null;
	InfoBulletin infoBulletin = null;
	
	private ClsBulletin(){
	}
	public InfoBulletin getInfoBulletin() {
		return infoBulletin;
	}

	public void setInfoBulletin(InfoBulletin infoBulletin) {
		this.infoBulletin = infoBulletin;
	}

	public List<InfoBulletin> getListOfBulletion() {
		return listOfBulletin;
	}

	public void setListOfBulletion(List<InfoBulletin> listOfBulletin) {
		this.listOfBulletin = listOfBulletin;
	}

	public GeneriqueConnexionService getService() {
		return service;
	}

	public void setService(GeneriqueConnexionService service) {
		this.service = service;
	}

	public ClsBulletin(GeneriqueConnexionService service){
		this.listOfBulletin = chargerListeVideBulletin();// new ArrayList<InfoBulletin>();
		this.service = service;
		this.infoBulletin = new InfoBulletin();
	}
	
	public List<InfoBulletin> chargerListeVideBulletin()
	{
		List<InfoBulletin> lst = new ArrayList<InfoBulletin>();
		InfoBulletin inf = null;
		for (int i = 0; i < 5; i++)
		{
			inf = new InfoBulletin();
			inf.setLibelle(null);
			inf.setPeriodepaie(null);
			inf.setNbul(9);
			lst.add(inf);
		}
		return lst;
	}
	
	/**
	 * =>charge_nbul
	 * @param cdos
	 * @param monthOfPay
	 */
	public void chargerBulletin(String cdos, String monthOfPay){
		ParameterUtil.println(">>chargerBulletin");
//	  i     NUMBER(2);
//
//	  BEGIN
//	    FOR i IN 1..5 LOOP
//	       x_mnt1(i) := 0;
//	       x_lib1(i) := NULL;
//	       BEGIN
//	          SELECT valm, vall INTO x_mnt1(i), x_lib1(i) FROM pafnom
//	           WHERE cdos = w_cdos
//	             AND ctab = 92
//	             AND cacc = LTRIM(TO_CHAR(c_mdp,'YYYYMM'))
//	             AND nume = i;
//	       EXCEPTION
//	          WHEN NO_DATA_FOUND THEN null;
//	       END;
//	    END LOOP;
//	  END charge_nbul;
		InfoBulletin info = null;
		ParamData nomenc = null;
		Object o = null;
		for (int i = 0; i < 5; i++) {
			nomenc = service.findAnyColumnFromNomenclature(cdos, "","92", monthOfPay, (i+1)+"");
			//o = service.get(Rhfnom.class, new RhfnomPK(cdos, 92, monthOfPay, i+1));
			if(nomenc != null){
				//nomenc = (Rhfnom)o;
				if(nomenc.getValm() != null && nomenc.getValm().intValue()!=0)
				{
					info = new InfoBulletin();
					info.libelle = nomenc.getVall();
					info.nbul = nomenc.getValm()!= null ?nomenc.getValm().intValue() : 0;
					if(this.listOfBulletin == null)
						this.listOfBulletin =chargerListeVideBulletin(); //new ArrayList<InfoBulletin>();
					this.listOfBulletin.set(i,info);
				}
			}
		}
	}
	/**
	 * =>nbul_ok
	 * @param cdos
	 * @param monthOfPay
	 * @param nbul
	 * @return true ou false
	 */
	public boolean validateBulletin(String cdos, String monthOfPay, int nbul){
		ParameterUtil.println(">>validateBulletin");
//			--------------------------------------------------------------------------------
//			-- Verifier la validite d'un No de bulletin
//			--------------------------------------------------------------------------------
//			FUNCTION nbul_ok(f_cdos IN VARCHAR2, f_aamm IN DATE, f_nbul IN NUMBER)
//			 RETURN BOOLEAN IS
	//
//			   trouve      BOOLEAN;
//			   cpt_nbul    SMALLINT;
	//
//			BEGIN
//			  IF f_nbul = 9 THEN
//			     RETURN TRUE;
//			  END IF;
	//
//			  w_cdos := f_cdos;
//			  charge_nbul(f_aamm);
	//
//			  IF x_mnt1(1) = 0 THEN
//			    RETURN FALSE;
//			  ELSE
//			    cpt_nbul := 1;
//			    trouve   := FALSE;
//			    WHILE cpt_nbul <= 5 AND NOT trouve LOOP
//			       IF x_mnt1(cpt_nbul) = f_nbul THEN
//			          trouve := TRUE;
//			       ELSE
//			          cpt_nbul := cpt_nbul + 1;
//			       END IF;
//			    END LOOP;
//			  END IF;
	//
//			  RETURN trouve;
	//
//			END nbul_ok;
			int cpt_nbul = 0;
			boolean trouve = false;
			if(nbul == 9)
				return true;
			chargerBulletin(cdos, monthOfPay);
			if(listOfBulletin != null && listOfBulletin.size() > 0){
				if(listOfBulletin.get(0).nbul == 0)
					return false;
				else{
					while(cpt_nbul <= 5-1 && (! trouve)){
						if(listOfBulletin.get(cpt_nbul).nbul == nbul)
							trouve = true;
						else
							cpt_nbul++;
					}
				}
			}
			return trouve;
		}
		
	/**
	 * =>rec_nbul
	 * @param cdos
	 * @param monthOfPay
	 * @return les infos du bulletin
	 */
		public InfoBulletin rechercheBulletin(String cdos, String monthOfPay){
			ParameterUtil.println(">>rechercheBulletin");
			InfoBulletin bul = null;
			boolean trouve = false;
//			cpt_nbul      SMALLINT;
//			  w_mois        SMALLINT;
//			  w_an          SMALLINT;
//			  w_trouve      BOOLEAN;
//			  w_mdp         DATE;
//			  w_dnbu        pados.dnbu%TYPE;
//			  w_mnt1        pafnom.valm%TYPE;
//			  w_lib1        pafnom.vall%TYPE;
//
//			BEGIN
//			  w_cdos := r_cdos;
//
//			  BEGIN
//			     SELECT ddmp,dnbu INTO w_mdp, w_dnbu FROM pados
//			      WHERE cdos = r_cdos;
//			  EXCEPTION
//			     WHEN NO_DATA_FOUND THEN
//			        f_nbul := 9;
//			        f_lbul := ' ';
//			  END;
			Date ddmp = null;
			int dnbu = 9;
			ClsDate myMoisPaieCourant = null;
			List l = service.find("select ddmp, dnbu from DossierPaie where idEntreprise = '" + cdos + "'");
			if(l != null && l.size() > 0){
				ddmp = (Date)((Object[])l.get(0))[0];
				myMoisPaieCourant = new ClsDate(ddmp);
				dnbu = (Integer)((Object[])l.get(0))[1];
			}
			else{
				if(bul==null) bul = new InfoBulletin();
				bul.libelle = "";
				bul.nbul = 9;
			}
//
//			  w_mdp := ADD_MONTHS(w_mdp,1);
//
			ddmp = myMoisPaieCourant.addMonth(1);
			myMoisPaieCourant = new ClsDate(ddmp);
//			  IF r_mdp > w_mdp THEN
			if(monthOfPay.compareTo(myMoisPaieCourant.getYearAndMonth()) > 0){
//			     -- rechercher le 1er bulletin d'une periode a venir
//			     premier_periode(r_mdp,f_nbul,f_lbul);
				bul = premierePeriode(cdos, monthOfPay);
			}
//			  ELSE
			else{
//			     w_trouve := TRUE;
				trouve = true;
				bul = new InfoBulletin();
				bul.libelle = "";
				bul.nbul = 9;
//			     BEGIN
//			        SELECT valm, vall INTO w_mnt1, w_lib1 FROM pafnom
//			         WHERE cdos = w_cdos
//			           AND ctab = 92
//			           AND cacc = TO_CHAR(w_mdp,'YYYYMM')
//			           AND nume = 1;
//			     EXCEPTION
//			        WHEN NO_DATA_FOUND THEN w_trouve := FALSE;
//			     END;
//			     f_nbul := 0;
//			     f_lbul :=  ' ';
//
				ParamData nomenc = null;
				nomenc = service.findAnyColumnFromNomenclature(cdos, "","92", myMoisPaieCourant.getYearAndMonth(), "1");
				//Object o = service.get(Rhfnom.class, new RhfnomPK(cdos, 92, myMoisPaieCourant.getYearAndMonth(), 1));
				if(nomenc == null){
					trouve = false;
				}
//			     IF NouZ(w_dnbu) THEN
				if(dnbu <= 0){
//			        -- 1er bulletin du mois a calculer
//			        IF w_trouve THEN
					if(trouve){
						//nomenc = (Rhfnom)o;
//			           -- Mois declare
//			           IF NOT NouZ(w_mnt1) THEN
						if(! ClsObjectUtil.isNull(nomenc)){
//			              f_nbul := w_mnt1;
//			              f_lbul := w_lib1;
							bul.libelle = nomenc.getVall();
							bul.nbul = nomenc.getValm().intValue();
						}
//			           END IF;
					}
//			        ELSE
					else{
//			           f_nbul := 9;
//			           f_lbul := ' ';
						bul.libelle = "";
						bul.nbul = 9;
					}
//			        END IF;
				}
//			     ELSE
				else{
//			        -- un bulletin a deja ete cloture sur la periode
//			        charge_nbul(w_mdp);
					chargerBulletin(cdos, myMoisPaieCourant.getYearAndMonth());
//			        cpt_nbul := 1;
//			        WHILE cpt_nbul <= 5 LOOP
//			           IF x_mnt1(cpt_nbul) = w_dnbu THEN
//			              EXIT;
//			           ELSE
//			              cpt_nbul := cpt_nbul + 1;
//			           END IF;
//			        END LOOP;
					int cpt_nbul = 1-1;
					if(listOfBulletin != null && listOfBulletin.size() > 0){
						while(cpt_nbul <= 5-1){
							if(listOfBulletin.get(cpt_nbul).nbul == dnbu)
								break;
							else
								cpt_nbul++;
						}
					}
					else
						cpt_nbul = 5-1;
//			        IF cpt_nbul < 5 THEN
//			           cpt_nbul := cpt_nbul + 1;
//			           f_nbul   := x_mnt1(cpt_nbul);
//			           f_lbul   := x_lib1(cpt_nbul);
//			        END IF;
					if(cpt_nbul < 5-1){
						cpt_nbul++;
						bul.nbul = listOfBulletin.get(cpt_nbul).nbul;
						bul.libelle = listOfBulletin.get(cpt_nbul).libelle;
					}
//			     END IF;
				}
			}
//			  END IF;
//
//			END rec_nbul;
			return bul;
		}
		

		/**
		 * =>premier_periode
		 * @param cdos
		 * @param monthOfPay
		 * @return les info du bulletin
		 */
		public InfoBulletin premierePeriode(String cdos, String monthOfPay){
			ParameterUtil.println(">>premierePeriode");
			InfoBulletin bul = new InfoBulletin();
			ParamData nome = service.findAnyColumnFromNomenclature(cdos, "","92", monthOfPay, "1");
			//Object nomenc = service.get(Rhfnom.class, new RhfnomPK(cdos, 92, monthOfPay, 1));
			if(nome != null){
				bul.libelle = nome.getVall();
				bul.nbul = nome.getValm().intValue();
			}
			else{
				bul.libelle = "";
				bul.nbul = 9;
			}
			return bul;
		}
		
		/**
		 * =>dernier_periode
		 * @param cdos
		 * @param monthOfPay
		 * @param nbul
		 * @return true ou false
		 */
		public boolean dernierePeriode(String cdos, String monthOfPay, int nbul){
			ParameterUtil.println(">>dernierPeriode");
			int cpt_nbul = 0;
			boolean trouve = false;
//		--------------------------------------------------------------------------------
//		-- Ramene TRUE si dernier bulletin de la periode
//		--------------------------------------------------------------------------------
//		FUNCTION dernier_periode(r_cdos IN VARCHAR2, p_mdp IN DATE, p_nbul IN NUMBER)
//		                         RETURN BOOLEAN IS
//
//		  cpt_nbul  SMALLINT;
//		  trouve    BOOLEAN;
//		  char1     VARCHAR2(1);
//
//		BEGIN
//		  w_cdos := r_cdos;
//		  BEGIN
//		     SELECT 'X' INTO char1 FROM pafnom
//		      WHERE cdos = r_cdos
//		        AND ctab = 92
//		        AND cacc = TO_CHAR(p_mdp,'YYYYMM')
//		        AND nume = 1;
//		  EXCEPTION
//		     WHEN NO_DATA_FOUND THEN null;
//		  END;
//
//		  IF SQL%NOTFOUND THEN
//		     RETURN TRUE;
//		  END IF;
			ParamData nomenc = service.findAnyColumnFromNomenclature(cdos, "","92", monthOfPay, "1");
			//Object nomenc = service.get(Rhfnom.class, new RhfnomPK(cdos, 92, monthOfPay, 1));
			if(nomenc == null)
				return true;
//
//		  charge_nbul(p_mdp);
			chargerBulletin(cdos, monthOfPay);
//		  cpt_nbul := 5;
			cpt_nbul = 5-1;
//		  WHILE cpt_nbul > 0 LOOP
//		    IF NOT NouZ(x_mnt1(cpt_nbul)) THEN
//		       EXIT;
//		    ELSE
//		       cpt_nbul := cpt_nbul - 1;
//		    END IF;
//		  END LOOP;
			while(cpt_nbul > 0){
				if(listOfBulletin.get(cpt_nbul).nbul > 0)
					break;
				else
					cpt_nbul--;
			}
//		  IF cpt_nbul > 0 THEN
//		     trouve := (x_mnt1(cpt_nbul) = p_nbul);
//		  ELSE
//		     trouve := FALSE;
//		  END IF;
			trouve = cpt_nbul > 0 ? listOfBulletin.get(cpt_nbul).nbul == nbul : false;
//
//		  RETURN trouve;
			return trouve;
		}
		
		/**
		 * =>dernier_periode
		 * Calcul prochain No bulletin sur prochaine periode de calcul.
		 * @param cdos
		 * @param monthOfPay
		 * @param nbul
		 * @return les informations du bulletin (nbul, periode, libell�)
		 */
		public InfoBulletin prochainePaie(String cdos, String monthOfPay, int nbul){
			ParameterUtil.println(">>prochainePaie");
//			PROCEDURE prochaine_paie(r_cdos IN VARCHAR2, c_mdp IN DATE, c_nbul IN NUMBER,
//                    s_mdp  IN OUT DATE, s_nbul IN OUT NUMBER) IS
			InfoBulletin infoBulletin = new InfoBulletin();
//			ind_1      NUMBER(2);
//			  cpt_nbul   SMALLINT;
//
//			  char1      VARCHAR2(1);
//			  char30     VARCHAR2(30);
//
//			BEGIN
//
//			  w_cdos := r_cdos;
//			  s_mdp := ADD_MONTHS(c_mdp,1);
			Date moissuivant = new ClsDate(monthOfPay, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).addMonth(1);
			ClsDate d = new ClsDate(moissuivant);
			String nextMonth = d.getYearAndMonth();
			infoBulletin.setPeriodepaie(d.getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM));
//
//			  BEGIN
//			     SELECT 'X' INTO char1 FROM pafnom
//			      WHERE cdos = r_cdos
//			        AND ctab = 92
//			        AND cacc = TO_CHAR(c_mdp,'YYYYMM')
//			        AND nume = 1;
//			  EXCEPTION
//			     WHEN NO_DATA_FOUND THEN null;
//			  END;
			ParamData nomenc = service.findAnyColumnFromNomenclature(cdos, "","92", monthOfPay, "1");
			//Object nomenc = service.get(Rhfnom.class, new RhfnomPK(cdos, 92, monthOfPay, 1));
//
//			  IF SQL%FOUND THEN
			if(nomenc != null){
//			     -- Voir si un autre bulletin sur la periode
//			     charge_nbul(c_mdp);
				chargerBulletin(cdos, monthOfPay);
//			     ind_1 := recherche_place(c_nbul);
				InfoBulletin info = null;
				int index = 0;
				for (int i = 0; i < listOfBulletin.size(); i++)
				{
					if(listOfBulletin.get(i).getNbul() == nbul){
						info = listOfBulletin.get(i);
						break;
					}
					index++;
				}
				
//			     IF ind_1 < 5 THEN
//			        ind_1 := ind_1 + 1;
//			        IF NOT NouZ(x_mnt1(ind_1)) THEN
//			           s_mdp  := c_mdp;
//			           s_nbul := x_mnt1(ind_1);
//			        ELSE
//			           -- prendre le 1er No bulletin periode suivante
//			           premier_periode(s_mdp,s_nbul,char30);
//			        END IF;
//			     ELSE
//			        -- prendre le 1er No bulletin periode suivante
//			        premier_periode(s_mdp,s_nbul,char30);
//			     END IF;
				if(index < 5-1){
					index++;
					if(index < listOfBulletin.size() && listOfBulletin.get(index) != null && listOfBulletin.get(index).getNbul() != null && listOfBulletin.get(index).getNbul() !=0){
						infoBulletin.setPeriodepaie(monthOfPay);
						infoBulletin.setNbul(listOfBulletin.get(index).getNbul());
					} 
					else{
						infoBulletin = premierePeriode(cdos, nextMonth);
						infoBulletin.setPeriodepaie(d.getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM));
					}
				}
				else{
					infoBulletin = premierePeriode(cdos, nextMonth);
					infoBulletin.setPeriodepaie(d.getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM));
				}
			}
//			  ELSE
			else{
//			     -- un seul bulletin sur la periode
//			     -- prendre le 1er No bulletin periode suivante
//			     premier_periode(s_mdp,s_nbul,char30);
				infoBulletin = premierePeriode(cdos, nextMonth);
				infoBulletin.setPeriodepaie(d.getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM));
			}
//			  END IF;
			//
			return infoBulletin;
		}
	
	public class InfoBulletin{
		Integer nbul = 9;
		String libelle = "";
		String periodepaie = null;
		public synchronized String getPeriodepaie() {
			return periodepaie;
		}
		public synchronized void setPeriodepaie(String periodepaie) {
			this.periodepaie = periodepaie;
		}
		public String getLibelle() {
			return libelle;
		}
		public void setLibelle(String libelle) {
			this.libelle = libelle;
		}
		public Integer getNbul() {
			return nbul;
		}
		public void setNbul(Integer nbul) {
			this.nbul = nbul;
		}
	}
}
