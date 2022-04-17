package com.kinart.paie.business.repository;

import com.kinart.paie.business.model.SequenceAuto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SequenceAutoRepository extends JpaRepository<SequenceAuto, Integer> {

    @Query("select a from SequenceAuto a where codecompteur = :codecompteur")
    SequenceAuto findByCodeSequence(@Param("codecompteur") String codecompteur);
}