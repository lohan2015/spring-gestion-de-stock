package com.kinart.api.portail.dto;

import com.kinart.portail.business.model.NotifAbsConge;
import lombok.*;
import org.springframework.beans.BeanUtils;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @author Cyrille MBASSI
 * @since 04.07.23
 */

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NotifabscongeDto implements Serializable {

    private Integer id;

    private Date date_created;

    @NotNull(message = "L'email du sender ne doit pas etre vide")
    @NotEmpty(message = "L'email du sender ne doit pas etre vide")
    @NotBlank(message = "L'email du sender ne doit pas etre vide")
    @Email(message = "L'email du sender n'est conforme")
    private String sender;

    @NotNull(message = "L'email du recipient ne doit pas etre vide")
    @NotEmpty(message = "L'email du recipient ne doit pas etre vide")
    @NotBlank(message = "L'email du recipient ne doit pas etre vide")
    @Email(message = "L'email du recipient n'est conforme")
    private String recipient;

    @NotNull(message = "Le message ne doit pas etre vide")
    @NotEmpty(message = "Le message ne doit pas etre vide")
    @NotBlank(message = "Le message ne doit pas etre vide")
    private String message;

    public static NotifabscongeDto fromEntity(NotifAbsConge entity){
        NotifabscongeDto dto = new NotifabscongeDto();
        BeanUtils.copyProperties(entity, dto);

        return dto;
    }

    public static NotifAbsConge fromEntity(NotifabscongeDto dto){
        NotifAbsConge entity = new NotifAbsConge();
        BeanUtils.copyProperties(dto, entity);

        return entity;
    }
}
