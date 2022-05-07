package com.kinart.api.gestiondestock.controller;

import com.kinart.api.gestiondestock.controller.api.CommandeClientApi;
import com.kinart.api.gestiondestock.dto.CommandeClientDto;
import com.kinart.api.gestiondestock.dto.LigneCommandeClientDto;
import com.kinart.api.gestiondestock.report.LigneCommandeReport;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.stock.business.exception.InvalidEntityException;
import com.kinart.stock.business.model.EtatCommande;
import com.kinart.stock.business.services.CommandeClientService;

import java.io.*;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.ResourceUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
public class CommandeClientController implements CommandeClientApi {

  private CommandeClientService commandeClientService;

  @Autowired
  public CommandeClientController(CommandeClientService commandeClientService) {
    this.commandeClientService = commandeClientService;
  }

  @Override
  public ResponseEntity<CommandeClientDto> save(CommandeClientDto dto) {
   try {
      commandeClientService.save(dto);
    } catch (InvalidEntityException e){
      return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
    }

    return new ResponseEntity(dto, HttpStatus.CREATED);
  }

  @Override
  public ResponseEntity<CommandeClientDto> updateEtatCommande(Integer idCommande, EtatCommande etatCommande) {
    return ResponseEntity.ok(commandeClientService.updateEtatCommande(idCommande, etatCommande));
  }

  @Override
  public ResponseEntity<CommandeClientDto> updateQuantiteCommande(Integer idCommande, Integer idLigneCommande, BigDecimal quantite) {
    return ResponseEntity.ok(commandeClientService.updateQuantiteCommande(idCommande, idLigneCommande, quantite));
  }

  @Override
  public ResponseEntity<CommandeClientDto> updateClient(Integer idCommande, Integer idClient) {
    return ResponseEntity.ok(commandeClientService.updateClient(idCommande, idClient));
  }

  @Override
  public ResponseEntity<CommandeClientDto> updateArticle(Integer idCommande, Integer idLigneCommande, Integer idArticle) {
    return ResponseEntity.ok(commandeClientService.updateArticle(idCommande, idLigneCommande, idArticle));
  }

  @Override
  public ResponseEntity<CommandeClientDto> deleteArticle(Integer idCommande, Integer idLigneCommande) {
    return ResponseEntity.ok(commandeClientService.deleteArticle(idCommande, idLigneCommande));
  }

  @Override
  public ResponseEntity<CommandeClientDto> findById(Integer id) {
    return ResponseEntity.ok(commandeClientService.findById(id));
  }

  @Override
  public ResponseEntity<CommandeClientDto> findByCode(String code) {
    return ResponseEntity.ok(commandeClientService.findByCode(code));
  }

  @Override
  public ResponseEntity<List<CommandeClientDto>> findAll() {
    return ResponseEntity.ok(commandeClientService.findAll());
  }

  @Override
  public ResponseEntity<List<LigneCommandeClientDto>> findAllLignesCommandesClientByCommandeClientId(Integer idCommande) {
    return ResponseEntity.ok(commandeClientService.findAllLignesCommandesClientByCommandeClientId(idCommande));
  }

  @Override
  public ResponseEntity<Void> delete(Integer id) {
    commandeClientService.delete(id);
    return ResponseEntity.ok().build();
  }

  @Override
  public void getReport(CommandeClientDto dto, HttpServletRequest request, HttpServletResponse response) {
    try {
      String filePath = ResourceUtils.getFile("classpath:RptCommandeClientFournisseur.jrxml")
              .getAbsolutePath();

      //System.out.println("Chemin report="+filePath);
      List<LigneCommandeReport> data = new ArrayList<LigneCommandeReport>();
      for(LigneCommandeClientDto ligne : dto.getLigneCommandeClients()){
        LigneCommandeReport lg = new LigneCommandeReport();
        lg.setCodeArticle(ligne.getArticle().getCodeArticle());
        lg.setLibelleArticle(ligne.getArticle().getDesignation());
        lg.setQuantite(ligne.getQuantite());
        lg.setPrixUnitaire(ligne.getPrixUnitaire());
        lg.setPrixTtc(ligne.getQuantite().multiply(ligne.getPrixUnitaire()));

        data.add(lg);
      }

      //System.out.println("Ajout datasource");
      JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(data);

     // System.out.println("Ajout paramètres");
      Map<String, Object> parameters = new HashMap<String, Object>();
      parameters.put("TITRE_ETAT", "CLIENT N° "+dto.getCode());
      parameters.put("USER", dto.getClient().getMail());
      parameters.put("DATE_CMD", new ClsDate(dto.getDateCommande()).getDateS());
      parameters.put("NOM_CLT_FRN", dto.getClient().getNom()+" "+dto.getClient().getPrenom());
      parameters.put("TEL_CLT_FRN", dto.getClient().getNumTel());
      parameters.put("ETAT_CMD", dto.getEtatCommande().toString());
      parameters.put("CHEMIN_LOGO", "D:\\Programmation orientee objet\\Technologies\\Angular\\projets\\gestiondestock\\logo\\logo.jpg");
      parameters.put("tableData", dataSource);

      //System.out.println("Compilation");
      JasperReport report = JasperCompileManager.compileReport(filePath);

      //System.out.println("Association des données");
      JasperPrint print = JasperFillManager.fillReport(report, parameters, new JREmptyDataSource(1));

      String fileName = "CommandeCLient"+dto.getCode()+".pdf";
      System.out.println("Nom fihier: "+fileName);
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

      //System.out.println(os);
      os.flush();
      os.close();
      is.close();

//      return ResponseEntity.ok().contentType(MediaType.parseMediaType(contentType))
//              .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
//              .body(resource);

//      ByteArrayInputStream stream = new ByteArrayInputStream(byteArray);
//      HttpHeaders headers = new HttpHeaders();
//      headers.add("Content-Disposition", "inline; filename="+fileName);
//
//        return ResponseEntity.ok()
//                .headers(headers)
//                .contentType(MediaType.APPLICATION_PDF)
//                .body(stream);

      //return new ResponseEntity<byte[]>(byteArray, headers, HttpStatus.OK);

    } catch(Exception e) {
      System.out.println("Exception while creating report");
      e.printStackTrace();
      //return new ResponseEntity<Resource>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }

}
