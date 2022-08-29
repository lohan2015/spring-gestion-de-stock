package com.kinart.evaluation.business.service;

import com.kinart.api.evaluation.dto.ParamGenerateEvaluationDto;

import java.util.List;

public interface GenererEvaluationService {
    /**
     *  Génération des fiche d'évaluation
     * @param dto
     * @return
     */
    List<String> genererFicheEvaluation(ParamGenerateEvaluationDto dto);
}
