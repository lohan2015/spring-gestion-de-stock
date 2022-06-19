package com.kinart;

import com.kinart.api.gestiondepaie.dto.CalculPaieDto;
import com.kinart.api.gestiondepaie.report.LigneDeclarationVersement;
import com.kinart.api.gestiondepaie.report.LigneMouvementCptble;
import com.kinart.api.gestiondepaie.report.parameter.EFCMRParameter;
import com.kinart.api.gestiondepaie.report.service.impl.EFCMRServiceImpl;
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

import javax.mail.*;
import javax.mail.internet.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.util.*;

public class TestReport {

    public static void main(String[] args) {
       executeReport5();
        //envoiMail();
    }

    private static void envoiMail() {
        // Get system properties
        Properties properties = new Properties();

        // Setup mail server
        properties.put("mail.smtp.auth", "true");
        //properties.put("mail.transport.protocol", "smtps");
        //properties.put("mail.smtp.socketFactory.port", "465");
        //properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.port", "465");
        String username =  "lohanlaurel@gmail.com";
        String pwd = "qszduiavxyicburp";

        // Get the Session object.// and pass username and password
        Session session = Session.getInstance(properties, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, pwd);
            }
        });

        // Get the default Session object.
        //javax.mail.Session session = javax.mail.Session.getInstance(properties,null);

        // Used to debug SMTP issues
        session.setDebug(true);

        try{
            // Create a default MimeMessage object.
            MimeMessage message = new MimeMessage(session);

            // Set From: header field of the header.
            message.setFrom(new InternetAddress("sirh@megatim.com"));

            // Set To: header field of the header.
            message.addRecipient(Message.RecipientType.TO,
                    new InternetAddress("mbacyrille2002@yahoo.fr"));

            // Set Subject: header field
            message.setSubject("This is the Subject Line!");

            MimeBodyPart messageBodyPart = new MimeBodyPart();


            // Fill the message
            String body = "Test envoi mail";
            messageBodyPart.setText(body);
            messageBodyPart.setContent(body, "text/html");

            // Create a multipar message
            Multipart multipart = new MimeMultipart();

            // Set text message part
            multipart.addBodyPart(messageBodyPart);

//            if(StringUtils.isNotBlank(outPutFile)){
//                // Part two is attachment
//                messageBodyPart = new MimeBodyPart();
//                DataSource source = new FileDataSource(outPutFile);
//                messageBodyPart.setDataHandler(new DataHandler(source));
//                messageBodyPart.setFileName(fileName);
//                multipart.addBodyPart(messageBodyPart);
//            }

            // Send the complete message parts
            message.setContent(multipart);

            // Send message
            Transport.send(message);
    } catch (AddressException e) {
            e.printStackTrace();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
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

    static void executeReport3(){
        try {
            String filePath = ResourceUtils.getFile("classpath:RptBulletinPaie.jrxml")
                    .getAbsolutePath();

            System.out.println("Chemin report="+filePath);
            List<CalculPaieDto> data = new ArrayList<CalculPaieDto>();
            for(int i=1; i<=5;i++){
                CalculPaieDto lg = new CalculPaieDto();
                lg.setRubq("0001");
                lg.setLibRubrique("ELEMENT SALAIRE "+i);
                lg.setTypeRubrique("GA");
                lg.setBasc(new BigDecimal(65000));
                lg.setTaux(new BigDecimal(1));
                lg.setMont(new BigDecimal(130000));

                data.add(lg);
            }
            for(int i=1; i<=5;i++){
                CalculPaieDto lg = new CalculPaieDto();
                lg.setRubq("0001");
                lg.setLibRubrique("RETENUE SALAIRE "+i);
                lg.setTypeRubrique("RE");
                lg.setBasc(new BigDecimal(650000));
                lg.setTaux(new BigDecimal(1));
                lg.setMont(new BigDecimal(1350000));

                data.add(lg);
            }
            for(int i=1; i<=5;i++){
                CalculPaieDto lg = new CalculPaieDto();
                lg.setRubq("0001");
                lg.setLibRubrique("PART PAT "+i);
                lg.setTypeRubrique("PP");
                lg.setBasc(new BigDecimal(65000));
                lg.setTaux(new BigDecimal(1));
                lg.setMont(new BigDecimal(130000));

                data.add(lg);
            }

            CalculPaieDto lg2 = new CalculPaieDto();
            lg2.setRubq("0975");
            lg2.setLibRubrique("SALAIRE DE BASE");
            lg2.setTypeRubrique("RE");
            lg2.setBasc(new BigDecimal(65000));
            lg2.setTaux(new BigDecimal(1));
            lg2.setMont(new BigDecimal(150000));

            data.add(lg2);

            System.out.println("Ajout datasource");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

            System.out.println("Ajout paramètres");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("NOM_SOCIETE", "KIN'ART REMAKE");
            parameters.put("ADRESSE_SOCIETE", "B.P.: 56789 DOUALA");
            parameters.put("CONTACT_SOCIETE", "Tél.: +237 694 45 67 23");
            parameters.put("INFO_MATRICULE", "006788");
            parameters.put("INFO_NOM", "MBASSI MASSOUKE CYRILLE");
            parameters.put("INFO_NIU", "34668");
            parameters.put("INFO_CATEGORIE", "CATEGORIE A");
            parameters.put("INFO_ECHELON", "ECHELON 1");
            parameters.put("INFO_CHARGE", "4");
            parameters.put("INFO_ADRESSE", "DOUALA 1");
            parameters.put("INFO_FONCTION", "INGENIEUR ETUDE");
            parameters.put("INFO_DATE_ENTREE", "01/08/2020");
            parameters.put("INFO_NUMERO_CNSS", "898865");
            parameters.put("INFO_CPTE_VIREMENT", "6800-5667887899-56");
            parameters.put("INFO_SEXE", "Masculin");
            parameters.put("INFO_DIRECTION", "Informatique");
            parameters.put("INFO_DEPARTEMENT", "Technique");
            parameters.put("INFO_SERVICE", "Achat");
            parameters.put("INFO_GRADE", "Cadre");
            parameters.put("INFO_SITFAM", "Marié");
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
            parameters.put("DATE_DEBUT", "01/01/2020");
            parameters.put("DATE_FIN", "31/01/2020");
            parameters.put("CHEMIN_LOGO", "D:\\Programmation orientee objet\\Technologies\\Angular\\projets\\gestiondestock\\logo\\logo.jpg");
            parameters.put("tableData", dataSource);
            parameters.put("INFO_NAP", data.get(data.size()-1).getMont());

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

    static void executeReport4(){
        try {
            String filePath = ResourceUtils.getFile("classpath:RptMouvementComptable.jrxml")
                    .getAbsolutePath();

            System.out.println("Chemin report="+filePath);
            List<LigneMouvementCptble> data = new ArrayList<LigneMouvementCptble>();
            BigDecimal totalDebit = BigDecimal.ZERO;
            BigDecimal totalCredit = BigDecimal.ZERO;
            for(int i=1; i<=5;i++){
                LigneMouvementCptble lg = new LigneMouvementCptble();
                lg.setNumcpte("Compte n° "+i);
                lg.setSens("D");
                lg.setLibecriture("ELEMENT SALAIRE "+i);
                lg.setMntpce(NumberFormat.getInstance(Locale.FRENCH).format(new BigDecimal("50000").doubleValue()));
                totalDebit = totalDebit.add(new BigDecimal("50000"));

                data.add(lg);
            }
            for(int i=1; i<=5;i++){
                LigneMouvementCptble lg = new LigneMouvementCptble();
                lg.setNumcpte("Compte n° "+i);
                lg.setSens("C");
                lg.setLibecriture("ELEMENT SALAIRE "+i);
                lg.setMntpce(NumberFormat.getInstance(Locale.FRENCH).format(new BigDecimal("40000").doubleValue()));
                totalCredit = totalCredit.add(new BigDecimal("40000"));

                data.add(lg);
            }

            System.out.println("Ajout datasource");
            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

            System.out.println("Ajout paramètres");
            Map<String, Object> parameters = new HashMap<String, Object>();
            parameters.put("NOM_SOCIETE", "KIN'ART REMAKE");
            parameters.put("ADRESSE_SOCIETE", "B.P.: 56789 DOUALA");
            parameters.put("CONTACT_SOCIETE", "Tél.: +237 694 45 67 23");
            parameters.put("TITRE_ETAT", "MOUVEMENT COMPTABLE JANVIER 2022");
            parameters.put("CHEMIN_LOGO", "D:\\Programmation orientee objet\\Technologies\\Angular\\projets\\gestiondestock\\logo\\logo.jpg");
            parameters.put("tableData", dataSource);
            parameters.put("TOTAL_DEBIT", NumberFormat.getInstance(Locale.FRENCH).format(totalDebit.doubleValue()));
            parameters.put("TOTAL_CREDIT", NumberFormat.getInstance(Locale.FRENCH).format(totalCredit.doubleValue()));

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

    static void executeReport5(){
        try {
            String filePath = ResourceUtils.getFile("classpath:RptDeclarationDeVersement.jrxml")
                    .getAbsolutePath();

            System.out.println("Ajout datasource");
            LigneDeclarationVersement declarationVersement = new LigneDeclarationVersement();
            declarationVersement.setFne(new BigDecimal(1250));
            declarationVersement.setCfcpat(new BigDecimal(6500));
            declarationVersement.setCfcsal(new BigDecimal(12700));
            declarationVersement.setIrpp(new BigDecimal(45000));
            declarationVersement.setTd(new BigDecimal(24500));
            declarationVersement.setCacirpp(new BigDecimal(2100));
            declarationVersement.setCnps(new BigDecimal(78000));
            declarationVersement.setRav(new BigDecimal(2300));
            Map<String, Object> parameters = EFCMRParameter.setParametersDeclVersement(declarationVersement,"202105");

            JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(List.of());
            System.out.println("Compilation");
            JasperReport report = JasperCompileManager.compileReport(filePath);

            System.out.println("Association des données");
            JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource(1));

            String fileName = "DeclarationDeVersement.pdf";
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
