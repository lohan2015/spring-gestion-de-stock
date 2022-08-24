package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.EfCmrApi;
import com.kinart.api.gestiondepaie.dto.ParamEFCMRDto;
import com.kinart.api.gestiondepaie.dto.ParameterOfDipeDto;
import com.kinart.api.gestiondepaie.report.LigneDeclarationVersement;
import com.kinart.api.gestiondepaie.report.service.EFCMRService;
import com.kinart.api.gestiondepaie.report.parameter.EFCMRParameter;
import com.kinart.paie.business.model.DossierPaie;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.StringUtil;
import com.kinart.paie.business.utils.ClsLanDipesMagnetiques;
import com.kinart.paie.business.utils.ClsParameterOfDipe;
import com.kinart.paie.business.utils.ClsTypeDipesMagnetiques;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

@RestController
public class EfCmrController implements EfCmrApi {

    private GeneriqueConnexionService generiqueConnexionService;
    private EFCMRService efcmrService;

    @Autowired
    public EfCmrController(GeneriqueConnexionService generiqueConnexionService, EFCMRService efcmrService) {
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
        String filePath = ResourceUtils.getFile("classpath:RptDeclarationDeVersement.jrxml")
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

    @Override
    public ResponseEntity<Object> generateDipeMagnetique(ParameterOfDipeDto dto, HttpServletRequest request, HttpServletResponse response) throws IOException {
        dto.annee = dto.aamm.substring(0, 4);
        String dossierDipe = null;
        String uploadDir = StringUtils.cleanPath("./generated-dipes/");
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        //Path fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        dossierDipe = Paths.get(uploadDir).toAbsolutePath().toString();
        ParamData dipePath = generiqueConnexionService.findAnyColumnFromNomenclature(dto.cdos, dto.clang, "99", "CHEMINDIPE", "1");
        if(dipePath != null && StringUtil.isNotBlank(dipePath.getVall()))
            dossierDipe = dipePath.getVall();
        if(!dossierDipe.endsWith(File.separator)) dossierDipe = dossierDipe+File.separator;

        ClsParameterOfDipe param = new ClsParameterOfDipe();
        BeanUtils.copyProperties(dto, param);

        List<DossierPaie> dossiers = generiqueConnexionService.find("FROM DossierPaie WHERE idEntreprise="+param.cdos);
        if(!dossiers.isEmpty()){
            DossierPaie dossierPaie = dossiers.get(0);
            param.ddex = dossierPaie.getDdex();
            param.dfex = dossierPaie.getDfex();
        }

//        System.out.println("CHEMIN="+dossierDipe);
//        System.out.println("CHEMIN COMPLET="+dossierDipe+param.cdos+"-"+param.aamm+"-C04.txt");
        param.cheminDipeMensuel = dossierDipe+param.cdos+"-"+param.aamm+"-C04.txt";

        param.cheminDipeDebutExercice = dossierDipe+param.cdos+"-"+param.aamm+"-C05.txt";

        param.cheminDipeFinExercice = dossierDipe+param.cdos+"-"+param.aamm+"-C03.txt";

        param.cheminDipeTemporaire = dossierDipe+param.cdos+"-"+param.aamm+"-C07.txt";

        param.cheminDipeEmbauche = dossierDipe+param.cdos+"-"+param.aamm+"-C15.txt";

        param.cheminDipeMensuel2 = dossierDipe+param.cdos+"-"+param.aamm+"-C04-2.txt";

        param.cheminDipeDebutExercice2 = dossierDipe+param.cdos+"-"+param.aamm+"-C05-2.txt";

        param.cheminDipeFinExercice2 = dossierDipe+param.cdos+"-"+param.aamm+"-C03-2.txt";

        param.cheminDipeTemporaire2 = dossierDipe+param.cdos+"-"+param.aamm+"-C07-2.txt";

        param.cheminDipeEmbauche2 = dossierDipe+param.cdos+"-"+param.aamm+"-C15-2.txt";


        param.cheminDipeCD10 = dossierDipe+param.cdos+"-"+param.aamm+"-CD10.txt";

        ClsLanDipesMagnetiques lanceur = new ClsLanDipesMagnetiques();
        lanceur.setService(generiqueConnexionService);
        lanceur.setRequest(request);

        lanceur.setParam(param);

        lanceur.init();

        lanceur.lancerGeneration();

        File file = null;
        FileInputStream is = null;
        String fileName = null;
        File file2 = null;
        FileInputStream is2 = null;
        String fileName2 = null;
        if(param.typeDipe == ClsTypeDipesMagnetiques.MENSUEL){
            fileName = param.cdos+"-"+param.aamm+"-C04.txt";
            file = new File(param.cheminDipeMensuel);
            is = new FileInputStream(file);

            fileName2 = param.cdos+"-"+param.aamm+"-C04-2.txt";
            file2 = new File(param.cheminDipeMensuel2);
            is2 = new FileInputStream(file2);
        } else if(param.typeDipe == ClsTypeDipesMagnetiques.CD10){
            fileName = param.cdos+"-"+param.aamm+"-CD10.txt";
            file = new File(param.cheminDipeCD10);
            is = new FileInputStream(file);
        } else if(param.typeDipe == ClsTypeDipesMagnetiques.DEBUT_EXERCICE){
            fileName = dossierDipe+param.cdos+"-"+param.aamm+"-C05.txt";
            file = new File(param.cheminDipeDebutExercice);
            is = new FileInputStream(file);

            fileName2 = param.cdos+"-"+param.aamm+"-C05-2.txt";
            file2 = new File(param.cheminDipeDebutExercice2);
            is2 = new FileInputStream(file2);
        } else if(param.typeDipe == ClsTypeDipesMagnetiques.EMBAUCHE){
            fileName = dossierDipe+param.cdos+"-"+param.aamm+"-C15.txt";
            file = new File(param.cheminDipeEmbauche);
            is = new FileInputStream(file);

            fileName2 = param.cdos+"-"+param.aamm+"-C15-2.txt";
            file2 = new File(param.cheminDipeEmbauche2);
            is2 = new FileInputStream(file2);
        } else if(param.typeDipe == ClsTypeDipesMagnetiques.FIN_EXERCICE){
            fileName = dossierDipe+param.cdos+"-"+param.aamm+"-C03.txt";
            file = new File(param.cheminDipeFinExercice);
            is = new FileInputStream(file);

            fileName2 = param.cdos+"-"+param.aamm+"-C03-2.txt";
            file2 = new File(param.cheminDipeFinExercice2);
            is2 = new FileInputStream(file2);
        } else if(param.typeDipe == ClsTypeDipesMagnetiques.TEMPORAIRE){
            fileName = dossierDipe+param.cdos+"-"+param.aamm+"-C07.txt";
            file = new File(param.cheminDipeTemporaire);
            is = new FileInputStream(file);

            fileName2 = param.cdos+"-"+param.aamm+"-C07-2.txt";
            file2 = new File(param.cheminDipeTemporaire2);
            is2 = new FileInputStream(file2);
        }

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

        if(org.apache.commons.lang3.StringUtils.isNotEmpty(fileName2)){
            response.setContentType("application/blob");

            // Response header
            response.setHeader("Pragma", "public");
            response.setHeader("responseType", "blob");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName2 + "\"");

            // Read from the file and write into the response
            os = response.getOutputStream();
            //System.out.println(os);
            buffer = new byte[(int) file2.length()];

            while ((len = is2.read(buffer)) != -1) {
                os.write(buffer, 0, len);
            }

            os.flush();
            os.close();
            is.close();
        }

        //BeanUtils.copyProperties(param, dto);
        return null;
    }

