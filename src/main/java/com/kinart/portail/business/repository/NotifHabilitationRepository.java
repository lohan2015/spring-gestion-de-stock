package com.kinart.portail.business.repository;

import com.kinart.portail.business.model.NotifHabilitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author c.mbassi
 * 06.07.2023
 */
public interface NotifHabilitationRepository extends JpaRepository<NotifHabilitation, Integer> {

   List<NotifHabilitation> findByRecipientOrderByCreationDateDesc(@Param("recipient") String recipient);

   List<NotifHabilitation> findBySenderOrderByCreationDateDesc(@Param("sender") String sender);
}
