package com.kinart.paie.business.repository;

import com.kinart.paie.business.model.PretExterneDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

public interface PretExterneDetailRepository extends JpaRepository<PretExterneDetail, Integer> {

    @Transactional
    @Modifying
    @Query("delete from PretExterneDetail where nprt = :nprt")
    void deleteDetailPretByNumero(@Param("nprt") String nprt);

    @Query("select a from PretExterneDetail a where nprt = :nprt")
    List<PretExterneDetail> findPretDetailByNumero(@Param("nprt") String nprt);
}