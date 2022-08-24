package com.kinart.paie.business.services;

import com.kinart.paie.business.model.CongeFictif;

import java.util.List;

public interface CongeFictifService {

    boolean save(CongeFictif dto);

    CongeFictif findById(Integer id);

    List<CongeFictif> findAll();

    List<CongeFictif> findByMatriculeAndPeriod(String nmat, String aamm, Integer nbul);

    void delete(Integer id);

}
