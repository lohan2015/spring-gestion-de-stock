package com.kinart.paie.business.repository;

import com.kinart.paie.business.model.PretExterneEntete;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface PretExterneEnteteRepository extends JpaRepository<PretExterneEntete, Integer> {

    @Transactional
    @Modifying
    @Query("delete from PretExterneEntete where nprt = :nprt")
    void deleteEntetePretByNumero(@Param("nprt") String nprt);

}