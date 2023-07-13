package com.kinart.portail.business.repository;

import com.kinart.portail.business.model.NotifModifInfo;
import com.kinart.portail.business.model.NotifPret;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author c.mbassi
 * 06.07.2023
 */
public interface NotifPretRepository extends JpaRepository<NotifPret, Integer> {

   List<NotifPret> findByRecipientOrderByCreationDateDesc(@Param("recipient") String recipient);

   List<NotifPret> findBySenderOrderByCreationDateDesc(@Param("sender") String sender);
}
