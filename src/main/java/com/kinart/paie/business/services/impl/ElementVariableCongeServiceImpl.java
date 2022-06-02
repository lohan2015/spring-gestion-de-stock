package com.kinart.paie.business.services.impl;

import com.kinart.api.gestiondepaie.dto.ElementVariableCongeDto;
import com.kinart.api.gestiondepaie.dto.ParamDataDto;
import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.repository.*;
import com.kinart.paie.business.services.ElementVariableCongeService;
import com.kinart.paie.business.services.utils.*;
import com.kinart.paie.business.validator.ElementVariableCongeValidator;
import com.kinart.paie.business.model.ElementVariableConge;
import com.kinart.paie.business.model.ElementVariableEnteteMois;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import com.kinart.stock.business.exception.InvalidEntityException;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.Query;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ElementVariableCongeServiceImpl implements ElementVariableCongeService {

    private ElementVariableEnteteMoisRepository elementVariableEnteteMoisRepository;
    private SalarieRepository salarieRepository;
    private CalendrierPaieRepository calendrierPaieRepository;
    private ElementVariableCongeRepository elementVariableCongeRepository;
    private ParamDataRepository paramDataRepository;
    private GeneriqueConnexionService service;

    @Autowired
    public ElementVariableCongeServiceImpl(ElementVariableEnteteMoisRepository elementVariableEnteteMoisRepository, SalarieRepository salarieRepository, CalendrierPaieRepository calendrierPaieRepository, ElementVariableCongeRepository elementVariableCongeRepository, ParamDataRepository paramDataRepository, GeneriqueConnexionService service) {
        this.elementVariableEnteteMoisRepository = elementVariableEnteteMoisRepository;
        this.salarieRepository = salarieRepository;
        this.calendrierPaieRepository = calendrierPaieRepository;
        this.elementVariableCongeRepository = elementVariableCongeRepository;
        this.paramDataRepository = paramDataRepository;
        this.service = service;
    }

    @Override
    public ElementVariableCongeDto save(ElementVariableCongeDto dto, String dateFormat, String typeBD) {
        List<String> errors = ElementVariableCongeValidator.validate(dto);
        if (!errors.isEmpty()) {
            log.error("EV congé non valide {}", dto);
            throw new InvalidEntityException("L'EV congé n'est pas valide", ErrorCodes.ARTICLE_NOT_VALID, errors);
        }

        if(dto.getId()!=null && dto.getId().intValue()!=0){
            String strDate = dto.getStartdate();
            if(typeBD.equalsIgnoreCase(TypeBDUtil.MY))
                strDate = new ClsDate(strDate,dateFormat).getDateS("yyyy/MM/dd");

            String strEndDate = dto.getEnddate();
            if(typeBD.equalsIgnoreCase(TypeBDUtil.MY))
                strEndDate = new ClsDate(strEndDate,dateFormat).getDateS("yyyy/MM/dd");

            String query = "UPDATE Rhteltvarconge SET nbja = " + dto.getNbja() + " ,nbjc = " + dto.getNbjc() + " ,motf = " + "'" + dto.getMotf() + "'" + " ,mont = " + dto.getMont() + " ,dfin = "
                    + "'" + strEndDate + "'" + " WHERE cdos = " + "'" + dto.getIdEntreprise() + "'" + " AND nmat = " + "'" + dto.getNmat() + "'" + " AND ddeb = " + "'"
                    + strDate + "'";
            try {
                service.updateFromTable(query);
            } catch (Exception e){
                e.printStackTrace();
            }

            return dto;
        }

        // TODO
        Date dated;
        Date datef;

        boolean fini;

        long nbja = 0;
        long nbjc = 0;
        BigDecimal bdNbja;
        BigDecimal bdNbjc;

        BigDecimal mtcg = new BigDecimal(0);
        boolean maj_Ok;
        Integer cdos = dto.getIdEntreprise();
        String cuti = dto.getCuti();
        String format = dateFormat;

        Date dfcg = new ClsDate(dto.getEnddate(), format).getDate();
        Date ddcg = new ClsDate(dto.getStartdate(), format).getDate();
        maj_Ok = false;

        //On va stocker le nombre de jours de congés et absences saisies par le user
        //de manière à s'assurer que le nombre total soit toujours égal à ces nombres la
        BigDecimal saveNbja = dto.getNbja();
        BigDecimal saveNbjc = dto.getNbjc();

        boolean abs2mois = false;
        ParamDataDto nome = paramDataRepository.findByNumeroLigne(Integer.valueOf(266), "ABSEN2MOIS", Integer.valueOf(2))
                                    .map(ParamDataDto::fromEntity)
                                    .orElseThrow(() ->
                                            new EntityNotFoundException(
                                                    "Aucune donnée avec l'ID = ABSEN2MOIS n' ete trouve dans la BDD",
                                                    ErrorCodes.ARTICLE_NOT_FOUND)
                                    );

        if(nome!=null && !StringUtils.hasLength(nome.getVall()))
            abs2mois = "O".equalsIgnoreCase(nome.getVall().trim());

        maj_Ok = true;
        ElementVariableEnteteMois entEV = elementVariableEnteteMoisRepository.findEntEVByMatricule(dto.getAamm(), dto.getNmat(), dto.getNbul());
        if(entEV==null){
            entEV = new ElementVariableEnteteMois();
            entEV.setAamm(dto.getAamm());
            entEV.setNmat(dto.getNmat());
            entEV.setNbul(dto.getNbul());
            entEV.setIdEntreprise(dto.getIdEntreprise());
            entEV.setAamm(dto.getAamm());
            entEV.setBcmo("N");
            entEV.setDdpa(new ClsDate(dto.getAamm(), "yyyyMM").getFirstDayOfMonth());
            entEV.setDfpa(new ClsDate(dto.getAamm(), "yyyyMM").getLastDayOfMonth());
            elementVariableEnteteMoisRepository.save(entEV);
        }

        ParamDataDto fnom1 = paramDataRepository.findByNumeroLigne(Integer.valueOf(22), dto.getMotf(), Integer.valueOf(1))
                            .map(ParamDataDto::fromEntity)
                            .orElseThrow(() ->
                                    new EntityNotFoundException(
                                            "Aucune donnée avec l'ID = "+dto.getMotf()+" n' ete trouve dans la table 22",
                                            ErrorCodes.ARTICLE_NOT_FOUND)
                            );

        ParamDataDto fnom3 = paramDataRepository.findByNumeroLigne(Integer.valueOf(22), dto.getMotf(), Integer.valueOf(3))
                                .map(ParamDataDto::fromEntity)
                                .orElseThrow(() ->
                                        new EntityNotFoundException(
                                                "Aucune donnée avec l'ID = "+dto.getMotf()+" n' ete trouve dans la table 22",
                                                ErrorCodes.ARTICLE_NOT_FOUND)
                                );

        // Maj Zones calcul fictif dans le fichier salarie (pasa01)
        if (fnom1.getValm() == 1 && fnom3.getValm() == 0)
        {
            Salarie agent = salarieRepository.findByMatriculeExactly(dto.getNmat());
            if (agent != null){
                agent.setNbjcf(dto.getNbjc());
                agent.setNbjaf(dto.getNbja());
                agent.setDdcf(ddcg);
                agent.setDfcf(dfcg);
                agent.setMtcf(new BigDecimal(0));
                agent.setPmcf(dto.getAamm());
                salarieRepository.save(agent);
            }

        }

        dated = ddcg;
        fini = false;

        ElementVariableConge det = null;
        long cptNbja=0;
        long cptNbjc=0;
        BigDecimal ecarta=BigDecimal.ZERO;
        BigDecimal ecartc = BigDecimal.ZERO;
        while (dated.compareTo(dfcg) <= 0)
        {
            if (fnom1.getValm() == 1 || abs2mois)
            {
                datef = new ClsDate(dated).getLastDayOfMonth();
                if (datef.compareTo(dfcg) > 0)
                {
                    datef = dfcg;
                    fini = true;
                }
            }
            else
            {
                datef = dfcg;
                fini = true;
            }
            if (fnom1.getValm() == 0)
            {
                nbja = AbsenceCongeUtil.pr_compte_jours(service, paramDataRepository, String.valueOf(cdos), "A", dated, datef, typeBD);
                nbjc = 0;
                mtcg = new BigDecimal(0);
            }
            else
            {
                nbjc = AbsenceCongeUtil.pr_compte_jours(service, paramDataRepository, String.valueOf(cdos), "C", dated, datef, typeBD);
                if (fnom3.getValm() != 2)
                {
                    nbja = AbsenceCongeUtil.pr_compte_jours(service, paramDataRepository, String.valueOf(cdos), "A", dated, datef, typeBD);
                    mtcg = dto.getMont();
                }
                else
                    nbja = 0;
            }

            bdNbja = new BigDecimal(nbja);
            bdNbjc = new BigDecimal(nbjc);
            cptNbja +=nbja;
            cptNbjc +=nbjc;
            if(fini)
            {
                ecarta = saveNbja.subtract(new BigDecimal(cptNbja));
                ecartc = saveNbjc.subtract(new BigDecimal(cptNbjc));
                nbja=nbja+ecarta.longValue();
                nbjc=nbjc+ecartc.longValue();

                bdNbja = bdNbja.add(ecarta);
                bdNbjc = bdNbjc.add(ecartc);

            }
            det = new ElementVariableConge();
            det.setDfin(datef);
            det.setNbja(bdNbja);
            det.setNbjc(bdNbjc);
            det.setCuti(cuti);
            det.setMotf(dto.getMotf());
            det.setMont(mtcg);

            elementVariableCongeRepository.save(det);

            dated = new ClsDate(datef).addDay(1);
        }

        return ElementVariableCongeDto.fromEntity(
                elementVariableCongeRepository.save(
                        ElementVariableCongeDto.toEntity(dto)
                )
        );
    }

    @Override
    public ElementVariableCongeDto findById(Integer id) {
        if (id == null) {
            log.error("EV congé ID is null");
            return null;
        }

        return elementVariableCongeRepository.findById(id).map(ElementVariableCongeDto::fromEntity).orElseThrow(() ->
                new EntityNotFoundException(
                        "Aucun EV congé avec l'ID = " + id + " n'a été trouvé dans la BDD",
                        ErrorCodes.ARTICLE_NOT_FOUND)
        );
    }

    @Override
    public List<ElementVariableCongeDto> findAll() {
        return elementVariableCongeRepository.findAll().stream()
                .map(ElementVariableCongeDto::fromEntity)
                .collect(Collectors.toList());
    }

    @Override
    public void delete(Integer id) {
        if (id == null) {
            log.error("EV congé ID is null");
            return;
        }
        elementVariableCongeRepository.deleteById(id);
    }

    @Override
    public List<ElementVariableCongeDto> findEVCongeByFilter(String matricule, String codemotif) {
        List<ElementVariableCongeDto> liste = new ArrayList<ElementVariableCongeDto>();
        String sqlQuery = "SELECT e.*, s.nom as nomsal, s.pren as prensal, t.vall as librub " +
                "FROM ElementVariableConge e " +
                "LEFT JOIN Salarie s ON (e.identreprise=s.identreprise AND e.nmat=s.nmat) "+
                "LEFT JOIN ParamData t ON (t.ctab=22 AND t.nume=1 AND t.identreprise=s.identreprise AND t.cacc=e.motf) "+
                "WHERE 1=1";

        if(org.apache.commons.lang3.StringUtils.isNotEmpty(matricule)) sqlQuery += " AND upper(e.nmat) LIKE :matricule";
        if(org.apache.commons.lang3.StringUtils.isNotEmpty(codemotif)) sqlQuery += " AND upper(e.motf) = :codemotif";

        try {
            Session session = service.getSession();
            Query query  = session.createSQLQuery(sqlQuery)
                    .addEntity("e", ElementVariableConge.class)
                    .addScalar("nomsal", StandardBasicTypes.STRING)
                    .addScalar("prensal", StandardBasicTypes.STRING)
                    .addScalar("librub", StandardBasicTypes.STRING);

            //query.setParameter("identreprise", identreprise);
            if(org.apache.commons.lang3.StringUtils.isNotEmpty(matricule)) query.setParameter("matricule", "%"+matricule+"%");
            if(org.apache.commons.lang3.StringUtils.isNotEmpty(codemotif)) query.setParameter("codemotif", codemotif);
            List<Object[]> lst = query.getResultList();
            service.closeSession(session);

            for (Object[] o : lst)
            {
                ElementVariableConge evDB = (ElementVariableConge)o[0];
                ElementVariableCongeDto evDto = ElementVariableCongeDto.fromEntity(evDB);
                if(o[1]!=null) evDto.setNomsalarie(o[1].toString());
                if(o[2]!=null) evDto.setNomsalarie(evDto.getNomsalarie()+" "+o[2].toString());
                if(o[3]!=null) evDto.setLibmotif(o[3].toString());

                liste.add(evDto);
            }

        } catch (Exception e){
            throw e;
        }

        return liste;
    }
}
