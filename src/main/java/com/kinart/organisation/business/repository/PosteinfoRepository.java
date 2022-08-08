package com.kinart.organisation.business.repository;

import com.kinart.organisation.business.model.Orgposte;
import com.kinart.organisation.business.model.Orgposteinfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PosteinfoRepository extends JpaRepository<Orgposteinfo, Integer> {

    @Query("Delete from Orgposteinfo a where codeposte = :codeposte")
    void deleteInfoByCodeposte(@Param("codeposte") String codeposte);
}
