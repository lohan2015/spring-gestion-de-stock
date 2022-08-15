package com.kinart.api.organisation.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OccupationPosteDto {
    private String matricule;
    private String nom;
    private String prenom;
    private Date debut;
    private Date fin;
}
