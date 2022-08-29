package com.kinart.api.evaluation.dto;

import io.swagger.annotations.ApiModel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.Date;

/**
 * @author c.mbassi
 */
@Data
@Builder
@AllArgsConstructor
@ApiModel(description = "Model de paramétrage des évaluations")
public class ParamGenerateEvaluationDto {
    private int identreprise;
    private String codeChamp;
    private String libelleChamp;
    private Date dtDebut;
    private Date dtFin;
}
