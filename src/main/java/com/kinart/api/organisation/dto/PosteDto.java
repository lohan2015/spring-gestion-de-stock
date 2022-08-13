package com.kinart.api.organisation.dto;

import com.kinart.api.gestiondepaie.dto.CalculPaieDto;
import com.kinart.api.gestiondestock.dto.AdresseDto;
import com.kinart.organisation.business.model.Orgposte;
import com.kinart.paie.business.model.CalculPaie;
import com.kinart.stock.business.model.AbstractEntity;
import com.kinart.stock.business.model.Adresse;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PosteDto {
    private Integer id;
    private Integer idEntreprise;
    private String codeposte;
    private String codeorganigramme;
    private String niv1;
    private String niv2;
    private String niv3;
    private String fonc;
    private String clas;
    private String cdfi;
    private String cdsp;
    private String tyfo;
    private String nifo;
    private Integer agmin;
    private Integer durmin;
    private Integer durmax;
    private BigDecimal espmin;
    private String hora;
    private String dispo;
    private String mobil;
    private String nuis;
    private String sexe;
    private String cat;
    private String ech;
    private String role;
    private String resp;
    private String ccout;
    private Date dtopo;
    private Date dtfpo;
    private Integer poids;
    private String site;
    private String lieutravail;
    private String libelle;
    private String codesite;
    private Blob fich;
    private String typmime;
    private String format;
    private String nomfich;
    private BigDecimal taille;
    private String red;
    private String ver;
    private String app;
    private String nomred;
    private String nomver;
    private String nomapp;
    private Date dtred;
    private Date dtver;
    private Date dtapp;
    private String interim;

    private String libellesite;
    private String libelleniv1;
    private String libelleniv2;
    private String libelleniv3;
    private String libelleorganigramme;
    private String libelleemploitype;
    private String libellecdfi;
    private String libellecdsp;
    private String libelletyfo;
    private String libellenifo;
    private String libellecat;
    private String libelleech;
    private String libelleclas;
    private String libellehora;
    private String libelledispo;
    private String libellemobil;
    private String libellenuis;
    private String libellesexe;
    private String codeADupliquer = null;
    private boolean etexist = false;
    private String perenplus1 = "N+1";
    private String perenplus2 = "N+2";
    private String peren = "N";
    private String libfoncredateur = null;
    private String libfoncverificateur = null;
    private String libfoncapobateur = null;

    List<PosteinfoDto> savoirs = new ArrayList<PosteinfoDto>();
    List<PosteinfoDto> savoirsEtres = new ArrayList<PosteinfoDto>();
    List<PosteinfoDto> savoirsFaires = new ArrayList<PosteinfoDto>();

    public static PosteDto fromEntity(Orgposte orgposte) {
        if (orgposte == null) {
            return null;
        }

        PosteDto dto = new  PosteDto();
        BeanUtils.copyProperties(orgposte, dto);
        return dto;
    }

    public static Orgposte toEntity(PosteDto dto) {
        if (dto == null) {
            return null;
        }

        Orgposte orgposte = new Orgposte();
        BeanUtils.copyProperties(dto, orgposte);
        return orgposte;
    }
}
