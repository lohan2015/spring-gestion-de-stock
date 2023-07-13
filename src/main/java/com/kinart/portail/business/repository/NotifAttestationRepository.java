package com.kinart.portail.business.repository;

import com.kinart.portail.business.model.NotifAttestation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author c.mbassi
 * 06.07.2023
 */
public interface NotifAttestationRepository extends JpaRepository<NotifAttestation, Integer> {

   List<NotifAttestation> findByRecipientOrderByCreationDateDesc(@Param("recipient") String recipient);

   List<NotifAttestation> findBySenderOrderByCreationDateDesc(@Param("sender") String sender);
}
