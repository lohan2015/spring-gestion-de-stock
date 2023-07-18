package com.kinart.api.portail.controller;

import com.kinart.api.gestiondepaie.dto.ParamDataDto;
import com.kinart.api.portail.dto.DemandeModifInfoDto;
import com.kinart.api.portail.dto.ElementVarCongeDto;
import com.kinart.api.portail.dto.ModifInfoSalarieDto;
import com.kinart.paie.business.model.Salarie;
import com.kinart.paie.business.repository.ParamDataRepository;
import com.kinart.paie.business.repository.SalarieRepository;
import com.kinart.portail.business.helper.FileNameHelper;
import com.kinart.portail.business.model.DemandeHabilitation;
import com.kinart.portail.business.model.DemandeModifInfo;
import com.kinart.portail.business.repository.DemandeModifInfoRepository;
import com.kinart.portail.business.service.NotificationModifInfoService;
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
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PORTAIL;

@Api("demande-modif-info")
@RestController
@RequiredArgsConstructor
public class DemandeModifInfoController {

    private final ObjectsValidator<DemandeModifInfoDto> validator;
    private final DemandeModifInfoRepository repository;
    private final NotificationModifInfoService notificationService;
    private final ParamDataRepository paramDataRepository;
    private final UtilisateurRepository utilisateurRepository;
    private final RestTemplate restTemplate;
    private final SalarieRepository salarieRepository;

    private FileNameHelper fileHelper = new FileNameHelper();

