package com.kinart.api.portail.controller;

import com.kinart.api.gestiondepaie.dto.ParamDataDto;
import com.kinart.api.portail.dto.DemandeHabilitationDto;
import com.kinart.api.portail.dto.DemandeHabilitationResponse;
import com.kinart.api.portail.dto.NotifabscongeDto;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.paie.business.services.utils.GeneriqueConnexionService;
import com.kinart.portail.business.helper.FileNameHelper;
import com.kinart.portail.business.model.DemandeHabilitation;
import com.kinart.portail.business.repository.DemandeHabilitationRepository;
import com.kinart.portail.business.service.NotificationHabilitationService;
import com.kinart.portail.business.utils.EnumStatusType;
import com.kinart.portail.business.validator.ObjectsValidator;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.ErrorCodes;
import com.kinart.stock.business.exception.InvalidEntityException;
import com.kinart.stock.business.model.Utilisateur;
import com.kinart.stock.business.repository.UtilisateurRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.beans.BeanUtils;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.sql.Blob;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PORTAIL;

@Api("demande-habilitation")
@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:4200")
public class DemandeHabilitationController {

    private final ObjectsValidator<DemandeHabilitationDto> validator;
    private final DemandeHabilitationRepository repository;
    private final NotificationHabilitationService notificationService;
    private final ParamDataRepository paramDataRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final GeneriqueConnexionService generiqueConnexionService;

    private FileNameHelper fileHelper = new FileNameHelper();

