package com.kinart.api.organisation.dto;

import com.kinart.organisation.business.model.Orgniveauemploitype;
import com.kinart.organisation.business.model.Orgposteinfo;
import com.kinart.stock.business.model.AbstractEntity;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/** 
 *  Compétences Métier ou Postes
 *     
*/
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PosteinfoDto {

    private Integer idEntreprise;
    private String codeposte;
    private String typeinfo;
    private String codeinfo1;
    private String codeinfo2;
    private String codeinfo3;
    private BigDecimal valminfo1;
    private String coeff;

    private String libelleinfo1;
    private String libelleinfo2;
    private String libelleinfo3;
    private String libelletypeinfo;

    public static PosteinfoDto fromEntity(Orgposteinfo orgniveau) {
        if (orgniveau == null) {
            return null;
        }

        PosteinfoDto dto = new  PosteinfoDto();
        BeanUtils.copyProperties(orgniveau, dto);
        return dto;
    }

    public static Orgposteinfo toEntity(PosteinfoDto dto) {
        if (dto == null) {
            return null;
        }

        Orgposteinfo orgniveau = new Orgposteinfo();
        BeanUtils.copyProperties(dto, orgniveau);
        return orgniveau;
    }

}
