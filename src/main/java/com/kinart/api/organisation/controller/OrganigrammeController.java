package com.kinart.api.organisation.controller;

import com.kinart.api.organisation.controller.api.OrganigrammeApi;
import com.kinart.api.organisation.dto.*;
import com.kinart.organisation.business.services.*;
import com.kinart.organisation.business.vo.ClsMessageCelluleVO;
import com.kinart.organisation.business.vo.ClsParametreOrganigrammeVO;
import com.kinart.paie.business.model.ParamData;
import com.kinart.paie.business.services.utils.ClsResultat;
import com.kinart.paie.business.services.utils.ClsTreater;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.paie.business.services.utils.ParameterUtil;
import com.kinart.api.organisation.dto.*;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.util.Date;
import java.util.List;

@RestController
public class OrganigrammeController implements OrganigrammeApi {

    private GeneriqueConnexionService service;
    private OrganigrammeService organigrammeService;

    @Autowired
    public OrganigrammeController(GeneriqueConnexionService service, OrganigrammeService organigrammeService) {
        this.service = service;
        this.organigrammeService = organigrammeService;
    }

    @Override
    public ResponseEntity<OrganigrammeDto> save(OrganigrammeDto dto) {
        try {
            organigrammeService.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<OrganigrammeDto> findById(Integer id) {
        OrganigrammeDto dto = organigrammeService.findById(id);
        if(dto!=null) return ResponseEntity.ok(dto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<OrganigrammeDto>> findAll(RechercheListeOrganigrammeDto search) {
        List<OrganigrammeDto> organigrammeDtos = organigrammeService.findAll(search);
        if(organigrammeDtos!=null) {
            return ResponseEntity.ok(organigrammeDtos);
        } else {
            throw new EntityNotFoundException("Pas de niveaux trouvés");
        }
    }

    @Override
    public void delete(String codeorganigramme) {
        organigrammeService.delete(codeorganigramme);
    }

    @Override
    public ResponseEntity<ResultatDessinOrganigrammeDto> dessinerOrganigramme(ParametreOrganigrammeDto dto, HttpServletRequest request, HttpServletResponse response) {
        ResultatDessinOrganigrammeDto result = new ResultatDessinOrganigrammeDto();
        ClsParametreOrganigrammeVO param = new ClsParametreOrganigrammeVO();
        BeanUtils.copyProperties(dto, param);
        result.setLegende(ClsParametreOrganigrammeVO.__getLegendeOrganigramme(request, service, param.getDossier()));
        ICellsDesigner paintre = null;
        if (StringUtils.equalsIgnoreCase(param.getTypeDiagramme(), ClsParametreOrganigrammeVO.DIAGRAMME_LISTE)){
            List l = service.find("from ParamData " + " where identreprise =" + param.getDossier() + " and ctab=266 and nume=2 and cacc='NFILSTOTAL'");
            for (Object object : l)
            {
                if (object instanceof ParamData)
                {
                    ParamData o1 = (ParamData) object;
                    param.setAfficherTousLesFils(StringUtils.equals("O", o1.getVall()));
                }
            }
            //param.setAfficherTousLesFils(StringUtils.equals("O", ClsConfigurationParameters.getConfigParameterValue(service, param.getDossier(), param.getLangue(), ClsConfigurationParameters.AFFICHER_NBRE_FILS_TOTAL_ORGANIGRAMME)));
        }

        ClsMessageCelluleVO message = new ClsMessageCelluleVO(request);
        if(StringUtils.equals(ClsParametreOrganigrammeVO.DIAGRAMME_RATEAU, param.getTypeDiagramme()))
            paintre = new ClsOrgCellsDesigner(service, param, message);
        else if(StringUtils.equals(ClsParametreOrganigrammeVO.DIAGRAMME_LISTE, param.getTypeDiagramme()))
            paintre = new ClsOrgListCellsDesigner(service, param, message);
//        else if(StringUtils.equals(ClsParametreOrganigrammeVO.DIAGRAMME_EXTRACTION, param.getTypeDiagramme())){
//            ClsOrgCellsExtract extract = new ClsOrgCellsExtract(service, param, message);
//            try {
////                result.setUrlPathExtract(extract._extractData(request));
////                result.setShowLienExtract(true);
//                String fileGenere = extract._extractData(request);
//                //TODO envoi du fichier
//                File file = new File(fileGenere);
//                FileInputStream is = new FileInputStream(file);
//                String fileName = "EXTRAIT_ORGANIGRAMME_" + param.getCelluleDepart() + "_" + param.getNiveauArrive() + "_"
//                        + new Date().getTime() + ".XLS";
//
//                response.setContentType("application/blob");
//
//                // Response header
//                response.setHeader("Pragma", "public");
//                response.setHeader("responseType", "blob");
//                response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
//
//                // Read from the file and write into the response
//                OutputStream os = response.getOutputStream();
//                //System.out.println(os);
//                byte[] buffer = new byte[(int) file.length()];
//
//                int len;
//                while ((len = is.read(buffer)) != -1) {
//                    os.write(buffer, 0, len);
//                }
//
//                os.flush();
//                os.close();
//                is.close();
//
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//
//            return ResponseEntity.ok(result);
//        }

        String organigramme = paintre._getFlowChart();
        result.setShowLienExtract(false);

        if (StringUtils.equalsIgnoreCase(param.getTypeDiagramme(), ClsParametreOrganigrammeVO.DIAGRAMME_RATEAU))
            result.setOrganigramme(StringUtils.replace(organigramme, ClsParametreOrganigrammeVO.STR_SPECIAL_CHAR, ClsParametreOrganigrammeVO. STR_SPECIAL_CHAR_REPLACEMENT));
        else result.setOrganigramme(organigramme);

        String strOrgName = "";
        if (paintre.getAllCellules().size() > 0)
        {
            ClsOrgCellule cellPere = paintre.getAllCellules().get(0);
            strOrgName = cellPere.getLibelle().replaceAll("apostrphe123", "'");
        }

        ClsResultat result2 = ClsTreater._getResultat("Organigramme à partir de %", "INF-80160", false, new String[] { strOrgName
                + " ------------------------------------------------------------------------------------------------" });
        result.setNomOrganigramme(ParameterUtil.__getConfirmationMsg2(result2.getLibelle()));

        //result.setLegende(ClsParametreOrganigrammeVO.__getLegendeOrganigramme(request, service, param.getDossier()));
        //result.setLegende(organigrammeService.getLegende(param.getDossier()));

        genererOrganigramme();

        return ResponseEntity.ok(result);
    }

    @Override
    public ResponseEntity<Object> extractionOrganigramme(ParametreOrganigrammeDto dto, HttpServletRequest request, HttpServletResponse response) {
        ResultatDessinOrganigrammeDto result = new ResultatDessinOrganigrammeDto();
        ClsParametreOrganigrammeVO param = new ClsParametreOrganigrammeVO();
        BeanUtils.copyProperties(dto, param);

        ClsMessageCelluleVO message = new ClsMessageCelluleVO(request);
        ClsOrgCellsExtract extract = new ClsOrgCellsExtract(service, param, message);
        try {
//                result.setUrlPathExtract(extract._extractData(request));
//                result.setShowLienExtract(true);
            String fileGenere = extract._extractData(request);
            //TODO envoi du fichier
            File file = new File(fileGenere);
            FileInputStream is = new FileInputStream(file);
            String fileName = "EXTRAIT_ORGANIGRAMME_" + param.getCelluleDepart() + "_" + param.getNiveauArrive() + "_"
                    + new Date().getTime() + ".XLS";

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

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public boolean existCellule(String codeorganigramme) {
        return organigrammeService.isCelluleExist(codeorganigramme);
    }

    @Override
    public String getPossibilites(String codepere) {
        return organigrammeService.getPossibilites(codepere);
    }

    @Override
    public List<String> controleAffectationPoste(OperationOrganigrammeDto dto) {
        return organigrammeService.controleAffectationPoste(dto);
    }

    @Override
    public List<String> affectationPosteOrganigramme(OperationOrganigrammeDto dto) {
        return organigrammeService.affectationPosteOrganigramme(dto);
    }

    @Override
    public List<String> controleAffectationSalarie(OperationOrganigrammeDto dto) {
        return organigrammeService.controleAffectationSalarie(dto);
    }

    @Override
    public List<String> affectationSalariePoste(OperationOrganigrammeDto dto) {
        return organigrammeService.affectationSalariePoste(dto);
    }

    @Override
    public List<String> rattacherCellules(OperationOrganigrammeDto dto) {
        return null;
    }

    private void genererOrganigramme(){
        try {

//            if(!showLien) return;
//
//            String appRealPath = CommonFunctions.getApplicationContextRealPath(this.request);
//            if (!appRealPath.endsWith(File.separator)) appRealPath += File.separator;
//            String nomFichierHtml = getUser()+"_Organigramme_du_"+new Date().getTime()+ ".html";
//            appRealPath += nomFichierHtml;
//
//            // Sauvegarde de l'organigramme dans un fichier html
//            File file = new File(appRealPath);
////			  FileUtils.writeStringToFile(file, organigramme.replaceAll("<br>", "<br/>"));
//            try {
//                if(!file.exists()) file.createNewFile();
//
//                FileWriter fw = new FileWriter(file, true);
//
//                BufferedWriter bw = new BufferedWriter(fw);
//                bw.write(organigramme.replaceAll("<br>", "<br/>"));
//                bw.close();
//                fw.close();
//
//            } catch (Exception e) {
//                // TODO: handle exception
//                e.printStackTrace();
//            }
//
//            setAppUrlPath(this.request.getContextPath() + "/" + "pages/genfiles/" + nomFichierHtml+ ".PDF");
////			  System.out.println(getAppUrlPath());
//            icebrowser.HTMLToPDFHeadless app = new HTMLToPDFHeadless();
//            app.init();
//            Vector urls = new Vector();
//            urls.addElement(appRealPath);
//
//            int formatdef = StormPageFormat.ISO_A4;
//            if("A0".equalsIgnoreCase(format)) formatdef = StormPageFormat.ISO_A0;
//            else if("A1".equalsIgnoreCase(format)) formatdef = StormPageFormat.ISO_A1;
//            else if("A2".equalsIgnoreCase(format)) formatdef = StormPageFormat.ISO_A2;
//            else if("A3".equalsIgnoreCase(format)) formatdef = StormPageFormat.ISO_A3;
//            else if("A4".equalsIgnoreCase(format)) formatdef = StormPageFormat.ISO_A4;
//            else if("A5".equalsIgnoreCase(format)) formatdef = StormPageFormat.ISO_A5;
//            else formatdef = StormPageFormat.NA_LETTER;
//
//            int miseEnPgeDef = StormPageFormat.LANDSCAPE;
//            if("PT".equalsIgnoreCase(typeformat)) miseEnPgeDef = StormPageFormat.PORTRAIT;
//            else miseEnPgeDef = StormPageFormat.LANDSCAPE;
//
//            scale = Double.parseDouble(echelle.trim());
//            app.test(urls, urls.size(), scale, formatdef, miseEnPgeDef);

        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

}
