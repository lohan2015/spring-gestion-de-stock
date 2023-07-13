package com.kinart.portail.business.repository;

import com.kinart.portail.business.model.NotifModifInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author c.mbassi
 * 06.07.2023
 */
public interface NotifModifInfoRepository extends JpaRepository<NotifModifInfo, Integer> {

   List<NotifModifInfo> findByRecipientOrderByCreationDateDesc(@Param("recipient") String recipient);

   List<NotifModifInfo> findBySenderOrderByCreationDateDesc(@Param("sender") String sender);
}