    @Override
    public ResponseEntity<ParameterOfDipeDto> getParamDipeMagnetique(ParameterOfDipeDto dto) throws IOException {
        String dossierDipe = null;
        String uploadDir = StringUtils.cleanPath("./generated-dipes/");
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)){
            Files.createDirectories(uploadPath);
        }
        //Path fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();
        dossierDipe = Paths.get(uploadDir).toAbsolutePath().toString();
        ParamData dipePath = generiqueConnexionService.findAnyColumnFromNomenclature(dto.cdos, dto.clang, "99", "CHEMINDIPE", "1");
        if(dipePath != null && StringUtil.isNotBlank(dipePath.getVall()))
            dossierDipe = dipePath.getVall();
        if(!dossierDipe.endsWith(File.separator)) dossierDipe = dossierDipe+File.separator;

        ClsParameterOfDipe param = new ClsParameterOfDipe();
        BeanUtils.copyProperties(dto, param);

        param.cheminDipeMensuel = dossierDipe+param.cdos+"-"+param.aamm+"-C04.txt";

        param.cheminDipeDebutExercice = dossierDipe+param.cdos+"-"+param.aamm+"-C05.txt";

        param.cheminDipeFinExercice = dossierDipe+param.cdos+"-"+param.aamm+"-C03.txt";

        param.cheminDipeTemporaire = dossierDipe+param.cdos+"-"+param.aamm+"-C07.txt";

        param.cheminDipeEmbauche = dossierDipe+param.cdos+"-"+param.aamm+"-C15.txt";

        param.cheminDipeMensuel2 = dossierDipe+param.cdos+"-"+param.aamm+"-C04-2.txt";

        param.cheminDipeDebutExercice2 = dossierDipe+param.cdos+"-"+param.aamm+"-C05-2.txt";

        param.cheminDipeFinExercice2 = dossierDipe+param.cdos+"-"+param.aamm+"-C03-2.txt";

        param.cheminDipeTemporaire2 = dossierDipe+param.cdos+"-"+param.aamm+"-C07-2.txt";

        param.cheminDipeEmbauche2 = dossierDipe+param.cdos+"-"+param.aamm+"-C15-2.txt";

        param.cheminDipeCD10 = dossierDipe+param.cdos+"-"+param.aamm+"-CD10.txt";
        BeanUtils.copyProperties(param, dto);
        return ResponseEntity.ok(dto);
    }
}
