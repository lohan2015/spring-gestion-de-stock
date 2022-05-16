package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.CalculPaieApi;
import com.kinart.api.gestiondepaie.dto.CalculPaieDto;
import com.kinart.api.gestiondepaie.dto.RechercheDto;
import com.kinart.api.gestiondepaie.dto.SalarieDto;
import com.kinart.api.gestiondestock.dto.LigneCommandeClientDto;
import com.kinart.api.gestiondestock.report.LigneCommandeReport;
import com.kinart.paie.business.services.CalculPaieService;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class CalculPaieController implements CalculPaieApi {

    private CalculPaieService calculPaieService;

    @Autowired
    public CalculPaieController(CalculPaieService calculPaieService) {
         this.calculPaieService = calculPaieService;
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
            SalarieDto SalarieDto = salariesDto.get(0);
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
             Map<String, Object> parameters = getReportParameters(SalarieDto);
             parameters.put("DATE_DEBUT", new ClsDate(new ClsDate(dto.periodeDePaie, "yyyyMM").getFirstDayOfMonth()).getDateS("dd/MM/yyyy"));
             parameters.put("DATE_FIN", new ClsDate(new ClsDate(dto.periodeDePaie, "yyyyMM").getFirstDayOfMonth()).getDateS("dd/MM/yyyy"));
             parameters.put("tableData", dataSource);
             parameters.put("INFO_NAP", calDto.getMont());

             //System.out.println("Compilation");
             JasperReport report = JasperCompileManager.compileReport(filePath);

             //System.out.println("Association des données");
             JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource(1));

             String fileName = SalarieDto.getNmat()+".pdf";
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

     private Map<String, Object> getReportParameters(SalarieDto SalarieDto){
         Map<String, Object> parameters = new HashMap<String, Object>();
         parameters.put("NOM_SOCIETE", "KIN'ART REMAKE");
         parameters.put("ADRESSE_SOCIETE", "B.P.: 56789 DOUALA");
         parameters.put("CONTACT_SOCIETE", "Tél.: +237 694 45 67 23");
         parameters.put("INFO_MATRICULE", SalarieDto.getNmat());
         parameters.put("INFO_NOM", SalarieDto.getNom()+" "+SalarieDto.getPren());
         parameters.put("INFO_NIU", SalarieDto.getNoss());
         parameters.put("INFO_CATEGORIE", SalarieDto.getLibcategorie());
         parameters.put("INFO_ECHELON", SalarieDto.getLibechelon());
         parameters.put("INFO_CHARGE", SalarieDto.getNbpt().intValue()+"");
         parameters.put("INFO_ADRESSE", SalarieDto.getAdr1());
         parameters.put("INFO_FONCTION", SalarieDto.getLibfonction());
         parameters.put("INFO_DATE_ENTREE", new ClsDate(SalarieDto.getDtes()).getDateS("dd/MM/yyyy"));
         parameters.put("INFO_NUMERO_CNSS", SalarieDto.getCont());
         parameters.put("INFO_CPTE_VIREMENT", SalarieDto.getGuic()+"-"+SalarieDto.getComp()+"-"+SalarieDto.getCle());
         parameters.put("INFO_SEXE", SalarieDto.getLibsexe());
         parameters.put("INFO_DIRECTION", SalarieDto.getLibniv1());
         parameters.put("INFO_DEPARTEMENT", SalarieDto.getLibniv2());
         parameters.put("INFO_SERVICE", SalarieDto.getLibniv3());
         parameters.put("INFO_GRADE", SalarieDto.getLibgrade());
         parameters.put("INFO_SITFAM", SalarieDto.getLibsitfam());
         parameters.put("INFO_DEVISE", "F CFA");
         parameters.put("MOIS_COL1", new BigDecimal(165000));
         parameters.put("MOIS_COL2", new BigDecimal(125000));
         parameters.put("MOIS_COL3", new BigDecimal(135000));
         parameters.put("MOIS_COL4", new BigDecimal(105000));
         parameters.put("MOIS_COL5", new BigDecimal(115000));
         parameters.put("CUM_COL1", new BigDecimal(0));
         parameters.put("CUM_COL2", new BigDecimal(0));
         parameters.put("CUM_COL3", new BigDecimal(0));
         parameters.put("CUM_COL4", new BigDecimal(0));
         parameters.put("CUM_COL5", new BigDecimal(0));
         parameters.put("CHEMIN_LOGO", "D:\\Programmation orientee objet\\Technologies\\Angular\\projets\\gestiondestock\\logo\\logo.jpg");

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
}