    @PostMapping(value = APP_ROOT_PORTAIL + "/demande/modfinfo/user", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande modfinfo", notes = "Cette methode permet d'enregistrer des demandes modfinfo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandeModifInfoDto> saveUser(@RequestBody DemandeModifInfoDto dto){
        validator.validate(dto);

        try {
            String fileName = fileHelper.generateDisplayName(dto.getFile().getOriginalFilename());
            dto.setFileName(fileName);
            dto.setData(dto.getFile().getBytes());
            dto.setFileType(dto.getFile().getContentType());
            dto.setFileSize(dto.getFile().getSize());

            // Get info user
            Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getUserDemModInfo().getEmail());
            if(user.isPresent()){
                dto.getUserDemModInfo().setNom(user.get().getNom());
                dto.getUserDemModInfo().setPrenom(user.get().getPrenom());
            } else throw new EntityNotFoundException("Utilisateur inexistant");

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
            dto.setValid(emailValidator);
            dto.setStatus(EnumStatusType.ATTENTE_VALIDATION);
            repository.save(DemandeModifInfoDto.toEntity(dto));

            // Gestion des notifications
            notificationService.sendModifInfoNotification(dto, dto.getValid());

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/modfinfo/valid", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande modfinfo", notes = "Cette methode permet d'enregistrer des demandes modfinfo")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandeModifInfoDto> saveValid(@RequestBody DemandeModifInfoDto dto){
        validator.validate(dto);
        try {
            Optional<DemandeModifInfo> dbDemande = repository.findById(dto.getId());
            if(dbDemande.isPresent()){
                DemandeModifInfo entite = dbDemande.get();
                entite.setStatus(dto.getStatus());
                repository.save(entite);

                // Gestion des notifications
                Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getUserDemModInfo().getEmail());
                Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entite.getValid());
                if(user.isPresent()){
                    dto.getUserDemModInfo().setNom(user.get().getNom());
                    dto.getUserDemModInfo().setPrenom(user.get().getPrenom());
                } else throw new EntityNotFoundException("Utilisateur inexistant");
                // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus().equals(EnumStatusType.VALIDEE)){
                    if(validator.isPresent())
                        notificationService.sendModifInfoNotificationSender(dto, validator.get().getPrenom()+ " "+validator.get().getNom());
                    // MAJ dans Amplitude RH
                    Optional<Salarie> salDB =   salarieRepository.findByAdr4(dto.getUserDemModInfo().getEmail());
                    if(salDB.isPresent()){
                        String host = "http://localhost:8082";
                        String urlPost = host+"/amplituderh/v1/modifinfosalarie";
                        ModifInfoSalarieDto modifInfoSalarieDto = new ModifInfoSalarieDto();
                        modifInfoSalarieDto.setCdos("02");
                        modifInfoSalarieDto.setMatricule(salDB.get().getNmat());
                        modifInfoSalarieDto.setKey(dto.getChampConcerne());
                        modifInfoSalarieDto.setValue(dto.getValeurSouhaitee());

                        HttpHeaders headers = new HttpHeaders();
                        headers.setContentType(MediaType.APPLICATION_JSON);
                        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
                        HttpEntity<ModifInfoSalarieDto> entity = new  HttpEntity<ModifInfoSalarieDto>(modifInfoSalarieDto,headers);
                        restTemplate.exchange(urlPost, HttpMethod.POST, entity, ModifInfoSalarieDto.class);
                    } else throw new EntityNotFoundException("Aucun salarié correspondant a l'adresse mail de l'utilisateur");

                } else if(dto.getStatus().equals(EnumStatusType.REJETEE))// Sinon notification du sender du rejet
                    notificationService.sendModifInfoNotificationRejet(dto);
            }
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
    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/modfinfo/showdocument/{demande-id}")
    public ResponseEntity<byte[]> getDocument(@RequestParam Integer demandeid) throws Exception {
        Optional<DemandeModifInfo> habilitation = repository.findById(demandeid);
        if(habilitation.isPresent())
            return ResponseEntity.ok().contentType(MediaType.valueOf(habilitation.get().getFileType())).body(habilitation.get().getData());
        else throw new EntityNotFoundException("Pas de fichier trouvé");
    }

    /**
     * Sends valid or default image bytes with given fileName pathVariable.
     *
     * @param demandeid
     * @return return valid byte array
     */
    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/modfinfo/showdocument2/{demande-id}")
    public ResponseEntity<Object> getDocument2(@RequestParam Integer demandeid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Optional<DemandeModifInfo> habilitation = repository.findById(demandeid);

        if(habilitation.isPresent()){
            response.setContentType(habilitation.get().getFileType());

            // Response header
            response.setHeader("Pragma", "public");
            response.setHeader("responseType", "blob");
            response.setHeader("Content-Disposition", "attachment; filename=\"" + habilitation.get().getFileName() + "\"");

            // Read from the file and write into the response
            OutputStream os = response.getOutputStream();
            os.write(habilitation.get().getData());

            os.flush();
            os.close();
        } else throw new EntityNotFoundException("Pas de fichier trouvé");

        return null;
    }

    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/modfinfo/status/{email}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Renvoi la liste des demandes", notes = "Cette methode permet de chercher et renvoyer la liste des éléments qui existent "
            + "dans la BDD", responseContainer = "List<DemandeHabilitationDto>")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "La liste des demandes absence conge / Une liste vide")
    })
    ResponseEntity<List<DemandeModifInfoDto>> findByUserAndDateStatus(
            @PathVariable("email") String email,
            @RequestParam("start-date") @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startDate,
            @RequestParam("end-date")  @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endDate,
            @RequestParam("status1")  String status1
    ){
        LocalDateTime start = LocalDateTime.of(startDate, LocalTime.of(0, 0, 0));
        LocalDateTime end = LocalDateTime.of(endDate, LocalTime.of(23, 59, 59));
        List<DemandeModifInfoDto> demandeAbsenceConges = repository.searchByUserEmailAndPeriodeStatus(start, end, email, status1).stream()
                .map(DemandeModifInfoDto::fromEntity)
                .collect(Collectors.toList());
        if(demandeAbsenceConges!=null) {
            return ResponseEntity.ok(demandeAbsenceConges);
        } else {
            throw new EntityNotFoundException("Pas de demandes");
        }
    }

    @DeleteMapping(value = APP_ROOT_PORTAIL + "/demande/modfinfo/{demand-id}")
    @ApiOperation(value = "Supprimer une demande pas encore validée", notes = "Cette methode permet de supprimer une demande pas encore validée")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément a ete supprime")
    })
    void delete(@PathVariable("demand-id") Integer demandid) throws Exception {
        Optional<DemandeModifInfo> entite = repository.findById(demandid);
        if(entite.isPresent()) repository.deleteById(demandid);

        // Notification validateur de l'annulation
        DemandeModifInfoDto dto = DemandeModifInfoDto.fromEntity(entite.get());
        notificationService.sendAnnulationModifInfoNotification(dto, dto.getValid());
    }
}
