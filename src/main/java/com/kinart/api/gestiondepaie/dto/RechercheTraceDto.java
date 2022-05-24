package com.kinart.api.gestiondepaie.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RechercheTraceDto {
    public String user = StringUtils.EMPTY;
    public String typeOperation = StringUtils.EMPTY;
}
