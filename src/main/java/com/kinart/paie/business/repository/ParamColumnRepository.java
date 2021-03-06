package com.kinart.paie.business.repository;

import com.kinart.paie.business.model.ParamColumn;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ParamColumnRepository extends JpaRepository<ParamColumn, Integer> {

    @Query("select a from ParamColumn a where id = :id")
    Optional<ParamColumn> findById(@Param("id") Integer id);

    @Query("select a from ParamColumn a where ctab = :ctab")
    List<ParamColumn> findByCodeTable(@Param("ctab") Integer ctab);
}