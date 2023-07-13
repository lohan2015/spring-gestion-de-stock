package com.kinart.portail.business.repository;

import com.kinart.portail.business.model.NotifAbsConge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author c.mbassi
 * 06.07.2023
 */
public interface NotifAbsCongeRepository extends JpaRepository<NotifAbsConge, Integer> {

   List<NotifAbsConge> findByRecipientOrderByCreationDateDesc(@Param("recipient") String recipient);

   List<NotifAbsConge> findBySenderOrderByCreationDateDesc(@Param("sender") String sender);
}
