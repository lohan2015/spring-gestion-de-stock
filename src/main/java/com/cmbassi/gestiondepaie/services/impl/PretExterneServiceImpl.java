package com.cmbassi.gestiondepaie.services.impl;

import com.cmbassi.gestiondepaie.dto.*;
import com.cmbassi.gestiondepaie.model.ElementVariableDetailMois;
import com.cmbassi.gestiondepaie.model.PretExterneDetail;
import com.cmbassi.gestiondepaie.model.PretExterneEntete;
import com.cmbassi.gestiondepaie.model.TypePret;
import com.cmbassi.gestiondepaie.repository.PretExterneDetailRepository;
import com.cmbassi.gestiondepaie.repository.PretExterneEnteteRepository;
import com.cmbassi.gestiondepaie.repository.TypePretRepository;
import com.cmbassi.gestiondepaie.services.PretExterneService;
import com.cmbassi.gestiondepaie.services.utils.ClsDate;
import com.cmbassi.gestiondepaie.services.utils.GeneriqueConnexionService;
import com.cmbassi.gestiondepaie.services.utils.ParameterUtil;
import com.cmbassi.gestiondestock.exception.EntityNotFoundException;
import com.cmbassi.gestiondestock.exception.ErrorCodes;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class PretExterneServiceImpl implements PretExterneService {

    private TypePretRepository typePretRepository;
    private PretExterneEnteteRepository pretExterneEnteteRepository;
    private PretExterneDetailRepository pretExterneDetailRepository;
    private GeneriqueConnexionService service;

    PretDto OUT_PRET = null;

    @Autowired
    public PretExterneServiceImpl(GeneriqueConnexionService service, TypePretRepository typePretRepository, PretExterneEnteteRepository pretExterneEnteteRepository, PretExterneDetailRepository pretExterneDetailRepository) {
        this.typePretRepository = typePretRepository;
        this.pretExterneEnteteRepository = pretExterneEnteteRepository;
        this.pretExterneDetailRepository = pretExterneDetailRepository;
        this.service = service;
    }

    @Override
    public  List<LigneEcheancierDto> loadEchancier(String numPret){
        List<LigneEcheancierDto> listeDetailPret = new ArrayList<LigneEcheancierDto>();
        List<PretExterneDetail> detailPret = pretExterneDetailRepository.findPretDetailByNumero(numPret);
        if(detailPret!=null){
            for(PretExterneDetail detail : detailPret){
                LigneEcheancierDto dto = new LigneEcheancierDto();
                dto.setDtDate(detail.getPerb());
                dto.setBgAmortissement(detail.getEcho());
                dto.setBgInterest(detail.getEchr());
                dto.setBgTaxe(detail.getInte());
                dto.setBgTaxe(detail.getInte());

                listeDetailPret.add(dto);
            }
        }

        return listeDetailPret;
    }

    @Override
    public List<PretExterneEnteteDto> findPretEntetePretByFilter(Optional<String> matricule, Optional<String> nprt, Optional<String> crub) {
        List<PretExterneEnteteDto> liste = new ArrayList<PretExterneEnteteDto>();
        String sqlQuery = "SELECT e.*, s.nom as nomsal, s.pren as prensal, t.lrub as librub " +
                "FROM PretExterneEntete e " +
                "LEFT JOIN Salarie s ON (e.identreprise=s.identreprise AND e.nmat=s.nmat) "+
                "LEFT JOIN ElementSalaire t ON (t.identreprise=s.identreprise AND t.crub=e.crub) "+
                "WHERE 1=1";

        if(matricule.isPresent()) sqlQuery += " AND upper(e.nmat) LIKE :matricule";
        if(nprt.isPresent()) sqlQuery += " AND e.nprt = :nprt";
        if(crub.isPresent()) sqlQuery += " AND e.crub = :crub";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", PretExterneEntete.class)
                    .addScalar("nomsal", StandardBasicTypes.STRING)
                    .addScalar("prensal", StandardBasicTypes.STRING)
                    .addScalar("librub", StandardBasicTypes.STRING);

            //query.setParameter("identreprise", identreprise);
            if(matricule.isPresent()) query.setParameter("matricule", "%"+matricule+"%");
            if(nprt.isPresent()) query.setParameter("nprt", nprt);
            if(crub.isPresent()) query.setParameter("crub", crub);
            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                PretExterneEntete evDB = (PretExterneEntete)o[0];
                PretExterneEnteteDto evDto = PretExterneEnteteDto.fromEntity(evDB);
                if(o[1]!=null) evDto.setNomsalarie(o[1].toString());
                if(o[2]!=null) evDto.setNomsalarie(evDto.getNomsalarie()+" "+o[2].toString());
                if(o[3]!=null) evDto.setLibrubrique(o[3].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return liste;
    }

    @Override
    public PretExterneEnteteDto save(PretExterneEnteteDto dto, List<LigneEcheancierDto> echeancier) {
        if(!StringUtils.hasLength(dto.getNprt()) ){
            // Création
            dto.setDcrp(new Date());
            String result = null;
            try {
                TypePretDto typePretDto = this.recupererNaturePret(dto.getIdEntreprise(), dto.getCodenatpret());
                String prochainIncrement = service.getProchainIncrement (typePretDto.getCodecompteur() );
                String souche = service.soucheCompteur (dto.getIdEntreprise() , typePretDto.getCodecompteur() );
                dto.setNprt(souche + prochainIncrement);
                result = souche + prochainIncrement;
                service.incrementerCompteur (typePretDto.getCodecompteur());
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
            pretExterneEnteteRepository.save(PretExterneEnteteDto.toEntity(dto));

            if(echeancier != null && echeancier.size() > 0)
            {
                PretExterneDetailDto pretExterneDetailDto = null;

                for(LigneEcheancierDto line : echeancier)
                {
                    if("4##TOTAL :".equalsIgnoreCase(line.getStrDate()))
                        continue;

                    try {
                        pretExterneDetailDto = new PretExterneDetailDto();
                        pretExterneDetailDto.setIdEntreprise(dto.getIdEntreprise());
                        pretExterneDetailDto.setPerb(line.getDtDate());
                        pretExterneDetailDto.setEcho(line.getBgAmortissement());
                        pretExterneDetailDto.setEchr(line.getBgInterest());
                        pretExterneDetailDto.setInte(line.getBgTaxe());
                        pretExterneDetailDto.setNbul(Integer.valueOf(9));
                        pretExterneDetailDto.setNprt(result);
                        pretExterneDetailRepository.save(PretExterneDetailDto.toEntity(pretExterneDetailDto));
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        continue;
                    }
                }
            }

            return dto;
        } else {
            // Modification
            pretExterneEnteteRepository.save(PretExterneEnteteDto.toEntity(dto));
            pretExterneDetailRepository.deleteDetailPretByNumero(dto.getNprt());
            if(echeancier != null && echeancier.size() > 0)
            {
                PretExterneDetailDto pretExterneDetailDto = null;

                for(LigneEcheancierDto line : echeancier)
                {
                    if("4##TOTAL :".equalsIgnoreCase(line.getStrDate()))
                        continue;

                    try {
                       pretExterneDetailDto = new PretExterneDetailDto();
                        pretExterneDetailDto.setIdEntreprise(dto.getIdEntreprise());
                        pretExterneDetailDto.setPerb(line.getDtDate());
                        pretExterneDetailDto.setEcho(line.getBgAmortissement());
                        pretExterneDetailDto.setEchr(line.getBgInterest());
                        pretExterneDetailDto.setInte(line.getBgTaxe());
                        pretExterneDetailDto.setNbul(Integer.valueOf(9));
                        pretExterneDetailDto.setNprt(dto.getNprt());
                        pretExterneDetailRepository.save(PretExterneDetailDto.toEntity(pretExterneDetailDto));
                    } catch (Exception e) {
                        // TODO: handle exception
                        e.printStackTrace();
                        continue;
                    }
                }
            }

            return dto;
        }
    }

    @Override
    public List<LigneEcheancierDto> generateEcheancier(PretExterneEnteteDto dto) {
        init_variables(dto);
        LigneEcheancierDto LigneEcheancier = null;
        for (int i = 1; i <= OUT_PRET.NombreEcheance; i++)
        {
            try
            {
                LigneEcheancier = new LigneEcheancierDto();
                LigneEcheancier.setDtDate(OUT_PRET.DateProchaineEcheance);

                if (i == OUT_PRET.NombreEcheance)
                    LigneEcheancier.setResteInitial(OUT_PRET.ResteInitial);
                else
                    LigneEcheancier.setResteInitial(new BigDecimal(0));

                LigneEcheancier.setBgMontant(this._formatMoney(OUT_PRET.MontantEcheance.add(LigneEcheancier.getResteInitial())));
                LigneEcheancier.setBgInterest(this._formatMoney(LigneEcheancier.getBgMontant().multiply(OUT_PRET.TauxInteret)));
                LigneEcheancier.setBgTaxe(this._formatMoney(LigneEcheancier.getBgInterest().multiply(OUT_PRET.TauxTaxe)));
                LigneEcheancier.setBgAmortissement(this._formatMoney(LigneEcheancier.getBgMontant().add(
                        LigneEcheancier.getBgInterest().add(LigneEcheancier.getBgTaxe()))));
                LigneEcheancier.setBgResteARembourser(this._formatMoney(OUT_PRET.ResteArembourser.subtract(LigneEcheancier.getBgMontant())));
                OUT_PRET.ResteArembourser = this._formatMoney(OUT_PRET.ResteArembourser.subtract(LigneEcheancier.getBgMontant()));
                OUT_PRET.DateProchaineEcheance = recupererDateProchaineEcheance(OUT_PRET.DateProchaineEcheance, OUT_PRET.listeMoisNRemb);

                genererRecapitulatifLigne(LigneEcheancier);

                genererStrLigne(LigneEcheancier);
                ajouterLigneEcheancierDansPret(LigneEcheancier);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                continue;
            }
        }

        genererstrRecapitulatif();

        LigneEcheancierDto total = new LigneEcheancierDto();
        total.setStrDate("4##TOTAL :");
        total.setStrMontant(OUT_PRET.getMontant());
        total.setStrAmortissement(OUT_PRET.getAmortissement());
        total.setStrResteARembourser(OUT_PRET.getResteARembourser());
        total.setStrInteret(OUT_PRET.getInteret());
        total.setStrTaxe(OUT_PRET.getTaxe());
        ajouterLigneEcheancierDansPret(total);

        return OUT_PRET.listeEcheancier;
    }

    public void ajouterLigneEcheancierDansPret(LigneEcheancierDto LigneEcheancier)
    {
        OUT_PRET.listeEcheancier.add(LigneEcheancier);
    }

    public void genererRecapitulatifLigne(LigneEcheancierDto LigneEcheancier)
    {
        OUT_PRET.bgAmortissement = OUT_PRET.bgAmortissement.add(LigneEcheancier.getBgAmortissement());
        OUT_PRET.bgMontant = OUT_PRET.bgMontant.add(LigneEcheancier.getBgMontant());
        OUT_PRET.bgInteret = OUT_PRET.bgInteret.add(LigneEcheancier.getBgInterest());
        OUT_PRET.bgTaxe = OUT_PRET.bgTaxe.add(LigneEcheancier.getBgTaxe());
        //OUT_PRET.bgResteARembourser = OUT_PRET.bgResteARembourser.add(LigneEcheancier.getBgResteARembourser());
        //on ajoute le montant ssi l'échéance est sup ou égal au mois de paie en cours
        String anneemois = new ClsDate(LigneEcheancier.getDtDate()).getYearAndMonth();
        if(anneemois.compareTo(OUT_PRET.entetepret.aamm)>=0)
            OUT_PRET.bgResteARembourser = OUT_PRET.bgResteARembourser.add(LigneEcheancier.getBgMontant());
    }

    public void genererstrRecapitulatif()
    {
        try
        {
            OUT_PRET.montant = this.formatMoney(OUT_PRET.bgMontant);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            OUT_PRET.amortissement = this.formatMoney(OUT_PRET.bgAmortissement);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            OUT_PRET.interet = this.formatMoney(OUT_PRET.bgInteret);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            OUT_PRET.resteARembourser = this.formatMoney(OUT_PRET.bgResteARembourser);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            OUT_PRET.taxe = this.formatMoney(OUT_PRET.bgTaxe);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }

    public void genererStrLigne(LigneEcheancierDto LigneEcheancier)
    {
        try
        {
            LigneEcheancier.setStrMontant(this.formatMoney(LigneEcheancier.getBgMontant()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            LigneEcheancier.setStrAmortissement(this.formatMoney(LigneEcheancier.getBgAmortissement()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            LigneEcheancier.setStrInteret(this.formatMoney(LigneEcheancier.getBgInterest()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            LigneEcheancier.setStrTaxe(this.formatMoney(LigneEcheancier.getBgTaxe()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            LigneEcheancier.setStrResteARembourser(this.formatMoney(LigneEcheancier.getBgResteARembourser()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        try
        {
            LigneEcheancier.setStrDate(OUT_PRET.entetepret.dateFormat.format(LigneEcheancier.getDtDate()));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

    }


    public void init_variables(PretExterneEnteteDto P_OBJECT_PRET)
    {
        try
        {
            OUT_PRET = new PretDto();
            OUT_PRET.entetepret = P_OBJECT_PRET;
            OUT_PRET.NATURE_PRET = recupererNaturePret(P_OBJECT_PRET.getIdEntreprise(), P_OBJECT_PRET.getCodenatpret());
            OUT_PRET.TauxInteret = recupererTauxInteret(P_OBJECT_PRET, OUT_PRET.NATURE_PRET);
            OUT_PRET.TauxTaxe = recupererTauxTaxe(P_OBJECT_PRET, OUT_PRET.NATURE_PRET);
            OUT_PRET.MontantPret = P_OBJECT_PRET.getMntp();
            OUT_PRET.NombreEcheance = Integer.valueOf(P_OBJECT_PRET.getIntNbreEch()); //P_OBJECT_PRET.getNbec();
            OUT_PRET.DatePremiereEcheance = P_OBJECT_PRET.getDpec();
            OUT_PRET.ResteArembourser = OUT_PRET.MontantPret;
            OUT_PRET.listeMoisNRemb = recupererMoisNonRemboursables(OUT_PRET.NATURE_PRET);

            if (P_OBJECT_PRET.getMtec() == null || (P_OBJECT_PRET.getMtec() != null && P_OBJECT_PRET.getMtec().compareTo(new BigDecimal(0)) == 0))
                OUT_PRET.MontantEcheance = this._formatMoney(new BigDecimal(OUT_PRET.MontantPret.doubleValue() / OUT_PRET.NombreEcheance));
            else
            {
                OUT_PRET.MontantEcheance = P_OBJECT_PRET.getMtec();
                if (OUT_PRET.MontantEcheance.compareTo(new BigDecimal(0)) != 0)
                    OUT_PRET.NombreEcheance = new BigDecimal(Math.ceil(OUT_PRET.MontantPret.doubleValue() / OUT_PRET.MontantEcheance.doubleValue())).intValue();
            }
            OUT_PRET.ResteInitial = OUT_PRET.MontantPret.subtract(OUT_PRET.MontantEcheance.multiply(new BigDecimal(OUT_PRET.NombreEcheance)));

            OUT_PRET.DateProchaineEcheance = recupererDatePremiereEcheance(OUT_PRET.DatePremiereEcheance, OUT_PRET.listeMoisNRemb);
            OUT_PRET.TauxInteret = this._formatMoney(new BigDecimal(OUT_PRET.TauxInteret.doubleValue()/100));
            OUT_PRET.TauxTaxe = this._formatMoney(new BigDecimal(OUT_PRET.TauxTaxe.doubleValue()/100));
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    public BigDecimal _formatMoney(Double value)
    {
        return new BigDecimal(value).setScale(OUT_PRET.entetepret.intNombreDeDecimales, RoundingMode.HALF_UP);
    }

    public BigDecimal _formatMoney(BigDecimal value)
    {
        return value.setScale(OUT_PRET.entetepret.intNombreDeDecimales, RoundingMode.HALF_UP);
    }

    public TypePretDto recupererNaturePret(Integer idEntreprise, String P_CODE_NATURE_PRET)
    {
        try
        {
            TypePret typePret = typePretRepository.findByCodeNature(P_CODE_NATURE_PRET);
            if(typePret!=null) return TypePretDto.fromEntity(typePret);
            return null;
        }
        catch (DataAccessException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    BigDecimal recupererTauxInteret(PretExterneEnteteDto P_OBJECT_PRET, TypePretDto NATURE_PRET)
    {
        BigDecimal TauxInteret = new BigDecimal(0);
        if (!"N".equalsIgnoreCase(NATURE_PRET.getIntauto()))
        {
            if("F".equalsIgnoreCase(NATURE_PRET.getIntauto()))
                TauxInteret = new BigDecimal(NATURE_PRET.getTauxint());
            else
                TauxInteret = P_OBJECT_PRET.getTint();
        }
        if (TauxInteret == null)
            TauxInteret = new BigDecimal(0);
        return TauxInteret;
    }

    BigDecimal recupererTauxTaxe(PretExterneEnteteDto P_OBJECT_PRET, TypePretDto NATURE_PRET)
    {
        BigDecimal TauxTaxe = new BigDecimal(0);
        if (!"N".equalsIgnoreCase(NATURE_PRET.getIntauto()))
        {
            if (!"N".equalsIgnoreCase(NATURE_PRET.getTaxauto()))
            {
                if("F".equalsIgnoreCase(NATURE_PRET.getTaxauto()))
                    TauxTaxe = new BigDecimal(NATURE_PRET.getTauxtaxe());
                else
                    TauxTaxe = P_OBJECT_PRET.getTtax();
            }
        }
        if (TauxTaxe == null)
            TauxTaxe = new BigDecimal(0);
        return TauxTaxe;
    }

    List<Integer> recupererMoisNonRemboursables(TypePretDto NATURE_PRET)
    {
        return __getMoisNonRembList(NATURE_PRET);
    }

    Date recupererDateProchaineEcheance(Date DatePremiereEcheance, List<Integer> ListeMoisNonRembs)
    {
        return ParameterUtil._getNextDueDate(DatePremiereEcheance, ListeMoisNonRembs);
    }

    Date recupererDatePremiereEcheance(Date DatePremiereEcheance, List<Integer> ListeMoisNonRembs)
    {
        return ParameterUtil._getNextDueFirstDate(DatePremiereEcheance, ListeMoisNonRembs);
    }

    public String formatMoney(BigDecimal value)
    {
        return ParameterUtil.__formatMoney(value, OUT_PRET.entetepret.strSeparateurDeMillier, OUT_PRET.entetepret.strSeparateurDeDecimal,
                OUT_PRET.entetepret.strDevise, OUT_PRET.entetepret.intNombreDeDecimales, OUT_PRET.entetepret.boolPartieDecimalAdmise,
                OUT_PRET.entetepret.boolDeviseAGaucheOuADroite);
    }

    /**
     * Obtenir la liste des mois non remboursables.
     *
     * @param oNatPret
     * @return
     */
    public static List<Integer> __getMoisNonRembList(TypePretDto oNatPret)
    {
        List<Integer> oMoisNonRembList = new ArrayList<>();

        try
        {
            if (oNatPret.getMoisnremb1() != null && oNatPret.getMoisnremb1().trim().length() != 0)
                oMoisNonRembList.add(Integer.valueOf(oNatPret.getMoisnremb1()));

            if (oNatPret.getMoisnremb2() != null && oNatPret.getMoisnremb2().trim().length() != 0)
                oMoisNonRembList.add(Integer.valueOf(oNatPret.getMoisnremb2()));

            if (oNatPret.getMoisnremb3() != null && oNatPret.getMoisnremb3().trim().length() != 0)
                oMoisNonRembList.add(Integer.valueOf(oNatPret.getMoisnremb3()));

            if (oNatPret.getMoisnremb4() != null && oNatPret.getMoisnremb4().trim().length() != 0)
                oMoisNonRembList.add(Integer.valueOf(oNatPret.getMoisnremb4()));

            if (oNatPret.getMoisnremb5() != null && oNatPret.getMoisnremb5().trim().length() != 0)
                oMoisNonRembList.add(Integer.valueOf(oNatPret.getMoisnremb5()));

            if (oNatPret.getMoisnremb6() != null && oNatPret.getMoisnremb6().trim().length() != 0)
                oMoisNonRembList.add(Integer.valueOf(oNatPret.getMoisnremb6()));
        }
        catch (Exception e)
        {
            // TODO: handle exception
            e.printStackTrace();
        }

        return oMoisNonRembList;
    }
}