    @PostMapping(value = APP_ROOT_PORTAIL + "/demande/habilitation/user", consumes = {MediaType.MULTIPART_FORM_DATA_VALUE, MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Sauvegarte demande habilitation", notes = "Cette methode permet d'enregistrer des demandes habilitation", responseContainer = "DemandeHabilitationResponse")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandeHabilitationResponse> saveUser(@RequestParam("email") String email, @RequestParam("image") MultipartFile file){
        //System.out.println("EMAIL-------------------"+email);
        //System.out.println("IMAGE 1-------------------"+file.getOriginalFilename());
        //System.out.println("IMAGE 2-------------------"+file.getContentType());
        //System.out.println("IMAGE 3-------------------"+file.getSize());
        //validator.validate(dto);
        //System.out.println("Declaration DemandeHabilitationDto-------------------");
        DemandeHabilitationDto dto = new DemandeHabilitationDto();

        try {
            //System.out.println("Debut Elements fixés-------------------");
            //String fileName = fileHelper.generateDisplayName(file.getOriginalFilename());
            dto.setFileName(file.getOriginalFilename());
            dto.setFileData(file.getBytes());
            dto.setFileType(file.getContentType());
            dto.setFileSize(file.getSize());
            dto.getUserDemHabil().setEmail(email);
            //System.out.println("Elements fixés-------------------");

            // Get info user
            Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getUserDemHabil().getEmail());
            if(user.isPresent()){
                dto.setUserDemHabil(user.get());
            } else throw new EntityNotFoundException("Utilisateur inexistant");
            //System.out.println("Utilisateur existant-------------------");

            // Fixer les validateurs
            String emailValidator = "cyrille.mbassi@yahoo.com";
            ParamDataDto fnom = paramDataRepository.findByNumeroLigne(Integer.valueOf(99), "VAL_DH", Integer.valueOf(1))
                    .map(ParamDataDto::fromEntity)
                    .orElseThrow(() ->
                            new EntityNotFoundException(
                                    "Aucune donnée avec l'ID = "+"VAL_DH"+" n' ete trouve dans la table 99",
                                    ErrorCodes.ARTICLE_NOT_FOUND)
                    );

            if(fnom == null)
                throw new EntityNotFoundException("Aucune donnée avec l'ID = "+"VAL_DH"+" n'a pas été trouvée dans la table 99", ErrorCodes.ARTICLE_NOT_FOUND);
            else emailValidator = fnom.getVall();
            //System.out.println("Validateur existant-------------------");

            dto.setValid(emailValidator);
            dto.setStatus(EnumStatusType.ATTENTE_VALIDATION);
            DemandeHabilitation entity = repository.save(DemandeHabilitationDto.toEntity(dto));
            if(entity != null)
                dto.setId(entity.getId());
            //System.out.println("Sauvegarde effectuée-------------------");

            // Gestion des notifications
            notificationService.sendHabilitationNotification(dto, dto.getValid());
            //System.out.println("Envoi mail effectué-------------------");

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(DemandeHabilitationResponse.fromDto(dto), HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/habilitation/valid", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande habilitation", notes = "Cette methode permet d'enregistrer des demandes habilitation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandeHabilitationResponse> saveValid(@RequestBody DemandeHabilitationResponse dto){
        //validator.validate(dto);
        try {
            //Optional<DemandeHabilitation> dbDemande = repository.findById(dto.getId());
            //if(dbDemande.isPresent()){
                /**DemandeHabilitation entite = dbDemande.get();
                entite.setStatus(EnumStatusType.valueOf(dto.getStatus()));
                repository.save(entite);*/
                repository.updateDemande(dto.getId(), dto.getStatus());

                // Gestion des notifications
                Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getEmail());
                Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(dto.getValid());
                DemandeHabilitationDto dto2 = new DemandeHabilitationDto();
                BeanUtils.copyProperties(dto, dto2);
                dto2.setCreationDate(new ClsDate(dto.getValueDate(), "dd/MM/yyyy").getDate().toInstant());

                if(user.isPresent()){
                    dto2.setUserDemHabil(user.get());
                } else throw new EntityNotFoundException("Utilisateur inexistant");
                // Si validé envoi mail a sender et validateur suivant
                //System.out.println("STATUS OF DEMAND================="+dto.getStatus());
                //System.out.println("VALIDATOR================="+validator.get().getEmail());
                if(dto.getStatus().equalsIgnoreCase(EnumStatusType.VALIDEE.getCode())){
                    if(validator.isPresent()){
                        //System.out.println("ENVOI MAIL================="+validator.get().getEmail());
                        notificationService.sendHabilitationNotificationSender(dto2, validator.get().getPrenom()+ " "+validator.get().getNom());
                    }

                } else if(dto.getStatus().equalsIgnoreCase(EnumStatusType.REJETEE.getCode())){
                    // Sinon notification du sender du rejet
                    //System.out.println("ENVOI MAIL================="+validator.get().getEmail());
                    notificationService.sendHabilitationNotificationRejet(dto2);
                }
            //}
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    /**
     * Sends valid or default image bytes with given fileName pathVariable.
     *
     * @param demandeid
     * @return return valid byte array
     */
    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/habilitation/showdocument/{demandeid}", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    public ResponseEntity<byte[]> getDocument(@PathVariable Integer demandeid) throws Exception {

        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.file_type, n.file_data "+
                "FROM demandehabilitation n "+
                "WHERE n.id=:demandeid";

        Query q = session.createSQLQuery(query);
        q.setParameter("demandeid", demandeid);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);
        byte[] fileData = null;
        String fileType = null;
        for (Object[] o : lst)
        {
            if(o[0]!=null) fileType = o[0].toString();
            if(o[1]!=null){
                Blob blob = (Blob) o[1];
                fileData = blob.getBytes(1l, (int)blob.length());
            }
        }

        // Optional<DemandeHabilitation> habilitation = repository.findById(demandeid);
        if(fileData != null)
            return ResponseEntity.ok().contentType(MediaType.valueOf(fileType)).body(fileData);
        else throw new EntityNotFoundException("Pas de fichier trouvé");
    }

    /**
     * Sends valid or default image bytes with given fileName pathVariable.
     *
     * @param demandeid
     * @return return valid byte array
     */
    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/habilitation/showdocument2/{demandeid}", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    public ResponseEntity<Object> getDocument2(@PathVariable Integer demandeid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Optional<DemandeHabilitation> habilitation = repository.findById(demandeid);

        if(habilitation.isPresent()){
            response.setContentType(habilitation.get().getFileType());

            // Response header
            response.setHeader("Pragma", "public");
            response.setHeader("responseType", "blob");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + habilitation.get().getFileName() + "\"");

            // Read from the file and write into the response
            OutputStream os = response.getOutputStream();
            os.write(habilitation.get().getFileData());

            os.flush();
            os.close();
        } else throw new EntityNotFoundException("Pas de fichier trouvé");

