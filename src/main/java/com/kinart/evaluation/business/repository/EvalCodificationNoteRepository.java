package com.kinart.evaluation.business.repository;

import com.kinart.evaluation.business.model.Evalcodificationnote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EvalCodificationNoteRepository extends JpaRepository<Evalcodificationnote, Integer> {

    @Query("select a from Evalcodificationnote a where code = :code")
    List<Evalcodificationnote> findByCode(@Param("code") String code);
}
