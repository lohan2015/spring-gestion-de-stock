package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.CalculPaieApi;
import com.kinart.api.gestiondepaie.dto.*;
import com.kinart.api.gestiondepaie.report.LigneMouvementCptble;
import com.kinart.api.gestiondestock.dto.LigneCommandeClientDto;
import com.kinart.api.gestiondestock.report.LigneCommandeReport;
import com.kinart.paie.business.model.ElementVariableDetailMois;
import com.kinart.paie.business.model.HistoCalculPaie;
import com.kinart.paie.business.model.VirementSalarie;
import com.kinart.paie.business.services.CalculPaieService;
import com.kinart.paie.business.services.VirementSalaireService;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.type.StandardBasicTypes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.*;

@RestController
public class CalculPaieController implements CalculPaieApi {

    private CalculPaieService calculPaieService;
    private GeneriqueConnexionService generiqueConnexionService;
    private VirementSalaireService virementSalaireService;
    private List<CumulPaieDto> histoCalcul;

    @Autowired
    public CalculPaieController(CalculPaieService calculPaieService, VirementSalaireService virementSalaireService, GeneriqueConnexionService generiqueConnexionService) {
         this.calculPaieService = calculPaieService;
        this.virementSalaireService = virementSalaireService;
        this.generiqueConnexionService = generiqueConnexionService;
    }

