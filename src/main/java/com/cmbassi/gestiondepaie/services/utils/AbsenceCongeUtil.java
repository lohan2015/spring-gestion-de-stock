package com.cmbassi.gestiondepaie.services.utils;

import com.cmbassi.gestiondepaie.dto.ParamDataDto;
import com.cmbassi.gestiondepaie.repository.ParamDataRepository;
import com.cmbassi.gestiondestock.exception.EntityNotFoundException;
import com.cmbassi.gestiondestock.exception.ErrorCodes;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.util.Date;
import java.util.List;

public class AbsenceCongeUtil {
    //	---------------------------------------------------------------
//	--         COMPTAGE DES JOURS DE CONGES ET D'ABSENCE         --
//	---------------------------------------------------------------
//	-- Si W_cas = 'C' on compte les jours de conges              --
//	--          sinon on compte les jours d'absence              --
//	---------------------------------------------------------------
//	-- LH 060198                                                 --
//	--    Prise en compte du parametre Base30 de T99.            --
//	--    Modification comptage absences pour mois de 31 jours   --
//	--       Si Base30 est utilise                               --
//	--       Si comptage Absence                                 --
//	--       Si mois de 31 jours                                 --
//	--          Si periode s'etend jusqu'au 31                   --
//	--          ALORS wnbja = 30 - DAY( ddeb ) + 1               --
//	--          Si periode s'etend du 1 au 30                    --
//	--          ALORS wnbja = 29                                 --
//	---------------------------------------------------------------
    public static long pr_compte_jours(GeneriqueConnexionService oService, ParamDataRepository paramDataRepository, String cdos, String cas, Date deb, Date fin, String typeBD)
    {

//	  w_nb		NUMBER;
        long nb;
//	  w_nbtot	NUMBER;
        long nbtot;
//	  w_mois	VARCHAR2(6);
        String mois;
//
//	  Nbj_max_par_mois  NUMBER;
        long nbj_max_par_mois = 0;
//	  Nbj_du_mois       NUMBER;
        long nbj_du_mois;
//	  Base_30           BOOLEAN;
        boolean base_30;
//	  char1             VARCHAR2(1);
        char char1;
//	  Premier_Jour      DATE;
        Date premier_jour;
//	  Dernier_Jour      DATE;
        Date dernier_jour;
//
//	  CURSOR curs_cal IS
//	      SELECT count(*), TO_CHAR(jour,'YYYYMM') FROM pacal
//	       WHERE cdos = :b_label.w_cdos
//	         AND jour BETWEEN W_Deb AND W_Fin
//	         AND ouvr LIKE (DECODE(W_Cas,'C','O','%'))
//	         AND fer  LIKE (DECODE(W_Cas,'C','N','%'))
//	       GROUP BY TO_CHAR(jour,'YYYYMM');
        String curs_sal = "Select count(*) as nb, to_char(jour,'YYYYMM') as mois From CalendrierPaie where cdos = :cdos ";
        curs_sal+=" and jour between :debut and :fin ";
        curs_sal += " and ouvr like (case :cas when 'C' then 'O' else '%' end) ";
        //curs_sal += " and fer like (case :cas when 'C' then 'N' else '%' end) ";
        curs_sal += " and fer like (case :cas when 'C' then 'N' else 'N' end) ";
        curs_sal += " and trav like (case :cas when 'C' then '%' else 'O' end) ";
        curs_sal+=" group by to_char(jour,'YYYYMM')";

        if (typeBD.equalsIgnoreCase(TypeBDUtil.IN))
        {
            curs_sal = "Select count(*) as nb, to_char1(jour,'YYYYMM') as mois From CalendrierPaie where cdos = '"+cdos+"' ";
            curs_sal+=" and jour between '"+new ClsDate(deb).getDateS("yyyy-MM-dd")+"' and '"+new ClsDate(fin).getDateS("yyyy-MM-dd")+"' ";
            curs_sal += " and ouvr like (case '"+cas+"' when 'C' then 'O' else '%' end) ";
            curs_sal += " and fer like (case '"+cas+"' when 'C' then 'N' else 'N' end) ";
            curs_sal += " and trav like (case '"+cas+"' when 'C' then '%' else 'O' end) ";
            curs_sal+=" group by 2";
        }

        if (typeBD.equalsIgnoreCase(TypeBDUtil.MS))
        {
            curs_sal = "Select count(*) as nb, dbo.formaterDateEnChaine(jour,'yyyyMM') as mois From Rhpcalendrier where cdos = :cdos ";
            curs_sal += " and jour between :debut and :fin ";
            curs_sal += " and ouvr like (case :cas when 'C' then 'O' else '%' end) ";
            //curs_sal += " and fer like (case :cas when 'C' then 'N' else '%' end) ";
            curs_sal += " and fer like (case :cas when 'C' then 'N' else 'N' end) ";
            curs_sal += " and trav like (case :cas when 'C' then '%' else 'O' end) ";
            curs_sal += " group by dbo.formaterDateEnChaine(jour,'yyyyMM')";
        }
        if (typeBD.equalsIgnoreCase(TypeBDUtil.MY))
        {
            curs_sal = "Select count(*) as nb, date_format(jour,'%Y%m') as mois From Rhpcalendrier where cdos = :cdos ";
            curs_sal+=" and jour between :debut and :fin ";
            curs_sal += " and ouvr like (case :cas when 'C' then 'O' else '%' end) ";
            //curs_sal += " and fer like (case :cas when 'C' then 'N' else '%' end) ";
            curs_sal += " and fer like (case :cas when 'C' then 'N' else 'N' end) ";
            curs_sal += " and trav like (case :cas when 'C' then '%' else 'O' end) ";
            curs_sal+=" group by date_format(jour,'%Y%m')";
        }

//	BEGIN

//	   -- Lecture de BASE30 en table 99
//	   BEGIN
//	      SELECT NVL(vall, ' '), NVL(valm, 0)
//	        INTO char1, Nbj_max_par_mois
//	        FROM pafnom
//	       WHERE cdos = :b_label.w_cdos
//	         AND ctab = 99
//	         AND cacc = 'BASE30'
//	         AND nume = 1;
//	   EXCEPTION
//	      WHEN OTHERS THEN
//	           char1 := 'O';
//	           Nbj_max_par_mois := 30;
//	   END;

        ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "BASE30", Integer.valueOf(1))
                                .map(ParamDataDto::fromEntity)
                                .orElseThrow(() ->
                                        new EntityNotFoundException(
                                                "Aucune donnée avec l'ID = "+"BASE30"+" n' ete trouve dans la table 99",
                                                ErrorCodes.ARTICLE_NOT_FOUND)
                                );
        if(fnom == null)
        {
            char1 = 'O';
            nbj_max_par_mois = 30;
        }
        else
        {
            char1 = (!StringUtils.hasLength(fnom.getVall())) ? fnom.getVall().charAt(0) : ' ';
            nbj_max_par_mois = fnom.getValm() != null ? fnom.getValm() : 0;
        }

//	   -- Controle coherence valeur saisie
//	   IF char1 != 'O' AND char1 != 'N'
//	   THEN
//	      char1 := 'O';
//	      Nbj_max_par_mois := 30;
//	   END IF;
        if(char1 != 'O' && char1 != 'N')
        {
            char1 = 'O';
            nbj_max_par_mois = 30;
        }

//	   IF char1 = 'O'
//	   THEN
//	      Base_30 := TRUE;
//	      IF NVL(Nbj_max_par_mois, 0) < 1 OR
//	         NVL(Nbj_max_par_mois, 0) > 99
//	      THEN
//	         Nbj_max_par_mois := 30;
//	      END IF;
//	   ELSE
//	      Base_30 := FALSE;
//	   END IF;
        if(char1 == 'O')
        {
            base_30 = true;
            if(nbj_max_par_mois < 1 ||nbj_max_par_mois > 99)
                nbj_max_par_mois = 30;
            fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "BASE30", Integer.valueOf(5))
                    .map(ParamDataDto::fromEntity)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Aucune donnée avec l'ID = "+"BASE30"+" n' ete trouve dans la table 99",
                                    ErrorCodes.ARTICLE_NOT_FOUND));
            if(fnom == null || (fnom != null && "O".equalsIgnoreCase(fnom.getVall())))
            {
                nbj_max_par_mois = 30;
            }
        }
        else
            base_30 = false;

