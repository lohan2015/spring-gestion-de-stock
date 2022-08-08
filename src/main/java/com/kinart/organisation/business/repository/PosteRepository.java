package com.kinart.organisation.business.repository;

import com.kinart.organisation.business.model.Orgposte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PosteRepository extends JpaRepository<Orgposte, Integer> {

    @Query("select a from Orgposte a where codeposte = :codeemploitype")
    List<Orgposte> findEmploiTypeByCOde(@Param("codeemploitype") String codeemploitype);
}