     @Override
    public ResponseEntity<CalculPaieDto> save(CalculPaieDto dto) {
         try {
             calculPaieService.save(dto);
         } catch (InvalidEntityException e){
             return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
         }

         return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public boolean calculPaieSalarie(RechercheDto dto) {
        return calculPaieService.calculPaieSalarie(dto);
    }

    @Override
    public ResponseEntity<List<CalculPaieDto>> findByMatriculeAndPeriod(String matricule, String periode, Integer numeBul) {
        List<CalculPaieDto> calculPaieDto = calculPaieService.findByMatriculeAndPeriod(matricule, periode, numeBul);
        if(calculPaieDto!=null) {
            return ResponseEntity.ok(calculPaieDto);
        } else {
            throw new EntityNotFoundException("Pas de bulletin trouvés");
        }
    }

    @Override
    public ResponseEntity<CalculPaieDto> findById(Integer id) {
        CalculPaieDto calculPaieDto = calculPaieService.findById(id);
        if(calculPaieDto!=null) return ResponseEntity.ok(calculPaieDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<CalculPaieDto>> findAll() {
        List<CalculPaieDto> calculPaieDto = calculPaieService.findAll();
        if(calculPaieDto!=null) {
            return ResponseEntity.ok(calculPaieDto);
        } else {
            throw new EntityNotFoundException("Pas de buleltins trouvés");
        }
    }

    @Override
    public void delete(Integer id) {
        calculPaieService.delete(id);
    }

     @Override
     public ResponseEntity<List<CalculPaieDto>> findResultCalculByFilter(RechercheDto dto) {
         List<CalculPaieDto> calculsPaieDto = calculPaieService.findResultCalculByFilter(dto);
         if(calculsPaieDto!=null) {
             return ResponseEntity.ok(calculsPaieDto);
         } else {
             throw new EntityNotFoundException("Pas de bulletins trouvés");
         }
     }

     @Override
     public ResponseEntity<Object> getReport(RechercheDto dto, HttpServletRequest request, HttpServletResponse response) {

        try {
            List<SalarieDto> salariesDto = calculPaieService.findListeSalarieByFilter(dto);
            if(salariesDto == null || salariesDto.size()==0) return new ResponseEntity("Pas de salarié correspondant aux critères", HttpStatus.BAD_REQUEST);
            SalarieDto salDto = salariesDto.get(0);
            List<CalculPaieDto> calculsPaieDto = calculPaieService.findResultCalculByFilter(dto);
            if(calculsPaieDto == null || calculsPaieDto.size()==0) return new ResponseEntity("Pas de bulletin à éditer", HttpStatus.BAD_REQUEST);

            String filePath = ResourceUtils.getFile("classpath:RptBulletinPaie.jrxml")
                     .getAbsolutePath();

             //System.out.println("Chemin report="+filePath);

             //System.out.println("Ajout datasource");
            CalculPaieDto calDto = calculsPaieDto.get(calculsPaieDto.size()-1);
            calculsPaieDto.remove(calDto);
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(calculsPaieDto);

             // System.out.println("Ajout paramètres");
             Map<String, Object> parameters = getReportParameters(salDto, dto.periodeDePaie, dto.numeroBulletin);
             parameters.put("DATE_DEBUT", new ClsDate(new ClsDate(dto.periodeDePaie, "yyyyMM").getFirstDayOfMonth()).getDateS("dd/MM/yyyy"));
             parameters.put("DATE_FIN", new ClsDate(new ClsDate(dto.periodeDePaie, "yyyyMM").getFirstDayOfMonth()).getDateS("dd/MM/yyyy"));
             parameters.put("tableData", dataSource);
             parameters.put("INFO_NAP", calDto.getMont());

             //System.out.println("Compilation");
             JasperReport report = JasperCompileManager.compileReport(filePath);

             //System.out.println("Association des données");
             JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource(1));

             String fileName = salDto.getNmat()+".pdf";
             String uploadDir = StringUtils.cleanPath("./generated-reports/");
             Path uploadPath = Paths.get(uploadDir);
             if (!Files.exists(uploadPath)){
                 Files.createDirectories(uploadPath);
             }

             //System.out.println("Export PDF");
             //byte[] byteArray = JasperExportManager.exportReportToPdf(print);

             //System.out.println("Export PDF in file");
             JasperExportManager.exportReportToPdfFile(print, uploadDir+fileName);

             Path fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
             Path filePat = fileStorageLocation.resolve(fileName).normalize();
             Resource resource = new UrlResource(filePat.toUri());

             System.out.println("Fichier à télécharger: "+Paths.get(uploadDir).toString()+"/"+fileName);
             //System.out.println("URI: "+filePat.toUri());

             File file = new File(filePat.toUri());
             FileInputStream is = new FileInputStream(file);
             response.setContentType("application/blob");

             // Response header
             response.setHeader("Pragma", "public");
             response.setHeader("responseType", "blob");
             response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

             // Read from the file and write into the response
             OutputStream os = response.getOutputStream();
             //System.out.println(os);
             byte[] buffer = new byte[(int) file.length()];

             int len;
             while ((len = is.read(buffer)) != -1) {
                 os.write(buffer, 0, len);
             }

             os.flush();
             os.close();
             is.close();

            // Mise à jour du bulletin
            generiqueConnexionService.updateFromTable("UPDATE CalculPaie SET trtb='2' WHERE idEntreprise="+salDto.getIdentreprise()+" AND nmat='"+salDto.getNmat()+"' AND aamm='"+dto.periodeDePaie+"'");

         } catch(Exception e) {
             System.out.println("Exception while creating report");
             e.printStackTrace();
         }

        return null;
     }

     private Map<String, Object> getReportParameters(SalarieDto salarieDto, String periode, String numbul){
         VirementSalarieDto virDto = virementSalaireService.findByMatricule(salarieDto.getNmat());
        Map<String, Object> parameters = new HashMap<String, Object>();
         parameters.put("NOM_SOCIETE", "MEGATIM");
         parameters.put("ADRESSE_SOCIETE", "B.P.: 56789 YAOUNDE");
         parameters.put("CONTACT_SOCIETE", "Tél.: +237 694 45 67 23");
         parameters.put("INFO_MATRICULE", salarieDto.getNmat());
         parameters.put("INFO_NOM", salarieDto.getNom()+" "+salarieDto.getPren());
         parameters.put("INFO_NIU", salarieDto.getNoss());
         parameters.put("INFO_CATEGORIE", salarieDto.getCat());
         parameters.put("INFO_ECHELON", salarieDto.getLibechelon());
         parameters.put("INFO_CHARGE", salarieDto.getNbpt().intValue()+"");
         parameters.put("INFO_ADRESSE", salarieDto.getAdr1());
         parameters.put("INFO_FONCTION", org.apache.commons.lang3.StringUtils.isNotEmpty(salarieDto.getLibfonction())?salarieDto.getLibfonction():"" );
         parameters.put("INFO_DATE_ENTREE", new ClsDate(salarieDto.getDtes()).getDateS("dd/MM/yyyy"));
         parameters.put("INFO_NUMERO_CNSS", org.apache.commons.lang3.StringUtils.isNotEmpty(salarieDto.getCont())?salarieDto.getCont():"");
         if(virDto != null) parameters.put("INFO_CPTE_VIREMENT", virDto.getGuic()+"-"+virDto.getComp()+"-"+virDto.getCle());
         else parameters.put("INFO_CPTE_VIREMENT", salarieDto.getGuic()+"-"+salarieDto.getComp()+"-"+salarieDto.getCle());
         parameters.put("INFO_SEXE", salarieDto.getLibsexe());
         parameters.put("INFO_DIRECTION", salarieDto.getLibniv1());
         parameters.put("INFO_DEPARTEMENT", salarieDto.getLibniv2());
         parameters.put("INFO_SERVICE", salarieDto.getLibniv3());
         parameters.put("INFO_GRADE", salarieDto.getLibgrade());
         parameters.put("INFO_SITFAM", org.apache.commons.lang3.StringUtils.isNotEmpty(salarieDto.getLibsitfam())?salarieDto.getLibsitfam():"");
         parameters.put("INFO_DEVISE", "F CFA");
//         parameters.put("MOIS_COL1", new BigDecimal(165000));
//         parameters.put("MOIS_COL2", new BigDecimal(125000));
//         parameters.put("MOIS_COL3", new BigDecimal(135000));
//         parameters.put("MOIS_COL4", new BigDecimal(105000));
//         parameters.put("MOIS_COL5", new BigDecimal(115000));
//         parameters.put("CUM_COL1", new BigDecimal(0));
//         parameters.put("CUM_COL2", new BigDecimal(0));
//         parameters.put("CUM_COL3", new BigDecimal(0));
//         parameters.put("CUM_COL4", new BigDecimal(0));
//         parameters.put("CUM_COL5", new BigDecimal(0));
         parameters.put("CHEMIN_LOGO", "D:\\Programmation orientee objet\\Technologies\\Angular\\projets\\gestiondestock\\logo\\logo.jpg");

         //TODO Entêtes colonnes cumuls
         Session session = generiqueConnexionService.getSession();
         String query = "SELECT DISTINCT " +
                 "case when NUME = 1 then VALL end PIED1, " +
                 "case when NUME = 2 then VALL end PIED2, " +
                 "case when NUME = 3 then VALL end PIED3, " +
                 "case when NUME = 4 then VALL end PIED4, " +
                 "case when NUME = 5 then VALL end PIED5, " +
                 "case when NUME = 6 then VALL end PIED6, nume " +
                 "FROM paramdata " +
                 "WHERE (CTAB = 99) AND (CACC = 'PIEDBUL') AND (NUME IN (1,2,3,4,5,6)) AND identreprise =:identreprise  order by nume  asc";
         Query q = session.createSQLQuery(query);
         q.setParameter("identreprise", salarieDto.getIdentreprise());

         List<Object[]> lst = q.getResultList();
         generiqueConnexionService.closeSession(session);

         int i = 1;
         for (Object[] o : lst)
         {
             parameters.put("NOM_COL"+i, o[i-1].toString());
             i = i + 1;
         }

         //TODO Données des cumuls
         session = generiqueConnexionService.getSession();
         query = "SELECT DISTINCT RUB.EPBUL,AGENT.NMAT , CALCUL.RUBQ, RUB.CRUB,CALCUL.AAMM, CALCUL.NBUL , RUB.PRBUL, "+
                 "CASE WHEN RUB.EPBUL = 1 THEN  NVL(CALCUL.MONT,0) END CUMUL11, "+
                 "CASE WHEN RUB.EPBUL = 1 THEN  NVL(CALCUL.MONT,0) + NVL(TBCUMUL.MONT,0)  END CUMUL21,  "+

                 "CASE WHEN RUB.EPBUL = 2 THEN  NVL(CALCUL.MONT,0)  END CUMUL12, "+
                 "CASE WHEN RUB.EPBUL = 2 THEN  NVL(CALCUL.MONT,0)  + NVL(TBCUMUL.MONT,0)  END CUMUL22,  "+

                 "CASE WHEN RUB.EPBUL = 3 THEN  NVL(CALCUL.MONT,0)   END CUMUL13,  "+
                 "CASE WHEN RUB.EPBUL = 3 THEN  NVL(CALCUL.MONT,0)   + NVL(TBCUMUL.MONT,0) END CUMUL23,  "+

                 "CASE WHEN RUB.EPBUL = 4 THEN  NVL(CALCUL.MONT,0)   END CUMUL14,  "+
                 "CASE WHEN RUB.EPBUL = 4 THEN  NVL(CALCUL.MONT,0)  + NVL(TBCUMUL.MONT,0)  END CUMUL24,  "+

                 "CASE WHEN RUB.EPBUL = 5 THEN  NVL(CALCUL.MONT,0)  END CUMUL15 , "+
                 "CASE WHEN RUB.EPBUL = 5 THEN  NVL(CALCUL.MONT,0)  + NVL(TBCUMUL.MONT,0) END CUMUL25 , "+

                 "CASE WHEN RUB.EPBUL = 6 THEN  NVL(CALCUL.MONT,0)  END CUMUL16 , "+
                 "CASE WHEN RUB.EPBUL = 6 THEN  NVL(AGENT.JAPEC,0) + NVL(AGENT.JAPA,0)   END CUMUL26  "+

                 "FROM SALARIE AGENT "+
                 "JOIN ELEMENTSALAIRE RUB ON (RUB.IDENTREPRISE = AGENT.IDENTREPRISE) AND RUB.EPBUL in (1,2,3,4,5,6)  and (RUB.CALC='O')  "+
                 "LEFT JOIN CALCULPAIE CALCUL  ON ( AGENT.IDENTREPRISE = CALCUL.IDENTREPRISE) AND (AGENT.NMAT=CALCUL.NMAT) AND (CALCUL.RUBQ=RUB.CRUB) AND  (CALCUL.IDENTREPRISE=AGENT.IDENTREPRISE) AND (CALCUL.NBUL = :nbul) AND (CALCUL.AAMM = :periode) "+
                 "LEFT JOIN CUMULPAIE TBCUMUL ON (RUB.CRUB=TBCUMUL.RUBQ AND AGENT.IDENTREPRISE=TBCUMUL.IDENTREPRISE AND AGENT.NMAT=TBCUMUL.NMAT  AND TBCUMUL.NBUL=:nbul AND TBCUMUL.AAMM= :percumul)  "+
                 "WHERE  (AGENT.IDENTREPRISE=:dossier)  AND (AGENT.NMAT = :matricule)  ORDER BY EPBUL";
         q = session.createSQLQuery(query);
         q.setParameter("dossier", salarieDto.getIdentreprise());
         q.setParameter("matricule", salarieDto.getNmat());
         q.setParameter("periode", periode);
         q.setParameter("percumul", periode.substring(0, 4)+"99");
         q.setParameter("nbul", numbul);

         lst = q.getResultList();
         generiqueConnexionService.closeSession(session);
         i = 1;
         int j = 6;
         if(lst!=null && lst.size()>0){
         for (Object[] o : lst)
         {
             parameters.put("MOIS_COL"+i, (o[i+j]!=null)?new BigDecimal(o[i+j].toString()):BigDecimal.ZERO);
             parameters.put("CUM_COL"+i, (o[i+j+1]!=null)?new BigDecimal(o[i+j+1].toString()):BigDecimal.ZERO);

             i = i + 1;
             j = j + 1;
         }
         } else{
             parameters.put("MOIS_COL1", new BigDecimal(0));
         parameters.put("MOIS_COL2", new BigDecimal(0));
         parameters.put("MOIS_COL3", new BigDecimal(0));
         parameters.put("MOIS_COL4", new BigDecimal(0));
         parameters.put("MOIS_COL5", new BigDecimal(0));
         parameters.put("CUM_COL1", new BigDecimal(0));
         parameters.put("CUM_COL2", new BigDecimal(0));
         parameters.put("CUM_COL3", new BigDecimal(0));
         parameters.put("CUM_COL4", new BigDecimal(0));
         parameters.put("CUM_COL5", new BigDecimal(0));
         }

         return parameters;
     }

    @Override
    public ResponseEntity<List<SalarieDto>> findListeSalarieByFilter(RechercheDto dto) {
        List<SalarieDto> salariesDto = calculPaieService.findListeSalarieByFilter(dto);
        if(salariesDto!=null) {
            return ResponseEntity.ok(salariesDto);
        } else {
            throw new EntityNotFoundException("Pas de salarié à calculer");
        }
    }

    @Override
    public ResponseEntity<Object> cloturerPaie(RechercheDto dto, HttpServletRequest request, HttpServletResponse response) {
        calculPaieService.cloturePaie(dto, request);
        return null;
    }

    @Override
    public ResponseEntity<List<CumulPaieDto>> findCumulByFilter(RechercheDto dto) {
        List<CumulPaieDto> histoCalcul = calculPaieService.findCumulByFilter(dto);
        if(histoCalcul !=null) {
            return ResponseEntity.ok(histoCalcul);
        } else {
            throw new EntityNotFoundException("Pas de bulletins trouvés");
        }
    }

    @Override
    public ResponseEntity<Object> getReportMvtCpte(RechercheDto dto, HttpServletRequest request, HttpServletResponse response) {

        try {
            // Get Data
            String annee = dto.periodeDePaie.substring(0, 4);
            String mois = dto.periodeDePaie.substring(4, 6);
            String numpce = mois+annee+dto.numeroBulletin;
            String queryString = "select NUMCPT, SENS, LIBECR, SUM(PCE_MT) PCE_MT from interfcomptable " +
                    "where (NUMPCE = '"+numpce+"') and (identreprise = "+dto.identreprise+") " +
                    "group by SENS, NUMCPT, LIBECR";
            Session session = generiqueConnexionService.getSession();
            javax.persistence.Query query  = session.createSQLQuery(queryString);
            List<Object[]> lst = query.getResultList();
            generiqueConnexionService.closeSession(session);

            BigDecimal totalDebit = BigDecimal.ZERO;
            BigDecimal totalCredit = BigDecimal.ZERO;
            List<LigneMouvementCptble> liste = new ArrayList<LigneMouvementCptble>();
            for (Object[] o : lst)
            {
                LigneMouvementCptble cptble = new LigneMouvementCptble();
                if(o[0]!=null) cptble.setNumcpte(o[0].toString());
                if(o[1]!=null) cptble.setSens(o[1].toString());
                if(o[2]!=null) cptble.setLibecriture(" "+o[2].toString());
                if(o[3]!=null) cptble.setMntpce(NumberFormat.getInstance(Locale.FRENCH).format(new BigDecimal(o[3].toString()).doubleValue())+" ");

                liste.add(cptble);
            }

            if(liste == null || liste.size()==0) return new ResponseEntity("Pas de données correspondant aux critères", HttpStatus.BAD_REQUEST);

            String filePath = ResourceUtils.getFile("classpath:RptMouvementComptable.jrxml")
                    .getAbsolutePath();

            //System.out.println("Chemin report="+filePath);

            //System.out.println("Ajout datasource");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(liste);

            // System.out.println("Ajout paramètres");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("NOM_SOCIETE", "MEGATIM");
            parameters.put("ADRESSE_SOCIETE", "B.P.: 56789 YAOUNDE");
            parameters.put("CONTACT_SOCIETE", "Tél.: +237 694 45 67 23");
            parameters.put("CHEMIN_LOGO", "D:\\Programmation orientee objet\\Technologies\\Angular\\projets\\gestiondestock\\logo\\logo.jpg");
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
            parameters.put("TITRE_ETAT", "MOUVEMENT COMPTABLE "+titreEtat+" "+annee);
            parameters.put("TOTAL_DEBIT", NumberFormat.getInstance(Locale.FRENCH).format(totalDebit.doubleValue()));
            parameters.put("TOTAL_CREDIT", NumberFormat.getInstance(Locale.FRENCH).format(totalCredit.doubleValue()));
            parameters.put("tableData", dataSource);

            //System.out.println("Compilation");
            JasperReport report = JasperCompileManager.compileReport(filePath);

            //System.out.println("Association des données");
            JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource(1));

            String fileName = "MvtCptable_"+numpce+".pdf";
            String uploadDir = StringUtils.cleanPath("./generated-reports/");
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            //System.out.println("Export PDF");
            //byte[] byteArray = JasperExportManager.exportReportToPdf(print);

            //System.out.println("Export PDF in file");
            JasperExportManager.exportReportToPdfFile(print, uploadDir+fileName);

            Path fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
            Path filePat = fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePat.toUri());

            System.out.println("Fichier à télécharger: "+Paths.get(uploadDir).toString()+"/"+fileName);
            //System.out.println("URI: "+filePat.toUri());

            File file = new File(filePat.toUri());
            FileInputStream is = new FileInputStream(file);
            response.setContentType("application/blob");

            // Response header
            response.setHeader("Pragma", "public");
            response.setHeader("responseType", "blob");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");

            // Read from the file and write into the response
            OutputStream os = response.getOutputStream();
            //System.out.println(os);
            byte[] buffer = new byte[(int) file.length()];

            int len;
            while ((len = is.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }

            os.flush();
            os.close();
            is.close();

        } catch(Exception e) {
            System.out.println("Exception while creating report");
            e.printStackTrace();
        }

        return null;
    }

}
