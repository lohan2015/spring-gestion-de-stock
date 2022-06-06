package com.kinart.api.gestiondepaie.report.parameter;

import com.kinart.api.gestiondepaie.report.LigneDeclarationVersement;
import com.kinart.paie.business.services.utils.ClsDate;

import java.util.HashMap;
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

        parameters.put("NOM_SOCIETE", "MEGATIM");
        parameters.put("ADRESSE_SOCIETE", "B.P.: 56789 YAOUNDE");
        parameters.put("CONTACT_SOCIETE", "Tél.: +237 694 45 67 23");
        parameters.put("MNTCNPS", ligne.getCnps());
        parameters.put("MNTIRPP", ligne.getIrpp());
        parameters.put("MNTCACIRPP", ligne.getCacirpp());
        parameters.put("MNTCFCSAL", ligne.getCfcsal());
        parameters.put("MNTCFCPAT", ligne.getCfcpat());
        parameters.put("MNTTD", ligne.getTd());
        parameters.put("MNTRAV", ligne.getRav());
        parameters.put("MNTFNE", ligne.getFne());
        parameters.put("CHEMIN_LOGO", "D:\\Programmation orientee objet\\Technologies\\Angular\\projets\\gestiondestock\\logo\\logo.jpg");

        return parameters;
    }
}
