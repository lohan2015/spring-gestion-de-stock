package com.kinart.api.gestiondepaie.controller;

import com.kinart.api.gestiondepaie.controller.api.DossierPaieApi;
import com.kinart.api.gestiondepaie.dto.DossierPaieDto;
import com.kinart.paie.business.services.DossierPaieService;
import com.kinart.paie.business.services.utils.ClsDate;
import com.kinart.stock.business.exception.EntityNotFoundException;
import com.kinart.stock.business.exception.InvalidEntityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
public class DossierPaieController implements DossierPaieApi {

    private DossierPaieService dossierPaieService;

    @Autowired
    public DossierPaieController(DossierPaieService dossierPaieService) {
        this.dossierPaieService = dossierPaieService;
    }

    @Override
    public ResponseEntity<DossierPaieDto> save(DossierPaieDto dto) {
        try {
            dossierPaieService.save(dto);
        } catch (InvalidEntityException e){
            return new ResponseEntity(e.getErrors(), HttpStatus.BAD_REQUEST);
        }

        return new ResponseEntity(dto, HttpStatus.CREATED);
    }

    @Override
    public ResponseEntity<DossierPaieDto> findById(Integer id) {
        DossierPaieDto dossierPaieDto = dossierPaieService.findById(id);
        if(dossierPaieDto!=null) return ResponseEntity.ok(dossierPaieDto);
        return new ResponseEntity(null, HttpStatus.NO_CONTENT);
    }

    @Override
    public ResponseEntity<List<DossierPaieDto>> findAll() {
        List<DossierPaieDto> dossierPaieDto = dossierPaieService.findAll();
        if(dossierPaieDto!=null) {
            return ResponseEntity.ok(dossierPaieDto);
        } else {
            throw new EntityNotFoundException("Pas de dossiers trouvés");
        }
    }

    @Override
    public void delete(Integer id) {
        dossierPaieService.delete(id);
    }

    @Override
    public String getMoisDePaieCourant(Integer idEntreprise, String dateFormat) {
        Date ddmp = dossierPaieService.getMoisDePaieCourant(idEntreprise);
        if(ddmp!=null) return new ClsDate(new ClsDate(ddmp, dateFormat).addMonth(1)).getYearAndMonth();
        return null;
    }

    @Override
    public Integer getNumeroBulletinPaie(Integer idEntreprise) {
        return dossierPaieService.getNumeroBulletinPaie(idEntreprise);
    }
}
