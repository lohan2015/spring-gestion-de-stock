package com.kinart.portail.business.repository;

import com.kinart.portail.business.model.DemandeAbsenceConge;
import com.kinart.portail.business.model.DemandeHabilitation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * @author c.mbassi
 * 06.07.2023
 */
public interface DemandeHabilitationRepository extends JpaRepository<DemandeHabilitation, Integer> {

   List<DemandeHabilitation> findByValid(@Param("valid") String valid);

    @Query("from DemandeHabilitation where valid=:emailadr")  // JPQL
    List<DemandeHabilitation> searchByEmail(@Param("emailadr") String email);

    @Query("from DemandeHabilitation where valid=:emailadr and status=:status ORDER BY creationDate DESC")  // JPQL
    List<DemandeHabilitation> searchByEmailAndStatus(@Param("emailadr") String email, @Param("status") String status);

    @Query(value = "select * from demandehabilitation d inner join utilisateur u where d.user_id = u.id and u.email=:email ORDER BY d.creationDate DESC", nativeQuery = true)  // SQL natif
    List<DemandeAbsenceConge> searchByUserEmail(@Param("email") String email);
}