//	   w_nbtot := 0;
        nbtot = 0;
        Session session = oService.getSession();
//		System.out.println(curs_sal);
        Query q = session.createSQLQuery(curs_sal).addScalar("nb", StandardBasicTypes.BIG_DECIMAL).addScalar("mois", StandardBasicTypes.STRING);
        if (!typeBD.equalsIgnoreCase(TypeBDUtil.IN)){
            q.setParameter("cdos", cdos);
            q.setParameter("cas", cas);
            q.setParameter("debut", deb);
            q.setParameter("fin", fin);
        }
        List<Object[]> lst = q.getResultList();


//	   OPEN curs_cal;
//	   LOOP
//	      FETCH curs_cal INTO w_nb, w_mois;
//	      EXIT WHEN curs_cal%NOTFOUND;
        ClsDate dtMois = null;

        //String nomClient = ClsConfigurationParameters.getConfigParameterValue(oService, cdos, null, ClsConfigurationParameters.NOM_CLIENT);
        for (Object[] o : lst)
        {

            nb = Integer.valueOf((o[0].toString()));
            //if(StringUtils.equals(ClsEntreprise.SOCIETE_OILYBIA_CMR, nomClient) || StringUtils.equals(ClsEntreprise.BDU, nomClient))
                //return nb;
//			mois = new ClsDate((Date)o[1]).getYearAndMonth();
            mois = o[1].toString();
            mois = StringUtil.oraLPad(mois, 6, "0");
            //Pour l'instant on se limite au calendrier

//	      IF Base_30
//	      THEN
//            if(base_30 && !StringUtils.equals(ClsEntreprise.BGFIGE, nomClient) && !StringUtils.equals(ClsEntreprise.SONIBANK, nomClient))
//            {
////	         Nbj_du_mois := TO_NUMBER(TO_CHAR(LAST_DAY(TO_DATE(w_mois,'YYYYMM')),'DD'));
//                dtMois = new ClsDate(mois, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM);
//                nbj_du_mois = new ClsDate(dtMois.getLastDayOfMonth()).getDay();
////	         IF Nbj_du_mois = w_nb
////	         THEN
////	            -- LH 060198 w_nb := 30;
////	            w_nb := Nbj_max_par_mois;
////	         ELSE
//                if(nbj_du_mois == nb)
//                    nb = nbj_max_par_mois;
//                else
//                {
////	            IF W_Cas != 'C'
////	            THEN
//                    if( ! StringUtils.equals("C", cas))
//                    {
////	               IF w_mois = TO_CHAR(W_Deb,'YYYYMM')
////	               THEN
////	                  Premier_Jour := W_deb;
////	               ELSE
////	                  Premier_Jour := TO_DATE('01'|| w_mois, 'DDYYYYMM');
////	               END IF;
//                        if(StringUtils.equals(mois, new ClsDate(deb).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM)))
//                            premier_jour = deb;
//                        else
//                            premier_jour = new ClsDate("01"+mois, "ddyyyyMM").getDate();
//
////	               IF w_mois = TO_CHAR(W_Fin,'YYYYMM')
////	               THEN
////	                  Dernier_Jour := W_Fin;
////	               ELSE
////	                  Dernier_Jour := LAST_DAY( TO_DATE( w_mois, 'YYYYMM') );
////	               END IF;
//                        if(StringUtils.equals(mois, new ClsDate(fin).getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM)))
//                            dernier_jour = fin;
//                        else
//                            dernier_jour = new ClsDate(mois, ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM).getLastDayOfMonth();
////	               IF Nbj_du_mois = 31
////	               THEN
//                        if(nbj_du_mois == 31)
//                        {
////	                  IF TO_NUMBER( TO_CHAR( Dernier_Jour,'DD' ) ) = 31
////	                  THEN
////	                     w_nb := Nbj_max_par_mois - TO_NUMBER( TO_CHAR( Premier_Jour,'DD')) + 1;
//                            if(new ClsDate(dernier_jour).getDay() == 31)
//                                nb = nbj_max_par_mois - new ClsDate(premier_jour).getDay() + 1;
//                            else
////	                  ELSE
//                            {
////	                     IF TO_NUMBER( TO_CHAR( Dernier_Jour, 'DD' )) = 30 AND
////	                        TO_NUMBER( TO_CHAR( Premier_Jour, 'DD' )) =  1
////	                     THEN
////	                        w_nb := 29;
////	                     END IF;
//                                if(new ClsDate(dernier_jour).getDay() == 30 && new ClsDate(premier_jour).getDay() == 1)
//                                    nb = 29;
////	                  END IF;
//                            }
////	               END IF;
//                        }
////	            END IF;
//                    }
////	         END IF;
//                }
////	      END IF;
//            }
//	      --IF TO_NUMBER(TO_CHAR(LAST_DAY(TO_DATE(w_mois,'MMYYYY')),'DD')) = w_nb THEN
//	      --   w_nb := 30;
//	      --END IF;
//	      w_nbtot := w_nbtot + w_nb;
            nbtot += nb;
//	   END LOOP;
        }
//	   CLOSE curs_cal;
//
//	   RETURN w_nbtot;
//	END;
        oService.closeSession(session);

        return nbtot;
    }
}
