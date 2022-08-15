package com.kinart.api.gestiondepaie.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.kinart.paie.business.model.ElementSalaire;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.BeanUtils;

import java.math.BigDecimal;
import java.util.List;


@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ElementSalaireLabelDto {

    // --------------------LIBELLE DES ZONES CHAMPS APPLICATION
    private String libellesexe;
    private String libellenat1;
    private String libellenat2;
    private String libellesit1;
    private String libellesit2;
    private String libellesit3;
    private String libellesit4;
    private String libelleniv11;
    private String libelleniv12;
    private String libelleniv13;
    private String libelleniv14;
    private String libelleniv21;
    private String libelleniv22;
    private String libelleniv23;
    private String libelleniv24;
    private String libelleniv31;
    private String libelleniv32;
    private String libelleniv33;
    private String libelleniv34;
    private String libellecs1;
    private String libellecs2;
    private String libellecs3;
    private String libellezca1;
    private String libellezca2;
    private String libellezca3;
    private String libellezca4;
    private String libelletyc1;
    private String libelletyc2;
    private String libelletyc3;
    private String libelletyc4;
    private String libelletyc5;
    private String libelletyc6;
    private String libelletyc7;
    private String libelletyc8;
    private String libellecat1;
    private String libellecat2;
    private String libelleavn;
    private String libellefon1;
    private String libellefon2;
    private String libellefon3;
    private String libellefon4;
    private String libellefon5;
    private String libellefon6;
    private String libellefon7;
    private String libellefon8;
    private String libellereg1;
    private String libellereg2;
    private String libellereg3;
    private String libellereg4;
    private String libellereg5;
    private String libellereg6;
    private String libellereg7;
    private String libellereg8;
    private String libelleclas1;
    private String libelleclas2;
    private String libelleclas3;
    private String libelleclas4;
    private String libellegra1;
    private String libellegra2;
    private String libellegra3;
    private String libellegra4;
    private String libellegra5;
    private String libellegra6;
    private String libellegra7;
    private String libellegra8;
    private String libellesynd;

}
