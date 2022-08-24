package com.kinart.api.gestiondepaie.dto;

import com.kinart.paie.business.model.SuspensionPaie;
import io.swagger.annotations.ApiModel;
import lombok.*;
import org.springframework.beans.BeanUtils;

import java.util.Date;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@ApiModel(description = "Model de gestion des suspensiins de paie")
public class SuspensionPaieDto {

    private Integer idEntreprise;
    private Integer id;
    private String nmat;
    private Date ddar;
    private Date dfar;
    private String mtar;
    private String nomSalarie;
    private String libmotif;
    private String mode;

    public static SuspensionPaieDto fromEntity(SuspensionPaie suspensionPaie) {
        if (suspensionPaie == null) {
            return null;
        }
        SuspensionPaieDto dto = new  SuspensionPaieDto();
        BeanUtils.copyProperties(suspensionPaie, dto);
        return dto;
    }

    public static SuspensionPaie toEntity(SuspensionPaieDto dto) {
        if (dto == null) {
            return null;
        }
        SuspensionPaie suspensionPaie = new SuspensionPaie();
        BeanUtils.copyProperties(dto, suspensionPaie);
        return suspensionPaie;
    }
}
