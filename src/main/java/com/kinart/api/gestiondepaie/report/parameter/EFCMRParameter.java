package com.kinart.api.gestiondepaie.report.parameter;

import com.kinart.api.gestiondepaie.report.LigneDeclarationVersement;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EFCMRParameter {

    /**
     *
     * @param ligne
     * @param periode
     * @return
     */
    public static Map<String, Object> setParametersDeclVersement(LigneDeclarationVersement ligne, String periode){
        Map<String, Object> parameters = new HashMap<String, Object>();

        BigDecimal irpp = (ligne.getIrpp()==null)?BigDecimal.ZERO:ligne.getIrpp();
        BigDecimal cacIrpp = (ligne.getCacirpp()==null)?BigDecimal.ZERO:ligne.getCacirpp();
        BigDecimal ttIrpp = irpp.add(cacIrpp);
        BigDecimal mntFeicom = cacIrpp.multiply(new BigDecimal(62)).divide(new BigDecimal(100), 0, RoundingMode.HALF_UP);
        BigDecimal mntCFC = ((ligne.getCfcsal()==null)?BigDecimal.ZERO:ligne.getCfcsal()).add((ligne.getCfcpat()==null)?BigDecimal.ZERO:ligne.getCfcpat());
        BigDecimal mntDirImpots = cacIrpp.multiply(new BigDecimal(10)).divide(new BigDecimal(100), 0, RoundingMode.HALF_UP);
        BigDecimal mntReceveur2 = (ligne.getTd()==null)?BigDecimal.ZERO:ligne.getTd();
        mntReceveur2 = mntReceveur2.add(cacIrpp).multiply(new BigDecimal(28)).divide(new BigDecimal(100), 0, RoundingMode.HALF_UP);

        parameters.put("NOM_SOCIETE", "MEGATIM");
        parameters.put("ADRESSE_SOCIETE", "B.P.: 56789 YAOUNDE");
        parameters.put("CONTACT_SOCIETE", "Tél.: +237 694 45 67 23");
        parameters.put("MNT_CNPS", NumberFormat.getInstance(Locale.FRENCH).format(ligne.getCnps()));
        parameters.put("MNT_IRPP", NumberFormat.getInstance(Locale.FRENCH).format(ligne.getIrpp()));
        parameters.put("MNT_CAC_IRPP", NumberFormat.getInstance(Locale.FRENCH).format(ligne.getCacirpp()));
        parameters.put("MNT_CFC_SAL", NumberFormat.getInstance(Locale.FRENCH).format(ligne.getCfcsal()));
        parameters.put("MNT_CFC_PAT", NumberFormat.getInstance(Locale.FRENCH).format(ligne.getCfcpat()));
        parameters.put("MNT_TD", NumberFormat.getInstance(Locale.FRENCH).format(ligne.getTd()));
        parameters.put("MNT_RAV", NumberFormat.getInstance(Locale.FRENCH).format(ligne.getRav()));
        parameters.put("MNT_FNE", NumberFormat.getInstance(Locale.FRENCH).format(ligne.getFne()));
        parameters.put("TOT_IRPP_CACIRPP", NumberFormat.getInstance(Locale.FRENCH).format(ttIrpp));

        parameters.put("MNT_FEICOM", NumberFormat.getInstance(Locale.FRENCH).format(mntFeicom));
        parameters.put("MNT_RECEVEUR_2", NumberFormat.getInstance(Locale.FRENCH).format(mntReceveur2));
        parameters.put("MNT_CFC", NumberFormat.getInstance(Locale.FRENCH).format(mntCFC));
        parameters.put("MNT_DIR_IMPOTS", NumberFormat.getInstance(Locale.FRENCH).format(mntDirImpots));

        parameters.put("TOTAL_1", NumberFormat.getInstance(Locale.FRENCH).format(ttIrpp.add(ligne.getCnps()).add(mntCFC).add(ligne.getTd()).add(ligne.getRav()).add(ligne.getFne())));
        parameters.put("TOTAL_2", NumberFormat.getInstance(Locale.FRENCH).format(irpp.add(mntFeicom).add(mntReceveur2).add(mntCFC).add(ligne.getRav()).add(ligne.getFne()).add(ligne.getCnps()).add(mntDirImpots)));
        //parameters.put("CHEMIN_LOGO", "D:\\Programmation orientee objet\\Technologies\\Angular\\projets\\gestiondestock\\logo\\logo.jpg");

        String annee = periode.substring(0, 4);
        String mois = periode.substring(4, 6);
        String titreEtat = org.apache.commons.lang3.StringUtils.EMPTY;
        if("01".equalsIgnoreCase(mois)) titreEtat = "JANVIER";
        else if("02".equalsIgnoreCase(mois)) titreEtat = "JANVIER";
        else if("03".equalsIgnoreCase(mois)) titreEtat = "FEVRIER";
        else if("04".equalsIgnoreCase(mois)) titreEtat = "MARS";
        else if("05".equalsIgnoreCase(mois)) titreEtat = "AVRIL";
        else if("06".equalsIgnoreCase(mois)) titreEtat = "MAI";
        else if("07".equalsIgnoreCase(mois)) titreEtat = "JUIN";
        else if("08".equalsIgnoreCase(mois)) titreEtat = "JUILLET";
        else if("09".equalsIgnoreCase(mois)) titreEtat = "AOUT";
        else if("10".equalsIgnoreCase(mois)) titreEtat = "SEPTEMBRE";
        else if("11".equalsIgnoreCase(mois)) titreEtat = "NOVEMBRE";
        else if("12".equalsIgnoreCase(mois)) titreEtat = "DECEMBRE";
        parameters.put("PERIODE_DECLARATION", titreEtat+" "+annee);


        return parameters;
    }
}
