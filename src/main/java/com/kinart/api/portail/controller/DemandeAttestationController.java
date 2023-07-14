package com.kinart.api.portail.controller;

import com.kinart.api.gestiondepaie.dto.ParamDataDto;
import com.kinart.api.portail.dto.DemandeAbsenceCongeDto;
import com.kinart.api.portail.dto.DemandeHabilitationDto;
import com.kinart.paie.business.repository.ParamDataRepository;
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
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Optional;

import static com.kinart.stock.business.utils.Constants.APP_ROOT_PORTAIL;

@Api("demande-habilitation")
@RestController
@RequiredArgsConstructor
public class DemandeHabilitationController {

    private final ObjectsValidator<DemandeHabilitationDto> validator;
    private final DemandeHabilitationRepository repository;
    private final NotificationHabilitationService notificationService;
    private final ParamDataRepository paramDataRepository;
    private final UtilisateurRepository utilisateurRepository;

    private FileNameHelper fileHelper = new FileNameHelper();

    @PostMapping(value = APP_ROOT_PORTAIL + "/demande/habilitation/user", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande habilitation", notes = "Cette methode permet d'enregistrer des demandes habilitation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandeHabilitationDto> saveUser(@RequestBody DemandeHabilitationDto dto){
        validator.validate(dto);

        try {
            String fileName = fileHelper.generateDisplayName(dto.getFile().getOriginalFilename());
            dto.setFileName(fileName);
            dto.setData(dto.getFile().getBytes());
            dto.setFileType(dto.getFile().getContentType());
            dto.setFileSize(dto.getFile().getSize());

            // Get info user
            Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getUserDemHabil().getEmail());
            if(user.isPresent()){
                dto.getUserDemHabil().setNom(user.get().getNom());
                dto.getUserDemHabil().setPrenom(user.get().getPrenom());
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
            dto.setStatus(EnumStatusType.EATTENTE_VALIDATION);
            repository.save(DemandeHabilitationDto.toEntity(dto));

            // Gestion des notifications
            notificationService.sendHabilitationNotification(dto, dto.getValid());

        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @PatchMapping(value = APP_ROOT_PORTAIL + "/demande/habilitation/valid", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    @ApiOperation(value = "Sauvegarte demande habilitation", notes = "Cette methode permet d'enregistrer des demandes habilitation")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "L'élément cree / modifie"),
            @ApiResponse(code = 400, message = "L'élément n'est pas valide")
    })
    ResponseEntity<DemandeAbsenceCongeDto> saveValid(@RequestBody DemandeHabilitationDto dto){
        validator.validate(dto);
        try {
            Optional<DemandeHabilitation> dbDemande = repository.findById(dto.getId());
            if(dbDemande.isPresent()){
                DemandeHabilitation entite = dbDemande.get();
                entite.setStatus(dto.getStatus());
                repository.save(entite);

                // Gestion des notifications
                Optional<Utilisateur> user =  utilisateurRepository.findUtilisateurByEmail(dto.getUserDemHabil().getEmail());
                Optional<Utilisateur> validator =  utilisateurRepository.findUtilisateurByEmail(entite.getValid());
                if(user.isPresent()){
                    dto.getUserDemHabil().setNom(user.get().getNom());
                    dto.getUserDemHabil().setPrenom(user.get().getPrenom());
                } else throw new EntityNotFoundException("Utilisateur inexistant");
                // Si validé envoi mail a sender et validateur suivant
                if(dto.getStatus().equals(EnumStatusType.VALIDEE)){
                    if(validator.isPresent())
                        notificationService.sendHabilitationNotificationSender(dto, validator.get().getPrenom()+ " "+validator.get().getNom());
                    notificationService.sendHabilitationNotification(dto, entite.getValid());
                } else if(dto.getStatus().equals(EnumStatusType.REJETEE))// Sinon notification du sender du rejet
                    notificationService.sendHabilitationNotificationRejet(dto);
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
    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/habilitation/showdocument/{demande-id}")
    public ResponseEntity<byte[]> getDocument(@RequestParam Integer demandeid) throws Exception {
        Optional<DemandeHabilitation> habilitation = repository.findById(demandeid);
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
    @GetMapping(value = APP_ROOT_PORTAIL + "/demande/habilitation/showdocument2/{demande-id}")
    public ResponseEntity<Object> getDocument2(@RequestParam Integer demandeid, HttpServletRequest request, HttpServletResponse response) throws Exception {
        Optional<DemandeHabilitation> habilitation = repository.findById(demandeid);

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

}
