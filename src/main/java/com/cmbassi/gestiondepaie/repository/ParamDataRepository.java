package com.cmbassi.gestiondepaie.repository;

import com.cmbassi.gestiondepaie.model.ParamData;
import com.cmbassi.gestiondepaie.model.Salarie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ParamDataRepository extends JpaRepository<ParamData, Integer> {

    @Query("select a from ParamData a where ctab = :ctab")
    List<ParamData> findByCodeTable(@Param("ctab") Integer ctab);

    @Query("select a from ParamData a where ctab = :ctab and cacc = :cacc")
    List<ParamData> findByCle(@Param("ctab") Integer ctab, @Param("cacc") String cacc);

    @Query("select a from ParamData a where ctab = :ctab and cacc = :cacc and nume = :nume")
    List<ParamData> findByNumeroLigne(@Param("ctab") Integer ctab, @Param("cacc") String cacc, @Param("nume") Integer nume);
}