package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.dto.RechercheDto;
import com.kinart.api.gestiondepaie.dto.ResultatPrecentralisationDto;
import com.kinart.api.gestiondepaie.controller.api.PrecentralisationApi;
import com.kinart.paie.business.services.precentralisation.ClsAxPrecentralisation;
import com.kinart.paie.business.services.precentralisation.ClsInfoPrecentPaie;
import com.kinart.paie.business.services.precentralisation.ClsRhtLogEtendu;
import com.kinart.paie.business.services.precentralisation.ClsSessionObjectName;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
public class PrecentralisationController implements PrecentralisationApi {

    private GeneriqueConnexionService generiqueConnexionService;

    @Autowired
    public PrecentralisationController(GeneriqueConnexionService generiqueConnexionService) {
        this.generiqueConnexionService = generiqueConnexionService;
    }

    @Override
    public ResultatPrecentralisationDto lancerPrecentralisation(RechercheDto dto, HttpServletRequest request) {
        ClsInfoPrecentPaie oPrecentralisation = new ClsInfoPrecentPaie();
        oPrecentralisation.setAamm(dto.periodeDePaie);
        oPrecentralisation.setVerif_edit_bul("N");
        oPrecentralisation.setRub_a_comptabilise("O");
        oPrecentralisation.setContinu_si_erreur("N");
        oPrecentralisation.setOnlyerrormessage("O");
        oPrecentralisation.setNmat(dto.nmatMin);
        oPrecentralisation.setValeurnmat(dto.nmatMin);
        oPrecentralisation.setNbul(dto.numeroBulletin);
        oPrecentralisation.setCdos(dto.identreprise);

        ClsAxPrecentralisation axPrecentralisation = new ClsAxPrecentralisation();// (ClsAxPrecentralisation) ServiceFinder.findBean("axPrecentralisationService");
        axPrecentralisation.setService(generiqueConnexionService);
        axPrecentralisation.checkcostcenter = false;
        axPrecentralisation.checkemail = false;

        if (axPrecentralisation.initTraitement(request, oPrecentralisation.getAamm(), oPrecentralisation.getNbul(), oPrecentralisation.getNmat(),
                oPrecentralisation.getVerif_edit_bul(), oPrecentralisation.getRub_a_comptabilise(), oPrecentralisation.getContinu_si_erreur(),
                oPrecentralisation.getOnlyerrormessage()))
        {
            axPrecentralisation.oInitialisationPrecentralisation.setCdos(dto.identreprise);
            int nbr = axPrecentralisation.oInitialisationPrecentralisation.getListeOfSalary().size();
            if(nbr != 0)
            {
                request.getSession().setAttribute(ParameterUtil.SESSION_DOSSIER, dto.identreprise+"");
                request.getSession().setAttribute(ParameterUtil.SESSION_LANGUE, "001");
                request.getSession().setAttribute(ParameterUtil.SESSION_LOGIN, dto.user+"");
                axPrecentralisation.traitementPrecentralisation(dto.identreprise+"", request, oPrecentralisation.getAamm(), oPrecentralisation.getNbul(),
                        oPrecentralisation.getNmat(), oPrecentralisation.getVerif_edit_bul(), oPrecentralisation.getRub_a_comptabilise(), oPrecentralisation
                                .getContinu_si_erreur(), oPrecentralisation.getOnlyerrormessage());
                List<ClsRhtLogEtendu> listeLogEtendu = (List<ClsRhtLogEtendu>)request.getSession().getAttribute(ClsSessionObjectName.SESSION_O_PRECENTRALISATION_LISTE);
                ResultatPrecentralisationDto result = new ResultatPrecentralisationDto();
                if(listeLogEtendu != null)
                    result.setMessage(listeLogEtendu.get(0).getRhtlog().getLigne());
                return result;
            }
            else
            {
                ResultatPrecentralisationDto result = new ResultatPrecentralisationDto();
                result.setMessage("Aucunes données retournées par  votre sélection.");
                return result;
            }
        }

        return null;
    }
}
