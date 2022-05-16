package com.kinart.paie.business.repository;

import com.kinart.paie.business.model.ParamData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParamDataRepository extends JpaRepository<ParamData, Integer> {

    @Query("select a from ParamData a where ctab = :ctab and nume=1")
    List<ParamData> findByCodeTable(@Param("ctab") Integer ctab);

    @Query("select a from ParamData a where ctab = :ctab and cacc = :cacc")
    List<ParamData> findByCle(@Param("ctab") Integer ctab, @Param("cacc") String cacc);

    @Query("select a from ParamData a where ctab = :ctab and cacc = :cacc and nume = :nume")
    Optional<ParamData> findByNumeroLigne(@Param("ctab") Integer ctab, @Param("cacc") String cacc, @Param("nume") Integer nume);
}
