package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.EFCMRApi;
import com.kinart.api.gestiondepaie.dto.CumulPaieDto;
import com.kinart.api.gestiondepaie.dto.ParamEFCMRDto;
import com.kinart.api.gestiondepaie.report.LigneDeclarationVersement;
import com.kinart.api.gestiondepaie.report.parameter.EFCMRParameter;
import com.kinart.api.gestiondepaie.report.service.EFCMRService;
import com.kinart.api.mail.service.EmailService;
import com.kinart.paie.business.services.CalculPaieService;
import com.kinart.paie.business.services.VirementSalaireService;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
public class EFCMRController implements EFCMRApi {

    private GeneriqueConnexionService generiqueConnexionService;
    private EFCMRService efcmrService;

    @Autowired
    public EFCMRController(GeneriqueConnexionService generiqueConnexionService, EFCMRService efcmrService) {
        this.generiqueConnexionService = generiqueConnexionService;
        this.efcmrService = efcmrService;
    }

    @Override
    public ResponseEntity<Object> generateDeclVersement(ParamEFCMRDto dto, HttpServletRequest request, HttpServletResponse response) throws FileNotFoundException {
        try {

        LigneDeclarationVersement declarationVersement = efcmrService.loadDataDeclarationVersement(generiqueConnexionService, dto);
        Map<String, Object> parameters = EFCMRParameter.setParametersDeclVersement(declarationVersement, dto.getPeriodePaie());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(List.of());
        //System.out.println("Compilation");
        String filePath = ResourceUtils.getFile("classpath:RptDeclarationVersement.jrxml")
                .getAbsolutePath();
        JasperReport report = JasperCompileManager.compileReport(filePath);

        //System.out.println("Association des données");
        JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource(1));

        String fileName = "DeclarationVersement"+dto.getPeriodePaie().substring(4,6)+"_"+dto.getPeriodePaie().substring(0,4)+".pdf";
        String uploadDir = StringUtils.cleanPath("./generated-reports/");
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }

        //System.out.println("Export PDF in file");
        JasperExportManager.exportReportToPdfFile(print, uploadDir+fileName);

        Path fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        Path filePat = fileStorageLocation.resolve(fileName).normalize();
        Resource resource = new UrlResource(filePat.toUri());

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

        System.out.println("Fichier à télécharger: "+Paths.get(uploadDir).toString()+"/"+fileName);

        } catch (Exception e){
            System.out.println("Exception while creating report");
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public ResponseEntity<Object> generateRedevAudioVisuelle(ParamEFCMRDto dto, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public ResponseEntity<Object> generateContribCFC(ParamEFCMRDto dto, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }

    @Override
    public ResponseEntity<Object> generateDipesMensuelles(ParamEFCMRDto dto, HttpServletRequest request, HttpServletResponse response) {
        return null;
    }
}
