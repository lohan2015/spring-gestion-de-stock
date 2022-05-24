package com.kinart.paie.business.repository;

import com.kinart.paie.business.model.HistoConnexion;
import com.kinart.paie.business.model.LogMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HistoConnexionRepository extends JpaRepository<HistoConnexion, Integer> {

    @Query("select a from HistoConnexion a where cuti = :user order by datc desc")
    List<HistoConnexion> findByUser(@Param("user") String user);

    @Query("select a from HistoConnexion a where cuti = :user and typeop like :optype order by datc desc")
    List<HistoConnexion> findByUserAndOperation(@Param("user") String user, @Param("optype") String optype);

    @Query("select a from HistoConnexion a where cuti = :user and typeop not like :optype order by datc desc")
    List<HistoConnexion> findByUserAndNoOperation(@Param("user") String user, @Param("optype") String optype);

    @Query("select a from HistoConnexion a where typeop like :optype order by datc desc")
    List<HistoConnexion> findByOperation(@Param("optype") String optype);

    @Query("select a from HistoConnexion a where typeop not like :optype order by datc desc")
    List<HistoConnexion> findByNoOperation(@Param("optype") String optype);
}