        return null;
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/habilitation/status/{email}", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.APPLICATION_PDF_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE, MediaType.APPLICATION_OCTET_STREAM_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandeHabilitationResponse>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    @CrossOrigin
    ResponseEntity<List<DemandeHabilitationResponse>> findByUserAndDateStatus(
            @PathVariable("email") String email,
            @RequestParam("startDate") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("endDate")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status1")  String status1
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        Session session = generiqueConnexionService.getSession();
        String query = "SELECT n.id, n.creation_date, n.identreprise, n.user_id, n.file_name "+
                ", n.file_type, n.file_size, n.valid, n.status, u1.nom, u1.prenom, u1.email "+
                "FROM demandehabilitation n "+
                "LEFT JOIN utilisateur u1 ON u1.id=n.user_id "+
                "WHERE (u1.email=:email OR n.valid=:email) AND n.creation_date BETWEEN :start AND :end ORDER BY n.creation_date DESC";

        Query q = session.createSQLQuery(query);
        q.setParameter("email", email);
        q.setParameter("start", start);
        q.setParameter("end", end);
        List<Object[]> lst = q.getResultList();
        generiqueConnexionService.closeSession(session);
        List<DemandeHabilitationResponse> liste = new ArrayList<DemandeHabilitationResponse>();
        for (Object[] o : lst)
        {
            DemandeHabilitationResponse cptble = new DemandeHabilitationResponse();
            if(o[0]!=null) cptble.setId(Integer.parseInt(o[0].toString()));
            if(o[1]!=null) cptble.setCreationDate(((Date) o[1]).toInstant());
            if(o[2]!=null) cptble.setIdEntreprise(Integer.parseInt(o[2].toString()));
            if(o[3]!=null) cptble.setUserId(Integer.parseInt(o[3].toString()));
            if(o[4]!=null) cptble.setFileName(o[4].toString());
            if(o[5]!=null) cptble.setFileType(o[5].toString());
            if(o[6]!=null) cptble.setFileSize(Long.parseLong(o[6].toString()));
            if(o[7]!=null) cptble.setValid(o[7].toString());
            if(o[8]!=null) cptble.setStatus(o[8].toString());
            if(o[9]!=null) cptble.setAuthor(o[9].toString());
            if(o[10]!=null) cptble.setAuthor(cptble.getAuthor()+" "+o[10].toString());
            if(o[11]!=null) cptble.setEmail(o[11].toString());


            cptble.setDemandid(String.valueOf(cptble.getId()));
            //System.out.println("DEMANDE ID="+cptble.getDemandid());
            cptble.setValueDate(new ClsDate((Date) o[1]).getDateS("dd/MM/yyyy"));

            liste.add(cptble);
        }

        if(liste!=null) {
            return ResponseEntity.ok(liste);
        } else {
            throw new EntityNotFoundException("Pas de notification");
        }
    }

    @DeleteMapping(value = APP_ROOT_PORTAIL + "/demande/habilitation/{demandid}", consumes = {MediaType.ALL_VALUE}, produces = {MediaType.ALL_VALUE})
    @ApiOperation(value = "Supprimer une demande pas encore validée", notes = "Cette methode permet de supprimer une demande pas encore validée")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    ResponseEntity<Void> delete(@PathVariable("demandid") Integer demandid) throws Exception {
        DemandeHabilitationResponse entite = repository.searchDemandeById(demandid);
        if(entite != null) repository.deleteDemande(demandid);

                // Notification validateur de l'annulation
        DemandeHabilitationDto dto = DemandeHabilitationResponse.toDto(entite);
        Optional<Utilisateur> user =  utilisateurRepository.findById(entite.getUserId());
        if(user.isPresent()) dto.setUserDemHabil(user.get());

        notificationService.sendAnnulationHabilitationNotification(dto, dto.getValid());

        return ResponseEntity.accepted().build();
    }
}
