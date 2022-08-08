package com.kinart.paie.business.services.impl;

import com.kinart.api.gestiondepaie.dto.*;
import com.kinart.paie.business.model.*;
import com.kinart.paie.business.repository.CalculPaieRepository;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.paie.business.services.CalculPaieService;
import com.kinart.paie.business.services.CongeFictifService;
import com.kinart.paie.business.services.DossierPaieService;
import com.kinart.paie.business.services.calcul.*;
import com.kinart.paie.business.services.cloture.*;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.ClsTreater;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.NumberUtils;
import com.kinart.paie.business.validator.CalculPaieValidator;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import com.kinart.stock.business.exception.InvalidEntityException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.Query;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class CalculPaieServiceImpl implements CalculPaieService {

    public int nbrErreur = 0;
    public int nbrTraite = 0;
    public int nbrATraiter = 0;

    private CalculPaieRepository calculPaieRepository;
    private GeneriqueConnexionService service;
    private DossierPaieService dossierPaieService;
    private ClsNomenclatureUtil nomenclatureUtil;
    private ParamDataRepository paramDataRepository;
    private CongeFictifService congeFictifService;

    @Autowired
    public CalculPaieServiceImpl(CalculPaieRepository calculPaieRepository
            , GeneriqueConnexionService service
            , DossierPaieService dossierPaieService, ClsNomenclatureUtil nomenclatureUtil
    , ParamDataRepository paramDataRepository, CongeFictifService congeFictifService) {
        this.calculPaieRepository = calculPaieRepository;
        this.dossierPaieService = dossierPaieService;
        this.service = service;
        this.nomenclatureUtil = nomenclatureUtil;
        this.paramDataRepository = paramDataRepository;
        this.congeFictifService = congeFictifService;
    }

    @Override
    public CalculPaieDto save(CalculPaieDto dto) {
        List<String> errors = CalculPaieValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("Calcul non valide {}", dto);
            throw new InvalidEntityException("Le calcul n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        return CalculPaieDto.fromEntity(
                calculPaieRepository.save(
                        CalculPaieDto.toEntity(dto)
                )
        );
    }

    @Override
    public CalculPaieDto findById(Integer id) {
        if (id == null) {
            log.error("Calcul ID is null");
            return null;
        }

        return calculPaieRepository.findById(id).map(CalculPaieDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun calcul avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public List<CalculPaieDto> findAll() {
        return calculPaieRepository.findAll().stream()
                .map(CalculPaieDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<CalculPaieDto> findByMatriculeAndPeriod(String nmat, String aamm, Integer nbul) {
        if (nmat == null) {
            log.error("Matricule salarié est null");
            return null;
        }

        return calculPaieRepository.findByMatriculeAndPeriod(nmat, aamm, nbul).stream()
                .map(CalculPaieDto::fromEntity).collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("Salarie ID is null");
            return;
        }
        calculPaieRepository.deleteById(id);
    }

    @Override
    public boolean calculPaieSalarie(RechercheDto dto) {
        // TODO
        ClsCalculLancement oCalcul = new ClsCalculLancement();
        oCalcul.setValeurCals("'O'");
        oCalcul.setNumerobulletin(dto.numeroBulletin);
        oCalcul.setPeriodepaie(dto.periodeDePaie);
        oCalcul.setIntervalle("I");
        oCalcul.setModepaiement("V");
        oCalcul.setMatriculedepart(dto.nmatMin);
        oCalcul.setMatriculearrive(dto.nmatMax);

        DossierPaie infodossier = DossierPaieDto.toEntity(this.dossierPaieService.findAll().get(0));
        if (infodossier != null)
         oCalcul.setInfodossier(infodossier);

        List<Salarie> ListeAgents = new ArrayList<Salarie>();
        oCalcul.setService(service);
        ListeAgents = oCalcul.ListeIntervalleAgent(oCalcul, dto.identreprise, dto.user, "O");
        oCalcul.setListeAgents(ListeAgents);
        String strDateFormat = dto.dateformat;

        ClsParameterOfPay parameter = new ClsParameterOfPay();

        ClsFictifParameterOfPay fictiveParameter = new ClsFictifParameterOfPay();

        boolean initfictif = true;

        //ClsNomenclatureUtil utilNomenclature = (ClsNomenclatureUtil) ServiceFinder.findBean("ClsNomenclatureUtil");

        ClsDate d = new ClsDate(dto.periodeDePaie, "yyyyMM");
        parameter.setMyMonthOfPay(d);
        parameter.setService(service);
        nomenclatureUtil.setService(service);
        nomenclatureUtil.setParamDataRepository(paramDataRepository);
        parameter.setUtilNomenclature(nomenclatureUtil);
        parameter.setLanceur(this);
        // calcul des salaires
        parameter.setDossier(dto.identreprise);
        parameter.setClas(oCalcul.getClassesalariemin());
        parameter.setUseRetroactif(false);
        parameter.setNumeroBulletin(Integer.valueOf(oCalcul.getNumerobulletin()));
        parameter.setMonthOfPay(oCalcul.getPeriodepaie());
        parameter.setMoisPaieCourant(oCalcul.getPeriodepaie());
        parameter.setPeriodOfPay(oCalcul.getPeriodepaie());
        parameter.setMyMonthOfPay(d);

        int nbrErreur = 0;
        if (!StringUtils.isNotEmpty(oCalcul.getModepaiement()))
        {
            if ("V".equals(oCalcul.getModepaiement().trim().toUpperCase()))
                parameter.setModePaiement(ClsEnumeration.EnModePaiement.V);
            else if ("E".equals(oCalcul.getModepaiement().trim().toUpperCase()))
                parameter.setModePaiement(ClsEnumeration.EnModePaiement.E);
            else if ("C".equals(oCalcul.getModepaiement().trim().toUpperCase()))
                parameter.setModePaiement(ClsEnumeration.EnModePaiement.C);
            else parameter.setModePaiement(ClsEnumeration.EnModePaiement.UNKNOWN);
        }
        else parameter.setModePaiement(ClsEnumeration.EnModePaiement.UNKNOWN);

        oCalcul.setNiveau1depart(dto.niv1Min);
        oCalcul.setNiveau1arrive(dto.niv1Max);
        oCalcul.setNiveau2depart(dto.niv2Min);
        oCalcul.setNiveau2arrive(dto.niv2Max);
        oCalcul.setNiveau3depart(dto.niv3Min);
        oCalcul.setNiveau3arrive(dto.niv3Max);
        oCalcul.setMatriculedepart(dto.nmatMin);
        oCalcul.setMatriculearrive(dto.nmatMax);
        parameter.setDepartMatricule(oCalcul.getMatriculedepart());
        parameter.setFinMatricule(oCalcul.getMatriculearrive());
        parameter.setsession_id(1322);
        parameter.setUti(dto.user);
        parameter.setDepartNiv1(oCalcul.getNiveau1depart());
        parameter.setFinNiv1(oCalcul.getNiveau1arrive());
        parameter.setDepartNiv2(oCalcul.getNiveau2depart());
        parameter.setFinNiv2(oCalcul.getNiveau2arrive());
        parameter.setDepartNiv3(oCalcul.getNiveau3depart());
        parameter.setFinNiv3(oCalcul.getNiveau3arrive());

        parameter.setAppDateFormat(strDateFormat);
        // initialisation

        String generationFichiers = "N";
        String dossierGenerationFichiers = "/";
        Integer nbrThread = NumberUtils.toInt("1");
        String synchro_traiter_salaire = "1";

        parameter.setGenfile(generationFichiers.charAt(0));
        parameter.setGenfilefolder(dossierGenerationFichiers);
        // parameter.request = request;
        // parameter.session = request.getSession();
        parameter.setLanceur(this);

        ClsTraiterSalaireThread.NBRE_AGENT_A_TRAITE = oCalcul.getListeAgents().size();
        ClsTraiterSalaireThread.NBRE_AGENT_TRAITE = 0;
        //System.out.println("NBRE DE SALARIE:================"+ClsTraiterSalaireThread.NBRE_AGENT_A_TRAITE);

        if (parameter.init())
        {
            if (StringUtils.equals("O", parameter.getFictiveCalculus()))
            {
                fictiveParameter = new ClsFictifParameterOfPay();
                BeanUtils.copyProperties(parameter, fictiveParameter);
                fictiveParameter.setFictiveMonthOfPay(parameter.getMonthOfPay());
                fictiveParameter.setMyMoisPaieCourant(parameter.getMyMoisPaieCourant().clone());
                fictiveParameter.setMyMonthOfPay(parameter.getMyMonthOfPay().clone());
                fictiveParameter.setLanceur(this);
                fictiveParameter.setCongeFictifService(this.congeFictifService);

                ClsFictifNomenclatureUtil utilNomenclatureFictif = new ClsFictifNomenclatureUtil(fictiveParameter);
                fictiveParameter.setUtilNomenclatureFictif(utilNomenclatureFictif);
                initfictif = fictiveParameter.init();
                if (!initfictif)
                {
                    nbrErreur = 1;

                    ClsTraiterSalaireThread.NBRE_AGENT_A_TRAITE = 0;
                    ClsTraiterSalaireThread.NBRE_AGENT_TRAITE = 0;
                    String message2 = ClsTreater._getResultat("**Erreur**", "INF-00972", false).getLibelle() + " ...";
                    String erreur = message2 + fictiveParameter.getError();
                    parameter.insererLogMessage(erreur);
                    return false;
                }
            }
            if (initfictif)
            {
                ClsTraiterSalaireThread.NBRE_AGENT_A_TRAITE = oCalcul.getListeAgents().size();
                ClsTraiterSalaireThread.NBRE_AGENT_TRAITE = 0;

                ClsSalariesEngine engine = new ClsSalariesEngine();
                // engine.httpSession = request.getSession();
                engine.setThreadmax(nbrThread);
                engine.setParameterOfSalary(parameter);
                engine.synchronize_traiter_salaire = StringUtils.equals("O", synchro_traiter_salaire);
                engine.setFictiveParameterOfPay(fictiveParameter);
                engine.setService(service);
                engine.setLanceur(this);
                engine.execute(oCalcul.getListeAgents());
            }
//            if (parameter.getGenfile() == 'O')
//                StringUtils.printOutObject(parameter, parameter.getGenfilefolder() + "\\parametresCalcul.txt");

            // *****on attend jusqu'a la fin du calcul de tous les bulletins
            while (!this.finCalcul())
            {
                // if (engine.nbrSalaries == engine.nbrSalariesTraite)
                // {
                // System.out.println("Fin calcul des bulletins du mois de " + periode);
                // break;
                // }
                // System.out.println("Calcul du salarié "+engine.nbrSalariesTraite+"/"+engine.nbrSalaries);
//                if (progression != null)
//                {
//                    progression.debutAffichageProgressBar(this.nbrATraiter);
//                    progression.mettreAJourProgressBar(this.nbrTraite);
//                }
            }
//            if (progression != null)
//            {
//                progression.debutAffichageProgressBar(this.nbrATraiter);
//                progression.mettreAJourProgressBar(this.nbrATraiter);
//            }
            //System.out.println("------------FIN CALCUL BULLETINS-------------");

            return true;

        }
        else
        {
            nbrErreur = 1;

            return false;
        }
    }

    public boolean finCalcul()
    {
        //System.out.println("Dans simulateur, A Traiter = "+this.nbrATraiter+" et Traité = "+this.nbrTraite);
        if(this.nbrTraite >= this.nbrATraiter) return true;

        return false;
    }

    @Override
    public List<CalculPaieDto> findResultCalculByFilter(RechercheDto dto) {
        List<CalculPaieDto> liste = new ArrayList<CalculPaieDto>();
        String sqlQuery = "SELECT e.*, s.nom as nomsal, s.pren as prensal, t.lrub as librub, t.typr as typerub " +
                "FROM CalculPaie e " +
                "LEFT JOIN Salarie s ON (e.identreprise=s.identreprise AND e.nmat=s.nmat) "+
                "LEFT JOIN ElementSalaire t ON (t.identreprise=s.identreprise AND t.crub=e.rubq) "+
                "WHERE 1=1";

        if(StringUtils.isNotEmpty(dto.nmatMin)) sqlQuery += " AND e.nmat >= '"+dto.nmatMin+"'";
        if(StringUtils.isNotEmpty(dto.nmatMax)) sqlQuery += " AND e.nmat <= '"+dto.nmatMin+"'";

        if(StringUtils.isNotEmpty(dto.niv1Min)) sqlQuery += " AND e.niv1 >= '"+dto.niv1Min+"'";
        if(StringUtils.isNotEmpty(dto.niv1Max)) sqlQuery += " AND e.niv1 <= '"+dto.niv1Max+"'";

        if(StringUtils.isNotEmpty(dto.niv2Min)) sqlQuery += " AND e.niv2 >= '"+dto.niv2Min+"'";
        if(StringUtils.isNotEmpty(dto.niv2Max)) sqlQuery += " AND e.niv2 <= '"+dto.niv2Max+"'";

        if(StringUtils.isNotEmpty(dto.niv3Min)) sqlQuery += " AND e.niv3 >= '"+dto.niv3Min+"'";
        if(StringUtils.isNotEmpty(dto.niv3Max)) sqlQuery += " AND e.niv3 <= '"+dto.niv3Max+"'";

        if(!dto.allRub) sqlQuery += " AND t.comp = 'O'";
        sqlQuery += " AND e.aamm = '"+dto.periodeDePaie+"'";
        sqlQuery += " AND e.nbul = "+dto.numeroBulletin;

        sqlQuery += " ORDER BY e.nmat, e.rubq ASC ";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", CalculPaie.class)
                    .addScalar("nomsal", StandardBasicTypes.STRING)
                    .addScalar("prensal", StandardBasicTypes.STRING)
                    .addScalar("librub", StandardBasicTypes.STRING)
                    .addScalar("typerub", StandardBasicTypes.STRING);

            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                CalculPaie evDB = (CalculPaie)o[0];
                CalculPaieDto evDto = CalculPaieDto.fromEntity(evDB);
                if(o[1]!=null) evDto.setNomSalarie(o[1].toString());
                if(o[2]!=null) evDto.setNomSalarie(evDto.getNomSalarie()+" "+o[2].toString());
                if(o[3]!=null) evDto.setLibRubrique(o[3].toString());
                if(o[4]!=null) evDto.setTypeRubrique(o[4].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return liste;
    }

    @Override
    public List<SalarieDto> findListeSalarieByFilter(RechercheDto dto) {
        List<SalarieDto> liste = new ArrayList<SalarieDto>();
        String sqlQuery = "SELECT e.*, s.vall as libniv1, t.vall as libniv2, d.vall as libniv3 " +
                ", c.vall as libcateg, f.vall as libfonc, g.vall as libech " +
                ", h.vall as libgrade, i.vall as libsexe, j.vall as libsitf " +
                "FROM Salarie e " +
                "LEFT JOIN Paramdata s ON (e.identreprise=s.identreprise AND e.niv1=s.cacc and s.ctab=1 and s.nume=1) "+
                "LEFT JOIN Paramdata t ON (e.identreprise=t.identreprise AND e.niv2=t.cacc and t.ctab=2 and t.nume=1) "+
                "LEFT JOIN Paramdata d ON (e.identreprise=d.identreprise AND e.niv3=d.cacc and d.ctab=3 and d.nume=1) "+
                "LEFT JOIN Paramdata f ON (e.identreprise=f.identreprise AND e.fonc=t.cacc and f.ctab=7 and f.nume=1) "+
                "LEFT JOIN Paramdata c ON (e.identreprise=c.identreprise AND e.cat=t.cacc and c.ctab=132 and c.nume=1) "+
                "LEFT JOIN Paramdata g ON (e.identreprise=g.identreprise AND e.ech=g.cacc and g.ctab=133 and g.nume=1) "+
                "LEFT JOIN Paramdata h ON (e.identreprise=h.identreprise AND e.grad=h.cacc and h.ctab=6 and h.nume=1) "+
                "LEFT JOIN Paramdata i ON (e.identreprise=i.identreprise AND e.sexe=i.cacc and i.ctab=205 and i.nume=1) "+
                "LEFT JOIN Paramdata j ON (e.identreprise=j.identreprise AND e.sitf=j.cacc and j.ctab=13 and j.nume=1) "+
                "WHERE 1=1";

        if(StringUtils.isNotEmpty(dto.nmatMin)) sqlQuery += " AND e.nmat >= '"+dto.nmatMin+"'";
        if(StringUtils.isNotEmpty(dto.nmatMax)) sqlQuery += " AND e.nmat <= '"+dto.nmatMin+"'";

        if(StringUtils.isNotEmpty(dto.niv1Min)) sqlQuery += " AND e.niv1 >= '"+dto.niv1Min+"'";
        if(StringUtils.isNotEmpty(dto.niv1Max)) sqlQuery += " AND e.niv1 <= '"+dto.niv1Max+"'";

        if(StringUtils.isNotEmpty(dto.niv2Min)) sqlQuery += " AND e.niv2 >= '"+dto.niv2Min+"'";
        if(StringUtils.isNotEmpty(dto.niv2Max)) sqlQuery += " AND e.niv2 <= '"+dto.niv2Max+"'";

        if(StringUtils.isNotEmpty(dto.niv3Min)) sqlQuery += " AND e.niv3 >= '"+dto.niv3Min+"'";
        if(StringUtils.isNotEmpty(dto.niv3Max)) sqlQuery += " AND e.niv3 <= '"+dto.niv3Max+"'";

        sqlQuery += " ORDER BY e.nmat ASC ";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", Salarie.class)
                    .addScalar("libniv1", StandardBasicTypes.STRING).addScalar("libniv2", StandardBasicTypes.STRING)
                    .addScalar("libniv3", StandardBasicTypes.STRING)
                    .addScalar("libcateg", StandardBasicTypes.STRING).addScalar("libfonc", StandardBasicTypes.STRING)
                    .addScalar("libech", StandardBasicTypes.STRING).addScalar("libgrade", StandardBasicTypes.STRING)
                    .addScalar("libsexe", StandardBasicTypes.STRING).addScalar("libsitf", StandardBasicTypes.STRING);

            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                Salarie evDB = (Salarie)o[0];
                SalarieDto evDto = SalarieDto.fromEntity(evDB);
                if(o[1]!=null) evDto.setLibniv1(o[1].toString());
                if(o[2]!=null) evDto.setLibniv2(o[2].toString());
                if(o[3]!=null) evDto.setLibniv3(o[3].toString());
                if(o[4]!=null) evDto.setLibcategorie(o[4].toString());
                if(o[5]!=null) evDto.setLibfonction(o[5].toString());
                if(o[6]!=null) evDto.setLibechelon(o[6].toString());
                if(o[7]!=null) evDto.setLibgrade(o[7].toString());
                if(o[8]!=null) evDto.setLibsexe(o[8].toString());
                if(o[9]!=null) evDto.setLibsitfam(o[9].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return liste;
    }

    public int getNbrErreur() {
        return nbrErreur;
    }

    public void setNbrErreur(int nbrErreur) {
        this.nbrErreur = nbrErreur;
    }

    public int getNbrTraite() {
        return nbrTraite;
    }

    public void setNbrTraite(int nbrTraite) {
        this.nbrTraite = nbrTraite;
    }

    public int getNbrATraiter() {
        return nbrATraiter;
    }

    public void setNbrATraiter(int nbrATraiter) {
        this.nbrATraiter = nbrATraiter;
    }

    public boolean cloturePaie(RechercheDto dto, HttpServletRequest request){
        ClsUpdateBulletin update = new ClsUpdateBulletin();

        ClsParameterOfPay parameter =  new ClsParameterOfPay();

        ClsCentralisationService centraservice = new ClsCentralisationService();
        centraservice.init(request, service, dto.identreprise);

        ClsUpdateBulletinService updateservice = new ClsUpdateBulletinService();
        updateservice.setService(service);

        String cdos = dto.identreprise;
        Date moispaie = new ClsDate(dto.periodeDePaie+"01", "yyyyMMdd").getDate();

        ClsDate myDate = new ClsDate(moispaie);

        update.dateformat = dto.dateformat;
        update.updateservice = updateservice;

        // ///////////////////////////////INITIALISATION DES PARAMETRES DU TRAITEMENT/////////////////////////////

        ClsInfoCloture search = new ClsInfoCloture();

        search.setAamm(myDate.getDateS(ClsParameterOfPay.FORMAT_DATE_PAY_PERIOD_YYYYMM));
        ClsBulletin bulletin = new ClsBulletin(service);
        //
        Date d = myDate.getLastDayOfMonth();

        ParamData fnom = centraservice.chercherEnNomenclature(cdos, 91, myDate.getYearAndMonth(), 3);

        if (fnom != null && StringUtils.isNotBlank(fnom.getVall()))
            d = new ClsDate(fnom.getVall(), "dd/MM/yyyy").getDate();

        search.setDatecompta(new ClsDate(d).getDateS(dto.dateformat));
        search.setDatevaleur(new ClsDate(d).getDateS(dto.dateformat));
        //
        ClsBulletin.InfoBulletin info = bulletin.rechercheBulletin(cdos, search.getAamm());
        search.setNbul(info.getNbul());
        //
        String filepath = null;
        ParamData oNomePAIEINTER = service.findAnyColumnFromNomenclature(cdos, "", "99", "PAIE-INTER", "2");
        if (StringUtils.isEmpty(oNomePAIEINTER.getVall()))
            filepath = org.springframework.util.StringUtils.cleanPath("./generated-reports/");
        else filepath = oNomePAIEINTER.getVall();

        oNomePAIEINTER = service.findAnyColumnFromNomenclature(cdos, "", "99", "PAIE-INTER", "3");
        if (StringUtils.isNotBlank(oNomePAIEINTER.getVall()))
            filepath += oNomePAIEINTER.getVall();

        search.setFilepath(filepath);

        String trait = dto.typetrt;//ClsInfoCloture.ALL;

        search.setActiontoexec(trait);

        // ///////////////////////////////FIN INITIALISATION DES PARAMETRES DU TRAITEMENT/////////////////////////////

        String aamm = dto.periodeDePaie;
        String datecompta = search.getDatecompta();
        int nbul = search.getNbul();
        String traitementlance = search.getActiontoexec();

        parameter.setMonthOfPay(aamm);

        parameter.setDossier(cdos);
        parameter.setNumeroBulletin(nbul);
        parameter.setMoisPaieCourant(aamm);
        parameter.setPeriodOfPay(aamm);

        String generationFichiers = "O";
        String dossierGenerationFichiers = org.springframework.util.StringUtils.cleanPath("./generated-reports/");

        String fusionRhtcongeagent = "O";
        update.fusionRhtcongeagent = fusionRhtcongeagent;

        parameter.setGenfile(generationFichiers.charAt(0));
        parameter.setGenfilefolder(dossierGenerationFichiers);

        // parameter.session = request.getSession();

        Integer nbrThread = 5;
        parameter.setThreadmax(nbrThread);
        //
        String session = dto.identreprise+"-"+dto.user;
        centraservice.setRubriquesMap(new HashMap<String, ElementSalaire>());
        centraservice.setRhfnomsMap(new HashMap<String, ParamData>());
        Integer nddd = 0;
        try
        {
            List<DossierPaie> list2 = service.find("from DossierPaie where idEntreprise = '" + cdos + "'");
            if (list2 != null && list2.size() > 0)
            {
                DossierPaie dossierInfo = list2.get(0);
                nddd = dossierInfo.getNddd() == null ? 0 : dossierInfo.getNddd().intValue();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        ClsGlobalUpdate globalUpdate = new ClsGlobalUpdate(request, service, centraservice, parameter, cdos, aamm, "0001", dto.user, nbul, new ClsDate(datecompta, dto.dateformat).getDate(), nddd, session,
                update);
        // globalUpdate.ui = this;
        String execExternalFunctions = "O";
        globalUpdate.setExecExternalFunction("O".equalsIgnoreCase(execExternalFunctions) ? true : false);
        globalUpdate.init();
        globalUpdate.printInitParameters();

        boolean result = true;

        if (ClsInfoCloture.CENTRA.equals(traitementlance))
            result = globalUpdate.lancerCentralisation();

        if (ClsInfoCloture.GENFICASCII.equals(traitementlance))
            globalUpdate.genrateMouvement();

        if (ClsInfoCloture.MAJ.equals(traitementlance))
            result = globalUpdate.lancerMiseAJour();

        if (ClsInfoCloture.ALL.equals(traitementlance))
            result = globalUpdate.execute();

        String error = globalUpdate.getError();

        return true;
    }

    @Override
    public List<CumulPaieDto> findCumulByFilter(RechercheDto dto) {
        List<CumulPaieDto> liste = new ArrayList<CumulPaieDto>();
        String sqlQuery = "SELECT e.*, s.nom as nomsal, s.pren as prensal, t.lrub as librub, t.typr as typerub " +
                "FROM HistoCalculPaie e " +
                "LEFT JOIN Salarie s ON (e.identreprise=s.identreprise AND e.nmat=s.nmat) "+
                "LEFT JOIN ElementSalaire t ON (t.identreprise=s.identreprise AND t.crub=e.rubq) "+
                "WHERE 1=1";

        if(StringUtils.isNotEmpty(dto.nmatMin)) sqlQuery += " AND e.nmat >= '"+dto.nmatMin+"'";
        if(StringUtils.isNotEmpty(dto.nmatMax)) sqlQuery += " AND e.nmat <= '"+dto.nmatMin+"'";

        if(StringUtils.isNotEmpty(dto.niv1Min)) sqlQuery += " AND e.niv1 >= '"+dto.niv1Min+"'";
        if(StringUtils.isNotEmpty(dto.niv1Max)) sqlQuery += " AND e.niv1 <= '"+dto.niv1Max+"'";

        if(StringUtils.isNotEmpty(dto.niv2Min)) sqlQuery += " AND e.niv2 >= '"+dto.niv2Min+"'";
        if(StringUtils.isNotEmpty(dto.niv2Max)) sqlQuery += " AND e.niv2 <= '"+dto.niv2Max+"'";

        if(StringUtils.isNotEmpty(dto.niv3Min)) sqlQuery += " AND e.niv3 >= '"+dto.niv3Min+"'";
        if(StringUtils.isNotEmpty(dto.niv3Max)) sqlQuery += " AND e.niv3 <= '"+dto.niv3Max+"'";

        if(!dto.allRub) sqlQuery += " AND t.comp = 'O'";
        sqlQuery += " AND e.aamm = '"+dto.periodeDePaie+"'";
        sqlQuery += " AND e.nbul = "+dto.numeroBulletin;

        sqlQuery += " ORDER BY e.nmat, e.rubq ASC ";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", HistoCalculPaie.class)
                    .addScalar("nomsal", StandardBasicTypes.STRING)
                    .addScalar("prensal", StandardBasicTypes.STRING)
                    .addScalar("librub", StandardBasicTypes.STRING)
                    .addScalar("typerub", StandardBasicTypes.STRING);

            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                HistoCalculPaie evDB = (HistoCalculPaie)o[0];
                CumulPaieDto evDto = CumulPaieDto.fromEntity(evDB);
                if(o[1]!=null) evDto.setNomSalarie(o[1].toString());
                if(o[2]!=null) evDto.setNomSalarie(evDto.getNomSalarie()+" "+o[2].toString());
                if(o[3]!=null) evDto.setLibRubrique(o[3].toString());
                if(o[4]!=null) evDto.setTypeRubrique(o[4].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return liste;
    }


}
