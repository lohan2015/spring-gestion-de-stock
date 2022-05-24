package com.kinart.paie.business.repository;

import com.kinart.paie.business.model.LogMessage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LogMessageRepository extends JpaRepository<LogMessage, Integer> {

    @Query("select a from LogMessage a where cuti = :user order by datc desc")
    List<LogMessage>  findByUser(@Param("user") String user);
}
