package com.kinart;

import com.kinart.api.gestiondestock.dto.LigneCommandeClientDto;
import com.kinart.api.gestiondestock.report.LigneCommandeReport;
import com.kinart.paie.business.services.utils.ClsDate;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.boot.SpringApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TestReport {

    public static void main(String[] args) {
       executeReport2();
    }

    static void methode1(){
        try {
            String filePath = ResourceUtils.getFile("classpath:Student.jrxml")
                    .getAbsolutePath();

            Subject subject1 = new Subject("Java", 80);
            Subject subject2 = new Subject("MySQL", 70);
            Subject subject3 = new Subject("PHP", 50);
            Subject subject4 = new Subject("MongoDB", 40);
            Subject subject5 = new Subject("C++", 60);

            List<Subject> list = new ArrayList<Subject>();
            list.add(subject1);
            list.add(subject2);
            list.add(subject3);
            list.add(subject4);
            list.add(subject5);

            JRBeanCollectionDataSource dataSource =
                    new JRBeanCollectionDataSource(list);

            JRBeanCollectionDataSource chartDataSource =
                    new JRBeanCollectionDataSource(list);

            String fileName = "CommandeCLientCMD0876.pdf";
            String uploadDir = StringUtils.cleanPath("./generated-reports/");
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)){
                Files.createDirectories(uploadPath);
            }

            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("studentName", "John");
            parameters.put("tableData", dataSource);
            parameters.put("subReport", getSubReport());
            parameters.put("subDataSource", getSubDataSource());
            parameters.put("subParameters", getSubParameters());

            JasperReport report = JasperCompileManager.compileReport(filePath);

            JasperPrint print =
                    JasperFillManager.fillReport(report, parameters, chartDataSource);

            byte[] byteArray = JasperExportManager.exportReportToPdf(print);

            System.out.println("Export PDF in file");
            JasperExportManager.exportReportToPdfFile(print, uploadDir+fileName);

            System.out.println("Génération OK");

        } catch(Exception e) {
            System.out.println("Exception while creating report");
            e.printStackTrace();
        }
    }

    public static JasperReport getSubReport () {

        JasperReport report;
        try {
            String filePath = ResourceUtils.getFile("classpath:FirstReport.jrxml").getAbsolutePath();
            report = JasperCompileManager.compileReport(filePath);
            return report;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static JRBeanCollectionDataSource getSubDataSource () {
        Student student1 = new Student(1L, "Raj", "Joshi", "Happy Street",
                "Delhi");

        Student student2 = new Student(2L, "Peter", "Smith", "Any Street",
                "Mumbai");

        List<Student> list = new ArrayList<Student>();
        list.add(student1);
        list.add(student2);

        JRBeanCollectionDataSource dataSource =
                new JRBeanCollectionDataSource(list);

        return dataSource;
    }

    public static Map<String, Object> getSubParameters () {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("studentName", "Raj");

        return parameters;
    }


    static void executeReport2(){
         try {
             String filePath = ResourceUtils.getFile("classpath:RptCommandeClientFournisseur.jrxml")
                     .getAbsolutePath();

             System.out.println("Chemin report="+filePath);
             List<LigneCommandeReport> data = new ArrayList<LigneCommandeReport>();
             LigneCommandeReport lg = new LigneCommandeReport();
             lg.setCodeArticle("ART001");
             lg.setLibelleArticle("HEINEKEN");
             lg.setQuantite(new BigDecimal(2));
             lg.setPrixUnitaire(new BigDecimal(650));
             lg.setPrixTtc(new BigDecimal(1300));

             data.add(lg);

             System.out.println("Ajout datasource");
             JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

             System.out.println("Ajout paramètres");
             Map<String, Object> parameters = new HashMap<String, Object>();
             parameters.put("TITRE_ETAT", "CLIENT N° 1223423");
             parameters.put("USER", "Printer");
             parameters.put("DATE_CMD", "01/02/2022");
             parameters.put("NOM_CLT_FRN", "CYRILLE MBASSI");
             parameters.put("TEL_CLT_FRN", "+2376890987");
             parameters.put("ETAT_CMD", "EN PREPARATION");
             parameters.put("CHEMIN_LOGO", "D:\\Programmation orientee objet\\Technologies\\Angular\\projets\\gestiondestock\\logo\\logo.jpg");
             parameters.put("tableData", dataSource);

             System.out.println("Compilation");
             JasperReport report = JasperCompileManager.compileReport(filePath);

             System.out.println("Association des données");
             JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource(1));

             String fileName = "CommandeCLientCMD0876.pdf";
             System.out.println("Nom fihier: "+fileName);
             String uploadDir = StringUtils.cleanPath("./generated-reports/");
             Path uploadPath = Paths.get(uploadDir);
             if (!Files.exists(uploadPath)){
                 Files.createDirectories(uploadPath);
             }

             System.out.println("Export PDF");
             byte[] byteArray = JasperExportManager.exportReportToPdf(print);

             System.out.println("Export PDF in file");
             JasperExportManager.exportReportToPdfFile(print, uploadDir+fileName);

         } catch(Exception e) {
             System.out.println("Exception while creating report");
             e.printStackTrace();
         }
     }
}
